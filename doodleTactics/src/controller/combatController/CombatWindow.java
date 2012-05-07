package controller.combatController;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import character.Character;
import character.Character.CharacterType;

import main.DoodleTactics;
import main.GameScreen;
import map.Tile;
import graphics.MenuItem;
import graphics.Rectangle;

public class CombatWindow extends MenuItem {

	protected charImage _attackerImg, _victimImg;
	private Character _attackerChar, _victimChar;
	
	private Tile _attackerTile;
	private Tile _victimTile;

	private CombatWindowController _c;

	private moveUpTimer _moveUpTimer;
	private attackTimer _attackTimer;
	private GameScreen _gs;
	private boolean _isAnimating;
	private int _attackerX, _battlersY = 0, _victimX, _victimY, _range;
	
	public CombatWindow(JPanel container, BufferedImage defaultPic, BufferedImage hoverPic, DoodleTactics dt, int priority) {
		super(container, defaultPic, hoverPic, dt, priority);
		this.setLocation(0, 17*Tile.TILE_SIZE);
		_gs = (GameScreen) container;
		_gs.addMenuItem(this);
		_attackTimer = new attackTimer(this);
		_moveUpTimer = new moveUpTimer(_attackTimer);
		
		_attackerImg = null;
		_victimImg = null;
		_attackerChar = null;
		_victimChar = null;
		_c = null;
	}

	public void animate(Tile src, Tile dest, CombatWindowController c, int range) {
		_attackerTile = src;
		_victimTile = dest;
		_attackerChar = src.getOccupant();
		_attackerImg = new charImage(_gs, 101);
		_attackerImg.setImage(src.getOccupant().getLeftImage());
		_victimChar = dest.getOccupant();
		_victimImg = new charImage(_gs, 102);
		_victimImg.setImage(dest.getOccupant().getRightImage());
		_c = c;
		
		_range = range;
		
		_isAnimating = true;
		this.setLocation(0, 17*Tile.TILE_SIZE);
		_battlersY = (17*Tile.TILE_SIZE)+215;
		this.setVisible(true);
		_attackerX = 700;
		_moveUpTimer.getListener().setMoveOffset(-40);
		_moveUpTimer.start();
	}
	
	public void paint(Graphics2D brush, BufferedImage img) {
		super.paint(brush, img);
		if (_isAnimating) {
			brush.drawImage(_attackerImg.getImage(), (int)_attackerImg.getX(), (int)_attackerImg.getY(), _gs);
			brush.drawImage(_victimImg.getImage(), (int)_victimImg.getX(), (int)_victimImg.getY(), _gs);
		}
	}
	
	public moveUpTimer getMoveUpTimer() {
		return _moveUpTimer;
	}
	
	private class moveUpTimer extends Timer {
		
		moveUpListener _listener;
		attackTimer _attackTimer;
		
		public moveUpTimer(attackTimer attackTimer) {
			super(20, null);
			_attackTimer = attackTimer;
			_listener = new moveUpListener(this);
			this.addActionListener(_listener);
		}
		
		public moveUpListener getListener() {
			return _listener;
		}
		
		public attackTimer getAttackTimer() {
			return _attackTimer;
		}
		
		private class moveUpListener implements ActionListener {

			private int count = 0, moveOffset = 0;
			private Timer _timer;
			
			public moveUpListener(Timer timer) {
				_timer = timer;
			}
			
			public void setMoveOffset(int offset) {
				moveOffset = offset;
			}
			
			public void actionPerformed(ActionEvent e) {
				CombatWindow.this.setLocation(0, CombatWindow.this.getY()+moveOffset);
				_attackerImg.setLocation(_attackerX, _battlersY+moveOffset);
				_victimImg.setLocation(400, _battlersY+moveOffset);
				_battlersY = _battlersY+moveOffset;
				count++;
				_gs.repaint();
				if (count == 8) {
					count = 0;
					_timer.stop();
//					_c.done();
					System.out.println("Stopped the timer");
					_gs.repaint();
					_attackerX = 700;
					if (moveOffset == -40) {
						moveUpTimer.this.getAttackTimer().start();
					}
					else {
						_c.done();
					}
				}
			}
		}
	}
	
	private class attackTimer extends Timer {
		
		CombatWindow _window;
		
		public attackTimer(CombatWindow window) {
			super(100, null);
			_window = window;
			this.addActionListener(new attackListener(this));
		}
		
		private class attackListener implements ActionListener {
			
			private Timer _timer;
			private int count = 0;
			
			public attackListener(Timer timer) {
				_timer = timer;
			}

			public void actionPerformed(ActionEvent e) {
				if (_attackerChar.getCharacterType() == CharacterType.WARRIOR || _attackerChar.getCharacterType() == CharacterType.THIEF) {
//					System.out.println("ANIMATING THE ATTACK");
					if (count < 6) {
						_attackerImg.setLocation(_attackerX-40, _battlersY);
						_victimImg.setLocation(400, _battlersY);
						_attackerX = _attackerX-40;
						switch(count) {
							case 0:
								_attackerImg.setRotation(-10);
								break;
							case 1:
								_attackerImg.setRotation(-5);
								break;
							case 2:
								_attackerImg.setRotation(0);
								break;
							case 3:
								_attackerImg.setRotation(5);
								break;
							case 4:
								_attackerImg.setRotation(10);
								break;
							case 6:
								_attackerImg.setRotation(0);
								break;
						}
						count++;
						_gs.repaint();
					}
					else {
						count = 0;
						
						_attackerChar.attack(_attackerTile, _victimTile, new Random(), count);
						_attackerChar.addExpForAttack(_victimChar);
						_victimChar.addExpForAttack(_attackerChar);
						_timer.stop();
						_window.getMoveUpTimer().getListener().setMoveOffset(40);
						_window.getMoveUpTimer().start();
					}
				}
				else {
					if (count < 6) {
						_attackerImg.setLocation(_attackerX-20, _battlersY);
						_victimImg.setLocation(400, _battlersY);
						_attackerX = _attackerX-20;
						switch(count) {
							case 0:
								_attackerImg.setRotation(-10);
								break;
							case 1:
								_attackerImg.setRotation(-5);
								break;
							case 2:
								_attackerImg.setRotation(0);
								break;
							case 3:
								_attackerImg.setRotation(5);
								break;
							case 4:
								_attackerImg.setRotation(10);
								break;
							case 6:
								_attackerImg.setRotation(0);
								break;
						}
						count++;
						_gs.repaint();
					}
					else {
						count = 0;
						_timer.stop();
						_attackerChar.attack(_attackerTile, _victimTile, new Random(), _range);
						_attackerChar.addExpForAttack(_victimChar);
						_victimChar.addExpForAttack(_attackerChar);
						_window.getMoveUpTimer().getListener().setMoveOffset(40);
						_window.getMoveUpTimer().start();
					}
				}
			}
		}
	}
		

	private class charImage extends Rectangle {

		private BufferedImage _image;
		
		public charImage(JPanel container, int priority) {
			super(container, priority);
		}

		@Override
		public BufferedImage getImage() {
			return _image;
		}
		
		public void setImage(BufferedImage img) {
			_image = img;
		}
	}
}
