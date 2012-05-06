package controller;

import graphics.MenuItem;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;

import controller.combatController.CombatController;

import character.Character;

import main.DoodleTactics;
import main.Screen;

public class SpecialAttackController extends GameScreenController {

	private double _x;
	private double _y;
	private MenuItem _graphic;
	
	public SpecialAttackController(DoodleTactics dt, String path, double x, double y) {
		super(dt);
		_x = x;
		_y = y;
		BufferedImage effect = _dt.importImage(path);
		_graphic = new MenuItem(_gameScreen,effect,effect,dt);
	}

	private class AnimationTimer extends Timer {

		private AnimationListener _listener;

		public AnimationTimer(JPanel container) {
			super(75, null);
			_listener = new AnimationListener(this, container);
			this.addActionListener(_listener);
		}
		
		private class AnimationListener implements java.awt.event.ActionListener {

			private Timer _timer;
			private int _cnt;
			private JPanel _container;

			public AnimationListener (Timer t, JPanel container) {
				_container = container;
				_timer = t;
				_cnt = 0;
			}

			public void actionPerformed(java.awt.event.ActionEvent e) {

				_cnt = (_cnt + 1) % 8;

				_container.repaint();
			}
		}
	}

}
