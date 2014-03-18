package net.megafox.entities;

import net.megafoxhunt.server.PlayerConnection;


public class Chased extends Entity {

	public Chased(int x, int y, int id, PlayerConnection player) {
		super(x, y, id, 5, Visibility.BOTH, player);
	}

}
