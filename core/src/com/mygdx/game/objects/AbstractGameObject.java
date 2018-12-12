/**
 * @author Andrew Stake
 */
package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.game.WorldController;
import com.mygdx.game.utils.Constants;

public abstract class AbstractGameObject
{
	public Body body;
	protected TextureRegion reg;
	public boolean mirrored;
	private boolean activated;
	
	/**
	 * Provides a default box2d body for the object if a differencet constructor is not used.
	 * @param img
	 */
	public AbstractGameObject(TextureRegion img)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(new Vector2(0, 0));
		
		body = WorldController.b2dWorld.createBody(bodyDef);
		
		PolygonShape box = new PolygonShape();
		box.setAsBox(0.5f, 0.5f);
		
		body.createFixture(box, 0.0f);
		body.setUserData(this);
		
		box.dispose();
		
		this.reg = img;
		activated = false;
	}
	
	public void update (float deltaTime) {}
	
	public void tryActivation()
	{
		if(!activated)
		{
			activated = true;
			activate();
		}
	}
	public void activate() {}
	
	/**
	 * Draw the object with default parameters
	 * @param batch
	 */
	public void render (SpriteBatch batch)
	{
		batch.draw(reg, body.getPosition().x - Constants.OFFSET , body.getPosition().y - Constants.OFFSET, 1, 1);	
	}
	
	/**
	 * Mirrors the image, true: left (flipped), false: right (default direction)
	 * @param direction
	 */
	public void mirror(boolean direction) //TODO figure out why this persists after a reset
	{
		if(direction != mirrored)
			mirrored = direction;
	}
}
