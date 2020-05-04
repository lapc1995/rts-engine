package com.mygdx.engine.gamelogic.gameobject.resource;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.mygdx.engine.gamelogic.gameobject.GameObjectType;
import com.mygdx.engine.gamelogic.gameobject.Resource;

public class GoldMine extends Resource{
	
	public GoldMine(Model model, int id) {
		super(model, GameObjectType.GOLDMINE, "Gold Mine", 500, id, ResourceType.GOLD, 2, 1, 3,  new btBoxShape(new Vector3(1, 0.5f, 1.5f)));
	}

}
