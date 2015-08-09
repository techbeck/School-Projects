import  java.util.Random;

public class Enemy
{
	static Random randomNum = new Random();

	/*** Attributes ***/

	private String name;
	private int hitPoints;
	private int strength;
	private Weapon weapon;

	/*** Singletons ***/

	private static Enemy goblin = null;
	private static Enemy skeleton = null;
	private static Enemy troll = null;
	private static Enemy wizard = null;

	/*** Methods ***/

	// constructor
	public Enemy(String _name, int _hitPoints, int _strength, Weapon _weapon)
	{
		name = _name;
		hitPoints = _hitPoints;
		strength = _strength;
		weapon = _weapon;
	}

	public String getName()
	{
		return name;
	}

	public int getHitPoints()
	{
		return hitPoints;
	}

	public void resetHitPoints()
	{
		hitPoints = 25;
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

	// enemy attacks player
	public void attack(Player _player)
	{
		int weaponDamage = weapon.getDamage();
		int attack = strength + weaponDamage;
		int playerOldHP = _player.getHitPoints();
		_player.decreaseHitPoints(attack);
		System.out.printf("%s attacks with ATK = %d + %d = %d\n", name, strength, weaponDamage, attack);
		System.out.printf("%s HP is now %d - %d = %d\n\n", _player.getName(), playerOldHP, attack, _player.getHitPoints());
	}

	public boolean isDefeated()
	{
		if (hitPoints <= 0)
		{
			return true;
		}
		return false;
	}

	// determine number of coins dropped as random number in range of [30-50]
	public int dropCoins()
	{
		return randomNum.nextInt(21) + 30;
	}

	// which method is called depends on which path is taken
	public static int getNumGoblins()
	{
		return randomNum.nextInt(4) + 2;
	}
	public static int getNumSkeletons()
	{
		return randomNum.nextInt(5) + 3;
	}
	public static int getNumTrolls()
	{
		return randomNum.nextInt(3) + 2;
	}

	// Utility Methods
	
	public static Enemy getGoblin()
	{
		if (goblin == null)
		{
			goblin = new Enemy("Goblin", 25, 4, Weapon.getAxe());
		}
		return goblin;
	}

	public static Enemy getSkeleton()
	{
		if (skeleton == null)
		{
			skeleton = new Enemy("Skeleton", 25, 3, Weapon.getShortSword());
		}
		return skeleton;
	}

	public static Enemy getTroll()
	{
		if (troll == null)
		{
			troll = new Enemy("Troll", 25, 3, Weapon.getClub());
		}
		return troll;
	}

	public static Enemy getWizard()
	{
		if (wizard == null)
		{
			wizard = new Enemy("Wizard", 40, 8, Weapon.getFireBlast());
		}
		return wizard;
	}
}