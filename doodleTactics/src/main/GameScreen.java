package main;
import graphics.MenuItem;
import graphics.Rectangle;
import graphics.Terrain;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.swing.Timer;

import controller.GameMenuController;
import controller.GameScreenController;
import controller.OverworldController;
import controller.combatController.CombatController;
import controller.combatController.CombatOrchestrator;
import controller.combatController.RandomBattleAI;

import character.Character;
import character.Mage;
import character.MainCharacter;
import graphics.Rectangle;

import map.InvalidMapException;
import map.InvalidTileException;
import map.Map;
import map.Tile;
import java.util.*;

/* currmap / prevmap vars
 * this is an idea we had about the game screen needing know what starting location
 * to set for the game screen when we enter from different locations
 */

public class GameScreen extends Screen<GameScreenController> {

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
	public GameScreen(DoodleTactics dt) {
		super(dt);
		this.setBackground(java.awt.Color.BLACK);
		MAP_WIDTH = 20;
		MAP_HEIGHT = 20;
		
		
		_currentCharacter = parseMainChar("src/tests/data/MainCharacterDemo");

		//	_gameMenuController = _dt.getGameMenuScreen().getController();
		_characterTerrainQueue = new PriorityQueue<Rectangle>(5, new Rectangle.RectangleComparator());
		_menuQueue = new PriorityQueue<MenuItem>(5, new Rectangle.RectangleComparator());

		//select a tile to go at the top left of the screen
		_isAnimating = false;
		_mapCache = new HashMap<String, Map>();
		
		this.repaint();
	}

	/**
	 * Parses the Main Character from the given file
	 */
	public MainCharacter parseMainChar(String filePath){
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
			String[] split = br.readLine().split(",");
			if(split.length != 7){
				System.out.println("Invalid Main Character File");
			} else{
				MainCharacter main =  new MainCharacter(this,split[2],split[3],split[4],split[5],split[6],split[2],5,5);
				int overflow = (main.getImage().getWidth() - Tile.TILE_SIZE) / 2;
				double x = 10*Tile.TILE_SIZE-overflow;
				double y=  8*Tile.TILE_SIZE - main.getImage().getHeight() + Tile.TILE_SIZE;
				main.setLocation(x, y);
				return main;
			}
		} catch (FileNotFoundException e) {
			System.out.println("Invalid Main Character File Path");
		} catch (IOException e) {
			System.out.println("Invalid Main Character File");
		}
		System.exit(0);
		return null;
	}
	
	/**
	 * sets the current map of the game screen to the map specified by the path.
	 * if the path was already in the cache, it loads the map from there, otherwise
	 * parses the map from the file at that path  
	 * @param mapPath is the path to the map to set
	 */
	public void setMap(String mapPath) {

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
				prevMap.setPrevXRef(_xRef);
				prevMap.setPrevYRef(_yRef);
			}
			_currMap = map;
			Character prevCharacter = _currentCharacter;
			_currentCharacter = _currMap.getMainCharacter();
			_terrainToPaint = _currMap.getTerrain();
			//set + reset xref and yref
			_xRef = _currMap.getPrevXRef();
			_yRef = _currMap.getPrevYRef();
			System.out.println("XREF:" + _xRef + " and YREF: " + _yRef);
			if(prevCharacter != null){
				System.out.println("GO!");
				_currentCharacter.setDirection(prevCharacter.getDirection());
			}
			// set all of the locations of the tiles relative to xref and yref
			for (int i = 0; i < map.getWidth(); i++) {
				for (int j = 0; j < map.getHeight(); j++) {
					map.getTile(i, j).setLocation((i - _xRef)*Tile.TILE_SIZE, (j - _yRef)*Tile.TILE_SIZE);
					map.getTile(i, j).setVisible(true);
				}
			}

		} catch (InvalidMapException e) {
			e.printMessage();
			System.exit(0);
		}
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

		public MapMoveTimer(int deltaX, int deltaY, boolean isPan) {
			super(50, null);
			this.addActionListener(new MyMoveListener(this, isPan));
			_deltaX = deltaX;
			_deltaY = deltaY;
		}

		private class MyMoveListener implements java.awt.event.ActionListener {

			private Timer _timer;
			private int _cnt = 0;
			private final int _numSteps = 6;
			private boolean _isPan;

			public MyMoveListener (Timer t, boolean isPan) {
				_timer = t;
				_isPan = isPan;
			}

			public void actionPerformed(java.awt.event.ActionEvent e) {

				for(int i = 0; i < MAP_WIDTH; i++) {
					for(int j = 0; j < MAP_HEIGHT; j++) {
						Tile t = _currMap.getTile(i, j);
						t.setLocation((t.getX() + (-_deltaX*Tile.TILE_SIZE / _numSteps)), t.getY() + (-_deltaY*Tile.TILE_SIZE / _numSteps));
						repaint();
					}
				}

				for(Rectangle r: _terrainToPaint){
					r.setLocation((r.getX() + (-_deltaX*Tile.TILE_SIZE / _numSteps)), r.getY() + (-_deltaY*Tile.TILE_SIZE / _numSteps));
					repaint();
				}

				List <Character> charsToPaint = getController().getCharactersToDisplay();

				/* if the camera is panning, then all of the characters including
				 * the character in control should move */
				if(_isPan) {
					_currentCharacter.setLocation((_currentCharacter.getX() + (-_deltaX*Tile.TILE_SIZE / _numSteps)), _currentCharacter.getY() + (-_deltaY*Tile.TILE_SIZE / _numSteps));
				}

				for(Character c : charsToPaint) {
					c.setLocation((c.getX() + (-_deltaX*Tile.TILE_SIZE / _numSteps)), c.getY() + (-_deltaY*Tile.TILE_SIZE / _numSteps));
					repaint();
				}

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
				}
			}
		}
	}

	public MainCharacter getMainChar() {
		return _currentCharacter;
	}

	/* accessor method returning whether or not the GameScreen is currently animating, used by Timer */
	public boolean isAnimating() {
		return _isAnimating;
	}

	public void mapUpdate(int x, int y) {

		/* if in the bounds of the map, specifically in relation to the main character,
		 * update the screen reference points and animate the map */
		/*System.out.println("--------MAP UPDATE---------");
		System.out.println("xRef: " + _xRef);
		System.out.println("YRef: " + _yRef);	*/

		if((_xRef + x + 11) <= MAP_WIDTH && (_xRef + x + 11) > 0 && (_yRef + y + 9) <= MAP_HEIGHT && (_yRef + y + 9) > 0) {

			_isAnimating = true;

			MapMoveTimer timer = new MapMoveTimer(x,y, false);
			timer.start();

			_xRef += x;
			_yRef += y;
		}

		//	System.out.println("--------------------------");
	}

	public void pan(int x, int y) {

		if((_xRef + x + 11) <= MAP_WIDTH && (_xRef + x + 11) > 0 && (_yRef + y + 9) <= MAP_HEIGHT && (_yRef + y + 9) > 0) {

			_isAnimating = true;

			MapMoveTimer timer = new MapMoveTimer(x,y, true);
			timer.start();

			_xRef += x;
			_yRef += y;
		}
	}

	public Map getMap() {
		return _currMap;
	}

	/**
	 * @param x the window x-coordinate
	 * @param y the window y-coordinate
	 * @return the tile corresponding to the given (x,y) coordinate in the window
	 * @author rroelke
	 */
	public Tile getTile(int x, int y) {
		return _currMap.getTile(getMapX(x), getMapY(y));
	}

	/**
	 * @param x
	 * @return the x-index of the tile in the map given the x-coordinate in the window
	 * @author rroelke
	 */
	public int getMapX(int x) {
		//	System.out.print("xRef: " + _xRef);
		return (x / Tile.TILE_SIZE) + _xRef;
	}

	/**
	 * @param y
	 * @return the y-index of the tile in the map given the y-coordinate in the window
	 * @author rroelke
	 */
	public int getMapY(int y) {
		return (y / Tile.TILE_SIZE) + _yRef;
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

	public void paintComponent(java.awt.Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;

		/*	System.out.println("-------PAINT--------");
		System.out.println("xMin: " + (_xRef - 1));
		System.out.println("xMax: " + (_xRef + 22));
		System.out.println("yMin: " + (_yRef - 1));
		System.out.println("yMax: " + (_yRef + 18));
		System.out.println("--------------------");		*/

		super.paintComponent(g);

		// paint all of the tiles first
		for(int i = 0; i < MAP_WIDTH; i++) {
			for(int j = 0; j < MAP_HEIGHT; j++) {
				// check that the given tile is within the bounds before painting it 
				//if((i < _xRef + 22 && i >= _xRef - 1) && (j < (_yRef + 18) && j >= (_yRef - 1))) {
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
		_characterTerrainQueue.add(_currentCharacter);

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

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	public void switchToGameMenu() {
		_dt.changeScreens(_dt.getGameMenuScreen());
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
	public void enterCombat(List<Character> enemies) {
		RandomBattleAI enemy = new RandomBattleAI(_dt, enemies);
		List<CombatController> e = new ArrayList<CombatController>();
		e.add(enemy);
		enterCombat(new CombatOrchestrator(_dt, e, null, null, RandomBattleAI.RANDOM_BATTLE_NUM_UNITS));
	}

	/**
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 * @param num the number of requested setup tiles
	 * @return a list of valid setup tiles given a map position
	 * @author rroelke
	 */
	public List<Tile> getValidSetupTiles(int num) {
		return _currMap.getValidSetupTiles(_currMap.getTile(getMapX(getWidth()/2), getMapY(getHeight()/2)), num);
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
	 * @return the menu element of the screen that contains the given point, null if none exists
	 */
	public MenuItem checkContains(java.awt.Point point) {

		MenuItem toReturn = null;
		for (MenuItem r : _menuQueue) {
			r.setDefault();
			if (r.contains(point)) {
				r.setHovered();
				toReturn = r;
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
}
