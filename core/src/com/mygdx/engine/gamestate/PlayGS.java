package com.mygdx.engine.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.mygdx.engine.gamelogic.PlayGL;
import com.mygdx.engine.renderer.Renderer;
import com.mygdx.engine.renderer.MouseInput;
import com.mygdx.engine.renderer.UI3DRenderer;
import com.mygdx.engine.ui.PlayUI;

public class PlayGS implements GameState{

	private PlayGL logic;
	private PlayUI ui;
	private UI3DRenderer renderer;
	private MouseInput rendererInput;
	private InputMultiplexer inputMultiplexer;
	
	public PlayGS() {
		this.ui = new PlayUI();
		this.logic = new PlayGL();
		this.renderer = new UI3DRenderer(ui.getStage(), logic);
		
		ui.addListener(logic);
		logic.addListener(ui);
		
		
		rendererInput = new MouseInput(renderer.camera);
		rendererInput.addListener(logic);
		logic.addListener(rendererInput);
		
		logic.init();
		
		this.inputMultiplexer = new InputMultiplexer();
		
		inputMultiplexer.addProcessor(ui.getStage());
		inputMultiplexer.addProcessor(rendererInput);
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		renderer.environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		renderer.environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
	}
	
	@Override
	public void update(float dt) {
		logic.update(dt);
		rendererInput.update(dt);
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
