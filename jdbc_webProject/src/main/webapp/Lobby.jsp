<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Monopoly Lobby</title>
<style>
	@import url('https://fonts.googleapis.com/css2?family=Cascadia+Code:ital,wght@0,200..700;1,200..700&display=swap');	
	body {
		background-color: rgb(144, 238, 144);
		font-family: "Cascadia Code", sans-serif;
	}
	#startBtn {	
		display: none;
	}
	.alertBox {
		display: none;
	}
	
</style>
</head>
<body>
	<h2>Monopoly Game</h2>
	<form action="addPlayer" method="post">
		<input type="text" name="username" placeholder="Enter Player Username" autocomplete="off" required/>
		<button type="submit">Add Player</button>
	</form>
	<div class="alertBox">
		<%= request.getAttribute("showAlert") %>
		<button id="alertCloseBtn">Close</button>
	</div>
	<div class="playerBoard">
		<% List<String> playerNames = (List<String>) request.getAttribute("playerNames"); %>
		<% if(playerNames != null) { %>
			<p>Total Players added: <%= request.getAttribute("playerCount") %></p>
			<% for(String name : playerNames) { %>
				<p><%= name %></p>
			<% } %>
		<% } %>
	</div>
	<form action="getGame" method="get">
		<button type="submit" id="startBtn">Start Game</button>
	</form>
	
	
	<script>
		var startBtn = document.getElementById("startBtn");
		startBtn.addEventListener("click", function() {
			alert("Monopoly game is starting...");
		});
		
		<% if(request.getAttribute("playerNames") != null) { %>
			startBtn.style.display = "block";
		<% } %>
	
		var alertBox = document.querySelector(".alertBox");
		
		var alertCloseBtn = document.getElementById("alertCloseBtn");
		alertCloseBtn.addEventListener("click", function() {
			alertBox.style.display = "none";
		});
		
		<% if (request.getAttribute("alertBox") != null) { %>"
			alertBox.style.display = "block";
		<% } %>
	
	</script>
</body>
</html>






















