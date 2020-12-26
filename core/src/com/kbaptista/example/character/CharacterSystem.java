package com.kbaptista.example.character;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;

import static com.kbaptista.example.config.Config.PLAYER_BIT;

public class CharacterSystem extends IteratingSystem implements EntityListener  {
	private final Vector2 RIGHT_MOVEMENT;
	private final Vector2 LEFT_MOVEMENT;
	private final World world;

	CharacterComponent characterComponent;

	public CharacterSystem(World world) {
		super(Family.all(CharacterComponent.class, PhysicsBodyComponent.class).get());
		this.world = world;
		RIGHT_MOVEMENT = new Vector2(2, 0);
		LEFT_MOVEMENT = new Vector2(-2, 0);
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);
		engine.addEntityListener(Family.all(CharacterComponent.class, PhysicsBodyComponent.class).get(), this);
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		handleKeyboardInput(entity.getComponent(PhysicsBodyComponent.class).body);
	}

	private void handleKeyboardInput(Body body) {
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && body.getLinearVelocity().x <= 4) {
			body.applyLinearImpulse(RIGHT_MOVEMENT, body.getWorldCenter(), true);
		} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) &&  body.getLinearVelocity().x >= -4) {
			body.applyLinearImpulse(LEFT_MOVEMENT, body.getWorldCenter(), true);
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && characterComponent.jumps > 0) {
			characterComponent.jumps--;
			body.applyLinearImpulse(new Vector2(0, 6f), body.getWorldCenter(), true);
		}
//		if (!body.getLinearVelocity().isZero())
//			Gdx.app.log("Linear velocity", body.getLinearVelocity().x + ", " + body.getLinearVelocity().y);
	}

	@Override
	public void entityAdded(Entity entity) {
		characterComponent = entity.getComponent(CharacterComponent.class);
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
