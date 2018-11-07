/**
 * @author Andrew Stake
 */
package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.game.WorldController;

public abstract class AbstractDynamicGameObject
{
	public Body body;
	
	public AbstractDynamicGameObject()
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(0, 0));
		
		body = WorldController.b2dWorld.createBody(bodyDef);
		
		PolygonShape box = new PolygonShape();
		box.setAsBox(0.5f, 0.5f);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = box;
		fixtureDef.density = 1.0f; 
		fixtureDef.friction = 0.2f;
		fixtureDef.restitution = 0.0f;
		
		body.createFixture(fixtureDef);
		
		box.dispose();
	}
	
	public void update (float deltaTime) {}
	
	public abstract void render (SpriteBatch batch);
}