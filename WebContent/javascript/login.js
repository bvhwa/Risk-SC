function validate()
{
    var xhttp = new XMLHttpRequest();
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    xhttp.open("GET", "/Risk-SC/LoginServlet?username="+username+"&password="+password, false);
    xhttp.send();
    var status = xhttp.response;
    if(status)
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