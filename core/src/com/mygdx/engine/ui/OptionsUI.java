package com.mygdx.engine.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mygdx.engine.gamelogic.message.MessageData;
import com.mygdx.engine.gamelogic.message.MessageEvent;
import com.mygdx.engine.gamelogic.message.MessageType;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class OptionsUI extends UI{

	private ArrayList<String> dis;
	
	public final SelectBox<String> displays;
	public final TextButton applyButton;
	public final TextButton backButton;
	public final TextButton saveButton;
	public final CheckBox fullscreenButton;
	public final CheckBox vsyncButton;
	public final SaveDialog saveDialog;
	
	public OptionsUI() {
		setDebug(true);
		dis = new ArrayList<String>();
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));
		skin.add("default", new BitmapFont());
		SelectBoxStyle selectBoxStyle = new SelectBoxStyle();
		selectBoxStyle.background= skin.newDrawable("white", Color.DARK_GRAY);
		selectBoxStyle.backgroundOpen = skin.newDrawable("white", Color.DARK_GRAY);
		selectBoxStyle.backgroundOver = skin.newDrawable("white", Color.BLUE);
		selectBoxStyle.font = skin.getFont("default");
		ListStyle ls = new ListStyle();
		ls.selection = skin.newDrawable("white", Color.BLUE);
		ls.background = skin.newDrawable("white", Color.DARK_GRAY);
		ls.font = skin.getFont("default");
		ls.fontColorSelected = Color.BLUE;
		ls.fontColorUnselected = Color.CYAN;
		ScrollPaneStyle sps = new ScrollPaneStyle();
		sps.background = skin.newDrawable("white", Color.DARK_GRAY);
		selectBoxStyle.scrollStyle = sps;
		selectBoxStyle.listStyle = ls;
		skin.add("default", selectBoxStyle);
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);
		CheckBoxStyle cbs = new CheckBoxStyle();
		cbs.font = skin.getFont("default");
		cbs.checkboxOff = skin.newDrawable("white", Color.BLACK);
		cbs.checkboxOn = skin.newDrawable("white", Color.CYAN);
		cbs.checked = skin.newDrawable("white", Color.CYAN);
		
		displays = new SelectBox<String>(selectBoxStyle);
		fullscreenButton = new CheckBox("Fullscreen", cbs);
		vsyncButton = new CheckBox("Vsync", cbs);
		applyButton = new TextButton("Apply", textButtonStyle);
		backButton = new TextButton("Back", textButtonStyle);
		saveButton = new TextButton("Save", textButtonStyle);
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.background = skin.newDrawable("white", Color.BROWN);
		labelStyle.font = skin.getFont("default");
		labelStyle.fontColor = Color.BLUE;
		skin.add("default", labelStyle);
		
		WindowStyle windowStyle = new WindowStyle();
		windowStyle.titleFont = skin.getFont("default");
		windowStyle.titleFontColor = Color.BLUE;
		windowStyle.background = skin.newDrawable("white", Color.DARK_GRAY);
		skin.add("default", windowStyle);
		saveDialog = new SaveDialog("", skin);
		
		addToTable();
		Gdx.input.setInputProcessor(stage);	
		
		applyButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Map<MessageData, String> data = new HashMap<MessageData, String>();
				data.put(MessageData.SELECTEDDISPLAY, displays.getSelected());
				data.put(MessageData.FULLSCREENON, Boolean.toString(fullscreenButton.isChecked()));
				data.put(MessageData.VSYNCON, Boolean.toString(vsyncButton.isChecked()));
				sendMessage(MessageType.APPLY, data);
			}
		});
		
		backButton.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				sendMessage(MessageType.BACK);
			}
			
		});
		
		saveDialog.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Map<MessageData, String> data = new HashMap<MessageData, String>();
				data.put(MessageData.SAVEON, Boolean.toString(saveDialog.result));
				sendMessage(MessageType.SAVE, data);
			}
		});
		
		
		
	}
	
	public void reset() {
		rootTable.clearChildren();
		addToTable();
	}

	private void addToTable() {
		rootTable.setFillParent(true);
		rootTable.top().padTop(Gdx.graphics.getHeight() / 4);
		
		Table table1 = new Table();
		rootTable.add(table1).top();
		table1.add(displays).space(Gdx.graphics.getWidth() / 4);
		
		Table table2 = new Table();
		table1.add(table2);
		table2.add(fullscreenButton).spaceBottom(Gdx.graphics.getWidth() / 24);
		table2.row();
		table2.add(vsyncButton);
		
		rootTable.row();
		Table table3 = new Table();
		rootTable.add(table3);
		table3.add(applyButton).spaceRight(Gdx.graphics.getWidth() / 8);
		
		rootTable.row();
		Table table4 = new Table();
		rootTable.add(table4);
		table4.add(backButton);

	}
	
	public class SaveDialog extends Dialog {

		public boolean result;
		
		public SaveDialog(String title, Skin skin) {
			super(title, skin);
			text("Save new settings?");
			button("Yes", true);
			button("No", false);
		}
		
		@Override
		protected void result(Object object) {
			result = (Boolean) object;
		}
		
	}

	@Override
	public void messageReceived(MessageEvent event) {
		Map<MessageData, String> data = event.getData();
		
		switch(event.getType()) {
		
			case APPLY:
				fullscreenButton.setChecked(Boolean.parseBoolean(data.get(MessageData.FULLSCREENON)));
				vsyncButton.setChecked(Boolean.parseBoolean(data.get(MessageData.VSYNCON)));
				displays.setSelected(data.get(MessageData.SELECTEDDISPLAY));
				break;
		
			case DISPLAY:
				dis.add(data.get(MessageData.DISPLAY));
				displays.setItems(dis.toArray(new String[dis.size()]));
				break;
		
			case RESET:
				reset();
				break;
			
			case ASK:
				saveDialog.show(getStage());
				break;
				
			default:
				break;
		
		}
	}


}
