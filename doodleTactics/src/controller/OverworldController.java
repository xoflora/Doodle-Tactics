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

import util.Util;

import controller.combatController.AIController.RandomBattleAI;
import character.Character;
import character.Character.CharacterDirection;

import main.DoodleTactics;
import main.GameScreen;
import map.Map;
import map.Tile;

public class OverworldController extends GameScreenController {

	private RandomMoveTimer _randomMoveTimer;
	
	private List<Tile> _randomEnemyAttackRange;
	private List<Tile> _randomEnemyMovementRange;

	public OverworldController(DoodleTactics dt, GameScreen game) {
		super(dt);
		_randomMoveTimer = new RandomMoveTimer();
		_randomEnemyAttackRange = new ArrayList<Tile>();
		_randomEnemyMovementRange = new ArrayList<Tile>();
	}
	
	/**
	 * adds to the highlighted enemy attack range the range from the enemy contained within the given tile
	 * @param t the source tile (assumed to have an occupant that is a randomly-generated enemy)
	 */
	private void addRandomEnemyRange(Tile t) {
		Character c = t.getOccupant();
		_randomEnemyAttackRange = Util.union(_randomEnemyAttackRange,
				_gameScreen.getMap().getAttackRange(t, c.getMovementRange(),
						c.getMinAttackRange(), c.getMaxAttackRange()));
		_randomEnemyMovementRange = Util.union(_randomEnemyMovementRange,
				_gameScreen.getMap().getMovementRange(t, c.getMovementRange()));
		
		for (Tile toPaint : _randomEnemyAttackRange)
			toPaint.setInEnemyAttackRange(true);
		for (Tile toPaint : _randomEnemyMovementRange)
			toPaint.setInMovementRange(true);
	}
	
	/**
	 * clears the highlighted random enemy attack range
	 */
	private void clearRandomEnemyRange() {
		for (Tile t : _randomEnemyAttackRange)
			t.setInEnemyAttackRange(false);
		for (Tile t : _randomEnemyMovementRange)
			t.setInMovementRange(false);
		_randomEnemyAttackRange = new ArrayList<Tile>();
		_randomEnemyMovementRange = new ArrayList<Tile>();
	}

	private class RandomMoveTimer extends Timer {

		public RandomMoveTimer() {
			super(1000, null);
			this.addActionListener(new RandomMoveListener());
		}

		private class RandomMoveListener implements java.awt.event.ActionListener {

			public void actionPerformed(java.awt.event.ActionEvent e) {

				if(_gameScreen.getMap() != null) {

					for(Character c : _gameScreen.getMap().getCharactersToDisplay()) {

						// provided this character is not the main character
						if(! c.equals(_gameScreen.getMainChar())) {

							//generate a random direction to move in
							Random r = new Random();
							int direction = r.nextInt(4);
							// retrieve the tile that corresponds to the given character
							Tile src = _gameScreen.getTile((int) c.getX(), (int) c.getY());
							System.out.println("src, x: " + src.x() + ", y:" + src.y());

							if(src != null) {

								System.out.println("character moving: " + c.getName() + " in direction " + direction + 
										" from " + src.getX() + "," + src.getY() + " to... ");

								try {

									Tile dest = null;

									switch(direction) {
									case 0:
										dest = _gameScreen.getMap().getNorth(src);
										if(dest != null && dest.canMove(Map.NORTH) && !dest.isOccupied()) {
											System.out.println("MOVE NORTH");
											System.out.println(dest.getX() / Tile.TILE_SIZE + "," + dest.getY() / Tile.TILE_SIZE);
											src.removeOccupant();
											c.moveToTile(src, dest, false);
											dest.setOccupant(c);
										}
										break;
									case 1:
										dest = _gameScreen.getMap().getSouth(src);
										if(dest != null && dest.canMove(Map.SOUTH) && !dest.isOccupied()) {
											System.out.println("MOVE SOUTH");
											System.out.println(dest.getX() / Tile.TILE_SIZE + "," + dest.getY() / Tile.TILE_SIZE);
											src.removeOccupant();
											c.moveToTile(src, dest, false);
											dest.setOccupant(c);
										}
										break;
									case 2:
										dest = _gameScreen.getMap().getEast(src);
										if(dest != null && dest.canMove(Map.EAST) && !dest.isOccupied()) {
											System.out.println("MOVE EAST");
											System.out.println(dest.getX() / Tile.TILE_SIZE + "," + dest.getY() / Tile.TILE_SIZE);
											src.removeOccupant();
											c.moveToTile(src, dest, false);
											dest.setOccupant(c);
										}
										break;
									case 3:
										dest = _gameScreen.getMap().getWest(src);
										if(dest != null && dest.canMove(Map.WEST) && !dest.isOccupied()) {
											System.out.println("MOVE WEST");
											System.out.println(dest.getX() / Tile.TILE_SIZE + "," + dest.getY() / Tile.TILE_SIZE);
											src.removeOccupant();
											c.moveToTile(src, dest, false);
											dest.setOccupant(c);
										}
										break;
									}

									System.out.println("-------");

								} catch (ArrayIndexOutOfBoundsException ex) {

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
		super.take();
		// TODO : center the map around the main character
		//_randomMoveTimer.start();
	}

	@Override
	public void release() {
		super.release();
		clearRandomEnemyRange();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == _dt.getMenuKey()){
			//do stuff
			_gameScreen.switchToGameMenu();
			_dt.getGameMenuScreen().setDefaultTabToUnits();
		}
		/*	TEST COMBAT CONTROLLER STUFF:
		 *  REMOVE WHEN DONE WITH ALL THAT
		 */
		else if(e.getKeyCode() == KeyEvent.VK_ENTER){ 
			_gameScreen.enterCombat(new HashMap<Character, Tile>());
		}

		else if(e.getKeyCode() == _dt.getInteractKey()){
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
		}

		//Character Movement
		else if(e.getKeyCode() == _dt.getLeftKey() || e.getKeyCode() == _dt.getRightKey() || e.getKeyCode() == _dt.getUpKey() || e.getKeyCode() == _dt.getDownKey()){
			if(!_gameScreen.isAnimating()) {	

				Character main = _gameScreen.getMainChar();

				Tile oldTile = _gameScreen.getTile((int)main.getX(),
						(int)main.getY());
				Tile newTile = null;
				if(e.getKeyCode() == _dt.getUpKey()) {

					newTile = _gameScreen.getMap().getNorth(oldTile);

					if (newTile != null){			
						main.setUp();
						if (newTile.canMove(Map.SOUTH))
							_gameScreen.moveMainCharacter(oldTile, newTile);
					}			
				} else if(e.getKeyCode() == _dt.getLeftKey()){
					newTile = _gameScreen.getMap().getWest(oldTile);
					if (newTile != null) {
						main.setLeft();
						if (newTile.canMove(Map.EAST))
							_gameScreen.moveMainCharacter(oldTile, newTile);
					}
				} else if(e.getKeyCode() == _dt.getDownKey()){
					newTile = _gameScreen.getMap().getSouth(oldTile);
					if (newTile != null){			
						main.setDown();

						if (newTile.canMove(Map.NORTH))
							_gameScreen.moveMainCharacter(oldTile, newTile);
					}
				} else if (e.getKeyCode() == _dt.getRightKey()){
					newTile = _gameScreen.getMap().getEast(oldTile);
					if (newTile != null){
						main.setRight();
						if (newTile.canMove(Map.WEST))
							_gameScreen.moveMainCharacter(oldTile, newTile);
					}
				}
			}
			_gameScreen.repaint();

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

	/*		if(e.getKeyChar() == 'w') {

				newTile = _gameScreen.getMap().getNorth(oldTile);

				if (newTile != null){			
					main.setUp();
					if (newTile.canMove(Map.SOUTH))
						_gameScreen.moveMainCharacter(oldTile, newTile);
				}
			}

			else if(e.getKeyChar() == 'a'){
				newTile = _gameScreen.getMap().getWest(oldTile);
				if (newTile != null) {
					main.setLeft();
					if (newTile.canMove(Map.EAST))
						_gameScreen.moveMainCharacter(oldTile, newTile);
				}
			}
			else if(e.getKeyChar() == 's'){
				newTile = _gameScreen.getMap().getSouth(oldTile);
				if (newTile != null){			
					main.setDown();

					if (newTile.canMove(Map.NORTH))
						_gameScreen.moveMainCharacter(oldTile, newTile);
				}
			}
			else if(e.getKeyChar() == 'd'){
				newTile = _gameScreen.getMap().getEast(oldTile);
				if (newTile != null){
					main.setRight();
					if (newTile.canMove(Map.WEST))
						_gameScreen.moveMainCharacter(oldTile, newTile);
				}
			}*/
			if(e.getKeyChar() == 'f'){

				try {
					Runtime.getRuntime().exec("google-chrome www.foodler.com");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			else if(e.getKeyChar() == 'g'){
				_gameScreen.switchToClassChooserMenu();
			} else if(e.getKeyChar() == 'l') {
				SpecialAttackController specialAttack = new SpecialAttackController(_dt);
				specialAttack.setSpecialTimer(new SplatterTimer(specialAttack, _dt, 500, 500));
				_gameScreen.pushControl(specialAttack);
			}
		}
		_gameScreen.repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		
		if (e.getButton() == MouseEvent.BUTTON1) {
			Tile t = _gameScreen.getTile(e.getX(), e.getY());
			if (t != null) {
				if (_gameScreen.getMap().getRandomBattleEnemies().contains(t.getOccupant())) {
					addRandomEnemyRange(t);
				}
			}
		}
		else if (e.getButton() == MouseEvent.BUTTON3)
			clearRandomEnemyRange();
	}
	
	@Override
	public void mousePressed(MouseEvent e) { }
	
	@Override
	public void mouseDragged(MouseEvent e) { }
}
