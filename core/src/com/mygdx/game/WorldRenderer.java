package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.objects.AbstractGameObject;
import com.mygdx.game.objects.Ladder;
import com.mygdx.game.utils.Constants;
import com.mygdx.game.objects.Character;

public class WorldRenderer implements Disposable
{
	private OrthographicCamera camera;
	private OrthographicCamera cameraUI;
	private SpriteBatch batch;
	private WorldController worldController;
	
	private Box2DDebugRenderer b2debugRenderer;
	private boolean debug = false;
	
	//Variables for printing text messages
	private String text;
	private boolean printText;
	private int timeTracker;
	
	//variables for health bar control
	private int flashTimer;
	private boolean flashOn;
	
	public WorldRenderer (WorldController wc)
	{
		worldController = wc;
		init();
	}
	
	private void init ()
	{
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		
		cameraUI = new OrthographicCamera(Constants.VIEWPORT_UI_WIDTH, Constants.VIEWPORT_UI_HEIGHT);
		cameraUI.position.set(0,0,0);
		cameraUI.setToOrtho(true); // flip y-axis
		cameraUI.update();
		
		b2debugRenderer = new Box2DDebugRenderer();
	}
	
	public void render ()
	{
		if(!worldController.playerIsDead)
		{
			renderLevel();
			renderUI();
		}
	}
	
	private void renderUI()
	{
		batch.setProjectionMatrix(cameraUI.combined);
		batch.begin();
		
		renderScore();
		renderMetrics();
		
		
		if(printText)
			renderText();
		
		batch.end();
	}

	/**
	 * Renders the health bar and the mana bar
	 */
	private void renderMetrics()
	{
		float x = 35;
		float y = 35;
		
		//Render health bar
		float health = worldController.activeRoom.player.curHealth;
		float totalHealth = worldController.activeRoom.player.totalHealth;
		
		if(health <= 0)
		{
			Assets.instance.healthBar.healthBarBackground.draw(batch, x, y, 400, 30);
		}
		else if(health / totalHealth < 0.02f)
		{
			Assets.instance.healthBar.healthBarBackground.draw(batch, x, y, 400, 30);
			if(flashOn)
			{
		        Assets.instance.healthBar.healthBar.draw(batch, x+3, y+3, 0.02f * 394, 24);
		        flashTimer--;
			}
			else
				flashTimer++;
			
			if(flashTimer <= 0)
				flashOn = false;
			else if (flashTimer >= 25)
				flashOn = true;

		}
		else
		{
			Assets.instance.healthBar.healthBarBackground.draw(batch, x, y, 400, 30);
	        Assets.instance.healthBar.healthBar.draw(batch, x+3, y+3, health / totalHealth * 394, 24);
		}
		
		//Render potion inventory
		int numHealthPotions = worldController.activeRoom.player.numHealthPotions;
		int numDamagePotions = worldController.activeRoom.player.numDamagePotions;
		
		batch.draw(Assets.instance.potions.healthPotion, x + 5, y + 40, 25, 25, 50, 50, 1f, 1f, 180);
		Assets.instance.fonts.defaultNormal.draw(batch, "X  " + numHealthPotions, x + 65, y + 55);
		
		batch.draw(Assets.instance.potions.damagePotion, x + 145, y + 40, 25, 25, 50, 50, 1f, 1f, 180);
		Assets.instance.fonts.defaultNormal.draw(batch, "X  " + numDamagePotions, x + 205, y + 55);
		
		if(worldController.activePotion == Character.PotionType.HEALTH)
			batch.draw(Assets.instance.potions.highlight, x + 5, y + 40, 25, 25, 50, 50, 1f, 1f, 180);
		else if(worldController.activePotion == Character.PotionType.DAMAGE)
			batch.draw(Assets.instance.potions.highlight, x + 145, y + 40, 25, 25, 50, 50, 1f, 1f, 180);
			
	}

	private void renderText()
	{
		GlyphLayout layout = new GlyphLayout();
		layout.setText(Assets.instance.fonts.defaultSmall, text);
		
		Assets.instance.fonts.defaultSmall.draw(batch, layout, cameraUI.viewportWidth / 2f - layout.width / 2f, cameraUI.viewportHeight / 2);
	
		timeTracker++;
		if (timeTracker > 100)
			printText = false;
	}
	
	public void prepText(String printout)
	{
		text = printout;
		timeTracker = 0;
		printText = true;
	}

	private void renderScore()
	{
		float x = cameraUI.viewportWidth-200;
		float y = 0;
		
		batch.draw(Assets.instance.goldCoin.goldCoin, x, y, 50, 50, 100, 100, 0.5f, -0.5f, 0);
		Assets.instance.fonts.defaultBig.draw(batch,"" + worldController.getScore(), x+75, y+37);
	}

	/**
	 * Renders the level and all objects within it
	 */
	private void renderLevel()
	{
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.activeRoom.render(batch);
		batch.end();
		
		if(debug)
			b2debugRenderer.render(WorldController.b2dWorld, camera.combined);
	}

	
	public void resize (int width, int height)
	{
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT/height) * width;
		camera.update();
		
		cameraUI.viewportHeight = Constants.VIEWPORT_UI_HEIGHT;
		cameraUI.viewportWidth = Constants.VIEWPORT_UI_HEIGHT / (float)height * (float)width;
		cameraUI.position.set(cameraUI.viewportWidth/2, cameraUI.viewportHeight/2, 0);
		cameraUI.update();
	}
	
	@Override public void dispose()
	{
		batch.dispose();
	}
	
}
