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

			"Olin Hall",						// 13
			"Tutor Hall",						// 14
			"Epstein Family Plaza",				// 15
			"Vivian Hall",						// 16
			"Salvatori Computer Science",		// 17
			"Seaver Science Library",			// 18

			"Leventhal School of Accounting",	// 19
			"Hoffman Hall",						// 20
			"Dauterive Hall",					// 21
			"Popovich Hall",					// 22
			"Fertita Hall",						// 23

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
	private int michelsonHall[] = {4, 6, 8, 13};
	private int seeleyMudd[] = {5, 7, 9, 13};
	private int hedcoNeuro[] = {5, 8, 10, 16, 34};
	private int stauffer[] = {9, 11, 16, 29};
	private int zumberge[] = {10, 19};

	/*
	 * Viterbi Territories
	 */
	private int salvatori[] = {};
	private int vivian[] = {};
	private int tutorHall[] = {};
	private int olin[] = {};
	private int eQuad[] = {};
	private int seaverScienceLibrary[] = {};

	/*
	 * Marshall Territories
	 */
	private int fertitaHall[] = {};
	private int popovich[] = {};
	private int hoffman[] = {};
	private int leventhal[] = {};
	private int dauteriveHall[] = {};

	/*
	 * Cinematic Arts Territories
	 */
	private int sca[] = {};
	private int scb[] = {};
	private int norrisCinema[] = {};

	/*
	 * Dornsife LAC Territories
	 */
	private int tutorCenter[] = {};
	private int taper[] = {};
	private int vonKleinsmid[] = {};
	private int leaveyLibrary[] = {};
	private int dohenyLibrary[] = {};
	private int alumniPark[] = {};
	private int mcCarthyQuad[] = {};
	private int tommyTrojan[] = {};

	/*
	 * Annenberg Territories
	 */
	private int wallisAnnenbergHall[] = {};
	private int annenbergSchool[] = {};
}
