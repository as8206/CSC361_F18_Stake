package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.screens.MenuScreen;
import com.mygdx.game.utils.GamePreferences;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.assets.AssetManager;

public class AmarethMain extends Game
{
	@Override
	public void create()
	{
		// Set Libgdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		// Load assets
		Assets.instance.init(new AssetManager());
		
		//Load preferences
		GamePreferences.instance.load();
		
		// Start game at menu screen
		setScreen(new MenuScreen(this));
	}
}
