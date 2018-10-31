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
//import com.mygdx.game.util.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class Assets
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
	
	public void init(AssetManager assetManager)
	{
		
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
	
	public class assetWall
	{
		
	}
	
	public class assetWallCorner
	{
		
	}
	
	public class assetDoor
	{
		
	}
	
	public class assetTile
	{
		
	}
	
	public class assetFloor
	{
		
	}
	
}
