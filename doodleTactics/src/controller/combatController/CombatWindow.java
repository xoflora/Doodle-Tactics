package controller.combatController;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;

import main.DoodleTactics;
import main.GameScreen;
import map.Tile;
import graphics.MenuItem;

public class CombatWindow {

	MenuItem _combatBox;
	Character _attacker, _victim;
	moveUpTimer _moveUpTimer;
	GameScreen _gs;
	
	public CombatWindow(GameScreen gs, DoodleTactics dt) {
		_combatBox = new MenuItem(gs, dt.importImage("src/graphics/menu/combat_window.png"), dt.importImage("src/graphics/menu/combat_window.png"), dt, 100);
		_combatBox.setLocation(0, 17*Tile.TILE_SIZE);
		_gs = gs;
		_gs.addMenuItem(_combatBox);
		_moveUpTimer = new moveUpTimer();
	}
	
	public void prepareWindow(Character attacker, Character victim) {
		_attacker = attacker;
		_victim = victim;
	}

	public void animate() {
		_combatBox.setLocation(0, 17*Tile.TILE_SIZE);
		_combatBox.setVisible(true);
		_moveUpTimer.start();
	}
	
	private class moveUpTimer extends Timer {
		
		public moveUpTimer() {
			super(5, null);
			this.addActionListener(new moveUpListener(this));
		}
		
		private class moveUpListener implements ActionListener {

			int count = 0;
			Timer _timer;
			
			public moveUpListener(Timer timer) {
				_timer = timer;
			}
			
			public void actionPerformed(ActionEvent e) {
				if (count < 13) {
					_combatBox.setLocation(0, _combatBox.getY()-25);
					count++;
					if (count == 13) {
						_combatBox.setLocation(0, _combatBox.getY()-10);
					}
					_gs.repaint();
				}
				else {
					//do weapon animations
					if (count == 13) {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							System.out.println("THREAD ISSUES");
						}
					}
					_combatBox.setLocation(0, _combatBox.getY()+25);
					count++;
					if (count == 26) {
						_combatBox.setLocation(0, _combatBox.getY()+10);
						count = 0;
						_timer.stop();
					}
					_gs.repaint();
				}
			}
		}
	}
}
