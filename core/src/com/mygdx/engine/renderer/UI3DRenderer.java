package com.mygdx.engine.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.engine.gamelogic.GameLogic;
import com.mygdx.engine.gamelogic.gameobject.Renderble;

public class UI3DRenderer implements Renderer{

	private Stage stage;
	private ModelBatch modelBatch;
	private Iterable<Renderble> objects;
	
	public final PerspectiveCamera camera;
	public final Environment environment;
	//public CameraInputController camController;
	//private RendererInput rendererInput;
	
	private ShapeRenderer shapeRenderer;
	
	public UI3DRenderer(Stage stage, GameLogic logic) {
		this.stage = stage;
		this.objects = logic.getRenderable();
		this.modelBatch = new ModelBatch();
		this.environment = new Environment();
		this.camera = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	    this.camera.position.set(10f, 10f, 10f);
	    this.camera.lookAt(0,0,0);
        this.camera.near = 1f;
        this.camera.far = 300f;
	    this.camera.update();
	   // this.rendererInput = new RendererInput(camera, logic);
        //this.camController = new CameraInputController(camera);
        this.shapeRenderer = new ShapeRenderer();
        
        this.shapeRenderer.setProjectionMatrix(this.shapeRenderer.getProjectionMatrix().translate(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0).rotate(0, 0, 1, 180));
        
        //inputMultiplexer.addProcessor(rendererInput);
        
	}
	
	@Override
	public void render() {
		stage.act();
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		//rendererInput.update();
		camera.update();
		modelBatch.begin(camera);
		for(Renderble obj : objects)
			obj.render(modelBatch, environment);
		modelBatch.end();
		
		shapeRenderer.begin(ShapeType.Line);
		//rendererInput.renderDebug(shapeRenderer);
		shapeRenderer.end();
		
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
	
	
}
