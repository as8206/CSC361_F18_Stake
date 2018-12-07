package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
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

	//Animation
	public float stateTime;
	public Animation<TextureRegion> animation;
	private Animation<AtlasRegion> walkingAnim;
	private TextureRegion drawnReg;
	private boolean standingStill;
	
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
	
		walkingAnim = Assets.instance.character.animCharacter;
		drawnReg = reg;
		standingStill = true;
	}
	
	@Override
	public void render (SpriteBatch batch)
	{		
		if(!standingStill)
		{
			drawnReg = animation.getKeyFrame(stateTime, true);
		}
		
		if(!mirrored)
			batch.draw(drawnReg, body.getPosition().x - Constants.OFFSET - 0.25f, body.getPosition().y - Constants.OFFSET + 0.1f, 1.5f, 1.5f);
		else
		{
			drawnReg.flip(true, false); //TODO refactor this so its not flipping every time, maybe just a flipped render
			batch.draw(drawnReg, body.getPosition().x - Constants.OFFSET - 0.25f, body.getPosition().y - Constants.OFFSET + 0.1f, 1.5f, 1.5f);
			drawnReg.flip(true, false);
		}
	}
	
	@Override
	public void update(float deltaTime)
	{
		checkCooldowns(deltaTime);
		stateTime += deltaTime;
		
		if(body.getLinearVelocity().x == 0 && body.getLinearVelocity().y == 0 && standingStill == false)
		{
			drawnReg = reg;
			standingStill = true;
		}
		else if((body.getLinearVelocity().x != 0 || body.getLinearVelocity().y != 0) && standingStill == true)
		{
			setAnimation(walkingAnim);
			standingStill = false;
		}
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

	public void takeHit(float damage) 
	{
		curHealth -= damage;
		if(curHealth < 0)
			curHealth = 0;
	}
	
	public void setAnimation(Animation animation)
	{
		this.animation = animation;
		stateTime = 0;
	}
}
