package net.megafoxhunt.entities;

public class Chaser extends AliveEntity{
	
	private static final float MOVEMENT_SPEED = 30;

	public Chaser(int id, float x, float y) {
		super(id, x, y, MOVEMENT_SPEED);
	}

}
