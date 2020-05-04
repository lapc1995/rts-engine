package com.mygdx.engine.gamelogic.gameobject;

import java.util.Map;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;

public class MyContactListener extends ContactListener{
	
	private Map<Integer, GameObject> allObjects;
	 
	public MyContactListener(Map<Integer, GameObject> allObjects) {
		this.allObjects = allObjects;
	}
	
	@Override
	public boolean onContactAdded (int userValue0, int partId0, int index0, int userValue1, int partId1, int index1) {
		//System.out.println(allObjects.get(userValue0).getName() + " " + allObjects.get(userValue1).getName());
		return true;
	}
    
}
