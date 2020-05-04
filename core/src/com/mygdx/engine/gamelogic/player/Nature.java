package com.mygdx.engine.gamelogic.player;

import java.util.HashMap;
import java.util.Map;

import com.mygdx.engine.gamelogic.gameobject.Resource;

public class Nature extends Player{

	private Map<Integer, Resource> resources;
	
	public Nature() {
		super("Nature", 0, 0, 0, 0, 200);
		resources = new HashMap<Integer, Resource>();
	}

	public void addResource(Resource resource) {
		resources.put(resource.getId(), resource);
	}
	
	public Resource getResource(int id) {
		return resources.get(id);
	}
	
	public boolean hasResource(int id) {
		return resources.containsKey(id);
	}
}
