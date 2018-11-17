function signUp()
{
    var xhttp = new XMLHttpRequest();
    var name = document.getElementById("name").value;
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var confirmPassword = document.getElementById("confirmPassword").value;


    xhttp.open("GET", "/Risk-SC/SignUpServlet?name="+name+"&username="+username+"&password="+password, 
    		"&confirmPassword"+confirmPassword, false);
    xhttp.send();
    
    var status = xhttp.response;
    if(status == 0)
    {
        sessionStorage.setItem("username", username);
        window.location.href = "/Risk-SC/LogIn.jsp";
    }
    else if(status == 1)
    {
        document.getElementById("error").innerHTML = "Error! Password must contain at least 8 characters!";
    }
    else if(status == 2)
    {
        document.getElementById("error").innerHTML = "Error! Your password must contain at least one letter."
    }
    else if(status == 3)
    {
        document.getElementById("error").innerHTML = "Error! Your password must contain at least one number."
    }
    else if(status == 4)
    {
        document.getElementById("error").innerHTML = "Error! Your The username you entered already exists."
    }

    return false;
}