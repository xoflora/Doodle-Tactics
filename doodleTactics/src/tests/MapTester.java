package tests;

import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import org.junit.*;

import map.*;

/**
 * 
 * @author rroelke
 * tests the classes and methods of the map package
 */
public class MapTester {
	
	private static Map _test;
	
	public static boolean tileEquals(Tile t, int x, int y) {
		return t.x() == x && t.y() == y;
	}
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		try {
			JPanel panel = new JPanel();
			Tile[][] tiles = new Tile[32][32];
			for (int i = 0; i < tiles.length; i++)
				for (int j = 0; j < tiles[i].length; j++)
					tiles[i][j] = Tile.tile(panel, "src/graphics/tile.png", 'F', i, j, 1);
			_test = new Map(tiles, "TestMap");
			
			_test.getTile(7, 7).setCost(5);
			_test.getTile(6, 8).setCost(2);
			
			_test.getTile(20, 17).setCost(7);
			_test.getTile(21, 16).setCost(7);
			_test.getTile(19, 16).setCost(2);
			
			_test.getTile(24, 12).setCost(6);
			_test.getTile(24, 11).setCost(6);
			_test.getTile(24, 13).setCost(2);
			
			_test.getTile(24, 4).setTilePermissions('A');
			_test.getTile(25, 3).setTilePermissions('6');
			_test.getTile(25, 4).setTilePermissions('A');
			
			_test.getTile(24, 23).setTilePermissions('0');
			_test.getTile(24, 24).setTilePermissions('0');
			_test.getTile(24, 25).setTilePermissions('0');
			_test.getTile(25, 24).setTilePermissions('E');
			_test.getTile(25, 23).setTilePermissions('B');
			_test.getTile(24, 26).setCost(2);
			_test.getTile(25, 26).setCost(2);
			_test.getTile(25, 25).setCost(2);
			_test.getTile(22, 23).setCost(2);
			
		} catch(InvalidTileException e) {
			assert(false);
		}
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
     * tests the map's distance estimation
     */
    public void testDistanceCalc() {
    	assert(_test.estimateDistance(_test.getTile(0, 0), _test.getTile(0, 0)) == 0);
    	assert(_test.estimateDistance(_test.getTile(0, 0), _test.getTile(0, 5)) == 5);
    	assert(_test.estimateDistance(_test.getTile(0, 0), _test.getTile(5, 0)) == 5);
    	assert(_test.estimateDistance(_test.getTile(0, 0), _test.getTile(5, 5)) == 10);
    	assert(_test.estimateDistance(_test.getTile(5, 5), _test.getTile(0, 0)) == 10);
    	assert(_test.estimateDistance(_test.getTile(5, 0), _test.getTile(0, 5)) == 10);
    	assert(_test.estimateDistance(_test.getTile(0, 5), _test.getTile(5, 0)) == 10);
    	assert(_test.estimateDistance(_test.getTile(8, 8), _test.getTile(0, 5)) == 11);
    	assert(_test.estimateDistance(_test.getTile(8, 8), _test.getTile(12, 5)) == 7);
    	assert(_test.estimateDistance(_test.getTile(8, 8), _test.getTile(0, 11)) == 11);
    	assert(_test.estimateDistance(_test.getTile(8, 8), _test.getTile(12, 11)) == 7);
    }

	@Test
	/**
	 * tests the Map's pathfinding capabilities
	 */
	public void testPathFinding() {
		
		/*
		 * failing cases - no path exists
		 */
		List<Tile> path = _test.getPath(_test.getTile(-1, 0), _test.getTile(0, 0));
		assert(path == null);
		
		/*
		 * trivial test cases - test a straight-line path with no obstructions
		 */
		path = _test.getPath(_test.getTile(0, 0), _test.getTile(0, 1));
		assert(path.size() == 2);
		assert(tileEquals(path.get(0), 0, 0));
		assert(tileEquals(path.get(1), 0, 1));
		
		path = _test.getPath(_test.getTile(0, 0), _test.getTile(1, 0));
		assert(path.size() == 2);
		assert(tileEquals(path.get(0), 0, 0));
		assert(tileEquals(path.get(1), 1, 0));
		
		path = _test.getPath(_test.getTile(0, 0), _test.getTile(0, 10));
		assert(path.size() == 11);
		for (int i = 0; i < path.size(); i++)
			assert(tileEquals(path.get(i), 0, i));
		
		path = _test.getPath(_test.getTile(0, 0), _test.getTile(10, 0));
		assert(path.size() == 11);
		for (int i = 0; i < path.size(); i++)
			assert(tileEquals(path.get(i), i, 0));
		
		/*
		 * tests a nonlinear path with no obstructions
		 */
		path = _test.getPath(_test.getTile(0, 0), _test.getTile(5, 5));
		assert(path.size() == 11);
		path = _test.getPath(_test.getTile(5, 5), _test.getTile(0, 0));
		assert(path.size() == 11);
		
		/*
		 * tests paths with movement-cost obstructions
		 */	
		path = _test.getPath(_test.getTile(6, 7), _test.getTile(8, 7));
		assert(path.size() == 5);
		assert(tileEquals(path.get(0), 6, 7));
		assert(tileEquals(path.get(1), 6, 6));
		assert(tileEquals(path.get(2), 7, 6));
		assert(tileEquals(path.get(3), 8, 6));
		assert(tileEquals(path.get(4), 8, 7));
		
		path = _test.getPath(_test.getTile(20, 16), _test.getTile(21, 17));
		assert(path.size() == 7);
		assert(tileEquals(path.get(0), 20, 16));
		assert(tileEquals(path.get(1), 20, 15));
		assert(tileEquals(path.get(2), 21, 15));
		assert(tileEquals(path.get(3), 22, 15));
		assert(tileEquals(path.get(4), 22, 16));
		assert(tileEquals(path.get(5), 22, 17));
		assert(tileEquals(path.get(6), 21, 17));
		
		path = _test.getPath(_test.getTile(27, 11), _test.getTile(23, 11));
		assert(path.size() == 7);
		assert(tileEquals(path.get(0), 27, 11));
		assert(tileEquals(path.get(1), 26, 11));
		assert(tileEquals(path.get(2), 25, 11));
		assert(tileEquals(path.get(3), 25, 10));
		assert(tileEquals(path.get(4), 24, 10));
		assert(tileEquals(path.get(5), 23, 10));
		assert(tileEquals(path.get(6), 23, 11));
		
		/*
		 * tests paths moving through movement-cost obstructions
		 */
		path = _test.getPath(_test.getTile(25, 13), _test.getTile(23, 11));
		assert(path.size() == 5);
		assert(tileEquals(path.get(0), 25, 13));
		assert(tileEquals(path.get(1), 24, 13));
		assert(tileEquals(path.get(2), 23, 13));
		assert(tileEquals(path.get(3), 23, 12));
		assert(tileEquals(path.get(4), 23, 11));
		
		/*
		 * tests paths moving around movement-permission constraints
		 */
		path = _test.getPath(_test.getTile(24, 3), _test.getTile(24, 4));
		assert(path.size() == 4);
		assert(tileEquals(path.get(0), 24, 3));
		assert(tileEquals(path.get(1), 23, 3));
		assert(tileEquals(path.get(2), 23, 4));
		assert(tileEquals(path.get(3), 24, 4));
		
		path = _test.getPath(_test.getTile(24, 3), _test.getTile(25, 4));
		assert(path.size() == 5);
		assert(tileEquals(path.get(0), 24, 3));
		assert(tileEquals(path.get(1), 23, 3));
		assert(tileEquals(path.get(2), 23, 4));
		assert(tileEquals(path.get(3), 24, 4));
		assert(tileEquals(path.get(4), 25, 4));
		
		path = _test.getPath(_test.getTile(24, 3), _test.getTile(25, 3));
		assert(path.size() == 6);
		assert(tileEquals(path.get(0), 24, 3));
		assert(tileEquals(path.get(1), 24, 2));
		assert(tileEquals(path.get(2), 25, 2));
		assert(tileEquals(path.get(3), 26, 2));
		assert(tileEquals(path.get(4), 26, 3));
		assert(tileEquals(path.get(5), 25, 3));
		
		/*
		 * tests paths with mixed permission and cost constraints
		 */
		path = _test.getPath(_test.getTile(22, 24), _test.getTile(25, 24));
		assert(path.size() == 10);
		assert(tileEquals(path.get(0), 22, 24));
		assert(tileEquals(path.get(1), 23, 24));
		assert(tileEquals(path.get(2), 23, 23));
		assert(tileEquals(path.get(3), 23, 22));
		assert(tileEquals(path.get(4), 24, 22));
		assert(tileEquals(path.get(5), 25, 22));
		assert(tileEquals(path.get(6), 25, 23));
		assert(tileEquals(path.get(7), 26, 23));
		assert(tileEquals(path.get(8), 26, 24));
		assert(tileEquals(path.get(9), 25, 24));
		
		path = _test.getPath(_test.getTile(23, 25), _test.getTile(25, 24));
		assert(path.size() == 6);
		assert(tileEquals(path.get(0), 23, 25));
		assert(tileEquals(path.get(1), 23, 26));
		assert(tileEquals(path.get(2), 24, 26));
		assert(tileEquals(path.get(3), 25, 26));
		assert(tileEquals(path.get(4), 25, 25));
		assert(tileEquals(path.get(5), 25, 24));
		
		path = _test.getPath(_test.getTile(25, 24), _test.getTile(23, 25));
		assert(path.size() == 6);
		assert(tileEquals(path.get(5), 23, 25));
		assert(tileEquals(path.get(4), 23, 26));
		assert(tileEquals(path.get(3), 24, 26));
		assert(tileEquals(path.get(2), 25, 26));
		assert(tileEquals(path.get(1), 25, 25));
		assert(tileEquals(path.get(0), 25, 24));

		System.out.println(_test);

	/*	try {
			BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
			String input = r.readLine();
			String[] coordinates;
			while (input != null && !input.equals("quit")) {
				try {
					if (input.equals("print map")) {
						System.out.println(_test);
					}
					else {
						coordinates = input.split(" ");
						path = _test.getPath(_test.getTile(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])),
								_test.getTile(Integer.parseInt(coordinates[2]), Integer.parseInt(coordinates[3])));
						if (path == null)
							System.out.println("No path.");
						else {
							for (int i = 0; i < path.size(); i++)
								System.out.println("Node " + i + ": " + path.get(i).x() + ", " + path.get(i).y());
						}
					}
					
				} catch(Exception e) {
					System.out.println("Input error.");
				} finally {
					System.out.println();
					input = r.readLine();
				}
			}
		} catch(IOException e) {

		}       */

	}
	
	
	@Test
	/**
	 * tests movement range generation of the map
	 */
	public void testMovementRange() {
		/*
		 * trivial test cases - 0 range
		 */
		List<Tile> range = _test.getMovementRange(_test.getTile(0, 0), 0);
		assert(range.size() == 1);
		assert(tileEquals(range.get(0), 0, 0));
		
		range = _test.getMovementRange(_test.getTile(1, 1), 0);
		assert(range.size() == 1);
		assert(tileEquals(range.get(0), 1, 1));
		
		/*
		 * test cases with no movement impediments
		 */
		List<Tile> compare = new LinkedList<Tile>();
		compare.add(_test.getTile(1, 1));	compare.add(_test.getTile(1, 0));
		compare.add(_test.getTile(0, 1));	compare.add(_test.getTile(2, 1));
		compare.add(_test.getTile(1, 2));
		range = _test.getMovementRange(_test.getTile(1, 1), 1);
		assert(compare.size() == range.size() && compare.containsAll(range));
		
		compare = new LinkedList<Tile>();
		compare.add(_test.getTile(0, 0));	compare.add(_test.getTile(1, 0));
		compare.add(_test.getTile(0, 1));	compare.add(_test.getTile(2, 0));
		compare.add(_test.getTile(0, 2));	compare.add(_test.getTile(3, 0));
		compare.add(_test.getTile(0, 3));
		compare.add(_test.getTile(1, 1));	compare.add(_test.getTile(2, 1));
		compare.add(_test.getTile(1, 2));
		range = _test.getMovementRange(_test.getTile(0, 0), 3);
		assert(compare.size() == range.size() && compare.containsAll(range));
		
		compare = new LinkedList<Tile>();
		compare.add(_test.getTile(0, 4));
		compare.add(_test.getTile(1, 3));	compare.add(_test.getTile(1, 4));	compare.add(_test.getTile(1, 5));
		compare.add(_test.getTile(2, 2));	compare.add(_test.getTile(2, 3));	compare.add(_test.getTile(2, 4));
											compare.add(_test.getTile(2, 5));	compare.add(_test.getTile(2, 6));
		compare.add(_test.getTile(3, 1));	compare.add(_test.getTile(3, 2));	compare.add(_test.getTile(3, 3));
											compare.add(_test.getTile(3, 4));	compare.add(_test.getTile(3, 5));
											compare.add(_test.getTile(3, 6));	compare.add(_test.getTile(3, 7));
		compare.add(_test.getTile(4, 0));	compare.add(_test.getTile(4, 1));	compare.add(_test.getTile(4, 2));
											compare.add(_test.getTile(4, 3));	compare.add(_test.getTile(4, 4));
											compare.add(_test.getTile(4, 5));	compare.add(_test.getTile(4, 6));
											compare.add(_test.getTile(4, 7));	compare.add(_test.getTile(4, 8));
		compare.add(_test.getTile(5, 1));	compare.add(_test.getTile(5, 2));	compare.add(_test.getTile(5, 3));
											compare.add(_test.getTile(5, 4));	compare.add(_test.getTile(5, 5));
											compare.add(_test.getTile(5, 6));	compare.add(_test.getTile(5, 7));
		compare.add(_test.getTile(6, 2));	compare.add(_test.getTile(6, 3));	compare.add(_test.getTile(6, 4));
											compare.add(_test.getTile(6, 5));	compare.add(_test.getTile(6, 6));
		compare.add(_test.getTile(7, 3));	compare.add(_test.getTile(7, 4));	compare.add(_test.getTile(7, 5));
		compare.add(_test.getTile(8, 4));
										
		range = _test.getMovementRange(_test.getTile(4, 4), 4);
		assert(compare.size() == range.size() && compare.containsAll(range));
		
		
		/*
		 * test cases with a cost impediment
		 */
		compare = new LinkedList<Tile>();
		compare.add(_test.getTile(7, 8));	compare.add(_test.getTile(7, 9));
		compare.add(_test.getTile(8, 8));
		range = _test.getMovementRange(_test.getTile(7, 8), 1);
		assert(compare.size() == range.size() && compare.containsAll(range));
		
		compare = new LinkedList<Tile>();
		compare.add(_test.getTile(7, 8));	compare.add(_test.getTile(7, 9));	compare.add(_test.getTile(7, 10));
		compare.add(_test.getTile(6, 8));	compare.add(_test.getTile(6, 9));
		compare.add(_test.getTile(8, 8));	compare.add(_test.getTile(8, 9));	compare.add(_test.getTile(8, 7));
		compare.add(_test.getTile(9, 8));
		range = _test.getMovementRange(_test.getTile(7, 8), 2);
	//	System.out.println(range.size() + " " + compare.size());
	//		for (int i = 0; i < range.size(); i++)
	//			System.out.println("Path " + i + ": " + range.get(i).x() + ", " + range.get(i).y());
		assert(compare.size() == range.size() && compare.containsAll(range));
		
		compare = new LinkedList<Tile>();
		compare.add(_test.getTile(7, 8));	compare.add(_test.getTile(7, 9));	compare.add(_test.getTile(7, 10));
											compare.add(_test.getTile(6, 9));	compare.add(_test.getTile(6, 10));
											compare.add(_test.getTile(8, 9));	compare.add(_test.getTile(8, 10));
											compare.add(_test.getTile(5, 9));	compare.add(_test.getTile(9, 9));
		compare.add(_test.getTile(6, 8));	compare.add(_test.getTile(6, 7));	compare.add(_test.getTile(5, 8));
		compare.add(_test.getTile(8, 8));	compare.add(_test.getTile(9, 8));	compare.add(_test.getTile(8, 7));
											compare.add(_test.getTile(10, 8));	compare.add(_test.getTile(9, 7));
											compare.add(_test.getTile(8, 6));	compare.add(_test.getTile(7, 11));
		range = _test.getMovementRange(_test.getTile(7, 8), 3);
		assert(compare.size() == range.size() && compare.containsAll(range));
		
		
		/*
		 * test ranges with movement-permission constraints
		 */
		
	}
}
