package com.mygdx.engine.gamelogic.gameobject;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.mygdx.engine.gamelogic.message.MessageData;
import com.mygdx.engine.gamelogic.player.Player;
import com.mygdx.engine.gamelogic.player.PlayerCatalog;

public abstract class Unit extends GameObject implements Renderble, Selectable, Updatable{

	private Vector2 startPos;
	private Vector2 lastPos;
	private Vector2 endPos;
	
	private boolean move;
	private float speed;
	private float elapsed;
	private Vector2 direction;
	private float distance;
	
	private int hitPoints;
	private int currentHitPoints;
	private double attackPower;
	private double attackRate;
	private double range;
	private double armor;
	
	private boolean changed;
	
	private OwnerType owner;
	
	private GameObject selected;
	
	private boolean onActivity;
	private ActivityType currentActivity;
	private ActivityType nextActivity;
	
	private PlayerCatalog playerCatalog;
	
	private Unit unit;
	private Timer.Task timer;
	
	private Stack<Vector2> movePoints;
	
	public Unit(Model model, GameObjectType type, String name, OwnerType owner, int id, PlayerCatalog playerCatalog, int width, int height, int depth, btCollisionShape collisionshape) {
		super(model, type, name, id, width, height, depth, collisionshape);
		move = false;
		speed = 10;
		elapsed = 0.01f;
		startPos = new Vector2();
		endPos = new Vector2();
		lastPos = new Vector2();
		
		hitPoints = currentHitPoints = 1;
		attackPower = 1;
		attackRate = 1;
		range = 0;
		armor = 0;
		
		selected = null;
		
		this.owner = owner;
		
		onActivity = false;
		
		currentActivity = ActivityType.NONE;
		nextActivity = ActivityType.NONE;
		
		changed = false;
		
		this.playerCatalog = playerCatalog;
		
		unit = null;
		
		timer = null;
		
		movePoints = null;
	}
	
	public abstract void kill();
	
	public void moveTo(float x, float y) {
		movePoints = MapPositions.INSTANCE.moveTo(getId(), (int)x, (int)y);
		Vector2 v = movePoints.pop();
		moveToPrivate(v.x, v.y);
	}
	
	private void moveToPrivate(float x, float y) {
		Vector3 v = new Vector3();
		super.transform.getTranslation(v);
		startPos = new Vector2(v.x, v.z);
		lastPos = new Vector2(v.x, v.z);
		endPos = new Vector2(x + 0.5f, y  + 0.5f);
		distance = Vector2.dst(startPos.x, startPos.y, endPos.x, endPos.y);
		Vector2 aux = new Vector2(endPos.x - startPos.x, endPos.y - startPos.y);
		direction = aux.nor();
		move = true;
		currentActivity = ActivityType.MOVING;
	}
	
	public void update(float dt) {
		if(move) {
			super.transform.trn(direction.x * speed * elapsed, 0, direction.y * speed * elapsed);
			Vector3 v = new Vector3();
			super.transform.getTranslation(v);
			MapPositions.INSTANCE.exitPosition((int)lastPos.x, (int)lastPos.y, getId(), getType());
			MapPositions.INSTANCE.enterPosition((int)v.x, (int)v.z, getId(), getType());
			lastPos.x = v.x;
			lastPos.y = v.z;
			super.transform.getTranslation(v);
			getCollisionObject().setWorldTransform(this.transform);
			if(Vector2.dst(startPos.x, startPos.y, v.x, v.z) >= distance) {
				if(movePoints.isEmpty()) {
					move = false;
					currentActivity = ActivityType.NONE;
				} else {
					Vector2 u = movePoints.pop();
					moveToPrivate(u.x, u.y);
				}
			}
		}
		
		if(currentActivity == ActivityType.NONE && nextActivity != ActivityType.NONE) {
			changeToNextActivity();
		}
	}
	
	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(int hitPoints) {
		this.hitPoints = hitPoints;
	}

	public int getCurrentHitPoints() {
		return currentHitPoints;
	}

	public void setCurrentHitPoints(int currentHitPoints) {
		this.currentHitPoints = currentHitPoints;
	}

	public double getAttackPower() {
		return attackPower;
	}

	public void setAttackPower(double attackPower) {
		this.attackPower = attackPower;
	}

	public double getAttackRate() {
		return attackRate;
	}

	public void setAttackRate(double attackRate) {
		this.attackRate = attackRate;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	public double getArmor() {
		return armor;
	}

	public void setArmor(double armor) {
		this.armor = armor;
	}

	public void addHealth(int amount) {
		if(amount > 0) {
			if(amount + currentHitPoints >= hitPoints)
				currentHitPoints = hitPoints;
			else
				currentHitPoints += amount;
		}
	}
	
	public void subtractHealth(int amount) {
		if(amount > 0) {
			if(currentHitPoints - amount <= 0)
				currentHitPoints = 0;
			else
				currentHitPoints -= amount;
		}
	}
	
	public void render(ModelBatch modelBatch, Environment environment) {
		modelBatch.render(this, environment);
	}
	
	@Override
	public Map<MessageData, String> dataToBeSent() {
		Map<MessageData, String> data = new HashMap<MessageData, String>();
		data.put(MessageData.NAME, getName());
		data.put(MessageData.OWNER, owner.toString());
		data.put(MessageData.HITPOINTS, Integer.toString(hitPoints));
		data.put(MessageData.CURRENTHITPOINTS, Integer.toString(currentHitPoints));
		data.put(MessageData.ATTACKPOWER, Double.toString(attackPower));
		data.put(MessageData.ATTACKRATE, Double.toString(attackRate));
		data.put(MessageData.RANGE, Double.toString(range));
		data.put(MessageData.ARMOR, Double.toString(armor));
		
		changed = false;
		
		return data;
	}
	
	@Override
	public OwnerType getOwner() {
		return owner;
	}
	
	@Override
	public void selectedOn() {
		materials.get(0).set(ColorAttribute.createDiffuse(Color.RED));
	}
	
	@Override
	public void selectedOff() {
		materials.get(0).set(ColorAttribute.createDiffuse(Color.BLUE));
	}
	
	public boolean changeToNextActivity() {
		if(currentActivity == ActivityType.NONE) {
			currentActivity = nextActivity;
			nextActivity = ActivityType.NONE;
			switch(currentActivity) {
			
				case ATTACKUNIT:
					if(MapPositions.INSTANCE.idIsNeighborToOtherId(getId(), unit.getId())) {
						timer = Timer.schedule(new Task() {
	
							@Override
							public void run() {
								ActivityType current = getCurrentActivity();
								if(current == ActivityType.ATTACKUNIT) {
									unit.subtractHealth(1);
									unit.setChanged();
								} else
									timer.cancel();
							}
							
						}, 1, 1);
					}
					return true;
					
				default:
					return false;
			}
		}
		return false;
	}
	
	public void setNextActivity(ActivityType type) {
		nextActivity = type;
	}

	public void setCurrentActivity(ActivityType type) {
		currentActivity = type;
	}
	
	public ActivityType getCurrentActivity() {
		return currentActivity;
	}
	
	public ActivityType getNextActivity() {
		return nextActivity;
	}
	
	@Override
	public void setChanged() {
		changed = true;
	}
	
	@Override
	public boolean isChanged() {
		return changed;
	}
	
	public Player getPlayer() {
		return playerCatalog.getPlayer(getOwner());
	}
	
	public PlayerCatalog getPlayerCatalog() {
		return playerCatalog;
	}
	
	public void goToUnit(Unit unit) {
		this.unit = unit;
		//if(!playerCatalog.isAlly(getOwner(), unit.getOwner())) {
			setNextActivity(ActivityType.ATTACKUNIT);
			goTo(unit.getId()); 
		//}
	}
	
	public void goTo(int id) {
		Vector2 v = MapPositions.INSTANCE.getAvailablePositionAround(id);
		if(v != null) 
			moveTo(v.x + 0.5f, v.y + 0.5f);
	}
	
	@Override
	public boolean workWithId(int id) {
		Selectable go = getPlayerCatalog().getSelectable(id);
		if(go.getOwner() == OwnerType.NATURE) {

		} else {
			Player p = getPlayerCatalog().getPlayer(go.getOwner());
			if(p.hasUnit(id)) {
				Unit u = getPlayerCatalog().getCurrentPlayer().getUnit(id);
				goToUnit(u);
				return true;
			} else if (p.hasBuilding(id)) {

			}	
		}
		return false;
	}
}
