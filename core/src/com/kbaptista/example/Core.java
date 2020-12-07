package com.kbaptista.example;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import games.rednblack.editor.renderer.SceneLoader;
import games.rednblack.editor.renderer.utils.ItemWrapper;
import games.rednblack.h2d.extention.spine.SpineItemType;

public class Core extends ApplicationAdapter {
	private SceneLoader sceneLoader;
	private OrthographicCamera camera;
	private Viewport viewport;

	private Box2DDebugRenderer b2dr; //only purpose is to see 2d bodies created

	@Override
	public void create () {
		sceneLoader = new SceneLoader();
		sceneLoader.injectExternalItemType(new SpineItemType());//Where the Magic Happens Between Spine and Scene loaded
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(640, 640,  camera);
		b2dr = new Box2DDebugRenderer();

		sceneLoader.loadScene("MainScene", viewport);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sceneLoader.getEngine().update(Gdx.graphics.getDeltaTime());
		b2dr.render(sceneLoader.getWorld(), camera.combined);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void dispose () {
		//sceneLoader.dispose(); TODO: not available yet on version 0.0.3, thus disposing them individually
		sceneLoader.getRayHandler().dispose();
		sceneLoader.getBatch().dispose();
		sceneLoader.getWorld().dispose();
		b2dr.dispose();
	}
}
