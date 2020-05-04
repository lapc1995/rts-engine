package com.mygdx.engine.gamelogic.gameobject.unit;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import com.mygdx.engine.gamelogic.gameobject.ActivityType;
import com.mygdx.engine.gamelogic.gameobject.Building;
import com.mygdx.engine.gamelogic.gameobject.GameObjectType;
import com.mygdx.engine.gamelogic.gameobject.MapPositions;
import com.mygdx.engine.gamelogic.gameobject.OwnerType;
import com.mygdx.engine.gamelogic.gameobject.Resource;
import com.mygdx.engine.gamelogic.gameobject.Selectable;
import com.mygdx.engine.gamelogic.gameobject.Unit;
import com.mygdx.engine.gamelogic.gameobject.building.ResourceCollector;
import com.mygdx.engine.gamelogic.gameobject.resource.ResourceType;
import com.mygdx.engine.gamelogic.message.MessageData;
import com.mygdx.engine.gamelogic.message.MessageType;
import com.mygdx.engine.gamelogic.player.Player;
import com.mygdx.engine.gamelogic.player.PlayerCatalog;

public class Villager extends Unit{
	
	private int maxCarryAmount;
	private int currentCarryAmount;
	private ResourceType carryType;
	private Resource resourcePlace;
	private ResourceCollector resourceCollector;
	private Timer.Task timer;
	
	private Building building;
	
	private boolean autoCollecting;
	
	public Villager(Model model, OwnerType owner, int id, PlayerCatalog playerCatalog) {
		super(model, GameObjectType.VILLAGER, "Villager", owner, id, playerCatalog, 1 , 1, 1, new btBoxShape(new Vector3(1, 1, 1)));
		setHitPoints(25);
		setCurrentHitPoints(25);
		setAttackPower(3);
		setAttackRate(2.07);
		setSpeed(5f);
		
		maxCarryAmount = 1;
		currentCarryAmount = 0;
		
		carryType = null;
		resourcePlace = null;
		resourceCollector = null;
	
		autoCollecting = false;
		
		building = null;
		

		
	}

	@Override
	public void kill() {
		// TODO Auto-generated method stub
		
	}
	
	private void addResource(long amount) {
		if(amount > 0) {
			if(currentCarryAmount + amount > maxCarryAmount)
				currentCarryAmount = maxCarryAmount;
			else {
				currentCarryAmount += amount;
			}
		}
	}
	
	public void addWood(int amount) {
		carryType = ResourceType.WOOD;
		addResource(amount);
	}
	
	public void addFood(int amount) {
		carryType = ResourceType.FOOD;
		addResource(amount);
	}
	
	public void addGold(int amount) {
		carryType = ResourceType.GOLD;
		addResource(amount);
	}
	
	public void addStone(int amount) {
		carryType = ResourceType.STONE;
		addResource(amount);
	}
	
	public void setResource(Resource resource) {
		this.resourcePlace = resource;
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
		
		if(autoCollecting) {
			if(currentCarryAmount == maxCarryAmount && getCurrentActivity() != ActivityType.MOVING) {
				Vector3 position = new Vector3();
				transform.getTranslation(position);
				int id = -1;
				if(carryType == ResourceType.WOOD) {
					id = MapPositions.INSTANCE.getNearestGameObject(GameObjectType.TOWNCENTER, new Vector2(position.x, position.z));
				} else if (carryType == ResourceType.GOLD || carryType == ResourceType.STONE) {
					int id1 = MapPositions.INSTANCE.getNearestGameObject(GameObjectType.MININGCAMP, new Vector2(position.x, position.z));
					int id2 = MapPositions.INSTANCE.getNearestGameObject(GameObjectType.TOWNCENTER, new Vector2(position.x, position.z));
					id = MapPositions.INSTANCE.getNearestGameObject(new Vector2(position.x, position.z), id1, id2);
				} else if (carryType == ResourceType.FOOD) {
					id = MapPositions.INSTANCE.getNearestGameObject(GameObjectType.TOWNCENTER, new Vector2(position.x, position.z));
				}
				goToResourceCollector((ResourceCollector)getPlayer().getBuilding(id));
			}
			else if(getCurrentActivity() == ActivityType.NONE) {
				 goToResource(resourcePlace);
			}
		}
	}
	
	public void goToResource(Resource resource) {
		autoCollecting = true;
		resourcePlace = resource;
		switch(resource.getResourceType()) {
		
			case FOOD:
				setNextActivity(ActivityType.FORAGING);
				break;
				
			case GOLD:
				setNextActivity(ActivityType.GOLD_MINNING);
				break;
				
			case STONE:
				setNextActivity(ActivityType.STONE_MINING);
				break;
				
			case WOOD:
				setNextActivity(ActivityType.LOGGING);
				break;
				
			default:
				break;
		}

		Vector3 aux = new Vector3();
		this.transform.getTranslation(aux);
		Vector2 v = MapPositions.INSTANCE.getNearestAvailablePositionAround(new Vector2(aux.x, aux.z), resource.getId());
		//Vector2 v = MapPositions.INSTANCE.getAvailablePositionAround(resource.getId());
		if(v != null) 
			moveTo(v.x, v.y);
	}
	
	public void goToBuilding(Building building) {
		this.building = building;
		if(!building.isCompleted()) {
			setNextActivity(ActivityType.BUILDING);
			Vector3 aux = new Vector3();
			this.transform.getTranslation(aux);
			Vector2 v = MapPositions.INSTANCE.getNearestAvailablePositionAround(new Vector2(aux.x, aux.z), building.getId());
			if(v != null) 
				moveTo(v.x, v.y);
		} else {
			if(building instanceof ResourceCollector) {
				goToResourceCollector((ResourceCollector)building);
			}
		}
	}
	
	@Override
	public boolean changeToNextActivity() {
		if(super.changeToNextActivity())
			return true;
		
		if(getCurrentActivity() != ActivityType.NONE) {
			
			switch(getCurrentActivity()) {
			
				case GOLD_MINNING:
				case STONE_MINING:
				case FORAGING:
				case LOGGING:
					super.changeToNextActivity();
					if(MapPositions.INSTANCE.idIsNeighborToOtherId(getId(), resourcePlace.getId())) {
						carryType = resourcePlace.getResourceType();
						timer = Timer.schedule(new Task() {

							@Override
							public void run() {
								ActivityType current = getCurrentActivity();
								if(current == ActivityType.GOLD_MINNING ||
								   current == ActivityType.STONE_MINING ||
								   current == ActivityType.FORAGING ||
								   current == ActivityType.LOGGING) {
									int n = resourcePlace.subtractAmount(1);
									if(n != 0) {
										currentCarryAmount += n;
										carryType = resourcePlace.getResourceType();
										setChanged();
									}else
										timer.cancel();
								} else
									timer.cancel();
							}
							
						}, 1, 1);
					}
					break;
				
				case MOVING:
					
					break;
				
				case NONE:
					break;
					
				case DROP_OFF:
					super.changeToNextActivity();
					if(MapPositions.INSTANCE.idIsNeighborToOtherId(getId(), resourceCollector.getId()))
						dropOffResources();
					break;
				
				case BUILDING:
					super.changeToNextActivity();
					if(MapPositions.INSTANCE.idIsNeighborToOtherId(getId(), building.getId())) {
						timer = Timer.schedule(new Task() {

							@Override
							public void run() {
								if(getCurrentActivity() == ActivityType.BUILDING) {
									if(!building.isCompleted()) {
										building.construct(1);
										building.setChanged();
									}
									else
										timer.cancel();
								}
							}
							
						}, 1, 1);
					}
					break;
					
				default:
					break;
			
			}
		}
		return true;
	}
	
	
	public void goToResourceCollector(ResourceCollector rc) {
		resourceCollector = rc;
		setNextActivity(ActivityType.DROP_OFF);

		Vector3 aux = new Vector3();
		this.transform.getTranslation(aux);
		Vector2 v = MapPositions.INSTANCE.getNearestAvailablePositionAround(new Vector2(aux.x, aux.z), rc.getId());
		if(v != null) 
			moveTo(v.x, v.y);
	}
	
	public void dropOffResources() {
		resourceCollector.addResource(carryType, currentCarryAmount);
		currentCarryAmount = 0;
		carryType = null;
		setCurrentActivity(ActivityType.NONE);
		setChanged();
	}
	
	@Override
	public Map<MessageData, String> dataToBeSent() {
		Map<MessageData, String>  data = super.dataToBeSent();
		if(carryType != null) {
			data.put(MessageData.RESOURCETYPE, carryType.toString());
			data.put(MessageData.RESOURCEAMOUNT, Integer.toString(currentCarryAmount));
		} else {
			data.put(MessageData.RESOURCERESET, null);
		}
		data.put(MessageData.BUTTON0, MessageType.CREATEBUILDINGTOWNCENTER.toString());
		data.put(MessageData.BUTTON1, MessageType.CREATEBUILDINGMININGCAMP.toString());
		
 		return data;
	}

	@Override
	public boolean executeOrder(MessageType type, Map<MessageData, String> data) {
		return false;
	}

	@Override
	public boolean workWithId(int id) {
		if(super.workWithId(id))
			return true;
		
		Selectable go = getPlayerCatalog().getSelectable(id);
		if(go.getOwner() == OwnerType.NATURE) {
			if(getPlayerCatalog().getNature().hasResource(id)) {
				Resource r = getPlayerCatalog().getNature().getResource(id);
				goToResource(r);
				return true;
			}
		} else {
			Player p = getPlayerCatalog().getPlayer(go.getOwner());
			if(p.hasUnit(id)) {
				
			} else if (p.hasBuilding(id)) {
				Building b = getPlayerCatalog().getCurrentPlayer().getBuilding(id);
				goToBuilding(b);
				return true;
			}	
		}
		return false;
	}

	@Override
	public void workWithVector(Vector3 vector) {
		moveTo(vector.x, vector.z);
	}
	
	@Override
	public void selectedOn() {
		Sound sound = Gdx.audio.newSound(Gdx.files.internal("roggan.mp3"));
		sound.play(1.0f);
		super.selectedOn();
	}
	
}
