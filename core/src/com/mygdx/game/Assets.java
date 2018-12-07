/**
 * Handles all assets that will be used in the game
 * @author Andrew Stake
 */

package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
//import com.mygdx.game.Assets.AssetFonts;
import com.mygdx.game.utils.Constants;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
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
	public assetWallEnd wallEnd;
	public assetChest chest;
	public assetCrate crate;
	public assetCoin goldCoin;
	public assetFonts fonts;
	public assetHealthBar healthBar;
	public assetUIBackground UIBackground;
	public assetAttacks attacks;
	
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
		wallEnd = new assetWallEnd(atlas);
		chest = new assetChest(atlas);
		crate = new assetCrate(atlas);
		goldCoin = new assetCoin(atlas);
		fonts = new assetFonts();
		healthBar = new assetHealthBar();
		UIBackground = new assetUIBackground();
		attacks = new assetAttacks(atlas);

	}
	
	@Override
	public void dispose()
	{
		assetManager.dispose();
		fonts.defaultSmall.dispose();
		fonts.defaultNormal.dispose();
		fonts.defaultBig.dispose();
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
			wallVertical= atlas.findRegion("wallVertical");
			wallHorizontal = atlas.findRegion("wallHorizontal");
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
			topRight = atlas.findRegion("wallTopRight");
			topLeft = atlas.findRegion("wallTopLeft");
			bottomRight = atlas.findRegion("wallBotRight");
			bottomLeft = atlas.findRegion("wallBotLeft");
		}
	}
	
	public class assetDoor
	{
		public final AtlasRegion doorHor;
		public final AtlasRegion doorVert;
		
		public assetDoor(TextureAtlas atlas)
		{
			doorHor = atlas.findRegion("doorHorizontal");
			doorVert = atlas.findRegion("doorVertical");
		}
	}
	
	public class assetTile
	{
		public final AtlasRegion tile;
		
		public assetTile (TextureAtlas atlas)
		{
			tile = atlas.findRegion("tiles1");
		}
	}
	
	public class assetFloor
	{
		public final AtlasRegion floor;
		
		public assetFloor (TextureAtlas atlas)
		{
			floor = atlas.findRegion("floor1");
		}
	}
	
	public class assetFloorBig
	{
		public final AtlasRegion floorBig;
		
		public assetFloorBig (TextureAtlas atlas)
		{
			floorBig = atlas.findRegion("floor2");
		}
	}
	
	public class assetRubble
	{
		public final AtlasRegion rubble;
		
		public assetRubble (TextureAtlas atlas)
		{
			rubble = atlas.findRegion("rubble1");
		}
	}
	
	public class assetRubbleBig
	{
		public final AtlasRegion rubbleBig;
		
		public assetRubbleBig (TextureAtlas atlas)
		{
			rubbleBig = atlas.findRegion("rubble2");
		}
	}
	
	public class assetLadderUp
	{
		public final AtlasRegion ladderUp;
		
		public assetLadderUp (TextureAtlas atlas)
		{
			ladderUp = atlas.findRegion("ladderUp");
		}
	}
	
	public class assetLadderDown
	{
		public final AtlasRegion ladderDown;
		
		public assetLadderDown (TextureAtlas atlas)
		{
			ladderDown = atlas.findRegion("ladderDown");
		}
	}
	
	public class assetBarbarian
	{
		public final AtlasRegion barbarian;
		
		public assetBarbarian (TextureAtlas atlas)
		{
			barbarian = atlas.findRegion("barbarian1");
		}
	}
	
	public class assetGoblin
	{
		public final AtlasRegion goblin;
		
		public assetGoblin (TextureAtlas atlas)
		{
			goblin = atlas.findRegion("goblin2");
		}
	}
	
	public class assetCharacter
	{
		public final AtlasRegion character;
		
		public assetCharacter (TextureAtlas atlas)
		{
			character = atlas.findRegion("wizard1");
		}
	}
	
	public class assetWallEnd
	{
		public final AtlasRegion wallEndLeft;
		public final AtlasRegion wallEndRight;
		public final AtlasRegion wallEndTop;
		public final AtlasRegion wallEndBot;
		
		public assetWallEnd (TextureAtlas atlas)
		{
			wallEndLeft = atlas.findRegion("wallDoorLeft");
			wallEndRight = atlas.findRegion("wallDoorRight");
			wallEndTop = atlas.findRegion("wallDoorTop");
			wallEndBot = atlas.findRegion("wallDoorBottom");
		}
	}
	
	public class assetChest
	{
		public final AtlasRegion chest;
		
		public assetChest (TextureAtlas atlas)
		{
			chest = atlas.findRegion("chest1");
		}
	}
	
	public class assetCrate
	{
		public final AtlasRegion crate;
		
		public assetCrate (TextureAtlas atlas)
		{
			crate = atlas.findRegion("crate1");
		}
	}
	
	public class assetCoin
	{
		public final AtlasRegion goldCoin;
		
		public assetCoin (TextureAtlas atlas)
		{
			goldCoin = atlas.findRegion("goldCoin");
		}
	}
	
	public class assetFonts 
	{
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;
		
		public assetFonts () 
		{
			// create three fonts using Libgdx's 15px bitmap font
			defaultSmall = new BitmapFont(new FileHandle("../core/assets/fonts/arial-15.fnt"), true);
			defaultNormal = new BitmapFont(new FileHandle("../core/assets/fonts/arial-15.fnt"), true);
			defaultBig = new BitmapFont(new FileHandle("../core/assets/fonts/arial-15.fnt"), true);
			// set font sizes
			defaultSmall.getData().setScale(1.5f);
			defaultNormal.getData().setScale(2.0f);
			defaultBig.getData().setScale(3.0f);
		
			// enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture().setFilter(
			TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(
			TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(
			TextureFilter.Linear, TextureFilter.Linear);
		}
	}
	
	public class assetHealthBar
	{
		public NinePatchDrawable healthBarBackground;
	    public NinePatchDrawable healthBar;

	    public assetHealthBar() 
	    {
		    TextureAtlas skinAtlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
		    NinePatch loadingBarBackgroundPatch = new NinePatch(skinAtlas.findRegion("default-round"), 5, 5, 4, 4);
		    NinePatch loadingBarPatch = new NinePatch(skinAtlas.findRegion("default-round-down"), 5, 5, 4, 4);
	    	healthBar = new NinePatchDrawable(loadingBarPatch);
	    	healthBarBackground = new NinePatchDrawable(loadingBarBackgroundPatch);
	    }
	}
	
	public class assetUIBackground
	{
		public Texture UIBackground;
		public Texture wedge;
		
		public assetUIBackground()
		{
			UIBackground = new Texture("../desktop/assets-raw/MenuBackground/MenuScreen.png");
			wedge = new Texture("../desktop/assets-raw/MenuBackground/MenuWedge.png");
		}
		
	}
	
	public class assetAttacks
	{
		public final AtlasRegion attack1;
		public final AtlasRegion arrow;
		
		public assetAttacks(TextureAtlas atlas)
		{
			attack1 = atlas.findRegion("attack1");
			arrow = atlas.findRegion("arrow");
		}
	}
}
