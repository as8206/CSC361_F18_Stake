package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Room;
import com.mygdx.game.WorldController;
import com.mygdx.game.objects.Character.PotionType;

public class Potion extends AbstractCollectedObject
{
	PotionType type;

	public Potion(TextureRegion img, Room room, WorldController wc, PotionType type) 
	{
		super(img, room, wc);

		this.type = type;
	}

	@Override
	public void activate() 
	{
		if(room.player.grabPotion(type))
		{
			room.removeCollectedObject(this);
			worldController.addToRemoval(body);
		}
	}

}
