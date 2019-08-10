package com.pldk.component.sliceUpload;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.pldk.common.utils.ToolUtil;
import com.pldk.component.sliceUpload.config.SliceConfig;
import com.pldk.component.sliceUpload.entity.FileInfo;
import com.pldk.component.sliceUpload.entity.UploadExtraParam;
import com.pldk.component.sliceUpload.exception.UploadException;
import com.pldk.component.sliceUpload.interfaces.SliceUpload;


/**
 * 
 * @author suhp
 *
 */
@Component
public class UploadExecute {
	
	@Autowired
	public SliceUpload sliceUpload;

	/**
	 * @author suhp
	 * @param file
	 * @param param
	 * @return
	 * @throws IOException
	 */
	public Object handleUploadFile(MultipartFile file, UploadExtraParam param) throws IOException {
		
		File newDestTempFile=null;
		
		FileInfo fileInfo = new FileInfo();

		String hash = param.getHash();
		String ok = param.getOk();

		String filePath = SliceConfig.reDir();

		String destPath = SliceConfig.getFilePath(filePath);

		File parentFileDir = new File(destPath);

		File parent = parentFileDir.getParentFile();

		File tempFileDir = new File(destPath + File.separator + hash + "-Temp");

		if (!parent.exists()) {
			parent.mkdirs();
		}
		if (!parentFileDir.exists()) {
			parentFileDir.mkdir();
		}
		if (!tempFileDir.exists()) {
			tempFileDir.mkdir();
		}

		Integer sliceCount = param.getSliceCount();
		Integer sliceIndex = param.getSliceIndex();

		File destTempFile = new File(tempFileDir, hash + "_" + sliceCount + "_" + sliceIndex);
		if (destTempFile.isFile() && destTempFile.exists()) {
			fileInfo.setHash(hash);
			fileInfo.setSize(file.getSize());
			return fileInfo;
		}

		file.transferTo(destTempFile);
		if (ok.equals("1") && sliceCount.equals(sliceIndex)) {
			
			boolean uploadDone = true;
			for (int i = 1; i <= sliceCount; i++) {
				File partFile = new File(tempFileDir, hash + "_" + sliceCount + "_" + i);
				if (!partFile.exists()) {
					uploadDone = false;
				}
			}
			
			String origFileName = file.getOriginalFilename();

			fileInfo.setName(origFileName);
			Long fileSize = file.getSize();
			fileInfo.setSize(file.getSize());
			if (null == origFileName || origFileName.isEmpty() || fileSize <= 0) {
				throw new UploadException("文件不能为空");
			}
			
			String fileSuffix = ToolUtil.getFileSuffix(origFileName);
			
			String webUrl = SliceConfig.getWebUrl(filePath, hash) + fileSuffix;
			
			fileInfo.setPath(webUrl);
			
			fileInfo.setMime(fileSuffix);
			
			
			if (uploadDone) {
				newDestTempFile = new File(parentFileDir, hash+fileSuffix);
				for (int i = 1; i <= sliceCount; i++) {
					File partFile = new File(tempFileDir, hash + "_" + sliceCount + "_" + i);
					FileOutputStream destTempfos = new FileOutputStream(newDestTempFile, true);
					FileUtils.copyFile(partFile, destTempfos);
					destTempfos.close();
				}
				FileUtils.deleteDirectory(tempFileDir);
			} else {
				if (sliceIndex == sliceCount - 1) {
					FileUtils.deleteDirectory(tempFileDir);
				}
			}
		} else {
			fileInfo.setHash(hash);
			fileInfo.setSize(file.getSize());
			return fileInfo;
		}

		fileInfo.setHash(hash);
		
		String[] types = {
				  ".mp4",".MP4",".mov",".MOV",".swf",".wmv",".avi",".AVI",".3gp",".mkv",".vob"
		};
		List<String> typeList = Arrays.asList(types);
		if(typeList.contains(fileInfo.getMime())){
			String picPath=convertVideoPic(newDestTempFile);
			fileInfo.setThumbPath(picPath);
		}
		return sliceUpload.afterUpload(fileInfo);
	}
	
	
	 /**
     * suhp
     * @param
     * @param
	 * @throws IOException 
     */
    public String convertVideoPic(File file){
    	try {
	    	FFmpegFrameGrabber ff = new FFmpegFrameGrabber(file);
	  	    ff.start();
	  	    // 截取中间帧图片(具体依实际情况而定)
	  	    int i = 0;
	  	    int length = ff.getLengthInFrames();
	  	    int middleFrame = length / 2;
	  	    Frame frame = null;
	  	    while (i < length) {
	  	      frame = ff.grabFrame();
	  	      if ((i > middleFrame) && (frame.image != null)) {
	  	        break;
	  	      }
	  	      i++;
	  	    }
	  	    // 截取的帧图片
	  	    Java2DFrameConverter converter = new Java2DFrameConverter();
	  	    BufferedImage srcImage = converter.getBufferedImage(frame);
	  	    int srcImageWidth = srcImage.getWidth();
	  	    int srcImageHeight = srcImage.getHeight();
	  	    // 对截图进行等比例缩放(缩略图)
	  	    int width = 480;
	  	    int height = (int) (((double) width / srcImageWidth) * srcImageHeight);
	  	    BufferedImage thumbnailImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
	  	    thumbnailImage.getGraphics().drawImage(srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
	  	    
	  	    String uid=UUID.randomUUID().toString().replace("-", "");
	  	
	        String filePath=SliceConfig.reDir();
	  	
	        String absUrl=SliceConfig.getFilePath(filePath)+File.separator+uid+".png";
	      
	    	String webUrl=SliceConfig.getWebUrl(filePath, uid)+".png";
	  	    
	  	    File picFile = new File(absUrl);
	  	    ImageIO.write(thumbnailImage, "png", picFile);
	  	    ff.stop();
	  	    return webUrl;
    	}catch(IOException io) {
    		return "";
    	}
  	}

}
