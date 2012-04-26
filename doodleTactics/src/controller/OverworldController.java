package controller;

import java.awt.Event;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import controller.combatController.RandomBattleAI;
import character.Character;

import main.DoodleTactics;
import main.GameScreen;
import map.Tile;

public class OverworldController extends GameScreenController {
	
	
	public OverworldController(DoodleTactics dt, GameScreen game) {
		super(dt);
		_gameScreen = game;
	}
	
	@Override
	public void take() {
		// TODO
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
		else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			_gameScreen.enterCombat((List<Character>)new ArrayList<Character>());
		}
	}

	@Override
	public void keyTyped(KeyEvent e) { 

	//	System.out.println("isAnimating:" + _gameScreen.isAnimating());
		
		if(!_gameScreen.isAnimating()) {			
			
			switch(e.getKeyChar()) {
			
			case 'w':
				System.out.println("w");
				_gameScreen.getMainChar().setUp();
				_gameScreen.mapUpdate(0, -1);
				break;
			case 'a':
				System.out.println("a");
				_gameScreen.getMainChar().setLeft();
				_gameScreen.mapUpdate(-1, 0);
				break;
			case 's':
				System.out.println("s");
				_gameScreen.getMainChar().setDown();
				_gameScreen.mapUpdate(0, 1);
				break;
			case 'd':
				System.out.println("d");
				_gameScreen.getMainChar().setRight();
				_gameScreen.mapUpdate(1, 0);
				break;
			}			
		}
	}
	
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
