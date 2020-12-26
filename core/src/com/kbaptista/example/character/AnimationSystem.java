package com.kbaptista.example.character;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.h2d.extention.spine.SpineObjectComponent;

import static com.kbaptista.example.character.CharacterComponent.MAX_JUMPS;
import static com.kbaptista.example.utils.Mappers.*;

public class AnimationSystem extends IteratingSystem {
	private AnimationState currentAnimation;

	public final static int LOOKING_LEFT = -1;
	public final static int LOOKING_RIGHT = 1;

	public AnimationSystem() {
		super(Family.all(CharacterComponent.class, SpineObjectComponent.class, PhysicsBodyComponent.class).get());
		currentAnimation = AnimationState.RUN;
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

	private AnimationState getNextAnimationState(Entity entity, SpineObjectComponent spineObjectComponent) {

		AnimationState animationState;
		if (Gdx.input.isKeyPressed(Input.Keys.UP) || isJumping(entity)) {
			animationState = AnimationState.JUMP;
		} else if (isAnyMovingKeyPressed()) {
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				spineObjectComponent.skeleton.setScaleX(-1);
			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				spineObjectComponent.skeleton.setScaleX(1);
			}
			animationState = AnimationState.RUN;
		} else {
			Vector2 velocity = physicsBodyComponentMapper.get(entity).body.getLinearVelocity();
			Gdx.app.log("ANimation", physicsBodyComponentMapper.get(entity).body.getMass() + "");
			if (velocity.x * velocity.x > 1) {
				animationState = AnimationState.RUN_TO_IDLE;
			} else {
				animationState = AnimationState.IDLE;
			}
		}
		return animationState;
	}

	private boolean isJumping(Entity entity) {
		return characterComponentMapper.get(entity).jumps != MAX_JUMPS;
	}

	private boolean isAnyMovingKeyPressed() {
		return Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
	}
}
