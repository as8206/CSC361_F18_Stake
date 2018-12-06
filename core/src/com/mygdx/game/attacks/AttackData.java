package com.mygdx.game.attacks;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AttackData
{
	public TextureRegion reg;
	public float damageMax, damageMin, velocity, radius;
	
	/**
	 * Constructor for an object that holds the data for a players attacks
	 * @param img
	 * @param max
	 * @param min
	 * @param speed
	 * @param radius
	 */
	public AttackData(TextureRegion img, float max, float min, float speed, float radius)
	{
		reg = img;
		damageMax = max;
		damageMin = min;
		velocity = speed;
		this.radius = radius;
	}
}
