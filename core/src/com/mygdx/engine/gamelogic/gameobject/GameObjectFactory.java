package com.mygdx.engine.gamelogic.gameobject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.mygdx.engine.gamelogic.gameobject.building.MiningCamp;
import com.mygdx.engine.gamelogic.gameobject.building.TownCenter;
import com.mygdx.engine.gamelogic.gameobject.resource.BerryBush;
import com.mygdx.engine.gamelogic.gameobject.resource.GoldMine;
import com.mygdx.engine.gamelogic.gameobject.resource.StoneMine;
import com.mygdx.engine.gamelogic.gameobject.resource.Tree;
import com.mygdx.engine.gamelogic.gameobject.unit.Villager;
import com.mygdx.engine.gamelogic.player.PlayerCatalog;

public enum GameObjectFactory {
	INSTANCE;
	
	private int counter = -1;
	
	public Unit createUnit(GameObjectType type, float x, float y, float z, OwnerType owner, PlayerCatalog playerCatalog) {
		ModelBuilder modelBuilder = new ModelBuilder();
		Model model;
		counter++;
		switch(type) {
		
			case VILLAGER:
				model = modelBuilder.createBox(1, 1, 1, new Material(ColorAttribute.createDiffuse(Color.BLUE)), Usage.Position | Usage.Normal);
				Villager villager = new Villager(model, owner, counter, playerCatalog);
				prepareObject(villager, x, y, z);
				return villager;
				
			default:
				return null;
		}
	}
	
	public Building createBuilding(GameObjectType type, float x, float y, float z, OwnerType owner, PlayerCatalog playerCatalog, boolean completed) {
		ModelBuilder modelBuilder = new ModelBuilder();
		Model model;
		counter++;
		
		switch(type) {
		
			case MININGCAMP:
				model = modelBuilder.createBox(2, 2, 2, new Material(ColorAttribute.createDiffuse(Color.FIREBRICK)), Usage.Position | Usage.Normal);
				MiningCamp miningCamp = new MiningCamp(model,  owner, counter, playerCatalog, completed);
				prepareObject(miningCamp, x, y, z);
				return miningCamp;
				
			case TOWNCENTER:
				model = modelBuilder.createBox(4, 3, 4, new Material(ColorAttribute.createDiffuse(Color.GOLDENROD)), Usage.Position | Usage.Normal);
				TownCenter townCenter = new TownCenter(model,  owner, counter, playerCatalog, completed);
				prepareObject(townCenter, x, y, z);
				return townCenter;
				
			default:
				return null;
				
		}
	}
	
	public Resource createResource(GameObjectType type, float x, float y, float z) {
		ModelBuilder modelBuilder = new ModelBuilder();
		Model model;
		counter++;
		Resource resource = null;
		switch(type) {
		
			case GOLDMINE:
				model = modelBuilder.createBox(2, 1, 3, new Material(ColorAttribute.createDiffuse(Color.YELLOW)), Usage.Position | Usage.Normal);
				resource = new GoldMine(model, counter);
				break;
				
			case STONEMINE:
				model = modelBuilder.createBox(2, 1, 3, new Material(ColorAttribute.createDiffuse(Color.GRAY)), Usage.Position | Usage.Normal);
				resource = new StoneMine(model, counter);
				break;
				
			case TREE:
				model = modelBuilder.createBox(2, 6, 2, new Material(ColorAttribute.createDiffuse(Color.GREEN)), Usage.Position | Usage.Normal);
				resource = new Tree(model, counter);
				break;
				
			case BERRYBUSH:
				model = modelBuilder.createBox(2, 2, 2, new Material(ColorAttribute.createDiffuse(Color.PURPLE)), Usage.Position | Usage.Normal);
				resource = new BerryBush(model, counter);
				break;
					
			default:
				break;
		}
		prepareObject(resource, x, y, z);
		return resource;
	}
	
	
	
	public GameObject create(GameObjectType type, float x, float y, float z, OwnerType owner) {
		ModelBuilder modelBuilder = new ModelBuilder();
		Model model;
		counter++;
		switch(type) {
		
			case MARKER:
				model = modelBuilder.createSphere(0.5f, 0.5f, 0.5f, 10, 10, new Material(ColorAttribute.createDiffuse(Color.RED)),  Usage.Position | Usage.Normal);
				Marker marker = new Marker(model, counter);
				marker.transform.translate(x + 0.5f, y + 0.5f, z + 0.5f);
				return marker;
				
			default:
				return null;

		}
	}
	
	public BuildingTemplate createBuildingTemplate(GameObjectType type) {
		ModelBuilder modelBuilder = new ModelBuilder();
		Model model;
		counter++;
		BuildingTemplate bt;
		switch(type) {
		
			case MININGCAMP:
				model = modelBuilder.createBox(2, 2, 2, new Material(ColorAttribute.createDiffuse(Color.FIREBRICK)), Usage.Position | Usage.Normal);
				bt = new BuildingTemplate (model, counter, 2, 2, 2, type,  new btBoxShape(new Vector3(2, 2, 2)));
				return bt;
				
			case TOWNCENTER:
				model = modelBuilder.createBox(4, 3, 4, new Material(ColorAttribute.createDiffuse(Color.GOLDENROD)), Usage.Position | Usage.Normal);
				bt = new BuildingTemplate (model, counter, 4, 3, 4, type, new btBoxShape(new Vector3(4, 3, 4)));
				return bt;
				
			default:
				return null;
		}
	}
	
	private void prepareObject(GameObject go, float x, float y, float z) {
		float xx = x + ((float)go.getWidth()) / 2;
		float yy = y + ((float)go.getHeigth()) / 2;
		float zz = z + ((float)go.getDepth()) / 2;
		go.transform.translate(xx, yy, zz);
		go.getCollisionObject().setWorldTransform(go.transform);
		
		int xxx = (int) x;
		int zzz = (int) z;
		
		for(int i = xxx; i < go.getWidth() + xxx; i++) {
			for(int j = zzz; j < go.getDepth() + zzz; j++) {
				MapPositions.INSTANCE.enterPosition(i , j, go.getId(), go.getType());
			}
		}
	}

}
