package com.pldk.component.sliceUpload.config;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pldk.common.utils.SpringContextUtil;
import com.pldk.component.fileUpload.config.properties.UploadProjectProperties;
import com.pldk.component.sliceUpload.config.properties.SliceProperties;

@Component
public class SliceConfig {
	
	@Autowired
	SliceProperties sliceProperties;
	
    private static String fileDir="file";
	
    public static String slicePath;
    
    public static String webPath;
    
    public static boolean isPath;
    
    public static String serverName="";
    
	@PostConstruct
    public void setSlicePath() {
    	slicePath=sliceProperties.getSlicePath();
    }
    
	@PostConstruct
    public void setWebPath() {
		webPath=sliceProperties.getWebPath();
    }
    
   	@PostConstruct
    public void setIsPath() {
    	isPath=sliceProperties.isPath();
    }
   	
   	@PostConstruct
	public void setServerName() {
   		serverName=sliceProperties.getServerName();
	}

    public static String getFilePath(String filePath) {
    	if(isPath) {
    		return slicePath+File.separator+fileDir+File.separator+filePath;
    	}
    	return getUploadPath()+File.separator+fileDir+File.separator+filePath;
    }
    
    public static String getWebUrl(String filePath,String hash) {
    	if(isPath) {
    		return webPath+"/"+fileDir+"/"+filePath+"/"+hash;
    	}
    	return serverName+getPathPattern()+"/"+fileDir+"/"+filePath+"/"+hash;
    }
    
    public static String getUploadPath(){
        UploadProjectProperties properties = SpringContextUtil.getBean(UploadProjectProperties.class);
        return properties.getFilePath();
    }
    public static String reDir() {  
        Long now = Long.parseLong(new SimpleDateFormat("yyyyMMdd").format(new Date()));    
        String fileName =""+now;    
        return fileName;  
    }
    
    public static String getPathPattern(){
        UploadProjectProperties properties = SpringContextUtil.getBean(UploadProjectProperties.class);
        return properties.getStaticPath().replace("/**", "");
    }
    
}
