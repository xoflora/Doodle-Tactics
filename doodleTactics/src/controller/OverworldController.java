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
import controller.combatController.UnitStatMenu;
import character.Character;
import character.Character.CharacterDirection;

import main.DoodleTactics;
import main.GameScreen;
import map.Map;
import map.Tile;

public class OverworldController extends GameScreenController {

	private RandomMoveTimer _randomMoveTimer;
	
	private OverworldMover _moveThread;
	
	public OverworldController(DoodleTactics dt, GameScreen game) {
		super(dt);
		_gameScreen = game;
		_randomMoveTimer = new RandomMoveTimer();
		_moveThread = null;
	}

	private class RandomMoveTimer extends Timer {
		
		public RandomMoveTimer() {
			super(10000, null);
			this.addActionListener(new RandomMoveListener());
		}
		
		private class RandomMoveListener implements java.awt.event.ActionListener {
			
			public void actionPerformed(java.awt.event.ActionEvent e) {

				if(_gameScreen.getMap() != null) {
				
					for(Character c : _gameScreen.getMap().getCharactersToDisplay()) {
						
						// provided this character is not the main character
						if(! c.equals(_gameScreen.getMainChar()) && c.getName().equals("Thighs")) {
							
							System.out.println("character at " + c.getCenterX()/Tile.TILE_SIZE + ", " + c.getCenterY()/Tile.TILE_SIZE);
							
							//generate a random direction to move in
							Random r = new Random();
							int direction = r.nextInt(4);
							// retrieve the tile that corresponds to the given character
							Tile src = _gameScreen.getTile((int) c.getCenterX(), (int) c.getCenterY());
							System.out.println("src, x: " + src.x() + ", y:" + src.y());
							
							if(src != null) {
								
								System.out.println("character moving: " + c.getName() + " in direction " + direction + 
										" from " + src.getX() + "," + src.getY() + " to... ");
								
								try {
							
									Tile dest = null;
									
									switch(direction) {
										case 0:
											dest = _gameScreen.getMap().getNorth(src);
											
											System.out.println("============MOVE NORTH============");
											System.out.println("dest is null? " + (dest == null));
											System.out.println("has permission to move to tile? " + dest.canMove(Map.NORTH));
											System.out.println("dest is occupied? " + dest.isOccupied());
											System.out.println("==================================");
											
											if(dest != null && dest.canMove(Map.NORTH) && !dest.isOccupied()) {
												
												System.out.println(dest.x() + ", " + dest.y());
												c.moveToTile(src, dest);
												src.removeOccupant();
												dest.setOccupant(c);
											}
											break;
										case 1:
											dest = _gameScreen.getMap().getSouth(src);
											
											System.out.println("============MOVE SOUTH============");
											System.out.println("dest is null? " + (dest == null));
											System.out.println("has permission to move to tile? " + dest.canMove(Map.SOUTH));
											System.out.println("dest is occupied? " + dest.isOccupied());		
											System.out.println("==================================");
											
											if(dest != null && dest.canMove(Map.SOUTH) && !dest.isOccupied()) {
												System.out.println(dest.x() + ", " + dest.y());												
												c.moveToTile(src, dest);
												src.removeOccupant();
												dest.setOccupant(c);
											}
											break;
										case 2:
											
											dest = _gameScreen.getMap().getEast(src);
											
											System.out.println("============MOVE EAST============");
											System.out.println("dest is null? " + (dest == null));
											System.out.println("has permission to move to tile? " + dest.canMove(Map.EAST));
											System.out.println("dest is occupied? " + dest.isOccupied());	
											System.out.println("==================================");
											
											if(dest != null && dest.canMove(Map.EAST) && !dest.isOccupied()) {
												System.out.println(dest.x() + ", " + dest.y());												
												c.moveToTile(src, dest);
												src.removeOccupant();
												dest.setOccupant(c);
											}
											break;
										case 3:
											dest = _gameScreen.getMap().getWest(src);
											
											System.out.println("============MOVE WEST============");
											System.out.println("dest is null? " + (dest == null));
											System.out.println("has permission to move to tile? " + dest.canMove(Map.WEST));
											System.out.println("dest is occupied? " + dest.isOccupied());	
											System.out.println("==================================");
											
											if(dest != null && dest.canMove(Map.WEST) && !dest.isOccupied()) {
												System.out.println(dest.x() + ", " + dest.y());												
												c.moveToTile(src, dest);
												src.removeOccupant();
												dest.setOccupant(c);
											}
											break;
									}
									
									System.out.println("-------");
								
								} catch (ArrayIndexOutOfBoundsException ex) {
									System.out.println("OUT OF BOUNDS IN RANDOM MOVER");
								}
							}
						}	
					}
				}
			}
		}
	}
	
	@Override
	public void take() {
		// TODO : center the map around the main character
		//_randomMoveTimer.start();
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
		_randomMoveTimer.stop();
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
			Character main = _gameScreen.getMainChar();
			Tile oldTile = _gameScreen.getTile((int)main.getX(),
					(int)main.getY());
			System.out.println(main.getX() + " " + main.getY());
			System.out.println(oldTile);
			Tile newTile = null;

			switch(_gameScreen.getMainChar().getDirection()){
			case LEFT:
				newTile = _gameScreen.getMap().getWest(oldTile);
				break;
			case RIGHT:
				newTile = _gameScreen.getMap().getEast(oldTile);
				break;
			case UP:
				newTile = _gameScreen.getMap().getNorth(oldTile);
				break;
			case DOWN:
				newTile = _gameScreen.getMap().getSouth(oldTile);
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

			Character main = _gameScreen.getMainChar();
			
			Tile oldTile = _gameScreen.getTile((int)main.getX(),
					(int)main.getY());
			Tile newTile = null;

			String input = "" + e.getKeyChar();
			char inputChar = input.toLowerCase().charAt(0);
			
			switch(inputChar) {

			case 'w':
				newTile = _gameScreen.getMap().getNorth(oldTile);
				
				if (_moveThread != null)
					if (_moveThread.moveCompleted()) {
						try {
							_moveThread.join();
						} catch (InterruptedException e1) {
							_dt.error("Threading error during movement.");
						}
						_moveThread = null;
					}
				
				if(newTile != null && (_moveThread == null || !_moveThread.isAlive())){
					main.setUp();
					if (newTile.canMove(Map.NORTH)) {
						_moveThread = new OverworldMover(_gameScreen, oldTile, newTile, _gameScreen.getMainChar());
						_moveThread.start();
					//	while (!_moveThread.moveCompleted());
					}
				}
				
				
	/*			if(newTile != null && newTile.canMove(Map.SOUTH)){			
					if(newTile.hasEnterEvent()){
						_gameScreen.pushControl(newTile.getEvent());
						break;
					} else if(_dt.getGameScreen().getMap().generatesRandomBattle(newTile)){
						_dt.getGameScreen().getMap().startBattle(newTile);
					}
					else {
						newTile.setOccupant(_gameScreen.getMainChar());
						oldTile.removeOccupant();
						_gameScreen.mapUpdate(0, -1);
					}
				}			*/
				break;
			case 'a':
				newTile = _gameScreen.getMap().getWest(oldTile);
				
				if (_moveThread != null)
					if (_moveThread.moveCompleted()) {
						try {
							_moveThread.join();
						} catch (InterruptedException e1) {
							_dt.error("Threading error during movement.");
						}
						_moveThread = null;
					}
				
				if(newTile != null && (_moveThread == null || !_moveThread.isAlive())){
					main.setLeft();
					if (newTile.canMove(Map.EAST)) {
						_moveThread = new OverworldMover(_gameScreen, oldTile, newTile, _gameScreen.getMainChar());
						_moveThread.start();
					}
					
					
				/*	if(newTile.hasEnterEvent()) {
						_gameScreen.pushControl(newTile.getEvent());
						break;
					}
					else if(_dt.getGameScreen().getMap().generatesRandomBattle(newTile)){
						_dt.getGameScreen().getMap().startBattle(newTile);
					}
					else {
						newTile.setOccupant(_gameScreen.getMainChar());
						_gameScreen.mapUpdate(-1, 0);
						oldTile.removeOccupant();
					}	*/
				}
				break;
			case 's':
				newTile = _gameScreen.getMap().getSouth(oldTile);
				
				if (_moveThread != null)
					if (_moveThread.moveCompleted()) {
						try {
							_moveThread.join();
						} catch (InterruptedException e1) {
							_dt.error("Threading error during movement.");
						}
						_moveThread = null;
					}
				
				if(newTile != null && (_moveThread == null || !_moveThread.isAlive())){
					main.setDown();
					if (newTile.canMove(Map.NORTH)) { 
						_moveThread = new OverworldMover(_gameScreen, oldTile, newTile, _gameScreen.getMainChar());
						_moveThread.start();
					}
				}

			/*	if(newTile != null && newTile.canMove(Map.NORTH)){			
					if(newTile.hasEnterEvent()){
						_gameScreen.pushControl(newTile.getEvent());
						break;
					}
					else if(_dt.getGameScreen().getMap().generatesRandomBattle(newTile)){
						_dt.getGameScreen().getMap().startBattle(newTile);
					}
					else {
						newTile.setOccupant(_gameScreen.getMainChar());
						_gameScreen.mapUpdate(0, 1);
						oldTile.removeOccupant();
					}
				}		*/
				break;
			case 'd':
				newTile = _gameScreen.getMap().getEast(oldTile);
				
				if (_moveThread != null)
					if (_moveThread.moveCompleted()) {
						try {
							_moveThread.join();
						} catch (InterruptedException e1) {
							_dt.error("Threading error during movement.");
						}
						_moveThread = null;
					}
				
				if(newTile != null && (_moveThread == null || !_moveThread.isAlive())){
					main.setRight();
					if (newTile.canMove(Map.WEST)) { 
						_moveThread = new OverworldMover(_gameScreen, oldTile, newTile, _gameScreen.getMainChar());
						_moveThread.start();
					}
				}
				
				
		/*		if(newTile != null && newTile.canMove(Map.WEST)){
					if(newTile.hasEnterEvent()) {
						_gameScreen.pushControl(newTile.getEvent());
						break;
					}
					else if(_dt.getGameScreen().getMap().generatesRandomBattle(newTile)){
						_dt.getGameScreen().getMap().startBattle(newTile);
					}
					else {
						newTile.setOccupant(_gameScreen.getMainChar());
						_gameScreen.mapUpdate(1, 0);
						oldTile.removeOccupant();
					}
				}			*/
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


//	@Override
//	public void mouseClicked(MouseEvent e) {
//			super.mouseClicked(e);
//		// TODO Auto-generated method stub
//	//	Tile t = _gameScreen.getTile(e.getX(), e.getY());
//	//	if (_validTiles.contains(t)) {
//		//	_selectedTile = t;
//
//	//		System.out.println(t.getOccupant());
//	//	}
//			
//		Tile t = _gameScreen.getTile(e.getX(), e.getY());
//		if (t != null)
//			System.out.println(t.getOccupant());
//	}

	@Override
	public void mousePressed(MouseEvent e) {
//		_randomMoveTimer.stop();
//		Tile t = _gameScreen.getTile(e.getX(), e.getY());
//		System.out.println("----------------------");
//		System.out.println("Tile " + t.x() + ", " +t.y());
//		System.out.println("Occupied?" + t.getOccupant());
//		System.out.println("Can Move:");
//		System.out.println("NORTH:" + t.canMove(Map.NORTH));
//		System.out.println("SOUTH:" + t.canMove(Map.SOUTH));
//		System.out.println("EAST:" + t.canMove(Map.EAST));
//		System.out.println("WEST:" + t.canMove(Map.WEST));
//		System.out.println("----------------------");
	}
	
	@Override
	public void mouseDragged(MouseEvent e) { }
	
	public void mouseMoved(MouseEvent e) {
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		_randomMoveTimer.start();
	}
	
	public void mouseClicked(MouseEvent e) {
		super.mouseMoved(e);
	}

	@Override
	public LinkedList<Character> getCharactersToDisplay() {
		return _gameScreen.getMap().getCharactersToDisplay();
	}

}
