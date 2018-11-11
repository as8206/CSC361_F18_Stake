package com.mygdx.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.objects.*;
import com.mygdx.game.utils.CameraHelper;
import com.mygdx.game.utils.Constants;

public class WorldController extends InputAdapter
{
	private static final String TAG = WorldController.class.getName();
	
	public Sprite[] testSprites;
	public Sprite[] testRoomSprites;
	public int selectedSprite;
	public CameraHelper cameraHelper;
	public static World b2dWorld;
	public Level level01;
	public Wall testWall;
	
	public WorldController()
	{
		init();
	}
	
	private void init()
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		b2dWorld = new World(new Vector2(0, 0), true); 
		
		initTestObjects();
		initTestRoomObjects();
		initLevel();
		
		cameraHelper.setTarget(level01.player);
		System.out.println(level01.movementGrid);
	}
	
	private void initLevel()
	{
		level01 = new Level(Constants.LEVEL_01);
		
	}

	private void initTestObjects()
	{
		// Create new array for 18 sprites
		testSprites = new Sprite[18];
		
		//Create a list of texture regions
		Array<TextureRegion> regions = new Array<TextureRegion>();
		regions.add(Assets.instance.barbarian.barbarian);
		regions.add(Assets.instance.character.character);
		regions.add(Assets.instance.door.doorHor);
		regions.add(Assets.instance.door.doorVert);
		regions.add(Assets.instance.floor.floor);
		regions.add(Assets.instance.floorBig.floorBig);
		regions.add(Assets.instance.goblin.goblin);
		regions.add(Assets.instance.ladderDown.ladderDown);
		regions.add(Assets.instance.ladderUp.ladderUp);
		regions.add(Assets.instance.rubble.rubble);
		regions.add(Assets.instance.rubbleBig.rubbleBig);
		regions.add(Assets.instance.tile.tile);
		regions.add(Assets.instance.wall.wallHorizontal);
		regions.add(Assets.instance.wall.wallVertical);
		regions.add(Assets.instance.wallCorner.bottomLeft);
		regions.add(Assets.instance.wallCorner.bottomRight);
		regions.add(Assets.instance.wallCorner.topLeft);
		regions.add(Assets.instance.wallCorner.topRight);
		
		//Create new sprites using a random texture region
		for(int i = 0; i < testSprites.length; i++)
		{
			Sprite spr = new Sprite(regions.pop());

			//define sprite size to be 1m x 1m in game world
			spr.setSize(1, 1);
			
			//Set origin to sprite's center
			spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
			
			//Calculate random positions for sprite
			float randomX = MathUtils.random(-8.0f, 8.0f);
			float randomY = MathUtils.random(-4.0f, 4.0f);

			spr.setPosition(randomX, randomY);

			
			// Put new sprite into array
			testSprites[i] = spr;
		}
		
		// Set first sprite as selected one
		selectedSprite = 0;
	}
	
	public void initTestRoomObjects()
	{
		testRoomSprites = new Sprite[302];
		int index = 0;
		Sprite spr = new Sprite(Assets.instance.wallCorner.topLeft);
		//middle = 8, corner = -8
		float x = -8.0f;
		float y = 8.0f;
		
		spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
		spr.setPosition(x, y);
		spr.setSize(1, 1);
		testRoomSprites[index] = spr;
		index++;
		x += 1.0f;
		
		for(int i = 0; i < 15; i++)
		{
			if(i == 6 || i == 7 || i == 8)
			{
				spr = new Sprite(Assets.instance.tile.tile);
				spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() /2.0f);
				spr.setPosition(x, y);
				spr.setSize(1, 1);
				testRoomSprites[index] = spr;
				index++;
			}
			
			spr = new Sprite(Assets.instance.wall.wallHorizontal);
			if(i == 7)
				spr.setRegion(Assets.instance.door.doorHor);
			if(i == 6)
				spr.setRegion(Assets.instance.wallEnd.wallEndLeft);
			if(i == 8)
				spr.setRegion(Assets.instance.wallEnd.wallEndRight);
			
			spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() /2.0f);
			spr.setPosition(x, y);
			spr.setSize(1, 1);
			testRoomSprites[index] = spr;
			index++;
			x += 1.0f;
		}
		
		spr = new Sprite(Assets.instance.wallCorner.topRight);
		spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
		spr.setPosition(x, y);
		spr.setSize(1, 1);
		testRoomSprites[index] = spr;
		index++;
		x = -8.0f;
		y -= 1.0f;
		
		for (int i = 0; i < 15; i++)
		{
			if(i == 6 || i == 7 || i == 8)
			{
				spr = new Sprite(Assets.instance.tile.tile);
				spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() /2.0f);
				spr.setPosition(x, y);
				spr.setSize(1, 1);
				testRoomSprites[index] = spr;
				index++;
			}
			
			spr = new Sprite(Assets.instance.wall.wallVertical);
			if (i == 7)
				spr.setRegion(Assets.instance.door.doorVert);
			if (i == 6)
				spr.setRegion(Assets.instance.wallEnd.wallEndTop);
			if (i == 8)
				spr.setRegion(Assets.instance.wallEnd.wallEndBot);
			spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
			spr.setPosition(x, y);
			spr.setSize(1, 1);
			testRoomSprites[index] = spr;
			index++;
			x += 1.0f;
			
			for (int j = 0; j < 15; j++)
			{
				spr = new Sprite(Assets.instance.tile.tile);
				spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
				spr.setPosition(x, y);
				spr.setSize(1, 1);
				testRoomSprites[index] = spr;
				index++;
				x += 1.0f;
			}
			
			if(i == 6 || i == 7 || i == 8)
			{
				spr = new Sprite(Assets.instance.tile.tile);
				spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() /2.0f);
				spr.setPosition(x, y);
				spr.setSize(1, 1);
				testRoomSprites[index] = spr;
				index++;
			}
			
			spr = new Sprite(Assets.instance.wall.wallVertical);
			if (i == 7)
				spr.setRegion(Assets.instance.door.doorVert);
			if (i == 6)
				spr.setRegion(Assets.instance.wallEnd.wallEndTop);
			if (i == 8)
				spr.setRegion(Assets.instance.wallEnd.wallEndBot);
			spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
			spr.setPosition(x, y);
			spr.setSize(1, 1);
			testRoomSprites[index] = spr;
			index++;
			x = -8.0f;
			y -= 1.0f;
		}
		
		spr = new Sprite(Assets.instance.wallCorner.bottomLeft);		
		spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
		spr.setPosition(x, y);
		spr.setSize(1, 1);
		testRoomSprites[index] = spr;
		index++;
		x += 1.0f;
		
		for(int i = 0; i < 15; i++)
		{
			if(i == 6 || i == 7 || i == 8)
			{
				spr = new Sprite(Assets.instance.tile.tile);
				spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() /2.0f);
				spr.setPosition(x, y);
				spr.setSize(1, 1);
				testRoomSprites[index] = spr;
				index++;
			}
			
			spr = new Sprite(Assets.instance.wall.wallHorizontal);
			if(i == 7)
				spr.setRegion(Assets.instance.door.doorHor);
			if(i == 6)
				spr.setRegion(Assets.instance.wallEnd.wallEndLeft);
			if(i == 8)
				spr.setRegion(Assets.instance.wallEnd.wallEndRight);
			spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() /2.0f);
			spr.setPosition(x, y);
			spr.setSize(1, 1);
			testRoomSprites[index] = spr;
			index++;
			x += 1.0f;
		}
		
		spr = new Sprite(Assets.instance.wallCorner.bottomRight);
		spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
		spr.setPosition(x, y);
		spr.setSize(1, 1);
		testRoomSprites[index] = spr;
		index++;
		x = -8.0f;
		y -= 1.0f;
		
		//place character in a random position
		spr = new Sprite(Assets.instance.character.character);
		spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
		spr.setPosition(MathUtils.random(-7.0f, 7.0f), MathUtils.random(-7.0f, 7.0f));
		spr.setSize(1.5f, 1.5f);
		testRoomSprites[index] = spr;
		index++;
		
	}
	
	public void update(float deltaTime)
	{
		b2dWorld.step(deltaTime, 5, 3);
		handleDebugInput(deltaTime);
		updateTestObjects(deltaTime);
		handlePlayerInput(deltaTime);
		cameraHelper.update(deltaTime);
		level01.update(deltaTime);
	}
	
	private void handlePlayerInput(float deltaTime)
	{
		Vector2 velocity = new Vector2(0,0);
		
		//Player Movement: x
		if (Gdx.input.isKeyPressed(Keys.D))
		{
			velocity.x += level01.player.movementSpeed;
		}
		else if (Gdx.input.isKeyPressed(Keys.A))
		{
			velocity.x -= level01.player.movementSpeed;
		}
		
		//Player Movement: y
		if (Gdx.input.isKeyPressed(Keys.W))
		{
			velocity.y += level01.player.movementSpeed;
		}
		else if (Gdx.input.isKeyPressed(Keys.S))
		{
			velocity.y -= level01.player.movementSpeed;
		}
		
		//Set velocity of player
		level01.player.body.setLinearVelocity(velocity); //TODO Abstract out level so player can be found when a new level is created
	}

	private void handleDebugInput(float deltaTime)
	{
		if (Gdx.app.getType() != ApplicationType.Desktop) return;
		
		// Selected Sprite Controls
		float sprMoveSpeed = 5 * deltaTime;
		if (Gdx.input.isKeyPressed(Keys.A)) 
			moveSelectedSprite(-sprMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.D))
			moveSelectedSprite(sprMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.W))
			moveSelectedSprite(0, sprMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.S))
			moveSelectedSprite(0, -sprMoveSpeed);
		
		// Camera Controls (zoom)
		float camMoveSpeed = 5 * deltaTime;
		float camMoveSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
			camMoveSpeed *= camMoveSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			moveCamera(-camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			moveCamera(camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.UP))
			moveCamera(0, camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.DOWN))
			moveCamera(0, -camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.BACKSPACE))
			cameraHelper.setPosition(0,0);
		
		// Camera Controls (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
			camZoomSpeed *= camZoomSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.COMMA))
			cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.PERIOD))
			cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.SLASH))
			cameraHelper.setZoom(1);
		
		//test moveTo
		if(Gdx.input.isKeyPressed(Keys.B))
		{
			level01.meleeEnemies.peek().moveTo(0, 0);
			System.out.println(level01.meleeEnemies.peek().body.getPosition());
		}
	}
	
	private void moveCamera(float x, float y)
	{
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x,y);
	}
	
	private void moveSelectedSprite(float x, float y)
	{
		testSprites[selectedSprite].translate(x, y);
	}
	
	private void updateTestObjects(float deltaTime)
	{
		// Get current rotation from selected sprite
		float rotation = testSprites[selectedSprite].getRotation();
		// Rotate sprite by 90 degrees per second
		rotation += 90 * deltaTime;
		// Wrap around 360 degrees
		rotation %= 360;
		// Set new rotation value to selected sprite
		testSprites[selectedSprite].setRotation(rotation);
	}
	
	@Override
	public boolean keyUp(int keycode)
	{
		// Reset game world
		if (keycode == Keys.R)
		{
			init();
			Gdx.app.debug(TAG, "Game world resetted");
		}
		// Toggle camera follow
		else if (keycode == Keys.ENTER)
		{
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null : 
				level01.player);
			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
		}
		return false;
	}
	
	
}








