package com.mygdx.engine.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.mygdx.engine.gamelogic.MenuGL;
import com.mygdx.engine.renderer.Renderer;
import com.mygdx.engine.renderer.UIRenderer;
import com.mygdx.engine.ui.MenuUI;


public class MenuGS implements GameState{
	
	private MenuUI ui;
	private MenuGL logic;
	private InputMultiplexer inputMultiplexer;
	private UIRenderer renderer;
	
	public MenuGS() {
		this.ui = new MenuUI();
		this.logic = new MenuGL();
		ui.addListener(logic);
		logic.addListener(ui);

		this.inputMultiplexer = new InputMultiplexer();
		this.renderer = new UIRenderer(ui.getStage());
		
		this.inputMultiplexer.addProcessor(ui.getStage());
		Gdx.input.setInputProcessor(inputMultiplexer);
	}
	
	@Override
	public void update(float dt) {
		logic.update(dt);
	}

	@Override
	public void dispose() {
		ui.dispose();
		logic.dispose();
	}

	@Override
	public Renderer getRenderer() {
		return renderer;
	}
}
