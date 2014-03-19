package net.megafox.entities;



public class Powerup extends Entity{

	public static final int TIME_SECONDS = 10;

	public Powerup(int x, int y, int id) {
		super(x, y, id, 0, Visibility.BOTH, null);
	}

}
