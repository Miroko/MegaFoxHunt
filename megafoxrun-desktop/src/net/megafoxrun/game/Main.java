package net.megafoxrun.game;


import net.megafoxhunt.core.MyGdxGame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Mega Fox Hunt";
		cfg.useGL20 = true;
		cfg.vSyncEnabled = true;
		new LwjglApplication(new MyGdxGame(), cfg);
	}
}
