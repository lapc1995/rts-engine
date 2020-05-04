package com.mygdx.engine.gamelogic;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.mygdx.engine.core.GameStateManagerCommander;
import com.mygdx.engine.core.GameStateType;
import com.mygdx.engine.gamelogic.gameobject.Renderble;
import com.mygdx.engine.gamelogic.message.MessageEvent;

public class MenuGL extends GameLogic {
	
	public MenuGL() {}

	@Override
	public void dispose() {}

	@Override
	public void messageReceived(MessageEvent event) {
		switch(event.getType()) {
		
			case PLAY:
				GameStateManagerCommander.changeState(GameStateType.PLAY);
				break;
				
			case OPTIONS:
				GameStateManagerCommander.changeState(GameStateType.OPTIONS);
				break;
				
			case EXIT:
				Gdx.app.exit();
				
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
