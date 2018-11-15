<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<link rel="stylesheet" type="text/css" href="LogInHelp.css" />
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>


    <div class="container">
        <div class="card card-container">
        	<div class ="title">Risk SC</div>
            <img id="profile-img" class="profile-img-card" src="trojan.png" style = "width: 200px; height: 200px;"/>
            <p id="profile-name" class="profile-name-card"></p>
            <form class="form-signin1">
				<input type="username" id="inputName" class="form-control" placeholder="Username" style = "margin-bottom:10px;">
                <input type="password" id="inputPassword" class="form-control" placeholder="Password" style = "margin-bottom:10px;">
                <button class="btn btn-lg btn-primary btn-block btn-signin" type="submit" onClick="location.href='SignUp.jsp'">Sign up</button>
                <button class="btn btn-lg btn-primary btn-block btn-signin" type="submit">Continue as Guest</button>
                <button class="btn btn-lg btn-primary btn-block btn-signin" type="submit">Log in</button>
            </form><!-- /form -->
        </div><!-- /card-container -->
    </div><!-- /container -->