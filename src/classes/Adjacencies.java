package classes;

public abstract class Adjacencies {
	/*
	 * List of Territories
	 */
	private String territories[] = {
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
			"Fertita Hall",						// 22

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
	private int lyonCenter[] = {1, 2, 24};
	private int dedeauxField[] = {0, 2, 4};
	private int mcKayCenter[] = {0, 1, 3, 4, 5, 24};
	private int littleGalen[] = {4, 5, 23, 34, 35};
	private int marksTennisStadium[] = {1, 2, 5, 6, 7};
	private int cromwellField[] = {2, 3, 4, 8, 9, 34};

	/*
	 * Dornsife STEM Territories
	 */
	private int kaprilean[] = {4, 7};
	private int michelsonHall[] = {4, 6, 8, 12};
	private int seeleyMudd[] = {5, 7, 9, 12};
	private int hedcoNeuro[] = {5, 8, 10, 15, 34};
	private int stauffer[] = {9, 11, 15, 29};
	private int zumberge[] = {10, 18};

	/*
	 * Viterbi Territories
	 */
	private int olin[] = {7, 8, 13, 14, 15};
	private int tutorHall[] = {12, 14, 16};
	private int eQuad[] = {12, 13, 15, 16, 17};
	private int vivian[] = {9, 10, 12, 14, 17};
	private int salvatori[] = {13, 14, 17};
	private int seaverScienceLibrary[] = {14, 15, 16};

	/*
	 * Marshall Territories
	 */
	private int leventhal[] = {11, 19, 20, 29, 30};
	private int hoffman[] = {18, 20};
	private int dauteriveHall[] = {18, 19, 21, 30, 33};
	private int popovich[] = {20,22};
	private int fertitaHall[] = {21};

	/*
	 * Cinematic Arts Territories
	 */
	private int norrisCinema[] = {24, 25};
	private int scb[] = {0, 2, 23, 25};
	private int sca[] = {3, 23, 24, 35};

	/*
	 * Dornsife LAC Territories
	 */
	private int taper[] = {27, 28, 35};
	private int vonKleinsmid[] = {26, 28, 31, 32, 35};
	private int leaveyLibrary[] = {26, 27, 32};
	private int tutorCenter[] = {10, 18, 30, 34};
	private int tommyTrojan[] = {18, 20, 29, 31, 34};
	private int alumniPark[] = {27, 30, 32, 33};
	private int mcCarthyQuad[] = {27, 28, 31, 33};
	private int dohenyLibrary[] = {20, 31, 32};

	/*
	 * Annenberg Territories
	 */
	private int wallisAnnenbergHall[] = {3, 5, 9, 29, 35};
	private int annenbergSchool[] = {3, 25, 26, 27, 34};
}
