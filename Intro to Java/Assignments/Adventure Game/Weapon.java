import java.util.Random;

public class Weapon
{
	/*** Public Constants ***/

	public static final int SHORT_SWORD_MIN = 1;
	public static final int SHORT_SWORD_MAX = 4;
	public static final int LONG_SWORD_MIN = 3;
	public static final int LONG_SWORD_MAX = 7;
	public static final int JUMP_KICK_MIN = 2;
	public static final int JUMP_KICK_MAX = 6;
	public static final int EAGLE_STRIKE_MIN = 3;
	public static final int EAGLE_STRIKE_MAX = 8;
	public static final int AXE_MIN = 2;
	public static final int AXE_MAX = 6;
	public static final int CLUB_MIN = 1;
	public static final int CLUB_MAX = 3;
	public static final int MACE_MIN = 2;
	public static final int MACE_MAX = 6;
	public static final int FIRE_BLAST_MIN = 4;
	public static final int FIRE_BLAST_MAX = 10;

	/*** Attributes ***/

	private String name;
	private int minDamage;
	private int maxDamage;

	/*** Singletons ***/
    
    private static Weapon shortSword = null;
    private static Weapon longSword = null;
    private static Weapon jumpKick = null;
    private static Weapon eagleStrike = null;
    private static Weapon axe = null;
    private static Weapon club = null;
    private static Weapon mace = null;
    private static Weapon fireBlast = null;

	/*** Methods ***/

	// constructor
	public Weapon(String _name, int _minDamage, int _maxDamage)
	{
		name = _name;
		minDamage = _minDamage;
		maxDamage = _maxDamage;
	}

	public String getName()
	{
		return name;
	}

	public int getMinDamage()
	{
		return minDamage;
	}

	public int getMaxDamage()
	{
		return maxDamage;
	}

	// determines weapon damage as random number in range of [minDamage-maxDamage]
	public int getDamage()
	{
		Random randomNum = new Random();
		return randomNum.nextInt(maxDamage - minDamage + 1) + minDamage;
	}

	// Utility Methods
	
	public static Weapon getShortSword()
	{
		if (shortSword == null)
		{
			shortSword = new Weapon("Short Sword", SHORT_SWORD_MIN, SHORT_SWORD_MAX);
		}
		return shortSword;
	}
	
	public static Weapon getLongSword()
	{
		if (longSword == null)
		{
			longSword = new Weapon("Long Sword", LONG_SWORD_MIN, LONG_SWORD_MAX);
		}
		return longSword;
	}

	public static Weapon getJumpKick()
	{
		if (jumpKick == null)
		{
			jumpKick = new Weapon("Jump Kick", Weapon.JUMP_KICK_MIN, Weapon.JUMP_KICK_MAX);
		}
		return jumpKick;
	}

	public static Weapon getEagleStrike()
	{
		if (eagleStrike == null)
		{
			eagleStrike = new Weapon("Eagle Strike", Weapon.EAGLE_STRIKE_MIN, Weapon.EAGLE_STRIKE_MAX);
		}
		return eagleStrike;
	}

	public static Weapon getAxe()
	{
		if (axe == null)
		{
			axe = new Weapon("Axe", Weapon.AXE_MIN, Weapon.AXE_MAX);
		}
		return axe;
	}

	public static Weapon getClub()
	{
		if (club == null)
		{
			club = new Weapon("Club", Weapon.CLUB_MIN, Weapon.CLUB_MAX);
		}
		return club;
	}

	public static Weapon getMace()
	{
		if (mace == null)
		{
			mace = new Weapon("Long Sword", MACE_MIN, MACE_MAX);
		}
		return mace;
	}

	public static Weapon getFireBlast()
	{
		if (fireBlast == null)
		{
			fireBlast = new Weapon("Fire Blast", Weapon.FIRE_BLAST_MIN, Weapon.FIRE_BLAST_MAX);
		}
		return fireBlast;
	}
}