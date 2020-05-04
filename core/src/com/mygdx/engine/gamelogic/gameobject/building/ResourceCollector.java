package com.mygdx.engine.gamelogic.gameobject.building;

import com.mygdx.engine.gamelogic.gameobject.resource.ResourceType;

public interface ResourceCollector {
	
	public void addResource(ResourceType type, int value);
	
	public int getId();

}
