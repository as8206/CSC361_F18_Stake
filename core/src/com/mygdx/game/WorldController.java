package com.mygdx.game;

import java.io.File;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
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
	private static final int roomArrayOffset = (Constants.MAXROOMS - 1) /2;
	
	public CameraHelper cameraHelper;
	public static World b2dWorld;
	public Room activeRoom;
	private AbstractGameObject touchedObject;
	public boolean enemiesDisabled;
	private Room[][] rooms;
	private Array<String> randomizedRooms;
	private Array<Body> bodiesToBeRemoved;
	private int score;
	
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
		rooms = new Room[Constants.MAXROOMS][Constants.MAXROOMS];
		bodiesToBeRemoved = new Array<Body>();
		score = 0;
		prepRoomFiles();
		
		initLevel();
		
		cameraHelper.setTarget(activeRoom.player);
	}
	
	/**
	 * Builds the levels and sets the first to the active level
	 */
	private void initLevel()
	{
		rooms[roomArrayOffset][roomArrayOffset] = new Room(Constants.STARTROOM, this, 0, 0);
		activeRoom = rooms[roomArrayOffset][roomArrayOffset]; //TODO add level switching
		
	}
	
	public void update(float deltaTime)
	{
		b2dWorld.step(deltaTime, 5, 3);
		handleDebugInput(deltaTime);
		handlePlayerInput(deltaTime);
		cameraHelper.update(deltaTime);
		activeRoom.update(deltaTime);
		removeBodies();
	}
	
	/**
	 * Removes all bodies in bodiesToBeRemoved from the Box2d World
	 */
	private void removeBodies()
	{
		for(Body body : bodiesToBeRemoved)
		{
			b2dWorld.destroyBody(body);
			bodiesToBeRemoved.removeValue(body, false);
		}
	}

	/**
	 * Adds a body to be removed from the world
	 * @param body
	 */
	public void addToRemoval(Body body)
	{
		bodiesToBeRemoved.add(body);
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
			velocity.x += activeRoom.player.movementSpeed;
		}
		else if (Gdx.input.isKeyPressed(Keys.A))
		{
			velocity.x -= activeRoom.player.movementSpeed;
		}
		
		//Player Movement: y
		if (Gdx.input.isKeyPressed(Keys.W))
		{
			velocity.y += activeRoom.player.movementSpeed;
		}
		else if (Gdx.input.isKeyPressed(Keys.S))
		{
			velocity.y -= activeRoom.player.movementSpeed;
		}
		//Set velocity of player
		activeRoom.player.body.setLinearVelocity(velocity);
		
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
	 * Handles the camera input and debug input
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
		
		//disable enemies and speeds up player for easier debugging
		if(Gdx.input.isKeyJustPressed(Keys.B)) //TODO remove this
		{
			if(enemiesDisabled)
			{
				enemiesDisabled = false;
				activeRoom.player.movementSpeed = 3.0f;
				System.out.println("Enemies Re-enabled");
			}
			else
			{
				enemiesDisabled = true;
				activeRoom.player.movementSpeed = 7.0f;
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
				activeRoom.player);
			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
		}
		
		return false;
	}

	@Override
	public void beginContact(Contact contact)
	{
		//collisions for gold coin, non-button activated
		if(contact.getFixtureA().getBody().getUserData() == activeRoom.player && contact.getFixtureB().getBody().getUserData().getClass() == GoldCoin.class && contact.getFixtureB().isSensor())
		{
			touchedObject = (AbstractGameObject) contact.getFixtureB().getBody().getUserData();
			touchedObject.activate();
			touchedObject = null;
			
		}
		else if(contact.getFixtureB().getBody().getUserData() == activeRoom.player && contact.getFixtureA().getBody().getUserData().getClass() == GoldCoin.class && contact.getFixtureB().isSensor())
		{
			touchedObject = (AbstractGameObject) contact.getFixtureB().getBody().getUserData();
			touchedObject.activate();
			touchedObject = null;
			
		}
		//collisions for button activated objects
		else if(contact.getFixtureA().getBody().getUserData() == activeRoom.player && contact.getFixtureB().isSensor()) //TODO may need to check that this isn't an enemy object
		{
			touchedObject = (AbstractGameObject) contact.getFixtureB().getBody().getUserData();
		}
		else if(contact.getFixtureB().getBody().getUserData() == activeRoom.player && contact.getFixtureA().isSensor())
		{
			touchedObject = (AbstractGameObject) contact.getFixtureA().getBody().getUserData();
		}
		
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

	/**
	 * Called if a door with no room linked is activated
	 * if a room already exists, links the door and calls swap room
	 * if a room doesn't exists, this creates it and swaps to it
	 * @param door
	 */
	public void createNewRoom(Door door) 
	{
		int roomOffsetX = activeRoom.roomOffsetX;
		int roomOffsetY = activeRoom.roomOffsetY;
		
		//finds and selects what side of the room the new linked door should be
		//also calculates the offset for the new room
		int newDoorSide;
		if(door.side == Door.TOP)
		{
			newDoorSide = Door.BOTTOM;
			roomOffsetY += Constants.ROOMOFFSET;
		}
		else if(door.side == Door.RIGHT)
		{
			newDoorSide = Door.LEFT;
			roomOffsetX += Constants.ROOMOFFSET;
		}
		else if(door.side == Door.BOTTOM)
		{
			newDoorSide = Door.TOP;
			roomOffsetY -= Constants.ROOMOFFSET;
		}
		else //if(door.side == Door.LEFT)
		{
			newDoorSide = Door.RIGHT;
			roomOffsetX -= Constants.ROOMOFFSET;
		}
		
		//Checks that the room isn't outside the bounds
		if((roomOffsetX / Constants.ROOMOFFSET) + roomArrayOffset >= Constants.MAXROOMS || (roomOffsetX / Constants.ROOMOFFSET) + roomArrayOffset < 0 )
		{
			return; //TODO trigger a message here "door is jammed"
		}
		
		if((roomOffsetY / Constants.ROOMOFFSET) + roomArrayOffset >= Constants.MAXROOMS || (roomOffsetY / Constants.ROOMOFFSET) + roomArrayOffset < 0)
		{
			return; //TODO also trigger same message here
		}
		
		if(rooms[(roomOffsetX / Constants.ROOMOFFSET) + roomArrayOffset][(roomOffsetY / Constants.ROOMOFFSET) + roomArrayOffset] != null)
		{
			door.setLinkedRoom(rooms[(roomOffsetX / Constants.ROOMOFFSET) + roomArrayOffset][(roomOffsetY / Constants.ROOMOFFSET) + roomArrayOffset]);
			swapRoom(rooms[(roomOffsetX / Constants.ROOMOFFSET) + roomArrayOffset][(roomOffsetY / Constants.ROOMOFFSET) + roomArrayOffset], door);
			return;
		}
			
		//checks that a new room is available //TODO if a new unique room isnt available, use a random non treasure room
		if(randomizedRooms.size == 0)
		{
			return; //TODO trigger message here "this door seems locked"
		}
		
		Room newRoom = new Room(randomizedRooms.pop(), this, roomOffsetX, roomOffsetY);
		
		Door newDoor = newRoom.doors.first();
		for (Door tempDoor : newRoom.doors)
		{
			if(tempDoor.side == newDoorSide)
				newDoor = tempDoor;
		}
		
		newDoor.setLinkedRoom(activeRoom);
		door.setLinkedRoom(newRoom);
		
		float newX, newY;
		if(newDoor.side == Door.TOP)
		{
			newX = newDoor.body.getPosition().x;
			newY = newDoor.body.getPosition().y - 1;
		}
		else if(newDoor.side == Door.RIGHT)
		{
			newX = newDoor.body.getPosition().x - 1;
			newY = newDoor.body.getPosition().y;
		}
		else if(newDoor.side == Door.BOTTOM)
		{
			newX = newDoor.body.getPosition().x;
			newY = newDoor.body.getPosition().y + 1;
		}
		else //if(newDoor.side == Door.LEFT)
		{
			newX = newDoor.body.getPosition().x + 1;
			newY = newDoor.body.getPosition().y;
		}
		activeRoom.player.body.setTransform(newX, newY, 0);
		newRoom.setPlayer(activeRoom.player);
		newRoom.reassignTarget();
		
		rooms[(roomOffsetX / Constants.ROOMOFFSET) + roomArrayOffset][(roomOffsetY / Constants.ROOMOFFSET) + roomArrayOffset] = newRoom;
		activeRoom = newRoom;
	}

	/**
	 * Swaps the active room to the new room from the given door
	 * @param newRoom
	 * @param door
	 */
	public void swapRoom(Room newRoom, Door door)
	{
		int newDoorSide;
		if(door.side == Door.TOP)
			newDoorSide = Door.BOTTOM;
		else if(door.side == Door.RIGHT)
			newDoorSide = Door.LEFT;
		else if(door.side == Door.BOTTOM)
			newDoorSide = Door.TOP;
		else //if(door.side == Door.LEFT)
			newDoorSide = Door.RIGHT;
		
		Door newDoor = newRoom.doors.first();
		for (Door tempDoor : newRoom.doors)
		{
			if(tempDoor.side == newDoorSide)
				newDoor = tempDoor;
		}
		
		float newX, newY;
		if(newDoor.side == Door.TOP)
		{
			newX = newDoor.body.getPosition().x;
			newY = newDoor.body.getPosition().y - 1;
		}
		else if(newDoor.side == Door.RIGHT)
		{
			newX = newDoor.body.getPosition().x - 1;
			newY = newDoor.body.getPosition().y;
		}
		else if(newDoor.side == Door.BOTTOM)
		{
			newX = newDoor.body.getPosition().x;
			newY = newDoor.body.getPosition().y + 1;
		}
		else //if(newDoor.side == Door.LEFT)
		{
			newX = newDoor.body.getPosition().x + 1;
			newY = newDoor.body.getPosition().y;
		}
		activeRoom.player.body.setTransform(newX, newY, 0);
		newRoom.setPlayer(activeRoom.player);
		newRoom.reassignTarget();
		
		activeRoom = newRoom;
	}
	
	public void prepRoomFiles()
	{
		randomizedRooms = new Array<String>();
	    File path = new File(Constants.ROOMFILES);

	    File [] files = path.listFiles();
	    for (int i = 0; i < files.length; i++)
	        if (files[i].isFile())
	            randomizedRooms.add(files[i].toString());
	    randomizedRooms.shuffle();
	}
	
}








