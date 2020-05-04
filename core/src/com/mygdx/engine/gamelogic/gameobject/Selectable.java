package com.mygdx.engine.gamelogic.gameobject;

import java.util.Map;

import com.badlogic.gdx.math.Vector3;
import com.mygdx.engine.gamelogic.message.MessageData;
import com.mygdx.engine.gamelogic.message.MessageType;

public interface Selectable {
	
	public Map<MessageData, String> dataToBeSent();
	
	public OwnerType getOwner();
	
	public void selectedOn();
	
	public void selectedOff();
	
	public void setChanged();
	
	public boolean isChanged();
	
	public boolean executeOrder(MessageType type, Map<MessageData, String> data);
	
	public boolean workWithId(int id);
	
	public void workWithVector(Vector3 vector);
}
