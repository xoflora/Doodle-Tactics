package character;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import event.DialogueBox;
import event.InvalidFileException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import controller.combatController.CombatController;

import graphics.Rectangle;
import graphics.Shape;
import items.*;
public abstract class Character extends Rectangle{

	/**
	 * 
	 */
	/**
	 * @author czchapma
	 */

	final static int LEVELCAP = 99;
	final static int NUM_STATS = 8;
	protected String _name;
	private static int numCharacters = 0;
	protected final int _id;

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
	protected Footgear _footgear;
	protected HashMap<Integer,Item> _inventory; //items not being worn
	protected int _capacity; //max number of items the character can carry

	//images
	private BufferedImage _avatar;
	private BufferedImage _profile;
	private BufferedImage _currentImage;
	private BufferedImage _left;
	private BufferedImage _right;
	private BufferedImage _up;
	private BufferedImage _down;

	private CombatController _affiliation; //player/AI etc

	public Character(JPanel container, String avatar, String profile, String left, String right, String up, String down, String name){
		super(container);
		_BASE_STATS = new int[NUM_STATS];
		_currentStats = new int[NUM_STATS];
		_unitPoints = new int[NUM_STATS]; 
		YIELD = new int[NUM_STATS];
		_id = numCharacters;
		numCharacters++;
		
		_name = name;
		_level = 1;
		_inventory	= new HashMap<Integer,Item>();
		_capacity = 5;
		
		_affiliation = null;

		try {
			_avatar = ImageIO.read(new File(avatar));
			_profile = ImageIO.read(new File(profile));
			_left = ImageIO.read(new File(left));
			_right = ImageIO.read(new File(right));
			_up = ImageIO.read(new File(up));
			_down = ImageIO.read(new File(down));
		} catch(IOException e) {
			//System.out.println("Bad file path!");
		}
	}
	
	/**
	 * getters and setters
	 */
	public int getHP(){
		return _currentHP;
	}

	public int getExp(){
		return _exp;
	}

	public int getLevel(){
		return _level;
	}

	public int[] getBaseStats(){
		return _BASE_STATS;
	}

	public String getName(){
		return _name;
	}

	public Weapon getWeapon(){
		return _equipped;
	}

	public Cuirass getCuirass(){
		return _cuirass;
	}

	public Shield getShield(){
		return _shield;
	}

	/**
	 * Initializes Current Stats to Base Stats
	 */
	protected void initStats(){
		for(int i=0; i<NUM_STATS; i++){
			_currentStats[i] = _BASE_STATS[i];
		}
		_currentHP = _BASE_STATS[MAX_HP];
	}

	/**
	 * Adds Item to inventory
	 * @param i - item to add
	 * @throws ItemException
	 */
	public void addToInventory(Item i) throws ItemException{
		//check if item is already in inventory
		if(_inventory.containsKey(i))
			throw new ItemException("Item " + i._id + " already in inventory");

		//check if capacity has been exceeded
		if(_inventory.size() == _capacity)
			throw new ItemException("Capacity reached");

		_inventory.put(i._id, i);
	}

	/**
	 * Removes Item from inventory
	 * @param i - Item to remove
	 * @throws ItemException if item not in _inventory
	 */
	public void removeFromInventory(Item i) throws ItemException{
		Item removed = _inventory.remove(i._id);
		if(removed == null)
			throw new ItemException("Item " + i._id  + " is not currently being worn");
	}

	/**
	 * Updates experience points
	 * @param toAdd -amount to add
	 */
	public void updateExp(int toAdd){
		_exp += toAdd;
	}

	/**
	 * Updates Health Points
	 * @param change - positive to increase HP, negative to decrease
	 */
	public void updateHP(int change){
		_currentHP +=change;
		//HP cannot exceed MaxHP
		if(_currentHP > _currentStats[MAX_HP])
			_currentHP = _currentStats[MAX_HP];
		//HP will not be below 0
		if(_currentHP < 0)
			_currentHP = 0;
	}

	/**
	 * Swaps the current shield for a new one
	 * @param s -shield to add
	 * @return old Shield, null if no shield previously
	 */
	public Shield changeShield(Shield s){
		Shield old = _shield;
		_shield = s;
		return old;
	}

	/**
	 * Swaps the current Cuirass for a new one
	 * @param c - Cuirass to add
	 * @return old Cuirass, null if none previously
	 */
	public Cuirass changeCuirass(Cuirass c){
		Cuirass old = _cuirass;
		_cuirass = c;
		return old;
	}

	/**
	 * Swaps the current Weapon for a new one
	 * @param w - Weapon to add
	 * @return old Weapon, null if none previously
	 */
	public Weapon changeWeapon(Weapon w){
		Weapon old = _equipped;
		_equipped = w;
		return old;
	}
	
	/**
	 * Swaps the current Footgear for  new one
	 * @param f - Footgear to add
	 * @return old Footgear, null if none previously
	 */
	public Footgear changeFootgear(Footgear f){
		Footgear old = _footgear;
		_footgear = f;
		return old;
	}


	/**
	 * levelUp
	 * transitions this Character to the next level (if possible) updating stats
	 * @throws InvalidLevelException if level > levelCap
	 */

	public void levelUp() throws InvalidLevelException{
		_level++;
		//_level cannot exceed imposed levelCap
		if(_level == LEVELCAP) throw new InvalidLevelException();


		//update stats
		for(int i=0; i<NUM_STATS; i++)
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
		System.out.println(_name + " attacks " + opponent._name + "!");
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
	public int getMinAttackRange(){
		//TODO Ryan fills in
		return 0;
	}
	
	public int getMaxAttackRange() {
		return 1;
	}


	/**
	 * paintProfile
	 * @author jeshapir
	 */
	public void paintProfile(){
		//TODO fill in
	}

	/**
	 * setLeft
	 * paints the left character image
	 * @author jeshapir
	 */
	public void setLeft(){
		_currentImage = _left;
	}

	/**
	 * setRight
	 * paints the right character image
	 * @author jeshapir
	 */
	public void setRight(){
		_currentImage = _right;
	}

	/**
	 * setUp
	 * paints the 'up' character image
	 * @author jeshapir
	 */
	public void setUp(){
		_currentImage = _up;
	}

	/**
	 * setDown
	 * paints the 'down' character image
	 * @author jeshapir
	 */
	public void setDown(){
		_currentImage = _down;
	}
	
	/**
	 * accessor method for the current image
	 * @return the current image on the game map
	 */
	
	public BufferedImage getCurrentImage() {
		return _currentImage;
	}
	
	public BufferedImage getProfileImage() {
		return _profile;
	}
	
	public BufferedImage getDownImage() {
		return _down;
	}

	/**
	 * flattens a Character to a file (using serialization), for saving purposes
	 * @param filepath - the location of the file to write to
	 */
	public void serialize(String filepath){
		FileOutputStream fos = null;
		ObjectOutputStream out  = null;
		try{
			fos = new FileOutputStream(filepath);
			out = new ObjectOutputStream(fos);
			out.writeObject(this);
			out.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * unflattens a character, opposite of serialize()
	 * @param filepath -- location of serialized file
	 * @return the unflattened Character retrieved from filepath
	 */
	public static Character restore(String filepath){
		Character c = null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try{
			fis = new FileInputStream(filepath);
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

	/**
	 * Equality test for Characters
	 * @param other -- the Character to compare to this
	 * @return true if this and other are 'equal', false otherwise
	 */
	public boolean equals(Character other){
		for(int i=0; i<NUM_STATS; i++)
			if((_BASE_STATS[i] != other._BASE_STATS[i]) || (_currentStats[i] != other._currentStats[i]))
				return false;
		if(_exp != other._exp)
			return false;
		if(_currentHP != other._currentHP)
			return false;
		if(_level != other._level)
			return false;

		return true;
	}
	
	/**
	 * @return the combat controller this unit is affiliated with
	 * @author rroelke
	 */
	public CombatController getAffiliation() {
		return _affiliation;
	}
	
	/**
	 * affiliates the character with the given combat controller
	 * @author rroelke
	 */
	public void affiliate(CombatController aff) {
		_affiliation = aff;
	}

	public void printStats(){
		System.out.println("---------Character Info----------");
		System.out.println("Character " + _name);
		System.out.println("Level: " + _level);
		System.out.println("HP: " + _currentHP);
		System.out.println("Experience Points: " + _exp);
		System.out.println("-----\nStats:");
		System.out.println("Strength: " + _currentStats[STRENGTH]);
		System.out.println("Defense: " + _currentStats[DEFENSE]);
		System.out.println("Skill: " + _currentStats[SKILL]);
		System.out.println("Speed: " + _currentStats[SPEED]);
		System.out.println("Special: " + _currentStats[SPECIAL]);
		System.out.println("Luck: " + _currentStats[LUCK]);
		System.out.println("Resistance: " + _currentStats[RESISTANCE]);
		System.out.println("Max HP: " + _currentStats[MAX_HP]);
		System.out.println("------\nItems");
		if(_equipped != null)
			System.out.println("Weapon: " + _equipped._id);
		if(_footgear != null)
			System.out.println("Footgear: " + _footgear._id);
		if(_shield != null)
			System.out.println("Shield: " + _shield._id);
		if(_cuirass != null)
			System.out.println("Cuirass: " + _cuirass._id);
		if(_inventory.size() > 0)
			System.out.println("inventory:");
		for(Entry<Integer, Item> entry : _inventory.entrySet())
				System.out.println(entry.getValue()._id);
		System.out.println("-------------------");


	}

	public static void testPreSerialize(){
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Character Demo:");
			System.out.println("Creating Characters...");
			Archer katniss = new Archer(null, "","","","","", "",  "Katniss");

			//Show Characters
			katniss.printStats();

			//Level Up
			br.readLine();

			System.out.println("leveling up");
			try {
				katniss.levelUp();
			} catch (InvalidLevelException e) {
				e.printStackTrace();
			}
			br.readLine();
			katniss.printStats();

			System.out.println("Level up 99 times");
			try{
				while(true)
					katniss.levelUp();
			} catch(InvalidLevelException e){
				System.out.println("Cannot level up after level : " + katniss._level);
			}
			br.readLine();
			katniss.printStats();
			
			//Battles
			System.out.println("Battle Simulation");
			br.readLine();
			Warrior jace = new Warrior(null, "","","","","", "", "Jace");
			Mage sebastian = new Mage(null, "","","","","", "", "Sebastian");
			jace.printStats(); 
			sebastian.printStats();
			br.readLine();
			
			jace.attack(sebastian);
			br.readLine();
			System.out.println("Suppose Sebastian takes 10 damage");
			sebastian.updateHP(-10);
			br.readLine();
			sebastian.printStats();
			sebastian.attack(jace);
			System.out.println("Now Jace loses 5");
			br.readLine();
			jace.updateHP(-5);
			jace.printStats();
			br.readLine();
			
			//Items
			System.out.println("Items--Equipment");
			Footgear f = new Footgear();
			System.out.println("Footgear " + f._id + " added to Sebastian");
			br.read();
			f.exert(sebastian);
			sebastian.printStats();
			
			Shield s = new Shield();
			System.out.println("Shield " + s._id + "  added to Jace");
			br.read();
			s.exert(jace);
			jace.printStats();
			
			Cuirass c = new Cuirass();
			System.out.println("Cuirass " + c._id + " added to Jace");
			br.read();
			c.exert(jace);
			jace.printStats();
			br.readLine();
			
			Axe a = new Axe();
			System.out.println("Sebastian takes an Axe " + a._id);
			a.exert(sebastian);
			br.readLine();
			sebastian.printStats();
			br.readLine();
			
			Bow b = new Bow();
			System.out.println("Sebastian decides he prefers a Bow " + b._id);
			br.readLine();
			b.exert(sebastian);
			sebastian.printStats();
			br.readLine();
			
			Staff staff = new Staff();
			System.out.println("Jace takes a Staff " + staff._id);
			br.readLine();
			staff.exert(jace);
			jace.printStats();
			br.readLine();
			
			System.out.println("\n\nItems-- non-Equipment");
			HealthPotion hp = new HealthPotion();
			System.out.println("Sebastian's Health is pretty low, luckily he found a Health Potion " + hp._id);
			sebastian.addToInventory(hp);
			sebastian.printStats();
			br.readLine();
			hp.exert(sebastian);
			sebastian.printStats();
			br.readLine();
			
			System.out.println("Now we want to save the game...");
			br.readLine();
			jace.serialize("src/character/" + jace._name + jace._id);
			sebastian.serialize("src/character/" + sebastian._name + sebastian._id);

		} catch(IOException e){
			e.printStackTrace();
		} catch (ItemException e) {
			e.printStackTrace();
		}	
	}
	public static void testPostSerialize(){
		System.out.println("Restore:");
		Warrior jace = (Warrior)restore("src/character/Jace1");
		Mage sebastian =  (Mage) restore("src/character/Sebastian2");
		jace.printStats();
		sebastian.printStats();
		
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			br.readLine();
			System.out.println("Dialogue");
			Thief margo = new Thief(null, "","","","","", "","Margo");
			HashMap<String, Character> h = new HashMap<String,Character>();
			h.put("Sebastian", sebastian);
			h.put("Jace", jace);
			h.put("Margo", margo);

			DialogueBox d = new DialogueBox(null, "src/tests/data/testDemo",h);
			d.print();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		testPreSerialize();
		testPostSerialize();
	}
}
