package event;

import graphics.MenuItem;
import graphics.Rectangle;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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

import javax.swing.JPanel;

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
	protected List<String> _phrases;
	protected List<Character> _characters;
	private MenuItem _background;
	private DialogueBox _db;
	private MenuItem _profile;
	private Integer _currIndex;

	/**
	 * Inner class used to paint a DialogueBox
	 */
	private class DialogueBox extends MenuItem{

		public DialogueBox(JPanel container, BufferedImage img,
				BufferedImage other, DoodleTactics dt,int priority) {
			super(container, img, other, dt,priority);
			System.out.println("INIT DIALOGUE");
			_background = new MenuItem(_gameScreen,img,img,_dt,10);
			_background.setVisible(true);
			//_background.setSize(10, 200);
			_background.setLocation(3, 625);

			_gameScreen.repaint();

		}

		@Override
		public void paint(java.awt.Graphics2D brush,BufferedImage img) {
			super.paint(brush, img);

			_background.paint(brush,img);
			paintNext(brush);
		}
	}

	private static final int CHARS_PER_LINE = 23;
	private static final int STRING_X = 450;
	private static final int STRING_Y = 665;
	private static final int LINE_OFFSET = 28;
	
	public static String formatString(String phrase) {
	
	/*	char[] chars = phrase.toCharArray();
		
		int j = 0;
		int best = 0;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == ' ') {
				best = i;
			}
			if (j == 24) {
				chars[best] = '\n';
				i = best;
				j = 0;
			}
			
			j++;
		}	*/
		String acc = "";
		String[] words = phrase.split(" ");
		int lineLength = 0;
		for (int i = 0; i < words.length; i++) {
			if (lineLength + words[i].length() < CHARS_PER_LINE) {
				acc += words[i] + " ";
				lineLength += words[i].length() + 1;
			}
			else {
				acc += "\n" + words[i] + " ";
				lineLength = words[i].length() + 1;
			}
		}
		return acc;
	//	return new String(chars);
	}

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
	
	/**
	 * Default Constructor (new game)
	 */
	public Dialogue(DoodleTactics dt,  String filename)
			throws InvalidEventException, IOException, FileNotFoundException{
		super(dt,false);
		_filename = filename;

	}
	
	/**
	 * Load game constructor
	 */
	public Dialogue(DoodleTactics dt,  String filename,boolean hasOccured)
	throws InvalidEventException, IOException, FileNotFoundException{
		super(dt,hasOccured);
		_filename = filename;
	}
	
	@Override
	public String save() {
		return "dialogue," + _filename + "," + Boolean.toString(this._hasOccurred);
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
		if(e.getKeyCode() == _dt.getInteractKey()){
			_currIndex++;
			_gameScreen.repaint();

			if(_currIndex >= _characters.size()){
				_gameScreen.popControl();
				return;
			}
						
			if(_phrases.get(_currIndex).startsWith("http://")){
				try {
					Runtime.getRuntime().exec("google-chrome " + _phrases.get(_currIndex));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		}
	}

	public void paintNext(Graphics2D brush) {
		BufferedImage profileImg = _characters.get(_currIndex).getProfileImage();
		_profile = new MenuItem(_gameScreen, profileImg,profileImg,_dt,5);
		_profile.setVisible(true);
		_profile.setSize(150, 150);
		_profile.setLocation(275,645);
		_profile.paint(brush,profileImg);

		brush.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		brush.setFont(new Font("M",Font.BOLD,25));
		brush.setColor(new Color(0,0,1));
		
		String[] lines = _phrases.get(_currIndex).split("\n");
		for (int i = 0; i < lines.length; i++)
			brush.drawString(lines[i], STRING_X, STRING_Y + i*LINE_OFFSET);
		
		
	//	brush.drawString(_phrases.get(_currIndex), 450,665);
		
		

	}

	@Override
	public void release() {
		if(_db != null)
			_gameScreen.removeMenuItem(_db);

		_gameScreen.removeMenuItem(_background);
	}

	@Override
	public void take() {
		_currIndex = 0;
		try {
			if(_phrases == null)
				parseMap();
			System.out.println("Start Dialogue!");
			BufferedImage img = _dt.importImage("src/graphics/menu/dialogue_box.png");

			_db = new DialogueBox(_gameScreen, img,img,_dt,5);
			_gameScreen.addMenuItem(_db);

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

		while(line != null){
			split = line.split(",");
			if(split.length != 2){
				throw new InvalidEventException(line);	
			}
			Character c;
			//Retrieve character from Hashmap
			if(allChars.containsKey(split[0].trim()))
				c = allChars.get(split[0].trim());
			else{
				//throw error if Character not found
				throw new InvalidEventException(split[0], line);
			}
//			_characters.add(c);

//			_phrases.add(split[1].trim());
			
			addPhrase(c, split[1].trim());
			
			line = br.readLine();
		}
	}
	
	/**
	 * adds a phrase to this dialogue
	 * @param c the character who speaks the phrase
	 * @param phrase the message spoken
	 */
	public void addPhrase(Character c, String phrase) {
		_characters.add(c);
		_phrases.add(formatString(phrase));
	}
}
