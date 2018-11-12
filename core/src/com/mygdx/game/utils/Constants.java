package com.mygdx.game.utils;

public class Constants 
{

	// Visible game world is 5 meters wide
	public static final float VIEWPORT_WIDTH = 5.0f;
	
	// Visible game world is 5 meters tall
	public static final float VIEWPORT_HEIGHT = 5.0f;

	//Location of description file for the texture atlas
	public static final String TEXTURE_ATLAS_OBJECTS =  "../core/assets/amareth.atlas";
	
	//Location of level 1
	public static final String LEVEL_01 = "../core/assets/levels/level01.png";
	
	//Location of level 2
	public static final String LEVEL_02 = "../core/assets/levels/level011.png";
	
	//offset of render vs collision box
	public static final float OFFSET = 0.5f;
	
	//radius of interaction circle for interact-able objects
	public static final float INTERRAD = 0.9f; //TODO radius value needs testing
}