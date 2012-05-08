package controller.combatController;

import items.Weapon.WeaponType;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import character.Character;

import main.DoodleTactics;
import main.GameScreen;
import map.Tile;
import graphics.MenuItem;
import graphics.Rectangle;

public class CombatWindow extends MenuItem {

	private static final int TEXT_X = 400;
	
	protected animateImage _attackerImg, _victimImg;
	private Character _attackerChar, _victimChar;
	
	private Tile _attackerTile;
	private Tile _victimTile;

	private CombatWindowController _c;

	private moveUpTimer _moveUpTimer;
	private attackTimer _attackTimer;
	private GameScreen _gs;
	private boolean _isAnimating, _didAttack;
	private int _attackerX, _battlersY = 0, _victimX, _victimY, _range;
	private animateImage _attackerWep, _victimWep, _attackerSecondWep, _victimSecondWep;
	private int[] _damageDone;
	
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
		_attackerWep = null;
		_victimWep = null;
		_attackerSecondWep = null;
		_victimSecondWep = null;
		
		_c = null;
		
		_damageDone = new int[2];
	}

	public void animate(Tile src, Tile dest, CombatWindowController c, int range) {
		_attackerTile = src;
		_victimTile = dest;
		_attackerChar = src.getOccupant();
		_attackerImg = new animateImage(_gs, 101);
		_attackerImg.setImage(src.getOccupant().getLeftImage());
		_victimChar = dest.getOccupant();
		_victimImg = new animateImage(_gs, 102);
		_victimImg.setImage(dest.getOccupant().getRightImage());
		_attackerWep = null;
		_attackerSecondWep = null;
		if (_attackerChar.getWeapon() != null) {
			_attackerWep = new animateImage(_gs, 100);
			WeaponType w = _attackerChar.getWeapon().getWeaponType();
			if (w == WeaponType.AXE) {
				_attackerWep.setImage(_dt.importImage("src/graphics/items/axe.png"));
			}
			else if (w == WeaponType.BOW) {
				_attackerWep.setImage(_dt.importImage("src/graphics/items/bow.png"));
				_attackerSecondWep = new animateImage(_gs, 100);
				_attackerSecondWep.setImage(_dt.importImage("src/graphics/items/arrow.png"));
				_attackerSecondWep.setSize(_attackerSecondWep.getImage().getWidth(), _attackerSecondWep.getImage().getHeight());
			}
			else if (w == WeaponType.STAFF) {
				_attackerWep.setImage(_dt.importImage("src/graphics/items/staff.png"));
				_attackerSecondWep = new animateImage(_gs, 100);
				_attackerSecondWep.setImage(_dt.importImage("src/graphics/items/orb.png"));
				_attackerSecondWep.setSize(_attackerSecondWep.getImage().getWidth(), _attackerSecondWep.getImage().getHeight());
			}
			else if (w == WeaponType.DAGGER) {
				_attackerWep.setImage(_dt.importImage("src/graphics/items/dagger.png"));
			}
		}
		_attackerImg.setSize(_attackerImg.getImage().getWidth(), _attackerImg.getImage().getHeight());
		if (_attackerChar.getWeapon() != null) {
			_attackerWep.setLocation(1000, 1000);
			_attackerWep.setSize(_attackerWep.getImage().getWidth(), _attackerWep.getImage().getHeight());
		}
		_victimImg.setSize(_victimImg.getImage().getWidth(), _victimImg.getImage().getHeight());
		
		_c = c;
		
		_range = range;
		
		_isAnimating = true;
		this.setLocation(0, 17*Tile.TILE_SIZE);
		_battlersY = (17*Tile.TILE_SIZE)+215;
		this.setVisible(true);
		_attackerX = 700;
		_moveUpTimer.getListener().setMoveOffset(-40);
		_didAttack = false;
		_moveUpTimer.start();
	}
	
	public void paint(Graphics2D brush, BufferedImage img) {
		super.paint(brush, img);
		if (_isAnimating) {
			brush.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			_victimImg.setVisible(true);
			_attackerImg.setVisible(true);
			_victimImg.paint(brush, _victimImg.getImage());
			if (_attackerSecondWep != null) {
				_attackerSecondWep.setVisible(true);
				_attackerSecondWep.paint(brush, _attackerSecondWep.getImage());
			}
			if (_attackerWep != null) {
				_attackerWep.setVisible(true);
				_attackerWep.paint(brush, _attackerWep.getImage());
			}
			_attackerImg.paint(brush, _attackerImg.getImage());
			
			if (_didAttack) {
				String str1 = "";
				String str2 = "";
				if (_damageDone == null) {
					str1 = _attackerChar.getName() + " has defeated " + _victimChar.getName() + "!";
				}
				else {
					if (_damageDone[0] == -1) {
						str1 = _attackerChar.getName() + " missed!";
					}
					else {
						str1 = _attackerChar.getName() + " did " + _damageDone[0] + " damage.";
					}
					if (_damageDone[1] == -1) {
						str2 = _victimChar.getName() + " missed!";
					}
					else {
						str2 = _victimChar.getName() + " did " + _damageDone[1] + " damage.";
					}
				}
				brush.setRenderingHint(
						RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
				brush.setFont(new Font("Verdana", Font.BOLD, 20));
				brush.setColor(new Color(255,255,255));
//				brush.setBackground(java.awt.Color.BLACK);
				brush.setStroke(new BasicStroke());
				brush.drawString(str1, TEXT_X, _battlersY-140);
				brush.drawString(str2, TEXT_X, _battlersY-110);
			}
			brush.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
//			brush.drawImage(_victimImg.getImage(), (int)_victimImg.getX(), (int)_victimImg.getY(), _gs);
//			brush.drawImage(_attackerWep.getImage(), (int)_attackerWep.getX(), (int)_attackerWep.getY(), _gs);
//			brush.drawImage(_attackerImg.getImage(), (int)_attackerImg.getX(), (int)_attackerImg.getY(), _gs);
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
					_gs.repaint();
					_attackerX = 700;
					if (moveOffset == -40) {
						moveUpTimer.this.getAttackTimer().start();
					}
					else {
						CombatWindow.this.setLocation(0, 17*Tile.TILE_SIZE+100);
						_didAttack = false;
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
				boolean runFar = false;
				if (_attackerChar.getWeapon() == null) {
					runFar = true;
				}
				else {
					WeaponType w = _attackerChar.getWeapon().getWeaponType();
					if (w == WeaponType.AXE || w == WeaponType.DAGGER) {
						runFar = true;
					}
				}
				if (runFar) {
					if (count < 6) {
						_attackerImg.setLocation(_attackerX-40, _battlersY);
						_victimImg.setLocation(400, _battlersY);
						_attackerX = _attackerX-40;
						switch(count) {
							case 0:
								_attackerImg.setRotation(-10);
								_didAttack = true;
								_damageDone = _attackerChar.attack(_attackerTile, _victimTile, new Random(), _range);
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
								_attackerImg.setLocation(_attackerX-40, _battlersY);
								_attackerX = _attackerX-15;
								_attackerImg.setRotation(0);
								break;
						}
						count++;
						_gs.repaint();
					}
					else if (count >=6 && count < 12) {
						if (_attackerChar.getWeapon() != null) {
							if (_attackerChar.getWeapon().getWeaponType() == WeaponType.AXE) {
								if (count == 6) {
									_attackerWep.setLocation(_attackerX-22, _attackerImg.getY()+14);
								}
								else {
									_attackerWep.setLocation(_attackerWep.getX(), _attackerWep.getY());
								}
								switch(count) {
								case 6:
									_attackerWep.setRotation(50);
									break;
								case 7:
									_attackerWep.setRotation(30);
									break;
								case 8:
									_attackerWep.setRotation(10);
									break;
								case 9:
									_attackerWep.setRotation(10);
									break;
								case 10:
									_attackerWep.setRotation(30);
									break;
								case 11:
									_attackerWep.setRotation(50);
									break;
								}
								count++;
								_gs.repaint();
							}
							else if (_attackerChar.getWeapon().getWeaponType() == WeaponType.DAGGER) {
								switch (count) {
									case 6:
										_attackerWep.setLocation(_attackerX-5, _attackerImg.getY()+20);
										break;
									case 7:
										_attackerWep.setLocation(_attackerWep.getX()-10, _attackerWep.getY());
										break;
									case 8:
										_attackerWep.setLocation(_attackerWep.getX()-10, _attackerWep.getY());
										break;
									case 9:
										_attackerWep.setLocation(_attackerWep.getX()+10, _attackerWep.getY());
										break;
									case 10:
										_attackerWep.setLocation(_attackerWep.getX()-10, _attackerWep.getY());
										break;
									case 11:
										_attackerWep.setLocation(_attackerWep.getX()+10, _attackerWep.getY());
										break;
									case 12:
										_attackerWep.setLocation(_attackerWep.getX()+10, _attackerWep.getY());
										break;
								}
								count++;
								_gs.repaint();
							}
						}
						else {
							count = 12;
						}
					}
					else {
						count = 0;
						if (_attackerWep != null) {
							_attackerWep.setLocation(1000, 1000);
						}
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
								_didAttack = true;
								_damageDone = _attackerChar.attack(_attackerTile, _victimTile, new Random(), _range);
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
					else if (count >= 6 && count <12) {
						if (_attackerChar.getWeapon() != null) {
							if (_attackerChar.getWeapon().getWeaponType() == WeaponType.BOW) {
								if (count == 6) {
									_attackerWep.setLocation(_attackerX-12, _attackerImg.getY()+5);
									_attackerSecondWep.setLocation(_attackerX-30, _attackerImg.getY()+25);
								}
								switch(count) {
								case 6:
									_attackerSecondWep.setLocation(_attackerSecondWep.getX()-20, _attackerSecondWep.getY());
									break;
								case 7:
									_attackerSecondWep.setLocation(_attackerSecondWep.getX()-20, _attackerSecondWep.getY());
									break;
								case 8:
									_attackerSecondWep.setLocation(_attackerSecondWep.getX()-20, _attackerSecondWep.getY());
									break;
								case 9:
									_attackerSecondWep.setLocation(_attackerSecondWep.getX()-20, _attackerSecondWep.getY());
									break;
								case 10:
									_attackerSecondWep.setLocation(_attackerSecondWep.getX()-20, _attackerSecondWep.getY());
									break;
								case 11:
									_attackerSecondWep.setLocation(_attackerSecondWep.getX()-20, _attackerSecondWep.getY());
									break;
								}
								count++;
								_gs.repaint();
							}
							if (_attackerChar.getWeapon().getWeaponType() == WeaponType.STAFF) {
								if (count == 6) {
									_attackerWep.setLocation(_attackerX-12, _attackerImg.getY());
									_attackerSecondWep.setLocation(_attackerX-30, _attackerImg.getY()+20);
								}
								switch(count) {
								case 6:
									_attackerWep.setRotation(-10);
									_attackerSecondWep.setLocation(_attackerSecondWep.getX()-20, _attackerSecondWep.getY());
									break;
								case 7:
									_attackerWep.setRotation(-20);
									_attackerSecondWep.setLocation(_attackerSecondWep.getX()-20, _attackerSecondWep.getY());
									break;
								case 8:
									_attackerWep.setRotation(-10);
									_attackerSecondWep.setLocation(_attackerSecondWep.getX()-20, _attackerSecondWep.getY());
									break;
								case 9:
									_attackerWep.setRotation(0);
									_attackerSecondWep.setLocation(_attackerSecondWep.getX()-20, _attackerSecondWep.getY());
									break;
								case 10:
									_attackerWep.setRotation(-10);
									_attackerSecondWep.setLocation(_attackerSecondWep.getX()-20, _attackerSecondWep.getY());
									break;
								case 11:
									_attackerWep.setRotation(-20);
									_attackerSecondWep.setLocation(_attackerSecondWep.getX()-20, _attackerSecondWep.getY());
									break;
								}
								count++;
								_gs.repaint();
							}
						}
						else {
							count = 12;
						}
					}
					else {
						count = 0;
						if (_attackerWep != null) {
							_attackerWep.setLocation(1000, 1000);
						}
						if (_attackerSecondWep != null) {
							_attackerSecondWep.setLocation(1000, 1000);
						}
						_attackerChar.addExpForAttack(_victimChar);
						_victimChar.addExpForAttack(_attackerChar);
						_timer.stop();
						_window.getMoveUpTimer().getListener().setMoveOffset(40);
						_window.getMoveUpTimer().start();
					}
				}
			}
		}
	}
		

	private class animateImage extends Rectangle {

		private BufferedImage _image;
		
		public animateImage(JPanel container, int priority) {
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
