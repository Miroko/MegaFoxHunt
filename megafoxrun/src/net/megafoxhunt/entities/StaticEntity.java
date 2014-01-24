package net.megafoxhunt.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StaticEntity {
	private String name;
	public String getName(){return name;}
	
	private Coordinates coordinates;
	public Coordinates getCoordinates(){return coordinates;}
		
	public StaticEntity(String name, int x, int y){
		this.coordinates = new Coordinates(x, y);
		this.name = name;
	}
	public void draw(SpriteBatch sb){
		
	}
}
