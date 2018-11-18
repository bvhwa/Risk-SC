<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<link rel="stylesheet" type="text/css" href="LogInHelp.css" />
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
<script src="javascript/signup.js"></script>


    <div class="container">
        <div class="card card-container">
        	<div class ="title">Risk SC</div>
            <img id="profile-img" class="profile-img-card" src="trojan.png" style = "width: 200px; height: 200px;"/>
            <p id="profile-name" class="profile-name-card"></p>
            <form class="form-signin1" onsubmit="return signUp();">
            	<input type="firstname" id="fname" class="form-control" placeholder="First Name" style = "margin-bottom:10px;">
                <input type="lastname" id="lname" class="form-control" placeholder="Last Name" style = "margin-bottom:10px;">
				<input type="pimage" id="image" class="form-control" placeholder="Profile Image URL" style = "margin-bottom:10px;">
				<input type="_username" id="_inputName" class="form-control" placeholder="Username" style = "margin-bottom:10px;">
                <input type="_password" id="_inputPassword" class="form-control" placeholder="Password" style = "margin-bottom:10px;">
                <input type="cpassword" id="confirmPassword" class="form-control" placeholder="Confirm Password" style = "margin-bottom:10px;">
                <button class="btn btn-lg btn-primary btn-block btn-signin" type="submit" style = "margin-bottom:0px;">Sign up</button>
            </form><!-- /form -->
        </div><!-- /card-container -->
    </div><!-- /container -->