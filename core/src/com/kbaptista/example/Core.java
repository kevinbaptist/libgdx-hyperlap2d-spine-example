package com.kbaptista.example;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import games.rednblack.editor.renderer.SceneLoader;

public class Core extends ApplicationAdapter {
	private SceneLoader sceneLoader;
	private OrthographicCamera camera;
	private Viewport viewport;

	@Override
	public void create () {
		sceneLoader = new SceneLoader();
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(640, 640,  camera);
		sceneLoader.loadScene("MainScene", viewport);
	}

	@Override
	public void render () {
		camera.update();

		Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		viewport.apply();
		sceneLoader.getEngine().update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void dispose () {
		//sceneLoader.dispose(); TODO: not available yet on version 0.0.3, thus disposing them individually
		sceneLoader.getRayHandler().dispose();
		sceneLoader.getBatch().dispose();
		sceneLoader.getWorld().dispose();
	}
}
