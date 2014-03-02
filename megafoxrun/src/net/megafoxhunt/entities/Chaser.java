package net.megafoxhunt.entities;


import net.megafoxhunt.core.MyGdxGame;

public class Chaser extends EntityMovable{
		
	private static final float MOVEMENT_SPEED = 5;
	
	public Chaser(int id, float x, float y) {
		super(id, x, y, MOVEMENT_SPEED, MyGdxGame.resources.DOG_ANIMATIONS);
	}


}
