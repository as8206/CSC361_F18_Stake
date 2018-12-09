package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.Assets;
import com.mygdx.game.Room;
import com.mygdx.game.WorldController;
import com.mygdx.game.utils.AudioManager;
import com.mygdx.game.utils.Constants;

public class GoldCoin extends AbstractGameObject
{
	protected Room room;
	protected WorldController worldController;
	protected boolean collected;

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
		collected = false;
	}

	@Override
	public void activate()
	{	
		if(!collected)
		{
			AudioManager.instance.play(Assets.instance.sounds.coinPickup);
			worldController.addScore(Constants.BASECOINSCORE * worldController.goldModifier);
			room.removeCoin(this);
			worldController.addToRemoval(body);
			System.out.println("Gold Coin touched, +10 gold. Score: " + worldController.getScore());
			
			collected = true;
		}
	}
}
