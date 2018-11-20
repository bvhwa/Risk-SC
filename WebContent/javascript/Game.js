/**
 * Javascript file for the Game
 */
var socket;

function connectToServer() {
	socket = new WebSocket("ws://localhost:8080/Risk-SC/gs");
	
	
	socket.onopen = function(event){
	
	}
	
	
	socket.onmessage = function(event){
		
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


function placeTroops() {
	
}

function attackTerritory() {
	
}

function moveTroops()
{
	
}


