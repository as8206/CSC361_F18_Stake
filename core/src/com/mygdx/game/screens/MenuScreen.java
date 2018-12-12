package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Assets;
import com.mygdx.game.utils.GamePreferences;
import com.mygdx.game.utils.ScoreList;
import com.mygdx.game.utils.AudioManager;
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
	private Skin skinPixthulhu;
	SpriteBatch batch;
	
	private float appearanceTimeClock;
	private final float wedgeTime = 0.5f;
	private final float logoTime = 1f;
	private final float buttonTime = 2f;
	private final float finalTime = 2.1f;
	private boolean wedgeAppeared;
	private boolean logoAppeared;
	private boolean buttonAppeared;
	
	//menu
	private Sprite imgBackground;
	private Sprite wedge;
	private Image imgLogo;
	private TextButton btnMenuPlay;
	private TextButton btnMenuOptions;
	private TextButton btnHighscores;
	
	//debug
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	private boolean debugEnabled = false;
	private float debugRebuildStage;
	
	//options
	private Window winOptions;
	private TextButton btnWinOptSave;
	private TextButton btnWinOptCancel;
	private CheckBox chkSound;
	private Slider sldSound;
	private CheckBox chkMusic;
	private Slider sldMusic;
	private CheckBox chkShowFpsCounter;
	
	//Highscore
	private Window winHighscore;
	private TextButton exitHighscore;
	private Label[] highscores;
	private Label highscoreTitle;
	
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
		skinPixthulhu = new Skin(Gdx.files.internal("../core/assets/ui/pixthulhu-ui.json"),
				new TextureAtlas("../core/assets/ui/pixthulhu-ui.atlas"));
		
		
		//build all layers
		buildBackground();
		Table layerControls = buildControls();
		Table layerLogo = buildLogo();
		Table layerOptionsWindow = buildOptionsWindowLayer();
		Table layerHighscoreWindow = buildHighscoreWindowLayer();
		
		//assemble stage for menu screen
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_UI_WIDTH, Constants.VIEWPORT_UI_HEIGHT);
		
		if(appearanceTimeClock > logoTime)
			stack.add(layerLogo);
		
		if(appearanceTimeClock > buttonTime)
			stack.add(layerControls);
		
		stage.addActor(layerOptionsWindow);
		stage.addActor(layerHighscoreWindow);
	}
	
	private Table buildHighscoreWindowLayer()
	{
		winHighscore = new Window("", skinPixthulhu);
		
		//Build the list and exit button
		winHighscore.add(buildHighscoreLables()).row();
		winHighscore.add(buildHighscoreButton());
		
		//Make window black
		winHighscore.setColor(1, 1, 1, 0.8f);
		
		//Hide by default
		winHighscore.setVisible(false);
		
		if(debugEnabled)
			winHighscore.debug();
		
		winHighscore.setSize(450, 600);
		winHighscore.pack();
		
		//sets the position
		winHighscore.setPosition(Constants.VIEWPORT_UI_WIDTH - winHighscore.getWidth() - 20, 50);
		return winHighscore;
	}

	private Actor buildHighscoreButton()
	{
		Table table = new Table();
		
		table.center().bottom().padBottom(15f).padTop(15f);
		
		//add button
		exitHighscore = new TextButton("Exit", skinPixthulhu);
		table.add(exitHighscore);
		
		//give the exit button something to do
		exitHighscore.addListener(new ChangeListener()
			{
				@Override
				public void changed(ChangeEvent event, Actor actor)
					{
						exitHighscores();
					}
			});
		
		return table;
	}

	private Table buildHighscoreLables()
	{
		Table table = new Table();
		
		table.center().top().padTop(20f);
		
		highscoreTitle = new Label("Highscores", skinPixthulhu);
		highscoreTitle.setFontScale(1.5f);
		table.add(highscoreTitle).padBottom(15f).row();
		
		highscores = new Label[ScoreList.MAXSCORES];
		ScoreList.instance.load();
		
		//creates the labels
		for(int i = 0; i < ScoreList.MAXSCORES; i++)
		{
			highscores[i] = new Label("Score " + (i + 1) + " : " + ScoreList.instance.getName(i) + " - " + ScoreList.instance.getScore(i), skinPixthulhu);
			table.add(highscores[i]).row();
		}
		
		return table;
	}

	/**
	 * Builds the Table for the options window layer of the menu
	 * @return Options Window Layer Table
	 */
	private Table buildOptionsWindowLayer()
	{
		winOptions = new Window("Options", skinLibgdx);
		
		// + Audio Settings: Sound/Music Checkbox and Volume Slider
		winOptions.add(buildOptWinAudioSettings()).row();
		
		// + Debug: Show FPS Counter
		winOptions.add(buildOptWinDebug()).row();
		
		// + Separator and Buttons (Save, Cancel)
		winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);
		
		// Make options window slightly transparent
		winOptions.setColor(1, 1, 1, 0.8f);
		// Hide Options window by default
		winOptions.setVisible(false);
		if (debugEnabled) winOptions.debug();
		// Let TableLayout recalculate widget sizes and positions
		winOptions.pack();
		// Move options window to bottom right corner
		winOptions.setPosition(Constants.VIEWPORT_UI_WIDTH - winOptions.getWidth() - 50, 50);
		return winOptions;
	}
	
	/**
	 * Builds the table for the audio options
	 * @return
	 */
	private Table buildOptWinAudioSettings()
	{
		Table tbl = new Table();
		// + Title: "Audio"
		tbl.pad(10, 10, 0, 10);
		tbl.add(new Label("Audio", skinLibgdx, "default-font", Color.ORANGE)).colspan(3);
		tbl.row();
		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);
		// + Checkbox, "Sound" label, sound volume slider
		chkSound = new CheckBox("", skinLibgdx);
		tbl.add(chkSound);
		tbl.add(new Label("Sound", skinLibgdx));
		sldSound = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
		tbl.add(sldSound);
		tbl.row();
		// + Checkbox, "Music" lable, music volume slider
		chkMusic = new CheckBox("", skinLibgdx);
		tbl.add(chkMusic);
		tbl.add(new Label("Music", skinLibgdx));
		sldMusic = new Slider(0.0f, 0.6f, 0.06f, false, skinLibgdx);
		tbl.add(sldMusic);
		tbl.row();
		return tbl;
	}
	
	/**
	 * Builds the table for the debug options
	 * @return
	 */
	private Table buildOptWinDebug()
	{
		Table tbl = new Table();
		
		// + Title: "Debug"
		tbl.pad(10, 10, 0, 10);
		tbl.add(new Label("Debug", skinLibgdx, "default-font", Color.RED)).colspan(3);
		tbl.row();
		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);
		
		// + Checkbox, "Show FPS Counter" label
		chkShowFpsCounter = new CheckBox("", skinLibgdx);
		tbl.add(new Label("Show FPS Counter", skinLibgdx));
		tbl.add(chkShowFpsCounter);
		tbl.row();
		
		return tbl;
	}
	
	/**
	 * Builds a separator and the save/cancel buttons for the option menu
	 * @return
	 */
	private Table buildOptWinButtons()
	{
		Table tbl = new Table();
		// + Separator
		Label lbl = null;
		lbl = new Label("", skinLibgdx);
		lbl.setColor(0.75f, 0.75f, 0.75f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skinLibgdx.newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
		tbl.row();
		lbl = new Label("", skinLibgdx);
		lbl.setColor(0.5f, 0.5f, 0.5f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skinLibgdx.newDrawable("white");
		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
		tbl.row();
		// + Save Button with event handler
		btnWinOptSave = new TextButton("Save", skinLibgdx);
		tbl.add(btnWinOptSave).padRight(30);
		btnWinOptSave.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor)
			{
				onSaveClicked();
			}
		});
		// + Cancel Button with event handler
		btnWinOptCancel = new TextButton("Cancel", skinLibgdx);
		tbl.add(btnWinOptCancel);
		btnWinOptCancel.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor)
			{
				onCancelClicked();
			}
		});
		return tbl;
	}
	
	/**
	 * Builds the layer for the menu's logo at the top of the screen
	 * @return
	 */
	private Table buildLogo() 
	{
		Table layer = new Table();
		layer.center().top().padTop(50f).padRight(200f);
		
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
		wedge = new Sprite(Assets.instance.UIBackground.wedge);
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
		layer.padBottom(150f).padRight(80f);
		
		// add play button
//		btnMenuPlay = new Button(skinAmareth, "play");
		btnMenuPlay = new TextButton("Play", skinPixthulhu);
		layer.add(btnMenuPlay).padBottom(100f);
		
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
		btnMenuOptions = new TextButton("Options", skinPixthulhu);
		layer.add(btnMenuOptions).padBottom(100f);
		
		//give the options button something to do
		btnMenuOptions.addListener(new ChangeListener()
			{
				@Override
				public void changed(ChangeEvent event, Actor actor)
					{
						onOptionsClicked();
					}
			});
		layer.row();
		
		//add highscores button
		btnHighscores = new TextButton("Highscores", skinPixthulhu);
		layer.add(btnHighscores);
		
		//give the options button something to do
		btnHighscores.addListener(new ChangeListener()
			{
				@Override
				public void changed(ChangeEvent event, Actor actor)
					{
						onHighscoresClicked();
					}
			});
		
		//show debug layer if we are on debug
		if(debugEnabled) 
			layer.debug();
		
		return layer;
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
		
		if(appearanceTimeClock < finalTime)
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
		wedge.draw(batch);
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
		//start music
		AudioManager.instance.play(Assets.instance.music.menuLoop);
		
		batch = new SpriteBatch();
		stage = new Stage(new StretchViewport(Constants.VIEWPORT_UI_WIDTH, Constants.VIEWPORT_UI_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		rebuildStage();
	}
	
	@Override public void hide() 
	{
		AudioManager.instance.stopMusic();
	}
	
	@Override public void pause() {}
	
	/**
	 * Calls the save settings method to retain game settings
	 * then calls the cancel method to exit options menu
	 */
	private void onSaveClicked()
	{
		saveSettings();
		onCancelClicked();
		AudioManager.instance.onSettingsUpdated();
	}
	
	/**
	 * Makes main menu buttons visible and hides options menu
	 */
	private void onCancelClicked()
	{
		btnMenuPlay.setVisible(true);
		btnMenuOptions.setVisible(true);
		btnHighscores.setVisible(true);
		winOptions.setVisible(false);
		AudioManager.instance.onSettingsUpdated();
	}
	
	private void onHighscoresClicked()
	{
		btnMenuPlay.setVisible(false);
		btnMenuOptions.setVisible(false);
		btnHighscores.setVisible(false);
		winHighscore.setVisible(true);
	}
	
	/**
	 * Handles what happens when the options button is clicked
	 */
	private void onOptionsClicked()
	{
		loadSettings();
		btnMenuPlay.setVisible(false);
		btnMenuOptions.setVisible(false);
		btnHighscores.setVisible(false);
		winOptions.setVisible(true);
	}
	
	/**
	 * Handles what happens when the play button is pressed
	 */
	protected void onPlayClicked() 
	{
		game.setScreen(new GameScreen(game));
	}
	
	private void loadSettings()
	{
		GamePreferences prefs = GamePreferences.instance;
		prefs.load();
		chkSound.setChecked(prefs.sound);
		sldSound.setValue(prefs.volSound);
		chkMusic.setChecked(prefs.music);
		sldMusic.setValue(prefs.volMusic);
		chkShowFpsCounter.setChecked(prefs.showFpsCounter);
	}
	
	/**
	 * Saves the current settings to the GamePreferences
	 */
	private void saveSettings()
	{
		GamePreferences prefs = GamePreferences.instance;
		prefs.sound = chkSound.isChecked();
		prefs.volSound = sldSound.getValue();
		prefs.music = chkMusic.isChecked();
		prefs.volMusic = sldMusic.getValue();
		prefs.showFpsCounter = chkShowFpsCounter.isChecked();
		prefs.save();
	}
	
	private void exitHighscores()
	{
		btnMenuPlay.setVisible(true);
		btnMenuOptions.setVisible(true);
		btnHighscores.setVisible(true);
		winHighscore.setVisible(false);
	}
}
