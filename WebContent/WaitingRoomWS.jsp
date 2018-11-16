<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Web Socket Waiting Room Implementation</title>
</head>
<body>
	<p id="numPlayers"></p>
	<br>
	<table id="listOfPlayers">
		<tr>
			<th>Players</th>
		</tr>
	</table>
	
	<script>
		var webSocket = new WebSocket("ws://localhost:8080/Risk-SC/endpoint");
		var numPlayers = document.getElementById("numPlayers");
		var listOfPlayers = document.getElementById("listOfPlayers");
		
		webSocket.onopen = function(message){wsOpen(message)};
		webSocket.onclose = function(message){wsClose(message)};
	
		function wsOpen(message){
			var player = listOfPlayers.insertRow(1);
			var playerName = player.insertCell(0);
			player.id = message.data;
		}
		
		function wsClose(message){
			deleteRow(message.data);
		}
		
		function deleteRow(rowId){
			var row = document.getElementById(rowId);
			row.parentNode.removeChild(row);
		}
	</script>
</body>
</html>