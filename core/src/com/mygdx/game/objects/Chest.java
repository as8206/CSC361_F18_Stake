/**
 * Holds the chests and the logic to open them
 * @author Andrew Stake
 */
package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.Assets;
import com.mygdx.game.Room;
import com.mygdx.game.WorldController;
import com.mygdx.game.utils.AudioManager;
import com.mygdx.game.utils.Constants;

public class Chest extends AbstractGameObject
{
	private Room room;
	private WorldController worldController;
	
	public Chest(TextureRegion img, Room room, WorldController wc)
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
		
		this.room = room;
		this.worldController = wc;
	}
	
	@Override
	public void activate()
	{
		//Play sound clip
		AudioManager.instance.play(Assets.instance.sounds.openChest);
		
		//Spawn loot
		int randomNumOfCoins = (int) (Math.random() * ((5 - 2) + 1)) + 2;
		GoldCluster tempCluster = new GoldCluster(Assets.instance.goldCoin.goldCluster, room, worldController, randomNumOfCoins);
		tempCluster.body.setTransform(body.getPosition().x, body.getPosition().y, 0);
		room.addCollectedObject(tempCluster);
		
		//Remove the chest
		room.removeChest(this);
		worldController.addToRemoval(body);
	}

}
