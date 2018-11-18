function login()
{
    var xhttp = new XMLHttpRequest();
    var username = document.getElementById("inputName").value;
    var password = document.getElementById("inputPassword").value;
    xhttp.open("GET", "/Risk-SC/LogInServlet?username="+username+"&password="+password, false);
    xhttp.send();

    var status = xhttp.response;
    alert(status);
    if(status == "User Signed In")
    {
        sessionStorage.setItem("username", username);
        window.location.href = "/Risk-SC/WaitingRoom.jsp";
    }    

    return false;
}

function signUp()	{

    window.location.href = "/Risk-SC/SignUp.jsp";

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