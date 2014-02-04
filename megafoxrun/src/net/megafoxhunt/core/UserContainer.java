package net.megafoxhunt.core;

import java.util.ArrayList;

public class UserContainer {
	
	private static ArrayList<User> USERS = new ArrayList<User>();
	
	public static int numberOfUsers(){
		return getUsersConcurrentSafe().size();
	}
	public static ArrayList<User> getUsersConcurrentSafe() {
		return (ArrayList<User>)USERS.clone();
	}	
	public static User getUserByID(int id){
		for(User user : USERS){
			if(user.getID() == id){
				return user;
			}
		}
		return null;
	}
	public static void addUser(User user) {
		USERS.add(user);
	}	
	public static void removeUser(User user) {
		USERS.remove(user);
	}	
	public static void removeUserById(int id) {
		for(User user : USERS) {
			if (user.getID() == id) {
				USERS.remove(user);
				break;
			}
		}
	}
}
