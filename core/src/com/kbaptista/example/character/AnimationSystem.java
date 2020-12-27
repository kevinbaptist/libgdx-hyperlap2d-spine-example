package com.kbaptista.example.character;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.h2d.extention.spine.SpineObjectComponent;

import static com.kbaptista.example.character.CharacterComponent.MAX_JUMPS;
import static com.kbaptista.example.utils.Mappers.*;

public class AnimationSystem extends IteratingSystem implements EntityListener {
	private AnimationState currentAnimation;

	public final static int LOOKING_LEFT = -1;
	public final static int LOOKING_RIGHT = 1;

	public AnimationSystem() {
		super(Family.all(CharacterComponent.class, SpineObjectComponent.class, PhysicsBodyComponent.class).get());
	}


	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		SpineObjectComponent spineObjectComponent = spineObjectComponentMapper.get(entity);
		AnimationState animationState = getNextAnimationState(entity, spineObjectComponent);

		if (currentAnimation != animationState) {
			currentAnimation = animationState;
			spineObjectComponent.setAnimation(animationState.getAction());
		}

	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		engine.addEntityListener(Family.all(CharacterComponent.class, SpineObjectComponent.class, PhysicsBodyComponent.class).get(), this);
	}

	private AnimationState getNextAnimationState(Entity entity, SpineObjectComponent spineObjectComponent) {
		AnimationState animationState;
		CharacterComponent characterComponent = characterComponentMapper.get(entity);
		if (characterComponent.isLocked) {
			animationState = AnimationState.PORTAL;
		} else if (isInAir(entity)) {
			animationState = AnimationState.JUMP;
		} else if (isAnyMovingKeyPressed()) {
			spineObjectComponent.skeleton.setScaleX(getLookingDirection(characterComponent)); // change direction
			animationState = AnimationState.RUN;
		} else {
			Vector2 velocity = physicsBodyComponentMapper.get(entity).body.getLinearVelocity();
			if (velocity.x * velocity.x > 2) {
				animationState = AnimationState.RUN_TO_IDLE;
			} else {
				animationState = AnimationState.IDLE;
			}
		}
		return animationState;
	}

	private boolean isInAir(Entity entity) {
		return isJumping(entity);
	}

	private float getLookingDirection(CharacterComponent characterComponent) {
		if (characterComponent.isMovingRight) {
			return LOOKING_RIGHT;
		} else {
			return LOOKING_LEFT;
		}
	}

	private boolean isJumping(Entity entity) {
		return characterComponentMapper.get(entity).jumps != MAX_JUMPS;
	}

	private boolean isAnyMovingKeyPressed() {
		return Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
	}

	@Override
	public void entityAdded(final Entity entity) {
		SpineObjectComponent spineObjectComponent = spineObjectComponentMapper.get(entity);
		spineObjectComponent.getState().setAnimation(0, AnimationState.PORTAL.getAction(), false);
		spineObjectComponent.getState().addListener(new com.esotericsoftware.spine.AnimationState.AnimationStateAdapter() {
			public void complete(com.esotericsoftware.spine.AnimationState.TrackEntry entry) {
				characterComponentMapper.get(entity).isLocked = false;
			}
		});
	}

	@Override
	public void entityRemoved(Entity entity) {

	}
}
