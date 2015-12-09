/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mytubeaws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "ListServlet", urlPatterns =
	{
		"/ListServlet"
})
public class ListServlet extends HttpServlet
{

	private AmazonS3 s3;
	final String bucketName = "darkspock";
	
	@Override
	public void init() throws ServletException
	{
		s3 = new AmazonS3Client();
	}

	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		ArrayList<String> nameList = new ArrayList<>();
		ArrayList<String> sizeList = new ArrayList<>();
		ArrayList<String> dateList = new ArrayList<>();
		
		ObjectListing objects = s3.listObjects(bucketName);
		do
		{
			for (S3ObjectSummary objectSummary : objects.getObjectSummaries())
			{
				nameList.add(objectSummary.getKey());
				sizeList.add(Long.toString(objectSummary.getSize()));
				dateList.add(StringUtils.fromDate(objectSummary.getLastModified()));
			}
			objects = s3.listNextBatchOfObjects(objects);
		} while (objects.isTruncated());
		
		request.setAttribute("nameList", nameList);
		request.setAttribute("sizeList", sizeList);
		request.setAttribute("dateList", dateList);
		request.getRequestDispatcher("/UploadResult.jsp").forward(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo()
	{
		return "MyTube list servlet.";
	}

}
