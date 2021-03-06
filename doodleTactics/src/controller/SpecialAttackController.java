package controller;

import graphics.MenuItem;

import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.Timer;

import controller.combatController.CombatController;

import character.Character;

import main.DoodleTactics;
import main.Screen;

import java.util.Random;

public class SpecialAttackController extends GameScreenController {
	
	private Timer _currentTimer;
	
	public SpecialAttackController(DoodleTactics dt) {
		super(dt);
	}
	
	public void setSpecialTimer(Timer t) {
		_currentTimer = t;
	}
	
	@Override
	public void take() {
		super.take();
		if(_currentTimer != null) {
			_currentTimer.start();
		}
	}

//	private class AnimationTimer extends Timer {
//
//		private AnimationListener _listener;
//		private LinkedList<MenuItem> _effects;
//		private int _width;
//		private int _height;
//		private int _rotationStep;
//		private int _spawnStep;
//		private boolean _reverse;
//
//		public AnimationTimer() {
//			super(40, null);
//			_listener = new AnimationListener();
//			this.addActionListener(_listener);
//			_effects = new LinkedList<MenuItem>();
//			_rotationStep = 5;
//			_spawnStep = 5;
//			_width = effect.getWidth();
//			_height = effect.getHeight();
//			_reverse = false;
//		}
//		
//		public void addGraphic() {
//			MenuItem graphic = new MenuItem(_gameScreen,effect,effect,_dt,4) {
//				@Override
//				public void paint(java.awt.Graphics2D brush, BufferedImage img) {
//					brush.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//					super.paint(brush, img);
//					brush.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
//				}
//			};
//			graphic.setSize(0, 0);
//			graphic.setLocation(_x, _y);
//			graphic.setVisible(true);
//			_gameScreen.addMenuItem(graphic);
//			_effects.add(graphic);
//		}
//		
//		private class AnimationListener implements java.awt.event.ActionListener {
//			
//			private int _cnt;
//			private int _numSteps;
//
//			public AnimationListener() {
//				_cnt = 0;
//				_numSteps = 20;
//			}
//
//			public void actionPerformed(java.awt.event.ActionEvent e) {
//				
//				if(_cnt % _spawnStep == 0) {
//					AnimationTimer.this.addGraphic();
//					_gameScreen.repaint();
//				}
//				
//				for(MenuItem m : _effects) {
//					double oldCenterX = m.getCenterX();
//					double oldCenterY = m.getCenterY();
//					m.setSize(m.getWidth() + _width / _numSteps, m.getHeight() + _height / _numSteps);
//					m.setLocation(m.getX() - (m.getCenterX() - oldCenterX), m.getY() - (m.getCenterY() - oldCenterY));
//					m.setRotation(m.getRotation() + _rotationStep);
//				}
//				
//				_cnt++;
//				
//				if(_cnt == _numSteps) {
//					if(_reverse) {
//						for(MenuItem m : _effects) { 
//							_gameScreen.removeMenuItem(m);
//						}
//						AnimationTimer.this.stop();
//						_gameScreen.popControl();
//					} else {
//						_cnt = 0;
//						_rotationStep = -_rotationStep;
//						_width = -_width;
//						_height = -_height;
//						_reverse = true;
//					}
//				}
//			}
//		}
//	}
	
}
