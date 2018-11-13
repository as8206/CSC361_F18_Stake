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
	public static final String STARTROOM = "../core/assets/levels/start/testingRoom.png";
	
	//Location of level 2
	public static final String LEVEL_02 = "../core/assets/levels/temp/level02.png";
	
	//Location of room png's
	public static final String ROOMFILES = "../core/assets/levels/temp"; //TODO break into various door entry point folders
	
	//offset of render vs collision box
	public static final float OFFSET = 0.5f;
	
	//radius of interaction circle for interact-able objects
	public static final float INTERRAD = 0.9f; //TODO radius value needs testing
	
	//maximum size a room pixmap will be drawn is 23 x 23
	//room offset value of 25 gives 2 meters or extra spacing between rooms
	public static final int ROOMOFFSET = 25;
	
	//max amount of rooms that can be on a floor, n X n grid
	public static final int MAXROOMS = 51;
}