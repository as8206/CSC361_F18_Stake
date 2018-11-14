package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class GoldCoin extends AbstractGameObject
{

	public GoldCoin(TextureRegion img)
	{
		super(img);
		
		body.destroyFixture(body.getFixtureList().first());
		
		CircleShape circle = new CircleShape();
		circle.setRadius(0.25f);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.isSensor = true;
		
		body.createFixture(fixtureDef);
		
		body.setUserData(this);
		
		circle.dispose();
	}

	@Override
	public void activate()
	{
		System.out.println("Gold Coin touched");
	}
}
