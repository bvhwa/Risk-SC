/**
 * Javascript file for the Game
 */
var socket;

function connectToServer() {
	socket = new WebSocket("ws://localhost:8080/Risk-SC/g");
	
	// onmessage starting identifiers
	var userString = "users:";
	var statisticsString = "statistics:\n";
	
	
	
	socket.onopen = function(event){
		
		var username = sessionStorage.getItem("username");
		var image = sessionStorage.getItem("image");
		var maxPlayers = sessionStorage.getItem("userNum");
		
		socket.send("player_info: " + username + " " + image + " " + maxPlayers);
		
	}
	
	
	socket.onmessage = function(event){
		
		if (event.data.startsWith(userString))	{
			updateUsernames(event.data.substring(userString.length).split("&"));
			
			
		} else if (event.data.startsWith(statisticsString))	{
			updateStats(event.data.substring(statisticsString.length));
		}
		
		if(event.data = "Place Troops")
			{
				placeTroops();
			}
		else if(event.data = "Attack")
			{
				attackTerritory();
			}
		else if(event.data = "Move Troops")
			{
				moveTroops();
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
	
}

function attackTerritory() {
	
}

function moveTroops()
{
	
}


