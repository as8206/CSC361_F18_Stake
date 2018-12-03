package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Assets;
import com.mygdx.game.utils.Constants;

/**
 * Handles the creation of the main menu screen for the game
 */
public class MenuScreen extends AbstractGameScreen
{
	private static final String TAG = MenuScreen.class.getName();
	
	private Stage stage;
	private Skin skinAmareth;
	private Skin skinLibgdx;
	SpriteBatch batch;
	
	private float appearanceTimeClock;
	private final float wedgeTime = 0.5f;
	private final float logoTime = 1f;
	private final float buttonTime = 2f;
	private boolean wedgeAppeared;
	private boolean logoAppeared;
	private boolean buttonAppeared;
	
	//menu
	private Sprite imgBackground;
	private Image imgLogo;
	private Button btnMenuPlay;
	private Button btnMenuOptions;
	
	//debug
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	private boolean debugEnabled = false;
	private float debugRebuildStage;
	
	public MenuScreen (Game game)
	{
		super(game);
		appearanceTimeClock = 0;
	}
	
	public void rebuildStage()
	{
		skinAmareth = new Skin(Gdx.files.internal(Constants.SKIN_AMARETH_UI),
				new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
		skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
				new TextureAtlas(Gdx.files.internal("uiskin.atlas")));
		
		//build all layers
		buildBackground();
		Table layerControls = buildControls();
		Table layerLogo = buildLogo();
//		TODO Table layerOptionsWindow = buildOptionsWindowLayer();
		
		//assemble stage for menu screen
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_UI_WIDTH, Constants.VIEWPORT_UI_HEIGHT);
		
		if(appearanceTimeClock > logoTime)
			stack.add(layerLogo);
		
		if(appearanceTimeClock > buttonTime)
			stack.add(layerControls);
		
//		TODO stage.addActor(layerOptionsWindow);
	}
	
	/**
	 * Builds the layer for the menu's logo at the top of the screen
	 * @return
	 */
	private Table buildLogo() 
	{
		Table layer = new Table();
		layer.center().top();
		
		// + Game Logo
		imgLogo = new Image(skinAmareth, "logo");
		layer.add(imgLogo);
		
		if (debugEnabled) layer.debug();
		return layer;
	}

	/**
	 * Render's the menu's background
	 * @return
	 */
	public void buildBackground()
	{		
		imgBackground = new Sprite(Assets.instance.UIBackground.UIBackground);	
	}
	
	/**
	 * Builds the layer for the menu's button controls
	 * @return
	 */
	public Table buildControls()
	{
		Table layer = new Table();

		//Set the layer to the bottom right
		layer.right().bottom();
		
		// add play button
		btnMenuPlay = new Button(skinAmareth, "play");
		layer.add(btnMenuPlay);
		
		//give the play button something to do
		btnMenuPlay.addListener(new ChangeListener()
			{
				@Override
				public void changed(ChangeEvent event, Actor actor)
					{
						onPlayClicked();
					}
			});
		
		layer.row();
		
		//add options button
		btnMenuOptions = new Button(skinAmareth, "options");
		layer.add(btnMenuOptions);
		
		//give the options button something to do
		btnMenuOptions.addListener(new ChangeListener()
			{
				@Override
				public void changed(ChangeEvent event, Actor actor)
					{
						//TODO add options
					}
			});
		
		//show debug layer if we are on debug
		if(debugEnabled) 
			layer.debug();
		
		return layer;
	}
	
	/**
	 * Handles what happens when the play button is pressed
	 */
	protected void onPlayClicked() 
	{
		game.setScreen(new GameScreen(game));
	}

	/**
	 * Draws the menu screen
	 * @param deltaTime
	 */
	@Override
	public void render (float deltaTime)
	{
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(appearanceTimeClock < 10)
		{
			appearanceTimeClock += deltaTime;
			rebuildStage();
		}
		
		if(debugEnabled)
		{
			debugRebuildStage -= deltaTime;
			if(debugRebuildStage <= 0)
			{
				debugRebuildStage = DEBUG_REBUILD_INTERVAL;
				rebuildStage();
			}
		}
		
		if(appearanceTimeClock > logoTime && !logoAppeared)
		{
			//TODO play particle effect
			logoAppeared = true;
		}
		if(appearanceTimeClock > buttonTime && !buttonAppeared)
		{
			//TODO play particle effect
			buttonAppeared = true;
		}
		//draws the background
		batch.begin();
		imgBackground.draw(batch);
		batch.end();
		
		stage.act(deltaTime);
		stage.draw();
		stage.setDebugAll(debugEnabled); //needs to be variable so debug variable can be used
	}
	
	/**
	 * Updates the size of the game window
	 * @param width
	 * @param heigth
	 */
	@Override public void resize(int width, int heigth)
	{
		stage.getViewport().update(width, heigth, true);
	}
	
	/**
	 * Initializes the stage size and rebuilds the stage
	 */	
	@Override public void show()
	{
		batch = new SpriteBatch();
		stage = new Stage(new StretchViewport(Constants.VIEWPORT_UI_WIDTH, Constants.VIEWPORT_UI_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		rebuildStage();
	}
	@Override public void hide() {}
	@Override public void pause() {}
}
