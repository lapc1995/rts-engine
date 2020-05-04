package com.mygdx.engine.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.engine.gamelogic.message.MessageEvent;
import com.mygdx.engine.gamelogic.message.MessageType;

public class MenuUI extends UI{
	
	public final TextButton playButton;
	public final TextButton configButton;
	public final TextButton exitButton;
	
	public MenuUI() {
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));
		skin.add("default", new BitmapFont());
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);
		rootTable.setFillParent(true);
		rootTable.center();
		
		rootTable.row();
		playButton = new TextButton("Play", skin);
		rootTable.add(playButton).spaceBottom(10);
		
		rootTable.row();
		configButton = new TextButton("Settings", skin);
		rootTable.add(configButton).spaceBottom(10);
		
		rootTable.row();
		exitButton = new TextButton("Exit", skin);
		rootTable.add(exitButton).spaceBottom(10);
		
		playButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				sendMessage(MessageType.PLAY);
			}
		});
		
		configButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				sendMessage(MessageType.OPTIONS);
			}
		});
		
		exitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				sendMessage(MessageType.EXIT);
			}
		});
		
	}

	@Override
	public void messageReceived(MessageEvent event) {
		// TODO Auto-generated method stub
		
	}
	
}
