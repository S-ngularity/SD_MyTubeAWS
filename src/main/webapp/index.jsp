<%-- 
    Document   : index
    Created on : Dec 9, 2015, 4:41:06 AM
    Author     : Administrator
--%>

<%@ page import = "com.amazonaws.AmazonClientException" %>
<%@ page import = "com.amazonaws.AmazonServiceException" %>
<%@ page import = "com.amazonaws.services.s3.AmazonS3" %>
<%@ page import = "com.amazonaws.services.s3.AmazonS3Client" %>
<%@ page import = "com.amazonaws.services.s3.model.ObjectListing" %>
<%@ page import = "com.amazonaws.services.s3.model.PutObjectRequest" %>
<%@ page import = "com.amazonaws.services.s3.model.S3ObjectSummary" %>
<%@ page import = "com.amazonaws.util.StringUtils" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="/SD_MyTubeAWS/resources/allmedia.css" />
        <title>MyTube</title>

		<link rel="shortcut icon" href="http://i.imgur.com/kSaAQGi.png"/>

    </head>
    <body>
		<h1 class="indexHeader">MyTube - Video Storage</h1>
		<div id="superHolder">

			<table id="listTable">
				<tr id="listTableHeader">
					<th>Name</td>
					<th>Size</td> 
					<th>Date</td>
				</tr>
				<%
					AmazonS3 s3 = new AmazonS3Client();
					final String bucketName = "darkspock";

					ObjectListing objects = s3.listObjects(bucketName);
					do
					{
						for (S3ObjectSummary objectSummary : objects.getObjectSummaries())
						{
							%><tr>
								<td><%out.println(objectSummary.getKey());%></td>
								<td><%out.println(Long.toString(objectSummary.getSize()));%>bytes</td>
								<td><%out.println(StringUtils.fromDate(objectSummary.getLastModified()));%></td>
							</tr><%
						}
						objects = s3.listNextBatchOfObjects(objects);
					} while (objects.isTruncated());
				%>
			</table>

			<div class="uploadDiv">
				<h3>Upload</h3>
				<form method="POST" name="Upload" action="UploadServlet" enctype="multipart/form-data" >
					File: <input type="file" name="videoFileId" id="videoFileId">
					<br>
					<br>
					<input type="submit" value="Upload">
				</form>
			</div>

			<br>
			<br>
			<br>
			<br>
			<div class="uploadDiv">
				<h3>Download</h3>
				<form method="POST" name="Download" action="DownloadServlet">
					Name: <input type="text" name="downloadKey">
					<input type="submit" value="Download">
				</form>
			</div>
		</div>
    </body>
</html>
