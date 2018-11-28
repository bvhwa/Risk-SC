package gamelogic;

public abstract class Adjacencies {
	/*
	 * List of Territories
	 */
	private static String territories[] = {
			"Lyon Center",						//  0
			"Dedeaux Field",					//  1
			"John McKay Center",				//  2
			"Little Galen Center",				//  3
			"Marks Tennis Stadium",				//  4
			"Cromwell Field",					//  5
			
			"Kaprielian Hall",					//  6
			"Michelson Hall",					//  7
			"Seeley G. Mudd",					//  8
			"Hedco Neurosciences Building",		//  9
			"Stauffer Hall",					// 10
			"Zumberge Hall",					// 11

			"Olin Hall",						// 12
			"Tutor Hall",						// 13
			"Epstein Family Plaza",				// 14
			"Vivian Hall",						// 15
			"Salvatori Computer Science",		// 16
			"Seaver Science Library",			// 17

			"Leventhal School of Accounting",	// 18
			"Hoffman Hall",						// 19
			"Dauterive Hall",					// 20
			"Popovich Hall",					// 21
			"Fertitta Hall",					// 22

			"Norris Cinema Theatre",			// 23
			"SCB",								// 24
			"SCA",								// 25
			
			"Taper Hall",						// 26
			"Von KleinSmid Center",				// 27
			"Leavey Library",					// 28
			"Tutor Campus Center",				// 29
			"Tommy Trojan",						// 30
			"Alumni Park",						// 31
			"McCarthy Quad",					// 32
			"Doheny Library",					// 33
			
			"Annenberg Hall",					// 34
			"Annenberg School for Communication"// 35
	};
	
	
	/*
	 * Athletics Territories
	 */
	private static int lyonCenter[] = {1, 2, 24};
	private static int dedeauxField[] = {0, 2, 4};
	private static int mcKayCenter[] = {0, 1, 3, 4, 5, 24};
	private static int littleGalen[] = {4, 5, 23, 34, 35};
	private static int marksTennisStadium[] = {1, 2, 5, 6, 7};
	private static int cromwellField[] = {2, 3, 4, 8, 9, 34};

	/*
	 * Dornsife STEM Territories
	 */
	private static int kaprilean[] = {4, 7};
	private static int michelsonHall[] = {4, 6, 8, 12};
	private static int seeleyMudd[] = {5, 7, 9, 12};
	private static int hedcoNeuro[] = {5, 8, 10, 15, 34};
	private static int stauffer[] = {9, 11, 15, 29};
	private static int zumberge[] = {10, 18};

	/*
	 * Viterbi Territories
	 */
	private static int olin[] = {7, 8, 13, 14, 15};
	private static int tutorHall[] = {12, 14, 16};
	private static int eQuad[] = {12, 13, 15, 16, 17};
	private static int vivian[] = {9, 10, 12, 14, 17};
	private static int salvatori[] = {13, 14, 17};
	private static int seaverScienceLibrary[] = {14, 15, 16};

	/*
	 * Marshall Territories
	 */
	private static int leventhal[] = {11, 19, 20, 29, 30};
	private static int hoffman[] = {18, 20};
	private static int dauteriveHall[] = {18, 19, 21, 30, 33};
	private static int popovich[] = {20,22};
	private static int fertitaHall[] = {21};

	/*
	 * Cinematic Arts Territories
	 */
	private static int norrisCinema[] = {24, 25};
	private static int scb[] = {0, 2, 23, 25};
	private static int sca[] = {3, 23, 24, 35};

	/*
	 * Dornsife LAC Territories
	 */
	private static int taper[] = {27, 28, 35};
	private static int vonKleinsmid[] = {26, 28, 31, 32, 35};
	private static int leaveyLibrary[] = {26, 27, 32};
	private static int tutorCenter[] = {10, 18, 30, 34};
	private static int tommyTrojan[] = {18, 20, 29, 31, 34};
	private static int alumniPark[] = {27, 30, 32, 33};
	private static int mcCarthyQuad[] = {27, 28, 31, 33};
	private static int dohenyLibrary[] = {20, 31, 32};

	/*
	 * Annenberg Territories
	 */
	private static int wallisAnnenbergHall[] = {3, 5, 9, 29, 35};
	private static int annenbergSchool[] = {3, 25, 26, 27, 34};
	
	/*
	 * Total Adjacency List
	 */
	private static int adjacencyList[][] = {
			lyonCenter,
			dedeauxField,
			mcKayCenter,
			littleGalen,
			marksTennisStadium,
			cromwellField,
			
			kaprilean,
			michelsonHall,
			seeleyMudd,
			hedcoNeuro,
			stauffer,
			zumberge,
			
			olin,
			tutorHall,
			eQuad,
			vivian,
			salvatori,
			seaverScienceLibrary,
			
			leventhal,
			hoffman,
			dauteriveHall,
			popovich,
			fertitaHall,
			
			norrisCinema,
			scb,
			sca,
			
			taper,
			vonKleinsmid,
			leaveyLibrary,
			tutorCenter,
			tommyTrojan,
			alumniPark,
			mcCarthyQuad,
			dohenyLibrary,
			
			wallisAnnenbergHall,
			annenbergSchool
	};
	
	/*
	 * List of Continents
	 */
	private static int[] continents = {5, 11, 17, 22, 25, 32, 35};
	
	/**
	 * Grabs the array of territories with the index being the associated
	 * key to the value stored at that location. The aforementioned key will
	 * be used within the adjacencyList to determine if the country given by
	 * the value of the key contains an edge with a country of another given value
	 * 
	 * @return a string array of all the territories with the index being the territory id
	 */
	public static String[] getTerritories()	{
		return territories;
	}
	
	/**
	 * @param index the territory id
	 * @return the string name associated with that territory id
	 */
	public static String getTerritoryName(int index)	{
		return territories[index];
	}
	
	/**
	 * Returns the territory id of a given territory name
	 * @param name the name of the territory to search for
	 * @return the id associated with the territory of the given name, -1 if name not found
	 */
	public static int getTerritoryID(String name)	{
		for (int i = 0; i < territories.length; i++)	{
			if (territories[i].equals(name))
				return i;
		}
		return -1;
	}
	
	/**
	 * The row corresponds with the index of the country in territories
	 * The columns in those rows are the list of the countries it is adjacent to.
	 * 
	 * @return the list of adjacencies between multiple different countries
	 */
	public static int[][] getAdjacencyList()	{
		return adjacencyList;
	}
	
	/**
	 * List of Continents:
	 * 		A continent is the territories starting at the previous index plus one to the current index
	 * 		i.e. 	Athletics		0-5
	 * 				Dornsife STEM	6-11
	 * 				Viterbi			12-17
	 * 				Marshall		18-22
	 * 				Cinematic Arts	23-25
	 * 				Dornsife LAC	26-33
	 * 				Annenberg		34-35
	 * 
	 * 		If a player owns a continent, they earn a bonus of the same amount of troops as territories of the continent
	 * 
	 * @return the list of continents by their last territory number
	 */
	public static int[]	getContinents()	{
		return continents;
	}
}
