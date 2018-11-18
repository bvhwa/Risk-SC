function login()
{
    var xhttp = new XMLHttpRequest();
    var username = document.getElementById("inputName").value;
    var password = document.getElementById("inputPassword").value;
    xhttp.open("GET", "/Risk-SC/LoginServlet?username="+username+"&password="+password, false);
    xhttp.send();
    var status = xhttp.response;
    if(status == 0)
    {
        sessionStorage.setItem("username", username);
        window.location.href = "/Risk-SC/WaitingRoom.jsp";
    }
    else
    {
        document.getElementById("error").innerHTML = "Error! Your username and password did not match an existing record.";
    }

    return false;
}

function signUp()
{
    var xhttp = new XMLHttpRequest();
    var username = document.getElementById("inputName").value;
    var password = document.getElementById("inputPassword").value;

    window.location.href = "/Risk-SC/SignUp.jsp";

    xhttp.open("GET", "/Risk-SC/SignUpServlet?f&username="+username+"&password="+password, false);
    xhttp.send();
    
    var status = xhttp.response;
    if(status == 0)
    {
        sessionStorage.setItem("username", username);
        window.location.href = "/Risk-SC/SignUp.jsp";
    }
    else if(status == 1)
    {
        alert("Error! Password must contain at least 8 characters!");
    }
    else if(status == 2)
    {
        alert("Error! Your password must contain at least one letter.");
    }
    else if(status == 3)
    {
        alert("Error! Your password must contain at least one number.");
    }
    else if(status == 4)
    {
        alert("Error! Your The username you entered already exists.");
    }

    return false;
}


function Guest()
{
	var xhttp = new XMLHttpRequest();
	var username = "guest";
	var password = "ThisIsALongPassword";
    xhttp.open("GET", "/Risk-SC/SignUpServlet?f&username="+username+"&password="+password, true);
    xhttp.send();
    window.location.href = "/Risk-SC/WaitingRoom.jsp";
    return false;
}