package character;

public class Archer extends Character{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Archer(){
		_name = "Archer";
		_BASE_STATS[STRENGTH] = 7;
		_BASE_STATS[DEFENSE] = 5;
		_BASE_STATS[SPECIAL] = 5;
		_BASE_STATS[RESISTANCE] = 6;
		_BASE_STATS[SPEED] = 9;
		_BASE_STATS[SKILL] = 9;
		_BASE_STATS[LUCK] = 3;
		_BASE_STATS[MAX_HP] = 14;
	}
}
