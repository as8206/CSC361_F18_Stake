package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.game.WorldController;
import com.mygdx.game.utils.Constants;

public class Character extends AbstractGameObject
{

	public Character(TextureRegion img)
	{
		super(img);
		
		body.getWorld().destroyBody(body);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(0, 0));
		
		Body tempBody = WorldController.b2dWorld.createBody(bodyDef);
		
		PolygonShape box = new PolygonShape();
		box.setAsBox(0.4f, 0.4f);
		
		tempBody.createFixture(box, 0.0f);
		
		box.dispose();
		
		body = tempBody;
	}
	
	@Override
	public void render (SpriteBatch batch)
	{
		batch.draw(reg, body.getPosition().x - Constants.OFFSET - 0.25f, body.getPosition().y - Constants.OFFSET + 0.1f, 1.5f, 1.5f);
	}

}
