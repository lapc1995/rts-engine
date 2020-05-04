package com.mygdx.engine.core;

import com.mygdx.engine.gamestate.GameState;
import com.mygdx.engine.gamestate.MenuGS;
import com.mygdx.engine.gamestate.OptionsGS;
import com.mygdx.engine.gamestate.PlayGS;

class GameStateFactory {

	static GameState createGameState(GameStateType gst) {

		switch(gst){
		case MENU:
			return new MenuGS();

		case OPTIONS:
			return new OptionsGS();

		case PLAY:
			return new PlayGS();
			
		default:
			return null;
		}

	}
	
}
