package com.mygdx.engine.core;

import java.util.Observable;
import java.util.Observer;

public class GameStateManagerCommander extends Observable{
	
	private static GameStateManagerCommander instance = null;
	
	private static GameStateManagerCommander getInstance() {
		if(instance == null) {
			instance = new GameStateManagerCommander();
	    }
	    return instance;
	}
	
	public static void _addObserver(Observer o) {
		GameStateManagerCommander instance = getInstance();
		instance.addObserver(o);
	}
	
	public static void changeState(GameStateType state) {
		GameStateManagerCommander instance = getInstance();
		Object[] flags = new Object[] {GameStateFlag.SET, state};
		instance.setChanged();
		instance.notifyObservers(flags);
	}
	
	public static void goToState(GameStateType state) {
		GameStateManagerCommander instance = getInstance();
		Object[] flags = new Object[] {GameStateFlag.PUSH, state};
		instance.setChanged();
		instance.notifyObservers(flags);
	}
}
