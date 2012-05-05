package main;

import items.HealthPotion;
import items.Item;
import items.ItemException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import controller.Controller;
import controller.combatController.player.PlayerCombatController;

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
	public static final int NUM_SAVE_OPTIONS = 4;

	
	private GameScreen _game;
	private GameMenuScreen _gameMenu;
	private MainMenuScreen _mainMenu;
	
	private Stack<Screen<? extends Controller>> _screens;
	private HashMap<String, Character> _allChars;
	private List<Character> _party;
	private HashMap<String,BufferedImage> _images;
	private HashMap<String,String> _savedGames;
	
	public DoodleTactics() {
		super("Doodle Tactics");
		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		this.setSize(TILE_COLS*map.Tile.TILE_SIZE+13,TILE_ROWS*map.Tile.TILE_SIZE+33);
		_screens = new Stack<Screen<?>>();
		_allChars = new HashMap<String,Character>();
		_images = new HashMap<String,BufferedImage>();
		_savedGames = new HashMap<String,String>();
		_party = new ArrayList<Character>();
		_gameMenu = new GameMenuScreen(this);
		_game = new GameScreen(this);
		_mainMenu = new MainMenuScreen(this);
		this.changeScreens(_mainMenu);
		this.setFocusable(false);
		this.setResizable(false);
		this.setVisible(true);
		
		
		//	_game.setMap("src/tests/data/testMapDemo");
		
		
		
	/*	Archer _char1 = new Archer(this,_game, 
				"src/graphics/characters/doodle_knight_portrait.png", "src/graphics/characters/warrior_left_color.png",
				"src/graphics/characters/warrior_right_color.png", "src/graphics/characters/warrior_back_color.png",
				"src/graphics/characters/warrior_front_color.png", "Dude", 10, 10);
		try {
			BufferedImage pot = importImage("src/graphics/items/donut.png");
			HealthPotion potion = new HealthPotion(pot, 10);
			potion.setDescription("Sup bro");
			_char1.addToInventory(potion);
			_char1.updateHP(-10);
		} catch (ItemException e) {
			e.printStackTrace();
		}
		try {
			BufferedImage pot2 = importImage("src/graphics/items/donut.png");
			HealthPotion pot = new HealthPotion(pot2, 10);
			pot.setDescription("This enchanted sprinkled doughnut heals your character for 10 HP.");
			_char1.addToInventory(pot);
		}  catch (ItemException e) {
			e.printStackTrace();
		}
		addCharacterToParty(_char1);
		
		addCharacterToParty(new Mage(this,_game,
				"src/graphics/characters/mage_portrait.png", "src/graphics/characters/mage_left.png",
				"src/graphics/characters/mage_right.png", "src/graphics/characters/mage_back.png",
				"src/graphics/characters/mage_front.png", "Whee", 5, 5));
		
		
		addCharacterToParty(new Thief(this,_game, 
				"src/graphics/characters/thief_portrait.png", "src/graphics/characters/thief_left.png",
				"src/graphics/characters/thief_right.png", "src/graphics/characters/thief_back.png",
				"src/graphics/characters/thief_front.png", "Shirley", 15, 15));
		
		addCharacterToParty(new Warrior(this,_game, 
				"src/graphics/characters/warrior_portrait.png", "src/graphics/characters/warrior_left_color.png",
				"src/graphics/characters/warrior_right_color.png", "src/graphics/characters/warrior_back_color.png",
				"src/graphics/characters/warrior_front_color.png", "Bob", 20, 10));*/
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
	public <T extends Controller> void setScreen(Screen<T> screen) {
		
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
			
		if (screen.getController() == null)
			screen.pushControl(screen.defaultController());
	}
	
	/**
	 * instructs a game screen to sleep; it is no longer the active part of the game
	 * @param screen the screen to temporarily shut down
	 */
	private <T extends Controller> void screenSleep(Screen<T> screen) {
		remove(screen);
		screen.setVisible(false);
		screen.setFocusable(false);
				
		repaint();
	}
	
	/**
	 * activates a dormant screen, setting it to the main game screen
	 * @param screen the screen to set to the forefront of gameplay
	 */
	private <T extends Controller> void screenActivate(Screen<T> screen) {
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
						
		repaint();
	}
	
	/**
	 * sets the screen to the given screen
	 * @param screen the new active screen of the game
	 */
	public <T extends Controller> void changeScreens(Screen<T> screen) {
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
	
	/**
	 * 
	 * Adds a Name/Character pair to the Character Map
	 */
	public void addCharacterToMap(Character c, String name){
		_allChars.put(name, c);
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
	public HashMap<String,String> getSavedFilePaths(){
		return _savedGames;
	}
	
	/**
	 * Called by any method which imports an image
	 * Maintains a HashMap of parsedImages to avoid reading in
	 * multiple images
	 */
	public BufferedImage importImage(String path){
		//Path already imported, simply return associated image
		if(_images.containsKey(path))
			return _images.get(path);
		//Otherwise read in image and add to HashMap
		else{
			try {
				BufferedImage img = ImageIO.read(new File(path));
				_images.put(path, img);
				return img;
			} catch (IOException e) {
				error("File " + path + " could not be parsed.");
				System.out.println("Returning Null");
				return null;

			}
		}
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
	
	/**
	 * Called when loading the game to reset the party
	 */
	public void setParty(List<Character> party){
		_party = party;
	}
		
	/**
	 * adds a character to the player's party
	 * @param c the character to add
	 * @author rroelke
	 */
	public void addCharacterToParty(Character c) {
		_party.add(c);
	}
	
	/**
	 * removes a character from the player's party
	 * @param c the character to remove
	 * @return whether or not the character was removed successfully
	 * @author rroelke
	 */
	public boolean removeCharacterFromParty(Character c) {
		boolean toReturn = _party.remove(c);
		if (!toReturn)
			return false;
		return true;
	}
	
	public boolean addSavedGame(String title,String filepath){
		if(_savedGames.size() >= NUM_SAVE_OPTIONS)
			return false;
		else{
			_savedGames.put(title,filepath);
			return true;
		}
			
	}

	/**
	 * indicates that an error has occurred and sets the game to an error screen
	 */
	public void error(String message) {
		changeScreens(new ErrorScreen(this, message));
	}
	
	
	public static void main(String[] args) {
		new DoodleTactics();
	}
}
