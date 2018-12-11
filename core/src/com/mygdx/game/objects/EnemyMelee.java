package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Assets;
import com.mygdx.game.Room;
import com.mygdx.game.WorldController;
import com.mygdx.game.utils.Constants;

public class EnemyMelee extends Enemy
{

	public EnemyMelee(TextureRegion img, Room level, WorldController wc)
	{
		super(img, level, wc);
		damage = Constants.MELEEDAMAGE;
		
		walkingAnim = Assets.instance.barbarian.animBarbarian;
		drawnReg = reg;
		standingStill = true;
	}

	@Override
	public void performAttack() 
	{
		target.takeHit(damage);
		cooldown = Constants.MELEECOOLDOWN;
	}
}
