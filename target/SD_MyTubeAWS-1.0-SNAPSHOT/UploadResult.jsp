<%-- 
    Document   : UploadResult
    Created on : Dec 9, 2015, 5:21:26 AM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="/resources/allmedia.css" />
        <title>MyTube - Upload</title>
	
	<link rel="shortcut icon" href="http://i.imgur.com/kSaAQGi.png"/>
    </head>
    <body>
		<h1 class="indexHeader">MyTube - Video Storage</h1>
		<div class="superHolderResult">
			<br>
			<%= request.getAttribute("resultText")%>
			<br>
			<br>
			<button onclick="location.href='index.jsp'">Home</button>
		</div>
    </body>
</html>
