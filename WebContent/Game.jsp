<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Game</title>
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
		
	    <script src="//code.jquery.com/jquery-2.0.3.min.js"></script>
		<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
		
		<style>
		html, body { 
			height:100%; 
			margin:0; 
			padding:0;
		}

		#NW { 
			top:0;   
			left:0;   
			position:fixed; 
			width:70%; 
			height:60%;
			padding:1em;
		  	box-sizing:border-box;
		  	-moz-box-sizing:border-box;
		  	-webkit-box-sizing:border-box;
			background: white;
			/*background-image: linear-gradient(rgb(104, 145, 162), rgb(12, 97, 33));*/
			
		}

		#NE { 
			top:0;   
			left:70%; 
			position:fixed;
			width:30%;
			height:60%;
			padding:1em;
		  	box-sizing:border-box;
		  	-moz-box-sizing:border-box;
		  	-webkit-box-sizing:border-box;
			/* background:blue;   */ 
			background-image: linear-gradient(rgb(104, 145, 162),rgb(12, 97, 33)); 
			
		}
		#SW { 
			top:60%; 
			left:0;   
			position:fixed;
			width:70%;
			height:40%;
			padding:1em;
		  	box-sizing:border-box;
		  	-moz-box-sizing:border-box;
		  	-webkit-box-sizing:border-box;
			/* background:green; */
			background-image: linear-gradient(rgb(12, 97, 33),rgb(104, 145, 162));
		}

		#SE { 
			top:60%; 
			left:70%;   
			position:fixed;
			width:30%;
			height:40%;
			padding:1em;
		  	box-sizing:border-box;
		  	-moz-box-sizing:border-box;
		  	-webkit-box-sizing:border-box;
			/* background:red;   */  
			background-image: linear-gradient(rgb(12, 97, 33),rgb(104, 145, 162));
			
		}    
		
		</style>
		
		<link rel="stylesheet" type="text/css" href="http://cdnjs.cloudflare.com/ajax/libs/qtip2/2.2.0/jquery.qtip.css">

		<script src="//cdnjs.cloudflare.com/ajax/libs/qtip2/2.2.0/jquery.qtip.js"></script>
		<script src="//unpkg.com/cytoscape/dist/cytoscape.min.js"></script>
		<script src="javascript/Game.js"></script>
		<script src="javascript/cytoscape.js"></script>
		<script src="javascript/cytoscape-qtip.js"></script>
		
		<style>
		
		#cy {
	        top:0;   
			left:0;   
			position:fixed; 
			width:70%; 
			height:60%;
			padding:1em;
			  box-sizing:border-box;
			 -moz-box-sizing:border-box;
			 -webkit-box-sizing:border-box;	
			
			 	
		 }
		
		</style>
	</head>
	<body onload="connectToServer();">
		<div class = "Main" style="background-image: linear-gradient(rgb(104, 145, 162), rgb(12, 97, 33));">
			<div id = "NW">
				<button onclick="changeMap()" class="btn btn-info" id = "change_map" style = "top: 2%;left: 5%; position: fixed; z-index:10;"> Switch Map </button>
				<div id = "cy" style = "display: block;"></div>
				<image id = "static_map" src = "draft-static-map.png" style = "max-width:100%; max-height:100%; display: none; top: 2%; left: 15%; position: relative;"> </image>
				
			</div>
			<div id="NE">
				<div class="card" style="width: auto; height: -webkit-fill-available; overflow-y: scroll;">
					<div class="card-body" >
						<h5 class="card-title" id="activity_title">Activity Log</h5>
						<div class = "log-box" style = "">
				    		<p class="card-text" id = "activity" style = "height: -webkit-fill-available;" ></p>
				    	</div>
				  	</div>
				</div>
			</div>
			<div id="SW">
				<div class="card" style="width: auto; height: -webkit-fill-available;">
					<div id = "place_troop" style="position: absolute; display: none;">
						<div id = "placeQuestion">Where do you want to place troops?</div>
						<div class = "p1" style = "display: -webkit-box;">
							<select class = "form-control" id="place_troop_location">
	 
							</select>
							<input type="number" class="form-control" id="place_troop_numbers" value = "1" min = "1"/>
						</div>
						<div id = "TroopsRemain">Troops Left: Some numbers</div>
						<button onclick="return placeTroops();" type="button" id = "place_troop_button">Place Troops</button>
					</div>
					<div id = "attack" style="position: absolute; display: none; ">
						<div class = "a1" style = "display: -webkit-box;">
							<div id = "attack_from">Attack From: </div>
							<select class = "form-control" id="attack_from_location" onchange="return updateAttackPossiblities(this.value);">
	
							</select>
						</div>
						<div class = "a2" style = "display: -webkit-box;">
							<div id = "attack_to">Attack To: </div>
							<select class = "form-control" id="attack_to_location">
	
							</select>
						</div>
						<input type="number" class="form-control" id="attack_troop_numbers" value="0" min="0"/>
						<button id = "attack_button" onclick="return attackTerritory();">Attack</button>
						<button id = "attack_finish_button" onclick="return finishAttack();">Finish Attacking</button>
					</div>
					<div id = "move_troop" style="position: absolute; display: none; ">
						<div class = "m1" style = "display: -webkit-box;">
							<div id = "move_from">Move from: </div>
							<select class = "form-control" id="move_from_location" onchange="return updateMovePossibilities(this.value);">
	
							</select>
						</div>
						<div class = "m2" style = "display: -webkit-box;">
							<div id = "move_to">Move To: </div>
							<select class = "form-control" id="move_to_location">
	
							</select>
						</div>
						<input type="number" class="form-control" id="move_troop_numbers" value="0" min="0"/>
						<button id = "move_finish_button" onclick="return finishMove();">Finish</button>
					</div>
					<div id = "waiting_stage" style="position: absolute; display: block; ">
						Waiting for other players to finish...
					</div>
					<div id = "waiting_defend_stage" style="position: absolute; display: none;">
						Waiting for defender to finish selecting troops...
					</div>
				</div>
			</div>
			<div id="SE">
				<!-- <div class="container" style="width: auto; height: -webkit-fill-available;"> -->
				<div class="card" style="width: auto; height: -webkit-fill-available; overflow-y: scroll; overflow-x: scroll;">
					<div class="card-body" >
						<h5 class="card-title">Game Statistics</h5>
				  <!-- <h2>Game Statistics</h2>         -->
				  <table class="table table-bordered" id = "stats_table" style="display: block">
				    <thead>
				      <tr>
				        <th>User</th>
				        <th>Territories</th>
				        <th>Troops</th>
				      </tr>
				    </thead>
				    <tbody>
				      <tr>
				        <td id = "player_1_username">Player 1</td>
				        <td id = "player_1_territories">0</td>
				        <td id = "player_1_troops">0</td>
				      </tr>
				      <tr>
				        <td id = "player_2_username">Player 2</td>
				        <td id = "player_2_territories">0</td>
				        <td id = "player_2_troops">0</td>
				      </tr>
				      <tr>
				        <td id = "player_3_username">Player 3</td>
				        <td id = "player_3_territories">0</td>
				        <td id = "player_3_troops">0</td>
				      </tr>
				      <tr>
				        <td id = "player_4_username">Player 4</td>
				        <td id = "player_4_territories">0</td>
				        <td id = "player_4_troops">0</td>
				      </tr>
				    </tbody>
				  </table>
				  <div id="guest_stats" style="display: none">
				  	Cannot display stats with guest login.
				  	Create an account to see your stats!
				  </div>
				</div>
				</div>
			</div>
		</div> 
	    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery-slim.min.js"><\/script>')</script>
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
	<script type="text/javascript">
	function changeMap() {
	    var x = document.getElementById("static_map");
	    var y = document.getElementById("cy");
	    if (x.style.display === "none") {
	        x.style.display = "block";
	        y.style.display = "none";
	    } else {
	        x.style.display = "none";
	        y.style.display = "block";
	    }
	}
	</script>
	</body>
</html>