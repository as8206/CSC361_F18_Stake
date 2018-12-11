//Aaron Gerber
package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class ScoreList
{
	public static final String TAG = ScoreList.class.getName();
	
	//Singleton of game preferences
	public static final ScoreList instance = new ScoreList();
	
	//Amount of top scores that are saved
	public static final int MAXSCORES = 10;
	
	//Sets up arrays to hold the highscores
	public int[] scores;
	public String[] names;

	private Preferences list;
	
	//Initialization of the singleton
	private ScoreList()
	{
		list = Gdx.app.getPreferences(Constants.SCORELIST);
		scores = new int[MAXSCORES];
		names = new String[MAXSCORES];
	}
	
	public void load()
	{
		for(int i = 0; i < MAXSCORES; i++)
		{
			scores[i] = list.getInteger("score" + i, 0);
			names[i] = list.getString("name" + i, "");
		}
		
	}
	
	public void save ()
	{
		for(int i = 0; i < MAXSCORES; i++)
		{
			list.putInteger("score" + i, scores[i]);
			list.putString("name" + i, names[i]);
		}
		
		list.flush();	
	}
	
	/**
	 * Checks if the score is high enough to be added to the list 
	 * and returns a boolean
	 * @param testScore
	 * @return
	 */
	public boolean checkScore(int testScore)
	{
		return false;
	}
	
	/**
	 * Inserts the score to the score list
	 * rechecks that the score is high enough
	 * @param newScore
	 * @param newName
	 */
	public void insertScore(int newScore, String newName)
	{
		
	}
}
