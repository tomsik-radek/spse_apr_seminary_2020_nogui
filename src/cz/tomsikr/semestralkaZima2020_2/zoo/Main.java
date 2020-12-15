package cz.tomsikr.semestralkaZima2020_2.zoo;

/**
 * @author tomsikr
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
	public static String usersConfFolder = "./permissions";
	public static String usersConfPath = usersConfFolder + "/users.conf";
	public static String animalsDBFolder = "./data";
	public static String animalsDBPath = animalsDBFolder + "/animals.txt";
	public static String loggedInPermission;
	public static String loggedInName;
	public static String appVersion = "0.9.3";
	public static String adminPhoneNumber;
	public static String guideURL;
	public static int animalIndex;
	
	public static ArrayList<Users> usersList = new ArrayList<Users>();
	public static ArrayList<Animals> animalsList = new ArrayList<Animals>();
	public static Scanner sc = new Scanner(System.in);
	public static Console con = System.console(); 
	public static boolean loggedIn = true;
	
	public static void main(String[] args) {
		//System.out.println("DEBUG WARNING - Zoo: Attempt 2");
		if((new File(usersConfPath).exists()) && (new File(usersConfPath).length() != 0)) {
			readUsersFileToArrayList();
			if(new File(animalsDBPath).exists() && (new File(animalsDBPath).length() != 0)){
				readAnimalsFileToArrayList();
			}
			loginMenu();
		}else{
			createNewUser(true);
			if(new File(usersConfPath).exists()) {
				loginMenu();
			}
		}
	}
	
	//MAIN METHODS
	
	public static void loginMenu(){
		String menuSelect = "";
		boolean valid = false;
		
		System.out.println("ZOO version " + appVersion);
		System.out.println("User guide available at: " + guideURL);
		System.out.println("If you have any issues contact your administrator at " + adminPhoneNumber);
		
		System.out.println("*********");
		System.out.println("Please log in...");
		System.out.println("Possible actions: ");
		
		do {
			System.out.println("1: Log in");
			System.out.println("2: Register");
			System.out.println("3: Forgot password");
			System.out.println("0: Exit program");
			System.out.print("Please select action: ");
			menuSelect = sc.nextLine();
			//System.out.println("DEBUG WARNING - menuSelect " + menuSelect);
			System.out.println();
			
			String name;
			String password;
			
			loggedIn = false;
			//System.out.println("DEBUG WARNING - loggedIn " + loggedIn);
			switch(menuSelect) {
				case "1":
					do {
						System.out.print("Enter your username: ");
						name = sc.nextLine();
						System.out.print("Enter password: ");
						if(con == null) {
							password = sc.nextLine();
						}else {
							char[] ch=con.readPassword();   
							password = String.valueOf(ch);   
						}
						
						//System.out.println("DEBUG WARNING - name and password " + name + ", " + password);
						
						valid = doNameAndPasswordMatch(name, password, true);
						try {
							if(valid == false) {
								System.out.println("Logging in...");
								TimeUnit.SECONDS.sleep(3);
								System.out.println("Error. Username or password are not valid. Please try again.");
							}else {
								System.out.println("Logging in...");
								TimeUnit.SECONDS.sleep(1);
							}
						}catch(InterruptedException e) {
							e.printStackTrace();
						}
						//System.out.println("DEBUG WARNING - are username and password valid or not: " + valid);
						loggedIn = true;
					}while(valid == false);
					
					break;
					
				case "2":
					System.out.println("Please contact your administrator at " + adminPhoneNumber + " to create your account.");
					System.out.println();
					break;
				case "3":
					System.out.println("Please contact your administrator at " + adminPhoneNumber + " to reset your password.");
					System.out.println();
					break;
				case "0":
					try {
						System.out.println("Thank you for running this program. This program will now exit...");
						TimeUnit.SECONDS.sleep(1);
					}catch(InterruptedException e) {
						e.printStackTrace();
					}
					System.exit(0);
					break;
			
				default: System.out.println("Invalid input, please try again.");
			}
			
			//System.out.println("DEBUG WARNING - loginMenu boolean valid " + valid);
			//System.out.println("DEBUG WARNING - loginMenu boolean loggedIn " + loggedIn);
			
		}while(!menuSelect.equals("0") && (loggedIn == false));
		
		if(loggedIn == true) {
			System.out.println("Login successful..");
			MainMenu();
		}
	}
	public static void MainMenu() {
		//printArrayListUsers();
		
		System.out.println();
		System.out.println("************************************************");
		System.out.println("ZOO Main Menu");
		System.out.println("User logged in: " + loggedInName);
		System.out.println("Users permission level: " + loggedInPermission);
		System.out.println("************************************************");
		
		String menuSelect = "";
		do {		
			System.out.println();
			//What can be done
			System.out.println("Possible actions:");
			if(!loggedInPermission.equals("administrator")) {
				System.out.println("1: Create a new record");
				System.out.println("2: Modify animal record");
				System.out.println("3: Delete a record");
				System.out.println("5: List all records");
				System.out.println("6: Change your password");
				System.out.println("7: list max ID");
			}
			if(loggedInPermission.equals("administrator")){
				System.out.println("1: Create a new user");
				System.out.println("2: Delete a user");
				System.out.println("3: Change password of a user");
				System.out.println("5: List all users");
				System.out.println("7: list max ID");
			}
			System.out.println("0: logout");
			System.out.print("Please select action: ");
			menuSelect = sc.nextLine();
			System.out.println();
			
			//What happens
					
			if(loggedInPermission.equals("administrator")) {
				if(menuSelect.equals("1")) {
					createNewUser(false);
				}
				if(menuSelect.equals("2")) {
					System.out.println("What user do you want to delete?");
					String temp = sc.nextLine();
					deleteUser(temp);
				}
				if(menuSelect.equals("3")) {
					System.out.println("What user's password do you want to change?");
					String username;
					username = sc.nextLine();
					changePassword(username);
				}
				if(menuSelect.equals("5")) {
					printArrayListUsers();
				}
				if(menuSelect.equals("7")) {
					System.out.println(getLastID("users"));
				}
			}
			
			if(!loggedInPermission.equals("administrator")) {
				if(menuSelect.equals("1")) {
					createAnimal();
				}
				if(menuSelect.equals("2")) {
					modifyAnimalRecord();
				}
				if(menuSelect.equals("3")) {
					deleteAnimalRecord();
				}
				if(menuSelect.equals("6")) {
					changePassword(loggedInName);
				}
				if(menuSelect.equals("5")) {
					printArrayListAnimals();
				}
				if(menuSelect.equals("7")) {
					System.out.println(getLastID("animals"));
				}
			}
			if(menuSelect.equals("0")) {
				loggedIn = false;
				System.out.println("You have been logged out");
				loginMenu();
			}
		}while(!(menuSelect.equals("0")));
	}
	
	//HELP METHODS
	
	//USER METHODS
	public static void createNewUser(boolean administrator) { //Only one administrator account can exist at a time, that is my "design" choice
		boolean match = true;
		String name = "";
		String password = "";
		String permission = "";
		String age = "";
		String phoneNumber = "";
		
		//System.out.println("DEBUG WARNING - boolean admin " + administrator);
		if(administrator == true) { //Should be only used when creating the very first user profile, when users.conf is missing
			System.out.println("User configuration file does not exist. Starting from scratch.");
			System.out.println("Creating a new administrator...");
		}else {
			System.out.println("Creating a new user...");
		}
		
		do {
			do {
				System.out.print("Enter name: ");
				name = sc.nextLine();	
				if((name.length() == 0) || (name.length() < 5)) {
					System.out.println("Name must be longer than 4 characters. Please try again.");
				}
			}while(name.length() > 4);
			
			match = doNameAndPasswordMatch(name, password, false);
			
			System.out.println("DEBUG WARNING - match " + match);
			if(match == true) {
				System.out.println("Name already used. Please try again.");
			}
		}while(match == true);
		
		do {
			System.out.print("Enter password: ");
			if(con == null) {
				password = sc.nextLine();
			}else {
				char[] ch=con.readPassword();   
				password = String.valueOf(ch);   
			}
			if(!(password.length() > 4)) {
				System.out.println("Password must be longer than 4 characters. Please try again");
			}
		}while(password.length() < 5);
		password = getHash(password, "MD5");
		password = password.toUpperCase();
		
		if(administrator == true) {
			permission = "administrator";
			System.out.print("Enter your phone number: ");
			phoneNumber = sc.nextLine();
		}else {
			permission = "doctor";
		}
		
		do {
			System.out.print("Enter age: ");
			age = sc.nextLine();
			if(age.length() == 0) {
				System.out.println("Age must be at least 1 character long. Please try again.");
			}
		}while(age.length() == 0);
		
		int id = getLastID("users") + 1;
		
		Users temp = new Users(id, name, password, permission, age, phoneNumber);
		usersList.add(temp);
		
		System.out.println("User " + name + " created. Press Enter to save and continue.");
		sc.nextLine();
		
		writeArrayListDataToFile("users");
		System.out.println("DEBUG WARNING - ArrayList data written to a file");
	}
	
	public static void deleteUser(String name){
		String input = "";
		System.out.println("You are about to delete user " + name + ". Are you sure? Y/N");
		
		input = sc.nextLine();
		input = input.toLowerCase();
		
		if(!input.equals("y")) {
			System.out.println("Aborting...");
			return;
		}
		
		if(input.equals("y")) {
			for(int i = 0; i<usersList.size(); i++) {
				//System.out.println("DEBUG WARNING - i: " + i + " - " + usersList.get(i).name);
				//System.out.println("DEBUG WARNING - input: " + input);
				if((usersList.get(i).name).equals(name)) {
					//System.out.println("DEBUG WARNING - match found");
					System.out.println("Deleting user " + name);
					usersList.remove(i);
				}else {
					System.out.println("No user with name " + name + " exists. Aborting...");
				}
			}
		}
		writeArrayListDataToFile("users");
	}
	
	public static boolean doNameAndPasswordMatch(String name, String password, boolean checkPassword) {
		boolean result = false;
		boolean valid = false;
		String hashedPassword;
		
		for(int i = 0; i<usersList.size(); i++) {
			String tempName = usersList.get(i).name;
			String tempPassword = usersList.get(i).password;
			String tempPermission = usersList.get(i).permission;
		
			//System.out.println("DEBUG WARNING - Checking user on index " + i);
			//System.out.println("DEBUG WARNING - checkPassword: " + checkPassword);

			
			if(checkPassword == false) {
				if(tempName.equals(name)) {
					result = true;
					System.out.println("DEBUG WARNING - result " + result);
					return result;
				}
				break;
			}
			
			/*
			System.out.println("DEBUG WARNING - tempName "+ tempName);
			System.out.println("DEBUG WARNING - Entered name "+ name);
			System.out.println("DEBUG WARNING - tempPassword "+ tempPassword);
			System.out.println("DEBUG WARNING - Entered password "+ password);		
			*/
			
			hashedPassword = getHash(password, "MD5").toUpperCase();
			//System.out.println("DEBUG WARNING - Entered hashed password "+ hashedPassword);
		
			if(checkPassword == true) {
				if(tempName.equals(name)){
					//System.out.println("DEBUG WARNING - Names match");
					if(tempPassword.equals(hashedPassword)) {
						valid = true;
						//System.out.println("DEBUG WARNING - Passwords match");
						//System.out.println("DEBUG WARNING - valid " + valid);
						loggedInPermission = tempPermission;
						loggedInName = tempName;
						break;
					}else {
						//System.out.println("DEBUG WARNING - Passwords do not match");
						valid = false;
						//System.out.println("DEBUG WARNING - valid " + valid);
					}
				}
			}
		}
		
		//System.out.println("DEBUG WARNING - valid for return " + valid);
		if(valid == true) {
			return result = true;
		}else {
			return result = false;
		}
	}
		
	
	public static void changePassword(String userToChange) {
		String newPassword;
		String oldPassword;
		System.out.println("Changing password of user " + userToChange);
		int userIndex = 0;
		
		//Looking for an index that corresponds to the user I'm looking for
		for(int i = 0; i < usersList.size(); i++) {
			if((usersList.get(i).name).equals(userToChange)) {
				userIndex = i;
				break;
			}
		}
		
		oldPassword = usersList.get(userIndex).password;
		
		do {
			System.out.print("Enter a new password: ");
			newPassword = sc.nextLine();
			//System.out.println("DEBUG WARNING - oldPassword " + oldPassword);
			//System.out.println("DEBUG WARNING - newPassword " + newPassword);
			newPassword = getHash(newPassword, "MD5").toUpperCase();
			//System.out.println("DEBUG WARNING - newPassword hashed " + newPassword);
			if((newPassword).equals(oldPassword)) {
				System.out.println("New password can not be the same as old password. Please try again.");
			}
		}while((newPassword).equals(oldPassword));
		
		for(Users user : usersList) {
			if(user.getName().equals(userToChange)) {
				user.setPassword(newPassword);
			}
		}	
		writeArrayListDataToFile("users");
	}
	
	//ANIMAL METHODS
	
	public static void createAnimal() {
		String species, category, name, age, health, weight, sex, wingspan, notes;
		int id = getLastID("animals") + 1;
		
		System.out.println("Creating a new animal. If you wish to not enter any data for that parameter, leave the input empty and press Enter");
		System.out.println();
		System.out.print("Enter subject's species: ");
		species = sc.nextLine();
		if(species.isEmpty()) {
			species = " ";
		}
		System.out.print("Enter subject's category: ");
		category = sc.nextLine();
		if(category.isEmpty()) {
			category = " ";
		}
		
		System.out.println("Does this subject have a name? Y/N");
		
		name = sc.nextLine();
		if(name.isEmpty()) {
			name = " ";
		}
		
		System.out.print("Enter " + name + "'s age: ");
		age = sc.nextLine();
		if(age.isEmpty()) {
			age = " ";
		}
		System.out.print("Enter " + name + "'s health: ");
		health = sc.nextLine();
		if(health.isEmpty()) {
			health = " ";
		}
		System.out.print("Enter " + name + "'s weight (KG): ");
		weight = sc.nextLine();
		if(weight.isEmpty()) {
			weight = " ";
		}
		System.out.print("Enter " + name + "'s sex: ");
		sex = sc.nextLine();
		if(sex.isEmpty()) {
			sex = " ";
		}
		if(category.equals("bird")) {
			System.out.println("Enter " + name + "'s wingspan: ");
			wingspan = sc.nextLine();
		}else {
			wingspan = " ";
		}
		
		System.out.println("Enter additional notes for " + name);
		notes = sc.nextLine();
		if(notes.isEmpty()) {
			notes = " ";
		}

		if(name.equals("subject")) {
			name = " ";
		}
		
		Animals animal = new Animals(id,species,category,name,age,health,weight,sex,wingspan,notes);
		animalsList.add(animal);
		writeArrayListDataToFile("animals");
		System.out.println("DEBUG WARNING - Arraylist animalsList written to a file");
	}
	
	public static void deleteAnimalRecord() {
		String input;
		boolean match = false;
		int index;
		int i = -1;
		int maxIndex = getLastID("animals");
		String sIndex;
		System.out.println();
		System.out.println("Do you want to list all records? Y/N");
		while(true) {
			input = sc.nextLine().toUpperCase();
			if(input.equals("Y")) {	
				printArrayListAnimals();
				break;
			}else if(input.equals("N")) {
				break;
			}
			System.out.println("Invalid input. Please try again.");
			System.out.println("Do you want to list all records?");
		}
		
		System.out.print("Enter ID of a record you want to delete: ");
		sIndex = sc.nextLine();
		//System.out.println("DEBUG WARNING - sIndex: " + sIndex);
		if(isNum(sIndex)) {
			index = Integer.parseInt(sIndex);
			if(index <= maxIndex) {
				for(Animals animal : animalsList) {
					if(animal.getID() == index) {
						i = animalsList.indexOf(animal);
						//System.out.println("DEBUG WARNING - i " + i);
						//System.out.println("DEBUG WARNING - arrayListIndex " + i);
						//sc.nextLine();
						match = true;
					}
				}
				if(match == false) {
					System.out.println("No such record exists. No changes will be made.");
				}else {
					System.out.println("Record found and was deleted");
				}
			}else {
				System.out.println("No such record exists. No changes will be made.");
			}
		}else {
			System.out.println("No such record exists. No changes will be made.");
		}
		if(i != -1) {
			animalsList.remove(i);
		}
		writeArrayListDataToFile("animals");
	}
	
	public static void modifyAnimalRecord() {
		String input;
		boolean match = false;
		int index;
		int maxIndex = getLastID("animals");
		String sIndex;
		System.out.println();
		System.out.println("Do you want to list all records? Y/N");
		while(true) {
			input = sc.nextLine().toUpperCase();
			if(input.equals("Y")) {	
				printArrayListAnimals();
				break;
			}else if(input.equals("N")) {
				break;
			}
			System.out.println("Invalid input. Please try again.");
			System.out.println("Do you want to list all records?");
		}
		
		System.out.print("Enter ID of a record you want to modify: ");
		sIndex = sc.nextLine();
		//System.out.println("DEBUG WARNING - sIndex: " + sIndex);
		if(isNum(sIndex)) {
			index = Integer.parseInt(sIndex);
			if(index <= maxIndex) {
				System.out.println("DEBUG WARNING - index " + index);
				System.out.println("DEBUG WARNING - maxIndex " + maxIndex);
				for(Animals animal : animalsList) {
					if(animal.getID() == index) {
						match = true;
						int i = animalsList.indexOf(animal);
						System.out.println("***************************************************");
						System.out.println("Editing record with index " + i + " and ID " + index);
						System.out.println("Current information of this animal is: ");
						System.out.println("ID: " + animalsList.get(i).id);
						System.out.println("Species: " + animalsList.get(i).species);
						System.out.println("Category: " + animalsList.get(i).category);
						System.out.println("Name: " + animalsList.get(i).name);
						System.out.println("Age: " + animalsList.get(i).age);
						System.out.println("Health: " + animalsList.get(i).health);
						System.out.println("Weight: " + animalsList.get(i).weight);
						System.out.println("Sex: " + animalsList.get(i).sex);
						System.out.println("Wingspan: " + animalsList.get(i).wingspan);
						System.out.println("Additional notes: " + animalsList.get(i).notes);
						System.out.println("***************************************************");
						
						String oldTemp;
						String newTemp;
						
						//This is going to be a long one
						//Changing species
						System.out.println("Do you want to change " + animalsList.get(i).name + "'s species? Y/N");
						while(true) {
							oldTemp = animalsList.get(i).species;
							input = sc.nextLine().toUpperCase();
							if(input.equals("Y")) {	
								do {
									System.out.print("Enter new species: ");
									newTemp = sc.nextLine();
									//System.out.println("DEBUG WARNING - newTemp " + newTemp);
									//System.out.println("DEBUG WARNING - oldTemp " + oldTemp);
									if((newTemp).equals(oldTemp)) {
										System.out.println("New species can not be the same as the old name. Please try again.");
										System.out.println("Do you want to change " + animalsList.get(i).name + "'s species? Y/N");
									}
								}while((newTemp).equals(oldTemp));
								animal.setSpecies(newTemp);
								break;
							}else if(input.equals("N")) {
								break;
							}
							System.out.println("Invalid input. Please try again.");
							System.out.println("Do you want to change " + animalsList.get(i).name + "'s species? Y/N");
						}
						//Changing category
						System.out.println("Do you want to change " + animalsList.get(i).name + "'s category? Y/N");
						while(true) {
							oldTemp = animalsList.get(i).category;
							input = sc.nextLine().toUpperCase();
							if(input.equals("Y")) {	
								do {
									System.out.print("Enter new category: ");
									newTemp = sc.nextLine();
									//System.out.println("DEBUG WARNING - newTemp " + newTemp);
									//System.out.println("DEBUG WARNING - oldTemp " + oldTemp);
									if((newTemp).equals(oldTemp)) {
										System.out.println("New category can not be the same as the old name. Please try again.");
										System.out.println("Do you want to change " + animalsList.get(i).name + "'s category? Y/N");
									}
								}while((newTemp).equals(oldTemp));
								animal.setCategory(newTemp);
								break;
							}else if(input.equals("N")) {
								break;
							}
							System.out.println("Invalid input. Please try again.");
							System.out.println("Do you want to change " + animalsList.get(i).name + "'s category? Y/N");
						}
						//Changing name
						System.out.println("Do you want to change " + animalsList.get(i).name + "'s name? Y/N");
						while(true) {
							oldTemp = animalsList.get(i).name;
							input = sc.nextLine().toUpperCase();
							if(input.equals("Y")) {	
								do {
									System.out.print("Enter new name: ");
									newTemp = sc.nextLine();
									//System.out.println("DEBUG WARNING - newTemp " + newTemp);
									//System.out.println("DEBUG WARNING - oldTemp " + oldTemp);
									if((newTemp).equals(oldTemp)) {
										System.out.println("New name can not be the same as the old name. Please try again.");
										System.out.println("Do you want to change " + animalsList.get(i).name + "'s name? Y/N");
									}
								}while((newTemp).equals(oldTemp));
								animal.setName(newTemp);
								break;
							}else if(input.equals("N")) {
								break;
							}
							System.out.println("Invalid input. Please try again.");
							System.out.println("Do you want to change " + animalsList.get(i).name + "'s name? Y/N");
						}
						//Changing age
						System.out.println("Do you want to change " + animalsList.get(i).name + "'s age? Y/N");
						while(true) {
							oldTemp = animalsList.get(i).age;
							input = sc.nextLine().toUpperCase();
							if(input.equals("Y")) {	
								do {
									System.out.print("Enter new age: ");
									newTemp = sc.nextLine();
									//System.out.println("DEBUG WARNING - newTemp " + newTemp);
									//System.out.println("DEBUG WARNING - oldTemp " + oldTemp);
									if((newTemp).equals(oldTemp)) {
										System.out.println("New age can not be the same as the old name. Please try again.");
										System.out.println("Do you want to change " + animalsList.get(i).name + "'s age? Y/N");
									}
								}while((newTemp).equals(oldTemp));
								animal.setAge(newTemp);
								break;
							}else if(input.equals("N")) {
								break;
							}
							System.out.println("Invalid input. Please try again.");
							System.out.println("Do you want to change " + animalsList.get(i).name + "'s age? Y/N");
						}
						//Changing health
						System.out.println("Do you want to change " + animalsList.get(i).name + "'s health? Y/N");
						while(true) {
							oldTemp = animalsList.get(i).health;
							input = sc.nextLine().toUpperCase();
							if(input.equals("Y")) {	
								do {
									System.out.print("Enter new health: ");
									newTemp = sc.nextLine();
									//System.out.println("DEBUG WARNING - newTemp " + newTemp);
									//System.out.println("DEBUG WARNING - oldTemp " + oldTemp);
									if((newTemp).equals(oldTemp)) {
										System.out.println("New health can not be the same as the old name. Please try again.");
										System.out.println("Do you want to change " + animalsList.get(i).name + "'s health? Y/N");
									}
								}while((newTemp).equals(oldTemp));
								animal.setHealth(newTemp);
								break;
							}else if(input.equals("N")) {
								break;
							}
							System.out.println("Invalid input. Please try again.");
							System.out.println("Do you want to change " + animalsList.get(i).name + "'s health? Y/N");
						}
						//Changing weight
						System.out.println("Do you want to change " + animalsList.get(i).name + "'s weight? Y/N");
						while(true) {
							oldTemp = animalsList.get(i).weight;
							input = sc.nextLine().toUpperCase();
							if(input.equals("Y")) {	
								do {
									System.out.print("Enter new weight: ");
									newTemp = sc.nextLine();
									//System.out.println("DEBUG WARNING - newTemp " + newTemp);
									//System.out.println("DEBUG WARNING - oldTemp " + oldTemp);
									if((newTemp).equals(oldTemp)) {
										System.out.println("New weight can not be the same as the old name. Please try again.");
										System.out.println("Do you want to change " + animalsList.get(i).name + "'s weight? Y/N");
									}
								}while((newTemp).equals(oldTemp));
								animal.setWeight(newTemp);
								break;
							}else if(input.equals("N")) {
								break;
							}
							System.out.println("Invalid input. Please try again.");
							System.out.println("Do you want to change " + animalsList.get(i).name + "'s weight? Y/N");
						}
						//Changing sex
						System.out.println("Do you want to change " + animalsList.get(i).name + "'s sex? Y/N");
						while(true) {
							oldTemp = animalsList.get(i).sex;
							input = sc.nextLine().toUpperCase();
							if(input.equals("Y")) {	
								do {
									System.out.print("Enter new sex: ");
									newTemp = sc.nextLine();
									//System.out.println("DEBUG WARNING - newTemp " + newTemp);
									//System.out.println("DEBUG WARNING - oldTemp " + oldTemp);
									if((newTemp).equals(oldTemp)) {
										System.out.println("New sex can not be the same as the old name. Please try again.");
										System.out.println("Do you want to change " + animalsList.get(i).name + "'s sex? Y/N");
									}
								}while((newTemp).equals(oldTemp));
								animal.setSex(newTemp);
								break;
							}else if(input.equals("N")) {
								break;
							}
							System.out.println("Invalid input. Please try again.");
							System.out.println("Do you want to change " + animalsList.get(i).name + "'s sex? Y/N");
						}
						//Changing wingspan
						System.out.println("Do you want to change " + animalsList.get(i).name + "'s wingspan? Y/N");
						while(true) {
							oldTemp = animalsList.get(i).wingspan;
							input = sc.nextLine().toUpperCase();
							if(input.equals("Y")) {	
								do {
									System.out.print("Enter new wingspan: ");
									newTemp = sc.nextLine();
									//System.out.println("DEBUG WARNING - newTemp " + newTemp);
									//System.out.println("DEBUG WARNING - oldTemp " + oldTemp);
									if((newTemp).equals(oldTemp)) {
										System.out.println("New wingspan can not be the same as the old name. Please try again.");
										System.out.println("Do you want to change " + animalsList.get(i).name + "'s wingspan? Y/N");
									}
								}while((newTemp).equals(oldTemp));
								animal.setWingspan(newTemp);
								break;
							}else if(input.equals("N")) {
								break;
							}
							System.out.println("Invalid input. Please try again.");
							System.out.println("Do you want to change " + animalsList.get(i).name + "'s wingspan? Y/N");
						}
						//Changing notes
						System.out.println("Do you want to change " + animalsList.get(i).name + "'s notes? Any old notes will be deleted. Y/N");
						while(true) {
							oldTemp = animalsList.get(i).notes;
							input = sc.nextLine().toUpperCase();
							if(input.equals("Y")) {	
								do {
									System.out.print("Enter new notes: ");
									newTemp = sc.nextLine();
									System.out.println("DEBUG WARNING - newTemp " + newTemp);
									System.out.println("DEBUG WARNING - oldTemp " + oldTemp);
									if((newTemp).equals(oldTemp)) {
										System.out.println("New notes can not be the same as the old name. Please try again.");
										System.out.println("Do you want to change " + animalsList.get(i).name + "'s notes? Any old notes will be deleted. Y/N");									}
								}while((newTemp).equals(oldTemp));
								animal.setNotes(newTemp);
								break;
							}else if(input.equals("N")) {
								break;
							}
							System.out.println("Invalid input. Please try again.");
							System.out.println("Do you want to change " + animalsList.get(i).name + "'s notes? Any old notes will be deleted. Y/N");						}		
						
						System.out.println("Editing of a record finished.");
					}
				}
				if(match == false) {
					System.out.println("No such record exists. No changes will be made.");
				}else {
					System.out.println("Record found and edited.");
				}
			}else {
				System.out.println("No such record exists. No changes will be made.");
			}
		}else {
			System.out.println("No such record exists. No changes will be made.");
		}
		writeArrayListDataToFile("animals");
	}
	//OTHER
	public static void readUsersFileToArrayList() {
		if(new File(usersConfPath).exists()) {
			try {
				BufferedReader bfr = new BufferedReader(new FileReader(usersConfPath));
				String line = bfr.readLine();
				while((line != null) && (!line.isEmpty())) {
					int id = 0;
					String name = "";
					String password = "";
					String permission = "";
					String age = "";
					String phoneNumber = "";
					
					if(line.contains(",")) {
						String parts[] = line.split(",");
						id = Integer.parseInt(parts[0].trim());
						name = parts[1].trim();
						password = parts[2].trim();
						password = password.toUpperCase();
						permission = parts[3].trim();
						age = parts[4].trim();
						if(permission.equals("administrator")) {
							phoneNumber = parts[5].trim();
							adminPhoneNumber = phoneNumber;
						}else {
							phoneNumber = "";
						}
					}
					Users temp = new Users(id,name,password,permission,age,phoneNumber);
					/*System.out.println("Name: " + name);
					System.out.println("Permission: " + permission);
					System.out.println("Password: " + password);*/
					
					usersList.add(temp);
					//System.out.println("DEBUG WARNING - User added");
					
					line = bfr.readLine();
				}
				bfr.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			System.out.println("DEBUG WARNING - All users loaded from config to arraylist");
		}
	}
	
	public static void readAnimalsFileToArrayList() {
		if(new File(animalsDBPath).exists()) {
			try {
				BufferedReader bfr = new BufferedReader(new FileReader(animalsDBPath));
				String line = bfr.readLine();
				while((line != null) && (!line.isEmpty())) {
					int id = 0;
					String species = "";
					String category = "";
					String name = "";
					String age = "";
					String health = "";
					String weight = "";
					String sex = "";
					String wingspan = "";
					String notes = "";
					
					//System.out.println("DEBUG WARNING - got this far 1");
					if(line.contains(",")) {
						//System.out.println("DEBUG WARNING - line " + line);
						String parts[] = line.split(",");
						//System.out.println("DEBUG WARNING - parts[].length " + parts.length);
						id = Integer.parseInt(parts[0].trim());
						species = parts[1].trim();
						category = parts[2].trim();
						name = parts[3].trim();
						age = parts[4].trim();
						health = parts[5].trim();
						weight = parts[6].trim();
						//System.out.println("DEBUG WARNING - got this far 2");
						sex = parts[7].trim();
						wingspan = parts[8].trim();
						//System.out.println("DEBUG WARNING - got this far 3");
						notes = parts[9].trim();
					}
					Animals temp = new Animals(id,species,category,name,age,health,weight,sex,wingspan,notes);
					animalsList.add(temp);
					//System.out.println("DEBUG WARNING - animal added");
					
					line = bfr.readLine();
				}
				bfr.close();
			}catch(IOException e){
				e.printStackTrace();
			}
			System.out.println("DEBUG WARNING - All animal records loaded from config to arraylist");
		}
	}
	
	public static void writeArrayListDataToFile(String selection) {
		String toAppend;
		if(selection.equals("users")) {	
			File dirUsr = new File(usersConfFolder);
			if(!dirUsr.exists()) {
				dirUsr.mkdir();
			}
			
			File fileUsr = new File(usersConfPath);
			if(fileUsr.exists()) {
				fileUsr.delete();
			}
			
			String name;
			String password;
			String permission;
			String age;
			String phoneNumber;
			for(int i = 0; i<usersList.size(); i++) {
				name = usersList.get(i).name;
				password = usersList.get(i).password;
				permission = usersList.get(i).permission;
				age = usersList.get(i).age;
				phoneNumber = usersList.get(i).phoneNumber;
			
				toAppend = name + ", " + password +  ", " + permission +  ", " + age +  ", " + phoneNumber;
				appendStringToTextFile(usersConfPath, toAppend);
			}
		}
		if(selection.equals("animals")) {	
			File dirAnm = new File(animalsDBFolder);
			if(!dirAnm.exists()) {
				dirAnm.mkdir();
				//System.out.println("DEBUG WARNING - data folder created");
			}else {
				//System.out.println("DEBUG WARNING - data folder already exists");
			}
			
			File fileAnm = new File(animalsDBPath);
			if(fileAnm.exists()) {
				fileAnm.delete();
			}				
			
			String species, category, name, age, health, weight, sex, wingspan, notes;
			int id;
			for(int i = 0; i<animalsList.size(); i++) {
				id = animalsList.get(i).id;
				species = animalsList.get(i).species;
				category = animalsList.get(i).category;
				name = animalsList.get(i).name;
				age = animalsList.get(i).age;
				health = animalsList.get(i).health;
				weight = animalsList.get(i).weight;
				sex = animalsList.get(i).sex;
				wingspan = animalsList.get(i).wingspan;
				notes = animalsList.get(i).notes;
				
				toAppend = id + ", " + species +  ", " + category +  ", " + name + ", " + age + ", " + health + ", " + weight + ", " + sex + ", " + wingspan + ", " + notes;
				appendStringToTextFile(animalsDBPath, toAppend);
			}
		}
	}
	
	public static void appendStringToTextFile(String path, String toAppend) {
		try(FileWriter fw = new FileWriter(path, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw))
			{
			out.println(toAppend);
			//System.out.println("DEBUG WARNING - Input " + toAppend + " written");
			    
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
		
	//Hashes the inputed password with MD5 and returns it as a usable String of 32 characters, thanks StackOverflow!
	public static String getHash(String txt, String hashType) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance(hashType);
			byte[] array = md.digest(txt.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
			}
			return sb.toString();
		        
		} catch (java.security.NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static int getLastID(String selection) {
		int lastID = 0;
		int maxID = 0;
		int id;
		
		if(selection.equals("users")) {
			for(Users user : usersList) {
				id = user.getID();
				if(id > maxID) {
					maxID = id;
				}
			}
		}
		
		if(selection.equals("animals")) {
			for(Animals animal : animalsList) {
				id = animal.getID();
				if(id > maxID) {
					maxID = id;
				}
			}
		}
		
		lastID = maxID;
		return lastID;
	}
	
	public static boolean isNum(String strNum) {
	    boolean ret = true;
	    try {
	        Double.parseDouble(strNum);
	    }catch (NumberFormatException e) {
	        ret = false;
	    }
	    return ret;
	}
	//DEBUG METHODS
	
	//Prints the usersList ArrayList
	public static void printArrayListUsers() {
		System.out.println();
		System.out.println("Listing all loaded user data");
		for(int i = 0; i<usersList.size(); i++) {
			System.out.println("i: " + i + " - id - " + usersList.get(i).id);
			System.out.println("i: " + i + " - " + usersList.get(i).name);
			System.out.println("i: " + i + " - " + usersList.get(i).password);
			System.out.println("i: " + i + " - " + usersList.get(i).permission);
			System.out.println("i: " + i + " - " + usersList.get(i).age);
			System.out.println("i: " + i + " - " + usersList.get(i).phoneNumber);
		} 
		int countRecords = usersList.size();
		System.out.println("There are " + countRecords + " users in the database");
		System.out.println();
	}
	
	public static void printArrayListAnimals() {
		System.out.println();
		System.out.println("Listing all loaded animal records");
		for(int i = 0; i<animalsList.size(); i++) {
			System.out.println("i: " + i + " - id - " + animalsList.get(i).id);
			System.out.println("i: " + i + " - species - " + animalsList.get(i).species);
			System.out.println("i: " + i + " - category - " + animalsList.get(i).category);
			System.out.println("i: " + i + " - name - " + animalsList.get(i).name);
			System.out.println("i: " + i + " - age - " + animalsList.get(i).age);
			System.out.println("i: " + i + " - health - " + animalsList.get(i).health);
			System.out.println("i: " + i + " - weight - " + animalsList.get(i).weight);
			System.out.println("i: " + i + " - sex - " + animalsList.get(i).sex);
			System.out.println("i: " + i + " - wingspan - " + animalsList.get(i).wingspan);
			System.out.println("i: " + i + " - notes - " + animalsList.get(i).notes);
		} 
		int countRecords = animalsList.size();
		System.out.println("There are " + countRecords + " records in the database");
		System.out.println();
	}
	
}
