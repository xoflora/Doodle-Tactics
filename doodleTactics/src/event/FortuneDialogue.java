package event;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Random;

import character.Character;

import main.DoodleTactics;

public class FortuneDialogue extends Dialogue {
	
	private static String[] CONFIRMATION_PHRASES =
	{"Nonetheless, so be it."};

	public FortuneDialogue(DoodleTactics dt, Character a, Character b)
			throws InvalidEventException, IOException, FileNotFoundException {
		super(dt, "");
		
		_phrases = new LinkedList<String>();
		_characters = new LinkedList<Character>();
		
		addPhrase(a, "What is it that compels you to attack me?\nI have done you no harm.");
		addPhrase(b, "You ask a question with a mysterious answer.");
		addPhrase(b, "Long ago, a fortune-teller instructed me:");
		
		Process fortune = Runtime.getRuntime().exec("fortune");
		BufferedReader r = new BufferedReader(new InputStreamReader(fortune.getInputStream()));
		
		String line = r.readLine();
		while (line != null) {
			addPhrase(b, line);
			line = r.readLine();
		}
		
		addPhrase(b, "...");
		addPhrase(b, "And this is why I must defeat you, stranger.");
		addPhrase(a, "That doesn't make any sense.");
		addPhrase(a, CONFIRMATION_PHRASES[new Random().nextInt(CONFIRMATION_PHRASES.length)]);
	}

}
