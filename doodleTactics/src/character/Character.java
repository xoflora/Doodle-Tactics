package character;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;
import controller.combatController.CombatController;
import main.DoodleTactics;
import main.GameScreen;
import map.Tile;
import graphics.Rectangle;
import items.*;

public abstract class Character extends Rectangle{

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * @author czchapma
	 */

	public final static int LEVELCAP = 99;
	public final static int NUM_STATS = 8;
	protected String _name;
	private static int numCharacters = 0;
	protected final int _id;

	//stats
	//indices where stat is located in subsequent arrays

	public final static int STRENGTH = 0;
	public final static int DEFENSE = 1;
	public final static int SPECIAL = 2;
	public final static int RESISTANCE = 3;
	public final static int SPEED = 4;
	public final static int SKILL = 5;
	public final static int LUCK =  6;
	public final static int MAX_HP =  7;

	public final static int CRITICAL_MULTIPLIER = 2;

	//stat arrays (indexed by type of stat, see above)
	protected final double[] _BASE_STATS; //initial
	protected int[] _currentStats; //updated with levelUp()
	protected int[] _unitPoints; //gained by character
	protected final int[] YIELD; //given out by character

	//misc. character info
	protected int _level, _exp, _currentHP;
	protected double _tileX,_tileY;

	//items
	protected Weapon _equipped;
	protected Cuirass _cuirass;
	protected Shield _shield;
	protected Footgear _footgear;
	protected HashMap<Integer, Item> _inventory; //items not being worn
	protected int _capacity; //max number of items the character can carry

	//images
	private transient BufferedImage _profile;
	private transient BufferedImage _currentImage;
	private transient BufferedImage _left;
	private transient BufferedImage _right;
	private transient BufferedImage _up;
	private transient BufferedImage _down;
	private String _profileFile,_leftFile,_rightFile,_upFile,_downFile;

	private transient CombatController _affiliation; //player/AI etc

	private transient FloatTimer _floatTimer; // internal timer used to animate floating
	private transient GameScreen _container;
	private boolean _isAnimating;

	private int _currDirection = 1; //1 = front, 2 = back, 3 = left, 4 = right 

	private int _hoverOffset;
	
	private transient PathTimer _pathTimer;
	private transient MoveTimer _moveTimer;
	
	private transient DoodleTactics _dt;

	public static enum CharacterDirection{
		LEFT,RIGHT,UP,DOWN
	}
	
	public enum CharacterType {
		ARCHER, MAGE, THIEF, WARRIOR, GENERAL;
	}

	public Character(DoodleTactics dt, GameScreen container, String profile, String left, String right,
			String up, String down, String name,double x, double y) {

		super(container);
		
		_dt = dt;
		_container = container;
		_BASE_STATS = new double[NUM_STATS];
		_currentStats = new int[NUM_STATS];
		_unitPoints = new int[NUM_STATS]; 
		YIELD = new int[NUM_STATS];
		_id = numCharacters;
		numCharacters++;

		_name = name;
		_level = 1;
		_inventory	= new HashMap<Integer, Item>();
		_capacity = 5;

		_affiliation = null;

		_profileFile = profile;
		_leftFile = left;
		_rightFile = right;
		_upFile = up;
		_downFile = down;
		initImages(dt);
		_currentImage = _down;

		this.setSize(_down.getWidth(), _down.getHeight());
		//		int overflow = 0;
		////		if(_down.getWidth() - Tile.TILE_SIZE <= 25.0)
		//		overflow = (_down.getWidth() - Tile.TILE_SIZE) / 2;
		this.setLocation(x, y);
		//		this.setLocation(x - overflow,y - _down.getHeight() + Tile.TILE_SIZE);
		_floatTimer = new FloatTimer(container);
		_hoverOffset = 0;
		this.startHovering();

		_pathTimer = null;
		_moveTimer = null;

	}

	private class FloatTimer extends Timer {

		private FloatListener _listener;

		public FloatTimer(JPanel container) {
			super(75, null);
			_listener = new FloatListener(this, container);
			this.addActionListener(_listener);
		}

		public void reset() {
			Character.this.setLocation(Character.this.getX(), Character.this.getY() - _listener.getOffset());
		}

		public float getListenerOffset(){
			return _listener.getOffset();
		}

		private class FloatListener implements java.awt.event.ActionListener {

			private Timer _timer;
			private int _cnt;
			private JPanel _container;
			private float _offset;

			public FloatListener (Timer t, JPanel container) {
				_container = container;
				_timer = t;
				_cnt = 0;
				_offset = 0;
			}

			public float getOffset() {
				//_cnt = 0;
				return _offset;
			}

			public void actionPerformed(java.awt.event.ActionEvent e) {

				_cnt = (_cnt + 1) % 8;

				switch(_cnt) {

				case 0:
					//Character.this.setLocation(Character.this.getX(), Character.this.getY() + 1.5);
					_hoverOffset += 1.5;
					break;
				case 1:
					//Character.this.setLocation(Character.this.getX(), Character.this.getY() + 1);
					_hoverOffset += 1;
					break;
				case 2:
					//Character.this.setLocation(Character.this.getX(), Character.this.getY() + 1);
					_hoverOffset += 1;
					break;
				case 3:
					//Character.this.setLocation(Character.this.getX(), Character.this.getY() + 1);
					_hoverOffset += 1;
					break;
				case 4:
					//Character.this.setLocation(Character.this.getX(), Character.this.getY() - 1);
					_hoverOffset -= 1;
					break;
				case 5:
					//Character.this.setLocation(Character.this.getX(), Character.this.getY() - 1);
					_hoverOffset -= 1;
					break;
				case 6:
					//Character.this.setLocation(Character.this.getX(), Character.this.getY() - 1);
					_hoverOffset -= 1;
					break;
				case 7:
					//Character.this.setLocation(Character.this.getX(), Character.this.getY() - 1.5);
					_hoverOffset -= 1.5;
					break;
				}

				_container.repaint();
			}
		}
	}

	/**
	 * moves the character to the given tile provided that it is valid to move to that tile
	 * @param screen the game screen in which movement is occurring
	 * @param t the desired tile to move the character to
	 * @param follow whether or not the camera should follow the movement
	 */
	public void moveToTile(Tile src, Tile dest, boolean follow) {

		System.out.println("---MOVE TO TILE---");

		System.out.println("xDiff before: " + (dest.getX() - src.getX()));
		System.out.println("yDiff before: " + (dest.getY() - src.getY()));

		int xDiff = ((int)dest.getX() - (int) src.getX()) / Tile.TILE_SIZE;
		int yDiff = ((int) dest.getY() - (int) src.getY()) / Tile.TILE_SIZE;

		System.out.println("xDiff: " + xDiff);
		System.out.println("yDiff: " + yDiff);

		_moveTimer = new MoveTimer(_container, xDiff, yDiff, follow);

		/* determine which orientation to set the character to */
		if(xDiff > 0) {
			this.setRight();
		} else if (xDiff < 0) {
			this.setLeft();
		}

		if(yDiff > 0) {
			this.setDown();
		} else if (yDiff < 0) {
			this.setUp();
		}

		_isAnimating = true;
		_moveTimer.start();
		
	}

	private class MoveTimer extends Timer {

		private GameScreen _screen;
		private int _deltaX;
		private int _deltaY;
		private boolean _follow;

		public MoveTimer(GameScreen container, int deltaX, int deltaY, boolean follow) {
			super(50, null);
			_screen = container;
			_deltaX = deltaX;
			_deltaY = deltaY;
			_follow = follow;
			this.addActionListener(new MoveListener(this, container));
		}

		private class MoveListener implements java.awt.event.ActionListener {

			private Timer _timer;
			private int _cnt;
			private int _numSteps;
			private JPanel _container;

			public MoveListener (Timer t, JPanel container) {
				_container = container;
				_timer = t;
				_cnt = 0;
				_numSteps = 6;
			}

			public void actionPerformed(java.awt.event.ActionEvent e) {
				double dx = _deltaX*Tile.TILE_SIZE / _numSteps;
				double dy = _deltaY*Tile.TILE_SIZE / _numSteps;
				Character.this.updateLocation(dx, dy);
				_screen.pan(-dx, -dy);
			//	Character.this.setLocation((Character.this.getX() + (_deltaX*Tile.TILE_SIZE / _numSteps)),
			//			Character.this.getY() + (_deltaY*Tile.TILE_SIZE / _numSteps));

				switch(_cnt) {
				case 0:
					Character.this.setRotation(-10);
					break;
				case 1:
					Character.this.setRotation(-5);
					break;
				case 2:
					Character.this.setRotation(0);
					break;
				case 3:
					Character.this.setRotation(5);
					break;
				case 4:
					Character.this.setRotation(10);
					break;
				case 5:
					Character.this.setRotation(0);
					break;
				}

				_container.repaint();

				_cnt+=1;

				/* if we've incremented numSteps times, then we should stop */
				/* otherwise, continue incrementing */
				if (_cnt == _numSteps) {
					_timer.stop();
					Character.this._isAnimating = false;
					System.out.println("---END MOVE TO TILE---");
				}

				_container.repaint();
			}
		}
	}

	private class PathTimer extends Timer {

		private List<Tile> _path;
		private boolean _follow;

		public PathTimer(List<Tile> path, boolean follow) {
			super(400, null);
			_path = path;
			_follow = follow;
			this.addActionListener(new PathListener());
		}

		private class PathListener implements java.awt.event.ActionListener {

			private int _cnt = 0;

			public void actionPerformed(java.awt.event.ActionEvent e) {

				Character.this.moveToTile(_path.get(_cnt), _path.get(_cnt + 1), _follow);

				_cnt += 1;

				/* if we've incremented numSteps times, then we should stop */
				/* otherwise, continue incrementing */
				if (_cnt == _path.size() - 1) {
					PathTimer.this.stop();
					//	System.out.println("=========END FOLLOW PATH=========");
					_affiliation.moveComplete();
				}
			}
		}
	}

	/**
	 * animates the character along the given path of tiles
	 * @param tiles a list of tiles to have the character follow, used for combat movement
	 */

	public void followPath(List<Tile> tiles) {
		
	//	System.out.println("=========START FOLLOW PATH=========");
		//tiles.remove(0);
	//	for(Tile t : tiles) {
	//		System.out.println("tile: " + t.getX() + "," + t.getY());
	//	}
	//	System.out.println("===================================");
		if(tiles != null && tiles.size() > 1) {
			_pathTimer = new PathTimer(tiles, true);
			_pathTimer.start();
		}
		else {
			System.out.println("Crunch");
			_affiliation.moveComplete();
		}
	}

	/**
	 * stops all motion of this character
	 */
	public void stopMotion() {
		if (_pathTimer != null && _pathTimer.isRunning())
			_pathTimer.stop();
		if (_moveTimer != null && _moveTimer.isRunning())
			_moveTimer.stop();

		_pathTimer = null;
		_moveTimer = null;
	}

	/**
	 * @return whether the character is presently in motion
	 */
	public boolean isMoving() {
		return (_pathTimer != null && _pathTimer.isRunning()) ||
		(_moveTimer != null && _moveTimer.isRunning());
	}

	/**
	 * Generates a Random Character, simple for now, perhaps random statistics in the future
	 */
	public static Character generateRandomCharacter(DoodleTactics dt, GameScreen gs, double tileX, double tileY){
		Random r = new Random();
		String begin = "src/graphics/characters/";
		switch(r.nextInt(4)){
		case 0:
			return new Archer(dt,gs, begin + "doodle_knight_portrait.png",begin + "knight_left.png",begin + "knight_right.png",begin + "knight_back.png",begin + "knight_front.png","RandomEnemy",tileX,tileY);
		case 1:
			return new Warrior(dt,gs, begin + "warrior_portrait.png",begin + "warrior_left_color.png",begin + "warrior_right_color.png",begin + "warrior_back_color.png",begin + "warrior_front_color.png","RandomEnemy",tileX,tileY);
		case 2:
			return new Mage(dt,gs, begin + "mage_portrait.png",begin + "mage_left.png",begin + "mage_right.png",begin + "mage_back.png",begin + "mage_front.png","RandomEnemy",tileX,tileY);
		case 3:
			return new Thief(dt,gs, begin + "thief_portrait.png",begin + "thief_left.png",begin + "thief_right.png",begin + "thief_back.png",begin + "thief_front.png","RandomEnemy",tileX,tileY);
		default:
			System.out.println("Character Generator failed!");
			return null;
		}
	}

	/**
	 * Loads a Character during deserialization
	 */
	public void load(DoodleTactics dt){
		_profile = dt.importImage(_profileFile);
		_left = dt.importImage(_leftFile);
		_right = dt.importImage(_rightFile);
		_up = dt.importImage(_upFile);
		_down = dt.importImage(_downFile);
		_currentImage = _down;
		_container = dt.getGameScreen();

		int overflow = 0;
		if(_down.getWidth() - Tile.TILE_SIZE <= 25.0)
			overflow = (_down.getWidth() - Tile.TILE_SIZE) / 2;
		this.setLocation(getX(),getY() - overflow);


		for(Integer i : _inventory.keySet()){
			_inventory.get(i).loadItem(dt);
		}

		if(_equipped != null)
			_equipped.loadItem(dt);
		if(_cuirass != null)
			_cuirass.loadItem(dt);
		if(_shield != null)
			_shield.loadItem(dt);
		if(_footgear != null)
			_footgear.loadItem(dt);
		_cuirass.load(dt);

		new FloatTimer(dt.getGameScreen());
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

	public double[] getBaseStats(){
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

	public Footgear getFootgear(){
		return _footgear;
	}

	public boolean isAnimating() {
		return _isAnimating;
	}

	/**
	 * 
	 * @return the Direction that the character is currently facing
	 */
	public CharacterDirection getDirection(){
		if(_currentImage.equals(_left))
			return CharacterDirection.LEFT;
		else if(_currentImage.equals(_right))
			return CharacterDirection.RIGHT;
		else if(_currentImage.equals(_up))
			return CharacterDirection.UP;
		else
			return CharacterDirection.DOWN;
	}

	/**
	 * Sets the Character to the input direction
	 */
	public void setDirection(CharacterDirection c){
		switch(c){
		case LEFT:
			setLeft();
			break;
		case RIGHT:
			setRight();
			break;
		case UP:
			setUp();
			break;
		case DOWN:
			setDown();
			break;
		}
	}

	@Override
	public int getPaintPriority(){
		return (int) this.getY();
	}

	/**
	 * @return whether or not the Character has dialogue
	 */
	/*	public boolean canConverse(){
		return _dialogue == null;
	}*/

	/**
	 * @return the Dialogue Event for the Character
	 * Guaranteed to be non-null ONLY if canConverse() returns true
	 */
	//	public Dialogue converse(){
	//		return _dialogue;
	//	}

	/**
	 * Initializes Current Stats to Base Stats
	 */
	public void initStats(){
		for(int i=0; i<NUM_STATS; i++){
			_currentStats[i] = (int) (_BASE_STATS[i]*10);
			if (i== NUM_STATS-1) {
//				System.out.println(this.getName() + " max HP: " + _currentStats[MAX_HP]);
			}
		}
		_currentHP = (int) (_BASE_STATS[MAX_HP]*10);
	}

	/**
	 * Adds Item to inventory
	 * @param i - item to add
	 * @throws ItemException
	 */
	public void addToInventory(Item i) throws ItemException{
		//check if capacity has been exceeded
		if(_inventory.size() == _capacity)
			throw new ItemException("Capacity reached");
		System.out.println("The ID of the added inventory item is: " + i._id);
		_inventory.put(i._id, i);
		System.out.println(_inventory.size());
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

	public void removeWeapon() {
		_equipped = null;
	}

	public void removeFootgear() {
		_footgear = null;
	}

	public void removeCuirass() {
		_cuirass = null;
	}

	public void removeShield() {
		_shield = null;
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
			_currentStats[i] = (int) (10 * _BASE_STATS[i] + _level*_BASE_STATS[i] + _unitPoints[i]/12);
		_currentHP = _currentStats[MAX_HP];
	}
	
	public void checkLevelUp() {
		if (_exp >= 100*(Math.pow(1.2, (_level-1)))) {
			_exp = (int) (_exp-(100*(Math.pow(1.2, (_level-1)))));
			try {
				this.levelUp();
				System.out.println("Level up!");
			} catch (InvalidLevelException e) {
				// TODO Auto-generated catch block
				_dt.error("You have reached the level cap. Good job for WINNING THE GAME.");
			}
		}
	}
	
	public void addExpForAttack(Character enemy) {
		int yield = (int) Math.max(2*((_level-1)-enemy.getLevel())+((100*Math.pow(1.2, _level-1))/10), 1);
		_exp+=yield;
		this.checkLevelUp();
	}
	
	public void addExpForDefeating(Character enemy) {
		int yield = (int) Math.max(2*((_level-1)-enemy.getLevel())+((100*Math.pow(1.2, (_level-1)))/5), 1);
		_exp+=yield;
		this.checkLevelUp();
	}
	
	public int getExpNeededToLevel() {
		int neededExp = (int) (100*(Math.pow(1.2, (_level-1))));
		return neededExp;
	}

	/**
	 * @return the attack strength of this enemy in combat
	 */
	public int getFullAttackStrength() {
		return _currentStats[STRENGTH] + (_equipped == null ? 0:_equipped.getPower());
	}

	/**
	 * @return the combat defense of this character (includes this equipment)
	 */
	public int getFullDefense() {
		return _currentStats[DEFENSE] + (_shield == null ? 0:_shield.getDefense()) +
		(_cuirass == null ? 0:_cuirass.getDefense());
	}

	/**
	 * @return the attack accuracy factoring in weapon accuracy, not factoring in opponent's
	 */
	public double getFullAttackAccuracy() {
		return 2.5*_currentStats[SKILL] + (_equipped == null ? 90:_equipped.getAccuracy());
	}

	/**
	 * @param other another character
	 * @return the probability (out of 100) that this character will connect when attacking the other
	 */
	public double getHitChance(Character other) {
		return getFullAttackAccuracy() - other._currentStats[SKILL];
	}

	/**
	 * @param other another character
	 * @return the probability (out of 100) of a critical hit occurring if this character attacks the other
	 */
	public double getCriticalChance(Character other) {
		return _currentStats[LUCK] - other._currentStats[LUCK];
	}
	
	public double getFullResistance() {
		return _currentStats[RESISTANCE] + (_cuirass == null ? 0:_cuirass.getResistance());
	}


	/**
	 *  attacks an opponent character
	 *  @param opponent the character to attack
	 *  @param r a random number generator
	 *  @author rroelke
	 */
	public int[] attack(Tile attacker, Tile opponent, Random r, int range){
		int offense, defense, damage;
		boolean critical;
		
		int[] damageDone = new int[2];

		if (r.nextInt(100) > getHitChance(opponent.getOccupant())-opponent.getEvasion()) {
			damageDone[0] = -1;
			System.out.println("Attack missed!");
		}
		else {

			damage = Math.max(getFullAttackStrength() - opponent.getOccupant().getFullDefense() - opponent.getDefense() +
					r.nextInt(Math.max((getFullAttackStrength() - opponent.getOccupant().getFullDefense() - opponent.getDefense())/4, 1)), 0);

			critical = (r.nextInt(100) <= (_currentStats[LUCK] - opponent.getOccupant()._currentStats[LUCK]));
			if (critical) {
				damage *= CRITICAL_MULTIPLIER;
				System.out.print("Critical hit! ");
			}

			opponent.getOccupant().updateHP(-damage);

			System.out.println(_name + " attacks " + opponent.getOccupant()._name + "!  " + opponent.getOccupant()._name +
					" takes " + damage + " damage!");

			damageDone[0] = damage;
			
			if (opponent.getOccupant()._currentHP <= 0) {
				this.addExpForDefeating(opponent.getOccupant());
				System.out.println(opponent.getOccupant().getName() + " defeated.");
				opponent.getOccupant().setDefeated();
				return null;
			}
		}
		System.out.println("max opponent range: " + opponent.getOccupant().getMaxAttackRange());
		System.out.println("min opponent range: " + opponent.getOccupant().getMinAttackRange());
		System.out.println("range: " + range);

		if (opponent.getOccupant().getMaxAttackRange() >= range && opponent.getOccupant().getMinAttackRange() <= range) {
			if (r.nextInt(100) > opponent.getOccupant().getFullAttackAccuracy() - _currentStats[SKILL] - attacker.getEvasion()) {
				damageDone[1] = -1;
				System.out.println("Attack missed!");
			}
			else {
				offense = opponent.getOccupant()._currentStats[STRENGTH] + (opponent.getOccupant()._equipped == null ? 0:opponent.getOccupant()._equipped.getPower());
				defense = _currentStats[DEFENSE] + (_cuirass == null ? 0:_cuirass.getDefense())
				+ (_shield == null ? 0:_shield.getDefense())+attacker.getDefense();
				critical = (r.nextInt(100) <= (opponent.getOccupant()._currentStats[LUCK] - _currentStats[LUCK]));
	
				damage = Math.max(offense - defense + r.nextInt(Math.max((offense - defense)/4, 1)), 0);
	
				if (critical) {
					damage *= CRITICAL_MULTIPLIER;
					System.out.println("Critical hit!");
				}
	
				updateHP(-damage);
	
				System.out.println(opponent.getOccupant()._name + " attacks " + _name + "!  " + _name +
						" takes " + damage + " damage!");
				
				damageDone[1] = damage;
	
				if (_currentHP <= 0) {
					System.out.println(getName() + " defeated.");
					setDefeated();
					return null;
				}
			}
		}
		else {
			System.out.println("Opponent couldn't attack you because of your range");
		}
		return damageDone;
	}

	/**
	 * computes the movement range of the Character
	 * @return
	 */
	public int getMovementRange(){
		if (_currentStats[SPEED] <= 6)
			return 3;
		else if (_currentStats[SPEED] <= 13)
			return 4;
		else if (_currentStats[SPEED] <= 21)
			return 5;
		else if (_currentStats[SPEED] <= 30)
			return 6;
		else if (_currentStats[SPEED] <= 40)
			return 7;
		else if (_currentStats[SPEED] <= 51)
			return 8;
		else if(_currentStats[SPEED] <= 62)
			return 9;
		else
			return 10;
	}

	/** 
	 * computes the attack range of the Character
	 */
	public int getMinAttackRange() {
		if (_equipped != null)
			return _equipped.getMinAttackRange();
		else
			return 1;
	}

	public int getMaxAttackRange() {
		if (_equipped != null)
			return _equipped.getMaxAttackRange();
		else
			return 1;
	}

	/**
	 * tells the character to start hovering by starting its internal timer
	 */
	public void startHovering() {
		_floatTimer.start();
	}

	/**
	 * tells the character to stop hovering by stopping its internal timer
	 */
	public void stopHovering() {
		_floatTimer.stop();
		_floatTimer.reset();
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
		_currDirection = 3;
		_currentImage = _left;
	}

	/**
	 * setRight
	 * paints the right character image
	 * @author jeshapir
	 */
	public void setRight(){
		_currDirection = 4;
		_currentImage = _right;
	}

	/**
	 * setUp
	 * paints the 'up' character image
	 * @author jeshapir
	 */
	public void setUp(){
		_currDirection = 2;
		_currentImage = _up;
	}

	/**
	 * setDown
	 * paints the 'down' character image
	 * @author jeshapir
	 */
	public void setDown(){
		_currDirection = 1;
		_currentImage = _down;
	}

	/**
	 * accessor method for the current image
	 * @return the current image on the game map
	 */

	public BufferedImage getImage() {
		return _currentImage;
	}

	public BufferedImage getProfileImage() {
		return _profile;
	}

	public BufferedImage getDownImage() {
		return _down;
	}

	public BufferedImage getRightImage() {
		return _right;
	}

	public BufferedImage getLeftImage() {
		return _left;
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
			in.readObject();
			in.close();
		} catch(IOException e){
			e.printStackTrace();
		} catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		return c;
	}

	public void initImages(DoodleTactics dt){
		_profile = dt.importImage(_profileFile);
		_left = dt.importImage(_leftFile);
		_right = dt.importImage(_rightFile);
		_up = dt.importImage(_upFile);
		_down = dt.importImage(_downFile);
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

	@Override 
	public void paint(java.awt.Graphics2D brush, BufferedImage img) {
		brush.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		double oldX = this.getX();
		double oldY = this.getY();
		int overflowX = (this.getDownImage().getWidth() - Tile.TILE_SIZE) / 2;
		int overflowY = (this.getDownImage().getHeight() - Tile.TILE_SIZE) / 2;
		this.setLocation(this.getX() - overflowX,this.getY() - overflowY + _hoverOffset);
		super.paint(brush,img);
		this.setLocation(oldX, oldY);
		brush.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
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

	public HashMap<Integer, Item> getInventory() {
		return _inventory;
	}

	public int getStrength() {
		return _currentStats[STRENGTH];
	}

	public int getDefense() {
		return _currentStats[DEFENSE];
	}

	public int getSpecial() {
		return _currentStats[SPECIAL];
	}

	public int getResistance() {
		return _currentStats[RESISTANCE];
	}

	public int getSpeed() {
		return _currentStats[SPEED];
	}

	public int getSkill() {
		return _currentStats[SKILL];
	}

	public int getLuck() {
		return _currentStats[LUCK];
	}

	public int getMAX_HP() {
		return _currentStats[MAX_HP];
	}
	
	public void addDefense(int defense) {
		_currentStats[DEFENSE] += defense;
	}
	
	public void addStrength(int str) {
		_currentStats[STRENGTH] += str;
	}
	
	public void addSpecial(int special) {
		_currentStats[SPECIAL] += special;
	}
	
	public void addResistance(int resistance) {
		_currentStats[RESISTANCE] += resistance;
	}
	
	public void addSpeed(int speed) {
		_currentStats[SPEED] += speed;
	}
	
	public void addSkill(int skill) {
		_currentStats[SKILL] += skill;
	}
	
	public void addLuck(int luck) {
		_currentStats[LUCK] += luck;
	}
	
	public void addMax_HP(int hp) {
		_currentStats[MAX_HP] += hp;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setImages(BufferedImage prof, BufferedImage left, BufferedImage right, BufferedImage up, BufferedImage down) {
		_profile = prof;
		_left = left;
		_right = right;
		_up = up;
		_down = down;
		switch(_currDirection) {
		case 1:
			_currentImage = _down;
			break;
		case 2: 
			_currentImage = _up;
			break;
		case 3:
			_currentImage = _left;
			break;
		case 4:
			_currentImage = _right;
			break;
		}
	}

	public void setBaseStats(double d, double e, double f, double g, double h, double i, double j, double k) {
		_BASE_STATS[0] = d;
		_BASE_STATS[1] = e;
		_BASE_STATS[2] = f;
		_BASE_STATS[3] =g;
		_BASE_STATS[4] = h;
		_BASE_STATS[5] = i;
		_BASE_STATS[6] = j;
		_BASE_STATS[7] = k;
	}
	
	public int[] getCurrStats() {
		return _currentStats;
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
		//		if(_inventory.size() > 0)
		//			System.out.println("inventory:");
		//		for(Entry<Integer, Item> entry : _inventory.entrySet())
		//				System.out.println(entry.getValue()._id);
		System.out.println("-------------------");

	}

	public int getCapacity() {
		return _capacity;
	}

	@Override
	public void setLocation(double x, double y){
		super.setLocation(x, y);
		this.setPaintPriority((int) y + _down.getHeight());
	}


	@Override
	public double getY(){
		return super.getY();
	}

	public void setAffiliation(CombatController combatControl) {
		_affiliation = combatControl;
	}

	public void setDefeated() {
		_affiliation.removeUnit(this);
	}
	
//	public abstract boolean canEquip(Weapon weapon);
	
	public abstract CharacterType getCharacterType();

	/*	public static void testPreSerialize(){
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

			Dialogue d = new Dialogue(null, "src/tests/data/testDemo",h);
			d.print();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidEventException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	*/

	/*	public static void main(String[] args){
		testPreSerialize();
		testPostSerialize();
	}	*/
	
	/**
	 * @return whether or not this character owns any equipment
	 */
	public boolean ownsEquipment() {
		if (_equipped != null || _cuirass != null || _shield != null || _footgear != null)
			return true;
		
		for (Integer i : _inventory.keySet())
			if (_inventory.get(i).isEquip())
				return true;
		return false;
	}
	
	/**
	 * equips a piece of equipment to this character
	 * @return whether or not the equip was successful
	 */
	public boolean equip(Equipment e) {
		try {
			if (e.isWeapon()) {
				Weapon _selectedWeapon = (Weapon) e;
				Weapon oldWeapon = changeWeapon(_selectedWeapon);
				removeFromInventory(e);
				if (oldWeapon != null) {
					addToInventory(oldWeapon);
				}
				return true;
			}
			else if (e.isCuirass()) {
				Cuirass _selectedCuirass = (Cuirass) e;
				Cuirass oldCuirass = changeCuirass(_selectedCuirass);
				removeFromInventory(_selectedCuirass);
				if (oldCuirass != null) {
					addToInventory(oldCuirass);
				}
				return true;
			}
			else if (e.isShield()) {
				Shield _selectedShield = (Shield) e;
				Shield oldShield = changeShield(_selectedShield);
				removeFromInventory(_selectedShield);
				if (oldShield != null) {
					addToInventory(oldShield);
				}
				return true;
			}
			else if (e.isFootgear()) {
				Footgear _selectedFootgear = (Footgear) e;
				Footgear oldFootgear = changeFootgear(_selectedFootgear);
				removeFromInventory(_selectedFootgear);
				if (oldFootgear != null) {
					addToInventory(oldFootgear);
				}
				return true;
			}
			return false;
		} catch(ItemException f) {
			return false;
		}
	}
}
