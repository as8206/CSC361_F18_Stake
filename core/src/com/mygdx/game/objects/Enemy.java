package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.game.Room;
import com.mygdx.game.AmarethMain;
import com.mygdx.game.WorldController;
import com.mygdx.game.utils.Constants;

public abstract class Enemy extends AbstractGameObject
{
	//maximum movement speed
	public final float movementSpeed = 3.0f;
	
	//target for attack and pathfinding
	Character target;
	public boolean touchingTarget;
	
	//Level the enemy is a part of
	Room level;
	
	//Health and damage variables
	public float curHealth;
	public float totalHealth;
	protected float damage;
	protected float cooldown;
	
	//Animation
	public float stateTime;
	public Animation<TextureRegion> animation;
	protected Animation<AtlasRegion> walkingAnim;
	protected TextureRegion drawnReg;
	protected boolean standingStill;
	
	/**
	 * Creates the object for the enemy, and changes abstract constructed static body to a dynamic body.
	 * @param img
	 */
	public Enemy(TextureRegion img, Room level)
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
		
		this.level = level;
		
		curHealth = Constants.ENEMYHEALTH;
		totalHealth = curHealth;
		
		cooldown = 0;
	}
	
	@Override
	public void render (SpriteBatch batch)
	{
		if(!standingStill)
		{
			drawnReg = animation.getKeyFrame(stateTime, true);
		}
		
		if(!mirrored)
		{
			batch.draw(drawnReg, body.getPosition().x - Constants.OFFSET - 0.25f, body.getPosition().y - Constants.OFFSET + 0.1f, 1.5f, 1.5f);
		}
		else
		{
			drawnReg.flip(true, false); //TODO refactor this same as in character
			batch.draw(drawnReg, body.getPosition().x - Constants.OFFSET - 0.25f, body.getPosition().y - Constants.OFFSET + 0.1f, 1.5f, 1.5f);
			drawnReg.flip(true, false);
		}
	}
	
	@Override
	public void update(float deltaTime)
	{
		move(deltaTime);
		
		if(cooldown != 0)
		{
			cooldown -= deltaTime;
			if(cooldown < 0)
			{
				cooldown = 0;
			}
		}
		if(cooldown == 0 && touchingTarget)
			performAttack();
		
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
		
		checkDeath();
	}
	
	/**
	 * called with each update
	 * decides if the enemy needs to move or is close enough to stay still
	 * @param deltaTime
	 */
	public void move(float deltaTime)
	{
		if((int)body.getPosition().x != (int)target.body.getPosition().x || (int)body.getPosition().y != (int)target.body.getPosition().y)
		{
			moveTo(target.body.getPosition().x, target.body.getPosition().y);
		}
		else
		{
			body.setLinearVelocity(0,0);
		}
		
		if(body.getLinearVelocity().x >= 0)
			mirror(false);
		else
			mirror(true);
	}
	
	/**
	 * Moves the enemy towards the position passed in
	 * @param posX
	 * @param posY
	 */
	public void moveTo(float posX, float posY)
	{
		float moveX = MathUtils.clamp(posX - body.getPosition().x, -movementSpeed, movementSpeed);
		float moveY = MathUtils.clamp(posY - body.getPosition().y, -movementSpeed, movementSpeed);
		
		body.setLinearVelocity(moveX, moveY);		
	}

	/**
	 * Sets this enemey's target
	 * @param target
	 */
	public void setTarget(Character target)
	{
		this.target = target;
	}
	
	/**
	 * Removes health if hit
	 * @param damage
	 */
	public void takeHit(float damage)
	{
		curHealth-=damage;
	}
	
	public void checkDeath()
	{
		if(curHealth <= 0)
		{
			deathAction();
			level.removeEnemy(this);
		}
	}

	private void deathAction()
	{
		// TODO add death sound and drops
		
	}

	public abstract void performAttack();
	
	public void setAnimation(Animation animation)
	{
		this.animation = animation;
		stateTime = 0;
	}

}
