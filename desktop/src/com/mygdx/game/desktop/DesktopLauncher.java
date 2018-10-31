package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class DesktopLauncher 
{
	private static boolean rebuildAtlas = true;
	private static boolean drawDebugOutline = true;
	
	public static void main (String[] arg) 
	{
		if (rebuildAtlas) 
	 	{
		 	Settings settings = new Settings();
		 	settings.maxWidth = 1024;
			 settings.maxHeight = 1024;
			 settings.duplicatePadding = false;
			 settings.debug = drawDebugOutline;
			 TexturePacker.process(settings, "assets-raw/images","../core/assets","amareth.atlas");
		 }
	 		LwjglApplicationConfiguration cfg = new
	 		LwjglApplicationConfiguration();
	 		cfg.title = "CanyonBunny";
	 		cfg.width = 800;
	 		cfg.height = 480;
	 		new LwjglApplication(new MyGdxGame(), cfg);
	 	
	}
}
