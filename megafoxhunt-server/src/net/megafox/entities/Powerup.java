package net.megafox.entities;

public class Powerup extends Entity{
	
	public static final int DURATION_RAGE_MS = 5000;
	public static final int DURATION_SPEED_MS = 5000;

	public Powerup(int x, int y, int id) {
		super(x, y, id, Visibility.BOTH, null);
	}

}

