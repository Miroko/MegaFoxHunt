package net.megafoxhunt.server;

import java.util.ArrayList;
import java.util.Scanner;

import net.megafoxhunt.server.GameRoom;
import net.megafoxhunt.server.KryoServer;

public class ServerDebugInput extends Thread{

	private KryoServer server;
	
	private boolean running;
	
	public ServerDebugInput(KryoServer server) {
		this.server = server;
		running = true;
	}
	
	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		
		while (running) {
			System.out.print("Next command: ");
			newCommand(scanner.nextLine());
		}
		
		scanner.close();
	}
	
	private void newCommand(String command) {
		if (command.equals("force-start") ||
			command.equals("run") ||
			command.equals("r")) {
			ArrayList<GameRoom> rooms = server.getRoomHandler().getRooms();
			for (GameRoom room : rooms) {
				room.startGame();
			}
		}
	}
	
	public void shutdown() {
		running = false;
	}
}
