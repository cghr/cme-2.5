package org.springframework.web.multipart;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.util.MultiValueMap;

public abstract interface MultipartRequest
{
  public abstract Iterator<String> getFileNames();
  
  public abstract MultipartFile getFile(String paramString);
  
  public abstract List<MultipartFile> getFiles(String paramString);
  
  public abstract Map<String, MultipartFile> getFileMap();
  
  public abstract MultiValueMap<String, MultipartFile> getMultiFileMap();
  
  public abstract String getMultipartContentType(String paramString);
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.multipart.MultipartRequest
 * JD-Core Version:    0.7.0.1
 */