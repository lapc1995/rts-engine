package com.mygdx.engine.gamelogic.gameobject.resource;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.mygdx.engine.gamelogic.gameobject.GameObjectType;
import com.mygdx.engine.gamelogic.gameobject.Resource;

public class Tree extends Resource{

	public Tree(Model model, int id) {
		super(model, GameObjectType.TREE, "Tree", 10, id, ResourceType.WOOD, 2, 6, 2, new btBoxShape(new Vector3(1, 3, 1)));
	}

}
