package com.mygdx.engine.gamelogic.gameobject;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public interface Renderble{
	
	public void render(ModelBatch modelBatch, Environment environment);

}
