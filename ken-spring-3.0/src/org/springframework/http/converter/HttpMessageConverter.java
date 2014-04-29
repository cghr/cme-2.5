package org.springframework.http.converter;

import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;

public abstract interface HttpMessageConverter<T>
{
  public abstract boolean canRead(Class<?> paramClass, MediaType paramMediaType);
  
  public abstract boolean canWrite(Class<?> paramClass, MediaType paramMediaType);
  
  public abstract List<MediaType> getSupportedMediaTypes();
  
  public abstract T read(Class<? extends T> paramClass, HttpInputMessage paramHttpInputMessage)
    throws IOException, HttpMessageNotReadableException;
  
  public abstract void write(T paramT, MediaType paramMediaType, HttpOutputMessage paramHttpOutputMessage)
    throws IOException, HttpMessageNotWritableException;
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.converter.HttpMessageConverter
 * JD-Core Version:    0.7.0.1
 */