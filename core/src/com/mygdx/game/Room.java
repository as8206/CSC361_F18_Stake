package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.attacks.Attack;
import com.mygdx.game.attacks.AttackEnemy;
import com.mygdx.game.objects.*;
import com.mygdx.game.objects.Character;
import com.mygdx.game.utils.Constants;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Room 
{
	public static final String TAG = Room.class.getName();
		
	public boolean[][] movementGrid;
	
	public enum BLOCK_TYPE
	{
		EMPTY(255,255,255), 			//White
		WALLHOR(0,0,0),
		WALLVERT(20,20,20),
		WALLTOPL(30,30,30),
		WALLTOPR(40,40,40),
		WALLBOTL(50,50,50),
		WALLBOTR(60,60,60),
		WALLENDL(70,70,70),
		WALLENDR(80,80,80),
		WALLENDT(90,90,90),
		WALLENDB(100,100,100),			//shades of gray
		DOORHOR(130,60,0),
		DOORVERT(140,50,0),				//shades of brown
		GTILE(127,255,255),
		GBROKEN1(72,255,255),
		GBROKEN2(0,255,255),			//shades of pale blue
		RUBBLE(255,100,0),
		RUBBLEBIG(255,127,0),			//shades of orange
		LADDERUP(255,0,255),
		LADDERDOWN(200,0,200),			//shades of purple
		CHEST(0,38,255),				//blue
		GOLDCOIN(255,255,0),			//yellow
		PLAYER(0,255,0),				//green
		ERANGED(255,0,0),				//red
		EMELEE(127,0,0);				//dark red
		
		private int color;
		
		private BLOCK_TYPE(int r, int g, int b)
		{
			color = r << 24 | g << 16 | b << 8 | 0xff;
		}
		
		public boolean sameColor(int color)	
		{
			return this.color == color;
		}
		
		public int getColor()
		{
			return color;
		}
	}
	
	//room offset
	public int roomOffsetX;
	public int roomOffsetY;
	
	//objects
	public Array<Wall> walls;
	public Array<Door> doors;
	public Array<Rubble> rubbles;
	public Array<Ladder> ladders;
	public Array<Chest> chests;
	public Array<GoldCoin> coins;
	public Array<EnemyRanged> rangedEnemies;
	public Array<EnemyMelee> meleeEnemies;
	public Character player;
	public Array<Attack> attacks;
	public Array<AttackEnemy> enemyAttacks;
	
	//non-interactable textures
	public Array<Sprite> grounds;
	
	//reference to worldController
	WorldController worldController;
	
	//removal arrays
	public Array<GoldCoin> coinsToBeRemoved;
	public Array<Attack> attacksToBeRemoved;
	public Array<Enemy> enemiesToBeRemoved;
	public Array<AttackEnemy> enemyAttacksToBeRemoved;
	
	public Room (String filename, WorldController wc, int offX, int offY)
	{
		worldController = wc;
		roomOffsetX = offX;
		roomOffsetY = offY;
		init(filename);
	}
	
	private void init(String filename)
	{		
		//objects
		walls = new Array<Wall>();
		doors = new Array<Door>();
		rubbles = new Array<Rubble>();
		ladders = new Array<Ladder>();
		chests = new Array<Chest>();
		coins = new Array<GoldCoin>();
		rangedEnemies = new Array<EnemyRanged>();
		meleeEnemies = new Array<EnemyMelee>();
		attacks = new Array<Attack>();
		enemyAttacks = new Array<AttackEnemy>();
		
		//non-interactable textures
		grounds = new Array<Sprite>();
		
		//removal arrays
		coinsToBeRemoved = new Array<GoldCoin>();
		attacksToBeRemoved = new Array<Attack>();
		enemiesToBeRemoved = new Array<Enemy>();
		enemyAttacksToBeRemoved = new Array<AttackEnemy>();
		
		//load image file that represents the level data
		Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
		
		//grid for ai movement
		//false- nothing on tile, true- tile not passable
		movementGrid = new boolean[pixmap.getWidth()][pixmap.getHeight()];
		
		//scan pixels form top-left to bottom-right
		int lastPixel = -1;
		
		for(int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++)
		{
			for(int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++)
			{
				//preparing a super so that the children can be used as if they were the super
				AbstractGameObject obj = null;
				Sprite spr = null;
				
				//get color of current pixels as 32-bit RGBA value
				int currentPixel = pixmap.getPixel(pixelX, pixelY);
				
				//find matching color value to id the block type
				//create the listed object if there is a match
				
				//empty space
				if(BLOCK_TYPE.EMPTY.sameColor(currentPixel))
				{
					
				}
				//door horizontal
				else if(BLOCK_TYPE.DOORHOR.sameColor(currentPixel))
				{
					if(pixelY == 0)
					{
						obj = new Door(Assets.instance.door.doorHor, Door.TOP, worldController);
					}
					else if (pixelY == pixmap.getHeight()-1)
					{
						obj = new Door(Assets.instance.door.doorHor, Door.BOTTOM, worldController);
					}
					else
					{
						System.out.println("ERROR: door location could not be found, defaulting to bottom");
						obj = new Door(Assets.instance.door.doorHor, Door.BOTTOM, worldController);
					}
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0);
					doors.add((Door)obj);
					
					spr = new Sprite(Assets.instance.tile.tile);
					spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
					spr.setPosition(pixelX - Constants.OFFSET + roomOffsetX, -pixelY - Constants.OFFSET + roomOffsetY); 
					spr.setSize(1, 1);
					
					grounds.add(spr);
					
					movementGrid[pixelX][pixelY] = true;
				}
				//door vertical
				else if(BLOCK_TYPE.DOORVERT.sameColor(currentPixel))
				{
					if(pixelX == 0)
					{
						obj = new Door(Assets.instance.door.doorHor, Door.LEFT, worldController);
					}
					else if(pixelX == pixmap.getWidth()-1)
					{
						obj = new Door(Assets.instance.door.doorHor, Door.RIGHT, worldController);
					}
					else
					{
						System.out.println("ERROR: door location could not be found, defaulting to right");
						obj = new Door(Assets.instance.door.doorHor, Door.RIGHT, worldController);
					}
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0);
					doors.add((Door)obj);
					
					spr = new Sprite(Assets.instance.tile.tile);
					spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
					spr.setPosition(pixelX - Constants.OFFSET + roomOffsetX, -pixelY - Constants.OFFSET + roomOffsetY); 
					spr.setSize(1, 1);
					
					grounds.add(spr);
					
					movementGrid[pixelX][pixelY] = true;
				}
				//wall horizontal
				else if(BLOCK_TYPE.WALLHOR.sameColor(currentPixel))
				{
					obj = new Wall(Assets.instance.wall.wallHorizontal);
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0);
					walls.add((Wall)obj);
					
					movementGrid[pixelX][pixelY] = true;
				}
				//wall vertical
				else if(BLOCK_TYPE.WALLVERT.sameColor(currentPixel))
				{
					obj = new Wall(Assets.instance.wall.wallVertical);
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0);
					walls.add((Wall)obj);
					
					movementGrid[pixelX][pixelY] = true;
				}
				//wall top left corner
				else if(BLOCK_TYPE.WALLTOPL.sameColor(currentPixel))
				{
					obj = new Wall(Assets.instance.wallCorner.topLeft);
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0);
					walls.add((Wall)obj);
					
					movementGrid[pixelX][pixelY] = true;
				}
				//wall top right corner
				else if(BLOCK_TYPE.WALLTOPR.sameColor(currentPixel))
				{
					obj = new Wall(Assets.instance.wallCorner.topRight);
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0);
					walls.add((Wall)obj);
					
					movementGrid[pixelX][pixelY] = true;
				}
				//wall bottom left corner
				else if(BLOCK_TYPE.WALLBOTL.sameColor(currentPixel))
				{
					obj = new Wall(Assets.instance.wallCorner.bottomLeft);
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0);
					walls.add((Wall)obj);
					
					movementGrid[pixelX][pixelY] = true;
				}
				//wall bottom right corner
				else if(BLOCK_TYPE.WALLBOTR.sameColor(currentPixel))
				{
					obj = new Wall(Assets.instance.wallCorner.bottomRight);
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0);
					walls.add((Wall)obj);
					
					movementGrid[pixelX][pixelY] = true;
				}
				//wall end left
				else if(BLOCK_TYPE.WALLENDL.sameColor(currentPixel))
				{
					obj = new Wall(Assets.instance.wallEnd.wallEndLeft);
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0);
					walls.add((Wall)obj);
					
					movementGrid[pixelX][pixelY] = true;
				}
				//wall end right
				else if(BLOCK_TYPE.WALLENDR.sameColor(currentPixel))
				{
					obj = new Wall(Assets.instance.wallEnd.wallEndRight);
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0);
					walls.add((Wall)obj);
					
					movementGrid[pixelX][pixelY] = true;
				}
				//wall end top
				else if(BLOCK_TYPE.WALLENDT.sameColor(currentPixel))
				{
					obj = new Wall(Assets.instance.wallEnd.wallEndTop);
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0);
					walls.add((Wall)obj);
					
					movementGrid[pixelX][pixelY] = true;
				}
				//wall end bottom
				else if(BLOCK_TYPE.WALLENDB.sameColor(currentPixel))
				{
					obj = new Wall(Assets.instance.wallEnd.wallEndBot);
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0);
					walls.add((Wall)obj);
					
					movementGrid[pixelX][pixelY] = true;
				}
				//tile ground texture
				else if(BLOCK_TYPE.GTILE.sameColor(currentPixel))
				{
					spr = new Sprite(Assets.instance.tile.tile);
					spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
					spr.setPosition(pixelX - Constants.OFFSET + roomOffsetX, -pixelY - Constants.OFFSET + roomOffsetY); 
					spr.setSize(1, 1);
					
					grounds.add(spr);
				}
				//tile broken texture 1
				else if(BLOCK_TYPE.GBROKEN1.sameColor(currentPixel))
				{
					spr = new Sprite(Assets.instance.floorBig.floorBig); //TODO change back to floor
					spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
					spr.setPosition(pixelX - Constants.OFFSET + roomOffsetX, -pixelY - Constants.OFFSET + roomOffsetY); 
					spr.setSize(1, 1);
					
					grounds.add(spr);
				}
				//tile broken texture 2
				else if(BLOCK_TYPE.GBROKEN2.sameColor(currentPixel))
				{
					spr = new Sprite(Assets.instance.floorBig.floorBig);
					spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
					spr.setPosition(pixelX - Constants.OFFSET + roomOffsetX, -pixelY - Constants.OFFSET + roomOffsetY); 
					spr.setSize(1, 1);
					
					grounds.add(spr);
				}
				//smaller rubble object
				else if(BLOCK_TYPE.RUBBLE.sameColor(currentPixel))
				{
					obj = new Rubble(Assets.instance.rubble.rubble);
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0);
					rubbles.add((Rubble)obj);
					
					spr = new Sprite(Assets.instance.tile.tile);
					spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
					spr.setPosition(pixelX - Constants.OFFSET + roomOffsetX, -pixelY - Constants.OFFSET + roomOffsetY); 
					spr.setSize(1, 1);
					
					grounds.add(spr); //TODO add random broken floors below rubble
					
					movementGrid[pixelX][pixelY] = true;
				}
				//larger rubble object
				else if(BLOCK_TYPE.RUBBLEBIG.sameColor(currentPixel))
				{
					obj = new Rubble(Assets.instance.rubbleBig.rubbleBig);
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0);
					rubbles.add((Rubble)obj);
					
					spr = new Sprite(Assets.instance.tile.tile);
					spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
					spr.setPosition(pixelX - Constants.OFFSET + roomOffsetX, -pixelY - Constants.OFFSET + roomOffsetY); 
					spr.setSize(1, 1);
					
					grounds.add(spr);
				}
				//ladder up
				//TODO maybe not a static object? maybe can walk through it
				else if(BLOCK_TYPE.LADDERUP.sameColor(currentPixel))
				{
					obj = new Ladder(Assets.instance.ladderUp.ladderUp);
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0);
					ladders.add((Ladder)obj);
					
					spr = new Sprite(Assets.instance.tile.tile);
					spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
					spr.setPosition(pixelX - Constants.OFFSET + roomOffsetX, -pixelY - Constants.OFFSET + roomOffsetY); 
					spr.setSize(1, 1);
					
					grounds.add(spr);
				}
				//ladder down
				//TODO maybe not a static object? maybe can walk through it
				else if(BLOCK_TYPE.LADDERDOWN.sameColor(currentPixel))
				{
					obj = new Ladder(Assets.instance.ladderDown.ladderDown);
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0);
					ladders.add((Ladder)obj);
					
					spr = new Sprite(Assets.instance.tile.tile);
					spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
					spr.setPosition(pixelX - Constants.OFFSET + roomOffsetX, -pixelY - Constants.OFFSET + roomOffsetY); 
					spr.setSize(1, 1);
					
					grounds.add(spr);
				}
				//chest object
				else if(BLOCK_TYPE.CHEST.sameColor(currentPixel))
				{
					obj = new Chest(Assets.instance.chest.chest);
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0);
					chests.add((Chest)obj);
					
					spr = new Sprite(Assets.instance.tile.tile);
					spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
					spr.setPosition(pixelX - Constants.OFFSET + roomOffsetX, -pixelY - Constants.OFFSET + roomOffsetY); 
					spr.setSize(1, 1);
					
					grounds.add(spr);
					
					movementGrid[pixelX][pixelY] = true;
				}
				//gold coin
				else if(BLOCK_TYPE.GOLDCOIN.sameColor(currentPixel))
				{
					obj = new GoldCoin(Assets.instance.goldCoin.goldCoin, this, worldController);
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0);
					coins.add((GoldCoin)obj);
					
					spr = new Sprite(Assets.instance.tile.tile);
					spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
					spr.setPosition(pixelX - Constants.OFFSET + roomOffsetX, -pixelY - Constants.OFFSET + roomOffsetY); 
					spr.setSize(1, 1);
					
					grounds.add(spr);
					
					movementGrid[pixelX][pixelY] = true;
				}
				//player spawn position
				else if(BLOCK_TYPE.PLAYER.sameColor(currentPixel))
				{
					obj = new Character(Assets.instance.character.character);
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0); //TODO make character offset for y a constant, will be used for enemies
					player = (Character)obj;
					
					spr = new Sprite(Assets.instance.tile.tile);
					spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
					spr.setPosition(pixelX - Constants.OFFSET + roomOffsetX, -pixelY - Constants.OFFSET + roomOffsetY); 
					spr.setSize(1, 1);
					
					grounds.add(spr);
				}
				//melee enemy spawn positions
				else if(BLOCK_TYPE.EMELEE.sameColor(currentPixel))
				{
					obj = new EnemyMelee(Assets.instance.barbarian.barbarian, this);
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0); //TODO make character offset for y a constant, will be used for enemies
					meleeEnemies.add((EnemyMelee)obj);
					
					spr = new Sprite(Assets.instance.tile.tile);
					spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
					spr.setPosition(pixelX - Constants.OFFSET + roomOffsetX, -pixelY - Constants.OFFSET + roomOffsetY); 
					spr.setSize(1, 1);
					
					grounds.add(spr);
				}
				//ranged enemy spawn positions
				else if(BLOCK_TYPE.ERANGED.sameColor(currentPixel))
				{
					obj = new EnemyRanged(Assets.instance.goblin.goblin, this);
					obj.body.setTransform(pixelX + roomOffsetX, -pixelY + roomOffsetY, 0); //TODO make character offset for y a constant, will be used for enemies
					rangedEnemies.add((EnemyRanged)obj);
					
					spr = new Sprite(Assets.instance.tile.tile);
					spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight() / 2.0f);
					spr.setPosition(pixelX - Constants.OFFSET + roomOffsetX, -pixelY - Constants.OFFSET + roomOffsetY); 
					spr.setSize(1, 1);
					
					grounds.add(spr);
				}
				else
				{
					int r = 0xff & (currentPixel >>> 24); 	//Red color channel
					int g = 0xff & (currentPixel >>> 16);	//Green color channel
					int b = 0xff & (currentPixel >>> 8);	//Blue color channel
					int a = 0xff & currentPixel;			//alpha channel
					Gdx.app.error(TAG, "Unknown object at x<" + pixelX + "> y<" + pixelY + ">: r<" + r + "> g<" + g + "> b<" + b + "> a<" + a + ">");
				}
				lastPixel = currentPixel;
			}
		}
		
		//Set enemy targets
		for(EnemyRanged enemy : rangedEnemies)
			enemy.setTarget(player);
		for(EnemyMelee enemy : meleeEnemies)
			enemy.setTarget(player);
		
		//free memory
		pixmap.dispose();
		Gdx.app.debug(TAG, "Level '" + filename + "' loaded");
	}
	
	public void render(SpriteBatch batch)
	{			
		//draw ground textures
		for(Sprite ground : grounds)
			ground.draw(batch);
				
		//Draw walls
		for(Wall wall : walls)
			wall.render(batch);	
		
		//Draw doors
		for(Door door : doors)
			door.render(batch);
		
		//draw rubble
		for(Rubble rubble : rubbles)
			rubble.render(batch);
		
		//draw ladders
		for(Ladder ladder : ladders)
			ladder.render(batch);
		
		//draw chests
		for(Chest chest : chests)
			chest.render(batch);
		
		//draw coins
		for(GoldCoin coin : coins)
			coin.render(batch);
		
		//draw ranged enemies
		for(EnemyRanged enemy : rangedEnemies)
			enemy.render(batch);
		
		//draw melee enemies
		for(EnemyMelee enemy : meleeEnemies)
			enemy.render(batch);
		
		player.render(batch);
		
		for(Attack attack : attacks)
			attack.render(batch);
	
		for(AttackEnemy enemyAttack : enemyAttacks)
			enemyAttack.render(batch);
	}
	
	public void update(float deltaTime)
	{
		if(!worldController.debugEnabled)
		{
			//update ranged enemies
			for(EnemyRanged enemy : rangedEnemies)
				enemy.update(deltaTime);
			
			//update melee enemies
			for(EnemyMelee enemy : meleeEnemies)
				enemy.update(deltaTime);
		}
		
		for(Attack attack : attacks)
			attack.update(deltaTime);
		
		player.update(deltaTime);
	}
	
	public void setPlayer(Character player)
	{
		this.player = player;
	}
	
	public void reassignTarget()
	{
		for(EnemyRanged enemy : rangedEnemies)
			enemy.setTarget(player);
		for(EnemyMelee enemy : meleeEnemies)
			enemy.setTarget(player);
	}
	
	public void activateRemoval()
	{
		for(GoldCoin grabbedCoin : coinsToBeRemoved)
		{
			for(GoldCoin coin : coins)
			{
				if(coin == grabbedCoin)
					coins.removeValue(grabbedCoin, false);
			}
			coinsToBeRemoved.removeValue(grabbedCoin, false);
		}
		
		for(Attack attackHit : attacksToBeRemoved)
		{
			for(Attack attack : attacks)
			{
				if(attack == attackHit)
					attacks.removeValue(attackHit, false);
			}
			attacksToBeRemoved.removeValue(attackHit, false);
		}
		
		for(Enemy enemyDead : enemiesToBeRemoved)
		{
			for(EnemyRanged er : rangedEnemies)
				if(enemyDead == er)
					rangedEnemies.removeValue(er, false);
			for(EnemyMelee em : meleeEnemies)
				if(enemyDead == em)
					meleeEnemies.removeValue(em, false);
			enemiesToBeRemoved.removeValue(enemyDead, false);
		}
		
		for(AttackEnemy attackHit : enemyAttacksToBeRemoved)
		{
			for(AttackEnemy attack : enemyAttacks)
			{
				if(attack == attackHit)
					enemyAttacks.removeValue(attackHit, false);
			}
			enemyAttacksToBeRemoved.removeValue(attackHit, false);
		}
	}
	
	public void removeCoin(GoldCoin grabbedCoin)
	{
		coinsToBeRemoved.add(grabbedCoin);
	}
	
	public void removeAttack(Attack attackHit)
	{
		attacksToBeRemoved.add(attackHit);
	}
	
	public void usePlayerAttack(int attackType, float targetX, float targetY)
	{
		Attack tempAttack = null;
		
		if(attackType == 1 && player.cooldown1 == 0)
		{
			tempAttack = new Attack(player.attack1, targetX, targetY, player);
			player.cooldown1 = player.attack1.cooldown;
		}
		if(tempAttack != null)
			attacks.add(tempAttack);
	}

	public void removeEnemy(Enemy enemy) 
	{
		enemiesToBeRemoved.add(enemy);
		worldController.addToRemoval(enemy.body);
	}

	public void addEnemyAttack(AttackEnemy attackEnemy) 
	{
		enemyAttacks.add(attackEnemy);		
	}
	
	public void removeEnemyAttack(AttackEnemy attackEnemy)
	{
		enemyAttacksToBeRemoved.add(attackEnemy);
	}
}
