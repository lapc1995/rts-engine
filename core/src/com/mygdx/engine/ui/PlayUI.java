package com.mygdx.engine.ui;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.mygdx.engine.core.Settings;
import com.mygdx.engine.gamelogic.message.MessageData;
import com.mygdx.engine.gamelogic.message.MessageEvent;
import com.mygdx.engine.gamelogic.message.MessageType;

public class PlayUI extends UI{
	
	public final TextButton deleteButton;
	private Label woodAmount;
	private Label foodAmount;
	private Label stoneAmount;
	private Label goldAmount;
	
	private Table buttonsTable;
	private Table infoTable;
	private Table tableRight;
	
	private Label objectName;
	private Label currentHealth;
	private Label totalHealth;
	private Label nationName;
	private Label playerName;
	private ProgressBar lifeBar;
	private Image objectImage;
	private Label relationState;
	
	private Label resourceType;
	private Label resourceAmount;
	
	private Table  infoLeft;
	private Table infoRight;
	
	private TextButton[] buttons;
	
	private Table infoRightQueue;
	private Label queue;
	private Label queueCurrent;
	private ProgressBar currentProgress;
	
	private int tableWidth;
	private int tableHeight;
	
	public PlayUI() {
		
		rootTable.debugAll();
		
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
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = skin.getFont("default");
		labelStyle.fontColor = Color.WHITE;
		
		ProgressBarStyle progressBarStyle = new ProgressBarStyle();
		progressBarStyle.knobAfter = skin.newDrawable("white", Color.RED);
		progressBarStyle.knobBefore = skin.newDrawable("white", Color.GREEN);
		
		buttonsTable = new Table();
		infoTable = new Table();
		infoTable.background(skin.newDrawable("white", Color.DARK_GRAY));
		tableRight = new Table();
		
		deleteButton = new TextButton("Delete", textButtonStyle);
		
		tableWidth = Settings.getWidth() / 3;
		tableHeight = (int) (Settings.getHeight() * 0.2);
		
		//info Table contents
		infoLeft = new Table();
		infoRight = new Table();
		infoLeft.setVisible(false);
		infoRight.setVisible(false);
		
		objectName = new Label("objectName", labelStyle);
		currentHealth = new Label("currentHealth", labelStyle);
		totalHealth = new Label("totalHealth", labelStyle);
		lifeBar = new ProgressBar(0, 100, 1, false, progressBarStyle);
		lifeBar.setValue(50);
		
		Texture texture = new Texture(Gdx.files.internal("icontest.png"));
		objectImage = new Image(texture);
		objectImage.setSize(texture.getWidth()/2,texture.getHeight()/2);
		
		nationName = new Label("nationName", labelStyle);
		playerName = new Label("playerName", labelStyle);
		relationState = new Label("relationState", labelStyle);
		
		resourceType = new Label("resourceType", labelStyle);
		resourceAmount = new Label("resourceAmount", labelStyle);
		
		//button Table contents
		buttons = new TextButton[16];
		
		for(int i = 0; i < buttons.length; i++) {
			buttons[i] = new TextButton(Integer.toString(i), textButtonStyle);
			buttons[i].setVisible(false);
		}
		
		
		//queue
		infoRightQueue = new Table();
		queue = new Label("queue", labelStyle);
		queueCurrent = new Label("queue", labelStyle);
		currentProgress = new ProgressBar(0, 100, 1, false, progressBarStyle);
		
		
		rootTable.bottom().left();
		
		//info table posing
		infoTable.top().left();
		infoTable.add(infoLeft);
		
		infoLeft.add(objectName);
		infoLeft.row();
		infoLeft.add(currentHealth);
		infoLeft.row();
		infoLeft.add(totalHealth);
		infoLeft.row();
		infoLeft.add(lifeBar);
		infoLeft.row();
		infoLeft.add(objectImage).prefSize(100);
		infoLeft.row();
		infoLeft.add(resourceType);
		infoLeft.add(resourceAmount);
		/*
		//infoTable.add(infoRight);
		infoRight.padLeft(100f);
		infoRight.add(nationName);
		infoRight.row();
		infoRight.add(playerName);
		infoRight.row();
		infoRight.add(relationState);
		*/
		//buttons table posing
		for(int i = 0; i < buttons.length; i++) {
			if(i % 4 == 0)
				buttonsTable.row();
			buttonsTable.add(buttons[i]).prefSize(50);
		}
		
		int tableWidth = Settings.getWidth() / 3;
		int tableHeight = (int) (Settings.getHeight() * 0.2);
		
		/*
		infoRightQueue.add(queueCurrent);
		infoRightQueue.add(currentProgress);
		infoRightQueue.row();
		infoRightQueue.add(queue);
		infoRightQueue.setVisible(false);
		*/
		rootTable.add(buttonsTable).minSize(tableWidth, tableHeight);
		rootTable.add(infoTable).minSize(tableWidth, tableHeight);
		rootTable.add(tableRight).minSize(tableWidth, tableHeight);

		woodAmount = new Label("0w", labelStyle);
		foodAmount = new Label("0f", labelStyle);
		stoneAmount = new Label("0s", labelStyle);
		goldAmount = new Label("0g", labelStyle);
		tableRight.add(woodAmount);
		tableRight.add(foodAmount);
		tableRight.add(stoneAmount);
		tableRight.add(goldAmount);
	}

	@Override
	public void messageReceived(MessageEvent event) {
		Map<MessageData, String> data = event.getData();
		
		switch(event.getType()) {
		
			case PLAYERDATA:
				foodAmount.setText(data.get(MessageData.FOOD));
				woodAmount.setText(data.get(MessageData.WOOD));
				stoneAmount.setText(data.get(MessageData.STONE));
				goldAmount.setText(data.get(MessageData.GOLD));
				break;
				
			case SELECTEDDATA:
				disableButtons();
				objectName.setText(data.get(MessageData.NAME));
				totalHealth.setText(data.get(MessageData.HITPOINTS));
				currentHealth.setText(data.get(MessageData.CURRENTHITPOINTS));
				
				if(data.containsKey(MessageData.HITPOINTS)) {
					lifeBar.setRange(0f, Integer.parseInt(data.get(MessageData.HITPOINTS)));
					lifeBar.setValue(Integer.parseInt(data.get(MessageData.CURRENTHITPOINTS)));
				}
				playerName.setText(data.get(MessageData.OWNERNAME));
				
				resourceType.setText(data.get(MessageData.RESOURCETYPE));
				resourceAmount.setText(data.get(MessageData.RESOURCEAMOUNT));
				
				if(data.containsKey(MessageData.RESOURCERESET)) {
					resourceType.setText("");
					resourceAmount.setText("");
				}
				
				if(data.containsKey(MessageData.QUEUE)) {
					setInfoRightQueue();
					queue.setText(data.get(MessageData.QUEUE));
					queueCurrent.setText(data.get(MessageData.CURRENTPROCESSING));
					currentProgress.setValue(Integer.parseInt(data.get(MessageData.PROCESSINGCOUNTER)));
					infoRightQueue.setVisible(true);
				} else {
					setInfoRightNormal();
					infoRight.setVisible(true);
				}
				
				 setButtons(data);
				 
				infoLeft.setVisible(true);
				break;
			
			case UNSELECTED:
				objectName.setText("");
				infoLeft.setVisible(false);
				infoRight.setVisible(false);
				infoRightQueue.setVisible(false);
				disableButtons();
				break;
				
			
			default:
				break;
		
		}
		
	}
	
	private void setButtons(final Map<MessageData, String> data) {
		
		if(data.containsKey(MessageData.BUTTON0)) {
			
			buttons[0].setText(data.get(MessageData.BUTTON0));
			buttons[0].setVisible(true);
			buttons[0].addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					sendMessage(MessageType.valueOf(data.get(MessageData.BUTTON0)));
				}
			});
			
		} if(data.containsKey(MessageData.BUTTON1)) {
			
			buttons[1].setText(data.get(MessageData.BUTTON1));
			buttons[1].setVisible(true);
			buttons[1].addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					sendMessage(MessageType.valueOf(data.get(MessageData.BUTTON1)));
				}
			});
			
		} if(data.containsKey(MessageData.BUTTON2)) {
			
		} if(data.containsKey(MessageData.BUTTON3)) {
			
		} if(data.containsKey(MessageData.BUTTON4)) {
			
		} if(data.containsKey(MessageData.BUTTON5)) {
			
		} if(data.containsKey(MessageData.BUTTON6)) {
			
		} if(data.containsKey(MessageData.BUTTON7)) {
			
		} if(data.containsKey(MessageData.BUTTON8)) {
			
		} if(data.containsKey(MessageData.BUTTON9)) {
			
		} if(data.containsKey(MessageData.BUTTON10)) {
			
		} if(data.containsKey(MessageData.BUTTON11)) {
			
		} if(data.containsKey(MessageData.BUTTON12)) {
			
		} if(data.containsKey(MessageData.BUTTON13)) {
			
		} else if(data.containsKey(MessageData.BUTTON14)) {
			
		} else if(data.containsKey(MessageData.BUTTON15)) {
			
		} 
		
	}
	
	private void disableButtons() {
		for(int i = 0; i< 15; i++) {
			Array<EventListener> l = buttons[i].getListeners();
			for(EventListener eventListener : l) {
				if(eventListener instanceof ChangeListener)
					buttons[i].removeListener(eventListener);
			}
			buttons[i].setVisible(false);
		}
	}
	
	private void setInfoRightNormal() {
		infoTable.removeActor(infoRightQueue);
		infoTable.add(infoRight);
		infoRight.padLeft(100f);
		infoRight.add(nationName);
		infoRight.row();
		infoRight.add(playerName);
		infoRight.row();
		infoRight.add(relationState);
	}
	
	private void setInfoRightQueue() {
		infoTable.removeActor(infoRight);
		infoTable.add(infoRightQueue);
		infoRightQueue.add(queueCurrent);
		infoRightQueue.add(currentProgress);
		infoRightQueue.row();
		infoRightQueue.add(queue);
	}
}
