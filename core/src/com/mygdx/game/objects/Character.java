package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Character extends AbstractGameObject
{

	public Character(TextureRegion img)
	{
		super(img);
	}
	
	@Override
	public void render (SpriteBatch batch)
	{
		batch.draw(reg, body.getPosition().x, body.getPosition().y, 1.5f, 1.5f);
	}

}
