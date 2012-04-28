package controller;

import java.awt.Event;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import controller.combatController.RandomBattleAI;
import character.Character;
import character.Character.CharacterDirection;

import main.DoodleTactics;
import main.GameScreen;
import map.Map;
import map.Tile;

public class OverworldController extends GameScreenController {

	public OverworldController(DoodleTactics dt, GameScreen game) {
		super(dt);
		_gameScreen = game;
	}

	@Override
	public void take() {
		// TODO : center the map around the main character

	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			//do stuff
			_gameScreen.switchToGameMenu();
			_dt.getGameMenuScreen().setDefaultTabToUnits();
		}

		/*	TEST COMBAT CONTROLLER STUFF:
		 *  REMOVE WHEN DONE WITH ALL THAT
		 */
		else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			_gameScreen.enterCombat((List<Character>)new ArrayList<Character>());
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
					}
					else{
						newTile.setOccupant(_gameScreen.getMainChar());
					}
					_gameScreen.mapUpdate(0, -1);
					oldTile.removeOccupant();
				}
				break;
			case 'a':
				System.out.println("a");
				newTile = _gameScreen.getMap().getTile(currentX - 1, currentY);
				_gameScreen.getMainChar().setLeft();

				if(newTile != null && newTile.canMove(Map.EAST)){			
					if(newTile.hasEnterEvent())
						_gameScreen.pushControl(newTile.getEvent());
					else{
						newTile.setOccupant(_gameScreen.getMainChar());
					}
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
					}
					else{
						newTile.setOccupant(_gameScreen.getMainChar());
					}

					_gameScreen.mapUpdate(0, 1);
					oldTile.removeOccupant();
				}
				break;
			case 'd':
				System.out.println("d");
				newTile = _gameScreen.getMap().getTile(currentX + 1,  currentY);
				_gameScreen.getMainChar().setRight();
				if(newTile != null && newTile.canMove(Map.WEST)){
					if(newTile.hasEnterEvent())
						_gameScreen.pushControl(newTile.getEvent());

					else{
						newTile.setOccupant(_gameScreen.getMainChar());
					}
					_gameScreen.mapUpdate(1, 0);
					oldTile.removeOccupant();
				}
				break;
			case KeyEvent.VK_SPACE:
				System.out.println("Space");
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
			case 'f':
				//Easter Egg -- Hungry? Visit foodler
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
