package classes;

import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

public class Game {
	
	private Territory[] map;
	private User[] players;
	
	public Game(Vector<User> players)	{
		
		init(players);
	}
	
	/**
	 * Initialize the game based on the number of players
	 * @param playerNum
	 */
	private void init(Vector<User> players)	{
		initPlayers(players);
		initMap(players.size());
	}
	
	private void initPlayers(Vector <User> players)	{
		this.players = new User[players.size()];
		for (int i = 0; i < players.size(); i++)
			this.players[i] = players.elementAt(i);
	}
	
	/**
	 * Randomly initialize the map by uniformly distributing the players over the territories
	 * @param playerNum the number of players in the game
	 */
	private void initMap(int playerNum)	{
		
		this.map = new Territory[Adjacencies.getTerritories().length];
		
		int territoriesToAllocate[] = new int[playerNum];
		for (int i = 0; i < playerNum; i++)
			territoriesToAllocate[i] = this.map.length/playerNum;
		
		for (int i = 0; i < this.map.length; i++)	{
			int temp = (int) (Math.random()*playerNum);
			while (territoriesToAllocate[temp] == 0)
				temp = (int) (Math.random()*playerNum);
			
			this.map[i] = new Territory(Adjacencies.getTerritory(i), temp, (Math.random() < 0.5) ? 1 : 2);
			territoriesToAllocate[temp]--;
		}
		
		for (int i = 1; i < this.map.length; i++)	{
			// Select a random value below the current value
			int randIndex = (int) (Math.random()*i);
			
			// Swap the randomly selected value with one below it
			int temp = this.map[i].getOccupier();
			this.map[i].setOccupier(this.map[randIndex].getOccupier());
			this.map[randIndex].setOccupier(temp);
		}
	}
	
	/**
	 * 
	 * @param playerID
	 * @param territory
	 * @param troopNum
	 * @return true if place was successful, false otherwise
	 */
	private boolean place(int playerID, int territory, int troopNum)	{
		
		// Debug Statement
		System.out.println(this.players[playerID].getUserName() + " wants to place " + troopNum + " troops at " + this.map[territory].getName() + " which has " + this.map[territory].getTroops() + " troops");
		
		// Place the troops so long as the player places onto a self-controlled territory
		if (this.map[territory].getOccupier() != playerID)
			return false;
		
		// Updates the troops on the territory and belonging to the player
		this.map[territory].addTroops(troopNum);
		this.players[playerID].addTroops(troopNum);
		
		// Debug Statement
		System.out.println(this.map[territory].getName() + " now has " + this.map[territory].getTroops() + " troops");
		return true;
	}
	
	/**
	 * 
	 * @param attackPlayerID
	 * @param attackTerritory
	 * @param numAttack
	 * @param defendPlayerID
	 * @param defendTerritory
	 * @param numDefend
	 * @return true if the attack was successful, false otherwise
	 */
	private boolean attack(int attackPlayerID, int attackTerritory, int numAttack, int defendPlayerID, int defendTerritory, int numDefend)	{
		
		// Debug Statement
		System.out.println(this.players[attackPlayerID].getUserName() + " wants to attack from " + this.map[attackTerritory].getName() + " with " + numAttack + " troops");
		System.out.println(this.players[defendPlayerID].getUserName() + " wants to defend at " + this.map[defendTerritory].getName() + " with " + numDefend + " troops");
		
		// Not possible to attack the same player with itself
		if (attackPlayerID == defendPlayerID)
			return false;
		
		// Attacker cannot attack with more troops than strictly less than his current amount
		if (this.map[attackTerritory].getTroops() <= numAttack)
			return false;
		
		// Defender cannot defend with more troops than less than or equal to his current amount
		if (this.map[defendTerritory].getTroops() < numDefend)
			return false;

		// Not possible to attack from one territory to another if they are not adjacent
		if (!areNeighbors(attackTerritory, defendTerritory))
			return false;
		
		// Store the simulated dice rolls for each attacker and defender
		int attackValues[] = new int[numAttack];
		int defendValues[] = new int[numDefend];
		
		for (int i = 0; i < attackValues.length; i++)
			attackValues[i] = this.simulateDiceRoll();
		
		for (int i = 0; i < defendValues.length; i++)
			defendValues[i] = this.simulateDiceRoll();
		
		// Sort Arrays by increasing value
		Arrays.sort(attackValues); 
		Arrays.sort(defendValues);
		
		// Compare the last results of each array as many times as the length of the smaller array
		boolean result[] = new boolean[Math.min(attackValues.length, defendValues.length)];
		for (int i = 0; i < result.length; i++)	{
			if (attackValues[(attackValues.length - 1) - i] > defendValues[(attackValues.length - 1) - i])
				result[i] = true;
			else
				result[i] = false;
		}
		
		// If the attacker won a single skirmish, then remove a troop from the defense
		// Otherwise, the defender must have one, so remove a troop from the attacker
		
		int attackTroopsLost = 0, defendTroopsLost = 0;
		for (int i = 0; i < result.length; i++)	{
			if (result[i])	{
				this.map[defendTerritory].removeTroops();
				this.players[defendPlayerID].removeTroops();
				defendTroopsLost++;
			}
			else	{
				this.map[attackTerritory].removeTroops();
				this.players[attackPlayerID].removeTroops();
				attackTroopsLost++;
			}
		}
		
		// If the defender has no more troops in his territory, then the attacker has conquered
		if (this.map[defendTerritory].getTroops() <= 0)	{
			
			// Update the occupier of the previously defended territory
			this.map[defendTerritory].setOccupier(attackPlayerID);
			
			// Move the remaining offensive troops into the defended territory
			int attackTroopsRemaining = numAttack - attackTroopsLost;
			this.map[defendTerritory].setTroops(attackTroopsRemaining);
			this.map[attackTerritory].removeTroops(attackTroopsRemaining);
			
			// Update the territories each player has
			this.players[attackPlayerID].addTerritory();
			this.players[defendPlayerID].removeTerritory();
			
		}
		
		// Debug Statement
		System.out.println(this.players[attackPlayerID].getUserName() + " attacked from " + this.map[attackTerritory].getName() + " and now has " + this.map[attackTerritory].getTroops() + " troops");
		System.out.println(this.players[defendPlayerID].getUserName() + " defended at " + this.map[defendTerritory].getName() + " and now has " + this.map[defendTerritory].getTroops() + " troops");
		
		// Successfully attacked from the attackingTerritory to the defendingTerritory
		return true;		
	}
	
	/**
	 * 
	 * @param playerID
	 * @param moveFromTerritory
	 * @param moveToTerritory
	 * @param troops
	 * @return true if the move was successful, false otherwise
	 */
	private boolean move(int playerID, int moveFromTerritory, int moveToTerritory, int troops)	{
		
		// Debug Statement
		System.out.println(this.players[playerID].getUserName() + " wants to move " + troops + " troops from " + this.map[moveFromTerritory].getName() + " to " + this.map[moveToTerritory].getName());
		
		// You cannot move more troops than strictly less than the amount in the territory
		if (this.map[moveFromTerritory].getTroops() <= troops)
			return false;
		
		// Not possible to move from one territory to another if they are not adjacent
		if (!areNeighbors(moveFromTerritory, moveToTerritory))
			return false;
		
		// Move the troops from one territory to another
		this.map[moveFromTerritory].removeTroops(troops);
		this.map[moveToTerritory].addTroops(troops);
		
		// Debug Statement
		System.out.println(this.map[moveFromTerritory].getName() + " now has " + this.map[moveFromTerritory].getTroops() + " troops");
		System.out.println(this.map[moveToTerritory].getName() + " now has " + this.map[moveToTerritory].getTroops() + " troops");
		return true;
		
	}

	/**
	 * 
	 * @param territory
	 * @param neighbor
	 * @return true if territory and neighbor are adjacent, false otherwise
	 */
	private boolean areNeighbors(int territory, int neighbor)	{
		// Determine if the two territories are adjacent
		boolean adjacentTerritories = false;
		for (int t: Adjacencies.getAdjacencyList()[territory])
			if (t == neighbor)
				adjacentTerritories = true;
		
		return adjacentTerritories;
	}
	
	/**
	 * 
	 * @return a number between 1 to 6 uniformly randomly
	 */
	private int simulateDiceRoll()	{
		return (int) (Math.random()*6) + 1;
	}
	
	/**
	 * @return the map
	 */
	public Territory[] getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(Territory[] map) {
		this.map = map;
	}
	
	
}
