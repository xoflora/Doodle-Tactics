package event;

import graphics.MenuItem;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import character.Archer;
import character.Character;

import main.DoodleTactics;


/**
 * 
 * @author czchapma
 * a dialogue instance controls a dialogue between characters
 */
public class Dialogue extends Event {
	
	private String _filename;
	private List<String> _phrases;
	private List<Character> _characters;
	private MenuItem _dialogueBox;
	private MenuItem _profile;
	private int _currIndex;
	/**
	 * Creates a DialogueBox by parsing a dialogue csv file in the following format
	 * name, phrase 1
	 * name2, phrase 2
	 * 
	 * @param filename - the name of the file in src/character
	 * @param allChars - a HashMap mapping name to Character
	 * @throws InvalidFileException --if something goes wrong during csv file parsing
	 * @throws IOException, FileNotFoundException 
	 */
	public Dialogue(DoodleTactics dt,  String filename)
			throws InvalidEventException, IOException, FileNotFoundException{
		super(dt);
		_filename = filename;
	}

	public void print(){
		for(int i=0; i<_phrases.size(); i++){
			System.out.println(_characters.get(i).getName() + ": " + _phrases.get(i));
		}
	}

	//getters
	public List<Character> getCharList(){
		return _characters;
	}

	public List<String> getDialogueList(){
		return _phrases;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()){
		case KeyEvent.VK_UP:
			//handle up
			System.out.println("UP");
			break;
		case KeyEvent.VK_DOWN:
			//handle down
			System.out.println("DOWN");
			_currIndex++;
			if(_currIndex >= _characters.size())
				_gameScreen.popControl();
			else
				paintNext();
			break;
		}
	}
	
	public void paintNext(){
		if(_profile != null){
			_gameScreen.removeMenuItem(_profile);
			_profile.setVisible(false);
		}

		BufferedImage profileImg = _characters.get(_currIndex).getProfileImage();
		_profile = new MenuItem(_gameScreen, profileImg,profileImg,_dt,5);
		_profile.setVisible(true);
		_profile.setSize(150, 150);
		_profile.setLocation(230,635);
		_gameScreen.addMenuItem(_profile);
		_gameScreen.repaint();

	}


	@Override
	public void release() {
		if(_profile != null)
			_gameScreen.removeMenuItem(_profile);

		_gameScreen.removeMenuItem(_dialogueBox);
	}

	@Override
	public void take() {
		_currIndex = 0;
		try {
			if(_phrases == null)
				parseMap();
			System.out.println("Start Dialogue!");
			BufferedImage img;

			img = ImageIO.read(new File("src/graphics/menu/dialogue_box.jpg"));
			_dialogueBox = new MenuItem(_gameScreen,img,img,_dt,0);
			_dialogueBox.setVisible(true);
			_dialogueBox.setSize(700, 200);
			_dialogueBox.setLocation(185,620);
			_gameScreen.addMenuItem(_dialogueBox);
			paintNext();
			
		} catch (IOException e) {
			System.out.println("Invalid Dialogue Box file");
		} catch (InvalidEventException e) {
			System.out.println("Invalid Event");
		}
		
//		_gameScreen.popControl();
		//display Dialogue box and start 		
	}
	
	public void parseMap() throws InvalidEventException,IOException{
		_phrases = new LinkedList<String>();
		_characters = new LinkedList<Character>();

		BufferedReader br = new BufferedReader(new FileReader(_filename));
		String line = br.readLine();
		String[] split;
		HashMap<String,Character> allChars = _dt.getCharacterMap();
		System.out.println("ALL CHARS");
		for(String s : allChars.keySet()){
			System.out.println(s);
		}
		
		//REMOVE LATER:
	/*\	allChars.put("Thighs",new Archer(dt.getGameScreen(), 
				"src/graphics/characters/pokeball.png", "src/graphics/characters/warrior_back.png",
				"src/graphics/characters/warrior_back.png", "src/graphics/characters/warrior_back.png",
				"src/graphics/characters/warrior_back.png", "Dude", 10, 10));
		allChars.put("Renoir", new Archer(dt.getGameScreen(), 
				"src/graphics/characters/sampleProfile.gif", "src/graphics/characters/warrior_back.png",
				"src/graphics/characters/warrior_back.png", "src/graphics/characters/warrior_back.png",
				"src/graphics/characters/warrior_back.png", "Dude", 10, 10));*/
		
		while(line != null){
			split = line.split(",");
			if(split.length != 2){
				throw new InvalidEventException(line);	
			}
			Character c;
			//Retrieve character from Hashmap
			if(allChars.containsKey(split[0].trim()))
				c = allChars.get(split[0].trim());
			else
				//throw error if Character not found
				throw new InvalidEventException(split[0], line);
			_characters.add(c);

			_phrases.add(split[1].trim());
			line = br.readLine();
		}

	}


}
