package com.pldk.component.sliceUpload.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;




@Data
@Component
@ConfigurationProperties(prefix = "wuxiao")
public class SliceProperties {

    private String slicePath;
	
	private String webPath;
	
	private String serverName="";
	
	private boolean isPath;
	
}
