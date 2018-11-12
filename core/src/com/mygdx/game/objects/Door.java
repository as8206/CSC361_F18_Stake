/**
 * Will hold both directions of door objects
 * @author Andrew Stake
 */
package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.utils.Constants;

public class Door extends AbstractGameObject
{
	//Constants for door side
	public static final int TOP = 1;
	public static final int RIGHT = 2;
	public static final int BOTTOM = 3;
	public static final int LEFT = 4;
	
	public int side;
	
	public Door(TextureRegion img/*, int side*/)
	{
		super(img);
		
//		this.side = side;
		//TODO add this back in and fix level
		
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
		System.out.println("door activated");
	}
}
