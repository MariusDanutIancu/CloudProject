package com.cloud.protectedservice.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import com.cloud.protectedservice.model.ImageModel;
import com.cloud.protectedservice.model.ProtectedModel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RefreshScope
@RestController
public class ProtectedServiceController {
	
	Logger logger = LoggerFactory.getLogger(ProtectedServiceController.class);
	
    @Value("${external.property}") String property;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
    	logger.info("Request /hello");
        return ResponseEntity.ok(property);
    }
    
    @GetMapping("/data")
    public ResponseEntity<ProtectedModel> data() {
    	logger.info("Request /data");
    	ProtectedModel data = new ProtectedModel();
    	data.setData("data");
        return ResponseEntity.ok(data);
    }
    
    @PostMapping("/uploadimage")
    public ResponseEntity<String> image(@RequestBody ImageModel image)
    {
    	logger.info("Request /uploadimage");
    	Path directoryPath = Paths.get(System.getProperty("java.io.tmpdir"));
    	
    	byte[] imageByte = null;
    	if (image.getImage()!= null)
    	{
    		imageByte = Base64.getDecoder().decode(image.getImage());
    	}
    	
    	if(image.getCollection() != null)
		{
    		directoryPath = createDirectory(image);
		}
    	
        if(saveImage(imageByte, new File(directoryPath.toString())) == true)
        {
        	return ResponseEntity.ok("ok");
        }
        else
        {
        	return ResponseEntity.ok("notok");
        }
    }
    
    private Path createDirectory(ImageModel image)
    {
    	Path directoryPath = Paths.get(System.getProperty("java.io.tmpdir"), image.getCollection());
		try 
		{
			Files.createDirectories(directoryPath);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return directoryPath;
    }
    
    private boolean saveImage(byte[] imageByte, File file)
    {
    	try 
    	{   	
    		File savedImage = File.createTempFile("image", ".dcm", file);
    		new FileOutputStream(savedImage.getAbsolutePath()).write(imageByte);
    		logger.info("Request /uploadimage: " + savedImage.getAbsolutePath());
    		return true;
		} 
    	catch (FileNotFoundException e) 
    	{
			e.printStackTrace();
		} 
    	catch (IOException e) 
    	{
    		e.printStackTrace();
    	}
    
    	return false;
    }
}