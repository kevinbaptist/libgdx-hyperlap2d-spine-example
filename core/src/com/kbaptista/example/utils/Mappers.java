package com.kbaptista.example.utils;

import com.badlogic.ashley.core.ComponentMapper;
import com.kbaptista.example.character.CharacterComponent;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.h2d.extention.spine.SpineObjectComponent;

public class Mappers {
	public static final ComponentMapper<CharacterComponent> characterComponentMapper = ComponentMapper.getFor(CharacterComponent.class);
	public static final ComponentMapper<PhysicsBodyComponent> physicsBodyComponentMapper = ComponentMapper.getFor(PhysicsBodyComponent.class);
	public static final ComponentMapper<SpineObjectComponent> spineObjectComponentMapper = ComponentMapper.getFor(SpineObjectComponent.class);
}
