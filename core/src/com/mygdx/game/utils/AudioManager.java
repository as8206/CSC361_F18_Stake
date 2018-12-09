package com.mygdx.game.utils;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Handles playing all audio for the game
 *
 */
public class AudioManager
{
	public static final AudioManager instance = new AudioManager();
	
	private Music playingMusic;
	
	// singleton: prevent instantiation from other classes
	private AudioManager() { }
	
	/**
	 * Will play a supplied sound. Applies default values to missing parameters
	 * by method overloading, adds volume default of 1.
	 * @param sound
	 */
	public void play(Sound sound)
	{
		play(sound, 1);
	}
	
	/**
	 * Will play a supplied sound. Applies default values to missing parameters
	 * by method overloading, adds pitch default of 1.
	 * @param sound
	 * @param volume
	 */
	public void play(Sound sound, float volume)
	{
		play(sound, volume, 1);
	}
	
	/**
	 * Will play a supplied sound. Applies default values to missing parameters
	 * by method overloading, adds pan default of 1.
	 * @param sound
	 * @param volume
	 * @param pitch
	 */
	public void play(Sound sound, float volume, float pitch)
	{
		play(sound, volume, pitch, 0);
	}
	
	/**
	 * Plays a supplied sound with the given volume, pitch, and pan after checking if game
	 * sound is turned on.
	 * @param sound
	 * @param volume
	 * @param pitch
	 * @param pan
	 */
	public void play(Sound sound, float volume, float pitch, float pan)
	{
		if(!GamePreferences.instance.sound)
			return;
		sound.play(GamePreferences.instance.volSound * volume, pitch, pan);
	}
	
	/**
	 * If music is turned on, the supplied music is playing and looped
	 * @param music
	 */
	public void play(Music music)
	{
		stopMusic();
		playingMusic = music;
		if(GamePreferences.instance.music)
		{
			music.setLooping(true);
			music.setVolume(GamePreferences.instance.volMusic);
			music.play();
		}
	}
	
	/**
	 * If music is turned on, the supplied music is playing and without looping
	 * @param music
	 */
	public void playNoLoop(Music music)
	{
		stopMusic();
		playingMusic = music;
		if(GamePreferences.instance.music)
		{
			music.setLooping(false);
			music.setVolume(GamePreferences.instance.volMusic);
			music.play();
		}
		
	}
	
	/**
	 * Stops the currently playing music
	 */
	public void stopMusic()
	{
		if(playingMusic != null)
			playingMusic.stop();
	}
	
	/**
	 * When settings are updated, checks settings and plays or doesn't
	 * play music accordingly.
	 */
	public void onSettingsUpdated()
	{
		if(playingMusic == null)
			return;
		playingMusic.setVolume(GamePreferences.instance.volMusic);
		if(GamePreferences.instance.music)
		{
			if(!playingMusic.isPlaying())
				playingMusic.play();
		}
		else
		{
			playingMusic.pause();
		}
	}

	public boolean isMusicPlaying() 
	{
		return playingMusic.isPlaying();
	}
}
