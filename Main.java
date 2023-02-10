package guessTheNumberV2;

import java.util.Random;
import java.util.Scanner;

public class Main {
  private static Scanner scan = new Scanner(System.in);
  private static Random rand = new Random();
	public static void main(String[] args) {
		
				
		Database db = new Database();
		db.openDB();
		db.createTable();
		options();
		System.out.print("Write a command:");
		int command = scan.nextInt();
		String name;
		scan.nextLine();
		
		while(command != 5)
		{
			if(command == 1)
			{
				int round = 1;
				int score = 0;
				int number = 0;
				int attempts = 3;
				int attemptsCopy = attempts;
				int inNumber = 0;
				boolean lost = false;
				System.out.print("Write your name:");
				name = scan.nextLine();
				
				while(command != 4 && command != 5)
				{
					lost = false;
					System.out.println("Round " + round);
					number = rand.nextInt(5 + (round - 1) * 2) + 1;
					System.out.println("Guess the number between 1 and " + (5 + ((round - 1) * 2)) + " inclusive.");
					System.out.println("You have " + attempts + " attempts.");
					System.out.print("Write the number:");
					while(attempts > 0 && inNumber != number)
					{			
						inNumber = scan.nextInt();
						scan.nextLine();
				        if(inNumber == number)
				        {
				        	System.out.println("You won");
				        	System.out.println();
				        	score += (round * 2) + attempts;
				        	System.out.println("Score:" + score);
				        	round++;	
				        	attempts = attemptsCopy + 1;
				        	attemptsCopy = attempts;
				        	inNumber = 0;
				        	break;
				        }
				        else if(attempts > 1)
				        {
				        	System.out.print("Write again:");
				        	attempts--;
				        }
				        else
				        {
				        	System.out.println("Game over.The number was " + number);
				        	db.insertInTable(name, score, round);
				        	System.out.println();
				        	attempts = 3;
				        	attemptsCopy = attempts;
				        	round = 1;
				            lost = true;
				        	break;
				        }
					}
					System.out.println("4 - exit the game\n5 - exit to windows\n1 - continue/replay");
					command = scan.nextInt();
					scan.nextLine();
					while(command != 4 && command != 5 && command != 1)
					{
						System.out.println("Incorrect command.Write again");
						command = scan.nextInt();
						scan.nextLine();
					}
					if((command == 4 || command == 5) && !lost)
					{
						db.insertInTable(name, score, round);
					}
				}
				System.out.println();
			}
			else if(command == 2)
			{
				db.displayScores(3);
				System.out.println();
			}
			else if(command == 3)
			{
				System.out.print("Write the name:");
				name = scan.nextLine();
				db.displayPlayerScores(name);
			}
			if(command != 54)
			{
				System.out.print("Write the command:");
				command = scan.nextInt();
				scan.nextLine();
			}
			else if(command == 5)
			{
				System.out.println("Exit");
				System.exit(0);
			}
		}	
		if(command == 5)
		{
			db.closeDB();
			System.out.println("Exit");
		}
	}
	
	public static void options() {
		System.out.println("1 - Play the game");
		System.out.println("2 - Display players list");
		System.out.println("3 - Print a player's scores");
		System.out.println("4 - Exit the game");
		System.out.println("5 - Exit to windows");
	}

}
