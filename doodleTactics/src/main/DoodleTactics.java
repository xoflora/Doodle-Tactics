package main;

import items.HealthPotion;
import items.Item;
import items.ItemException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import controller.Controller;

import character.Archer;
import character.Character;
import character.Mage;
import character.Thief;
import character.Warrior;

/**
 * 
 * @author rroelke
 * the main class; orchestrates a DoodleTactics game session
 */
public class DoodleTactics extends JFrame {
	
	public static final int TILE_ROWS = 17;
	public static final int TILE_COLS = 21;
	
	private GameScreen _game;
	private GameMenuScreen _gameMenu;
	private MainMenuScreen _mainMenu;
	
	private Stack<Screen<?>> _screens;
	private HashMap<String, Character> _allChars;
	private List<Character> _party;
	
	public DoodleTactics() {
		super("Doodle Tactics");
		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		this.setSize(TILE_COLS*map.Tile.TILE_SIZE+13,TILE_ROWS*map.Tile.TILE_SIZE+33);
		
		_gameMenu = new GameMenuScreen(this);
		_game = new GameScreen(this);
		_mainMenu = new MainMenuScreen(this);
		
		_screens = new Stack<Screen<?>>();
		_party = new ArrayList<Character>();
		
		this.changeScreens(_mainMenu);
		this.setFocusable(false);
		this.setResizable(false);
		this.setVisible(true);
		
		Archer _char1 = new Archer(_game, "src/graphics/characters/pokeball.png",
				"src/graphics/characters/pokeball.png", "src/graphics/characters/warrior_back.png",
				"src/graphics/characters/warrior_back.png", "src/graphics/characters/warrior_back.png",
				"src/graphics/characters/warrior_back.png", "src/graphics/characters/warrior_back.png", 10, 10);
		_party.add(_char1);
		BufferedImage pot;
		try {
			pot = ImageIO.read(new File("src/graphics/characters/warrior_left.png"));
			_char1.addToInventory(new HealthPotion(pot, 10));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ItemException e) {
			e.printStackTrace();
		}
		
		_party.add(new Mage(_game, "src/graphics/characters/pokeball.png",
				"src/graphics/characters/pokeball.png", "src/graphics/characters/warrior_front.png",
				"src/graphics/characters/warrior_front.png", "src/graphics/characters/warrior_front.png",
				"src/graphics/characters/warrior_front.png", "src/graphics/characters/warrior_front.png", 5, 5));
		
		_party.add(new Thief(_game, "src/graphics/characters/pokeball.png",
				"src/graphics/characters/pokeball.png", "src/graphics/characters/warrior_front.png",
				"src/graphics/characters/warrior_front.png", "src/graphics/characters/warrior_front.png",
				"src/graphics/characters/warrior_front.png", "src/graphics/characters/warrior_front.png", 15, 15));
		
		_party.add(new Warrior(_game, "src/graphics/characters/pokeball.png",
				"src/graphics/characters/pokeball.png", "src/graphics/characters/warrior_front.png",
				"src/graphics/characters/warrior_front.png", "src/graphics/characters/warrior_front.png",
				"src/graphics/characters/warrior_front.png", "src/graphics/characters/warrior_front.png", 20, 10));

	}
	
	/**
	 * @return the current screen of the game
	 */
	public Screen<? extends Controller> currentScreen() {
		try {
			return _screens.peek();
		} catch(EmptyStackException e) {
			return null;
		}
	}
	
	/** 
	 * @param screen, the new screen for the game
	 */
	public void setScreen(Screen screen) {
		
		/* Check that the current screen is not null before
		 * removing */
		if(currentScreen() != null) {
			this.remove(currentScreen());
			screen.setVisible(false);
			currentScreen().setFocusable(false);
		}
		//	currentScreen() = screen;
		_screens.push(screen);
			screen.setFocusable(true);
			this.add(screen);
			screen.setVisible(true);
			this.repaint();
			screen.grabFocus();
	}
	
	/**
	 * instructs a game screen to sleep; it is no longer the active part of the game
	 * @param screen the screen to temporarily shut down
	 */
	private void screenSleep(Screen<? extends Controller> screen) {
		remove(screen);
		screen.setVisible(false);
		screen.setFocusable(false);
		
		System.out.println("hello " + (screen == _game));
		
		repaint();
	}
	
	/**
	 * activates a dormant screen, setting it to the main game screen
	 * @param screen the screen to set to the forefront of gameplay
	 */
	private void screenActivate(Screen<? extends Controller> screen) {
	/*	screen.setVisible(true);
		screen.setFocusable(true);
		repaint();
		this.add(screen);

		screen.grabFocus();		*/
		
		screen.setFocusable(true);
		this.add(screen);
		screen.setVisible(true);
		this.repaint();
		screen.grabFocus();
		
		System.out.println("wertwer " + (screen == _game));
				
		repaint();
	}
	
	/**
	 * sets the screen to the given screen
	 * @param screen the new active screen of the game
	 */
	public void changeScreens(Screen<? extends Controller> screen) {
	/*	if (currentScreen() != null) {
			System.out.println("sellpgin " + (screen == currentScreen()));
			screenSleep(currentScreen());
		}
		
		_screens.push(screen);
		screenActivate(screen);	*/
		setScreen(screen);
	}
	
	/**
	 * reverts the game back to the previous screen
	 * @return the previous game screen
	 */
	public Screen<? extends Controller> revertScreen() {
		Screen<? extends Controller> toReturn = _screens.pop();
		screenSleep(toReturn);
		screenActivate(_screens.peek());
				
		return toReturn;
	}
	
	/**
	 * @author czchapma
	 * @return _allChars, the HashMap mapping String to Character name
	 */
	public HashMap<String,Character> getCharacterMap(){
		return _allChars;
	}
	
	public GameScreen getGameScreen() {
		return _game;
	}
	
	public GameMenuScreen getGameMenuScreen() {
		return _gameMenu;
	}
	
	public MainMenuScreen getMainMenuScreen() {
		return _mainMenu;
	}
	
	/**
	 * @return a list of all the units currently in the game
	 */
	public List<Character> characterList() {
		List<Character> toReturn = new ArrayList<Character>();
		
		for (String s : _allChars.keySet())
			toReturn.add(_allChars.get(s));
		
		return toReturn;
	}
	
	/**
	 * @return the player's party of units
	 */
	public List<Character> getParty() {
		return _party;
	}
	
	public static void main(String[] args) {
		new DoodleTactics();
	}
	
}
