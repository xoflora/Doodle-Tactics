package controller;

import graphics.MenuItem;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.Timer;

import main.DoodleTactics;
import main.GameScreen;

	public class SplatterTimer extends Timer {

		private AnimationListener _listener;
		private LinkedList<MenuItem> _effects;
		private int _width;
		private int _height;
		private int _rotationStep;
		private int _spawnStep;
		private boolean _reverse;
		private GameScreen _gameScreen;
		private BufferedImage _effect;
		private SpecialAttackController _specialControl;
		private DoodleTactics _dt;
		private int _x;
		private int _y;

		public SplatterTimer(SpecialAttackController specialControl, DoodleTactics dt, int x, int y) {
			super(40, null);
			_listener = new AnimationListener();
			this.addActionListener(_listener);
			_dt = dt;
			_specialControl = specialControl;
			_effect = _dt.importImage("src/graphics/effects/special_splatter.png");
			_gameScreen = dt.getGameScreen();
			_effects = new LinkedList<MenuItem>();
			_rotationStep = 5;
			_spawnStep = 5;
			_x = x;
			_y = y;
			_width = _effect.getWidth();
			_height = _effect.getHeight();
			_reverse = false;
		}
		
		public void addGraphic() {
			MenuItem graphic = new MenuItem(_gameScreen,_effect,_effect,_dt,4) {
				@Override
				public void paint(java.awt.Graphics2D brush, BufferedImage img) {
					brush.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
					super.paint(brush, img);
					brush.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
				}
			};
			graphic.setSize(0, 0);
			graphic.setLocation(_x, _y);
			graphic.setVisible(true);
			_gameScreen.addMenuItem(graphic);
			_effects.add(graphic);
		}
		
		private class AnimationListener implements java.awt.event.ActionListener {
			
			private int _cnt;
			private int _numSteps;

			public AnimationListener() {
				_cnt = 0;
				_numSteps = 20;
			}

			public void actionPerformed(java.awt.event.ActionEvent e) {
				
				if(_cnt % _spawnStep == 0) {
					SplatterTimer.this.addGraphic();
					_gameScreen.repaint();
				}
				
				for(MenuItem m : _effects) {
					double oldCenterX = m.getCenterX();
					double oldCenterY = m.getCenterY();
					m.setSize(m.getWidth() + _width / _numSteps, m.getHeight() + _height / _numSteps);
					m.setLocation(m.getX() - (m.getCenterX() - oldCenterX), m.getY() - (m.getCenterY() - oldCenterY));
					m.setRotation(m.getRotation() + _rotationStep);
				}
				
				_cnt++;
				
				if(_cnt == _numSteps) {
					if(_reverse) {
						for(MenuItem m : _effects) { 
							_gameScreen.removeMenuItem(m);
						}
						SplatterTimer.this.stop();
						_gameScreen.popControl();
					} else {
						_cnt = 0;
						_rotationStep = -_rotationStep;
						_width = -_width;
						_height = -_height;
						_reverse = true;
					}
				}
			}
		}
	}
