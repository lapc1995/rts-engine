package com.mygdx.engine.gamelogic.gameobject;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.mygdx.engine.gamelogic.gameobject.resource.ResourceType;
import com.mygdx.engine.gamelogic.message.MessageData;
import com.mygdx.engine.gamelogic.message.MessageType;

public abstract class Resource extends GameObject implements Selectable, Updatable, Renderble{

	private int maxResourceAmount;
	private int currentResourceAmount;
	private OwnerType owner;
	private ResourceType resourceType;
	private boolean changed;
	
	public Resource(Model model, GameObjectType type, String name, int resourceAmount, int id, ResourceType resourceType, int width, int height, int depth, btCollisionShape collisionshape) {
		super(model, type, name, id, width, height, depth, collisionshape);
		this.maxResourceAmount = resourceAmount;
		this.currentResourceAmount = resourceAmount;
		this.owner = OwnerType.NATURE;
		this.resourceType = resourceType;
		changed = false;
	}
	
	public void setMaxResourceAmount(int amount) {
		maxResourceAmount = amount;
	}
	
	public void setCurrentResourceAmount(int amount) {
		currentResourceAmount = amount;
	}
	
	public int getCurrentResouceAmount() {
		return currentResourceAmount;
	}
	
	public int getMaxResourceAmount() {
		return maxResourceAmount;
	}
	
	public ResourceType getResourceType() {
		return resourceType;
	}
	
	public int subtractAmount(int amount) {
		int result;
		if(amount > currentResourceAmount) {
			result = currentResourceAmount;
			currentResourceAmount = 0;
		} else {
		currentResourceAmount -= amount;
			result = amount;
		}
		return result;
	}
	
	public void update(float dt) {

	}
	
	@Override
	public void render(ModelBatch modelBatch, Environment environment) {
		modelBatch.render(this, environment);
	}

	@Override
	public OwnerType getOwner() {
		return owner;
	}
	
	@Override
	public Map<MessageData, String> dataToBeSent() {
		Map<MessageData, String> data = new HashMap<MessageData, String>();
		data.put(MessageData.NAME, getName());
		data.put(MessageData.OWNER, owner.toString());
		data.put(MessageData.CURRENTRESOURCEAMOUNT, Integer.toString(currentResourceAmount));
		data.put(MessageData.MAXRESOURCEAMOUNT, Integer.toString(maxResourceAmount));
		return data;
	}
	
	@Override
	public void selectedOn() {
		model.materials.get(0).set(ColorAttribute.createDiffuse(Color.RED));
	}
	
	@Override
	public void selectedOff() {
		model.materials.get(0).set(ColorAttribute.createDiffuse(Color.YELLOW));
	}
	
	@Override
	public void setChanged() {
		changed = true;
	}
	
	@Override
	public boolean isChanged() {
		return changed;
	}
	
	@Override
	public boolean executeOrder(MessageType type, Map<MessageData, String> data) {
		return true;
	}

	@Override
	public boolean workWithId(int id) {
		return false;
		
	}

	@Override
	public void workWithVector(Vector3 vector) {
		// TODO Auto-generated method stub
		
	}
}
