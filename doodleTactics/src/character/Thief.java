package character;

import javax.swing.JPanel;

public class Thief extends Character{
	
<<<<<<< HEAD
	public Thief(String name){
		super(name);

=======
	public Thief(JPanel container, String avatar, String profile, String left, String right, String up, String down){
		super(container, avatar, profile, left, right, up, down);
		_name = "Thief";
		
>>>>>>> fcd3420727af5008370212e05ace5a3fb2d234d6
		//set base stats
		_BASE_STATS[STRENGTH] = 6;
		_BASE_STATS[DEFENSE] = 6;
		_BASE_STATS[SPECIAL] = 5;
		_BASE_STATS[RESISTANCE] = 6;
		_BASE_STATS[SPEED] = 10;
		_BASE_STATS[SKILL] = 7;
		_BASE_STATS[LUCK] = 9;
		_BASE_STATS[MAX_HP] = 13;
		initStats();
	}
}
