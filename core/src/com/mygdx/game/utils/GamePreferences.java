//Aaron Gerber
package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class GamePreferences
{
	public static final String TAG = GamePreferences.class.getName();
	
	//Singleton of game preferences
	public static final GamePreferences instance = new GamePreferences();
	
	//Set up the various settings players can alter
	public boolean sound;
	public boolean music;
	public float volSound;
	public float volMusic;
	public boolean showFpsCounter;

	private Preferences prefs;
	
	//Initialization of the singleton
	private GamePreferences()
	{
		prefs = Gdx.app.getPreferences(Constants.SETTINGS);
	}
	
	public void load()
	{
		sound = prefs.getBoolean("sound",true);
		music = prefs.getBoolean("music", true);
		volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f), 0.0f, 1.0f);
		volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.3f), 0.0f, 0.6f);
		showFpsCounter = prefs.getBoolean("showFpsCounter", false);
	}
	
	public void save ()
	{
		prefs.putBoolean("sound", sound);
		prefs.putBoolean("music", music);
		prefs.putFloat("volSound", volSound);
		prefs.putFloat("volMusic", volMusic);
		prefs.putBoolean("showFpsCounter", showFpsCounter);
		prefs.flush();	
	}
}
