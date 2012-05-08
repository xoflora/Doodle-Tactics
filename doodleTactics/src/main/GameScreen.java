package main;
import event.InvalidEventException;
import graphics.MenuItem;
import graphics.Rectangle;
import graphics.Terrain;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Timer;

import controller.GameScreenController;
import controller.OverworldController;
import controller.combatController.CombatController;
import controller.combatController.CombatOrchestrator;
import controller.combatController.CombatWindow;
import controller.combatController.RandomBattleOrchestrator;
import controller.combatController.AIController.RandomBattleAI;

import character.Archer;
import character.Character;
import character.Mage;
import character.MainCharacter;
import character.Warrior;

import map.InvalidMapException;
import map.Map;
import map.Tile;
import java.util.*;

/* currmap / prevmap vars
 * this is an idea we had about the game screen needing know what starting location
 * to set for the game screen when we enter from different locations
 */

public class GameScreen extends Screen<GameScreenController> {
	
	private static final int WINDOW_WIDTH = DoodleTactics.TILE_COLS*Tile.TILE_SIZE;
	private static final int WINDOW_HEIGHT = DoodleTactics.TILE_ROWS*Tile.TILE_SIZE;

	private static final int STAT_MENU_PRIORITY = 0;
	private static final int SETUP_WINDOW_PRIORITY = -10;
	private static final int DIALOGUE_PRIORITY = 10;

	private static final int NUM_TILES_X = 21;
	private static final int NUM_TILES_Y = 17;

	private static final int MAP_CACHE_SIZE = 5;

	private static int MAP_WIDTH, MAP_HEIGHT;
	private MainCharacter _currentCharacter;
	private Map _currMap;
	private int _xRef;
	private int _yRef;
	private boolean _isAnimating;
	//	private GameMenuController _gameMenuController;
	private PriorityQueue<Rectangle> _characterTerrainQueue; // the list of characters / images to render on the screen
	private PriorityQueue<MenuItem> _menuQueue;
	private List<Terrain> _terrainToPaint;
	private HashMap<String, Map> _mapCache; // a hash map representing the cache of all of the maps in the game, maps file paths to maps
	
	private int _xWindowOffset;
	private int _yWindowOffset;
	
	private CombatWindow _popUpCombat;

	public GameScreen(DoodleTactics dt) {
		super(dt);
		this.setBackground(java.awt.Color.BLACK);
		MAP_WIDTH = 20;
		MAP_HEIGHT = 20;

		parseParty("src/tests/data/PartyDemo");

		//	_gameMenuController = _dt.getGameMenuScreen().getController();
		_characterTerrainQueue = new PriorityQueue<Rectangle>(5, new Rectangle.RectangleComparator());
		_menuQueue = new PriorityQueue<MenuItem>(5, new Rectangle.RectangleComparator());

		//select a tile to go at the top left of the screen
		_isAnimating = false;
		_mapCache = new HashMap<String, Map>();
		
		_xWindowOffset = 0;
		_yWindowOffset = 0;
		
		_popUpCombat = new CombatWindow(this, _dt.importImage("src/graphics/menu/combat_window.png"), _dt.importImage("src/graphics/menu/combat_window.png"), dt, 100);

		this.repaint();
	}

	/**
	 * Parses the character party from the given file
	 */
	public void parseParty(String filePath){
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
			String[] split = br.readLine().split(",");
			if(split.length != 7){
				System.out.println("Invalid Party File");
			} else{
				//Parse Main Character
				MainCharacter main =  new MainCharacter(_dt,this,split[2],split[3],split[4],split[5],split[6],split[1],5,5);
				int overflow = (main.getImage().getWidth() - Tile.TILE_SIZE) / 2;
				double x = 10*Tile.TILE_SIZE-overflow;
				double y=  8*Tile.TILE_SIZE - main.getImage().getHeight() + Tile.TILE_SIZE;
				main.setLocation(x, y);
				_currentCharacter =  main;
				_dt.addCharacterToParty(_currentCharacter);
				//parse party
				
				String line = br.readLine();
				while(line != null){
					split =  line.split(",");
					if(split.length != 7){
						System.out.println("Failed to parse party, length " +split.length);
						return;
					}
					else{
						if(split[0].equals("Archer"))
							_dt.addCharacterToParty((new Archer(_dt,this,split[2],split[3],split[4],split[5],split[6],split[1],0,0)));
						else if(split[0].equals("Warrior"))
							_dt.addCharacterToParty((new Warrior(_dt,this,split[2],split[3],split[4],split[5],split[6],split[1],0,0)));
						else if(split[0].equals("Mage"))
							_dt.addCharacterToParty(new Mage(_dt,this,split[2],split[3],split[4],split[5],split[6],split[1],0,0));
						else if(split[0].equals("Thief"))
							_dt.addCharacterToParty(new Archer(_dt,this,split[2],split[3],split[4],split[5],split[6],split[1],0,0));
						else
							System.out.println("Invalid Character Type");
					}
						
					line = br.readLine();
				}
			}
		} catch (FileNotFoundException e) {
			_dt.error("Invalid party file path.");
		} catch (IOException e) {
			_dt.error("Invalid party file.");
		}
	}

	/**
	 * sets the current map of the game screen to the map specified by the path.
	 * if the path was already in the cache, it loads the map from there, otherwise
	 * parses the map from the file at that path  
	 * @param mapPath is the path to the map to set
	 * @param mainCharacterX the x-position IN THE MAP, NOT ON SCREEN of the main character
	 * @param mainCharacterY the y-position IN THE MAP, NOT ON SCREEN of the main character
	 */
	public void setMap(String mapPath, int mainCharacterX, int mainCharacterY) {
		
		Map map = null;

		try {
			// if the map is already in the cache, just retrieve it from there
			if(_mapCache.get(mapPath) != null) {
				map = _mapCache.get(mapPath);
			} else {
				// otherwise, read the map from that file and put it in the cache
				map = Map.map(_dt,this, mapPath);
				_mapCache.put(mapPath, map);
			}

			//Store XRef and YRef
			Map prevMap = _currMap;
			if(prevMap != null){
				//prevMap.setPrevXRef(_xRef);
				//prevMap.setPrevYRef(_yRef);
				//Remove references to previous randomBattle
				
				
				
				prevMap.setPrevXWindowOffset(_xWindowOffset);
				prevMap.setPrevYWindowOffset(_yWindowOffset);
			}
			
			


			_currMap = map;
			//_xRef = _currMap.getPrevXRef();
			//_yRef = _currMap.getPrevYRef();
			
			_xWindowOffset = _currMap.getPrevXWindowOffset();
			_yWindowOffset = _currMap.getPrevYWindowOffset();
			
			
			_currMap.clearRandomBattleMaps();
			_currMap.assignRandomEnemies();
			
			Character prevCharacter = _currentCharacter;
			_currentCharacter = _currMap.getMainCharacter();
			_terrainToPaint = _currMap.getTerrain();
			//set + reset xref and yref
	//		System.out.println("XREF: " + _xRef + " YREF: " + _yRef);
			if(prevCharacter != null){
				_currentCharacter.setDirection(prevCharacter.getDirection());
				
			}
			// set all of the locations of the tiles relative to xref and yref
			for (int i = 0; i < map.getWidth(); i++) {
				for (int j = 0; j < map.getHeight(); j++) {
				//	map.getTile(i, j).setLocation((i - _xRef)*Tile.TILE_SIZE, (j - _yRef)*Tile.TILE_SIZE);
					map.getTile(i, j).setLocation(i*Tile.TILE_SIZE - _xWindowOffset, j * Tile.TILE_SIZE - _yWindowOffset);
					map.getTile(i, j).setVisible(true);
				}
			}
			
			setVisible(true);
			
			Tile charTile = map.getTile(mainCharacterX, mainCharacterY);
			charTile.setOccupant(getMainChar());
			panToMapTile(mainCharacterX, mainCharacterY);
			getMainChar().setLocation(charTile.getX(), charTile.getY());

		} catch (InvalidMapException e) {
			e.printMessage();
			System.exit(0);
		}
		
		//Autosave!
		 Calendar cal = Calendar.getInstance();
		this.saveGame("Autosave at " + cal.getTime());

	}

	@Override
	protected GameScreenController defaultController() {
		return new OverworldController(_dt, this);
	}

	/**
	 * Timer used for animating the map either for panning or moving in the overworld
	 * @author jeshapir
	 *
	 */
	private class MapMoveTimer extends Timer {

		private int _deltaX, _deltaY;

	/*	public MapMoveTimer(int deltaX, int deltaY, boolean isPan) {
			super(50, null);
			this.addActionListener(new MyMoveListener(this, isPan));
			_deltaX = deltaX;
			_deltaY = deltaY;
		}	*/
		
		public MapMoveTimer(Tile src, Tile dest) {
			super(50, null);
			addActionListener(new MyMoveListener(this, false, dest));
			if (src.x() == dest.x()) {
				_deltaX = 0;
				if (src.y() < dest.y())
					_deltaY = 1;
				else
					_deltaY = -1;
			}
			else {
				_deltaY = 0;
				if (src.x() < dest.x())
					_deltaX = 1;
				else
					_deltaX = -1;
			}
		}

		private class MyMoveListener implements java.awt.event.ActionListener {

			private Timer _timer;
			private int _cnt = 0;
			private final int _numSteps = 6;
			private boolean _isPan;
			
			Tile _dest;

			public MyMoveListener (Timer t, boolean isPan, Tile dest) {
				_timer = t;
				_isPan = isPan;
				_dest = dest;
			}

			public void actionPerformed(java.awt.event.ActionEvent e) {

			/*	for(int i = 0; i < MAP_WIDTH; i++) {
					for(int j = 0; j < MAP_HEIGHT; j++) {
						Tile t = _currMap.getTile(i, j);
						t.setLocation((t.getX() + (-_deltaX*Tile.TILE_SIZE / _numSteps)), t.getY() + (-_deltaY*Tile.TILE_SIZE / _numSteps));
						repaint();
					}
				}	*/
				
				pan(-_deltaX*Tile.TILE_SIZE / _numSteps, -_deltaY*Tile.TILE_SIZE / _numSteps);
				_currentCharacter.updateLocation(_deltaX*Tile.TILE_SIZE / _numSteps, _deltaY*Tile.TILE_SIZE / _numSteps);
				repaint();

			/*	for(Rectangle r: _terrainToPaint){
					r.setLocation((r.getX() + (-_deltaX*Tile.TILE_SIZE / _numSteps)), r.getY() + (-_deltaY*Tile.TILE_SIZE / _numSteps));
					repaint();
				}	*/

			//	List <Character> charsToPaint = getController().getCharactersToDisplay();

				/* if the camera is panning, then all of the characters including
				 * the character in control should move */
				//				if(_isPan) {
				//					_currentCharacter.setLocation((_currentCharacter.getX() + (-_deltaX*Tile.TILE_SIZE / _numSteps)), _currentCharacter.getY() + (-_deltaY*Tile.TILE_SIZE / _numSteps));
				//				}

		/*		for(Character c : charsToPaint) {
					c.setLocation((c.getX() + (-_deltaX*Tile.TILE_SIZE / _numSteps)), c.getY() + (-_deltaY*Tile.TILE_SIZE / _numSteps));
					repaint();
				}		*/

				/* if the camera is not panning, the main character is walking so do rotations
				 * for animation appropriately */
				if(!_isPan) {
					switch(_cnt) {
					case 0:
						_currentCharacter.setRotation(-10);
						break;
					case 1:
						_currentCharacter.setRotation(-5);
						break;
					case 2:
						_currentCharacter.setRotation(0);
						break;
					case 3:
						_currentCharacter.setRotation(5);
						break;
					case 4:
						_currentCharacter.setRotation(10);
						break;
					case 5:
						_currentCharacter.setRotation(0);
						break;
					}
				}

				_cnt+=1;

				/* if we've incremented numSteps times, then we should stop */
				/* otherwise, continue incrementing */
				if (_cnt == _numSteps) {
					_isAnimating = false;
					_timer.stop();
					
					if (_dest.hasEnterEvent())
						pushControl(_dest.getEvent());
					else if(_currMap.generatesRandomBattle(_dest)){
						_currMap.startBattle(_dest);
					}
				}
			}
		}
	}

	public MainCharacter getMainChar() {
		return _currentCharacter;
	}

	/**
	 * sets the given character as the focus of the camera
	 * @param c the character to center the camera around
	 */
	public void centerCamera(Character c) {
		int charX = (int) c.getX() / Tile.TILE_SIZE;
		int charY = (int) c.getY() / Tile.TILE_SIZE;

		_xRef = charX - 10;
		_yRef = charY - 8;
	}

	/* accessor method returning whether or not the GameScreen is currently animating, used by Timer */
	public boolean isAnimating() {
		return _isAnimating;
	}
	
	/**
	 * old code; translates a character by the given amount of tiles
	 * @param x the number of tiles in the x-direction to move the main character
	 * @param y the number of tiles in the y-direction to move the main character
	 */
	public void mapUpdate(int x, int y) {

	//	 if in the bounds of the map, specifically in relation to the main character,
	//	 update the screen reference points and animate the map

	//	if((_xRef + x + 11) <= MAP_WIDTH && (_xRef + x + 11) > 0 && (_yRef + y + 9) <= MAP_HEIGHT && (_yRef + y + 9) > 0) {

			_isAnimating = true;

//			MapMoveTimer timer = new MapMoveTimer(x,y, false);
//			timer.start();

		//	_xRef += x;
		//	_yRef += y;
	//	}
	}
	
	/**
	 * moves the screen's main character from source to destination.  Assumes that the movement is valid
	 * @param src the starting tile
	 * @param dest the ending tile
	 */
	public void moveMainCharacter(Tile src, Tile dest) {
		_isAnimating = true;
		
		src.setOccupant(null);
		dest.setOccupant(_currentCharacter);
		
		
		new MapMoveTimer(src, dest).start();
	}

	synchronized public void pan(double x, double y) {
		
	//	System.out.println(x + " " + y);

	/*	if((_xRef + x + 11) <= MAP_WIDTH && (_xRef + x + 11) > 0 && (_yRef + y + 9) <= MAP_HEIGHT && (_yRef + y + 9) > 0) {

			_isAnimating = true;

			MapMoveTimer timer = new MapMoveTimer(x,y, true);
			timer.start();

			_xRef += x;
			_yRef += y;
		}	*/
		
		_xWindowOffset -= x;
		_yWindowOffset -= y;
		
	//	_currentCharacter.updateLocation(x, y);
		//update character locations
		synchronized (getController()) {
			for(Character c : getController().getCharactersToDisplay())
				c.updateLocation(x, y);
		}
		
		//update map locations
		for(int i = 0; i < MAP_WIDTH; i++)
			for(int j = 0; j < MAP_HEIGHT; j++)
				_currMap.getTile(i, j).updateLocation(x, y);
		

		
		//update terrain location
		for (Terrain t : _terrainToPaint)
			t.updateLocation(x, y);
		
	//	for (MenuItem m : _menuQueue)
	//		m.updateLocation(x, y);
	}
	
	public void panToCoordinate(double x, double y) {
		System.out.println("Coordinate: " + x + " " + y);
		if (getWidth() == 0 || getHeight() == 0)
			pan(WINDOW_WIDTH/2 - x, WINDOW_HEIGHT/2 - y);
		else
			pan(getWidth()/2 - x, getHeight()/2 - y);
	//	pan(getWidth()/2, getHeight()/2);
		
		System.out.println(getWidth()/2 + " " +  getHeight()/2);
	}
	
	public void panToMapTile(int x, int y) {
		Tile t = _currMap.getTile(x, y);
		panToCoordinate(t.getX(), t.getY());
		
	/*	System.out.println("TIle: " + t.getX() + " " + t.getY());
		System.out.println("Offsets: " + _xWindowOffset + " " + _yWindowOffset);
		
		//set t.getX() to be in the center of the map; t.getX() = (_xWindowOffset + width())/2
		double deltaX = (getWidth() - 2*t.getX()) - _xWindowOffset;
		double deltaY = (getHeight() - 2*t.getY()) - _yWindowOffset;
		pan(-(t.getX() - _xWindowOffset)/2, -(t.getY() - _yWindowOffset)/2);	*/
	}

	public Map getMap() {
		return _currMap;
	}

	/**
	 * @param d the window x-coordinate
	 * @param e the window y-coordinate
	 * @return the tile corresponding to the given (x,y) coordinate in the window
	 * @author rroelke
	 */
	public Tile getTile(double x, double y) {
		return _currMap.getTile(getMapX(x), getMapY(y));
	}

	/**
	 * @param x
	 * @return the x-index of the tile in the map given the x-coordinate in the window
	 * @author rroelke
	 */
	public int getMapX(double x) {
		//	System.out.print("xRef: " + _xRef);
	//	return (x / Tile.TILE_SIZE) + _xRef;
		return (int)(x + _xWindowOffset) / Tile.TILE_SIZE;
	}

	/**
	 * @param y
	 * @return the y-index of the tile in the map given the y-coordinate in the window
	 * @author rroelke
	 */
	public int getMapY(double y) {
	//	return (y / Tile.TILE_SIZE) + _yRef;
	//	System.out.println(y + " " + _yWindowOffset);
		return (int)(y + _yWindowOffset) / Tile.TILE_SIZE;
	}

	/**
	 * 
	 * @return the PriorityQueue storing the Characters to paint
	 */
	public PriorityQueue<Rectangle> getCharacterQueue(){
		return _characterTerrainQueue;
	}

	/*	/**
	 * 
	 * @return The PriorityQueue storing the Menu items to paint
	 */
	/*	public PriorityQueue<MenuItem> getMenuQueue(){
		return _menuQueue;
	}	*/

	/**
	 * adds a menu item for drawing
	 * @param m the menu item to add
	 */
	public void addMenuItem(MenuItem m) {
		synchronized(_menuQueue) {
			_menuQueue.add(m);
		}
	}
	
	public CombatWindow getPopUpCombat() {
		return _popUpCombat;
	}

	/**
	 * removes a menu item
	 * @param m the item to remove
	 * @return whether or not removing was successful
	 */
	public boolean removeMenuItem(MenuItem m) {
		boolean b;
		synchronized(_menuQueue) {
			b = _menuQueue.remove(m);
			m.setVisible(false);
		}
		return b;
	}

	synchronized public void paintComponent(java.awt.Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;

		/*	System.out.println("-------PAINT--------");
		System.out.println("xMin: " + (_xRef - 1));
		System.out.println("xMax: " + (_xRef + 22));
		System.out.println("yMin: " + (_yRef - 1));
		System.out.println("yMax: " + (_yRef + 18));
		System.out.println("--------------------");		*/

		super.paintComponent(g);

		if(_currMap != null) {

			// paint all of the tiles first
			for(int i = 0; i < MAP_WIDTH; i++) {
				for(int j = 0; j < MAP_HEIGHT; j++) {
					// check that the given tile is within the bounds before painting it 
					//if((i < _xRef + 22 && i >= _xRef - 1) && (j < (_yRef + 18) && j >= (_yRef - 1))) {
		//			Tile t = _currMap.getTile(i, j);
		//			t.updateLocation(_xWindowOffset, _yWindowOffset);
					_currMap.getTile(i,j).paint(g, _currMap.getTile(i,j).getImage());
					//}
				}
			}

			// add all Characters to PriorityQueue
			List<Character> charsToPaint = this.getController().getCharactersToDisplay();

			//	System.out.println("====print characters====");
			for(Character c : charsToPaint) {
				//		System.out.println("name: " + c.getName());
				_characterTerrainQueue.add(c);
				//int overflow = (c.getImage().getWidth() - Tile.TILE_SIZE) / 2;
				//c.setLocation(10*Tile.TILE_SIZE-overflow, 8*Tile.TILE_SIZE);
			}

			// add all Terrain to PriorityQueue
			for(Terrain t : _terrainToPaint){
				//		System.out.println("Priority : " + (t.getPaintPriority()));
				//		System.out.println("Adding Terrain");
				_characterTerrainQueue.add(t);
			}

			// add the main character to the queue
		//	_characterTerrainQueue.add(_currentCharacter);

			//	System.out.println("There are " + _characterTerrainQueue.size() + " things to paint");

			// paint all characters and terrains
			while(!_characterTerrainQueue.isEmpty()) {
				Rectangle toPaint = _characterTerrainQueue.poll();
				//		System.out.println("Painted: " + toPaint.getPaintPriority());
				toPaint.setVisible(true);
				toPaint.paint(g, toPaint.getImage());				
			}

			//print all the menu items
			List<MenuItem> items = new LinkedList<MenuItem>();
			synchronized (_menuQueue) {
				while (!_menuQueue.isEmpty()) {
					items.add(_menuQueue.poll());
				}

				for (MenuItem m : items) {
					m.paint(g, m.getImage());
					_menuQueue.add(m);
				}
			}
		}

		//		if (_currentCharacter != null) {
		//			int overflow = (_currentCharacter.getImage().getWidth() - Tile.TILE_SIZE) / 2;
		//			_currentCharacter.setLocation(10*Tile.TILE_SIZE-overflow, 8*Tile.TILE_SIZE);
		//			_currentCharacter.paint((Graphics2D) g,_currentCharacter.getImage());
		//		}

		//System.out.println("--------------------");

		//		m.setLocation(_currentCharacter.getX(),_currentCharacter.getY());
		//		m.setVisible(true);
		//		m.setDown();
		//		m.setFillColor(java.awt.Color.BLACK);
		//		m.setSize(65, 50);
		//		m.paint((Graphics2D) g,m.getImage());
		//		_currentCharacter = m;
		
	}

	public void switchToGameMenu() {
		_dt.changeScreens(_dt.getGameMenuScreen());
	}
	
	public void switchToClassChooserMenu() {
		_dt.changeScreens(_dt.getClassChoserScreen());
	}

	/**
	 * transitions the game to a new combat
	 * @author rroelke
	 */
	public void enterCombat(CombatOrchestrator orch) {
		pushControl(orch);
	}

	/**
	 * transitions the game to a new combat
	 * used principally for random battles
	 * @param enemies
	 * @author rroelke
	 */
	public void enterCombat(HashMap<Character, Tile> enemies) {
		RandomBattleAI enemy = new RandomBattleAI(_dt, enemies);
		List<CombatController> e = new ArrayList<CombatController>();
		e.add(enemy);
		enterCombat(new RandomBattleOrchestrator(_dt, e, null, null, RandomBattleAI.RANDOM_BATTLE_NUM_UNITS));
	}

	/**
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 * @param num the number of requested setup tiles
	 * @return a list of valid setup tiles given a map position
	 * @author rroelke
	 */
	public List<Tile> getValidSetupTiles(int num) {
		return _currMap.getValidSetupTiles(getTile(_currentCharacter.getX(), _currentCharacter.getY()), num);
	}

	/**
	 * Accessor method used by Overworld Controller to deal with tile permissions
	 * @return the XRef
	 */
	public int getXRef(){
		return this._xRef;
	}

	/**
	 * Accessor method used by Overworld Controller to deal with tile permissions
	 * @return the YRef
	 */
	public int getYRef(){
		return this._yRef;
	}

	/**
	 * @param point the location of the screen to check
	 * @return the menu element of highest paint priority the screen that contains the given point;
	 * 	 null if none exists
	 */
	public MenuItem checkContains(java.awt.Point point) {

		MenuItem toReturn = null;
		synchronized (_menuQueue) {
			for (MenuItem r : _menuQueue) {
				r.setDefault();
				if (r.contains(point)) {
					r.setHovered();
					if (toReturn == null || r.getPaintPriority() > toReturn.getPaintPriority())
						toReturn = r;
				}
			}
		}

		repaint();
		return toReturn;
	}

	/**
	 * adds a character to the gamescreen for display
	 */
	public void addCharacter(Character c) {
		_currMap.getCharactersToDisplay().add(c);
	}

	/**
	 * removes a character from the gamescreen for display
	 */
	public void removeCharacter(Character c) {
		_currMap.getCharactersToDisplay().remove(c);
	}


	public void saveGame(String filename){
		String filepath =  "src/tests/saves/" + filename;
		System.out.println("Saving game!");
		FileOutputStream fos;
		ObjectOutputStream out;

		//Store XRef and YRef
		System.out.println("Prev X Win Offset: " + _xWindowOffset);
		System.out.println("Prev Y Win Offset: " + _yWindowOffset);
		_currMap.setPrevXWindowOffset(_xWindowOffset);
		_currMap.setPrevYWindowOffset(_yWindowOffset);
		System.out.println("MAIN CHAR X: " + _currMap.getMainCharacter().getX() +
				" Y: " + _currMap.getMainCharacter().getY());
	//	System.out.println("Other CHAR X: " + _currMap.getCharactersToDisplay().get(1).getX() +
	//			" Y: " + _currMap.getCharactersToDisplay().get(1).getY());
		
		
		int overflowX = (_currMap.getMainCharacter().getDownImage().getWidth() - Tile.TILE_SIZE) / 2;
		int overflowY = (_currMap.getMainCharacter().getDownImage().getHeight() - Tile.TILE_SIZE) / 2;
		//_currMap.getMainCharacter().setLocation(_currMap.getMainCharacter().getX() - overflowX,_currMap.getMainCharacter().getY() - overflowY);

		
		_dt.addSavedGame(filename, filepath);
		writeFilepathsFile();
		try {
			fos = new FileOutputStream(filepath);
			out = new ObjectOutputStream(fos);
			out.writeObject(_mapCache);
			out.writeObject(_currMap);
			out.writeObject(_dt.getCharacterMap());
			out.writeObject(_dt.getParty());
			out.writeObject(_currentCharacter);
			out.writeObject(_dt.getGameMenuScreen().getKeyCodes());
			out.writeInt(_xWindowOffset);
			out.writeInt(_yWindowOffset);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeFilepathsFile(){
		FileOutputStream fos;
		ObjectOutputStream out;
		try {
			fos = new FileOutputStream("src/tests/saves/savedGames");
			out = new ObjectOutputStream(fos);
			out.writeObject(_dt.getSavedFilePaths());
		}  catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void readFilepathsFile(){
		FileInputStream fis;
		ObjectInputStream in;
		try {
			fis = new FileInputStream("src/tests/saves/savedGames");
			in = new ObjectInputStream(fis);
			_dt.setSavedFilePaths((HashMap<String,String>) in.readObject());
		} catch(FileNotFoundException e){
			//Do Nothing...no games to load
		}catch(IOException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void loadGame(String filepath){
		System.out.println("Loading game!");
		FileInputStream fis;
		ObjectInputStream in;

		try {
			fis = new FileInputStream(filepath);
			in = new ObjectInputStream(fis);
			_mapCache =  (HashMap<String,Map>) in.readObject();
			_currMap = (Map) in.readObject();
			_dt.setCharacterMap((HashMap<String,Character>) in.readObject());
			_dt.setParty((List<Character>) in.readObject());
			_currentCharacter = (MainCharacter) in.readObject();

			_dt.getGameMenuScreen().load((int[]) in.readObject());
			_xWindowOffset = in.readInt();
			_yWindowOffset = in.readInt();

			//Reload all party characters
			for(Character c : _dt.getParty())
				c.load(_dt);
			in.close();

			//reload every map
			for(String path : _mapCache.keySet()){
				Map m = _mapCache.get(path);
				m.load(_dt);
			}
			
			_terrainToPaint = _currMap.getTerrain();
			
			System.out.println("Finished Loading!");
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch(InvalidEventException e){
			e.printStackTrace();
		}

	}
	
	public int getXWindowOffset() {
		return _xWindowOffset;
	}
	public int getYWindowOffset() {
		return _yWindowOffset;
	}
}
