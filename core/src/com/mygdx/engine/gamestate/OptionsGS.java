package com.mygdx.engine.gamestate;

import com.mygdx.engine.gamelogic.OptionsGL;
import com.mygdx.engine.renderer.Renderer;
import com.mygdx.engine.renderer.UIRenderer;
import com.mygdx.engine.ui.OptionsUI;

public class OptionsGS implements GameState{
	
	private OptionsUI ui;
	private OptionsGL logic;
	private UIRenderer renderer;
	
	public OptionsGS() {
		this.ui = new OptionsUI();
		this.logic = new OptionsGL();
		ui.addListener(logic);
		logic.addListener(ui);
		this.renderer = new UIRenderer(ui.getStage());
		logic.sendInicialData();
	}

	@Override
	public void update(float dt) {
		ui.update(dt);
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Renderer getRenderer() {
		return renderer;
	}

}
