package com.mygdx.engine.gamestate;

import com.mygdx.engine.renderer.Renderer;

public interface GameState {
	
	public abstract void update(float dt);

	public abstract void dispose();

	public abstract Renderer getRenderer();

}
