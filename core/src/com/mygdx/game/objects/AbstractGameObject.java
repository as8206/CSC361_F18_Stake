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

public abstract class AbstractGameObject
{
	public Body body;
	protected TextureRegion reg;
	
	public AbstractGameObject(TextureRegion img)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(new Vector2(0, 0));
		
		body = WorldController.b2dWorld.createBody(bodyDef);
		
		PolygonShape box = new PolygonShape();
		box.setAsBox(0.5f, 0.5f);
		
		body.createFixture(box, 0.0f);
		
		box.dispose();
		
		this.reg = img;
	}
	
	public void update (float deltaTime) {}
	
	public void render (SpriteBatch batch)
	{
		batch.begin();
		batch.draw(reg, body.getPosition().x, body.getPosition().y, 1, 1);	
		batch.end(); //TODO remove batch commands from here and move to location in renderer
	}
}
