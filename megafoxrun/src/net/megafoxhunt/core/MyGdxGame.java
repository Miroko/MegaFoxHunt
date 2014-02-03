package net.megafoxhunt.core;


import java.util.Scanner;
import net.megafoxhunt.entities.EntityContainer;
import net.megafoxhunt.screens.MenuScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class MyGdxGame extends Game {
	
	public static MyGdxGame INSTANCE;
	
	public static UserContainer userContainer = new UserContainer();
	
	private static GameNetwork NETWORK;
	public static GameNetwork getNetwork(){return NETWORK;}
	
	private static EntityContainer ENTITY_CONTAINER;
	public static EntityContainer getEntityContainer(){return ENTITY_CONTAINER;}
	
	
	@Override
	public void create() {
		INSTANCE = this;
		ENTITY_CONTAINER = new EntityContainer();		
		Gdx.input.setInputProcessor(new GameInputProcessor());		
		this.setScreen(new MenuScreen());		
		NETWORK = new GameNetwork();	
		setUsername();		
		NETWORK.connect("localhost", 6666);
	}
	public void setUsername(){
		Scanner scanner = new Scanner(System.in);
		System.out.print("Pelaajan nimi: ");
		userContainer.getUser().setName(scanner.nextLine());
		scanner.close();
	}		
	@Override
	public void dispose() {
	}
}
