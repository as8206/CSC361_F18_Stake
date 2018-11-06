package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Wall extends AbstractGameObject
{	
	public Wall(TextureRegion img)
	{
		super(img);
	}
	
	@Override
	public void render(SpriteBatch batch)
	{
		batch.begin();
		batch.draw(reg, body.getPosition().x, body.getPosition().y, 1, 1);	
		batch.end();
	}

}
