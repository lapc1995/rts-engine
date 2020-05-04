package com.mygdx.engine.gamelogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mygdx.engine.gamelogic.gameobject.GameObject;
import com.mygdx.engine.gamelogic.gameobject.Renderble;
import com.mygdx.engine.gamelogic.gameobject.Selectable;
import com.mygdx.engine.gamelogic.gameobject.Updatable;

public class InnerLogic {
	
	private Map<Integer, GameObject> allObjects;
	private ArrayList<Updatable> updatableObjects;
	private ArrayList<Renderble> renderableObjects;
	private Map<Integer, Selectable>  selectableObjects;
	private int selectedId;
	
	public InnerLogic(){
		allObjects = new HashMap<Integer, GameObject>();
		updatableObjects = new ArrayList<Updatable>();
		renderableObjects = new ArrayList<Renderble>();
		selectableObjects = new HashMap<Integer, Selectable> ();
		selectedId = -1;
	}
	
	public void update(float dt) {
		for(Updatable up : updatableObjects)
			up.update(dt);
	}
	
	public ArrayList<Renderble> getRenderable() {
		return renderableObjects;
	}
	
	public Set<Integer> getSelectablesIds() {
		return selectableObjects.keySet();
	}
	
	public void addObject(GameObject obj) {
		allObjects.put(obj.getId(), obj);
		if(obj instanceof Renderble)
			renderableObjects.add((Renderble) obj);
		if(obj instanceof Updatable)
			updatableObjects.add((Updatable) obj);
		if(obj instanceof Selectable)
			selectableObjects.put(obj.getId(), (Selectable) obj);
	}
	
	public void removeObject(GameObject obj) {
		allObjects.remove(obj);
		if(obj instanceof Renderble)
			renderableObjects.remove((Renderble) obj);
		if(obj instanceof Updatable)
			updatableObjects.remove((Updatable) obj);
		if(obj instanceof Selectable)
			selectableObjects.remove((Selectable)obj);
	}
	
    public int getSelectedId() {
    	return selectedId;
    }
    
    public void addRenderble(Renderble r) {
    	renderableObjects.add(r);
    }
    
    public GameObject getGameObject(int id) {
    	if(id< 0)
    		return null;
    	return allObjects.get(id);
    }
    
    public Selectable getSelectable(int id) {
    	if(id< 0)
    		return null;
    	return selectableObjects.get(id);
    }
    
    public void setSelectedId(int id) {
    	if(selectedId != -1)
    		getSelectable(selectedId).selectedOff();
    	selectedId = id;
    	if(selectedId != -1)
    		getSelectable(selectedId).selectedOn();
    }
    
    public boolean isSelected() {
    	return selectedId != -1;
    }
    
    public Map<Integer, GameObject> getAllObjects() {
    	return allObjects;
    }
}
