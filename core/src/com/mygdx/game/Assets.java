/**
 * Handles all assets that will be used in the game
 * @author Andrew Stake
 */

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.utils.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class Assets implements AssetErrorListener, Disposable
{
public static final String TAG = Assets.class.getName();
	
	public static final Assets instance = new Assets();
	
	private AssetManager assetManager;
	
	//singleton: prevent instantiation from other classes
	private Assets() {}
	
	public assetWall wall;
	public assetWallCorner wallCorner;
	public assetDoor door;
	public assetTile tile;
	public assetFloor floor;
	public assetFloorBig floorBig;
	public assetRubble rubble;
	public assetRubbleBig rubbleBig;
	public assetLadderUp ladderUp;
	public assetLadderDown ladderDown;
	public assetBarbarian barbarian;
	public assetGoblin goblin;
	public assetCharacter character;
	
	//initializes the assets class and all of its inner classes
	public void init(AssetManager assetManager)
	{
		this.assetManager = assetManager;
		//set assent manager error handler
		assetManager.setErrorListener(this);
		//load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		//start loading assets and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames());
		
		for (String a : assetManager.getAssetNames())
			Gdx.app.debug(TAG, "asset: " + a);
		
		TextureAtlas atlas = new TextureAtlas(Constants.TEXTURE_ATLAS_OBJECTS);
		
		//enable texture filtering for pixel smoothing
		for(Texture t : atlas.getTextures())
		{
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		//create asset objects
		wall = new assetWall(atlas);
		wallCorner = new assetWallCorner(atlas);
		door = new assetDoor(atlas);
		tile = new assetTile(atlas);
		floor = new assetFloor(atlas);
		floorBig = new assetFloorBig(atlas);
		rubble = new assetRubble(atlas);
		rubbleBig = new assetRubbleBig(atlas);
		ladderUp = new assetLadderUp(atlas);
		ladderDown = new assetLadderDown(atlas);
		barbarian = new assetBarbarian(atlas);
		goblin = new assetGoblin(atlas);
		character = new assetCharacter(atlas);
	}
	
	@Override
	public void dispose()
	{
		assetManager.dispose();
	}
	
	//Removed @Override due to no error method with these parameter is AssetErrorListener interface
	public void error(String filename, Class type, Throwable throwable)
	{
		Gdx.app.error(TAG, "Couldn't load asset '" + filename + "'", (Exception)throwable);
	}
	
	public void error(AssetDescriptor asset, Throwable throwable)
	{
		Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
	}
	
	//asset class for the walls
	public class assetWall
	{
		//The actual asset to be used for this object
		public final AtlasRegion wallVertical;
		public final AtlasRegion wallHorizontal;
				
		//Create the object and give it's sprite the proper image
		public assetWall(TextureAtlas atlas)
		{
			wallVertical= atlas.findRegion("wall_vertical");
			wallHorizontal = atlas.findRegion("wall_horizontal");
		}
	}
	
	//asset class for the 4 wall corners
	public class assetWallCorner
	{
		public final AtlasRegion topRight;
		public final AtlasRegion topLeft;
		public final AtlasRegion bottomRight;
		public final AtlasRegion bottomLeft;
		
		public assetWallCorner(TextureAtlas atlas)
		{
			topRight = atlas.findRegion("wall_top_right");
			topLeft = atlas.findRegion("wall_top_left");
			bottomRight = atlas.findRegion("wall_bot_right");
			bottomLeft = atlas.findRegion("wall_bot_left");
		}
	}
	
	public class assetDoor
	{
		public final AtlasRegion doorHor;
		public final AtlasRegion doorVert;
		
		public assetDoor(TextureAtlas atlas)
		{
			doorHor = atlas.findRegion("door_horizontal");
			doorVert = atlas.findRegion("door_vertical");
		}
	}
	
	public class assetTile
	{
		public final AtlasRegion tile;
		
		public assetTile (TextureAtlas atlas)
		{
			tile = atlas.findRegion("tile_1");
		}
	}
	
	public class assetFloor
	{
		public final AtlasRegion floor;
		
		public assetFloor (TextureAtlas atlas)
		{
			floor = atlas.findRegion("floor_1");
		}
	}
	
	public class assetFloorBig
	{
		public final AtlasRegion floorBig;
		
		public assetFloorBig (TextureAtlas atlas)
		{
			floorBig = atlas.findRegion("floor_2");
		}
	}
	
	public class assetRubble
	{
		public final AtlasRegion rubble;
		
		public assetRubble (TextureAtlas atlas)
		{
			rubble = atlas.findRegion("rubble_1");
		}
	}
	
	public class assetRubbleBig
	{
		public final AtlasRegion rubbleBig;
		
		public assetRubbleBig (TextureAtlas atlas)
		{
			rubbleBig = atlas.findRegion("rubble_2");
		}
	}
	
	public class assetLadderUp
	{
		public final AtlasRegion ladderUp;
		
		public assetLadderUp (TextureAtlas atlas)
		{
			ladderUp = atlas.findRegion("ladder_up");
		}
	}
	
	public class assetLadderDown
	{
		public final AtlasRegion ladderDown;
		
		public assetLadderDown (TextureAtlas atlas)
		{
			ladderDown = atlas.findRegion("ladder_down");
		}
	}
	
	public class assetBarbarian
	{
		public final AtlasRegion barbarian;
		
		public assetBarbarian (TextureAtlas atlas)
		{
			barbarian = atlas.findRegion("barbarian_1");
		}
	}
	
	public class assetGoblin
	{
		public final AtlasRegion goblin;
		
		public assetGoblin (TextureAtlas atlas)
		{
			goblin = atlas.findRegion("goblin_2");
		}
	}
	
	public class assetCharacter
	{
		public final AtlasRegion character;
		
		public assetCharacter (TextureAtlas atlas)
		{
			character = atlas.findRegion("wizard_1");
		}
	}
}
