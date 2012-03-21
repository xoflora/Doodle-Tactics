package character;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import controller.CombatController;

import items.*;
public abstract class Character implements Serializable{
	/**
	 * @author czchapma
	 */
	
	final static int LEVELCAP = 99;
	final static int NUM_STATS = 8;
	protected String _name;
	
	//stats
	//indices where stat is located in subsequent arrays
	protected final static int STRENGTH = 0;
	protected final static int DEFENSE = 1;
	protected final static int SPECIAL = 2;
	protected final static int RESISTANCE = 3;
	protected final static int SPEED = 4;
	protected final static int SKILL = 5;
	protected final static int LUCK =  6;
	protected final static int MAX_HP =  7;

	
	//stat arrays (indexed by type of stat, see above)
	protected final int[] _BASE_STATS; //initial
	protected int[] _currentStats; //updated with levelUp()
	protected int[] _unitPoints; //gained by character
	protected final int[] YIELD; //given out by character
	
	//misc. character info
	protected int _level, _exp, _currentHP;
	
	//items
	protected Weapon _equipped;
	protected Cuirass _cuirass;
	protected Shield _shield;
	protected List<Item> _inventory; //items not being worn
	protected int capacity; //max number of items the character can carry
	
	//images
	private BufferedImage _avatar;
	private BufferedImage _profile;
	private BufferedImage _left;
	private BufferedImage _right;
	private BufferedImage _up;
	private BufferedImage _down;
	
	private CombatController _affiliation; //player/AI etc
	
	//constructor
	public Character(){
		_BASE_STATS = new int[NUM_STATS];
		_currentStats = new int[NUM_STATS];
		_unitPoints = new int[NUM_STATS];
		YIELD = new int[NUM_STATS];
	}
	
	
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
		//TODO
		return -1;
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
	public void paintLeft(Graphics2D brush){
		//TODO fill in
	}
	
	/**
	 * paintRight
	 * paints the right character image
	 * @author jeshapir
	 */
	public void paintRight(Graphics2D brush){
		//TODO fill in
	}
	
	/**
	 * paintUp
	 * paints the 'up' character image
	 * @author jeshapir
	 */
	public void paintUp(Graphics2D brush){
		//TODO fill in
	}
	
	/**
	 * paintDown
	 * paints the 'down' character image
	 * @author jeshapir
	 */
	public void paintDown(Graphics2D brush){
		//TODO Joe fills in
	}
	
	/**
	 * flattens a Character to a file (using serialization), for saving purposes
	 * @author czchapma
	 */
	public void serialize(){
		String filename = "src/character/" + _name + ".ser";
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
			fis = new FileInputStream("src/character/" + name + ".ser");
			in = new ObjectInputStream(fis);
			c = (Character)in.readObject();
			in.close();
		} catch(IOException e){
			e.printStackTrace();
		} catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		return c;
	}
	
	public boolean equals(Character other){
		for(int i=0; i<NUM_STATS; i++)
			if(_BASE_STATS[i] != other._BASE_STATS[i])
				return false;
		if(_exp != other._exp)
			return false;
		if(_currentHP != other._currentHP)
			return false;
		if(_level != other._level)
			return false;
		
		return true;
	}
	
	public static void main(String[] args){
		//serialization test
		Archer a = new Archer();
		a._exp = 500;
		a.serialize();
		Archer restored = (Archer) restore(a._name);
		assert(restored.equals(a));
		
		Warrior w = new Warrior();
		w._currentHP = 100;
		w.serialize();
		Warrior restoredW = (Warrior) restore(w._name);
		assert(restoredW.equals(w));
		
		Mage m = new Mage();
		m._level = 5;
		m.serialize();
		Mage restoredM = (Mage) restore(m._name);
		assert(restoredM.equals(m));
		
		Thief t = new Thief();
		t._level = 1;
		t._currentHP = 50;
		t._exp = 200;
		t.serialize();
		Thief restoredT = (Thief) restore(t._name);
		assert(restoredT.equals(t));
	}

}
