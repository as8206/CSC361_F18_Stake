package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.Assets;
import com.mygdx.game.Room;
import com.mygdx.game.WorldController;
import com.mygdx.game.attacks.AttackEnemy;
import com.mygdx.game.utils.Constants;

public class EnemyRanged extends Enemy
{

	public EnemyRanged(TextureRegion img, Room level, WorldController wc)
	{
		super(img, level, wc);
		damage = Constants.RANGEDDAMAGE;
		
		CircleShape circle = new CircleShape();
		circle.setRadius(Constants.RANGEDRANGE);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.isSensor = true;
		
		body.createFixture(fixtureDef);
		
		walkingAnim = Assets.instance.goblin.animGoblin;
		drawnReg = reg;
		standingStill = true;
	}

	@Override
	public void performAttack() 
	{
		room.addEnemyAttack(new AttackEnemy(Assets.instance.attacks.arrow, target.body.getPosition().x, target.body.getPosition().y, this));
		cooldown = Constants.RANGEDCOOLDOWN;		
	}

}
