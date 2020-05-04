package com.mygdx.engine.gamelogic.gameobject;

import java.util.Map;

import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;

public class Collision {

	private btBroadphaseInterface broadphase;
	private btCollisionWorld collisionWorld;
    private btCollisionConfiguration collisionConfig;
    private btDispatcher dispatcher;
    private MyContactListener contactListener;
	
	public Collision(Map<Integer, GameObject> allObjects) {
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);
        contactListener = new MyContactListener(allObjects);
	}
	
	public void addCollisionObject(btCollisionObject collisionObject) {
		collisionWorld.addCollisionObject(collisionObject);
	}
	
	public void update() {
		collisionWorld.performDiscreteCollisionDetection();
	}
	
	public void dispose() {
		broadphase.dispose();
		collisionWorld.dispose();
		collisionConfig.dispose();
		dispatcher.dispose();
	}
	
}
