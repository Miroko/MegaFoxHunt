package net.megafoxhunt.entities;

import net.megafoxhunt.core.MyGdxGame;

public class PickupItem extends Entity{

	public static final int TIME_SECONDS = 10;
	
	public PickupItem(int id, float x, float y) {
		super(id, x, y, MyGdxGame.resources.PICKUP_ITEM_ANIMATIONS);
	}


}
