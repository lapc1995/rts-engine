package com.mygdx.engine.gamelogic.gameobject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import com.badlogic.gdx.math.Vector2;

public enum MapPositions {
	INSTANCE;
	
	private int[][] map = null;
	private int size = 40;
	private Map<Integer, ArrayList<Vector2>> idPositions;
	private Map<GameObjectType, ArrayList<Integer>> typeIds;
	
	private void createMap() {
		typeIds = new HashMap<GameObjectType, ArrayList<Integer>>();
		idPositions = new HashMap<Integer, ArrayList<Vector2>>();
		map = new int[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				map[i][j] = -1;
			}
		}
	}
	
	public void enterPosition(int x, int y, int id, GameObjectType type) {
		if(map == null)
			createMap();
		if(map[x][y] == -1) {
				map[x][y] = id;
			if(idPositions.containsKey(id)) {
				ArrayList<Vector2> list = idPositions.get(id);
				list.add(new Vector2(x,y));
				idPositions.remove(id);
				idPositions.put(id, list);
			} else {
				ArrayList<Vector2> list = new ArrayList<Vector2>();
				list.add(new Vector2(x, y));
				idPositions.put(id, list);
			}
			if(typeIds.containsKey(type)) {
				ArrayList<Integer> ids = typeIds.get(type);
				ids.add(id);
				typeIds.remove(type);
				typeIds.put(type, ids);
			} else {
				ArrayList<Integer> ids = new ArrayList<Integer>();
				ids.add(id);
				typeIds.put(type, ids);
			}
		}
	}
	
	public void exitPosition(int x, int y, int id, GameObjectType type) {
		if(map == null)
			createMap();
		if(map[x][y] == id) {
			map[x][y] = -1;
			ArrayList<Vector2> list = idPositions.get(id);
			list.remove(new Vector2(x, y));
			idPositions.remove(id);
			idPositions.put(id, list);
		}
	}
	
	public Vector2 getAvailablePositionAround(int id) {
		ArrayList<Vector2> list = idPositions.get(id);
		for(Vector2 v : list) {
			Vector2 result = checkNeighbors((int)v.x, (int)v.y, -1);
			if(result != null) {
				return result;
			}
		}
		return null;
	}
	
	public Vector2 getNearestAvailablePositionAround(Vector2 current, int id) {
		ArrayList<Vector2> list = idPositions.get(id);
		Vector2 result = null;
		float distance = Float.MAX_VALUE;
		for(Vector2 v : list) {
			Vector2 aux = checkNearestNeighbors(current, (int)v.x, (int)v.y, -1); 
			if(aux != null) {
				float distanceAux = current.dst(aux);
				if(distanceAux < distance) {
					distance = distanceAux;
					result = aux;
				}
			}
		}
		return result;
	}
	
	private Vector2 checkNeighbors(int x, int y, int id) {
		if(y-1 >= 0 && map[x][y-1] == id)
			return new Vector2(x, y-1);
		else if(y+1 < size &&  map[x][y+1] == id)
			return new Vector2(x, y+1);
		else if(x-1 >= 0 && map[x-1][y] == id)
			return new Vector2(x-1, y);
		else if(x+1 < size && map[x+1][y] == id)
			return new Vector2(x+1, y);
		return null;
	}
	
	private Vector2 checkNearestNeighbors(Vector2 current, int x, int y, int id) {
		Vector2 result = null;
		float distance = Float.MAX_VALUE;
		
		if(y-1 >= 0 && map[x][y-1] == id) {
			float aux = current.dst(x, y - 1);
			if(aux < distance) {
				distance = aux;
				result = new Vector2(x, y-1);
			}
		}
		if(y+1 < size &&  map[x][y+1] == id){
			float aux = current.dst(x, y + 1);
			if(aux < distance) {
				distance = aux;
				result = new Vector2(x, y + 1);
			}
		}
		if(x-1 >= 0 && map[x-1][y] == id){
			float aux = current.dst(x - 1, y);
			if(aux < distance) {
				distance = aux;
				result = new Vector2(x - 1, y);
			}
		}
		if(x+1 < size && map[x+1][y] == id){
			float aux = current.dst(x + 1, y);
			if(aux < distance) {
				distance = aux;
				result = new Vector2(x + 1, y);
			}
		}
		return result;
	}
	
	public void printMap() {
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(map[i][j] == -1)
					System.out.print(". ");
				else
					System.out.print(map[i][j] + " ");

			}
			System.out.println();
		}
		System.out.println("end");
	}
	
	public boolean idIsNeighborToOtherId(int id, int otherId) {
		for(Vector2 v : idPositions.get(id)) {
			if(checkNeighbors((int)v.x, (int)v.y, otherId) != null)
				return true;
		}
		return false;
	}
	
	public int getNearestGameObject(GameObjectType type, Vector2 currentPosition) {
		ArrayList<Integer> ids = typeIds.get(type);
		int result = -1;
		float distance = Float.MAX_VALUE;
		for(Integer id : ids) {
			for(Vector2 v : idPositions.get(id)) {
				float aux = v.dst(currentPosition.x, currentPosition.y);
				if(aux < distance) {
					distance = aux;
					result = id;
				}
			}
		}
		return result;
	}
	
	public int getNearestGameObject(Vector2 currentPosition, int... ids) {
		int result = -1;
		float distance = Float.MAX_VALUE;
		for(Integer id : ids) {
			if(id != -1) {
				for(Vector2 v : idPositions.get(id)) {
					float aux = v.dst(currentPosition.x, currentPosition.y);
					if(aux < distance) {
						distance = aux;
						result = id;
					}
				}
			}
		}
		return result;
	}
	
	public Stack<Vector2> aStarSearch(Vector2 startPoint, Vector2 goalPoint) {

		startPoint = new Vector2((int)startPoint.x, (int)startPoint.y);
		goalPoint = new Vector2((int)goalPoint.x, (int)goalPoint.y);
		
		Map<Vector2, Double> gScore = new HashMap<Vector2, Double>();
		final Map<Vector2, Double> fScore = new HashMap<Vector2, Double>();
		
		Set<Vector2> closedSet = new HashSet<Vector2>();
		Queue<Vector2> openSet = new PriorityQueue<Vector2>(new Comparator<Vector2>(){

			@Override
			public int compare(Vector2 v1, Vector2 v2) {
				return fScore.get(v1).intValue() - fScore.get(v2).intValue();
			}
		}
		);

		Map<Vector2, Vector2> cameFrom = new HashMap<Vector2, Vector2>();
		
		openSet.add(startPoint);
		gScore.put(startPoint, 0.0);
		fScore.put(startPoint, heuristic(startPoint, goalPoint));
		
		while(!openSet.isEmpty()) {
			
			Vector2 current = openSet.poll();
			if(current.equals(goalPoint))
				return reconstructPath(cameFrom, current);
						
			openSet.remove(current);
			closedSet.add(current);
			
			ArrayList<Vector2> ns = getNeighbors(current);
			
			for(Vector2 neighbor : ns) {
				if(closedSet.contains(neighbor))
					continue;
				
				double tentative_gScore = gScore.get(current) + current.dst(neighbor);
				if(!openSet.contains(neighbor)) {
					fScore.put(neighbor, Double.MAX_VALUE);
					gScore.put(neighbor, Double.MAX_VALUE);
					openSet.add(neighbor);
				} else if(tentative_gScore >= gScore.get(neighbor))
					continue;
				
				cameFrom.put(neighbor, current);
				gScore.put(neighbor, tentative_gScore);
				double heu = heuristic(neighbor, goalPoint);
				fScore.put(neighbor, gScore.get(neighbor) + heu);
				//System.out.println("Node:" + neighbor + " g:" + gScore.get(neighbor) + " f:" + fScore.get(neighbor) + " h:" + heu + "real:" + neighbor.dst(goalPoint));
			}
		}
		
		return null;
	}
	
	private Stack<Vector2> reconstructPath(Map<Vector2, Vector2> cameFrom, Vector2 current) {
		Stack<Vector2> path = new Stack<Vector2>();
		path.push(current);
		while(cameFrom.containsKey(current)) {
			path.push(cameFrom.get(current));
			current = cameFrom.get(current);
		}
		path.pop();
		return path;
	}
	
	private double diagonalDistance(Vector2 v1, Vector2 v2) {
		double d1 = 1;
		double d2 = Math.sqrt(2);
		int dx = Math.abs(Math.round(v1.x) - Math.round(v2.x));
		int dy = Math.abs(Math.round(v1.y) - Math.round(v2.y));
		return d1 * (dx + dy) + (d2 - 2 * d1) * Math.min(dx, dy); 
	}
	
	private double euclideanDistance(Vector2 v1, Vector2 v2) {
		int dx = Math.abs(Math.round(v1.x) - Math.round(v2.x));
		int dy = Math.abs(Math.round(v1.y) - Math.round(v2.y));
		return 1 * Math.sqrt(dx * dx + dy * dy);
	}
	
	private double heuristic(Vector2 v1, Vector2 v2) {
		return euclideanDistance(v1, v2);
	}
	
	

	private ArrayList<Vector2> getNeighbors(Vector2 v) {
		ArrayList<Vector2> result = new ArrayList<Vector2>();
		int x = Math.round(v.x);
		int y = Math.round(v.y);
		
		if(y-1 >= 0) {
			if(x-1 >= 0 && map[x-1][y-1] == -1)
				result.add(new Vector2(x-1, y-1));
			if(x >= 0 && map[x][y-1] == -1)
				result.add(new Vector2(x, y-1));
			if(x+1 < size && map[x+1][y-1] == -1)
				result.add(new Vector2(x+1, y-1));
		} 
		if(y+1 < size) {
			if(x-1 >= 0 && map[x-1][y+1] == -1)
				result.add(new Vector2(x-1, y+1));
			if(x >= 0 && map[x][y+1] == -1)
				result.add(new Vector2(x, y+1));
			if(x+1 < size && map[x+1][y+1] == -1)
				result.add(new Vector2(x+1, y+1));
		} 
		if(x + 1 < size && map[x+1][y] == -1)
			result.add(new Vector2(x+1, y));
		if(x - 1 >= 0 && map[x - 1][y] == -1)
			result.add(new Vector2(x-1, y));
		
		return result;
	}
	
	public Stack<Vector2> moveTo(int currentId, int goalId) {
		Vector2 start = idPositions.get(currentId).get(0);
		Vector2 goal =   getNearestAvailablePositionAround(start, goalId); 
	
		return aStarSearch(start, goal);
	}
	
	public Stack<Vector2> moveTo(int currentId, int x, int y) {
		Vector2 start = idPositions.get(currentId).get(0);
		return aStarSearch(start, new Vector2(x,y));
	}

}
