package com.kbaptista.example.character;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.kbaptista.example.listener.PlayerInputHandler;
import com.kbaptista.example.utils.Mappers;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;

import static com.kbaptista.example.config.Config.PLAYER_BIT;

public class CharacterSystem extends IteratingSystem implements EntityListener {

	private final int MAX_VELOCITY = 4;
	private final Vector2 RIGHT_MOVEMENT = new Vector2(2, 0);
	private final Vector2 LEFT_MOVEMENT = new Vector2(-2, 0);
	private final Vector2 UP_MOVEMENT = new Vector2(0, 6);

	private final World world;
	private CharacterComponent characterComponent;
	private final PlayerInputHandler inputProcessor;

	public CharacterSystem(World world) {
		super(Family.all(CharacterComponent.class, PhysicsBodyComponent.class).get());
		this.world = world;
		inputProcessor = new PlayerInputHandler();
		Gdx.input.setInputProcessor(inputProcessor);
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		engine.addEntityListener(Family.all(CharacterComponent.class, PhysicsBodyComponent.class).get(), this);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Body playerBody = Mappers.physicsBodyComponentMapper.get(entity).body;
		if (characterComponent.isMovingRight) {
			moveRightIfPossible(playerBody);
		} else if (characterComponent.isMovingLeft) {
			moveLeftIfPossible(playerBody);
		}
		if (characterComponent.isMovingUp) {
			moveUpIfPossible(playerBody);
		}
		if (characterComponent.isHoverboard) {
			playerBody.applyForce(new Vector2(0, 10), playerBody.getWorldCenter(), true);
		}
	}

	private void moveUpIfPossible(Body playerBody) {
		if (characterComponent.canJump()) {
			characterComponent.jump();
			applyMovement(UP_MOVEMENT, playerBody);
		}
	}

	private void moveRightIfPossible(Body playerBody) {
		if (playerBody.getLinearVelocity().x <= MAX_VELOCITY)
			applyMovement(RIGHT_MOVEMENT, playerBody);
	}

	private void moveLeftIfPossible(Body playerBody) {
		if (playerBody.getLinearVelocity().x >= -MAX_VELOCITY)
			applyMovement(LEFT_MOVEMENT, playerBody);
	}

	private void applyMovement(Vector2 direction, Body playerBody) {
		playerBody.applyLinearImpulse(direction, playerBody.getWorldCenter(), true);
	}


	@Override
	public void entityAdded(Entity entity) {
		characterComponent = Mappers.characterComponentMapper.get(entity);
		inputProcessor.setCharacterComponent(characterComponent);
		//TODO: Give more meaning to those numbers
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(5, 5);
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		entity.getComponent(PhysicsBodyComponent.class).body = world.createBody(bodyDef);

		FixtureDef fixtureDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(1);
		shape.setPosition(new Vector2(1f, 1f));
		fixtureDef.shape = shape;
		fixtureDef.filter.categoryBits = PLAYER_BIT;
		entity.getComponent(PhysicsBodyComponent.class).body
				.createFixture(fixtureDef)
				.setUserData(entity);
		entity.getComponent(PhysicsBodyComponent.class).body.createFixture(defineHead(fixtureDef));
	}

	private FixtureDef defineHead(FixtureDef fixtureDef) {
		//TODO: Give more meaning to those numbers
		CircleShape head = new CircleShape();
		head.setRadius(0.5f);
		head.setPosition(new Vector2(1f, 2.5f));
		fixtureDef.shape = head;
		return fixtureDef;
	}

	@Override
	public void entityRemoved(Entity entity) {

	}
}
