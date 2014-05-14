package net.megafoxhunt.entities;

import net.megafoxhunt.core.MyGdxGame;



public class Berry extends Entity{
	
	public Berry(int id, float x, float y) {
		super(id, x, y, MyGdxGame.resources.BERRY_ANIMATIONS);
		// TODO Auto-generated constructor stub
	}

	public void playEatSound(){
		MyGdxGame.resources.marja_ker‰t‰‰n.play();		
	}

}
