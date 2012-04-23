package main;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Stack;

import javax.swing.Timer;

import controller.GameMenuController;
import controller.GameScreenController;
import controller.OverworldController;

import character.MainCharacter;

import map.InvalidMapException;
import map.InvalidTileException;
import map.Map;
import map.Tile;

/* currmap / prevmap vars
 * this is an idea we had about the game screen needing know what starting location
 * to set for the game screen when we enter from different locations
 */

public class GameScreen extends Screen<GameScreenController> {
	
	private static final int NUM_TILES_X = 21;
	private static final int NUM_TILES_Y = 17;
	
	private static final int DEFAULT_XREF = 5;
	private static final int DEFAULT_YREF = 5;

	private static int MAP_WIDTH, MAP_HEIGHT;
	private MainCharacter _mainCharacter;
	private List<Character> _characters;

	private Map _prevMap; 
	private Map _currMap;
	private int _xRef;
	private int _yRef;
	private boolean _isAnimating;
	private GameMenuController _gameMenuController;
	
	public GameScreen(DoodleTactics dt) {
		super(dt);
				
		this.setBackground(java.awt.Color.BLACK);
		MAP_WIDTH = 20;
		MAP_HEIGHT = 20;
		
		_gameMenuController = _dt.getGameMenuScreen().getController();
	/*	Tile[][] testTiles = new Tile[MAP_WIDTH][MAP_HEIGHT];
		for(int i = 0; i < MAP_WIDTH; i++) {
			for(int j = 0; j < MAP_HEIGHT; j++) {
				try {
					testTiles[i][j] = new Tile(this,"src/graphics/tiles/tile.png", i, j,1,1);
				} catch (InvalidTileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				testTiles[i][j].setVisible(true);
			}
		}			*/		
		//select a tile to go at the top left of the screen
		_xRef = DEFAULT_XREF;
		_yRef = DEFAULT_YREF;
		_isAnimating = false;
		
		//set the location of the rest of the tiles relative to the topleft tile
	/*	for(int i = 0; i < MAP_WIDTH; i++) {
			for(int j = 0; j < MAP_HEIGHT; j++) {
				testTiles[i][j].setLocation((i-_xRef)*Tile.TILE_SIZE, (j-_yRef)*Tile.TILE_SIZE);
			}
		}				*/
		
		_mainCharacter = new MainCharacter(this, "src/graphics/characters/warrior_front.png",
				"src/graphics/characters/warrior_front.png", "src/graphics/characters/warrior_left.png",
				"src/graphics/characters/warrior_right.png", "src/graphics/characters/warrior_back.png",
				"src/graphics/characters/warrior_front.png","test");
		_mainCharacter.setDown();
		_mainCharacter.setFillColor(java.awt.Color.BLACK);
		_mainCharacter.setSize(65, 50);
		
		int overflow = (65-48)/2;
		_mainCharacter.setLocation((10*Tile.TILE_SIZE)-overflow, 8*Tile.TILE_SIZE);
		_mainCharacter.setVisible(true);
		
		try {
			setMap(Map.map(this, "src/tests/data/testMapDemo"));
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
				_cnt+=1;
				if (_cnt == _numSteps) {
					_isAnimating = false;
					_timer.stop();
				}
			}
		}
	}

	public MainCharacter getMainChar() {
		return _mainCharacter;
	}

	/* accessor method returning whether or not the GameScreen is currently animating, used by Timer */
	public boolean isAnimating() {
		return _isAnimating;
	}
	
	public void mapUpdate(int x, int y) {
		
		// oh my god this is soooooo awesome
		
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
	 */
	public Tile getTile(int x, int y) {
		return _currMap.getTile(getMapX(x), getMapY(y));
	}
	
	/**
	 * @param x
	 * @return the x-index of the tile in the map given the x-coordinate in the window
	 */
	public int getMapX(int x) {
	//	System.out.print("xRef: " + _xRef);
		return (x / Tile.TILE_SIZE) + _xRef;
	}
	
	/**
	 * @param y
	 * @return the y-index of the tile in the map given the y-coordinate in the window
	 */
	public int getMapY(int y) {
		return (y / Tile.TILE_SIZE) + _yRef;
	}
	 
	public void paintComponent(java.awt.Graphics g) {
		
	/*	System.out.println("-------PAINT--------");
		System.out.println("xMin: " + (_xRef - 1));
		System.out.println("xMax: " + (_xRef + 22));
		System.out.println("yMin: " + (_yRef - 1));
		System.out.println("yMax: " + (_yRef + 18));
		System.out.println("--------------------");		*/
		
		super.paintComponent(g);

		for(int i = 0; i < MAP_WIDTH; i++) {
			for(int j = 0; j < MAP_HEIGHT; j++) {
				// check that the given tile is within the bounds before painting it 
				if((i < _xRef + 22 && i >= _xRef - 1) && (j < (_yRef + 18) && j >= (_yRef - 1))) {
					_currMap.getTile(i,j).paint((Graphics2D) g, _currMap.getTile(i,j).getImage());
				}
			}
		}
		
		if (_mainCharacter != null)
			_mainCharacter.paint((Graphics2D) g,_mainCharacter.getCurrentImage());
		
		//System.out.println("--------------------");
}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
	public void switchToGameMenu() {
		_dt.changeScreens(_dt.getGameMenuScreen());
	}
}


//package main;
//
//import java.awt.Graphics2D;
//import java.util.HashMap;
//import java.util.List;
//import javax.swing.Timer;
//
//import character.MainCharacter;
//
//import map.Map;
//import map.Tile;
//
//@SuppressWarnings("serial")
//public class GameScreen extends Screen {
//
//	private static int MAP_WIDTH, MAP_HEIGHT;
//	private MainCharacter _mainCharacter;
//	private List<Character> _characters;
//	private Map _map;
//	private int _xRef, _yRef;
//	
//	public GameScreen(controller.Controller control, DoodleTactics dt) {
//		super(control, dt);
//		this.setBackground(java.awt.Color.BLACK);
//		MAP_WIDTH = 10;
//		MAP_HEIGHT = 50;
//		_xRef = Math.min(MAP_WIDTH - 11, 0);
//		_yRef = Math.min(MAP_HEIGHT - 9, 0);
//		//Tile[x][y]
//		
//		Tile[][] testTiles = new Tile[MAP_WIDTH][MAP_HEIGHT];
//		for(int i = 0; i < MAP_WIDTH; i++) {
//			for(int j = 0; j < MAP_HEIGHT; j++) {
//				testTiles[i][j] = new Tile(this,"src/graphics/tile.png", i, j);
//				testTiles[i][j].setVisible(true);
//			}
//		}
//		for (int k = _xRef; k<_xRef+21; k++) {
//			for (int m = _yRef; m<_yRef+17; m++) {
//				if (k >= 0 && k < MAP_WIDTH && m >= 0 && m < MAP_HEIGHT) {
//					testTiles[k][m].setFillColor(java.awt.Color.BLACK);
//					testTiles[k][m].setLocation((k-_xRef)*Tile.TILE_SIZE, (m-_yRef)*Tile.TILE_SIZE);
//				}
//			}
//		}
//		_mainCharacter = new MainCharacter(this, "src/graphics/warrior_front.png", "src/graphics/warrior_front.png", "src/graphics/warrior_left.png", "src/graphics/warrior_right.png", "src/graphics/warrior_back.png", "src/graphics/warrior_front.png","test");
//		_mainCharacter.setDown();
//		_mainCharacter.setFillColor(java.awt.Color.BLACK);
//		_mainCharacter.setSize(65, 50);
//		int overflow = (65-48)/2;
//		_mainCharacter.setLocation((10*Tile.TILE_SIZE)-overflow, 8*Tile.TILE_SIZE);
//		_mainCharacter.setVisible(true);
//		_map = new Map(testTiles,"c-level demo map");
//		this.repaint();
//	}
//	
//	private class MapMoveTimer extends Timer {
//		
//		private GameScreen _gs;
//		private HashMap<Tile,Integer> _tiles;
//		private int _deltaX, _deltaY;
//		public int cnt = 0;
//		
//		public MapMoveTimer(GameScreen gs, HashMap<Tile,Integer> tiles, int deltaX, int deltaY) {
//			super(50, null);
//			this.addActionListener(new MyMoveListener(this));
//			_gs = gs;
//			_tiles = tiles;
//			_deltaX = deltaX;
//			_deltaY = deltaY;
//		}
//		
//		private class MyMoveListener implements java.awt.event.ActionListener {
//
//			Timer _timer;
//			
//			public MyMoveListener (Timer t) {
//				_timer = t;
//			}
//			
//			public void actionPerformed(java.awt.event.ActionEvent e) {
//				if (cnt == 8) {
//					_timer.stop();
//				}
//				else {
//					
//					for(Tile t : _tiles.keySet()) {
//							t.setLocation(t.getX()- _deltaX*(Tile.TILE_SIZE/8), t.getY()- _deltaY*(Tile.TILE_SIZE/8));
//					}
//					
//					cnt+=1;
//					_gs.repaint();
//				}
//			}
//		}
//	}
//	
//	@Override
//	public void render() {
//		// TODO Auto-generated method stub
//		
//	}
//	
//	public Tile[][] getCurrentTiles() {
//		Tile[][] tiles = new Tile[21][17];
//		
//		for (int k = _xRef; k<_xRef+21; k++) {
//			for (int m = _yRef; m<_yRef+17; m++) {
//				//System.out.println("k = " + k + " ;m = " + m);
//				if (k >= 0 && k < MAP_WIDTH && m >= 0 && m < MAP_HEIGHT) {
//					tiles[k-_xRef][m -_yRef] = _map.getTile(k,m);
//				}
//			}
//		}
//		return tiles;
//	}
//	
//	public void mapUpdate(int deltaX, int deltaY) {
//		
//		System.out.println("xRef: " + _xRef);
//		System.out.println("yRef: " + _yRef);
//		
//		// get the tiles before we changed their locations
//		Tile[][] oldTiles = getCurrentTiles();
//		
//		//change the locations of the tiles based on deltaX, deltaY, xRef, and YRef
//		
//		if (_yRef+deltaY < MAP_HEIGHT - 8 && _yRef+deltaY>=0-8) {
//			_yRef += deltaY;
//		}
//		if (_xRef+deltaX < MAP_WIDTH - 10 && _xRef+deltaX >= 0-10) {
//			_xRef += deltaX;
//		}
//		
//		boolean canMove = true;
//		
//		//if()
//		
//		for (int k = _xRef; k<_xRef+21; k++) {
//			for (int m = _yRef; m<_yRef+17; m++) {
//				if (! (_xRef >= 0 && k < MAP_WIDTH && m>= 0 && m < MAP_HEIGHT)) {
//					System.out.println("k: " + k + "; m: " + m);
////					canMove = false;
////					System.out.println("MADE IT");
//				}
//			}
//		}
//		
//		// get the new tiles that are in the range of xRef and YRef
//		Tile[][] newTiles = getCurrentTiles();
//		
//		/* if we are in the boundaries create a dictionary of all of the tiles that were in the
//		 * range before the change and after the change */
//		if(canMove) {
//			
//			HashMap<Tile,Integer> tiles = new HashMap<Tile,Integer>();
//			
//			for (int i = 0; i < newTiles.length; i++) {
//				for (int j = 0; j < newTiles[i].length; j++) {
//					
//					if(tiles.get(newTiles[i][j]) == null && newTiles[i][j] != null) {
//						tiles.put(newTiles[i][j], 1);
//					}
//					
//					if(tiles.get(oldTiles[i][j]) == null && oldTiles[i][j] != null) {
//						tiles.put(oldTiles[i][j], 0);
//					}
//				}
//			}
//			
//			MapMoveTimer t = new MapMoveTimer(this,tiles,deltaX,deltaY);
//			t.start();
//			this.repaint();
//		}
//	}
//	
//	public MainCharacter getMainChar() {
//		return _mainCharacter;
//	}
//	 
//	public void paintComponent(java.awt.Graphics g) {
//		System.out.println("paint");
//		super.paintComponent(g);
//		for(int i = _xRef; i < _xRef+21; i++) {
//			for(int j = _yRef; j < _yRef+17; j++) {
//				if (i >= 0 && i < MAP_WIDTH && j >= 0 && j < MAP_HEIGHT) {
//					_map.getTile(i,j).paint((Graphics2D) g, _map.getTile(i,j).getImage());
//				}
//			}
//		}
////		System.out.println(_mainCharacter.getCurrentImage());
//		_mainCharacter.paint((Graphics2D) g,_mainCharacter.getCurrentImage());
//	}
//
//}
