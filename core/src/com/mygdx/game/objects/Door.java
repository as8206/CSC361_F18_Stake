/**
 * Will hold both directions of door objects
 * @author Andrew Stake
 */
package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.Assets;
import com.mygdx.game.Room;
import com.mygdx.game.WorldController;
import com.mygdx.game.utils.AudioManager;
import com.mygdx.game.utils.Constants;

public class Door extends AbstractGameObject
{
	//Constants for door side
	public static final int TOP = 1;
	public static final int RIGHT = 2;
	public static final int BOTTOM = 3;
	public static final int LEFT = 4;
	
	//holds the value of the side of the room the door is on
	public int side;
	
	//reference to the world controller
	private WorldController worldController;
	
	//holds the reference to the room this door connects to
	private Room linkedRoom;
	
	public Door(TextureRegion img, int side, WorldController wc)
	{
		super(img);
		
		this.side = side;
		worldController = wc;
		
		CircleShape circle = new CircleShape();
		circle.setRadius(Constants.INTERRAD);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.isSensor = true;
		
		body.createFixture(fixtureDef);
		
		circle.dispose();
		
		body.setUserData(this);
		//TODO add sensors to prompt open
	}

	@Override
	public void activate()
	{
		AudioManager.instance.play(Assets.instance.sounds.doorOpen);
		
		if(linkedRoom != null)
			worldController.swapRoom(linkedRoom, this);
		else
			worldController.createNewRoom(this);
	}

	public void setLinkedRoom(Room room)
	{
		linkedRoom = room;
	}
}
