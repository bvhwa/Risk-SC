package gamelogic;

import java.util.Vector;

public class Test {
	
	public static void main(String args[])	{
		
		String firstNames[] = {"Rohin", "Michael", "Amar", "Fariz"};
		String lastNames[] = {"Prabhakaran", "Eberhardt", "Khokar", "Attamimi"};
		String userNames[] = {"bulbasaur", "charmander", "squirtle", "pikachu"};
		
		int playerNum = (int) (Math.random()*3) + 2;
		System.out.println("There are " + playerNum + " players.");
		
		Vector<Player> players = new Vector<Player>();
		for (int i = 0; i < playerNum; i++)
			players.add(new Player(i, firstNames[i], lastNames[i], userNames[i], i == 0));

		GameLogic g = new GameLogic(players);
		
		int count = 1;
		for (Territory t: g.getTerritoryMap())	{
			System.out.println(count + ". " + ((count++ < 10) ? " ": "") + g.getPlayers()[t.getOccupier()] + ": " + t);
		}
		
		System.out.println();
		
		for (Player p: g.getPlayers())
			System.out.println(p + " has " + p.getTerritories() + " territories and " + p.getTroops() + " troops");
		
		System.out.println();
		System.out.println("A player needs more than " + g.getWinAmount() + " territories to win the game");
		
		/* Attempt to Place
		 * Tests:
		 * 		1. Placing on a non-owned territory should fail -- Passed
		 * 		2. Placing on an owned territory should pass -- Passed
		 * 
		 * Edited so you cannot place on a non-owned territory
		 */
		
		System.out.println();
		System.out.println("Placing 100 times on random territories");
		for (int i = 0; i < 100; i++)	{
			g.place((int) (Math.random() * g.getTerritoryMap().length), (int) (Math.random()*10) + 1);
		}		
		
		System.out.println();
		count = 1;
		for (Territory t: g.getTerritoryMap())	{
			System.out.println(count + ". " + ((count++ < 10) ? " ": "") + g.getPlayers()[t.getOccupier()] + ": " + t);
		}
		
		System.out.println();
		
		for (Player p: g.getPlayers())
			System.out.println(p + " has " + p.getTerritories() + " territories and " + p.getTroops() + " troops");
		
		
		/* Attempt to Attack
		 * Tests:
		 * 		1. Attacking yourself: False -- Passed
		 * 		2. Attacking with more troops than possible: False -- Passed
		 * 		2. Attacking with all the troops from a territory: False -- Passed
		 * 		4. Defending with more troops than possible: False -- Passed
		 * 		5. Attacking an non-adjacent territory: False -- Passed
		 * 		6. Attacking with an acceptable amount of troops to an adjacent territory: True -- Passed
		 * 		7. Attacking with enough troops to conquer the adjacent territory: True -- Passed
		 * 
		 */
		System.out.println();
		System.out.println("Attacking 100 times on random territories");
		for (int i = 0; i < 100; i++)	{
			int attackTerritory = (int) (Math.random() * g.getTerritoryMap().length);
			int defendTerritory = Adjacencies.getAdjacencyList()[attackTerritory][(int) (Math.random() * Adjacencies.getAdjacencyList()[attackTerritory].length)];
			
			g.attack(attackTerritory, g.getTerritoryMap()[attackTerritory].getTroops() - 1, defendTerritory, g.getTerritoryMap()[defendTerritory].getTroops());
		}
		
		System.out.println();
		count = 1;
		for (Territory t: g.getTerritoryMap())	{
			System.out.println(count + ". " + ((count++ < 10) ? " ": "") + g.getPlayers()[t.getOccupier()] + ": " + t);
		}
		System.out.println();
		
		for (Player p: g.getPlayers())
			System.out.println(p + " has " + p.getTerritories() + " territories and " + p.getTroops() + " troops");
		
		
		/* Attempt to Move
		 * Tests:
		 * 		1. Move between the same territory: False -- Passed
		 * 		2. Move between two territories controlled by two different people: False -- Passed
		 * 		3. Move between two non-adjacent territories: False -- Passed
		 * 		4. Move between territories where the number to move exceeds the amount of troops in the moveFromTerritory: False -- Passed
		 * 		5. Move between territories where the number to move equals the amount of troops in the moveFromTerritory: False -- Passed
		 * 		6. Move between territories where the number to move is less than the amount of troops in moveFromTerritory: True -- Passed
		 */
	
		System.out.println();
		System.out.println("Moving 100 times on random territories");
		for (int i = 0; i < 100; i++)	{
			int moveFromTerritory = (int) (Math.random() * g.getTerritoryMap().length);
			int moveToTerritory = Adjacencies.getAdjacencyList()[moveFromTerritory][(int) (Math.random() * Adjacencies.getAdjacencyList()[moveFromTerritory].length)];
			
			g.move(moveFromTerritory, moveToTerritory, (int) (Math.random()*5) + 1);
		}
		
		System.out.println();
		count = 1;
		for (Territory t: g.getTerritoryMap())	{
			System.out.println(count + ". " + ((count++ < 10) ? " ": "") + g.getPlayers()[t.getOccupier()] + ": " + t);
		}
		System.out.println();
		
		for (Player p: g.getPlayers())
			System.out.println(p + " has " + p.getTerritories() + " territories and " + p.getTroops() + " troops");
		
		/*
		 * Checking Owned, Non-Owned Adjacent, and Adjacent Territories
		 * Owned Tests:
		 * 		1. Given a valid playerID: the owned territories -- Passed
		 * 		2. Given a invalid playerID: null -- Passed
		 * 
		 * Owned Adjacent Tests:
		 * 		1. Given a valid territoryID: the adjacent controlled territories -- Passed
		 * 		2. Given an invalid territoryID: null -- Passed
		 * 
		 * Non-Owned Adjacent Tests:
		 * 		1. Given an valid territoryID: the adjacent non-owned territories -- Passed
		 * 		2. Given an invalid territoryID: null -- Passed
		 */
		
		
		for (int i = 0; i < g.getPlayers().length; i++)	{
			count = 1;
			System.out.println();
			for (Territory t: g.getOwnedTerritories(i))	{
				System.out.println(count + ". " + ((count++ < 10) ? " ": "") + g.getPlayers()[t.getOccupier()] + ": " + t);
			}
		}
		
		for (int i = 0; i < g.getPlayers().length; i++)	{
			System.out.println();
			for (Territory t: g.getOwnedTerritories(i))	{
				System.out.print(g.getPlayers()[t.getOccupier()] + " owns " + t.getName() + " and adjacent territories ");
				for (Territory tAdjacent : g.getAdjacentOwnedTerritories(t.getID()))	{
					System.out.print(tAdjacent.getName() +  " ");
				}
				System.out.println();
			}
		}
		
		for (int i = 0; i < g.getPlayers().length; i++)	{
			System.out.println();
			for (Territory t: g.getOwnedTerritories(i))	{
				System.out.print(g.getPlayers()[t.getOccupier()] + " owns " + t.getName() + " and can attack non-owned adjacent territories ");
				for (Territory tAdjacent : g.getAdjacentNonOwnedTerritories(t.getID()))	{
					System.out.print(tAdjacent.getName() +  " ");
				}
				System.out.println();
			}
		}
		
		
	}

}
