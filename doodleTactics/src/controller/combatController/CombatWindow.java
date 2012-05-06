package controller.combatController;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

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

	charImage _attackerImg, _victimImg;
	Character _attackerChar, _victimChar;
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
		_attackerImg = new charImage(_gs, 101);
		_attackerImg.setImage(src.getLeftImage());
		_victimImg = new charImage(_gs, 102);
		_victimImg.setImage(dest.getRightImage());
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
			brush.drawImage(_attackerImg.getImage(), 700, _battlersY, _gs);
			brush.drawImage(_victimImg.getImage(), 400, _battlersY, _gs);
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
				if (count < 8) {
					CombatWindow.this.setLocation(0, CombatWindow.this.getY()-40);
					_battlersY = _battlersY-40;
					count++;
					if (count == 8) {
//						CombatWindow.this.setLocation(0, CombatWindow.this.getY()-5);
					}
					_gs.repaint();
				}
				else {
//					_attackerChar.getCharacterType() == CharacterType.WARRIOR || _attackerChar.getCharacterType() == CharacterType.
//
//								Character.this.setLocation((Character.this.getX() + (_deltaX*Tile.TILE_SIZE / _numSteps)), Character.this.getY() + (_deltaY*Tile.TILE_SIZE / _numSteps));
//
//								switch(_cnt) {
//								case 0:
//									Character.this.setRotation(-10);
//									break;
//								case 1:
//									Character.this.setRotation(-5);
//									break;
//								case 2:
//									Character.this.setRotation(0);
//									break;
//								case 3:
//									Character.this.setRotation(5);
//									break;
//								case 4:
//									Character.this.setRotation(10);
//									break;
//								case 5:
//									Character.this.setRotation(0);
//									break;
//								}
//
//								_container.repaint();
//
//								_cnt+=1;
//
//								/* if we've incremented numSteps times, then we should stop */
//								/* otherwise, continue incrementing */
//								if (_cnt == _numSteps) {
//									_timer.stop();
//									Character.this._isAnimating = false;
//									System.out.println("---END MOVE TO TILE---");
//								}
//
//								_container.repaint();
//							}
//						}
//					}
					if (count == 8) {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e1) {
							System.out.println("THREAD ISSUES");
						}
					}
					CombatWindow.this.setLocation(0, CombatWindow.this.getY()+40);
					_battlersY = _battlersY+40;
					count++;
					if (count == 16) {
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
