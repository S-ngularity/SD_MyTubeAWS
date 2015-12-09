<%-- 
    Document   : DownloadResult
    Created on : Dec 9, 2015, 6:22:46 AM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="/SD_MyTubeAWS/resources/allmedia.css" />
        <title>MyTube - Download</title>
	
	<link rel="shortcut icon" href="http://i.imgur.com/kSaAQGi.png"/>
    </head>
    <body>
		<h1 class="indexHeader">MyTube - Video Storage</h1>
		<div class="superHolderResult">
			<br>
			<%= request.getAttribute("resultText")%>
			<br>
			<br>
			<a href="<%= request.getAttribute("downloadLink")%>">DOWNLOAD</a>
			<br>
			<br>
			<button onclick="location.href='index.jsp'">Home</button>
		</div>
    </body>
</html>
