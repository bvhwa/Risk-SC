/**
 * Javascript file for the Game
 */
var socket;

function connectToServer() {
	socket = new WebSocket("ws://localhost:8080/Risk-SC/g");
	
	// onmessage starting identifiers
	var statisticsString = "statistics:\n";
	
	
	
	socket.onopen = function(event){
		
		var username = sessionStorage.getItem("username");
		var image = sessionStorage.getItem("image");
		var maxPlayers = sessionStorage.getItem("userNum");
		
		socket.send("player_info: " + username + " " + image + " " + maxPlayers);
		
	}
	
	
	socket.onmessage = function(event){
		
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
		}
	}
	
	socket.onclose = function(event){
	}
	
	socket.onerror = function(event){		
	}
}

function updateUsernames(usernamesString)	{
	
}

function updateStats(statString)	{
	
	alert(statString);
	
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
	var placingString = "Placing_Troops ";
	var troopsToPlace = document.getElementById("place_troop_numbers").value;
	
	var troops = parseInt(document.getElementById("TroopsRemain").innerHTML.split(":")[1]);
	
	if (troopsToPlace > troops)	{
		alert(troopsToPlace + " troop cannot be placed with only " + troops + " troops remaining");
	} else	{
		var territory = document.getElementById("place_troop_location").value;
		alert(territory);
		
		placingString += troopsToPlace + " " + territory;
		socket.send(placingString);
		
		var troopsLeft = troops - troopsToPlace;
		document.getElementById("TroopsRemain").innerHTML = "Troops Left: " + troopsLeft;
		
		if (troopsLeft == 0)	{
			document.getElementById("place_troop").style.display = "none";
			document.getElementById("attack").style.display = "block";
		}
	}
	
	return false;
}

function attackTerritory() {
	
}

function moveTroops()
{
	
}


