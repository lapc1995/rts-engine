package com.mygdx.engine.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.mygdx.engine.gamelogic.gameobject.Renderble;

public class Graphics3DRenderer implements Renderer{

	private ModelBatch modelBatch;
	private Iterable<Renderble> objects;
	
	public final PerspectiveCamera camera;
	public final Environment environment;
	
	public Graphics3DRenderer(Iterable<Renderble> objects) {
		this.objects = objects;
		this.modelBatch = new ModelBatch();
		this.environment = new Environment();
		this.camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	    this.camera.position.set(10f, 10f, 10f);
	    this.camera.lookAt(0,0,0);
        this.camera.near = 1f;
        this.camera.far = 300f;
	    this.camera.update();
	}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		modelBatch.begin(camera);
		for(Renderble obj : objects)
			obj.render(modelBatch, environment);
		modelBatch.end();
	}

	@Override
	public void resize(int width, int height) {}

}
