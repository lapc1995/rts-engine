package com.mygdx.engine.gamelogic;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.mygdx.engine.gamelogic.gameobject.BuildingTemplate;
import com.mygdx.engine.gamelogic.gameobject.GameObjectType;
import com.mygdx.engine.gamelogic.gameobject.MapPositions;
import com.mygdx.engine.gamelogic.gameobject.OwnerType;
import com.mygdx.engine.gamelogic.gameobject.Renderble;
import com.mygdx.engine.gamelogic.gameobject.Selectable;
import com.mygdx.engine.gamelogic.message.MessageData;
import com.mygdx.engine.gamelogic.message.MessageEvent;
import com.mygdx.engine.gamelogic.message.MessageType;
import com.mygdx.engine.gamelogic.player.PlayerCatalog;

public class PlayGL extends GameLogic{

	private PlayerCatalog playerCatalog;
	
	private boolean activeRayCasting;
	private Ray ray;
	private BuildingTemplate toBeBuilt;
	private Vector3 pos;
	
	public PlayGL() {
		
		Bullet.init();
		
		playerCatalog = new PlayerCatalog();
		playerCatalog.addPlayer(OwnerType.PLAYER1, "Luis", 1000, 1000, 1000, 1000, 200);

		playerCatalog.addResource(GameObjectType.GOLDMINE, 4 , 0, 4);
		playerCatalog.addResource(GameObjectType.STONEMINE, 4 , 0, 15);
		
		
		playerCatalog.addResource(GameObjectType.BERRYBUSH, 8 , 0, 15);
		playerCatalog.addResource(GameObjectType.TREE, 15 , 0, 15);
		
		playerCatalog.addUnit(GameObjectType.VILLAGER, OwnerType.PLAYER1, 6 , 0, 6, playerCatalog);
		playerCatalog.addUnit(GameObjectType.VILLAGER, OwnerType.PLAYER1, 8 , 0, 8, playerCatalog);

		playerCatalog.addBuilding(GameObjectType.TOWNCENTER, OwnerType.PLAYER1, 15 , 0, 8, playerCatalog, true);
		
		playerCatalog.addBuilding(GameObjectType.MININGCAMP, OwnerType.PLAYER1, 11, 0, 9, playerCatalog, true);
		
		activeRayCasting = false;
		ray = null;
		toBeBuilt = null;
		pos = null;
		
		
		
		
	}
	
	@Override
	public void dispose() {
		
	}
	


	public void init() {
		sendMessage(MessageType.PLAYERDATA, playerCatalog.getCurrentPlayer().dataToBeSend());
	}
	
	
	@Override
	public void messageReceived(MessageEvent event) {
		
		switch(event.getType()) {
			
			case MOVERAY:
				 move(event);
				break;
			
			case SELECTRAY:
				select(event);
				break;
				
				
			case CREATEVILLAGER:
				playerCatalog.getCurrentSelect().executeOrder(event.getType(), event.getData());
				break;
				
			case CREATEBUILDINGMININGCAMP:
				removeBuildingTemplate();
				createBuilding(GameObjectType.MININGCAMP);
				break;
				
			case CREATEBUILDINGTOWNCENTER:
				removeBuildingTemplate();
				createBuilding(GameObjectType.TOWNCENTER);
				break;
				
				
			case ACTIVECASTING:
				ray = unpackRay(event.getData());
				break;
				
				
			default:
				break;

		}
	}
	

	
	@Override
	public void update(float dt) {
		playerCatalog.update(dt);
		
		if(playerCatalog.isSelected() && playerCatalog.getCurrentSelect().isChanged()) {
			Selectable s = playerCatalog.getCurrentSelect();
			Map<MessageData, String> toBeSent = s.dataToBeSent();
			toBeSent.put(MessageData.OWNERNAME, playerCatalog.getPlayer(s.getOwner()).getName());
			sendMessage(MessageType.SELECTEDDATA, toBeSent);	
		}
		
		if(playerCatalog.getCurrentPlayer().isChanged()) {
			Map<MessageData, String> toBeSent = playerCatalog.getCurrentPlayer().dataToBeSend();
			sendMessage(MessageType.PLAYERDATA, toBeSent);	
		}
		
		if(activeRayCasting && ray != null) {
			pos =  playerCatalog.getTerrainCollisionVector(ray, false);
			if(pos != null) {
				Vector3 r = new Vector3( pos.x + ((float)toBeBuilt.getWidth()) / 2, pos.y + ((float)toBeBuilt.getHeigth()) / 2, pos.z + ((float)toBeBuilt.getDepth()) / 2);
				Quaternion q = new Quaternion();
				toBeBuilt.transform.getRotation(q);
				toBeBuilt.transform.set(r, q);
			}
		}
	}

	@Override
	public ArrayList<Renderble> getRenderable() {
		return playerCatalog.getRenderables();
	}
	
	private Ray unpackRay(Map<MessageData, String> data) {
		Ray ray = new Ray();
		
		Vector3 origin = new Vector3(Float.parseFloat(data.get(MessageData.RAYORIGINX)),
				 					 Float.parseFloat(data.get(MessageData.RAYORIGINY)),
				 					 Float.parseFloat(data.get(MessageData.RAYORIGINZ)));
		
		Vector3 direction = new Vector3(Float.parseFloat(data.get(MessageData.RAYDIRECTIONX)),
										Float.parseFloat(data.get(MessageData.RAYDIRECTIONY)),
										Float.parseFloat(data.get(MessageData.RAYDIRECTIONZ)));
		
		ray.set(origin, direction);
		
		return ray;
	}
	
	
	private void move(MessageEvent event) {
		if(!playerCatalog.isSelected())
			return;
		Ray raym = unpackRay(event.getData());
		if(activeRayCasting) {
			int id = playerCatalog.addBuilding(toBeBuilt.getBuildingType(), OwnerType.PLAYER1, pos.x, 0, pos.z, playerCatalog, false).getId();
			playerCatalog.getCurrentSelect().workWithId(id);
			sendMessage(MessageType.ACTIVECASTINGOFF);
			activeRayCasting = false;
			playerCatalog.removeBuildingTemplate(toBeBuilt);
			return;
		}
		int id = playerCatalog.getObject(raym);
		
		if(id == -1) {
			Vector3 vector = playerCatalog.getTerrainCollisionVector(raym, false);
			playerCatalog.getCurrentSelect().workWithVector(vector);
		} else
			playerCatalog.getCurrentSelect().workWithId(id);
	}
	
	private void select(MessageEvent event) {
		Ray rays = unpackRay(event.getData());
		int index = playerCatalog.getObject(rays);
		playerCatalog.setSelectedId(index);
		if(index == -1) {
			sendMessage(MessageType.UNSELECTED);
			if(activeRayCasting) {
				activeRayCasting = false;
				removeBuildingTemplate();
			}
			sendMessage(MessageType.ACTIVECASTINGOFF);
		}
		else {
			Selectable s = playerCatalog.getCurrentSelect();
			removeBuildingTemplate();
			Map<MessageData, String> toBeSent = s.dataToBeSent();
			toBeSent.put(MessageData.OWNERNAME, playerCatalog.getPlayer(s.getOwner()).getName());
			sendMessage(MessageType.SELECTEDDATA, toBeSent);	
		}
	}
	
	private void createBuilding(GameObjectType type) {
		sendMessage(MessageType.ACTIVECASTINGON);
		toBeBuilt = playerCatalog.addBuildingTemplate(type);
		activeRayCasting = true;
	}
	
	private void removeBuildingTemplate() {
		if(toBeBuilt != null) {
			playerCatalog.removeBuildingTemplate(toBeBuilt);
			toBeBuilt = null;
		}
	}

}
