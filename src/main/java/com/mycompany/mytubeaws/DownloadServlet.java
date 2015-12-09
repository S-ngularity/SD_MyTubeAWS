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
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletOutputStream;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Administrator
 */
@WebServlet(name = "DownloadServlet", urlPatterns =
	{
		"/DownloadServlet"
})
public class DownloadServlet extends HttpServlet
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
		String result = "";
		String downloadLink = "";
		
		String inputKey = request.getParameter("downloadKey");
		
		if(inputKey == null)
			result += "inputKey is null; ";
		else if(inputKey.length() == 0)
			result += "inputKey is blank; ";
		else
		{
			result += "Downloading an object; ";
			
			try
			{
//				S3Object object = s3.getObject(new GetObjectRequest(bucketName, inputKey));
//				S3ObjectInputStream objectContent = object.getObjectContent();
//				
//				File f = File.createTempFile("aws-java-sdk-download", "");
//				FileOutputStream fos = new FileOutputStream(f);
//				IOUtils.copy(objectContent, fos);
//				fos.close();
//				
//				result += "Content-Type: '" + object.getObjectMetadata().getContentType() + "'; ";
//				result += "Mime '" + new MimetypesFileTypeMap().getContentType(f) + "'; ";
//				
//				response.setContentType(new MimetypesFileTypeMap().getContentType(f));
//				response.setHeader("Content-Disposition","attachment;filename="+inputKey);
//				
//				ServletOutputStream out = response.getOutputStream();
//				
//				
//				f.delete();
				
				GeneratePresignedUrlRequest requestUrl = new GeneratePresignedUrlRequest(bucketName, inputKey);
				downloadLink = s3.generatePresignedUrl(requestUrl).toString();
				System.out.println(downloadLink);
				//result += downloadLink + " ; ";
			}
			catch (AmazonServiceException ase) {
				System.out.println("Caught an AmazonServiceException, which means your request made it "
					  + "to Amazon S3, but was rejected with an error response for some reason.");
				System.out.println("Error Message:    " + ase.getMessage());
				System.out.println("HTTP Status Code: " + ase.getStatusCode());
				System.out.println("AWS Error Code:   " + ase.getErrorCode());
				System.out.println("Error Type:       " + ase.getErrorType());
				System.out.println("Request ID:       " + ase.getRequestId());
				
				result += "AmazonServiceException thrown; ";
			}
			catch (AmazonClientException ace) {
				System.out.println("Caught an AmazonClientException, which means the client encountered "
					  + "a serious internal problem while trying to communicate with S3, "
					  + "such as not being able to access the network.");
				System.out.println("Error Message: " + ace.getMessage());
								
				result += "AmazonClientException thrown; ";
			}
		}
		
		System.out.println(result);
		
		request.setAttribute("resultText", result);
		request.setAttribute("downloadLink", downloadLink);
		request.getRequestDispatcher("/DownloadResult.jsp").forward(request, response);
		//response.sendRedirect("UploadResult.jsp");
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo()
	{
		return "MyTube download servlet.";
	}
}
