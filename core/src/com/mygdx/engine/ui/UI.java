package com.mygdx.engine.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.engine.gamelogic.message.MessageListener;
import com.mygdx.engine.gamelogic.message.MessageSender;

public abstract class UI extends MessageSender implements MessageListener{
	
	private boolean debug;
	protected Stage stage;
	protected Table rootTable;
	protected Skin skin;
	
	public UI() {
		rootTable = new Table();
		stage = new Stage(new ScreenViewport());
		
		skin = new Skin();
		rootTable.setFillParent(true);
		stage.addActor(rootTable);
	}
	
	public void setDebug(boolean b) {
		debug = b;
		rootTable.setDebug(b);
	}
	
	public boolean isDebuging() {
		return debug;
	}
	
	public void setSkin(String path) {
		skin = new Skin(Gdx.files.internal(path));
	}
	
	public Table getTable() {
		return rootTable;
	}
	
	public Skin getSkin() {
		return skin;
	}
	
	public void render() {
		stage.draw();
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}
	
	public void update(float dt) {
		stage.act();
	}
			
}
