package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.Room;
import com.mygdx.game.WorldController;

/**
 * Abstract that defines objects that are activated by making contact with a sensor
 * @author Andrew Stake
 *
 */
public abstract class AbstractCollectedObject extends AbstractGameObject
{
	protected Room room;
	protected WorldController worldController;
	protected boolean collected;

	public AbstractCollectedObject(TextureRegion img, Room room, WorldController wc)
	{
		super(img);
		
		body.destroyFixture(body.getFixtureList().first());
		
		CircleShape circle = new CircleShape();
		circle.setRadius(0.3f);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.isSensor = true;
		
		body.createFixture(fixtureDef);
		
		body.setUserData(this);
		
		circle.dispose();
		
		this.room = room;
		this.worldController = wc;
		collected = false;
	}
	
	public abstract void activate();
}
