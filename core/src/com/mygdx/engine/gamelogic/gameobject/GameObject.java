package com.mygdx.engine.gamelogic.gameobject;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

public abstract class GameObject extends ModelInstance{

	public final Vector3 center;
	public final Vector3 dimensions;
	public final float radius;
	private BoundingBox bounds;
	private GameObjectType type;
	private String name;
	private int id;
	
	private int width;
	private int height;
	private int depth;
	
	private btCollisionObject body;
	
	public GameObject(Model model, GameObjectType type, String name, int id, int width, int height, int depth, btCollisionShape shape) {
		super(model);
		center = new Vector3();
		dimensions = new Vector3();
		bounds = new BoundingBox();
		calculateBoundingBox(bounds);
		bounds.getCenter(center);
		bounds.getDimensions(dimensions);
		radius = dimensions.len() / 2f;
		this.type = type;
		this.name = name;
		this.id = id;
		this.width = width;
		this.height = height;
		this.depth = depth;
		
        body = new btCollisionObject();
        body.setCollisionShape(shape);
        
        body.setWorldTransform(this.transform);
		body.setUserValue(id);
		body.setCollisionFlags(body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
	} 
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public GameObjectType getType() {
		return type;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeigth() {
		return height;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public btCollisionObject getCollisionObject() {
		return body;
	}
	
}
