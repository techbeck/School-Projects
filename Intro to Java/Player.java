import java.util.Random;
import java.util.Scanner;

public class Player
{
	/*** Attributes ***/

	private String name;
	private int hitPoints;
	private int strength;
	private Weapon weapon;
	private int coins;

	/*** Singletons ***/

	private static Player rogue = null;
	private static Player paladin = null;
	private static Player jackieChan = null;
	private static Player chuckNorris = null;

	/*** Methods ***/

	// constructor
	public Player(String _name, int _hitPoints, int _strength, Weapon _weapon)
	{
		name = _name;
		hitPoints = _hitPoints;
		strength = _strength;
		weapon = _weapon;
		coins = 0;
	}

	public String getName()
	{
		return name;
	}

	public int getHitPoints()
	{
		return hitPoints;
	}

	public void increaseHitPoints(int _pointsIncrease)
	{
		hitPoints += _pointsIncrease;
	}

	public void decreaseHitPoints(int _pointsDecrease)
	{
		hitPoints -= _pointsDecrease;
	}

	public int getStrength()
	{
		return strength;
	}

	public void increaseStrength(int _strengthIncrease)
	{
		strength += _strengthIncrease;
	}

	public void setWeapon(Weapon _weapon)
	{
		weapon = _weapon;
	}

	public int getCoins()
	{
		return coins;
	}

	public void increaseCoins(int _coins)
	{
		coins += _coins;
	}

	public void decreaseCoins(int _coins)
	{
		coins -= _coins;
	}

	public void attack(Enemy _enemy)
	{
		int weaponDamage = weapon.getDamage();
		int attack = strength + weaponDamage;
		int enemyOldHP = _enemy.getHitPoints();
		_enemy.decreaseHitPoints(attack);
		System.out.printf("%s attacks with ATK = %d + %d = %d\n", name, strength, weaponDamage, attack);
		System.out.printf("%s HP is now %d - %d = %d\n\n", _enemy.getName(), enemyOldHP, attack, _enemy.getHitPoints());
	}

	public void battleMinion(Enemy _enemy)
	{
		while (_enemy.getHitPoints() > 0 && hitPoints > 0)
		{
			this.attack(_enemy);

			if (_enemy.isDefeated())
			{
				break;
			}

			_enemy.attack(this);	
		} // end while	
	}

	public void battleWizard(Enemy _enemy)
	{
		Random randomNums = new Random();
		Scanner keyboard = new Scanner(System.in);

		int randomNumAnswer = randomNums.nextInt(6) + 1;

		System.out.printf("***%s vs The Evil Wizard***\n", name);

		while (hitPoints > 0 && _enemy.getHitPoints() > 0)
		{
			System.out.println("Choose your action:\n1. Attack\n2. Attempt Spell Cast\n");
            System.out.print("What would you like to do: ");
            int playerActionChoice = keyboard.nextInt();
            System.out.println();

			switch(playerActionChoice)
			{
				case 1:		// Attack
					this.attack(_enemy);
					break;
				case 2:		// Attempt spell
					System.out.print("Enter your guess: ");
					int randomNumGuess = keyboard.nextInt();
					if (randomNumGuess == randomNumAnswer)
					{
						System.out.println("\nCorrect!\n");
						System.out.printf("The %s's spell is cast successfully! The Wizard's HP is now 0!\n\n", name);
						_enemy.decreaseHitPoints(_enemy.getHitPoints());
					}
					else
					{
						System.out.println("\nIncorrect! The spell cast fails!\n");
					}
					break;
				default:	// fail-safe
					System.out.printf("\nInvalid input. %d is not an available option.\n\n", playerActionChoice);
					System.exit(0);
			}

			if (_enemy.getHitPoints() <= 0)
			{
				break;
			}
			_enemy.attack(this);
		} // end while

		if (this.isDefeated())
			{
				System.out.printf("--%s is defeated in battle!--\n\nGAME OVER\n", name);
				System.exit(0);
			}
	}

	public boolean isDefeated()
	{
		if (hitPoints <= 0)
		{
			return true;
		}
		return false;
	}

	// Utility Methods
	
	public static Player getRogue()
	{
		if (rogue == null)
		{
			rogue = new Player("Rogue", 50, 7, Weapon.getShortSword());
		}
		return rogue;
	}

	public static Player getPaladin()
	{
		if (paladin == null)
		{
			paladin = new Player("Paladin", 30, 12, Weapon.getLongSword());
		}
		return paladin;
	}

	public static Player getJackieChan()
	{
		if (jackieChan == null)
		{
			jackieChan = new Player("Jackie Chan", 40, 10, Weapon.getJumpKick());
		}
		return jackieChan;
	}

	public static Player getChuckNorris()
	{
		if (chuckNorris == null)
		{
			chuckNorris = new Player("Chuck Norris", 60, 12, Weapon.getEagleStrike());
		}
		return chuckNorris;
	}
}