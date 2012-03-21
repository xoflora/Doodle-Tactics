package event;

import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author rroelke
 * a dialogue instance controls a dialogue between characters
 */
public class Dialogue extends Event {
	
	List<String> _phrases;
	List<Character> _characters;
	
	public Dialogue(){
		_phrases = new ArrayList<String>();
		_characters = new ArrayList<Character>();
	}
	public static Dialogue dialogue(String filename) {
		return null;
	}
	
	/**
	 * parses a dialogue file based on the following rules
	 *  
	 * @param filename - the name of the file in src/character
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void parse(String filename) throws FileNotFoundException, IOException{
		BufferedReader br;
		br = new BufferedReader(new FileReader("src/character/" + filename));
		String line = br.readLine();
		while(line != null){
			//TODO fill in
			line = br.readLine();
		}
		
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
}
