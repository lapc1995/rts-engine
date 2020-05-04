package com.mygdx.engine.core;

import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.mygdx.engine.gamestate.GameState;

class GameStateManager implements Runnable, Observer{

	private Stack<GameState> stack;
	
	public GameStateManager() {
		stack = new Stack<GameState>();
		GameStateManagerCommander._addObserver(this);
		pushGameState(GameStateType.MENU);
	}
	
	@Override
	public void run() {
		while(true)
			while(!stack.isEmpty())
				stack.peek().update(Gdx.graphics.getDeltaTime());
			
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg0 instanceof GameStateManagerCommander) {
			Object[] flags = (Object[]) arg1;
			GameStateFlag flag = (GameStateFlag) flags[0];
			GameStateType type = (GameStateType) flags[1];
			switch(flag) {
				case PUSH:
					pushGameState(type);
					break;
				case SET:
					setGameState(type);
					break;
				default:
					break;
			}
		}
	}
	
	private void popGameState() {
		if(!stack.isEmpty()) {
			stack.pop();
		}
	}
	
	private void pushGameState(GameStateType gst) {
	
		GameState state = GameStateFactory.createGameState(gst);
		stack.push(state);
	}
	
	private void setGameState(GameStateType gst) {
		while(!stack.isEmpty())
			popGameState();
		pushGameState(gst);
	}
	
	public GameState getCurrentGameState() {
		return stack.peek();
	}
	
	

}
