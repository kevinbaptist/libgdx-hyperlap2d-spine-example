package com.kbaptista.example;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kbaptista.example.character.AnimationSystem;
import com.kbaptista.example.character.CharacterComponent;
import com.kbaptista.example.character.CharacterSystem;
import com.kbaptista.example.listener.WorldContactListener;
import games.rednblack.editor.renderer.SceneLoader;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.utils.ItemWrapper;
import games.rednblack.h2d.extention.spine.SpineItemType;

import static com.kbaptista.example.utils.Mappers.physicsBodyComponentMapper;

public class Core extends ApplicationAdapter {
	private SceneLoader sceneLoader;
	private OrthographicCamera camera;
	private Viewport viewport;

	private Box2DDebugRenderer b2dr; //only purpose is to see 2d bodies created

	private PhysicsBodyComponent player;

	@Override
	public void create() {
		sceneLoader = new SceneLoader();
		sceneLoader.injectExternalItemType(new SpineItemType());//Where the Magic Happens Between Spine and Scene loaded

		sceneLoader.getEngine().addSystem(new CharacterSystem(sceneLoader.getWorld()));
		sceneLoader.getEngine().addSystem(new AnimationSystem());

		camera = new OrthographicCamera();
		viewport = new ExtendViewport(15, 10, camera);
		b2dr = new Box2DDebugRenderer();

		sceneLoader.loadScene("MainScene", viewport);
		loadCharacter("Character");

		sceneLoader.getWorld().setContactListener(new WorldContactListener());
	}

	/**
	 * Injecting our component to identify the Character
	 *
	 * @param identifier given in HyperLap2D editor.
	 */
	private void loadCharacter(String identifier) {
		ItemWrapper root = new ItemWrapper(sceneLoader.getRoot());
		Entity character = root.getChild(identifier).getEntity();
		player = physicsBodyComponentMapper.get(character);
		character.add(new CharacterComponent());
	}

	@Override
	public void render() {
		this.clearRender();
		this.update();
		this.centerCamera();
	}

	private void update() {
		sceneLoader.getEngine().update(Gdx.graphics.getDeltaTime());
		b2dr.render(sceneLoader.getWorld(), camera.combined);
	}

	private void clearRender() {
		Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	private void centerCamera() {
		camera.position.x = player.body.getPosition().x;
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void dispose() {
		//sceneLoader.dispose(); TODO: not available yet on version 0.0.3, thus disposing them individually
		sceneLoader.getRayHandler().dispose();
		sceneLoader.getBatch().dispose();
		sceneLoader.getWorld().dispose();
		b2dr.dispose();
	}
}
