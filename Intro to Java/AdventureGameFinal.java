/*
Rebecca Addison
CS7
Summer 2015
Assignment 4

Bonus Points:
4th character choice
3rd path choice
fail-safe for invalid input
uses singletons & utility methods (creates objects within their classes)
- could make constructors private due to these methods, but I didn't in order to stick to the assignment outline
*/
 
import java.util.Scanner;
import java.util.Random;

/**
Adventure Game:
User's chosen character battles minions, then the Evil Wizard.
In each battle, the combatants take turns and fight to the death.
Attack damage is based on character's strength + the weapon's damage, which varies from turn to turn.
In between the minion battles and the Wizard battle, the user can choose to enter the Item Shop to buy items.
*/
public class AdventureGameFinal
{
	public static void main(String[] args)
	{
		/*** Objects ***/
		Scanner keyboard = new Scanner(System.in);
		Random randomNum = new Random();
		Player player = null;
		Enemy minion = null;

		System.out.println("\nAdventure Game - Start!\n");
		System.out.println("Here are the characters:");
		System.out.println("1. Rogue\n2. Paladin\n3. Jackie Chan\n4. Chuck Norris\n");

		System.out.print("Which character do you choose?: ");
		int characterChoice = keyboard.nextInt();

		// Assumption 1: User will input either 1, 2, 3, or 4 for character.
		switch (characterChoice)
		{
			case 1:		// Rogue
				player = Player.getRogue();
				break;
			case 2:		// Paladin
				player = Player.getPaladin();
				break;
			case 3:		// Jackie Chan
				player = Player.getJackieChan();
				break;
			case 4:		// Chuck Norris
				player = Player.getChuckNorris();
				break;
			default:
				System.out.printf("\nInvalid input for character. %d is not an available option.\n\n", characterChoice);
				System.exit(0);
		}

		System.out.printf("You chose: %s\n\n", player.getName());

		System.out.print("The Evil Wizard must be defeated! He is in The Castle. To get to ");
		System.out.println("The Castle, you must travel through either:");
		System.out.println("1. The Forest\n2. The Graveyard\n3. The Cave\n");

		System.out.print("Which path will you take?: ");
		int pathChoice = keyboard.nextInt();
		int numEnemies = 0;

		//Assumption 2: User will input either 1, 2, or 3 for path.
		switch(pathChoice)
		{
			case 1:		// Forest
				System.out.println("You chose: The Forest\n");
				minion = Enemy.getGoblin();
				numEnemies = minion.getNumGoblins();
				System.out.printf("Once you enter The Forest, you encounter %d Goblins! Time for battle!\n\n", numEnemies);
				break;
			case 2:		// Graveyard
				System.out.println("You chose: The Graveyard\n");
				minion = Enemy.getSkeleton();
				numEnemies = minion.getNumSkeletons();
				System.out.printf("Once you enter The Graveyard, you encounter %d Skeletons! Time for battle!\n\n", numEnemies);
				break;
			case 3:		// Cave
				System.out.println("You chose: The Cave\n");
				minion = Enemy.getTroll();
				numEnemies = minion.getNumTrolls();
				System.out.printf("Once you enter The Cave, you encounter %d Trolls! Time for battle!\n\n", numEnemies);
				break;
			default:
				System.out.printf("\nInvalid input for path. %d is not an available option.\n\n", pathChoice);
				System.exit(0);
		}

		// Phase 1 battle simulation
		for (int i = 1; i <= numEnemies; i++)
		{
			minion.resetHitPoints();

			System.out.printf("***%s vs %s %d***\n", player.getName(), minion.getName(), i);
			
			player.battleMinion(minion);

			if (player.isDefeated())
			{
				System.out.printf("--%s is defeated in battle!--\n\nGAME OVER\n", player.getName());
				System.exit(0);
			}
			else
			{
				System.out.printf("%s defeated %s %d!\n", player.getName(), minion.getName(), i);
				System.out.printf("%s gains %d gold coins!\n\n", player.getName(), minion.dropCoins());
				player.increaseCoins(minion.dropCoins());
			}
		} // end minion battle for loop

		System.out.printf("Your HP is: %d\n\n", player.getHitPoints());

		// Item Shop simulation
		ItemShop shop = new ItemShop();
		shop.visitItemShop(player);

		System.out.println("You have now reached The Castle! Time to battle The Evil Wizard!\n");

		// Phase 2 battle simulation
		Enemy wizard = Enemy.getWizard();

		player.battleWizard(wizard);

		if (player.getHitPoints() > 0)
		{
			System.out.printf("--%s wins the battle!--\n\n", player.getName());
			System.out.println("You win! Congratulations!\n");
		}
		else
		{
			System.out.printf("--%s is defeated in battle!--\n\nGAME OVER\n\n", player.getName());
		}
	}
}