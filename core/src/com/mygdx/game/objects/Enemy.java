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
import com.mygdx.game.WorldController;
import com.mygdx.game.utils.Constants;

public abstract class Enemy extends AbstractGameObject
{
	//maximum movement speed
	public final float movementSpeed = 3.0f;
	
	//target for attack and pathfinding
	Character target;
	
	/**
	 * Creates the object for the enemy, and changes abstract contructed static body to a dynamic body.
	 * @param img
	 */
	public Enemy(TextureRegion img)
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
	}
	
	@Override
	public void render (SpriteBatch batch)
	{
		batch.draw(reg, body.getPosition().x - Constants.OFFSET - 0.25f, body.getPosition().y - Constants.OFFSET + 0.1f, 1.5f, 1.5f);
	}
	
	public void moveTo(float posX, float posY)
	{
		float moveX = MathUtils.clamp(posX - body.getPosition().x, -movementSpeed, movementSpeed);
		float moveY = MathUtils.clamp(posY - body.getPosition().y, -movementSpeed, movementSpeed);
		
		body.setLinearVelocity(moveX, moveY);		
	}
	
	public Array<Vector2> findPath()
	{
		Array<Vector2> tempPath = new Array<Vector2>();
		
		int targetX = (int) target.body.getPosition().x;
		int targetY = (int) target.body.getPosition().y;
		int curX = (int) body.getPosition().x;
		int curY = (int) body.getPosition().y;
		int nextX, nextY;
		
		if(targetX != curX && targetY != curY)
		{
			if(Math.abs(targetX - curX) > Math.abs(targetY - curY))
			{
				nextX = curX + (MathUtils.clamp(targetX - curX, -1, 1));
				nextY = curY;
				//check x, then y, then diagonal if both x and y are clear and difference in y is not 0. If none are clear continue to expand out.
				//Once a working move is found, add it to the path plus the next recusive call.
			}
			else
			{
				//same code as above but prioritizing y over x.
			}
		}
		else
		{
			
		}
		
		return null;
	}
	
	public Array<Vector2> findPath(int queryX, int queryY)
	{
		return null;
	}
	
	public void setTarget(Character target)
	{
		this.target = target;
	}

}
