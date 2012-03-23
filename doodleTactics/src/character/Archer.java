package character;

import javax.swing.JPanel;

public class Archer extends Character{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

<<<<<<< HEAD
	public Archer(String name){
		super(name);
=======
	public Archer(JPanel container, String avatar, String profile, String left, String right, String up, String down){
		super(container, avatar, profile, left, right, up, down);
		_name = "Archer";
>>>>>>> fcd3420727af5008370212e05ace5a3fb2d234d6
		_BASE_STATS[STRENGTH] = 7;
		_BASE_STATS[DEFENSE] = 5;
		_BASE_STATS[SPECIAL] = 5;
		_BASE_STATS[RESISTANCE] = 6;
		_BASE_STATS[SPEED] = 9;
		_BASE_STATS[SKILL] = 9;
		_BASE_STATS[LUCK] = 3;
		_BASE_STATS[MAX_HP] = 14;
		initStats();
	}
}
