package org.springframework.web.multipart;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public abstract interface MultipartFile
{
  public abstract String getName();
  
  public abstract String getOriginalFilename();
  
  public abstract String getContentType();
  
  public abstract boolean isEmpty();
  
  public abstract long getSize();
  
  public abstract byte[] getBytes()
    throws IOException;
  
  public abstract InputStream getInputStream()
    throws IOException;
  
  public abstract void transferTo(File paramFile)
    throws IOException, IllegalStateException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.multipart.MultipartFile
 * JD-Core Version:    0.7.0.1
 */