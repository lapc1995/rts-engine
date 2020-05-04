package com.mygdx.engine.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

public class Engine implements ApplicationListener{
 
	private GameStateManager gsm;
	
	@Override
	public void create() {
		System.out.println("dddd");
		//Settings.applySavedDisplay();
		System.out.println("ssss");
		gsm = new GameStateManager();
		
	}

	@Override
	public void resize(int width, int height) {
		gsm.getCurrentGameState().getRenderer().resize(width, height);
	}

	@Override
	public void render() {
		if(gsm.getCurrentGameState() != null && gsm.getCurrentGameState().getRenderer() != null) {
			gsm.getCurrentGameState().update(Gdx.graphics.getDeltaTime());
			gsm.getCurrentGameState().getRenderer().render();	
		}
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
