package com.mygdx.engine.gamelogic.gameobject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.mygdx.engine.gamelogic.message.MessageData;
import com.mygdx.engine.gamelogic.message.MessageType;
import com.mygdx.engine.gamelogic.player.Player;
import com.mygdx.engine.gamelogic.player.PlayerCatalog;

public abstract class Building extends GameObject implements Renderble, Selectable, Updatable{

	private PlayerCatalog playerCatalog;
	private OwnerType owner;
	private boolean changed;
	
	private int hitPoints;
	private int currentHitPoints;
	private double attackPower;
	private double attackRate;
	private double range;
	private double armor;
	
	private boolean completed;
	
	private Queue<GameObjectType> queue;
	private int currentCompletion;
	private Timer.Task timer;
	private boolean asOrders;
	private GameObjectType currentOrder;
	private boolean processingOrder;
	
	public Building(Model model, GameObjectType type, String name, OwnerType owner, int id, PlayerCatalog playerCatalog, int width, int height, int depth, boolean completed, int hitpoints, btCollisionShape collisionshape) {
		super(model, type, name, id, width, height, depth, collisionshape);
		this.playerCatalog = playerCatalog;
		this.owner = owner;
		changed = false;
		this.completed = completed;
		this.hitPoints = hitpoints;
		if(completed) {
			currentHitPoints = hitPoints;
		} else {
			currentHitPoints = 0;
			setHeight(-getHeigth() / 2);
		}
		
		queue = new LinkedList<GameObjectType>();
		currentCompletion = 0;
		timer = null;
		asOrders = false;
		currentOrder = null;
		processingOrder = false;
	}
	
	@Override
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
		if(currentOrder != null) {
			data.put(MessageData.CURRENTPROCESSING, currentOrder.toString());
			data.put(MessageData.PROCESSINGCOUNTER, Integer.toString(currentCompletion));
			
			StringBuilder sb = new StringBuilder();
			for(GameObjectType type : queue) {
				sb.append(type.toString() + ":");
			}
			data.put(MessageData.QUEUE, sb.toString());
		}
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

	public void addHealth(long amount) {
		if(amount > 0) {
			if(amount + currentHitPoints >= hitPoints)
				currentHitPoints = hitPoints;
			else
				currentHitPoints += amount;
		}
	}
	
	public void subtractHealth(long amount) {
		if(amount > 0) {
			if(currentHitPoints - amount <= 0)
				currentHitPoints = 0;
			else
				currentHitPoints -= amount;
		}
	}
	
	public boolean isCompleted() {
		return completed;
	}
	
	public void construct(int n) {
		if(!completed) {
			if(n + currentHitPoints > hitPoints) {
				currentHitPoints = hitPoints;
				completed = true;
			} else {
				currentHitPoints += n;
				System.out.println("Cur:" + currentHitPoints);
				Vector3 v = new Vector3();
				transform.getTranslation(v);
			}
		}
	}
	
	@Override
	public void update(float dt) {
		if(!completed && currentHitPoints > 0) {
			Quaternion q = new Quaternion();
			transform.getRotation(q);
			Vector3 v = new Vector3();
			transform.getTranslation(v);
			
			v.y = -1.0f * getHeigth() * currentHitPoints / hitPoints;
			transform.set(v, q);
			
		}
		
		if(asOrders && !processingOrder) {
			currentOrder = queue.poll();
			asOrders = !queue.isEmpty();
			processingOrder = true;
			timer = Timer.schedule(new Task() {

				@Override
				public void run() {
					if(currentCompletion < 100) {
						currentCompletion += 50;
						setChanged();
					} else {
						createUnit(currentOrder);
						currentCompletion = 0;
						processingOrder = false;
						currentOrder = null;
						setChanged();
						timer.cancel();
					}
				}
				
			}, 1, 1);
		}
	}
	
	public void addOrder(GameObjectType type) {
		queue.add(type);
		asOrders = true;
		setChanged();
	}
	
	public boolean executeOrder(MessageType type, Map<MessageData, String> data) {
		
		return false;
	}
	
	public abstract void createUnit(GameObjectType type);

}
