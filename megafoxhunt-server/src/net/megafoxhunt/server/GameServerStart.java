package net.megafoxhunt.server;

public class GameServerStart {

	public static void main(String[] args) {
		GameServer server = new GameServer();
		server.start();

		ServerDebugInput s = new ServerDebugInput(server);
		s.start();
	}

}
