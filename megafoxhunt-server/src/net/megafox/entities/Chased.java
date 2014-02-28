package net.megafox.entities;

import net.megafox.entities.Entity.Visibility;

public class Chased extends Entity {

	public Chased(int x, int y, int id) {
		super(x, y, id, 5, Visibility.BOTH);
	}

}
