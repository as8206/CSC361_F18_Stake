package com.mygdx.game.attacks;

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
	
	/**
	 * Creates the attack object
	 * @param img
	 * @param max
	 * @param min
	 * @param speed
	 * @param radius
	 * @param targetX
	 * @param targetY
	 * @param attacker
	 */
	public Attack(AttackData data, float targetX, float targetY, Character attacker)
	{
		super(data.reg);
		tarX = targetX - Constants.CENTERX;
		tarY = targetY - Constants.CENTERY;
		
//		damageMax = max;
//		damageMin = min;
//		velocity = speed;
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;
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
		//TODO find and set velocity and direction
		body.setTransform(attacker.body.getPosition(), 1);
		
		Vector2 target = findTargetVector();
		
		body.setLinearVelocity(1, 1);
	}
	
	private Vector2 findTargetVector()
	{
		float moveX = MathUtils.clamp(posX - body.getPosition().x, -movementSpeed, movementSpeed);
		float moveY = MathUtils.clamp(posY - body.getPosition().y, -movementSpeed, movementSpeed);
		return null;
	}

	@Override
	public void render(SpriteBatch batch)
	{
		batch.draw(reg, body.getPosition().x - Constants.OFFSET, body.getPosition().y - Constants.OFFSET, 0.5f, 0.5f, 1, 1, 1, 1, rotation, false);
	}
	
	public float findRotation()
	{
//		if(tarX > 0)
//		{
//			rotation = (float) Math.atan((tarY/tarX));
//		}
//		else if(tarX < 0)
//		{
//			rotation = 
//		}
		
		return 0f;
	}
}
