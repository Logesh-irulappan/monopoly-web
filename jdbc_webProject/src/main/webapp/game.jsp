<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.List" %>   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Monopoly Started!</title>
<link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200&icon_names=savings" />
	<style>	
		@import url('https://fonts.googleapis.com/css2?family=Cascadia+Code:ital,wght@0,200..700;1,200..700&display=swap');
		body {
			display: grid;
			height: 97vh;
			grid-template-rows: 1fr 1fr;
			grid-template-columns: 1fr 1fr;
			grid-template-areas: "left" "right"
								 "left" "right";	
			font-family: "Cascadia Code", sans-serif;
			border: 3px solid whitesmoke;
			background-image: url('monopolyBG.jpg');
			background-size: contain;
			border-radius: 7px;
		}
		
		.left {
			display: flex;
			width: 500px;
			flex-direction: column;
			padding: 10px 10px;
			justify-content: space-evenly;
		}
		
		.left #commandHeader {
			margin-right: 200px;
		}
		
		.first {	
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			height: 200px;
			border: 3px solid whitesmoke;
		}
		
		input {
			padding: 10px 10px;
			border-radius: 7px;
			border: none;
			outline: none;
		}
		
		#dice {
			border: none;
			padding: 30px 30px;
			border-radius: 7px;
			background-image: url("dice-roll-dice.gif");
			background-size: contain;
			background-repeat: no-repeat;
			color: whitesmoke;
			font-weight: bolder;
			cursor: pointer;
		}
		
		.commandsView {
			background-color: whitesmoke;
			display: flex;
			flex-direction: column;
			border-radius: 7px;
		}
		
		.mid {
			display: flex;
			border: 3px solid whitesmoke;
			flex-direction: column;
			align-items: center;
			justify-content: center;
		}
		
		.last {
			display: flex;
			border: 3px solid whitesmoke;
			flex-direction: column;
			align-items: center;
			justify-content: center;
		}
		
		.last button {
			cursor: pointer;
			padding: 10px 10px;
		}
			
		.mid form {
			display: flex;
			padding: 10px 10px;
			align-items: center;
			justify-items: space-between;
		}
		
		.right {
			display: flex;
			flex-direction: column;
			justify-content: space-evenly;
			margin-top: 20px;
		}
		
		.resultContainer {
			width: 680px;
			height: 400px;
			color: black;
			padding: 5px 5px;
			border-radius: 7px;
			background-color: whitesmoke;
			overflow-y: scroll;
		}
		
		.playerDetails {
			display: flex;
			align-items: center;
			justify-content: space-evenly;
		}
		
		#inputForm {	
			display: none;
		}
		
		#payDiv {
			display: none;
		}
		
		#borrowDiv {
			display: none;
		}
		
	</style>
</head>
<body>
	<div class="left">
		<div class="first">
			<h2 id="gameHeader">Monopoly</h2>
			<form id="diceForm" action="diceRoll" method="post">
				<button id="dice" type="submit"></button>
			</form>
		</div>
		<div class="last">
			<h3>Bank Handler</h3>
			<div class="options">
				<button id="borrow">Borrow Bank</button>
				<button id="payBank">Pay Bank</button>
			</div>
			<form action="bankHandler" method="get">
				<div id="borrowDiv">
					<input name="borrowAmount" placeholder="Enter Amount..." />
					<button type="submit">Borrow</button>
				</div>
			</form>
			<form action="bankHandler" method="get">
				<div id="payDiv">
					<input name="payAmount" placeholder="Enter Amount..." />
					<button type="submit">Pay</button>
				</div>
			</form>
		</div>
		<div class="commandsView">
			<h3 id="commandHeader">Commands</h3>
			<ul>
				<li>PAY_RENT</li>
				<li>PAY_TAX</li>
				<li>BUY_PROPERTY</li>
				<li>BUY_HOUSES</li>
				<li>SKIP</li>
				<li>OK</li>
			</ul>
		</div>
	</div>
	
	<div class="right"> 
		<div class="mid">
			<h3>Command Input</h3>
			<form id="inputForm" action="command" method="get">
				<input name="commandInput" autocomplete="off" placeholder="Enter your command..." required/>
				<button type="submit">Submit</button>
			</form>
		</div>
		<% List<String> playerData = (List<String>) request.getAttribute("playerData"); %>
		<% if(playerData != null) { %>
			<div class="playerDetails">
				<div>
					<p>PlayerName: <%= playerData.get(0) %> </p> 
					<p>Balance <span class="material-symbols-outlined">savings</span>
					$<%= playerData.get(1) %> </p>
				</div>
				<div>
					<p>Borrowed Amount: $<%= playerData.get(2) %> </p>
				</div>
			</div>
		<% } %>
		<div class="resultContainer">
			<% List<String> results = (List<String>) request.getAttribute("results"); %>
			<% if(results != null) { %>
				<% for(String res : results) { %>
					<div class="res">
						<p><%= res %></p>
					</div>
				<% } %>
			<% } %>
		</div>
	</div>
	
	<script type="text/javascript">
		var borrowDiv = document.getElementById("borrowDiv");
		var payDiv = document.getElementById("payDiv");
	
		var borrowBtn = document.getElementById("borrow");
		borrowBtn.addEventListener("click", function() {
			borrowDiv.style.display = "block";
			payDiv.style.display = "none";
		});
		
		var payBtn = document.getElementById("payBank");
		payBtn.addEventListener("click", function() {
			payDiv.style.display = "block";
			borrowDiv.style.display = "none";
		});
		
		var diceBtn = document.getElementById("dice");
		var commandInput = document.getElementById("inputForm");
		
		<% if(request.getAttribute("hideDice") != null) { %>
			diceBtn.style.display = "none";
			commandInput.style.display = "block";
		<% } %>
		
	</script>
</body>
</html>



















































