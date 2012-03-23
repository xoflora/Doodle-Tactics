package event;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import character.Character;

import main.DoodleTactics;


/**
 * 
 * @author czchapma
 * a dialogue instance controls a dialogue between characters
 */
public class DialogueBox extends Event {
	
	private List<String> _phrases;
	private List<Character> _characters;
	
	
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
	public DialogueBox(String filename, HashMap<String,Character> allChars) throws InvalidFileException, IOException, FileNotFoundException{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		 _phrases = new LinkedList<String>();
		 _characters = new LinkedList<Character>();
		String line = br.readLine();
		String[] split;
		while(line != null){
			split = line.split(",");
			if(split.length != 2)
				throw new InvalidFileException(line);
			
			Character c = allChars.get(split[0].trim());
			//throw error if Character not found
			if(c == null)
				throw new InvalidFileException(split[0], line);
			_characters.add(c);
			
			_phrases.add(split[1].trim());
			line = br.readLine();
		}
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
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public void display() {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
