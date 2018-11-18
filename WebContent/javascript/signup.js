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
    
    console.log("/Risk-SC/SignUpServlet?first="+first+"&last="+last+"&username="+username+"&password="+password+"&confirmPassword="+confirmPassword+"&image="+image);
    
    var status = xhttp.response;
    alert(status);
    
    if (status == "Signed Up Successfully!")	{
    	sessionStorage.setItem("username", username);
    }
//    if(status == 0)
//    {
//        sessionStorage.setItem("username", username);
////        window.location.href = "/Risk-SC/LogIn.jsp";
//    }
//    else if(status == 1)
//    {
//        alert("Error! Password must contain at least 8 characters!");
//    }
//    else if(status == 2)
//    {
//        alert("Error! Your password must contain at least one letter.");
//    }
//    else if(status == 3)
//    {
//        alert("Error! Your password must contain at least one number.");
//    }
//    else if(status == 4)
//    {
//        alert("Error! The username you entered already exists.");
//    }	
//    else if (status == 5)	{
//    	alert("Error! The passwords you have entered do not match.")
//    }
    	
    return false;
}