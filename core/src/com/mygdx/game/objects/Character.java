package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.game.Assets;
import com.mygdx.game.WorldController;
import com.mygdx.game.attacks.Attack;
import com.mygdx.game.attacks.AttackData;
import com.mygdx.game.utils.Constants;

public class Character extends AbstractGameObject
{
	public float movementSpeed = 3.0f;
	
	public float curHealth;
	public float totalHealth;
	
	public AttackData attack1, attack2, attackUlt;
	public float cooldown1, cooldown2, cooldownUlt;
	
	/**
	 * Creates the object for the player character, and changes abstract contructed static body to a dynamic body.
	 * @param img
	 */
	public Character(TextureRegion img)
	{
		super(img);
		
		body.getWorld().destroyBody(body);
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(0, 0));
		
		Body tempBody = WorldController.b2dWorld.createBody(bodyDef);
		
		PolygonShape box = new PolygonShape();
		box.setAsBox(0.4f, 0.4f);
		
		tempBody.createFixture(box, 0.0f);
		
		box.dispose();
		
		PolygonShape hitBox = new PolygonShape();
		hitBox.setAsBox(.5f, .75f, new Vector2(0,.3f), 0);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = hitBox;
		fixtureDef.isSensor = true;
		
		tempBody.createFixture(fixtureDef);
		
		hitBox.dispose();
		
		body = tempBody;
		
		body.setUserData(this);
		
		curHealth = Constants.STARTINGHEALTH;
		totalHealth = curHealth;
		
		attack1 = new AttackData(Assets.instance.attacks.attack1, Constants.ATTACKMAX, Constants.ATTACKMIN, Constants.ATTACKSPEED, Constants.ATTACKSIZE, Constants.COOLDOWN);
	}
	
	@Override
	public void render (SpriteBatch batch)
	{
		if(!mirrored)
			batch.draw(reg, body.getPosition().x - Constants.OFFSET - 0.25f, body.getPosition().y - Constants.OFFSET + 0.1f, 1.5f, 1.5f);
		else
		{
			reg.flip(true, false); //TODO refactor this so its not flipping every time, maybe just a flipped render
			batch.draw(reg, body.getPosition().x - Constants.OFFSET - 0.25f, body.getPosition().y - Constants.OFFSET + 0.1f, 1.5f, 1.5f);
			reg.flip(true, false);
		}
	}
	
	@Override
	public void update(float deltaTime)
	{
		checkCooldowns(deltaTime);
	}

	/**
	 * Checks the cooldowns for attacks and resets them if needed
	 * @param deltaTime
	 */
	private void checkCooldowns(float deltaTime) 
	{
		if(cooldown1 > 0)
			cooldown1 -= deltaTime;
		if(cooldown1 < 0)
			cooldown1 = 0;
		
		if(cooldown2 > 0)
			cooldown2 -= deltaTime;
		if(cooldown2 < 0)
			cooldown2 = 0;

		if(cooldownUlt > 0)
			cooldownUlt -= deltaTime;
		if(cooldownUlt < 0)
			cooldownUlt = 0;		
	}
}
