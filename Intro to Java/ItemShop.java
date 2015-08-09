import java.util.Scanner;

public class ItemShop
{
	/*** Private Constants ***/

	private final int LONG_SWORD_COST = 120;
	private final int SHORT_SWORD_COST = 90;
	private final int MACE_COST = 80;
	private final int MAGIC_RING_COST = 150;
	private final int HEALING_POTION_COST = 10;

	/*** Attributes ***/

	private int playerOrder;
	private int quantity;
	private int discount;
	private int totalCost = 0;
	private int finalCost = 0;
	private int userDecision;
	private int itemEffect;

	/*** Methods ***/

	public void visitItemShop(Player _player)
	{
		Scanner keyboard = new Scanner(System.in);

		System.out.print("Would you like to visit The Item Shop?\n" +
							"Enter 1 for \"yes\" or 0 for \"no\": ");
		userDecision = keyboard.nextInt();
		
		if (userDecision==0)
			return;

		System.out.println("\nWelcome to the Item Shop!");

		while (userDecision != 0 && _player.getCoins() > 0)
		{
			System.out.printf("\nYou currently have %d gold.\n\n", _player.getCoins());

			System.out.println("Here’s what we have for sale (all prices are in units of gold) :\n");
			System.out.printf("%-20s%4d\n", "1. Long Sword", 120);
			System.out.printf("%-20s%4d\n", "2. Short Sword", 90);
			System.out.printf("%-20s%4d\n", "3. Mace", 80);
			System.out.printf("%-20s%4d\n", "4. Ring of Strength", 150);
			System.out.printf("%-20s%4d\n\n", "5. Healing Potion", 10);
			System.out.print("Please enter the item number: ");
			playerOrder = keyboard.nextInt();

			System.out.print("Please enter the quantity: ");
			quantity = keyboard.nextInt();
			// Assumption 1: User will input a positive number.
			// Assumption 2: User will only order one weapon.

			switch (playerOrder)
			{
				case 1:		// Long Sword
					totalCost = (LONG_SWORD_COST * quantity);
					break;
				case 2:		// Short Sword
					totalCost = (SHORT_SWORD_COST * quantity);
					break;
				case 3:		// Mace
					totalCost = (MACE_COST * quantity);
					break;
				case 4:		// Ring of Strength
					totalCost = (MAGIC_RING_COST * quantity);
					break;
				case 5:		// Healing Potion
					totalCost = (HEALING_POTION_COST * quantity);
					break;
				default:
					System.out.printf("\nInvalid input. %d is not an available option.\n\n", playerOrder);
					System.exit(0);
			}

			System.out.println("\nTotal cost: " + totalCost + " gold");

			if (quantity >= 3)
			{
				discount = (int)(totalCost * 0.10);
			}
			else
			{
				discount = 0;
			}
					
			System.out.println("Discount: " + discount + " gold");

			finalCost = totalCost - discount;

			if (finalCost <= _player.getCoins())
			{
				System.out.printf("Final cost: %d gold\n\n", finalCost);
				_player.decreaseCoins(finalCost);
				switch (playerOrder)
				{
					case 1:		// Long Sword
						_player.setWeapon(Weapon.getLongSword());
						System.out.println("The transaction is successful!");
						System.out.printf("Your remaining funds: %d gold\n\n", _player.getCoins());
						System.out.println("Thank you! Your transaction is complete!\n");
						System.out.println("You purchased: Long Sword");
						System.out.println("Your weapon damage is now: 3 - 7");
						break;
					case 2:		// Short Sword
						_player.setWeapon(Weapon.getShortSword());
						System.out.println("The transaction is successful!");
						System.out.printf("Your remaining funds: %d gold\n\n", _player.getCoins());
						System.out.println("Thank you! Your transaction is complete!\n");
						System.out.println("You purchased: Short Sword");
						System.out.println("Your weapon damage is now: 1 - 4");
						break;
					case 3:		// Mace
						_player.setWeapon(Weapon.getMace());
						System.out.println("The transaction is successful!");
						System.out.printf("Your remaining funds: %d gold\n\n", _player.getCoins());
						System.out.println("Thank you! Your transaction is complete!\n");
						System.out.println("You purchased: Mace");
						System.out.println("Your weapon damage is now: 2 - 6");
						break;
					case 4:		// Ring of Strength
						int oldStrength = _player.getStrength();
						itemEffect = 5 * quantity;
						_player.increaseStrength(itemEffect);
						System.out.println("The transaction is successful!");
						System.out.printf("Your remaining funds: %d gold\n\n", _player.getCoins());
						System.out.println("Thank you! Your transaction is complete!\n");
						System.out.println("\nYou purchased: Ring of Strength");
						System.out.printf("Your strength has increased to %d + %d = %d \n", oldStrength, itemEffect, _player.getStrength());
						break;
					case 5:		// Healing Potion
						int oldHP = _player.getHitPoints();
						itemEffect = 10 * quantity;
						_player.increaseHitPoints(itemEffect);
						System.out.println("The transaction is successful!");
						System.out.printf("Your remaining funds: %d gold\n\n", _player.getCoins());
						System.out.println("Thank you! Your transaction is complete!\n");
						System.out.println("You purchased: Healing Potion");
						System.out.printf("Your HP has increased to %d + %d = %d \n", oldHP, itemEffect, _player.getHitPoints());
						break;
				} // end of switch
			}
			else
			{
				System.out.printf("Final cost: %d gold\n\n", finalCost);
				System.out.println("You have insufficient funds!");
			}

			System.out.print("\nWould you like to make another purchase?\n" +
								"Enter 1 for \"yes\" or 0 for \"no\": ");
			userDecision = keyboard.nextInt();
			if (!(userDecision == 1 || userDecision == 0))
			{
				System.out.printf("\nInvalid input. %d is not an available option.\n\n", userDecision);
				System.exit(0);
			}
		} // end of while

		System.out.println("\nGoodbye! Please stop again!");
	}
}