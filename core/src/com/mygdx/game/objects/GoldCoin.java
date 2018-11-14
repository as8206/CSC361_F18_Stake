package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.Room;
import com.mygdx.game.WorldController;

public class GoldCoin extends AbstractGameObject
{
	private Room room;
	private WorldController worldController;

	public GoldCoin(TextureRegion img, Room room, WorldController wc)
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
	}

	@Override
	public void activate()
	{
		System.out.println("Gold Coin touched + 10 gold");
		room.removeCoin(this);
		worldController.addToRemoval(body);
	}
}
