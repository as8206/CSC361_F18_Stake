package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
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
import com.mygdx.game.utils.AudioManager;
import com.mygdx.game.utils.Constants;

public class Character extends AbstractGameObject
{
	public enum PotionType 
	{
		HEALTH, DAMAGE;
	}

	public float movementSpeed = 3.0f;
	
	private WorldController worldController;
	
	//health
	public float curHealth;
	public float totalHealth;
	
	//attacks
	public AttackData attack1, attack2, attackUlt;
	public float cooldown1, cooldown2, cooldownUlt;
	public float damageModifier;

	//Animation
	public float stateTime;
	public Animation<TextureRegion> animation;
	private Animation<AtlasRegion> walkingAnim;
	private TextureRegion drawnReg;
	private boolean standingStill;
	
	//inventory
	public int numHealthPotions;
	public int numDamagePotions;
	
	/**
	 * Creates the object for the player character, and changes abstract contructed static body to a dynamic body.
	 * @param img
	 */
	public Character(TextureRegion img, WorldController wc)
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
		
		initAttacks();
		damageModifier = 1;
	
		walkingAnim = Assets.instance.character.animCharacter;
		drawnReg = reg;
		standingStill = true;
		
		numHealthPotions = 0;
		numDamagePotions = 0;
		
		worldController = wc;
	}
	
	/**
	 * Prepares the attack data objects for the default attacks
	 */
	private void initAttacks() 
	{
		//attack 1
		attack1 = new AttackData(Assets.instance.attacks.attack1, Constants.ATTACKMAX, Constants.ATTACKMIN, Constants.ATTACKSPEED, Constants.ATTACKSIZE, Constants.COOLDOWN, "../core/assets/particles/fireballEffect.pfx", "../core/assets/particles", Assets.instance.sounds.fireballCast);		
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

	/**
	 * Applies damage to the player
	 * @param damage
	 */
	public void takeHit(float damage) 
	{
		AudioManager.instance.play(Assets.instance.sounds.hitTaken);
		
		curHealth -= damage;
		if(curHealth < 0)
			curHealth = 0;
	}
	
	/**
	 * Given the potion type, will add the potion to inventory if the player has space
	 * @param type
	 * @return
	 */
	public boolean grabPotion(PotionType type)
	{
		if(type == PotionType.HEALTH)
		{
			if(numHealthPotions < Constants.MAXHEALTHPOTIONS)
			{
				numHealthPotions++;
				return true;
			}
			else
			{
				worldController.prepText("Health Potions Full");
				return false;
			}
				
		}
		else if(type == PotionType.DAMAGE)
		{
			if(numDamagePotions < Constants.MAXDAMAGEPOTIONS)
			{
				numDamagePotions++;
				return true;
			}
			else
			{
				worldController.prepText("Damage Increase Potions Full");
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Uses a potion based on its type and the amount the player has
	 * @param type
	 */
	public void usePotion(PotionType type)
	{
		if(type == PotionType.HEALTH)
		{
			if(numHealthPotions > 0)
			{
				if(curHealth == totalHealth)
				{
					worldController.prepText("Health Full");
					return;
				}
				curHealth += Constants.HEALTHPOTIONHEALING;
				if(curHealth > totalHealth)
					curHealth = totalHealth;
				numHealthPotions--;
				//TODO add sound effect
			}
			else
				worldController.prepText("No Health Potions");
		}
		else if(type == PotionType.DAMAGE)
		{
			if(numDamagePotions > 0)
			{
				worldController.prepText("Would use damage Potion");
				numDamagePotions--;
			}
			else
				worldController.prepText("No Damage Increase Potions");
		}
	}
	
	public void setAnimation(Animation animation)
	{
		this.animation = animation;
		stateTime = 0;
	}
}
