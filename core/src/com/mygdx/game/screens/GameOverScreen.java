package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.utils.Constants;
import com.mygdx.game.utils.ScoreList;

public class GameOverScreen extends AbstractGameScreen
{
	private int finalScore;

	private Skin skinAmareth;
	private Skin skinLibgdx;
	private Skin skinPixthulhu;
	private Stage stage;
	
	private Button enterButton;
	private Button continueButton;
	private TextField textInput;
	private Label promptText;
	
	private Label gameoverText;
	private Label scoreText;
	private Label highscoreText;

	public GameOverScreen(Game game, int endScore)
	{
		super(game);
		finalScore = endScore;
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
		Table layerInputs = buildInputs();
		Table layerMessage = buildMessage();
		
		//assemble stage for menu screen
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_UI_WIDTH, Constants.VIEWPORT_UI_HEIGHT);
		
		stack.add(layerInputs);
		stack.add(layerMessage);
	}

	private Table buildMessage()
	{
		Table layer = new Table();
		
		//Set layer to the center
		layer.center();
		
		//Add messages
		gameoverText = new Label("GAME OVER", skinPixthulhu);
		gameoverText.setFontScale(3);
		layer.add(gameoverText).row();
		
		scoreText = new Label("Score: " + finalScore, skinPixthulhu);
		scoreText.setFontScale(1.5f);
		layer.add(scoreText).row();

		if(ScoreList.instance.checkScore(finalScore))
		{
			highscoreText = new Label("New Highscore!", skinPixthulhu);
			layer.add(highscoreText);
		}
		layer.padBottom(50);
		
		return layer;
	}

	/**
	 * Builds the layer for the menu's button controls
	 * @return
	 */
	private Table buildInputs()
	{
		Table layer = new Table();

		//Set the layer to centered at the bottom
		layer.center().bottom();
		layer.padBottom(20f);
		
		if(ScoreList.instance.checkScore(finalScore))
		{
			//Add text promt
			promptText = new Label("Enter Name:", skinPixthulhu);
			layer.add(promptText).row();
			
			//Add name input field
			textInput = new TextField("", skinPixthulhu);
			layer.add(textInput).width(600).height(100).row();
			
			// add continue button
			enterButton = new TextButton("Enter and Continue", skinPixthulhu);
			layer.add(enterButton);
			
			//give the continue button something to do
			enterButton.addListener(new ChangeListener()
				{
					@Override
					public void changed(ChangeEvent event, Actor actor)
						{
							newScoreEnter();
						}
				});
		}
		else
		{
			//add continue button
			continueButton = new TextButton("Continue to Menu", skinPixthulhu);
			layer.add(continueButton);
			
			//give continue button something to do
			continueButton.addListener(new ChangeListener()
					{
						@Override
						public void changed(ChangeEvent event, Actor actor)
							{
								game.setScreen(new MenuScreen(game));
							}
					});
		}
		
		return layer;
	}
	
	@Override
	public void render(float deltaTime)
	{
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(deltaTime);
		stage.draw();
	}

	@Override
	public void resize(int width, int height)
	{
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show()
	{
		stage = new Stage(new StretchViewport(Constants.VIEWPORT_UI_WIDTH, Constants.VIEWPORT_UI_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		rebuildStage();		
	}

	@Override
	public void hide()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause()
	{
		// TODO Auto-generated method stub
		
	}
	
	private void newScoreEnter()
	{
		String name = textInput.getText();
		System.out.println(name);
		
		if(name == "")
			return;
		
		ScoreList.instance.insertScore(finalScore, name);
		game.setScreen(new MenuScreen(game));
	}

}
