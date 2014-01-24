package net.megafoxhunt.entities;

import java.awt.Point;

public class StaticEntity {
	private String name;
	public String getName(){return name;}
	
	private Point point;
	public Point getPoint(){return point;}
		
	public StaticEntity(String name, int x, int y){
		this.point = new Point(x, y);
		this.name = name;
	}
}
