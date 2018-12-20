function signUp()
{
    var xhttp = new XMLHttpRequest();
    var first = document.getElementById("fname").value;
    var last = document.getElementById("lname").value;
    var image = document.getElementById("image").value;
    var username = document.getElementById("_inputName").value;
    var password = document.getElementById("_inputPassword").value;
    var confirmPassword = document.getElementById("confirmPassword").value;
    
    xhttp.open("GET", "/Risk-SC/SignUpServlet?first="+first+"&last="+last+"&username="+username+"&password="+password+"&confirmPassword="+confirmPassword+"&image="+image, false);
    xhttp.send();
    
    // console.log("/Risk-SC/server/SignUpServlet?first="+first+"&last="+last+"&username="+username+"&password="+password+"&confirmPassword="+confirmPassword+"&image="+image);
    
    var status = xhttp.response;
    alert(status);
    
    if (status == "Signed Up!")	{
    	sessionStorage.setItem("username", username);
    	window.location.href = "/Risk-SC/LogIn.jsp";
    }
    	
    return false;
}