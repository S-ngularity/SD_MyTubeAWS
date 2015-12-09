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
//import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
//import com.amazonaws.services.s3.model.ListObjectsRequest;
//import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
//import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
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
@WebServlet(name = "UploadServlet", urlPatterns =
	{
		"/UploadServlet"
})
@MultipartConfig
public class UploadServlet extends HttpServlet
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

		// configures upload settings
		DiskFileItemFactory factory = new DiskFileItemFactory(); // sets memory threshold - beyond which files are stored in disk
		factory.setSizeThreshold(1024*1024*3); // 3mb
		factory.setRepository(new File(System.getProperty("java.io.tmpdir"))); // sets temporary location to store files

		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setFileSizeMax(1024*1024*40); // sets maximum size of upload file
		upload.setSizeMax(1024*1024*50); // sets maximum size of request (include file + form data)

		try{
			List<FileItem> formItems = upload.parseRequest(request); // parses the request's content to extract file data

			if (formItems != null && formItems.size() > 0) // iterates over form's fields
			{
				for (FileItem item : formItems) // processes only fields that are not form fields
				{
					if (!item.isFormField())
					{
						String fileName = item.getName();

						File file = File.createTempFile("aws-java-sdk-upload", "");
						item.write(file); // write form item to file (?)

						try
						{
							s3.putObject(new PutObjectRequest(bucketName, fileName, file));
							result += "File uploaded successfully; ";
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

						file.delete();
					}
				}
			}
		} catch (Exception ex) {
			result += "Generic exception: '" + ex.getMessage() + "'; ";
			ex.printStackTrace();
		}
		
		System.out.println(result);
		
		request.setAttribute("resultText", result);
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
		return "MyTube upload servlet.";
	}

}
