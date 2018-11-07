/**
 * Will hold both directions of door objects
 * @author Andrew Stake
 */
package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Door extends AbstractGameObject
{

	public Door(TextureRegion img)
	{
		super(img);
		//TODO add sensors to prompt open
	}

}
