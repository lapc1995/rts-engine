package com.mygdx.engine.gamelogic.gameobject.building;

import java.util.Map;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.mygdx.engine.gamelogic.gameobject.Building;
import com.mygdx.engine.gamelogic.gameobject.GameObjectType;
import com.mygdx.engine.gamelogic.gameobject.MapPositions;
import com.mygdx.engine.gamelogic.gameobject.OwnerType;
import com.mygdx.engine.gamelogic.gameobject.resource.ResourceType;
import com.mygdx.engine.gamelogic.message.MessageData;
import com.mygdx.engine.gamelogic.message.MessageType;
import com.mygdx.engine.gamelogic.player.PlayerCatalog;

public class TownCenter extends Building implements ResourceCollector{

	public TownCenter(Model model, OwnerType owner, int id, PlayerCatalog playerCatalog, boolean completed) {
		super(model, GameObjectType.TOWNCENTER, "Town Center", owner, id, playerCatalog, 4, 3, 4, completed, 1000,  new btBoxShape(new Vector3(2, 1.5f, 2)));
		// TODO Auto-generated constructor stub
	}

	public void createVillager() {
		Vector2 position = MapPositions.INSTANCE.getAvailablePositionAround(getId());
		getPlayerCatalog().addUnit(GameObjectType.VILLAGER, getOwner(), position.x, 0, position.y, getPlayerCatalog());
	}
	
	@Override
	public Map<MessageData, String> dataToBeSent() {
		Map<MessageData, String> data = super.dataToBeSent();
		data.put(MessageData.BUTTON0, MessageData.CREATEVILLAGER.toString());
		return data;
	}

	@Override
	public boolean executeOrder(MessageType type, Map<MessageData, String> data) {
		if(super.executeOrder(type, data))
			return true;
		
		switch(type) {
		
			case CREATEVILLAGER:
				addOrder(GameObjectType.VILLAGER);
				return true;
				
			default:
				return false;
			
		}
		
	}

	@Override
	public boolean workWithId(int id) {
		return false;
		
	}

	@Override
	public void workWithVector(Vector3 vector) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addResource(ResourceType type, int value) {
		if(type == ResourceType.GOLD)
			getPlayer().addGold(value);
		else if(type == ResourceType.FOOD)
			getPlayer().addFood(value);
		else if(type == ResourceType.WOOD)
			getPlayer().addWood(value);
		else if(type == ResourceType.STONE)
			getPlayer().addStone(value);
		getPlayer().setChanged();
	}

	@Override
	public void createUnit(GameObjectType type) {
		Vector2 position = MapPositions.INSTANCE.getAvailablePositionAround(getId());
		getPlayerCatalog().addUnit(type, getOwner(), position.x, 0, position.y, getPlayerCatalog());
	}

}
