package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;

public class Constants 
{

	// Visible game world is 5 meters wide
	public static final float VIEWPORT_WIDTH = 5.0f;
	
	// Visible game world is 5 meters tall
	public static final float VIEWPORT_HEIGHT = 5.0f;
	
	// GUI Width
	public static final float VIEWPORT_UI_WIDTH = 1600.0f;
	// GUI Height
	public static final float VIEWPORT_UI_HEIGHT = 960.0f;

	//Location of description file for the texture atlas
	public static final String TEXTURE_ATLAS_OBJECTS =  "../core/assets/amareth.atlas";
	
	//Location of the amareth json file
	public static final String SKIN_AMARETH_UI = "../core/assets/ui/amarethUI.json";
	
	//Location of the description file for the ui atlas
	public static final String TEXTURE_ATLAS_UI = "../core/assets/ui/amareth-ui.atlas";
	
	//Location of the UI json file
	public static final String SKIN_LIBGDX_UI = "../core/assets/ui/uiskin.json";
	
	//Location of level 1
	public static final String STARTROOM = "../core/assets/levels/start/testingRoom.png"; //TODO change to real spawn room
	
	//Location of level 2
	public static final String LEVEL_02 = "../core/assets/levels/temp/level02.png";
	
	//Location of room png's
	public static final String ROOMFILES = "../core/assets/levels/rooms"; //TODO break into various door entry point folders
	
	//Location of looped rooms
	public static final String LOOPROOMS = "../core/assets/levels/loopedRooms";
	
	//offset of render vs collision box
	public static final float OFFSET = 0.5f;
	
	//radius of interaction circle for interact-able objects
	public static final float INTERRAD = 0.9f; //TODO radius value needs testing
	
	//maximum size a room pixmap will be drawn is 23 x 23
	//room offset value of 25 gives 2 meters or extra spacing between rooms
	public static final int ROOMOFFSET = 25;
	
	//max amount of rooms that can be on a floor, n X n grid
	public static final int MAXROOMS = 51;

	//Base score from a single coin pickup
	public static final int BASECOINSCORE = 10;

	//Base starting health for the player
	public static final float STARTINGHEALTH = 100;
	
	//Base attack damage min
	public static final float ATTACKMIN = 8;
	
	//Base attack damage max
	public static final float ATTACKMAX = 12;
	
	//Base attack velocity
	public static final float ATTACKSPEED = 5;
	
	//Base attack sensor body radius
	public static final float ATTACKSIZE = .25f;
	
	//cooldown time for attack 1, in seconds
	public static final float COOLDOWN = .3f;
	
	//Holds the game settings
	public static final String SETTINGS = "gameSettings.txt";
	
	//Holds the highscore list
	public static final String SCORELIST = "scorelist.txt";

	//Center screen locations
	public static final float CENTERX = 800;
	public static final float CENTERY = 480;
	
	//Enemy Base health
	public static final float ENEMYHEALTH = 30;
	
	//Enemy Base Melee damage and cooldown
	public static final float MELEEDAMAGE = 15;
	public static final float MELEECOOLDOWN = 1;
	
	//Enemy Base Ranged damage, range, speed, and cooldown
	public static final float RANGEDDAMAGE = 8;
	public static final float RANGEDRANGE = 5;
	public static final float RANGEDCOOLDOWN = 1;
	public static final float RANGEDSPEED = 10;
	
	//Chance that an enemy will drop a potion in percent
	public static final float POTIONCHANCE = 30;

	//The number of dungeon songs that will be looped
	public static final int NUMOFDUNGEONSONGS = 2;
	
	//Max number of potions that can be held
	public static final int MAXHEALTHPOTIONS = 3;
	public static final int MAXDAMAGEPOTIONS = 2;
	
	//Health Potion healing amount
	public static final int HEALTHPOTIONHEALING = 50;
	
	//Damage Potion damage increase amount (added to damage multiplier, 1 is base plus 1, ie doubled)
	//and modifier time length in seconds
	public static final int DAMAGEPOTIONINCREASE = 1;
	public static final int DAMAGEPOTIONDURATION = 5;
}