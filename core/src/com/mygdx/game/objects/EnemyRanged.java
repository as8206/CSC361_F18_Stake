package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.Assets;
import com.mygdx.game.Room;
import com.mygdx.game.WorldController;
import com.mygdx.game.attacks.AttackEnemy;
import com.mygdx.game.utils.Constants;

public class EnemyRanged extends Enemy
{
	private boolean reachedTarget;

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
		standingStill = true; //TODO set in abstract
		reachedTarget = false;
	}

	@Override
	public void performAttack() 
	{
		room.addEnemyAttack(new AttackEnemy(Assets.instance.attacks.arrow, target.body.getPosition().x, target.body.getPosition().y, this));
		cooldown = Constants.RANGEDCOOLDOWN;		
	}

	/**
	 * Moves the enemy towards the position passed in
	 * overridden to stop when in range
	 * @param posX
	 * @param posY
	 */
	public void moveTo(float posX, float posY)
	{
		float moveX, moveY;
		Vector2 distanceToTarget = new Vector2(posX - body.getPosition().x, posY - body.getPosition().y);
		
		if(distanceToTarget.len() < (Constants.RANGEDRANGE - 1.5f))
		{
			reachedTarget = true;
		}
		
		if(distanceToTarget.len() > Constants.RANGEDRANGE)
		{
			reachedTarget = false;
		}
		
		if(reachedTarget)
		{
			moveX = 0;
			moveY = 0;
		}
		else
		{
			moveX = MathUtils.clamp(posX - body.getPosition().x, -movementSpeed, movementSpeed);
			moveY = MathUtils.clamp(posY - body.getPosition().y, -movementSpeed, movementSpeed);
		}
		
		body.setLinearVelocity(moveX, moveY);		
	}
}
