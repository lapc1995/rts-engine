package com.mygdx.engine.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.engine.core.Engine;

public class DesktopLauncher {
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		try {
			new LwjglApplication(new Engine(), config);

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
}
