/**
 * Javascript file for the Game
 */

function updateGuestFunctionality(username)	{
	if (username == "guest")	{
		document.getElementById("guest_stats").style.display = "block";
		document.getElementById("stats_table").style.display = "none";
	}
}


var socket;

function connectToServer() {
	socket = new WebSocket("ws://localhost:8080/Risk-SC/g");
	
	/**
	 * Send the users information to the WebSocket Server
	 */
	socket.onopen = function(event){
		
		var username = sessionStorage.getItem("username");
		var image = sessionStorage.getItem("image");
		var maxPlayers = sessionStorage.getItem("userNum");
		
		updateGuestFunctionality(username);
		
		socket.send("player_info: " + username + " " + image + " " + maxPlayers);
		
	}
	
	/**
	 * Handle the message the WebSocket Server returns
	 */
	socket.onmessage = function(event){
		
		/**
		 * onMessage Cases:
		 * 		1. Statistics
		 * 		2. Activity Log
		 * 
		 * 		3. Show Place Option
		 * 		4. Show Attack Option
		 * 		5. Show Move Option
		 * 		6. Show Waiting Option
		 * 
		 * 		7. Update Attacking Territory Possibilities after Selection
		 * 		8. Update Moving Territory Possibilities after Selection
		 * 
		 * 		9. Won Game
		 */
		
		// Statistics
		if (event.data.startsWith("statistics:\n"))	{
			updateStats(event.data);
			return false;
		} 
		
		// Activity Log
		if (event.data.startsWith("Activity:"))	{
			updateActivity(event.data);
			return false;
		}
		
		// Show Place Option
		if(event.data.startsWith("Place Troops"))	{
			showPlace(event.data);
			return false;
		}
		
		// Show Attack Option
		if (event.data.startsWith("Update Attacking"))	{
			showAttack(event.data);
			return false;
		}
		
		// Show Move Option
		if (event.data.startsWith("Update Moving"))	{
			showMove(event.data);
			return false;
		}
		
		if (event.data.startsWith("Update Waiting"))	{
			showWaiting();
			return false;
		}
		
		// Update Attacking Territory Possibilities after Selection
		if (event.data.startsWith("Attack To:"))	{
			updateAttackAfterSelection(event.data);
			return false;
		} 
		
		// Update Moving Territory Possibilities after Selection
		if (event.data.startsWith("Move To"))	{
			updateMoveAfterSelection(event.data);
			return false;
		}
		
		// Ask the user how many players to defend with
		if (event.data.startsWith("Initiated Attack"))	{
			askDefender(event.data);
			return false;
		}
		
		// Finished the attack call
		if (event.data.startsWith("Finished Attack"))	{
			document.getElementById("waiting_defend_stage").style.display = "none";
			document.getElementById("attack").style.display = "block";
			return false;
		}
		
		// Update the Cytoscape Map
		if (event.data.startsWith("Update Map:\n"))	{
			updateMap(event.data);
			return false;
		}
		
		if (event.data.startsWith("Winner - "))	{
			endGame(event.data);
		}
		
		
	}
	
	socket.onclose = function(event){
		alert('Onclose called' + event + '\n' + 'code is' + event.code + '\n' + 'reason is ' + event.reason + '\n' + 'wasClean is ' + event.wasClean);
	}
	
	socket.onerror = function(event){
		alert("Error: " + event.data);
	}
}

/**
 * Updates the statistics on the current page
 * @param message the message containing the current statistics received from the WebSocket Server
 * @returns false to prevent updating
 */
function updateStats(message)	{
	
	var statString = message.substring("statistics:\n".length);
	
	// Start of the htmlString is always the same
	var htmlString = "<thead>\n<tr>\n<th>User</th>\n<th>Territories</th>\n<th>Troops</th>\n</tr>\n</thead>\n<tbody>";
	
	var playerStats = statString.split("\n");
	
	// Update the amount of rows proportional to the amount of players
	for (var i = 0; i < playerStats.length; i++)	{
		
		htmlString += "<tr>\n";
		
		var playerInfo = playerStats[i].split("\t");
		
		var username = "<td id = \"player_" + (i + 1) + "_username\">" + playerInfo[0] + "</td>\n";
		var territories = "<td id = \"player_" + (i + 1) + "_username\">" + playerInfo[1] + "</td>\n";
		var troops = "<td id = \"player_" + (i + 1) + "_username\">" + playerInfo[2] + "</td>\n";
		
		htmlString += username + territories + troops;
		
		htmlString += "</tr>\n";
	}
  
	// Closing the body of the htmlString
  	htmlString += "</tbody>\n";
	
	document.getElementById("stats_table").innerHTML = htmlString;
}

/**
 * Updates the activity log on the current page
 * @param message the message containing the activity received from the WebSocket Server
 * @returns false to prevent updating
 */
function updateActivity(message)	{
	document.getElementById("activity").innerHTML += message.substring("Activity:".length) + "<br />";
	return false;
}

/**
 * Hides the Waiting Stage and shows the Place Stage with updated elements
 * @param message the message containing the values of the elements on place division received from the WebSocket Server
 * @returns false to prevent updating
 */
function showPlace(message)	{
	document.getElementById("waiting_stage").style.display = "none";
	document.getElementById("place_troop").style.display = "block";
	
	var territories = message.split("\n");
	document.getElementById("TroopsRemain").innerHTML = "Troops Left: " + territories[0].split(":")[1];
	
	var element = document.getElementById("place_troop_numbers");
	element.max = territories[0].split(":")[1];
	element.value = element.max;
	
	var territoryString = "";
	for (var i = 1; i < territories.length; i++)	{
		territoryString += "<option>" + territories[i] + "</option>\n";
	}
	
	document.getElementById("place_troop_location").innerHTML = territoryString;
	return false;
}

/**
 * Hides the Place Stage and shows the Attack Stage with updated elements
 * @param message the message containing the values of the elements on attack division received from the WebSocket Server
 * @returns false to prevent updating
 */
function showAttack(message)	{
	var data = message.split("\n");
	var ownedTerritories = data[1].split("\t");
	var nonOwnedAdjacentTerritories = data[2].split("\t");
	var maxTroops = data[3];
	
	// Update Attack From Location Possibilities
	var territoryString = "";
	for (var i = 1; i < ownedTerritories.length; i++)	{
		territoryString += "<option>" + ownedTerritories[i] + "</option>\n";
	}
	document.getElementById("attack_from_location").innerHTML = territoryString;
	
	// Update Attack To Location Possibilities
	var nonOwnedTerritoryString = "";
	for (var i = 1; i < nonOwnedAdjacentTerritories.length; i++)	{
		nonOwnedTerritoryString += "<option>" + nonOwnedAdjacentTerritories[i] + "</option>";
	}
	document.getElementById("attack_to_location").innerHTML = nonOwnedTerritoryString;
	
	// Update the Maximum amount of troops
	var attackTroops = document.getElementById("attack_troop_numbers");
	attackTroops.value = maxTroops;
	attackTroops.min = "1";
	attackTroops.max = maxTroops;
	return false;
}

/**
 * Hides the Attack Stage and shows the Move Stage with updated elements
 * @param message the message containing the values of the elements on move division received from the WebSocket Server
 * @returns false to prevent updating
 */
function showMove(message)	{
	var data = message.split("\n");
	var ownedTerritories = data[1].split("\t");
	var ownedAdjacentTerritories = data[2].split("\t");
	var maxTroops = data[3];
	
	// Update Move From Location Possibilities
	var territoryString = "";
	for (var i = 1; i < ownedTerritories.length; i++)	{
		territoryString += "<option>" + ownedTerritories[i] + "</option>\n";
	}
	document.getElementById("move_from_location").innerHTML = territoryString;
	
	// Update Move To Location Possiblities
	var ownedTerritoryString = "";
	for (var i = 1; i < ownedAdjacentTerritories.length; i++)	{
		ownedTerritoryString += "<option>" + ownedAdjacentTerritories[i] + "</option>";
	}
	document.getElementById("move_to_location").innerHTML = ownedTerritoryString;
	
	// Update the Maximum amount of troops
	var moveTroops = document.getElementById("move_troop_numbers");
	moveTroops.value = maxTroops;
	moveTroops.min = "0";
	moveTroops.max = maxTroops;
}

function showWaiting()	{
	document.getElementById("move_troop").style.display = "none";
	document.getElementById("waiting_stage").style.display = "block";
}

/**
 * Updates the Attack Stage after the selection of the attacking territory
 * @param message the message containing the updated values of the attack stage after selection received from the WebSocket Server
 * @returns false to prevent updating
 */
function updateAttackAfterSelection(message)	{
	var territories = event.data.split("\n");
	var element = document.getElementById("attack_troop_numbers");
	var num = territories[0].split(":")[1];
	
	element.value = num;
	element.min = "1";
	element.max = num;
	territories.splice(0,1);
	var territoryString = "";
	for (var i = 0; i < territories.length; i++)	{
		territoryString += "<option>" + territories[i] + "</option>\n";
	}
	document.getElementById("attack_to_location").innerHTML = territoryString;
}

/**
 * Updates the Move Stage after the selection of the territory to move from
 * @param message the message containing the updated values of the move stage after selection received from the WebSocket Server
 * @returns false to prevent updating
 */
function updateMoveAfterSelection(message)	{
	var territories = event.data.split("\n");
	var element = document.getElementById("move_troop_numbers");
	var num = territories[0].split(":")[1];
	
	element.value = num;
	element.min = "1";
	element.max = num;
	territories.splice(0,1);
	var territoryString = "";
	for (var i = 0; i < territories.length; i++)	{
		territoryString += "<option>" + territories[i] + "</option>\n";
	}
	document.getElementById("move_to_location").innerHTML = territoryString;
}

/**
 * Displays the winner on the screen and then takes the user back to the Waiting Room to Play another game
 * @param message the message containing the details of who won the game
 * @returns false to prevent updating
 */
function endGame(message)	{
	alert(message);
	window.location.href = "/Risk-SC/WaitingRoom.jsp";
	return false;
}

/**
 * Called upon the onclick of the Place Troops Button on the Place Stage
 * Sends location and amount of troops being placed until no more troops are left to be placed in which case it sends a finish message
 * @returns false to prevent updating
 */
function placeTroops() {
	var placingString = "Placing,";
	
	var troopsToPlace = document.getElementById("place_troop_numbers").value;
	var territory = document.getElementById("place_troop_location").value;
	
	var troops = parseInt(document.getElementById("TroopsRemain").innerHTML.split(":")[1]);
	
	var message = "";
	
	// Error Checking
	if (territory.length == 0)	{
		message += "A territory must be selected\n";
	}
	
	if (troopsToPlace.length == 0)	{
		message += "An amount of troops must be filled in\n"
	} else if ((troopsToPlace > troops) || (troopsToPlace < 1))	{
		message += troopsToPlace + " troops cannot be placed";
	} 
	
	if (message.length == 0)	{
		
		placingString += troopsToPlace + "," + territory;
		socket.send(placingString);
		
		var troopsLeft = troops - troopsToPlace;
		document.getElementById("TroopsRemain").innerHTML = "Troops Left: " + troopsLeft;
		
		if (troopsLeft == 0)	{
			document.getElementById("place_troop").style.display = "none";
			document.getElementById("attack").style.display = "block";
			
			socket.send("Finished Placing");
		}
	} else	{
		alert(message);
	}
	
	return false;
}

/**
 * Called upon the onchange of the Attacking From Select Item on the Attack Stage
 * Sends a message with the value to the WebSocket Server requesting the possible territories to attack
 * @param value the value of the territory that is attacking received from the Attack Stage
 * @returns false to prevent updating
 */
function updateAttackPossiblities(value)	{
	socket.send("Attack from:" + value);
	return false;
}

/**
 * Called upon the onchange of the Moving From Select Item on the Move Stage
 * Sends a message with the value to the WebSocket Server requesting the possible territories to move to
 * @param value the value of the territory troops are moving from received from the Move Stage
 * @returns false to prevent updating
 */
function updateMovePossibilities(value)	{
	socket.send("Move from:" + value);
	return false;
}

/**
 * Called upon the onclick of the Done Button on the Attack Stage
 * Sends a message to the WebSocket Server indicating a transition to the Move Stage
 * @returns false to prevent updating
 */
function finishAttack()	{
	document.getElementById("attack").style.display = "none";
	document.getElementById("move_troop").style.display = "block";
	socket.send("Finished Attacking");
	return false;
}

/**
 * Called upon the onclick of the Done Button on the Move Stage
 * Sends a message to the WebSocket Server indicating the Move made by the user as well as a transition to the Waiting Stage
 * @returns false to prevent updating
 */
function finishMove()	{
	var moveFromLocation = document.getElementById("move_from_location").value;
	var moveToLocation = document.getElementById("move_to_location").value;
	var troops = document.getElementById("move_troop_numbers").value;
	
	// Add Error Validation
	
	var message = "";
	
	if ((moveFromLocation.length > 0) && (moveToLocation.length == 0))	{
		message += "The location to move to cannot be empty\n";
	}
	
	if (troops.length == 0)	{
		message += "The amount of troops to move with cannot be empty\n";
	} else if ((troops > document.getElementById("move_troop_numbers").max) ||  (troops < document.getElementById("move_troop_numbers").min))	{
		message += "The amount of troops to move with cannot be " + troops + "\n";
	}
	
	// If there are no territories to move from with more than 1 troop, then just finished
	if (moveFromLocation.length == 0)	{
		socket.send("Finished Moving");
	} else if (message.length == 0)	{
		socket.send("Moving," + moveFromLocation + "," + moveToLocation + "," + troops);
		socket.send("Finished Moving");
	} else	{
		alert(message);
	}
	return false;
}


/**
 * Called upon the onclick of the Attack Button on the Attack Stage
 * Sends a message to the WebSocket Server indicating the Attack made by the user
 * @returns false to prevent updating
 */
function attackTerritory() {
	var attackFromLocation = document.getElementById("attack_from_location").value;
	var attackToLocation = document.getElementById("attack_to_location").value;
	var troops = document.getElementById("attack_troop_numbers").value;
	
	var message = "";
	
	if (attackFromLocation.length == 0)	{
		message += "The location to attack from cannot be empty\n";
	}
	
	if (attackToLocation.length == 0)	{
		message += "The location to attack cannot be empty\n";
	}
	
	if (troops.length == 0)	{
		message += "The amount of troops to attack with cannot be empty\n";
	} else if ((troops > document.getElementById("attack_troop_numbers").max) ||  (troops < document.getElementById("attack_troop_numbers").min))	{
		message += "The amount of troops to attack with cannot be " + troops + "\n";
	}
	
	if (message.length == 0)	{
		socket.send("Initiated Attack," + attackFromLocation + "," + attackToLocation + "," + troops);
		
		document.getElementById("waiting_defend_stage").style.display = "block";
		document.getElementById("attack").style.display = "none";
		
		socket.send("Attacked");
	} else	{
		alert(message);
	}
	return false;
}

/**
 * Asks the defender how many soldiers they would like to defend with given the attacking data
 * @param message the message containing the data to tell the defender
 * @returns false to prevent updating
 */
function askDefender(message)	{
	var messages = message.split("\n");
	var data = messages[0].split(",");
	
	var attackFromTerritory = data[1];
	var attackToTerritory = data[2];
	var attackTroops = parseInt(data[3]);
	var defendTroops = parseInt(data[4]);
	
	var valid = false;
	var userValue;
	
	while (!valid)	{
		userValue = parseInt(prompt(messages[1], "1"));
		if (userValue > 0 && userValue <= Math.min(defendTroops, 2))	{
			valid = true;
		}	
	}
	
	socket.send("Attacking," + attackFromTerritory + "," + attackToTerritory + "," + attackTroops + "," + userValue);
	
	return false;
}

function updateMap(data)	{
	console.log(data);
	/*
	 * 
	 * INITIALIZE GRAPH
	 * 
	 */
	var cy = cytoscape({
		  container: document.getElementById('cy'),
		  elements: [
			  // NODES
			  // Athletics
			  { data: { id: '0', name: 'Lyon Center', continent: 'Athletics', value: '0' }, position: { x: -300, y: -300 }, classes: 'Athletics', },
			  { data: { id: '1', name: 'Dedeaux Field', continent: 'Athletics', value: '0' }, position: { x: -350, y: -250 }, classes: 'Athletics' },
			  { data: { id: '2', name: 'McKay Center', continent: 'Athletics', value: '0' }, position: { x: -250, y: -225 }, classes: 'Athletics' },
			  { data: { id: '3', name: 'Little Galen', continent: 'Athletics', value: '0' }, position: { x: -150, y: -200 }, classes: 'Athletics' },
			  { data: { id: '4', name: 'Marks Tennis Stadium', continent: 'Athletics', value: '0' }, position: { x: -400, y: -200 }, classes: 'Athletics' },
			  { data: { id: '5', name: 'Cromwell Field', continent: 'Athletics', value: '0' }, position: { x: -200, y: -150 }, classes: 'Athletics' },
			  
			  // Dornsife STEM
			  { data: { id: '6', name: 'Kaprelian Hall', continent: 'Dornsife STEM', value: '0' }, position: { x: -450, y: -150 }, classes: 'DSTEM' },
			  { data: { id: '7', name: 'Michelson Center', continent: 'Dornsife STEM', value: '0' }, position: { x: -350, y: -125 }, classes: 'DSTEM' },
			  { data: { id: '8', name: 'Seeley G. Mudd', continent: 'Dornsife STEM', value: '0' }, position: { x: -250, y: -100 }, classes: 'DSTEM' },
			  { data: { id: '9', name: 'Hedco Neurosciences', continent: 'Dornsife STEM', value: '0' }, position: { x: -150, y: -50 }, classes: 'DSTEM' },
			  { data: { id: '10', name: 'Stauffer Hall', continent: 'Dornsife STEM', value: '0' }, position: { x: -75, y: 100 }, classes: 'DSTEM' },
			  { data: { id: '11', name: 'Zumberge Hall', continent: 'Dornsife STEM', value: '0' }, position: { x: 0, y: 150 }, classes: 'DSTEM' },
			  
			  // Viterbi
			  { data: { id: '12', name: 'Olin Hall', continent: 'Viterbi School of Engineering', value: '0' }, position: { x: -300, y: 10 }, classes: 'Viterbi' },
			  { data: { id: '13', name: 'Tutor Hall', continent: 'Viterbi School of Engineering', value: '0' }, position: { x: -450, y: 50 }, classes: 'Viterbi' },
			  { data: { id: '14', name: 'EQuad', continent: 'Viterbi School of Engineering', value: '0' }, position: { x: -250, y: 80 }, classes: 'Viterbi' },
			  { data: { id: '15', name: 'Vivian Hall', continent: 'Viterbi School of Engineering', value: '0' }, position: { x: -200, y: 30 }, classes: 'Viterbi' },
			  { data: { id: '16', name: 'Salvatori Hall', continent: 'Viterbi School of Engineering', value: '0' }, position: { x: -300, y: 125 }, classes: 'Viterbi' },
			  { data: { id: '17', name: 'Seaver Library', continent: 'Viterbi School of Engineering', value: '0' }, position: { x: -225, y: 150 }, classes: 'Viterbi' },
			  
			  // SCA
			  { data: { id: '23', name: 'Norris Cinema', continent: 'School of Cinematic Arts', value: '0' }, position: { x: -50, y: -400}, classes: 'Cinema' },
			  { data: { id: '24', name: 'SCB', continent: 'School of Cinematic Arts', value: '0' }, position: { x: -150, y: -300}, classes: 'Cinema' },
			  { data: { id: '25', name: 'SCA', continent: 'School of Cinematic Arts', value: '0' }, position: { x: -50, y: -300}, classes: 'Cinema' },
			  
			  // Marshall
			  { data: { id: '18', name: 'Levanthal Hall', continent: 'Marshall School of Business', value: '0' }, position: { x: 100, y: 150 }, classes: 'Marshall' },
			  { data: { id: '19', name: 'Hoffman Hall', continent: 'Marshall School of Business', value: '0' }, position: { x: 175, y: 250 }, classes: 'Marshall' },
			  { data: { id: '20', name: 'Dauterive Hall', continent: 'Marshall School of Business', value: '0' }, position: { x: 300, y: 150 }, classes: 'Marshall' },
			  { data: { id: '21', name: 'Popovich Hall', continent: 'Marshall School of Business', value: '0' }, position: { x: 350, y: 200 }, classes: 'Marshall' },
			  { data: { id: '22', name: 'Fertitta Hall', continent: 'Marshall School of Business', value: '0' }, position: { x: 450, y: 200 }, classes: 'Marshall' },
			  
			  // Dornsife LAC
			  { data: { id: '29', name: 'Tutor Campus Center', continent: 'Dornsife College of LAS', value: '0' }, position: { x: 100, y: 50 }, classes: 'LDornsife' },
			  { data: { id: '26', name: 'Taper Hall', continent: 'Dornsife College of LAS', value: '0' }, position: { x: 60, y: -325 }, classes: 'LDornsife' },
			  { data: { id: '27', name: 'Von KleinSmid Hall', continent: 'Dornsife College of LAS', value: '0' }, position: { x: 150, y: -250 }, classes: 'LDornsife' },
			  { data: { id: '28', name: 'Leavey Library', continent: 'Dornsife College of LAS', value: '0' }, position: { x: 400, y: -300 }, classes: 'LDornsife' },
			  { data: { id: '33', name: 'Doheny Library', continent: 'Dornsife College of LAS', value: '0' }, position: { x: 250, y: -125 }, classes: 'LDornsife' },
			  { data: { id: '31', name: 'Alumni Park', continent: 'Dornsife College of LAS', value: '0' }, position: { x: 150, y: -150 }, classes: 'LDornsife' },
			  { data: { id: '32', name: 'McCarthy Quad', continent: 'Dornsife College of LAS', value: '0' }, position: { x: 400, y: -200 }, classes: 'LDornsife' },
			  { data: { id: '30', name: 'Tommy Trojan', continent: 'Dornsife College of LAS', value: '0' }, position: { x: 200, y: -10 }, classes: 'LDornsife' },
		
			  // Annenberg
			  { data: { id: '34', name: 'Wallis Annenberg Hall', continent: 'Annenberg School of Comm', value: '0' }, position: { x: -50, y: -50 }, classes: 'Annenberg' },
			  { data: { id: '35', name: 'Annenberg School', continent: 'Annenberg School of Comm', value: '0' }, position: { x: 0, y: -150 }, classes: 'Annenberg' },
			  
			  // EDGES
			  { data: { id: 'a', source: '0', target: '1' } },
			  { data: { id: 'b', source: '0', target: '2' } },
			  { data: { id: 'c', source: '0', target: '24' } },
			  { data: { id: 'd', source: '0', target: '2' } },
			  { data: { id: 'e', source: '1', target: '2' } },
			  { data: { id: 'f', source: '1', target: '4' } },
			  { data: { id: 'g', source: '2', target: '3' } },
			  { data: { id: 'h', source: '2', target: '4' } },
			  { data: { id: 'i', source: '2', target: '5' } },
			  { data: { id: 'j', source: '2', target: '24' } },
			  { data: { id: 'k', source: '3', target: '5' } },
			  { data: { id: 'l', source: '3', target: '25' } },
			  { data: { id: 'm', source: '3', target: '34' } },
			  { data: { id: 'n', source: '3', target: '35' } },
			  { data: { id: 'o', source: '4', target: '5' } },
			  { data: { id: 'p', source: '4', target: '6' } },
			  { data: { id: 'q', source: '4', target: '7' } },
			  { data: { id: 'r', source: '5', target: '8' } },
			  { data: { id: 's', source: '5', target: '9' } },
			  { data: { id: 't', source: '5', target: '34' } },
			  
			  { data: { id: 'u', source: '6', target: '7' } },
			  { data: { id: 'v', source: '7', target: '8' } },
			  { data: { id: 'w', source: '7', target: '12' } },
			  { data: { id: 'x', source: '8', target: '9' } },
			  { data: { id: 'y', source: '8', target: '12' } },
			  { data: { id: 'z', source: '9', target: '10' } },
			  { data: { id: 'za', source: '9', target: '15' } },
			  { data: { id: 'zb', source: '9', target: '34' } },
			  { data: { id: 'zc', source: '10', target: '11' } },
			  { data: { id: 'aa', source: '10', target: '15' } },
			  { data: { id: 'bb', source: '10', target: '29' } },
			  { data: { id: 'cc', source: '11', target: '18' } },
			
			  { data: { id: 'dd', source: '12', target: '13' } },
			  { data: { id: 'ee', source: '12', target: '14' } },
			  { data: { id: 'ff', source: '12', target: '15' } },
			  { data: { id: 'gg', source: '13', target: '14' } },
			  { data: { id: 'hh', source: '13', target: '16' } },
			  { data: { id: 'ii', source: '14', target: '15' } },
			  { data: { id: 'jj', source: '14', target: '16' } },
			  { data: { id: 'kk', source: '14', target: '17' } },
			  { data: { id: 'll', source: '15', target: '17' } },
			  { data: { id: 'mm', source: '16', target: '17' } },
			  
			  { data: { id: 'nn', source: '18', target: '19' } },
			  { data: { id: 'oo', source: '18', target: '20' } },
			  { data: { id: 'pp', source: '18', target: '29' } },
			  { data: { id: 'qq', source: '18', target: '30' } },
			  { data: { id: 'rr', source: '19', target: '20' } },
			  { data: { id: 'ss', source: '20', target: '21' } },
			  { data: { id: 'ooo', source: '20', target: '30' } },
			  { data: { id: 'ppp', source: '20', target: '33' } },
			  { data: { id: 'tt', source: '21', target: '22' } },
			  
			  { data: { id: 'uu', source: '23', target: '24' } },
			  { data: { id: 'vv', source: '23', target: '25' } },
			  { data: { id: 'ww', source: '24', target: '25' } },
			  { data: { id: 'xx', source: '25', target: '35' } },
			  
			  { data: { id: 'yy', source: '26', target: '27' } },
			  { data: { id: 'zz', source: '26', target: '28' } },
			  { data: { id: 'aaa', source: '26', target: '35' } },
			  { data: { id: 'bbb', source: '27', target: '28' } },
			  { data: { id: 'ccc', source: '27', target: '31' } },
			  { data: { id: 'ddd', source: '27', target: '32' } },
			  { data: { id: 'nnn', source: '27', target: '35' } },
			  { data: { id: 'eee', source: '28', target: '32' } },
			  { data: { id: 'fff', source: '29', target: '30' } },
			  { data: { id: 'ggg', source: '29', target: '34' } },
			  { data: { id: 'hhh', source: '30', target: '31' } },
			  { data: { id: 'iii', source: '30', target: '34' } },
			  { data: { id: 'jjj', source: '31', target: '32' } },
			  { data: { id: 'kkk', source: '31', target: '33' } },
			  { data: { id: 'lll', source: '32', target: '33' } },
			  
			  { data: { id: 'mmm', source: '34', target: '35' } }
			  
			  
			],
			
			layout:{
				name: 'preset' // To allow manual positioning
			},

		    style: [
		    	{
		            selector: 'node',
		            style: {
		            	label: 'data(value)',
						'text-valign': 'center',
						'text-halign': 'center',
						width: 50,
						height: 50
		            }
		        },
		        {
		            selector: '.Athletics',
		            style: {
		                
		                'border-color': 'darkred',
		                'border-width': 4,
		                'background-color': 'white', 
						
						
		            }
		        },
		        {
		            selector: '.DSTEM',
		            style: {
		                'border-color': 'blue',
		                'border-width': 4,
		                'background-color': 'white', 
						
						
		            }
		        },
		        {
		            selector: '.Viterbi',
		            style: {
		                'border-color': 'orange',
		                'border-width': 4,
		                'background-color': 'white', 
						
		            }
		        },
		        {
		            selector: '.Cinema',
		            style: {
		                'border-color': 'purple',
		                'border-width': 4,
		                'background-color': 'white', 
						
		            }
		        },
		        {
		            selector: '.Marshall',
		            style: {
		                'border-color': 'red',
		                'border-width': 4,
		                'background-color': 'white', 
						
		            }
		        },
		        {
		            selector: '.Annenberg',
		            style: {
		                'border-color': 'gold',
		                'border-width': 4,
		                'background-color': 'white', 
						
		            }
		        },
		        {
		            selector: '.LDornsife',
		            style: {
		                'border-color': 'pink',
		                'border-width': 4,
		                'background-color': 'white', 
						
		            }
		        },
		        {
		            selector: 'node.highlight',
		            style: {
		               
		            }
		        },
		        {
		            selector: 'node.semitransp',
		            style:{ 'opacity': '0.5' }
		        },
		        {
		            selector: 'edge.highlight',
		            style: { 'mid-target-arrow-color': '#FFF' }
		        },
		        {
		            selector: 'edge.semitransp',
		            style:{ 'opacity': '0.2' }
		        }
		        ] 
		});
	
		// Make nodes ungrabbable/unmovable
		cy.nodes().ungrabify();
		
		// Highlight neighboring nodes with edges upon hover
		cy.on('select', 'node', function(e) {
		    var sel = e.target;
		    cy.elements()
		        .difference(sel.outgoers()
		            .union(sel.incomers()))
		        .not(sel)
		        .addClass('semitransp');
		    sel.addClass('highlight')
		        .outgoers()
		        .union(sel.incomers())
		        .addClass('highlight');
		});
		cy.on('unselect', 'node', function(e) {
		    var sel = e.target;
		    cy.elements()
		        .removeClass('semitransp');
		    sel.removeClass('highlight')
		        .outgoers()
		        .union(sel.incomers())
		        .removeClass('highlight');
		});
		
		// Display tooltip upon click with QTip (jQuery)
		cy.elements('node').qtip({
				content: 
					function() {
						return '<b>' + this.data('name') + '</b>' + '<br />' + this.data('continent') + '<br />' + 'Troops: ' + this.data('value');
					},
				
				position: {
					my: 'top center',
					at: 'bottom center'
				},
				style: {
					classes: 'qtip-bootstrap',
					tip: {
						width: 16,
						height: 8
					}
				},
				show: {
					event: 'mouseover'
				},
				hide: {
					event: 'mouseout'
				}
	});
		
	// Split the given data into an array of 36 Strings where every element contains the data of an individual country
	var countries = data.substring("Update Map:\n".length).split("\n");
	for (var i = 0; i < countries.length; i++)	{
		// Split the data of an individual country into an array of 3 strings: territoryID, numberOfTroops, and ownerID
		var countryData = countries[i].split(" ");
		
		var territoryID = countryData[0];
		var numberOfTroops = countryData[1];
		var ownerID = countryData[2];
		
		var newTerritoryID = '#' + territoryID;
		console.log(newTerritoryID);
		console.log(numberOfTroops);
		
		// TODO: Given the three elements above, update the associated map
		var update = cy.$(newTerritoryID);
		update.data('value', numberOfTroops);
		
		if (ownerID == 0){
			update.style('background-color', 'peachpuff');
		}
		else if (ownerID == 1){
			update.style('background-color', 'lightblue');
		}
		else if (ownerID == 2){
			update.style('background-color', 'lightgreen');
		}
		else if (ownerID == 3){
			update.style('background-color', 'thistle');
		}
		
	}
	
}


