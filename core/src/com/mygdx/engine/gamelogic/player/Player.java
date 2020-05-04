package com.mygdx.engine.gamelogic.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mygdx.engine.gamelogic.gameobject.Building;
import com.mygdx.engine.gamelogic.gameobject.GameObjectType;
import com.mygdx.engine.gamelogic.gameobject.Unit;
import com.mygdx.engine.gamelogic.message.MessageData;

public class Player {
	
	private String name;
	
	private int wood;
	private int food;
	private int stone;
	private int gold;
	
	private Map<Integer, Unit> units;
	private Map<Integer, Building> buildings;
	
	private ArrayList<GameObjectType> availableBuildings;
	
	private int currentPopulation;
	private int maxPopulation;
	
	private boolean changed;
	
	public Player(String name, int wood, int food, int stone, int gold, int maxPopulation){
		this.name = name;
		this.wood = wood;
		this.food = food;
		this.stone = stone;
		this.gold = gold;
		
		this.units = new HashMap<Integer, Unit>();
		this.buildings = new HashMap<Integer, Building>();
		
		this.currentPopulation = 0;
		this.maxPopulation = maxPopulation;
		
		changed = true;
		
		availableBuildings = new ArrayList<GameObjectType>();
		availableBuildings.add(GameObjectType.MININGCAMP);
		availableBuildings.add(GameObjectType.MININGCAMP);
		
	}
	
	public void addUnit(Unit unit) {
		currentPopulation++;
		units.put(unit.getId(), unit);
	}
	
	public void addBuilding(Building building) {
		buildings.put(building.getId(), building);
	}
	
	public int getWood() {
		return wood;
	}
	
	public int getFood() {
		return food;
	}
	
	public int getStone() {
		return stone;
	}
	
	public int getGold() {
		return gold;
	}
	
	public int getCurrentPopulation() {
		return currentPopulation;
	}
	
	public int getMaxPopulation() {
		return maxPopulation;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean hasUnit(int id) {
		return units.containsKey(id);
	}
	
	public boolean hasBuilding(int id) {
		return buildings.containsKey(id);
	}
	
	public Unit getUnit(int id) {
		return units.get(id);
	}
	
	public Building getBuilding(int id) {
		return buildings.get(id);
	}
	
	public void addGold(int value) {
		gold += value;
		System.out.println("Gold" + gold);
	}
	
	public void addStone(int value) {
		stone += value;
	}
	
	public void addFood(int value) {
		food += value;
	}
	
	public void addWood(int value) {
		wood += value;
	}

	public Map<MessageData, String> dataToBeSend() {
		Map<MessageData, String> data = new HashMap<MessageData, String>();
		data.put(MessageData.FOOD, Integer.toString(food));
		data.put(MessageData.WOOD, Integer.toString(wood));
		data.put(MessageData.STONE, Integer.toString(stone));
		data.put(MessageData.GOLD, Integer.toString(gold));
		data.put(MessageData.CURRENTPOPULATION, Integer.toString(currentPopulation));
		data.put(MessageData.MAXPOPULATION, Integer.toString(maxPopulation));
		return data;
	}
	
	public void setChanged() {
		changed = true;
	}
	
	public boolean isChanged() {
		return changed;
	}

	public ArrayList<GameObjectType> getAvailableList() {
		return availableBuildings;
	}

}
