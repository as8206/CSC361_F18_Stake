package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.objects.AbstractGameObject;
import com.mygdx.game.objects.Ladder;
import com.mygdx.game.utils.Constants;

public class WorldRenderer implements Disposable
{
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private WorldController worldController;
	
	private Box2DDebugRenderer b2debugRenderer;
	private boolean debug = true;
	
	public WorldRenderer (WorldController wc)
	{
		worldController = wc;
		init();
	}
	
	private void init ()
	{
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		
		b2debugRenderer = new Box2DDebugRenderer();
	}
	
	public void render ()
	{
		renderLevel(); //TODO add level parameter to load a specified level
	}
	
	/**
	 * Renders the level and all objects within it
	 */
	private void renderLevel()
	{
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.activeRoom.render(batch);
		batch.end();
		
		if(debug)
			b2debugRenderer.render(WorldController.b2dWorld, camera.combined);
	}

	
	public void resize (int width, int height)
	{
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT/height) * width;
		camera.update();
	}
	
	@Override public void dispose()
	{
		batch.dispose();
	}
	
}
