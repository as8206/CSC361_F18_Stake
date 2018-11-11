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
import com.mygdx.game.Level;
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
	Level level;
	
	//Keeps track of time for pathfinding
	float timeKeeper = 0;
	
	//current path and step
	private Array<Vector2> path;
	private Vector2 step;
	private boolean pathReset;
	
	/**
	 * Creates the object for the enemy, and changes abstract contructed static body to a dynamic body.
	 * @param img
	 */
	public Enemy(TextureRegion img, Level level)
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
		
		this.level = level;
	}
	
	public void initStep()
	{
		step = new Vector2(body.getPosition().x, body.getPosition().y);
	}
	
	@Override
	public void render (SpriteBatch batch)
	{
		batch.draw(reg, body.getPosition().x - Constants.OFFSET - 0.25f, body.getPosition().y - Constants.OFFSET + 0.1f, 1.5f, 1.5f);
	}
	
	@Override
	public void update(float deltaTime)
	{
		move(deltaTime);
	}
	
	public void move(float deltaTime)
	{
		timeKeeper += deltaTime;
		
		if(timeKeeper > .5)
		{
			path = findPath(body.getPosition());
			pathReset = true;
			timeKeeper = 0;
		}
		
		if(pathReset)
		{
			step = path.pop();
			System.out.println(this + " Reset: x: " + step.x + " y: " + step.y);
			pathReset = false;
		}
		
		if(path.size != 0 && step.x == (int)body.getPosition().x && step.y == (int) body.getPosition().y)
		{
			step = path.pop();
			System.out.println(this + " Reached: x: " + step.x + " y: " + step.y);
		}
//		else
//		{
//			System.out.println("size: " + path.size);
//		}
		
//		if((int)body.getPosition().x != step.x || (int)body.getPosition().y != step.y)
//		{
//			moveTo(step.x, step.y);
//		}
		moveTo(step.x, step.y);
	}
	
	public void moveTo(float posX, float posY)
	{
		float moveX = MathUtils.clamp(posX - body.getPosition().x, -movementSpeed, movementSpeed);
		float moveY = MathUtils.clamp(posY - body.getPosition().y, -movementSpeed, movementSpeed);
		
		body.setLinearVelocity(moveX, moveY);		
	}
	
	public Array<Vector2> findPath(Vector2 position)
	{
		Array<Vector2> tempPath = new Array<Vector2>();
		
		int targetX = (int) target.body.getPosition().x;
		int targetY = (int) target.body.getPosition().y;
		int curX = (int) position.x;
		int curY = (int) position.y;
		int potentialX, potentialY;
		Vector2 nextMove;
		
		if(targetX != curX && targetY != curY)
		{
			if(Math.abs(targetX - curX) > Math.abs(targetY - curY))
			{
				//Checks the direct x move
				potentialX = curX + (MathUtils.clamp(targetX - curX, -1, 1));
				potentialY = curY;
				
				if(!level.movementGrid[potentialX][-potentialY])
				{
					nextMove = new Vector2(potentialX, potentialY);
					
					//Checks the direct y move
					potentialX = curX;
					potentialY = curY + (MathUtils.clamp(targetY - curY, -1, 1));
					if(!level.movementGrid[potentialX][-potentialY])
					{
						//Checks the diagonal
						potentialX = curX + (MathUtils.clamp(targetX - curX, -1, 1));
						potentialY = curY + (MathUtils.clamp(targetY - curY, -1, 1));
						if(!level.movementGrid[potentialX][-potentialY])
						{
							nextMove.set(new Vector2(potentialX, potentialY));
						}

					}
					
					tempPath.addAll(findPath(nextMove));
					tempPath.add(nextMove);
					return tempPath;
				}
				
				//Checks the direct y move
				potentialX = curX;
				potentialY = curY + (MathUtils.clamp(targetY - curY, -1, 1));
				if(!level.movementGrid[potentialX][-potentialY])
				{
					nextMove = new Vector2(potentialX, potentialY);
					
					tempPath.addAll(findPath(nextMove));
					tempPath.add(nextMove);
					return tempPath;
				}
				
				//Checks the reverse x move
				potentialX = curX - (MathUtils.clamp(targetX - curX, -1, 1));
				potentialY = curY;
				
				if(!level.movementGrid[potentialX][-potentialY])
				{
					nextMove = new Vector2(potentialX, potentialY);
					
					tempPath.addAll(findPath(nextMove));
					tempPath.add(nextMove);
					return tempPath;
				}
				
				//Checks the reverse y move
				potentialX = curX;
				potentialY = curY - (MathUtils.clamp(targetY - curY, -1, 1));
				if(!level.movementGrid[potentialX][-potentialY])
				{
					nextMove = new Vector2(potentialX, potentialY);
					
					tempPath.addAll(findPath(nextMove));
					tempPath.add(nextMove);
					return tempPath;
				}
				
					
				//check x, then y, then diagonal if both x and y are clear and difference in y is not 0. If none are clear continue to expand out.
				//Once a working move is found, add it to the path plus the next recusive call.
			}
			else
			{
				//Checks the direct y move
				potentialX = curX;
				potentialY = curY + (MathUtils.clamp(targetY - curY, -1, 1));
				
				if(!level.movementGrid[potentialX][-potentialY])
				{
					nextMove = new Vector2(potentialX, potentialY);
					
					//Checks the direct x move
					potentialX = curX + (MathUtils.clamp(targetX - curX, -1, 1));
					potentialY = curY;
					if(!level.movementGrid[potentialX][-potentialY])
					{
						//Checks the diagonal
						potentialX = curX + (MathUtils.clamp(targetX - curX, -1, 1));
						potentialY = curY + (MathUtils.clamp(targetY - curY, -1, 1));
						if(!level.movementGrid[potentialX][-potentialY])
						{
							nextMove.set(new Vector2(potentialX, potentialY));
						}

					}
					
					tempPath.addAll(findPath(nextMove));
					tempPath.add(nextMove);
					return tempPath;
				}
				
				//Checks the direct x move
				potentialX = curX + (MathUtils.clamp(targetX - curX, -1, 1));
				potentialY = curY;
				
				if(!level.movementGrid[potentialX][-potentialY])
				{
					nextMove = new Vector2(potentialX, potentialY);
					
					tempPath.addAll(findPath(nextMove));
					tempPath.add(nextMove);
					return tempPath;
				}
				
				//Checks the reverse y move
				potentialX = curX;
				potentialY = curY - (MathUtils.clamp(targetY - curY, -1, 1));
				if(!level.movementGrid[potentialX][-potentialY])
				{
					nextMove = new Vector2(potentialX, potentialY);
					
					tempPath.addAll(findPath(nextMove));
					tempPath.add(nextMove);
					return tempPath;
				}
				
				//Checks the reverse x move
				potentialX = curX - (MathUtils.clamp(targetX - curX, -1, 1));
				potentialY = curY;
				
				if(!level.movementGrid[potentialX][-potentialY])
				{
					nextMove = new Vector2(potentialX, potentialY);
					
					tempPath.addAll(findPath(nextMove));
					tempPath.add(nextMove);
					return tempPath;
				}
			}
		}
		
		nextMove = new Vector2(curX, curY);
		tempPath.add(nextMove);
		return tempPath;
	}
	
	public void setTarget(Character target)
	{
		this.target = target;
		path = findPath(body.getPosition());
	}

}
