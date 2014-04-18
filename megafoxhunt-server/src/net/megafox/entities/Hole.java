package net.megafox.entities;


public class Hole extends Entity {

	private boolean holeCooldown;
	
	public Hole(int x, int y, int id) {
		super(x, y, id, Visibility.BOTH, null);
		this.holeCooldown = false;
	}
	
	public boolean isHoleCooldown() {
		return holeCooldown;
	}
	
	public void setHoleCooldown(boolean b) {
		this.holeCooldown = b;
	}
}
