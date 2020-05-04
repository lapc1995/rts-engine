package com.mygdx.engine.gamelogic.gameobject;

import java.util.HashMap;
import java.util.Map;

import com.mygdx.engine.gamelogic.player.Player;

public class Diplomacy {
	
	Map<OwnerType,Map<OwnerType, DiplomacyType>> diplomacy;
	
	public Diplomacy(Map<OwnerType, Player> players){
		Map<OwnerType, DiplomacyType> aux = new HashMap<OwnerType, DiplomacyType>();
		for(OwnerType type : players.keySet())
			aux.put(type, DiplomacyType.NEUTRAL);
		
		for(OwnerType type : players.keySet())
			diplomacy.put(type, new HashMap<OwnerType, DiplomacyType>(aux));
	}
	
	private void setDiplomacy(OwnerType me, OwnerType other, DiplomacyType type) {
		diplomacy.get(me).remove(other);
		diplomacy.get(me).put(other, type);
	}
	
	public void setAlly(OwnerType me, OwnerType other) {
		setDiplomacy(me, other, DiplomacyType.ALLY);
	}

	public void setNeutral(OwnerType me, OwnerType other) {
		setDiplomacy(me, other, DiplomacyType.NEUTRAL);
	}
	
	public void setEnemy(OwnerType me, OwnerType other) {
		setDiplomacy(me, other, DiplomacyType.ENEMY);
	}
	
	public boolean isAlly(OwnerType me, OwnerType other) {
		return diplomacy.get(me).get(other) == DiplomacyType.ALLY;
	}
	
	public boolean isNeutral(OwnerType me, OwnerType other) {
		return diplomacy.get(me).get(other) == DiplomacyType.NEUTRAL;
	}
	
	public boolean isEnemy(OwnerType me, OwnerType other) {
		return diplomacy.get(me).get(other) == DiplomacyType.ENEMY;
	}
}
