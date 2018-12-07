package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Room;
import com.mygdx.game.utils.Constants;

public class EnemyMelee extends Enemy
{

	public EnemyMelee(TextureRegion img, Room level)
	{
		super(img, level);
		damage = Constants.MELEEDAMAGE;
	}

	@Override
	public void performAttack() 
	{
		target.takeHit(damage);
		cooldown = Constants.MELEECOOLDOWN;
	}
}
