package com.mygdx.game.attacks;

import java.sql.Date;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.game.WorldController;
import com.mygdx.game.objects.AbstractGameObject;
import com.mygdx.game.utils.Constants;
import com.mygdx.game.objects.Character;

public class Attack extends AbstractGameObject
{
//	private float damageMax, damageMin, velocity;
	private AttackData data;
	private float rotation;
	private float tarX;
	private float tarY;
	private Vector2 target;
	
	/**
	 * Creates the attack object
	 * @param data
	 * @param targetX
	 * @param targetY
	 * @param attacker
	 */
	public Attack(AttackData data, float targetX, float targetY, Character attacker)
	{
		super(data.reg);
		body.getWorld().destroyBody(body);
		
		tarX = targetX - Constants.CENTERX;
		tarY = targetY - Constants.CENTERY;
		tarY *= -1;
		this.data = data;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(0, 0));
		
		body = WorldController.b2dWorld.createBody(bodyDef);
		
		CircleShape circle = new CircleShape();
		circle.setRadius(data.radius);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.isSensor = true;
		
		body.createFixture(fixtureDef);
		
		body.setUserData(this);
		
		circle.dispose();
		
		rotation = findRotation();
		target = findTarget();
		
		body.setTransform(attacker.body.getPosition(), 1);
		
		body.setLinearVelocity(target);
	}
	
	/**
	 * Finds the velocity vector for the projectile
	 * @return
	 */
	private Vector2 findTarget() 
	{
		Vector2 tempVector = new Vector2(1,1);
		tempVector.setLength(data.velocity);
		tempVector.setAngle(rotation + 90);
		return tempVector;
	}

	@Override
	public void render(SpriteBatch batch)
	{
		batch.draw(reg, body.getPosition().x - Constants.OFFSET, body.getPosition().y - Constants.OFFSET, 0.5f, 0.5f, 1, 1, 1, 1, rotation, false);
	}
	
	/**
	 * Finds the angle of rotation for the projectile
	 * @return
	 */
	private float findRotation()
	{
		//conditionals for all quadrants and axis
		if(tarX > 0 && tarY > 0)
		{
			rotation = (float) Math.atan((tarY/tarX));
			rotation = (float) ((rotation*180)/Math.PI);
		}
		else if(tarX < 0 && tarY > 0)
		{
			rotation = (float) Math.atan((tarY/tarX));
			rotation = (float) ((rotation*180)/Math.PI);
			rotation = 180 - Math.abs(rotation);
		}
		else if(tarX < 0 && tarY < 0)
		{
			rotation = (float) Math.atan((tarY/tarX));
			rotation = (float) ((rotation*180)/Math.PI);
			rotation = 180 + Math.abs(rotation);
		}
		else if(tarX > 0 && tarY < 0)
		{
			rotation = (float) Math.atan((tarY/tarX));
			rotation = (float) ((rotation*180)/Math.PI);
			rotation = 90 - Math.abs(rotation);
			rotation = 270 + rotation;
		}
		else if(tarX > 0 && tarY == 0)
			rotation = 0;
		else if(tarX == 0 && tarY > 0)
			rotation = 90;
		else if(tarX < 0 && tarY == 0)
			rotation =  180;
		else if(tarX == 0 && tarY < 0)
			rotation = 270;
		
		return (rotation - 90);
	}

	public float genDamage() 
	{
		return (float) ((Math.random() * (data.damageMax - data.damageMin)) + data.damageMin);
	}
}
