<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="ISO-8859-1">
		<title>Waiting Room (Practice with WebSockets)</title>
	</head>
	<body>
		<form>
			<input id="message" type="text">
			<input onclick="sendMessage();" value="Echo" type="button">
        	<input onclick="closeConnection();" value="Disconnect" type="button">
    	</form>
    	<br>
    	<textarea id="echoText" rows="5" cols="30"></textarea>
    	<script>
    		var webSocket = new WebSocket("ws://localhost:8080/Risk-SC/waitingroom");
    		var echoText = document.getElementById("echoText");
    		echoText.value = "";
    		
    		var message = document.getElementById("message");
    		webSocket.onopen = function(message){open(message)};
    		webSocket.onmessage = function(message){getMessage(message)};
    		webSocket.onclose = function(message){close(message)};
    		webSocket.onerror = function(message){error(message)};
    		
    		function open(message){
    			echoText.value += "Connected! \n";
    		}
    		function sendMessage(message){
    			webSocket.send(message.value);
    			echoText.value += "Message sent to server : " + message.value + "\n";
    			message.value = "";
    		}
    		function closeConnection(){
    			webSocket.close();
    		}
    		function getMessage(message){
    			echoText.value += "Message received from server: " + message.data + "\n";
    		}
    		function close(message){
    			echoText.value += "Disconnected! \n";
    		}
    		function error(message){
    			echoText.value += "Error! \n";
    		}
    		
    	
    	
    	
    	</script>
	</body>
</html>