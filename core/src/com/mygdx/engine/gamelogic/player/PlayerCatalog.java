package com.mygdx.engine.gamelogic.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.mygdx.engine.gamelogic.InnerLogic;
import com.mygdx.engine.gamelogic.gameobject.Building;
import com.mygdx.engine.gamelogic.gameobject.BuildingTemplate;
import com.mygdx.engine.gamelogic.gameobject.Collision;
import com.mygdx.engine.gamelogic.gameobject.Diplomacy;
import com.mygdx.engine.gamelogic.gameobject.GameObject;
import com.mygdx.engine.gamelogic.gameobject.GameObjectFactory;
import com.mygdx.engine.gamelogic.gameobject.GameObjectType;
import com.mygdx.engine.gamelogic.gameobject.Marker;
import com.mygdx.engine.gamelogic.gameobject.OwnerType;
import com.mygdx.engine.gamelogic.gameobject.Renderble;
import com.mygdx.engine.gamelogic.gameobject.Resource;
import com.mygdx.engine.gamelogic.gameobject.Selectable;
import com.mygdx.engine.gamelogic.gameobject.Terrain;
import com.mygdx.engine.gamelogic.gameobject.Unit;

public class PlayerCatalog {
	
	private Nature nature;
	private Map<OwnerType, Player> players;
	private int nPlayers;
	private InnerLogic innerLogic;
	private Terrain terrain;
	private Marker marker;
	private Diplomacy diplomacy;
	private Collision collision;
	
	public PlayerCatalog() {
		nature = new Nature();
		players = new HashMap<OwnerType, Player>();
		nPlayers = 0;
		
		innerLogic = new InnerLogic();
		
		marker = (Marker)addObject(GameObjectType.MARKER, 0, 0, 0);
		
		terrain = new Terrain(40, 40);
		innerLogic.addRenderble(terrain);
		
		diplomacy = new Diplomacy(players);
		
		collision = new Collision(innerLogic.getAllObjects());
	}
	
	public void addPlayer(OwnerType owner, String name, int wood, int food, int stone, int gold, int maxPopulation) {
		players.put(owner, new Player(name, wood, food, stone, gold, maxPopulation));
		nPlayers++;
	}
	
	public Player getPlayer(OwnerType owner) {
		if(owner == OwnerType.NATURE)
			return nature;
		return players.get(owner);
	}

	public int getNPlayer() {
		return nPlayers;
	}
	
	public Player getCurrentPlayer() {
		return players.get(OwnerType.PLAYER1);
	}
	
	public Nature getNature() {
		return nature;
	}
	
	public Unit addUnit(GameObjectType type, OwnerType owner, float x, float y, float z, PlayerCatalog playerCatalog) {
		Unit unit = GameObjectFactory.INSTANCE.createUnit(type, x , y, z, owner, playerCatalog);
		if(unit != null) {
			collision.addCollisionObject(unit.getCollisionObject());
			innerLogic.addObject(unit);
			getPlayer(owner).addUnit(unit);
		}
		return unit;
	}
	
	public Building addBuilding(GameObjectType type, OwnerType owner, float x, float y, float z, PlayerCatalog playerCatalog, boolean completed) {
		Building building = GameObjectFactory.INSTANCE.createBuilding(type, x , y, z, owner, playerCatalog, completed);
		if(building != null) {
			collision.addCollisionObject(building.getCollisionObject());
			innerLogic.addObject(building);
			getPlayer(owner).addBuilding(building);
		}
		return building;
	}
	
	public Resource addResource(GameObjectType type, float x, float y, float z) {
		Resource resource = GameObjectFactory.INSTANCE.createResource(type, x , y, z);
		if(resource != null) {
			collision.addCollisionObject(resource.getCollisionObject());
			innerLogic.addObject(resource);
			nature.addResource(resource);
		}
		return resource;
	}
	
	
	public GameObject addObject(GameObjectType type, float x, float y, float z) {
		GameObject o = GameObjectFactory.INSTANCE.create(type, x, y, z, null);
		if(o != null)
			innerLogic.addObject(o);
		return o;
	}
	
	public BuildingTemplate addBuildingTemplate(GameObjectType type) {
		BuildingTemplate o = GameObjectFactory.INSTANCE.createBuildingTemplate(type);
		if(o != null)
			innerLogic.addObject(o);
		return o;
	}
	
	public void removeBuildingTemplate(BuildingTemplate buildingTemplate) {
		innerLogic.removeObject(buildingTemplate);
	}
	
	public Vector3 getTerrainCollisionVector(Ray ray, boolean centered) {
		return terrain.getCollisionVector(ray, centered);
	}
	
	public int getSelectedId() {
		return innerLogic.getSelectedId();
	}
	
	public Selectable getSelectable(int id) {
		return innerLogic.getSelectable(id);
	}
	
	public Selectable getCurrentSelect() {
		return innerLogic.getSelectable(innerLogic.getSelectedId());
	}
	
	public void setSelectedId(int id) {
		innerLogic.setSelectedId(id);
	}
	
	public int getObject(Ray ray) {
		int resultId = -1;
		float distance = -1;
		Vector3 position = new Vector3();
		
		for(Integer i : innerLogic.getSelectablesIds()) {
			GameObject instance = innerLogic.getGameObject(i);
			
			instance.transform.getTranslation(position);
			position.add(instance.center);
			
            final float len = ray.direction.dot(position.x-ray.origin.x, position.y-ray.origin.y, position.z-ray.origin.z);
            if (len < 0f)
                continue;

            float dist2 = position.dst2(ray.origin.x+ray.direction.x*len, ray.origin.y+ray.direction.y*len, ray.origin.z+ray.direction.z*len);
            if (distance >= 0f && dist2 > distance) 
                continue;

            if (dist2 <= instance.radius * instance.radius) {
                resultId = instance.getId();
                distance = dist2;
            }
		}
		return resultId;
	}
	
	public boolean isSelected() {
		return innerLogic.isSelected();
	}
	
	public void update(float dt) {
		innerLogic.update(dt);
		collision.update();
	}
	
	public ArrayList<Renderble> getRenderables() {
		return innerLogic.getRenderable();
	}
	
	public boolean isAlly(OwnerType me, OwnerType other) {
		return diplomacy.isAlly(me, other);
	}
	
	public boolean isNeutral(OwnerType me, OwnerType other) {
		return diplomacy.isNeutral(me, other);
	}
	
	public boolean isEnemy(OwnerType me, OwnerType other) {
		return diplomacy.isEnemy(me, other);
	}
		
}
