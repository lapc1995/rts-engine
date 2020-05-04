package com.mygdx.engine.gamelogic.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class MessageSender {
	
	private List<MessageListener> listeners;

	public MessageSender() {
		listeners = new ArrayList<MessageListener>();
	}
	
	public synchronized void addListener(MessageListener l ) {
		listeners.add( l );
	}
	    
	public synchronized void removeMoodListener(MessageListener l ) {
		listeners.remove( l );
	}
	
	public synchronized void sendMessage(MessageType type,  Map<MessageData, String> data) {
		send(type, data);
	}
	
	public synchronized void sendMessage(MessageType type) {
		send(type, null);
	}
	
	private  synchronized void send(MessageType type, Map<MessageData, String> data) {
		for(MessageListener listener : listeners) {
			MessageEvent e = new MessageEvent(this, type, data); 
			listener.messageReceived(e);
		}
	}
}
