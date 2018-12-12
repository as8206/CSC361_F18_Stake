package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.Assets;
import com.mygdx.game.Room;
import com.mygdx.game.WorldController;
import com.mygdx.game.utils.AudioManager;
import com.mygdx.game.utils.Constants;

public class GoldCoin extends AbstractCollectedObject
{
	public GoldCoin(TextureRegion img, Room room, WorldController wc)
	{
		super(img, room, wc);
	}

	@Override
	public void activate()
	{	
		if(!collected)
		{
			AudioManager.instance.play(Assets.instance.sounds.coinPickup);
			worldController.addScore(Constants.BASECOINSCORE * worldController.goldModifier);
			room.removeCollectedObject(this);
			worldController.addToRemoval(body);
			System.out.println("Gold Coin touched, +10 gold. Score: " + worldController.getScore());
			
			collected = true;
		}
	}
}
