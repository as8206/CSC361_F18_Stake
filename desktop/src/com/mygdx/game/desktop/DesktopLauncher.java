package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.AmarethMain;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class DesktopLauncher 
{
	private static boolean rebuildAtlas = true;
	private static boolean drawDebugOutline = false;
	
	public static void main (String[] arg) 
	{
		if (rebuildAtlas) 
	 	{
		 	Settings settings = new Settings();
		 	settings.maxWidth = 1024;
			 settings.maxHeight = 1024;
			 settings.duplicatePadding = true;
			 settings.debug = drawDebugOutline;
			 
			 //Builds game atlas
			 TexturePacker.process(settings, "assets-raw/images","../core/assets","amareth.atlas");
			 
			 //Builds UI atlas
			 TexturePacker.process(settings, "assets-raw/images-ui","../core/assets/ui","amareth-ui.atlas");
		 }
	 		LwjglApplicationConfiguration cfg = new
	 		LwjglApplicationConfiguration();
	 		cfg.title = "Amareth";
	 		cfg.width = 1600;
	 		cfg.height = 960;
	 		new LwjglApplication(new AmarethMain(), cfg);
	 	
	}
}
