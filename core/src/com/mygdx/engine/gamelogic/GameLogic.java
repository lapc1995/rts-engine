package com.mygdx.engine.gamelogic;

import java.util.ArrayList;

import com.mygdx.engine.gamelogic.gameobject.Renderble;
import com.mygdx.engine.gamelogic.message.MessageListener;
import com.mygdx.engine.gamelogic.message.MessageSender;

public abstract class GameLogic extends MessageSender implements MessageListener {
	
	
	public abstract void update(float dt);
	
	public abstract void dispose();

	public abstract ArrayList<Renderble> getRenderable();
	
}
