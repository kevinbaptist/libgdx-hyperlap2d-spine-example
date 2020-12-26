package com.kbaptista.example.character;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.h2d.extention.spine.SpineObjectComponent;

import static com.kbaptista.example.utils.Mappers.characterComponentMapper;
import static com.kbaptista.example.utils.Mappers.physicsBodyComponentMapper;
import static com.kbaptista.example.utils.Mappers.spineObjectComponentMapper;

public class AnimationSystem extends IteratingSystem {
	private AnimationState currentAnimation;

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
		PhysicsBodyComponent physicsBodyComponent = physicsBodyComponentMapper.get(entity);
		CharacterComponent characterComponent = characterComponentMapper.get(entity);
		AnimationState animationState;
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			animationState = AnimationState.JUMP;
		} else if (isAnyMovingKeyPressed()) {
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				spineObjectComponent.skeleton.setScaleX(-1);
			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				spineObjectComponent.skeleton.setScaleX(1);
			}

			if (characterComponent.jumps != 1) {
				animationState = AnimationState.JUMP;
			} else {
				animationState = AnimationState.RUN;
			}
		} else {
			Vector2 velocity = physicsBodyComponent.body.getLinearVelocity();
			if (characterComponent.jumps != 1) {
				animationState = AnimationState.JUMP;

			} else if (velocity.x * velocity.x > 1) {
				animationState = AnimationState.RUN_TO_IDLE;
			} else {
				animationState = AnimationState.IDLE;
			}
		}
		return animationState;
	}

	private boolean isAnyMovingKeyPressed() {
		return Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
	}
}
