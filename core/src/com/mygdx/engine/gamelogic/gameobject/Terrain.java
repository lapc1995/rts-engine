package com.mygdx.engine.gamelogic.gameobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.mygdx.engine.temp.Tile;
import com.mygdx.engine.temp.TileType;


public class Terrain implements Renderble{
	
	private final float TILE_SIZE = 1f;
	
	private Model model;
	private ModelInstance instance;
	private Tile[][] tiles;
	private TextureAtlas atlas;
	private BoundingBox[][] boxes;
	private ModelInstance grid;
	
	private boolean gridOn;
	
	public Terrain(int sizeX, int sizeY) {
		tiles = new Tile[sizeX][sizeY];
		boxes = new BoundingBox[sizeX][sizeY];
        for(int i = 0; i < sizeX; i += TILE_SIZE) {
        	for(int j = 0; j < sizeY; j += TILE_SIZE) {
        		if( i % 2 == 0)
        			tiles[i][j] = new Tile(TileType.Dirt);
        		else
        			tiles[i][j] = new Tile(TileType.Grass);
        	}
        }

        atlas = new TextureAtlas(Gdx.files.internal("terrain.pack"));
        Texture ter = new Texture(Gdx.files.internal("terrain.png"));
        
        ModelBuilder modelBuilder = new ModelBuilder();
        MeshPartBuilder meshPartBuilder;
        
        modelBuilder.begin();
        meshPartBuilder = modelBuilder.part("1", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.TextureCoordinates , new Material(TextureAttribute.createDiffuse(ter)));
        int counter = 0;
        int counterMax = 2500;
        for(int i = 0; i < sizeX; i += TILE_SIZE) {
        	for(int j = 0; j < sizeY; j += TILE_SIZE) {
        		if(tiles[i][j].type == TileType.Grass)
        			meshPartBuilder.setUVRange(atlas.findRegion("grass"));
        		else
        			meshPartBuilder.setUVRange(atlas.findRegion("dirt"));
        		
        		meshPartBuilder.rect(i, 0f, j, i, 0f, j + TILE_SIZE, i + TILE_SIZE, 0f, j + TILE_SIZE, (i + TILE_SIZE), 0f, j, 0, 1f, 0);
        		
        		BoundingBox bb = new BoundingBox();
        		/*
        		bb.ext(i, 0f, j);
        		bb.ext(0f, j + TILE_SIZE, i + TILE_SIZE);
        		bb.ext(i + TILE_SIZE, 0f, j + TILE_SIZE);
        		bb.ext((i + TILE_SIZE), 0f, j);
        		*/
        		bb.ext(i, 0f, j);
        		bb.ext(i, 0f, j + TILE_SIZE);
        		bb.ext(i + TILE_SIZE, 0f, j + TILE_SIZE);
        		bb.ext((i + TILE_SIZE), 0f, j);
        		bb.ext(i, TILE_SIZE, j);
        		bb.ext(i, TILE_SIZE, j + TILE_SIZE);
        		bb.ext(i + TILE_SIZE, TILE_SIZE, j + TILE_SIZE);
        		bb.ext((i + TILE_SIZE), TILE_SIZE, j);
        		boxes[i][j] = bb;
        		
        		counter++;
        		if(counter == counterMax) {
        			modelBuilder.node();
        			counter = 0;
        			meshPartBuilder = modelBuilder.part(Integer.toString(i), GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.TextureCoordinates, new Material(TextureAttribute.createDiffuse(ter)));
        		}
        	}
        }
        model = modelBuilder.end();
        instance = new ModelInstance(model);
        
        Model gridModel = modelBuilder.createLineGrid(sizeX, sizeY, 1, 1, new Material(ColorAttribute.createDiffuse(Color.FIREBRICK)), Usage.Position | Usage.Normal);
        grid = new ModelInstance(gridModel);
        grid.transform.setTranslation(sizeX / 2, 0.005f, sizeY / 2);
        
        gridOn = true;
        

        
	}

	@Override
	public void render(ModelBatch modelBatch, Environment environment) {
		modelBatch.render(instance, environment);
		if(gridOn) {
			modelBatch.render(grid);
		}
		
        if(Gdx.input.isKeyJustPressed(Keys.G)) {
        	gridOn ^= true;
        }
	}
	
	public BoundingBox[][] getTerrainCollision() {
		return boxes;
	}
	
	public Vector3 getCollisionVector(Ray ray,  boolean centered) {
		for(int i = 0; i < boxes.length; i++) {
			for(int j = 0; j < boxes[0].length; j++) {
				Vector3 vector = new Vector3();
				if(Intersector.intersectRayBounds(ray, boxes[i][j], vector)) {
					if(centered)
						return new Vector3(i + (TILE_SIZE / 2), 0f , j + (TILE_SIZE / 2));
					else
						return new Vector3(i, 0f, j);
				}
			}
		}
		return null;
	}
	

}
