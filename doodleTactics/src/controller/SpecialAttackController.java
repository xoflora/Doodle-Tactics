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

	private double _x;
	private double _y;
	private AnimationTimer _timer;
	private BufferedImage effect = _dt.importImage("src/graphics/effects/special_splatter.png");
	
	public SpecialAttackController(DoodleTactics dt, double x, double y) {
		super(dt);
		_x = x;
		_y = y;
		_timer = new AnimationTimer();
	}

	private class AnimationTimer extends Timer {

		private AnimationListener _listener;
		private LinkedList<MenuItem> _effects;
		private final int _width = effect.getWidth();
		private final int _height = effect.getHeight();
		private final int _rotationStep = 5;
		private final int _spawnStep = 5;

		public AnimationTimer() {
			super(75, null);
			_listener = new AnimationListener();
			this.addActionListener(_listener);
			_effects = new LinkedList<MenuItem>();
		}
		
		public void addGraphic() {
			MenuItem graphic = new MenuItem(_gameScreen,effect,effect,_dt,4) {
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
					AnimationTimer.this.addGraphic();
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
					for(MenuItem m : _effects) { 
						_gameScreen.removeMenuItem(m);
					}
					AnimationTimer.this.stop();
					_gameScreen.popControl();
				}
			}
		}
	}
	
	@Override
	public void take() {
		super.take();
		_timer.start();
	}
}
