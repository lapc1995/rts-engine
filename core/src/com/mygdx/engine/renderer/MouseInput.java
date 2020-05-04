package com.mygdx.engine.renderer;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.collision.Ray;
import com.mygdx.engine.core.Settings;
import com.mygdx.engine.gamelogic.message.MessageData;
import com.mygdx.engine.gamelogic.message.MessageEvent;
import com.mygdx.engine.gamelogic.message.MessageListener;
import com.mygdx.engine.gamelogic.message.MessageSender;
import com.mygdx.engine.gamelogic.message.MessageType;

public class MouseInput extends MessageSender implements InputProcessor, MessageListener{
	
	private  PerspectiveCamera camera;
	
	private boolean moveLeft;
	private boolean moveRight;
	private boolean moveUp;
	private boolean moveDown;
	
	private Rectangle rectLeft;
	private Rectangle rectRight;
	private Rectangle rectBottom;
	private Rectangle rectUp;
	
	private int limit;
	
	private int rectSize;
	
	private boolean activeCasting;
	
	public MouseInput(PerspectiveCamera camera) {
		this.camera = camera;
		
		this.moveLeft = false;
		this.moveRight = false;
		this.moveDown = false;
		this.moveUp = false;
		
		this.limit = (int) (Settings.getHeight() * 0.8);
		this.rectSize = 30;
		
		this.rectLeft = new Rectangle(0, 0, rectSize, Settings.getHeight());
		this.rectRight = new Rectangle(Settings.getWidth() - rectSize, 0, rectSize, Settings.getHeight());
		this.rectBottom = new Rectangle(0, Settings.getHeight() - 80, Settings.getWidth(), rectSize);
		this.rectUp = new Rectangle(0, 0, Settings.getWidth(), rectSize);
		
		this.activeCasting = false;
	}
	
	public void update(float dt) {
		if(moveLeft)
			moveCameraLeft(dt);
		if(moveRight)
			moveCameraRight(dt);
		if(moveUp)
			moveCameraUp(dt);
		if(moveDown)
			moveCameraDown(dt);
	}
	
	private void moveCameraDown(float dt) {
		camera.translate(1 * dt, 0, 1 * dt);
	}

	private void moveCameraUp(float dt) {
		camera.translate(-1 * dt, 0, -1 * dt);
	}

	private void moveCameraRight(float dt) {
		camera.translate(1 * dt, 0, -1 * dt);
	}
	
	private void moveCameraLeft(float dt) {
		camera.translate(-1 * dt, 0, 1 * dt);
	}

	private void zoomIn(){
		camera.translate(-1, -1, -1);
	}
	
	private void zoomOut() {
		camera.translate(1, 1 , 1);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
		
			case Keys.Q:
				zoomIn();
				break;
				
			case Keys.A:
				zoomOut();
				break;

		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(screenY < limit) {
			if(button == 0)
				leftMouseButton(screenX, screenY);
			if(button == 1)
				rightMouseButton(screenX, screenY);
		}
		
		return false;
	}

	private boolean leftMouseButton(int screenX, int screenY) {
		Ray ray = camera.getPickRay(screenX, screenY);
		sendMessage(MessageType.SELECTRAY, packRay(ray));
		return true;
	}
	
	private boolean rightMouseButton(int screenX, int screenY) {
		Ray ray = camera.getPickRay(screenX, screenY);
		sendMessage(MessageType.MOVERAY, packRay(ray));
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if(rectLeft.contains(screenX, screenY))
			moveLeft = true;
		else
			moveLeft = false;
		
		if(rectRight.contains(screenX, screenY))
			moveRight = true;
		else
			moveRight = false;
		
		if(rectBottom.contains(screenX, screenY))
			moveDown = true;
		else
			moveDown = false;
		
		if(rectUp.contains(screenX, screenY))
			moveUp = true;
		else
			moveUp = false;
		
		if(activeCasting && screenY < limit)
			activeRayCasting(screenX, screenY);
		
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		if(amount == 1)
			zoomOut();
		else if (amount == -1)
			zoomIn();
			
		return false;
	}

	public void renderDebug(ShapeRenderer sr) {
		sr.rect(rectLeft.x, rectLeft.y, rectLeft.width, rectLeft.height);
		sr.rect(rectRight.x, rectRight.y, rectRight.width, rectRight.height);
		sr.rect(rectBottom.x, rectBottom.y, rectBottom.width, rectBottom.height);
		sr.rect(rectUp.x, rectUp.y, rectUp.width, rectUp.height);
	}
	
	private Map<MessageData, String> packRay(Ray ray) {
		Map<MessageData, String> data = new HashMap<MessageData, String>();
		data.put(MessageData.RAYDIRECTIONX, Float.toString(ray.direction.x));
		data.put(MessageData.RAYDIRECTIONY, Float.toString(ray.direction.y));
		data.put(MessageData.RAYDIRECTIONZ, Float.toString(ray.direction.z));
		data.put(MessageData.RAYORIGINX, Float.toString(ray.origin.x));
		data.put(MessageData.RAYORIGINY, Float.toString(ray.origin.y));
		data.put(MessageData.RAYORIGINZ, Float.toString(ray.origin.z));
		
		return data;
	}
	
	public void activeRayCasting(int screenX, int screenY) {
		Ray ray = camera.getPickRay(screenX, screenY);
		sendMessage(MessageType.ACTIVECASTING, packRay(ray));
	}
	
	@Override
	public void messageReceived(MessageEvent event) {
	
		switch(event.getType()) {
		
			case ACTIVECASTINGON:
				activeCasting = true;
				break;
				
			case ACTIVECASTINGOFF:
				activeCasting = false;
				break;
		
			default:
				break;
		
		}
		
	}
}
