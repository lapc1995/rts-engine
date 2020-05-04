package com.mygdx.engine.renderer;

public class RendererManager {
	
	private Renderer renderer;
	
	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}
	
	public void resize(int width, int height) {
		renderer.resize(width, height);
	}
	
	public boolean isRendererSet() {
		return renderer != null;
	}

	public void render() {
		if(isRendererSet())
			renderer.render();
	}

}
