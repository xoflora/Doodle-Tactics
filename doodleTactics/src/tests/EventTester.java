package tests;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import event.*;
import org.junit.*;

import character.*;
import character.Character;

/**
 * 
 * @author czchapma
 * Tests the classes and methods of the package event
 */
public class EventTester {
	@BeforeClass
	public static void setUpClass() throws Exception {
		
	}
	
    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

	@Test
	/**
	 * tests parsing in files
	 */
	public void testDialogueParsing(){
		HashMap<String, Character> map = new HashMap<String,Character>();
		DialogueBox db;
		Archer a = new Archer(null, null, null, null, null, null, null,"archer");
		Mage m = new Mage(null, null, null, null, null, null, null,"mage");
		Thief t = new Thief(null, null, null, null, null, null, null,"thief");
		Warrior w = new Warrior(null, null, null, null, null, null, null,"warrior");
		
		//base case (no characters), working
		try{
			db = new DialogueBox(null, "src/tests/data/testDialogueEmpty", map);
			assert(db.getCharList().isEmpty());
			assert(db.getDialogueList().isEmpty());
		} catch(FileNotFoundException e){
			assert(false);
		} catch(IOException e){
			assert(false);
		} catch(InvalidFileException e){
			assert(false);
		}
		
		//1 character, working
		
		map.put("archer",a);
		LinkedList<Character> expectedCharList = new LinkedList<Character>();
		expectedCharList.add(a);
		LinkedList<String> expectedStringList = new LinkedList<String>();
		expectedStringList.add("i am an archer");
		try{
			db = new DialogueBox(null, "src/tests/data/testDialogue1", map);
			assert(db.getCharList().equals(expectedCharList));
			assert(db.getDialogueList().equals(expectedStringList));
		} catch(FileNotFoundException e){
			assert(false);
		} catch(IOException e){
			assert(false);
		} catch(InvalidFileException e){
			assert(false);
		}

		
		
		//general case (4 characters), working
		map.put("mage", m);
		map.put("thief", t);
		map.put("warrior", w);
		
		expectedCharList.add(m);
		expectedCharList.add(t);
		expectedCharList.add(w);
		
		expectedStringList.add("i am a mage");
		expectedStringList.add("i am a thief");
		expectedStringList.add("i am a warrior");

		try{
			db = new DialogueBox(null, "src/tests/data/testDialogue4", map);
			assert(db.getDialogueList().equals(expectedStringList));
			assert(db.getCharList().equals(expectedCharList));
		} catch(FileNotFoundException e){
			assert(false);
		} catch(IOException e){
			assert(false);
		} catch(InvalidFileException e){
			assert(false);
		}
		
		//failure case 1: file not found
		try{
			db = new DialogueBox(null, "src/tests/data/testNotFound", map);
			assert(false);
		} catch(FileNotFoundException e){
			assert(true);
		} catch (InvalidFileException e) {
			assert(false);
		} catch (IOException e) {
			assert(false);
		}
		
		//failure case 2: Character not found
		try{
			db = new DialogueBox(null, "src/tests/data/testDialogue2", map);
			assert(false);
		} catch(FileNotFoundException e){
			assert(false);
		} catch (InvalidFileException e) {
			assert(e.getMessage().equals("Character randomCharacter not found (line: randomCharacter, whatever)"));
		} catch (IOException e) {
			assert(false);
		}
		
		//failure case 3: invalid csv file
		try{
			db = new DialogueBox(null, "src/tests/data/testDialogue3", map);
			assert(false);
		} catch(FileNotFoundException e){
			assert(false);
		} catch (InvalidFileException e) {
			assert(e.getMessage().equals("\"too, many, commas\" did not parse correctly"));
		} catch (IOException e) {
			assert(false);
		}
	}

}
