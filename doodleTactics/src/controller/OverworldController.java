package controller;

import java.awt.Event;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import controller.combatController.RandomBattleAI;
import character.Character;
import character.Character.CharacterDirection;

import main.DoodleTactics;
import main.GameScreen;
import map.Map;
import map.Tile;

public class OverworldController extends GameScreenController {

	//private RandomMoveTimer _randomMoveTimer;
	
	public OverworldController(DoodleTactics dt, GameScreen game) {
		super(dt);
		_gameScreen = game;
		//_randomMoveTimer = new RandomMoveTimer();
	}

//	private class RandomMoveTimer extends Timer {
//		
//		public RandomMoveTimer() {
//			super(4000, null);
//			this.addActionListener(new RandomMoveListener());
//		}
//		
//		private class RandomMoveListener implements java.awt.event.ActionListener {
//			
//			public void actionPerformed(java.awt.event.ActionEvent e) {
//
//				if(_gameScreen.getMap() != null) {
//				
//					for(Character c : _gameScreen.getMap().getCharactersToDisplay()) {
//						
//						// provided this character is not the main character
//						if(! c.equals(_gameScreen.getMainChar())) {
//							
//							System.out.println("character at " + c.getX() + ", " + c.getY());
//							
//							//generate a random direction to move in
//							Random r = new Random();
//							int direction = r.nextInt(4);
//							// retrieve the tile that corresponds to the given character
//							Tile src = _gameScreen.getTile((int) c.getX(), (int) c.getY());
//							System.out.println("src, x: " + src.x() + ", y:" + src.y());
//							
//							if(src != null) {
//								
//								System.out.println("character moving: " + c.getName() + " in direction " + direction + 
//										" from " + src.getX() + "," + src.getY() + " to... ");
//								
//								try {
//							
//									Tile dest = null;
//									
//									switch(direction) {
//										case 0:
//											dest = _gameScreen.getMap().getNorth(src);
//											if(dest != null && dest.canMove(Map.NORTH) && !dest.isOccupied()) {
//												System.out.println(dest.getX() + "," + dest.getY());
//												c.moveToTile(dest);
//											}
//											break;
//										case 1:
//											dest = _gameScreen.getMap().getSouth(src);
//											if(dest != null && dest.canMove(Map.SOUTH) && !dest.isOccupied()) {
//												System.out.println(dest.getX() + "," + dest.getY());
//												c.moveToTile(dest);
//											}
//											break;
//										case 2:
//											dest = _gameScreen.getMap().getEast(src);
//											if(dest != null && dest.canMove(Map.EAST)) {
//												System.out.println(dest.getX() + "," + dest.getY());
//												c.moveToTile(dest);
//											}
//											break;
//										case 3:
//											dest = _gameScreen.getMap().getWest(src);
//											if(dest != null && dest.canMove(Map.WEST)) {
//												System.out.println(dest.getX() + "," + dest.getY());
//												c.moveToTile(dest);
//											}
//											break;
//									}
//									
//									System.out.println("-------");
//								
//								} catch (ArrayIndexOutOfBoundsException ex) {
//									
//								}
//							}
//						}	
//					}
//				}
//			}
//		}
//	}
	
	@Override
	public void take() {
		// TODO : center the map around the main character
		System.out.println("OVERWORLD TAKES CONTROL");
		//_randomMoveTimer.start();
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
		//_randomMoveTimer.stop();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()){
		case KeyEvent.VK_CONTROL:
			//do stuff
			_gameScreen.switchToGameMenu();
			_dt.getGameMenuScreen().setDefaultTabToUnits();
			break;

			/*	TEST COMBAT CONTROLLER STUFF:
			 *  REMOVE WHEN DONE WITH ALL THAT
			 */
		case KeyEvent.VK_ENTER: 
			_gameScreen.enterCombat(new HashMap<Character, Tile>());
			break;

		case KeyEvent.VK_SPACE:
			System.out.println("Space");
			int currentX = _gameScreen.getMapX(_gameScreen.getX()) + 10;
			int currentY = _gameScreen.getMapY(_gameScreen.getY()) + 8;
			Tile newTile = null;

			switch(_gameScreen.getMainChar().getDirection()){
			case LEFT:
				newTile = _gameScreen.getMap().getTile(currentX - 1, currentY);
				break;
			case RIGHT:
				newTile = _gameScreen.getMap().getTile(currentX + 1, currentY);
				break;
			case UP:
				newTile = _gameScreen.getMap().getTile(currentX, currentY - 1);
				break;
			case DOWN:
				newTile = _gameScreen.getMap().getTile(currentX, currentY + 1);
				break;
			}

			if(newTile != null && newTile.interactible()){
				System.out.println("Here");
				_gameScreen.pushControl(newTile.getEvent());
			}
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) { 

		//	System.out.println("isAnimating:" + _gameScreen.isAnimating());

		if(!_gameScreen.isAnimating()) {	

			int currentX = _gameScreen.getMapX(_gameScreen.getX()) + 10;
			int currentY = _gameScreen.getMapY(_gameScreen.getY()) + 8;
			Tile oldTile = _gameScreen.getMap().getTile(currentX, currentY);
			Tile newTile = null;

			switch(e.getKeyChar()) {

			case 'w':
				System.out.println("w");
				newTile = _gameScreen.getMap().getTile(currentX, currentY - 1);
				_gameScreen.getMainChar().setUp();
				if(newTile != null && newTile.canMove(Map.SOUTH)){			
					if(newTile.hasEnterEvent()){
						_gameScreen.pushControl(newTile.getEvent());
						break;
					} else if(_dt.getGameScreen().getMap().generatesRandomBattle(newTile)){
						_dt.getGameScreen().getMap().startBattle(newTile);
					}
					
					newTile.setOccupant(_gameScreen.getMainChar());
					oldTile.removeOccupant();
					_gameScreen.mapUpdate(0, -1);
				}
				break;
			case 'a':
				System.out.println("a");
				newTile = _gameScreen.getMap().getTile(currentX - 1, currentY);
				_gameScreen.getMainChar().setLeft();

				if(newTile != null && newTile.canMove(Map.EAST)){			
					if(newTile.hasEnterEvent()) {
						_gameScreen.pushControl(newTile.getEvent());
						break;
					}
					else if(_dt.getGameScreen().getMap().generatesRandomBattle(newTile)){
						_dt.getGameScreen().getMap().startBattle(newTile);
					}
					
					newTile.setOccupant(_gameScreen.getMainChar());
					_gameScreen.mapUpdate(-1, 0);
					oldTile.removeOccupant();
				}
				break;
			case 's':
				System.out.println("s");
				newTile = _gameScreen.getMap().getTile(currentX, currentY + 1);
				_gameScreen.getMainChar().setDown();

				if(newTile != null && newTile.canMove(Map.NORTH)){			
					if(newTile.hasEnterEvent()){
						_gameScreen.pushControl(newTile.getEvent());
						break;
					}
					else if(_dt.getGameScreen().getMap().generatesRandomBattle(newTile)){
						_dt.getGameScreen().getMap().startBattle(newTile);
					}
					
					newTile.setOccupant(_gameScreen.getMainChar());
					_gameScreen.mapUpdate(0, 1);
					oldTile.removeOccupant();
				}
				break;
			case 'd':
				System.out.println("d");
				newTile = _gameScreen.getMap().getTile(currentX + 1,  currentY);
				_gameScreen.getMainChar().setRight();
				if(newTile != null && newTile.canMove(Map.WEST)){
					if(newTile.hasEnterEvent()) {
						_gameScreen.pushControl(newTile.getEvent());
						break;
					}
					else if(_dt.getGameScreen().getMap().generatesRandomBattle(newTile)){
						_dt.getGameScreen().getMap().startBattle(newTile);
					}
					
					newTile.setOccupant(_gameScreen.getMainChar());
					_gameScreen.mapUpdate(1, 0);
					oldTile.removeOccupant();
				}
				break;
			case 'f':

				try {
					Runtime.getRuntime().exec("google-chrome www.foodler.com");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		_gameScreen.repaint();
	}


	/*	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		Tile t = _gameScreen.getTile(e.getX(), e.getY());
	//	if (_validTiles.contains(t)) {
		//	_selectedTile = t;

			System.out.println(t.getOccupant());
	//	}
	}	*/

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseDragged(MouseEvent e) { }

	@Override
	public void mouseMoved(MouseEvent e) { }

	@Override
	public LinkedList<Character> getCharactersToDisplay() {
		return _gameScreen.getMap().getCharactersToDisplay();
	}

}
