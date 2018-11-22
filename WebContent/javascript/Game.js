/**
 * Javascript file for the Game
 */
var socket;

function connectToServer() {
	socket = new WebSocket("ws://localhost:8080/Risk-SC/g");
	
	// onmessage starting identifiers
	var statisticsString = "statistics:\n";
	var activityString = "Activity:";
	
	
	
	socket.onopen = function(event){
		
		var username = sessionStorage.getItem("username");
		var image = sessionStorage.getItem("image");
		var maxPlayers = sessionStorage.getItem("userNum");
		
		socket.send("player_info: " + username + " " + image + " " + maxPlayers);
		
	}
	
	
	socket.onmessage = function(event){
		
		alert(event.data);
		
		if (event.data.startsWith(statisticsString))	{
			updateStats(event.data.substring(statisticsString.length));
		} else if(event.data.startsWith("Place Troops"))	{
			document.getElementById("waiting_stage").style.display = "none";
			document.getElementById("place_troop").style.display = "block";
			
			var territories = event.data.split("\n");
			document.getElementById("TroopsRemain").innerHTML = "Troops Left: " + territories[0].split(":")[1];
			document.getElementById("place_troop_numbers").max = territories[0].split(":")[1];
			
			territories.splice(0,1);
			var territoryString = "";
			for (var i = 0; i < territories.length; i++)	{
				territoryString += "<option>" + territories[i] + "</option>\n";
			}
			
			document.getElementById("place_troop_location").innerHTML = territoryString;
			document.getElementById("attack_from_location").innerHTML = territoryString;
			document.getElementById("move_from_location").innerHTML = territoryString;
			
		}
		else if(event.data == "Attack")	{
			document.getElementById("place_troop").style.display = "none";
			document.getElementById("attack").style.display = "block";
			attackTerritory();
		}
		else if(event.data == "Move Troops")	{
			document.getElementById("attack").style.display = "none";
			document.getElementById("move_troop").style.display = "block";
			moveTroops();
		} else if (event.data == "Waiting")	{
			document.getElementById("move_troop").style.display = "none";
			document.getElementById("waiting_stage").style.display = "block";
		} else if (event.data.startsWith(activityString))	{
			document.getElementById("activity").innerHTML += event.data.substring(activityString.length) + "<br />";
		} else if (event.data.startsWith("Attack To:"))	{
			var territories = event.data.split("\n");
			var element = document.getElementById("attack_troop_numbers");
			var num = territories[0].split(":")[1];
			
			if (num == "0")	{
				element.min = num;
				element.value = num;
			} else	{
				element.min = "1";
				element.value = "1";
			}
			
			element.max = num;
			territories.splice(0,1);
			var territoryString = "";
			for (var i = 0; i < territories.length; i++)	{
				territoryString += "<option>" + territories[i] + "</option>\n";
			}
			document.getElementById("attack_to_location").innerHTML = territoryString;
		} else if (event.data.startsWith("Move To"))	{
			var territories = event.data.split("\n");
			var element = document.getElementById("move_troop_numbers");
			var num = territories[0].split(":")[1];
			
			if (num == "0")	{
				element.min = num;
				element.value = num;
			} else	{
				element.min = "1";
				element.value = "1";
			}
			
			element.max = num;
			territories.splice(0,1);
			var territoryString = "";
			for (var i = 0; i < territories.length; i++)	{
				territoryString += "<option>" + territories[i] + "</option>\n";
			}
			document.getElementById("move_to_location").innerHTML = territoryString;
		} else if (event.data.startsWith("Update Attacking"))	{
			var data = event.data.split("\n");
			var ownedTerritories = data[1].split("\t");
			var nonOwnedAdjacentTerritories = data[2].split("\t");
			var maxTroops = data[3];
			
			// Update Attack From Location Possibilities
			var territoryString = "";
			for (var i = 1; i < ownedTerritories.length; i++)	{
				territoryString += "<option>" + ownedTerritories[i] + "</option>\n";
			}
			document.getElementById("attack_from_location").innerHTML = territoryString;
			
			// Update Attack To Location Possiblities
			var nonOwnedTerritoryString = "";
			for (var i = 1; i < nonOwnedAdjacentTerritories.length; i++)	{
				nonOwnedTerritoryString += "<option>" + nonOwnedAdjacentTerritories[i] + "</option>";
			}
			document.getElementById("attack_to_location").innerHTML = nonOwnedTerritoryString;
			
			// Update the Maximum amount of troops
			var attackTroops = document.getElementById("attack_troop_numbers");
			if (maxTroops == "0")	{
				attackTroops.value = "0";
				attackTroops.min = "0";
				attackTroops.max = "0";
			} else	{
				attackTroops.value = "1";
				attackTroops.min = "1";
				attackTroops.max = maxTroops;
			}
			
		} else if (event.data.startsWith("Update Moving"))	{
			var data = event.data.split("\n");
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
			var attackTroops = document.getElementById("move_troop_numbers");
			if (maxTroops == "0")	{
				attackTroops.value = "0";
				attackTroops.min = "0";
				attackTroops.max = "0";
			} else	{
				attackTroops.value = "1";
				attackTroops.min = "1";
				attackTroops.max = maxTroops;
			}
		}
		
	}
	
	socket.onclose = function(event){
		alert('Onclose called' + event + '\n' + 'code is' + event.code + '\n' + 'reason is ' + event.reason + '\n' + 'wasClean is ' + event.wasClean);
	}
	
	socket.onerror = function(event){
		alert("Error: " + event.data);
	}
}

function updateStats(statString)	{
	
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


function placeTroops() {
	var placingString = "Placing,";
	var troopsToPlace = document.getElementById("place_troop_numbers").value;
	
	var troops = parseInt(document.getElementById("TroopsRemain").innerHTML.split(":")[1]);
	
	if (troopsToPlace > troops)	{
		alert(troopsToPlace + " troop cannot be placed with only " + troops + " troops remaining");
	} else	{
		var territory = document.getElementById("place_troop_location").value;
		alert(territory);
		
		placingString += troopsToPlace + "," + territory;
		socket.send(placingString);
		
		var troopsLeft = troops - troopsToPlace;
		document.getElementById("TroopsRemain").innerHTML = "Troops Left: " + troopsLeft;
		
		if (troopsLeft == 0)	{
			document.getElementById("place_troop").style.display = "none";
			document.getElementById("attack").style.display = "block";
			
			socket.send("Finished Placing");
		}
	}
	
	return false;
}

function updateAttackPossiblities(value)	{
	socket.send("Attack from:" + value);
	return false;
}

function updateMovePossibilities(value)	{
	socket.send("Move from:" + value);
	return false;
}

function finishAttack()	{
	document.getElementById("attack").style.display = "none";
	document.getElementById("move_troop").style.display = "block";
	socket.send("Finished Attacking");
	return false;
}

function finishMove()	{
	var moveFromLocation = document.getElementById("move_from_location").value;
	var moveToLocation = document.getElementById("move_to_location").value;
	var troops = document.getElementById("move_troop_numbers").value;
	
	// Add Error Validation	
	
	document.getElementById("move_troop").style.display = "none";
	document.getElementById("waiting_stage").style.display = "block";
	socket.send("Moving," + moveFromLocation + "," + moveToLocation + "," + troops);
	return false;
}


function attackTerritory() {
	var attackFromLocation = document.getElementById("attack_from_location").value;
	var attackToLocation = document.getElementById("attack_to_location").value;
	var troops = document.getElementById("attack_troop_numbers").value;
	
	// Add Error Validation
	
	socket.send("Attacking," + attackFromLocation + "," + attackToLocation + "," + troops);
	return false;
}

function moveTroops()
{
	
}


