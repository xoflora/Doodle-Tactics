package controller.combatController;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;

import character.Character;

import main.DoodleTactics;
import main.GameScreen;
import map.Tile;
import graphics.MenuItem;

public class CombatWindow extends MenuItem {

	Character _attacker, _victim;
	moveUpTimer _moveUpTimer;
	GameScreen _gs;
	boolean _isAnimating;
	int _attackerX, _battlersY, _victimX, _victimY;
	
	public CombatWindow(JPanel container, BufferedImage defaultPic, BufferedImage hoverPic, DoodleTactics dt, int priority) {
		super(container, defaultPic, hoverPic, dt, priority);
		this.setLocation(0, 17*Tile.TILE_SIZE);
		_gs = (GameScreen) container;
		_gs.addMenuItem(this);
		_moveUpTimer = new moveUpTimer();
	}
	
	public void prepareWindow(Character src, Character dest) {
		_attacker = src;
		_victim = dest;
	}

	public void animate() {
		_isAnimating = true;
		this.setLocation(0, 17*Tile.TILE_SIZE);
		_battlersY = (17*Tile.TILE_SIZE)+215;
		this.setVisible(true);
		_moveUpTimer.start();
	}
	
	public void paint(Graphics2D brush, BufferedImage img) {
		super.paint(brush, img);
		if (_isAnimating) {
			brush.drawImage(_attacker.getLeftImage(), 700, _battlersY, _gs);
			brush.drawImage(_victim.getRightImage(), 400, _battlersY, _gs);
		}
	}
	
	private class moveUpTimer extends Timer {
		
		public moveUpTimer() {
			super(1, null);
			this.addActionListener(new moveUpListener(this));
		}
		
		private class moveUpListener implements ActionListener {

			int count = 0;
			Timer _timer;
			
			public moveUpListener(Timer timer) {
				_timer = timer;
			}
			
			public void actionPerformed(ActionEvent e) {
				if (count < 11) {
					CombatWindow.this.setLocation(0, CombatWindow.this.getY()-30);
					_battlersY = _battlersY-30;
					count++;
					if (count == 11) {
//						CombatWindow.this.setLocation(0, CombatWindow.this.getY()-5);
					}
					_gs.repaint();
				}
				else {
					//do weapon animations
					if (count == 11) {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e1) {
							System.out.println("THREAD ISSUES");
						}
					}
					CombatWindow.this.setLocation(0, CombatWindow.this.getY()+30);
					_battlersY = _battlersY+30;
					count++;
					if (count == 22) {
//						CombatWindow.this.setLocation(0, CombatWindow.this.getY()+5);
						count = 0;
						_isAnimating = false;
						_timer.stop();
					}
					_gs.repaint();
				}
			}
		}
	}
}
