package com.cloud.restclientservice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.logging.LoggingFeature;

public class App 
{	
	public static String sendImage(ImageModel image)
	{
		Client client = ClientBuilder
	            .newBuilder()
	            .property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY)
	            .property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "WARNING")
	            .build();
		
    	WebTarget webTarget = client
    			.target("http://localhost:8002/api/protected-service/")
    			.path("/uploadimage");
    	
    	Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
    	Response response = invocationBuilder.post(Entity.entity(image, MediaType.APPLICATION_JSON));
    	return response.toString();
	}
	
	public static String encodeBase64(File image)
	{
		String encodedfile = null;
		FileInputStream fileInputStreamReader = null;
        try 
        {
			fileInputStreamReader = new FileInputStream(image);
            byte[] bytes = new byte[(int)image.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = Base64.getEncoder().encodeToString(bytes);
        } 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }

        return encodedfile;
	}
	
	public static ImageModel processImage(File image)
	{
		ImageModel uploadImage = new ImageModel();
		uploadImage.setImage(encodeBase64(image));
		uploadImage.setCollection("tempcollection");
		return uploadImage;
	}
	
	public static void sendImages(final File folder) 
	{    
		for (File fileEntry : folder.listFiles()) 
	    {
	        if (fileEntry.isFile() && FilenameUtils.getExtension(fileEntry.getAbsolutePath()).equals("dcm")) 
	        {
	        	ImageModel processedImage = processImage(fileEntry);
	        	String response = sendImage(processedImage);
	        	System.out.println(response);
	        }
	    }
	}

    public static void main( String[] args )
    {
    	File folder = new File("C:\\temp\\series-000001");
    	sendImages(folder);
    }
}
