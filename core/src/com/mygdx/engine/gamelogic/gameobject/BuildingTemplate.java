package com.mygdx.engine.gamelogic.gameobject;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

public class BuildingTemplate extends GameObject implements Renderble{

	private GameObjectType buildingType;
	
	public BuildingTemplate(Model model, int id, int width, int height, int depth, GameObjectType buildingType, btCollisionShape collisionshape) {
		super(model, GameObjectType.BUILDINGTEMPLATE, "BuildingTemplate", id, width, height, depth, collisionshape);
		this.buildingType = buildingType;
	}
	
	@Override
	public void render(ModelBatch modelBatch, Environment environment) {
		modelBatch.render(this, environment);
	}
	
	public GameObjectType getBuildingType() {
		return buildingType;
	}

}
