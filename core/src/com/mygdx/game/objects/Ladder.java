/**
 * Will hold both types of ladders
 * @author Andrew Stake
 */
package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.utils.Constants;

public class Ladder extends AbstractGameObject
{	
	public Ladder(TextureRegion img)
	{
		super(img);
		
		CircleShape circle = new CircleShape();
		circle.setRadius(Constants.INTERRAD);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.isSensor = true;
		
		body.createFixture(fixtureDef);
		
		body.setUserData(this);
		
		circle.dispose();
	}
	
	public void activate()
	{
		System.out.println("ladder touched");
	}

}
