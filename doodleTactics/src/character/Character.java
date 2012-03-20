package character;

import java.awt.image.BufferedImage;
import items.*;
public abstract class Character {
	/**
	 * @author czchapma
	 */
	
	final static int LEVELCAP = 99;
	
	String _name;
	
	//stats
	//indices where stat is located in subsequent arrays
	final static int STRENGTH = 0;
	final static int DEFENSE = 1;
	final static int SPECIAL = 2;
	final static int RESISTANCE = 3;
	final static int SPEED = 4;
	final static int SKILL = 5;
	final static int LUCK =  6;
	final static int MAX_HP =  7;

	
	//stat arrays (indexed by type of stat, see above)
	final int[] _BASE_STATS; //initial
	int[] _currentStats; //updated with levelUp()
	int[] _unitPoints; //gained by character
	final int[] YIELD; //given out by character
	
	//misc. character info
	int _level, _exp, _currentHP;
	
	//items
	Weapon _equipped;
	Curirass _cuirass;
	Shield _shield;
	ArrayList<Item> _inventory; //items not being worn
	int capacity; //max number of items the character can carry
	
	//images
	private BufferedImage _avatar;
	private BufferedImage _profile;
	private BufferedImage _left;
	private BufferedImage _right;
	private BufferedImage _up;
	private BufferedImage _down;
	
	private CombatController _affiliation; //player/AI etc
	
	
	//methods
	
	/**
	 * levelUp
	 * transitions this Character to the next level (if possible) updating stats
	 * @throws InvalidLevelException if level > levelCap
	 */
	
	public void levelUp() throws InvalidLevelException{
		//_level cannot exceed imposed levelCap
		if(_level == LEVELCAP) throw new InvalidLevelException();
		
		_level++;
		
		//update stats
		for(int i=0; i<7; i++)
			_currentStats[i] = 10 * _BASE_STATS[i] + _level*_BASE_STATS[i] + _unitPoints[i]/12;
	}
	
	//TODO update signatures, get from ryan + joe
	/**
	 *  attack
	 *  @param opponent
	 *  @author rroelke
	 */
	public void attack(Character opponent){
		//TODO Ryan fills in
	}

	/** getMovementRange
	 * @author rroelke
	 */
	
	public void getMovementRange(){
		//TODO Ryan fills in
	}
	
	/** getAttackRange
	 * @author rroelke
	 */
	public void getAttackRange(){
		//TODO Ryan fills in
	}

	
	/**
	 * paintProfile
	 * @author jeshapir
	 */
	public void paintProfile(){
		//TODO fill in
	}
	
	/**
	 * paintLeft
	 * paints the left character image
	 * @author jeshapir
	 */
	public void paintLeft(Brush b){
		//TODO fill in
	}
	
	/**
	 * paintRight
	 * paints the right character image
	 * @author jeshapir
	 */
	public void paintRight(Brush b){
		//TODO fill in
	}
	
	/**
	 * paintUp
	 * paints the 'up' character image
	 * @author jeshapir
	 */
	public void paintUp(Brush b){
		//TODO fill in
	}
	
	/**
	 * paintDown
	 * paints the 'down' character image
	 * @author jeshapir
	 */
	public void paintDown(Brush b){
		//TODO Joe fills in
	}
	
	/**
	 * toXML
	 * converts a Character to an XML format, for saving purposes
	 * @author czchapma
	 */
	public String toXML(){
		//TODO write
		return null;
	}
	

}
