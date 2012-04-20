package main;

import java.awt.Graphics2D;
import java.util.List;
import javax.swing.Timer;

import character.MainCharacter;

import map.Map;
import map.Tile;

@SuppressWarnings("serial")
public class GameScreen extends Screen {

	private static int MAP_WIDTH, MAP_HEIGHT;
	private MainCharacter _mainCharacter;
	private List<Character> _characters;
	private Map _map;
	private int _xRef, _yRef;
	
	public GameScreen(controller.Controller control, DoodleTactics dt) {
		super(control, dt);
		this.setBackground(java.awt.Color.BLACK);
		MAP_WIDTH = 10;
		MAP_HEIGHT = 50;
		_xRef = Math.min(MAP_WIDTH - 11, 0);
		_yRef = Math.min(MAP_HEIGHT - 9, 0);
		//Tile[x][y]
		
		Tile[][] testTiles = new Tile[MAP_WIDTH][MAP_HEIGHT];
		for(int i = 0; i < MAP_WIDTH; i++) {
			for(int j = 0; j < MAP_HEIGHT; j++) {
				testTiles[i][j] = new Tile(this,"src/graphics/tile.png", i, j);
				testTiles[i][j].setVisible(true);
			}
		}
		for (int k = _xRef; k<_xRef+21; k++) {
			for (int m = _yRef; m<_yRef+17; m++) {
				if (k >= 0 && k < MAP_WIDTH && m >= 0 && m < MAP_HEIGHT) {
					testTiles[k][m].setFillColor(java.awt.Color.BLACK);
					testTiles[k][m].setLocation((k-_xRef)*Tile.TILE_SIZE, (m-_yRef)*Tile.TILE_SIZE);
				}
			}
		}
		_mainCharacter = new MainCharacter(this, "src/graphics/warrior_front.png", "src/graphics/warrior_front.png", "src/graphics/warrior_left.png", "src/graphics/warrior_right.png", "src/graphics/warrior_back.png", "src/graphics/warrior_front.png","test");
		_mainCharacter.setDown();
		_mainCharacter.setFillColor(java.awt.Color.BLACK);
		_mainCharacter.setSize(65, 50);
		int overflow = (65-48)/2;
		_mainCharacter.setLocation((10*Tile.TILE_SIZE)-overflow, 8*Tile.TILE_SIZE);
		_mainCharacter.setVisible(true);
		_map = new Map(testTiles,"c-level demo map");
		this.repaint();
	}
	
	private class MapMoveTimer extends Timer {
		
		private GameScreen _gs;
		private Tile[][] _newTiles;
		private Tile[][] _oldTiles;
		private int _deltaX, _deltaY;
		public int cnt = 0;
		
		public MapMoveTimer(GameScreen gs, Tile[][] newTiles, Tile[][] oldTiles, int deltaX, int deltaY) {
			super(50, null);
			this.addActionListener(new MyMoveListener(this));
			_gs = gs;
			_newTiles = newTiles;
			_oldTiles = oldTiles;
			_deltaX = deltaX;
			_deltaY = deltaY;
		}
		
		private class MyMoveListener implements java.awt.event.ActionListener {

			Timer _timer;
			
			public MyMoveListener (Timer t) {
				_timer = t;
			}
			
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (cnt == 8) {
					_timer.stop();
				}
				else {
					for(int i = 0; i < _newTiles.length; i++) {
						for(int j = 0; j < _newTiles[i].length; j++) {
							if (_oldTiles[i][j] != null) {
								_oldTiles[i][j].setLocation(_oldTiles[i][j].getX()+ Math.abs(_deltaX)*(Tile.TILE_SIZE/8), _oldTiles[i][j].getY()+ Math.abs(_deltaY)*(Tile.TILE_SIZE/8));
							}
							if (_newTiles[i][j] != null) {
								_newTiles[i][j].setLocation(_newTiles[i][j].getX()+ (Tile.TILE_SIZE/8), _newTiles[i][j].getY()+ (Tile.TILE_SIZE/8));
							}
						}
					}
					cnt+=1;
					_gs.repaint();
				}
			}
		}
	}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
	public Tile[][] getCurrentTiles() {
		Tile[][] tiles = new Tile[21][17];
		
		for (int k = _xRef; k<_xRef+21; k++) {
			for (int m = _yRef; m<_yRef+17; m++) {
				//System.out.println("k = " + k + " ;m = " + m);
				if (k >= 0 && k < MAP_WIDTH && m >= 0 && m < MAP_HEIGHT) {
					tiles[k-_xRef][m -_yRef] = _map.getTile(k,m);
				}
			}
		}
		return tiles;
	}
	
	public void mapUpdate(int deltaX, int deltaY) {
		
		int cnt = 0;
		
		Tile[][] oldTiles = getCurrentTiles();
		for (int i = 0; i<21; i++) {
			for (int j = 0; j<17; j++) {
				System.out.println(oldTiles[i][j]);
			}
		}
		
		if (_yRef+deltaY < MAP_HEIGHT - 8 && _yRef+deltaY>=0-8) {
			_yRef += deltaY;
		}
		if (_xRef+deltaX < MAP_WIDTH - 10 && _xRef+deltaX >= 0-10) {
			_xRef += deltaX;
		}
		for (int k = _xRef; k<_xRef+21; k++) {
			for (int m = _yRef; m<_yRef+17; m++) {
				//System.out.println("k = " + k + " ;m = " + m);
				if (k >= 0 && k < MAP_WIDTH && m >= 0 && m < MAP_HEIGHT) {
					_map.getTile(k,m).setLocation((k-_xRef)*Tile.TILE_SIZE, (m-_yRef)*Tile.TILE_SIZE);
				}
			}
		}
		
		Tile[][] newTiles = getCurrentTiles();
		
		Tile[][] toAnimate = new Tile[MAP_WIDTH + Math.abs(deltaX)][MAP_HEIGHT + Math.abs(deltaY)];
		
		for (int i = 0; i< toAnimate.length; i++) {
			for (int j = 0; i< toAnimate[i].length; j++) {
				if (deltaX == -1) {
					if (i == 0) {
						toAnimate[i][j] = newTiles[i][j];
					}
					else {
						toAnimate[i][j] = oldTiles[i-1][j];
					}
				}
				else if (deltaX == 1) {
					if (i == 0) {
						toAnimate[i][j] = oldTiles[i][j];
					}
					else {
						toAnimate[i][j] = newTiles[i-1][j];
					}
				}
				else if (deltaY == -1) {
					//moving up
					if (j == 0) {
						toAnimate[i][j] = newTiles[i][j];
					}
					else {
						toAnimate[i][j] = oldTiles[i][j-1];
					}
				}
				else {
					//moving down
					if (j == 0) {
						
					}
				}
			}
		}
		
		MapMoveTimer t = new MapMoveTimer(this,newTiles,oldTiles,deltaX,deltaY);
		t.start();
		this.repaint();
	}
	
	public MainCharacter getMainChar() {
		return _mainCharacter;
	}
	
	public Map getMap() {
		return _map;
	}
	 
	public void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
		for(int i = _xRef; i < _xRef+21; i++) {
			for(int j = _yRef; j < _yRef+17; j++) {
				if (i >= 0 && i < MAP_WIDTH && j >= 0 && j < MAP_HEIGHT) {
					_map.getTile(i,j).paint((Graphics2D) g, _map.getTile(i,j).getImage());
				}
			}
		}
//		System.out.println(_mainCharacter.getCurrentImage());
		_mainCharacter.paint((Graphics2D) g,_mainCharacter.getCurrentImage());
	}

}
