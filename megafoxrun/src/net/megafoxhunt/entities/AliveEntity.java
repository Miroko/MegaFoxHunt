package net.megafoxhunt.entities;

public class AliveEntity extends StaticEntity{

	public AliveEntity(String name, int x, int y) {
		super(name, x, y);		
	}
	public void move(int mx, int my){
		this.getPoint().translate(mx, my);
	}
	
}
