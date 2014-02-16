package net.megafoxhunt.server;

import java.util.Scanner;

import net.megafoxhunt.server.GameRoom;


public class ServerDebugInput extends Thread{

	private GameServer server;
	
	private boolean running;
	
	public ServerDebugInput(GameServer server) {
		this.server = server;
		running = true;
	}
	
	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		
		while (running) {
			System.out.println("Next command: ");
			newCommand(scanner.nextLine());
		}
		
		scanner.close();
	}
	
	private void newCommand(String command) {
		if (command.equals("force-start") ||
			command.equals("run") ||
			command.equals("r")) {
			for (GameRoom room : server.getRoomHandler().getAllRoomsConcurrentSafe()) {
				room.startGame();
			}
		}
	}
	
	public void shutdown() {
		running = false;
	}
}
