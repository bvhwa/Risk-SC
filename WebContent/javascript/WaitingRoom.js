/**
 * 
 */

var socket;

function connectToServer()	{
	socket = new WebSocket("ws://localhost:8080/Risk-SC/wr");
	socket.onopen = function(event)	{
		socket.send(sessionStorage.getItem("username"));
	}
	socket.onmessage = function(event)	{
		
		if (event.data == "Ready to Start Game")	{
			window.location.href = "/Risk-SC/Game.jsp";
		} else	{
		
			var usernames = event.data.split("&");
			sessionStorage.setItem("userNum", usernames.length);
			
			for (var i = 1; i <= 4; i++)	{
				document.getElementById("wplayer" + i).innerHTML = (i <= usernames.length) ?  usernames[i - 1] : "Waiting for players";
			}
		}
	}
	socket.onclose = function(event) {}
}

function startGame()	{
	if (sessionStorage.getItem("username") == document.getElementById("wplayer1").innerHTML)	{
		if (document.getElementById("wplayer2").innerHTML != "Waiting for players")	{
			socket.send("Ready to Start Game");
		} else	{
			alert("You need at least 2 players to start the game");
		}
	} else	{
		alert("Only the host can start the game");
	}
}