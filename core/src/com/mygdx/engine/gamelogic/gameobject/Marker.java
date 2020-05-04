package com.mygdx.engine.gamelogic.gameobject;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector3;

public class Marker extends GameObject implements Updatable, Renderble{

	private boolean showing;
	private float timeSince;
	
	public Marker(Model model, int id) {
		super(model, GameObjectType.MARKER, "Marker", id, 1, 1, 1, null);
		reset();
	}

	@Override
	public void render(ModelBatch modelBatch, Environment environment) {
		if(showing)
			modelBatch.render(this, environment);
	}

	@Override
	public void update(float dt) {
		if(showing) {
			timeSince += dt;
			if(timeSince > 3.0f) {
				reset();
			} 
		}	
	}
	
	public void show(Vector3 pos) {
		super.transform.setToTranslation(pos.x, 0.5f, pos.z);
		showing = true;
	}
	
	private void reset() {
	    showing = false;
	    timeSince = 0;
	}

}
