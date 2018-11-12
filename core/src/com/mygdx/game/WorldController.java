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
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.game.objects.*;
import com.mygdx.game.utils.CameraHelper;
import com.mygdx.game.utils.Constants;

public class WorldController extends InputAdapter implements ContactListener
{
	private static final String TAG = WorldController.class.getName();
	
	public int selectedSprite;
	public CameraHelper cameraHelper;
	public static World b2dWorld;
	public Room activeLevel;
	public Array<Room> levels;
	public Wall testWall;
	public AbstractGameObject touchedObject;
	public boolean disabled;
	
	public WorldController()
	{
		init();
	}
	
	/**
	 * Prepares all of the objects for the world
	 */
	private void init()
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		b2dWorld = new World(new Vector2(0, 0), true); 
		b2dWorld.setContactListener(this);
		levels = new Array<Room>();
		
		initLevel();
		
		cameraHelper.setTarget(activeLevel.player);
		System.out.println(activeLevel.movementGrid);
	}
	
	/**
	 * Builds the levels and sets the first to the active level
	 */
	private void initLevel()
	{
		levels.add(new Room(Constants.LEVEL_01, this));
		activeLevel = levels.first(); //TODO add level switching
		
	}
	
	public void update(float deltaTime)
	{
		b2dWorld.step(deltaTime, 5, 3);
		handleDebugInput(deltaTime);
		handlePlayerInput(deltaTime);
		cameraHelper.update(deltaTime);
		activeLevel.update(deltaTime);
	}
	
	/**
	 * Handles the input for player movement and actions
	 * @param deltaTime
	 */
	private void handlePlayerInput(float deltaTime)
	{
		Vector2 velocity = new Vector2(0,0);
		
		//Player Movement: x
		if (Gdx.input.isKeyPressed(Keys.D))
		{
			velocity.x += activeLevel.player.movementSpeed;
		}
		else if (Gdx.input.isKeyPressed(Keys.A))
		{
			velocity.x -= activeLevel.player.movementSpeed;
		}
		
		//Player Movement: y
		if (Gdx.input.isKeyPressed(Keys.W))
		{
			velocity.y += activeLevel.player.movementSpeed;
		}
		else if (Gdx.input.isKeyPressed(Keys.S))
		{
			velocity.y -= activeLevel.player.movementSpeed;
		}
		//Set velocity of player
		activeLevel.player.body.setLinearVelocity(velocity);
		
		//interaction button
		if (Gdx.input.isKeyJustPressed(Keys.E))
		{
			if(touchedObject != null)
			{
				touchedObject.activate();
			}
		}
		
	}

	/**
	 * Handles the camera input
	 * @param deltaTime
	 */
	private void handleDebugInput(float deltaTime)
	{
		if (Gdx.app.getType() != ApplicationType.Desktop) return;
		
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
		
		//disable enemies for easier debugging
		if(Gdx.input.isKeyJustPressed(Keys.B)) //TODO remove this
		{
			if(disabled)
			{
				activeLevel.disableEnemies(false);
				disabled = false;
				System.out.println("Enemies Re-enabled");
			}
			else
			{
				activeLevel.disableEnemies(true);
				disabled = true;
				System.out.println("Enemies Disabled");
			}
		}
	}
	
	/**
	 * Moves the camera to the given position
	 * @param x
	 * @param y
	 */
	private void moveCamera(float x, float y)
	{
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x,y);
	}
	
	/**
	 * Handles higher level input commands
	 */
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
				activeLevel.player);
			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
		}
		return false;
	}

	@Override
	public void beginContact(Contact contact)
	{
		if(contact.getFixtureA().getBody().getUserData() == activeLevel.player && contact.getFixtureB().isSensor()) //TODO may need to check that this isn't an enemy object
		{
			touchedObject = (AbstractGameObject) contact.getFixtureB().getBody().getUserData();
		}
		else if(contact.getFixtureB().getBody().getUserData() == activeLevel.player && contact.getFixtureA().isSensor())
		{
			touchedObject = (AbstractGameObject) contact.getFixtureA().getBody().getUserData();
		}
		
		System.out.println(touchedObject);
	}

	@Override
	public void endContact(Contact contact)
	{
		if(contact.getFixtureA().getBody().getUserData() == touchedObject)
		{
			touchedObject = null;	
		}
		else if(contact.getFixtureB().getBody().getUserData() == touchedObject)
		{
			touchedObject = null;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse)
	{
		// TODO Auto-generated method stub
		
	}
	
	
}








