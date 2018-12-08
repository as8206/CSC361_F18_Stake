package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.Assets;
import com.mygdx.game.WorldController;
import com.mygdx.game.WorldRenderer;
import com.mygdx.game.utils.AudioManager;

public class GameScreen extends AbstractGameScreen
{
	private static final String TAG = GameScreen.class.getName();
	
	/**
	 * Allows us to control the game's state
	 */
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	
	private boolean paused;
	private int nextSong;
	
	public GameScreen(Game game)
	{
		super(game);
		nextSong = 1;
	}
	
	/**
	 * Determines when to update the game world and how to do so. 
	 * @param deltaTime
	 */
	@Override
	public void render(float deltaTime)
	{
		//checks and updates music
		if(AudioManager.instance.isMusicPlaying() == false)
		{
			if(nextSong == 1)
				AudioManager.instance.playNoLoop(Assets.instance.music.dungeonLoop1);
			else if (nextSong == 2)
				AudioManager.instance.playNoLoop(Assets.instance.music.dungeonLoop2);
			
			nextSong++;
			if(nextSong > 2)
				nextSong = 1;
				
		}
		//Do not update the game world when paused
		if (!paused)
			//Update the game world by the time that has passed since last rendered frame
			worldController.update(deltaTime);
		
		//sets the clear screen color to cornflower blue
		Gdx.gl.glClearColor(0,  0,  0, 1);
		
		//clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//render game world to screen
		worldRenderer.render();
	}
	
	/**
	 * resizes the window of the game to the dimensions given
	 * @param width
	 * @param height
	 */
	@Override
	public void resize(int width, int height)
	{
		worldRenderer.resize(width, height);
	}
	
	/**
	 * create and display the world to the player
	 */
	@Override
	public void show()
	{
		//start music
		AudioManager.instance.playNoLoop(Assets.instance.music.dungeonLoop1);
		
		worldController = new WorldController(game);
		worldRenderer = new WorldRenderer(worldController);
		worldController.setWorldRenderer(worldRenderer);
		Gdx.input.setCatchBackKey(true);
	}
	
	/**
	 * what we do if the game isn't the current active window
	 */
	@Override
	public void hide()
	{
		//stops music
		AudioManager.instance.stopMusic();
		
		worldRenderer.dispose();
		Gdx.input.setCatchBackKey(false);
	}
	
	
	/**
	 * pause the game
	 */
	@Override
	public void pause()
	{
		paused = true;
	}
	
	/**
	 * resume the game
	 */
	@Override
	public void resume()
	{
		super.resume();
		paused = false;
	}
}
