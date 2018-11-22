/**
 * Javascript file for the Game
 */
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
	
	if (moveFromLocation.length == 0)	{
		message += "The location to move from cannot be empty\n";
	}
	
	if (moveToLocation.length == 0)	{
		message += "The location to move to cannot be empty\n";
	}
	
	if (troops.length == 0)	{
		message += "The amount of troops to move with cannot be empty\n";
	} else if ((troops > document.getElementById("move_troop_numbers").max) ||  (troops < document.getElementById("move_troop_numbers").min))	{
		message += "The amount of troops to move with cannot be " + troops + "\n";
	}
	
	if (message.length == 0)	{
		document.getElementById("move_troop").style.display = "none";
		document.getElementById("waiting_stage").style.display = "block";
		socket.send("Moving," + moveFromLocation + "," + moveToLocation + "," + troops);
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
	
	// Add Error Validation
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
		socket.send("Attacking," + attackFromLocation + "," + attackToLocation + "," + troops);
		socket.send("Attacked");
	} else	{
		alert(message);
	}
	return false;
}


