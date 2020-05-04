package com.mygdx.engine.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Graphics.DisplayMode;

public class Settings {
	
	public static void applySavedDisplay() {
		Preferences prefs = Gdx.app.getPreferences("GraphicPrefs");
		if(!prefs.get().isEmpty()) {
			int height = prefs.getInteger("height");
			int width = prefs.getInteger("width");
			int bitsPerPixel = prefs.getInteger("bitsPerPixel");
			int refreshRate = prefs.getInteger("refreshRate");
			Boolean fullscreen = prefs.getBoolean("fullscreen");
			Boolean vsync = prefs.getBoolean("vsync");
			
			if(fullscreen) {
				DisplayMode[] displayModes = Gdx.graphics.getDisplayModes();
				String newDisplay = width + "x" + height + " " + bitsPerPixel + "bits " + refreshRate + "Hz";
				for(DisplayMode dm : displayModes) {
					String s = dm.width + "x" + dm.height + " " + dm.bitsPerPixel + "bits " + dm.refreshRate + "Hz";
					if(s.equals(newDisplay))
						Gdx.graphics.setFullscreenMode(dm);
				}
			}
			else {
				Gdx.graphics.setWindowedMode(width, height);
			}
			
			Gdx.graphics.setVSync(vsync);
		} else {
			setDefaultDisplay();
			applySavedDisplay();
		}
	}
	
	private static void setDefaultDisplay() {
		Preferences prefs = Gdx.app.getPreferences("GraphicPrefs");
		DisplayMode dm = Gdx.graphics.getDisplayMode();
		prefs.putInteger("height", dm.height);
		prefs.putInteger("width", dm.width);
		prefs.putInteger("bitsPerPixel", dm.bitsPerPixel);
		prefs.putInteger("refreshRate", dm.refreshRate);
		prefs.putBoolean("fullscreen", false);
		prefs.getBoolean("vsync", false);
		prefs.flush();
	}

	private static Preferences getPreferences() {
		Preferences prefs = Gdx.app.getPreferences("GraphicPrefs");
		if(prefs.get().isEmpty())
			setDefaultDisplay();
		return prefs;
	}
	
	public static int getHeight() {
		return getPreferences().getInteger("height");
	}
	
	public static int getWidth() {
		return getPreferences().getInteger("width");
	}
	
	public static int getBitsPerPixel() {
		return getPreferences().getInteger("bitsPerPixel");
	}
	
	public static int getRefreshRate() {
		return getPreferences().getInteger("refreshRate");
	}
	
	public static boolean getFullscreen() {
		return getPreferences().getBoolean("fullscreen");
	}

	public static boolean getVsync() {
		return getPreferences().getBoolean("vsync");
	}

	public static void saveSettings(DisplayMode dm, boolean fullscreen, boolean vsync) {
		Preferences prefs = Gdx.app.getPreferences("GraphicPrefs");
		prefs.putInteger("height", dm.height);
		prefs.putInteger("width", dm.width);
		prefs.putInteger("bitsPerPixel", dm.bitsPerPixel);
		prefs.putInteger("refreshRate", dm.refreshRate);
		prefs.putBoolean("fullscreen", fullscreen);
		prefs.putBoolean("vsync", vsync);
		prefs.flush();
	}
}
