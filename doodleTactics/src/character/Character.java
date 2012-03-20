package character;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

import items.*;
public abstract class Character implements Serializable{
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
	Cuirass _cuirass;
	Shield _shield;
	List<Item> _inventory; //items not being worn
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

	/**
	 * computes the movement range of the Character
	 * @return
	 */
	public int getMovementRange(){
		
	}
	
	/** 
	 * computes the attack range of the Character
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
	 * flattens a Character to a file (using serialization), for saving purposes
	 * @author czchapma
	 */
	public void serialize(){
		String filename = _name + ".ser";
		FileOutputStream fos = null;
		ObjectOutputStream out  = null;
		try{
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(this);
			out.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * unflattens a character
	 */
	public static Character restore(String name){
		Character c = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try{
			fis = new FileInputStream(name + ".ser");
			in = new ObjectInputStream(fis);
			c = (Character)in.readObject();
			in.close();
		} catch(IOException e){
			e.printStackTrace();
		} catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}

}
