package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.game.Room;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.WorldController;
import com.mygdx.game.utils.Constants;

public abstract class Enemy extends AbstractGameObject
{
	//maximum movement speed
	public final float movementSpeed = 3.0f;
	
	//target for attack and pathfinding
	Character target;
	
	//Level the enemy is a part of
	Room level;
	
	/**
	 * Creates the object for the enemy, and changes abstract contructed static body to a dynamic body.
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
		
		body = tempBody;
		body.setUserData(this);
		
		this.level = level;
	}
	
	@Override
	public void render (SpriteBatch batch)
	{
		if(!mirrored)
		{
			batch.draw(reg, body.getPosition().x - Constants.OFFSET - 0.25f, body.getPosition().y - Constants.OFFSET + 0.1f, 1.5f, 1.5f);
		}
		else
		{
			reg.flip(true, false); //TODO refactor this same as in character
			batch.draw(reg, body.getPosition().x - Constants.OFFSET - 0.25f, body.getPosition().y - Constants.OFFSET + 0.1f, 1.5f, 1.5f);
			reg.flip(true, false);
		}
	}
	
	@Override
	public void update(float deltaTime)
	{
		move(deltaTime);
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
		
		if(body.getLinearVelocity().x >= 0) //TODO figure out why this only works for barbarian
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

}
