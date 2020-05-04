package com.mygdx.engine.gamelogic.gameobject.building;

import java.util.Map;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.mygdx.engine.gamelogic.gameobject.Building;
import com.mygdx.engine.gamelogic.gameobject.GameObjectType;
import com.mygdx.engine.gamelogic.gameobject.OwnerType;
import com.mygdx.engine.gamelogic.gameobject.resource.ResourceType;
import com.mygdx.engine.gamelogic.message.MessageData;
import com.mygdx.engine.gamelogic.message.MessageType;
import com.mygdx.engine.gamelogic.player.PlayerCatalog;

public class MiningCamp extends Building implements ResourceCollector{

	public MiningCamp(Model model, OwnerType owner, int id, PlayerCatalog playerCatalog, boolean completed) {
		super(model, GameObjectType.MININGCAMP, "Mining Camp", owner, id, playerCatalog, 2, 2, 2, completed, 100,  new btBoxShape(new Vector3(1, 1, 1)));
	}

	@Override
	public void addResource(ResourceType type, int value) {
		if(type == ResourceType.GOLD)
			getPlayer().addGold(value);
		else if(type == ResourceType.STONE)
			getPlayer().addStone(value);
		getPlayer().setChanged();
	}

	@Override
	public boolean executeOrder(MessageType type, Map<MessageData, String> data) {
		return false;
		
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
	public void createUnit(GameObjectType type) {
		// TODO Auto-generated method stub
		
	}
	

}
