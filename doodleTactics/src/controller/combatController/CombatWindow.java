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
	
	public void prepareWindow(Character src, Character dest) {
		_attacker = src;
		_victim = dest;
		_attacker.getLeftImage();
		_gs.addCharacter(_attacker);
		_gs.addCharacter(_victim);
	}

	public void animate() {
		_combatBox.setLocation(0, 17*Tile.TILE_SIZE);
		_combatBox.setVisible(true);
		_moveUpTimer.start();
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
					_combatBox.setLocation(0, _combatBox.getY()-30);
					count++;
					if (count == 11) {
						_combatBox.setLocation(0, _combatBox.getY()-5);
					}
					_gs.repaint();
				}
				else {
					//do weapon animations
					if (count == 11) {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							System.out.println("THREAD ISSUES");
						}
					}
					_combatBox.setLocation(0, _combatBox.getY()+30);
					count++;
					if (count == 22) {
						_combatBox.setLocation(0, _combatBox.getY()+5);
						count = 0;
						_gs.removeCharacter(_attacker);
						_gs.removeCharacter(_victim);
						_timer.stop();
					}
					_gs.repaint();
				}
			}
		}
	}
}
