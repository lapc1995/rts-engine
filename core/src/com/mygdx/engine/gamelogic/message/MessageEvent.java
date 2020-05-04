package com.mygdx.engine.gamelogic.message;

import java.util.EventObject;
import java.util.Map;

public class MessageEvent extends EventObject {

	private static final long serialVersionUID = 5471445095906878452L;

	private MessageType type;
	private Map<MessageData, String> data;
	
	public MessageEvent(Object arg0, MessageType type, Map<MessageData, String> data) {
		super(arg0);
		this.type = type;
		this.data = data;
	}
	
	
	public MessageType getType() {
		return type;
	}
	
	public  Map<MessageData, String> getData() {
		return data;
	}

}
