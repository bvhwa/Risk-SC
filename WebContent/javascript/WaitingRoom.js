/**
 * 
 */

function connectToServer()	{
	socket = new WebSocket("ws://localhost:8080/Risk-SC/ss");
	socket.onopen = function(event)	{
		socket.send(sessionStorage.getItem("username"));
	}
	socket.onmessage = function(event)	{
		var usernames = event.data.split("&");
		
		alert(event.data);
		alert(usernames);
		
		for (var i = 1; i <= 4; i++)	{
			document.getElementById("wplayer" + i).innerHTML = (i <= usernames.length) ?  usernames[i - 1] : "Waiting for players";
		}
	}
	socket.onclose = function(event)	{
//		document.getElementById("mychat").innerHTML += "Disconnected!<br />";
	}
}

function sendMessage()	{
	socket.send("Michael: " + document.chatform.message.value);
	return false;
}