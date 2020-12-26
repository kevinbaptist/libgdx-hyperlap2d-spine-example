package com.kbaptista.example.listener;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.kbaptista.example.character.CharacterComponent;

import static com.kbaptista.example.config.Config.GROUND_BIT;
import static com.kbaptista.example.config.Config.PLAYER_BIT;

public class WorldContactListener implements ContactListener {
	@Override
	public void beginContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();

		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
		switch (cDef) {
			case PLAYER_BIT | GROUND_BIT:
				if (fixA.getFilterData().categoryBits == PLAYER_BIT) {
					((Entity) fixA.getUserData()).getComponent(CharacterComponent.class).reset();
				} else {
					((Entity) fixB.getUserData()).getComponent(CharacterComponent.class).reset();
				}
				break;
		}
	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
}
