package com.mygdx.engine.gamelogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.mygdx.engine.core.GameStateManagerCommander;
import com.mygdx.engine.core.GameStateType;
import com.mygdx.engine.core.Settings;
import com.mygdx.engine.gamelogic.gameobject.Renderble;
import com.mygdx.engine.gamelogic.message.MessageData;
import com.mygdx.engine.gamelogic.message.MessageEvent;
import com.mygdx.engine.gamelogic.message.MessageType;

public class OptionsGL extends GameLogic{

	private Map<String, DisplayMode> displayMap;
	
	private String currentDisplay;
	private boolean isFullscreen;
	private boolean isVsyncOn;
	
	private String oldCurrentDisplay;
	private boolean oldIsFullscreen;
	private boolean oldVsyncOn;
	
	public OptionsGL() {
		displayMap = new HashMap<String, DisplayMode>();
		setDisplayModes();
		currentDisplay = Settings.getWidth() + "x" + Settings.getHeight() + " " + Settings.getBitsPerPixel()  + "bits " + Settings.getRefreshRate() + "Hz";
	}
	
	public void sendInicialData() {
		sendDisplays();
		
		Map<MessageData, String> data = new HashMap<MessageData, String>();
		data.put(MessageData.SELECTEDDISPLAY, currentDisplay);
		data.put(MessageData.FULLSCREENON, Boolean.toString(Settings.getFullscreen()));
		data.put(MessageData.VSYNCON, Boolean.toString(Settings.getVsync()));
		sendMessage(MessageType.APPLY, data);
	}
	
	@Override
	public void dispose() {
		
	}
	
	
	private void sendDisplays() {
		for(String dm : displayMap.keySet()) {
			Map<MessageData, String> data = new HashMap<MessageData, String>();
			data.put(MessageData.DISPLAY, dm);
			sendMessage(MessageType.DISPLAY, data);
		}
	}
	
	private void setDisplayModes() {
		DisplayMode[] displayModes = Gdx.graphics.getDisplayModes();
		for(DisplayMode dm : displayModes) {
			String s = dm.width + "x" + dm.height + " " + dm.bitsPerPixel + "bits " + dm.refreshRate + "Hz";
			displayMap.put(s, dm);
		}
	}
	
	private void applySettings(String display, boolean isFullscreen, boolean vsync) {
		//save old settings
		this.oldIsFullscreen = this.isFullscreen;
		this.oldCurrentDisplay = this.currentDisplay;
		this.oldVsyncOn = this.isVsyncOn;	
		
		//settings
		this.isFullscreen = isFullscreen;
		this.currentDisplay = display;
		this.isVsyncOn = vsync;
		
		changeSettings();
	}
	
	private void saveSettings() {
		DisplayMode dm = displayMap.get(currentDisplay);
		Settings.saveSettings(dm, isFullscreen, isVsyncOn);
		this.oldIsFullscreen = this.isFullscreen;
		this.oldCurrentDisplay = this.currentDisplay;
		this.oldVsyncOn = this.isVsyncOn;
	}
	
	private void changeSettings() {	
		DisplayMode dm = displayMap.get(currentDisplay);
		if(isFullscreen)
			Gdx.graphics.setFullscreenMode(dm);
		else {
			Gdx.graphics.setWindowedMode(dm.width, dm.height);
		}
		Gdx.graphics.setVSync(isVsyncOn);
	}
	
	private void revertSettings() {
		this.isFullscreen = this.oldIsFullscreen ;
		this.currentDisplay = this.oldCurrentDisplay ;
		this.isVsyncOn = this.oldVsyncOn;
		
		changeSettings();
	}

	@Override
	public void messageReceived(MessageEvent event) {
		Map<MessageData, String> data = event.getData();
		
		switch(event.getType()) {
		
			case APPLY:
				 applySettings(data.get(MessageData.SELECTEDDISPLAY), Boolean.parseBoolean(data.get(MessageData.FULLSCREENON)), (Boolean.parseBoolean(data.get(MessageData.VSYNCON))));
				 sendMessage(MessageType.RESET);
				 sendMessage(MessageType.ASK);
				 break;
				 
			case SAVE:
				if(Boolean.parseBoolean(data.get(MessageData.SAVEON)))
					saveSettings();
				else {
					revertSettings();
					Map<MessageData, String> data2 = event.getData();
					data2.put(MessageData.SELECTEDDISPLAY, currentDisplay);
					data2.put(MessageData.FULLSCREENON, Boolean.toString(Settings.getFullscreen()));
					data2.put(MessageData.VSYNCON, Boolean.toString(Settings.getVsync()));
					sendMessage(MessageType.APPLY, data2);
					sendMessage(MessageType.RESET);
				}
				break;
				
			case BACK:
				GameStateManagerCommander.changeState(GameStateType.MENU);
				break;
				
			default:
				break;
		
		}
		
	}

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Renderble> getRenderable() {
		// TODO Auto-generated method stub
		return null;
	}



	

}
