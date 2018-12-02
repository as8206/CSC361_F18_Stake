package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/**
 * Handles the creation of the main menu screen for the game
 */
public class MenuScreen extends AbstractGameScreen
{
	private static final String TAG = MenuScreen.class.getName();
	
	public MenuScreen (Game game)
	{
		super(game);
	}
	
	/**
	 * Draws the menu screen
	 * @param deltaTime
	 */
	@Override
	public void render (float deltaTime)
	{
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(Gdx.input.isTouched())
			game.setScreen(new GameScreen(game));
	}
	
	@Override public void resize(int width, int heigth) {}
	@Override public void show() {}
	@Override public void hide() {}
	@Override public void pause() {}
}
