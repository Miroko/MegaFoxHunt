package net.megafox.entities;

import java.util.TimerTask;

import net.megafoxhunt.server.PlayerConnection;
import net.megafoxhunt.shared.KryoNetwork.PowerupRage;
import net.megafoxhunt.shared.KryoNetwork.PowerupSpeed;

public class Powerup extends Entity{
	
	public static final int DURATION_RAGE = 5000;
	public static final int DURATION_SPEED = 2500;

	public Powerup(int x, int y, int id) {
		super(x, y, id, 0, Visibility.BOTH, null);
	}

}

