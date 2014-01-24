package net.megafoxhunt.entities;

public class Coordinates {
	public float x;
	public float y;
	public Coordinates(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void translate(float mx, float my){
		this.x += mx;
		this.y += my;
	}
}
