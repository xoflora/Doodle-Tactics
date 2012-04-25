package main;
import graphics.Rectangle;
import graphics.Terrain;

import java.awt.Graphics2D;
import java.util.ArrayList;
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
	
	private static final int DEFAULT_XREF = 5;
	private static final int DEFAULT_YREF = 5;

	private static int MAP_WIDTH, MAP_HEIGHT;
	private MainCharacter _currentCharacter;
	private Map _currMap;
	private int _xRef;
	private int _yRef;
	private boolean _isAnimating;
	private GameMenuController _gameMenuController;
	private PriorityQueue<Rectangle> _characterTerrainQueue; // the list of characters / images to render on the screen
	private PriorityQueue<Rectangle> _menuQueue;
	private LinkedList<Terrain> _terrainToPaint;
	
	public GameScreen(DoodleTactics dt) {
		super(dt);
				
		this.setBackground(java.awt.Color.BLACK);
		MAP_WIDTH = 20;
		MAP_HEIGHT = 20;
		
		_gameMenuController = _dt.getGameMenuScreen().getController();
		_characterTerrainQueue = new PriorityQueue<Rectangle>(5, new Rectangle.RectangleComparator());
		_menuQueue = new PriorityQueue<Rectangle>(5, new Rectangle.RectangleComparator());
		//select a tile to go at the top left of the screen
		_xRef = DEFAULT_XREF;
		_yRef = DEFAULT_YREF;
		_isAnimating = false;
		
		_currentCharacter = new MainCharacter(this, "src/graphics/characters/warrior_front.png",
				"src/graphics/characters/warrior_front.png", "src/graphics/characters/warrior_left.png",
				"src/graphics/characters/warrior_right.png", "src/graphics/characters/warrior_back.png",
				"src/graphics/characters/warrior_front.png","test");
		
		_currentCharacter.setDown();
		_currentCharacter.setFillColor(java.awt.Color.BLACK);
		_currentCharacter.setSize(65, 50);
		
		int overflow = (65-48)/2;
		_currentCharacter.setLocation((10*Tile.TILE_SIZE)-overflow, 8*Tile.TILE_SIZE);
		_currentCharacter.setVisible(true);
		
		try {
			setMap(Map.map(this, "src/tests/data/testMapDemo"));
			_terrainToPaint = _currMap.getTerrain();
		} catch (InvalidMapException e) {
			e.printMessage();
		}
		this.repaint();
	}
	
	public void setMap(Map m) {
		_currMap = m;
		for (int i = 0; i < m.getWidth(); i++)
			for (int j = 0; j < m.getHeight(); j++) {
				m.getTile(i, j).setLocation((i - DEFAULT_XREF)*Tile.TILE_SIZE, (j - DEFAULT_YREF)*Tile.TILE_SIZE);
				m.getTile(i, j).setVisible(true);
			}
	}
	
	@Override
	protected GameScreenController defaultController() {
		return new OverworldController(_dt, this);
	}
	
	private class MapMoveTimer extends Timer {

		private int _deltaX, _deltaY;

		public MapMoveTimer(int deltaX, int deltaY) {
			super(50, null);
			this.addActionListener(new MyMoveListener(this));
			_deltaX = deltaX;
			_deltaY = deltaY;
		}

		private class MyMoveListener implements java.awt.event.ActionListener {

			private Timer _timer;
			private int _cnt = 0;
			private final int _numSteps = 6;

			public MyMoveListener (Timer t) {
				_timer = t;
			}

			public void actionPerformed(java.awt.event.ActionEvent e) {

				/* if we've incremented numSteps times, then we should stop */
				/* otherwise, continue incrementing */
				for(int i = 0; i < MAP_WIDTH; i++) {
					for(int j = 0; j < MAP_HEIGHT; j++) {
						Tile t = _currMap.getTile(i, j);
						t.setLocation((t.getX() + (-_deltaX*Tile.TILE_SIZE / _numSteps)), t.getY() + (-_deltaY*Tile.TILE_SIZE / _numSteps));
						repaint();
					}
				}
				for(Rectangle r: _terrainToPaint){
					r.setLocation((r.getX() + (-_deltaX*Tile.TILE_SIZE / _numSteps)), r.getY() + (-_deltaY*Tile.TILE_SIZE / _numSteps));
					System.out.println("x: " + (r.getX() + (-_deltaX*Tile.TILE_SIZE / _numSteps)) + " y: " + r.getY() + (-_deltaY*Tile.TILE_SIZE / _numSteps));
					repaint();
				}
				
//			System.out.println("cnt: " + _cnt);
			_cnt+=1;
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
/*		System.out.println("--------MAP UPDATE---------");
		System.out.println("xRef: " + _xRef);
		System.out.println("YRef: " + _yRef);		*/
		
		if((_xRef + x + 11) <= MAP_WIDTH && (_xRef + x + 11) > 0 && (_yRef + y + 9) <= MAP_HEIGHT && (_yRef + y + 9) > 0) {
			
			_isAnimating = true;
			
			MapMoveTimer timer = new MapMoveTimer(x,y);
			timer.start();
			
			_xRef += x;
			_yRef += y;
		}
		
		System.out.println("--------------------------");
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
	
	
	/**
	 * 
	 * @return The PriorityQueue storing the Menu items to paint
	 */
	public PriorityQueue<Rectangle> getMenuQueue(){
		return _menuQueue;
	}
	 
	public void paintComponent(java.awt.Graphics g) {
		
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
				if((i < _xRef + 22 && i >= _xRef - 1) && (j < (_yRef + 18) && j >= (_yRef - 1))) {
					_currMap.getTile(i,j).paint((Graphics2D) g, _currMap.getTile(i,j).getImage());
				}
			}
		}
		
		//Add all Characters and Terrains to PriorityQueue
		List<Character> charsToPaint = this.getController().getCharactersToDisplay();
		for(Character c : charsToPaint)
			_characterTerrainQueue.add(c);
		
		for(Terrain t : _terrainToPaint){
			System.out.println("Adding Terrain");
			_characterTerrainQueue.add(t);
			double x = t.getX();
			double y = t.getY();
			t.paint((Graphics2D) g, t.getImage());				
		}
		
		System.out.println("There are " + _characterTerrainQueue.size() + " things to paint");

		// paint all characters and terrains
		while(!_characterTerrainQueue.isEmpty()){
			Rectangle toPaint = _characterTerrainQueue.poll();
		}
		
		// finally paint all of the menus
		Rectangle[] menuArray = (Rectangle []) _menuQueue.toArray(new Rectangle[_menuQueue.size()]);
		Arrays.sort(menuArray);
		
		if (_currentCharacter != null)
			_currentCharacter.paint((Graphics2D) g,_currentCharacter.getCurrentImage());
		
		
		//System.out.println("--------------------");
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
		System.out.println(_currMap == null);
		System.out.println(_currMap.getTile(getMapX(getWidth()/2), getMapY(getHeight()/2)));
		return _currMap.getValidSetupTiles(_currMap.getTile(getMapX(getWidth()/2), getMapY(getHeight()/2)), num);
	}
}
