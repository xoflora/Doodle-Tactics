package controller;

import graphics.MenuItem;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.Timer;

import main.DoodleTactics;
import main.GameScreen;
import map.Tile;

	public class ArrowTimer extends Timer {

		private AnimationListener _listener;
		private LinkedList<MenuItem> _effects;
		private int _crossWidth;
		private int _crossHeight;
		private int _arrowWidth;
		private int _arrowHeight;
		private int _spawnStep;
		private boolean _reverse;
		private GameScreen _gameScreen;
		private BufferedImage _upshot;
		private MenuItem _arrow;
		private MenuItem _crosshair;
		private SpecialAttackController _specialControl;
		private DoodleTactics _dt;
		private int _srcX;
		private int _srcY;
		private int _destX;
		private int _destY;

		public ArrowTimer(SpecialAttackController specialControl, DoodleTactics dt, int srcX, int srcY, int destX, int destY) {
			super(40, null);
			
			System.out.println("srcX: " + srcX);
			System.out.println("srcY: " + srcY);
			System.out.println("destX: " + destX);
			System.out.println("destY" + destY);
			
			_srcX = srcX;
			_srcY = srcY;
			_dt = dt;
			_gameScreen = dt.getGameScreen();
			_specialControl = specialControl;
			_upshot = _dt.importImage("src/graphics/effects/upshot_splatter.png");
			_arrow = new MenuItem(_gameScreen, _dt.importImage("src/graphics/effects/special_arrow.png"), 
					_dt.importImage("src/graphics/effects/special_arrow_down.png"), _dt, 4); 
			BufferedImage crosshair = _dt.importImage("src/graphics/effects/crosshair.png"); 
			_crosshair = new MenuItem(_gameScreen, crosshair, crosshair, _dt, 4);
			_arrow.setLocation(srcX * Tile.TILE_SIZE, srcY * Tile.TILE_SIZE);
			_arrow.setVisible(true);
			_arrow.setHighQuality(true);
			_crosshair.setVisible(true);
			_crosshair.setHighQuality(true);
			_crossWidth = (int) _crosshair.getWidth();
			_crossHeight = (int) _crosshair.getHeight();
			_crosshair.setSize(0, 0);
			_crosshair.setLocation(destX * Tile.TILE_SIZE, destY * Tile.TILE_SIZE);
			_effects = new LinkedList<MenuItem>();
			_arrowWidth = (int) _arrow.getWidth();
			_arrowHeight = (int) _arrow.getHeight();
			_spawnStep = 5;
			_destX = destX;
			_destY = destY;
			_listener = new AnimationListener();
			this.addActionListener(_listener);
			_reverse = false;
		}
		
		public void addExplosion(int cnt) {
			MenuItem graphic = new MenuItem(_gameScreen,_upshot,_upshot,_dt,4);
			graphic.setHighQuality(true);
			graphic.setSize(0, 0);
			graphic.setLocation(_destX,_destY);
			graphic.setVisible(true);
			_gameScreen.addMenuItem(graphic);
			_effects.add(graphic);
		}
		
		private class AnimationListener implements java.awt.event.ActionListener {
			
			private int _cnt;
			private int _numSteps;
			private double _arrowPeak;

			public AnimationListener() {
				_cnt = 0;
				_numSteps = 20;
				//_crosshair.setSize(0, 0);
				_gameScreen.addMenuItem(_arrow);
				_gameScreen.addMenuItem(_crosshair);
				_reverse = false;
			}
			
			public void scale(boolean grow) {
				
				double oldCenterX = _crosshair.getCenterX();
				double oldCenterY = _crosshair.getCenterY();
				
				if(grow) {
					_crosshair.setSize(_crosshair.getWidth() + (_crossWidth / (_numSteps*2)), _crosshair.getHeight() + (_crossHeight / (_numSteps*2)));
				} else {
					_crosshair.setSize(_crosshair.getWidth() - (_crossWidth / (_numSteps)), _crosshair.getHeight() - (_crossHeight / (_numSteps)));
					_arrow.setSize(_arrow.getWidth(), _arrow.getHeight() - (_arrowHeight / _numSteps));
				}
				
				_crosshair.setLocation(_crosshair.getX() - (_crosshair.getCenterX() - oldCenterX), _crosshair.getY() - (_crosshair.getCenterY() - oldCenterY));
			}
				
			public void actionPerformed(java.awt.event.ActionEvent e) {
				
				if(_cnt < _numSteps) {
					System.out.println("First case: " + ((_srcY*Tile.TILE_SIZE) / _numSteps));
					_arrow.setLocation(_arrow.getX(), _arrow.getY() - ((DoodleTactics.TILE_ROWS*Tile.TILE_SIZE) / _numSteps));
					_arrowPeak = _arrow.getY();
				} else if(_cnt < _numSteps * 2){
					System.out.println("Second case: " + ((_destY*Tile.TILE_SIZE) + _crossHeight - _arrowPeak));
					_arrow.setLocation(_arrow.getX(), _arrow.getY() + (((_destY*Tile.TILE_SIZE) - _arrowPeak) / _numSteps));
				} else {
					scale(false);
				}
				
				if(!_reverse) {
					scale(true);
				}
			
				_cnt++;
				
				if(_cnt == _numSteps) {
					_arrow.setLocation((_destX*Tile.TILE_SIZE)-(_arrow.getWidth()/2), _arrow.getY());
					_arrow.setHovered();
				} else if(_cnt == (_numSteps * 2) || _cnt == (_numSteps * 3)) {
					if(_reverse) {
						ArrowTimer.this.stop();
						_gameScreen.removeMenuItem(_arrow);
						_gameScreen.removeMenuItem(_crosshair);
						_gameScreen.popControl();
					} else {
						_reverse = true;
					}
				}
				}
			}
		
		@Override
		public void start() {
			//_dt.getGameScreen().panToMapTile(_srcX, _srcY);
			super.start();
		}
	}
