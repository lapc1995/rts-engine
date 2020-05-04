package com.mygdx.engine.gamelogic.gameobject.resource;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.mygdx.engine.gamelogic.gameobject.GameObjectType;
import com.mygdx.engine.gamelogic.gameobject.Resource;

public class BerryBush extends Resource{

	public BerryBush(Model model, int id) {
		super(model, GameObjectType.BERRYBUSH, "Berry Bush", 500, id, ResourceType.FOOD, 2, 2, 2,  new btBoxShape(new Vector3(1, 1, 1)));
	}

}
