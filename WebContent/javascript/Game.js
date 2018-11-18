/**
 * Javascript file for the Game
 */
var socket;

function connectToServer() {
	socket = new WebSocket("ws://localhost:8080/Risk-SC/gs");
	
	socket.onopen = function(event){
		socket.send(sessionStorage.getItem("username"));
	}
	
	socket.onmessage = function(event){
		
	}
	
	socket.onclose = function(event){
		
	}
	
	socket.onerror = function(event){
		
	}
}