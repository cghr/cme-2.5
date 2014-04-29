/*  1:   */ package org.springframework.http.converter.xml;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import javax.xml.transform.Result;
/*  5:   */ import javax.xml.transform.Source;
/*  6:   */ import javax.xml.transform.Transformer;
/*  7:   */ import javax.xml.transform.TransformerException;
/*  8:   */ import javax.xml.transform.TransformerFactory;
/*  9:   */ import javax.xml.transform.stream.StreamResult;
/* 10:   */ import javax.xml.transform.stream.StreamSource;
/* 11:   */ import org.springframework.http.HttpHeaders;
/* 12:   */ import org.springframework.http.HttpInputMessage;
/* 13:   */ import org.springframework.http.HttpOutputMessage;
/* 14:   */ import org.springframework.http.MediaType;
/* 15:   */ import org.springframework.http.converter.AbstractHttpMessageConverter;
/* 16:   */ 
/* 17:   */ public abstract class AbstractXmlHttpMessageConverter<T>
/* 18:   */   extends AbstractHttpMessageConverter<T>
/* 19:   */ {
/* 20:47 */   private final TransformerFactory transformerFactory = TransformerFactory.newInstance();
/* 21:   */   
/* 22:   */   protected AbstractXmlHttpMessageConverter()
/* 23:   */   {
/* 24:55 */     super(new MediaType[] { MediaType.APPLICATION_XML, MediaType.TEXT_XML, new MediaType("application", "*+xml") });
/* 25:   */   }
/* 26:   */   
/* 27:   */   public final T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage)
/* 28:   */     throws IOException
/* 29:   */   {
/* 30:61 */     return readFromSource(clazz, inputMessage.getHeaders(), new StreamSource(inputMessage.getBody()));
/* 31:   */   }
/* 32:   */   
/* 33:   */   protected final void writeInternal(T t, HttpOutputMessage outputMessage)
/* 34:   */     throws IOException
/* 35:   */   {
/* 36:66 */     writeToResult(t, outputMessage.getHeaders(), new StreamResult(outputMessage.getBody()));
/* 37:   */   }
/* 38:   */   
/* 39:   */   protected void transform(Source source, Result result)
/* 40:   */     throws TransformerException
/* 41:   */   {
/* 42:76 */     this.transformerFactory.newTransformer().transform(source, result);
/* 43:   */   }
/* 44:   */   
/* 45:   */   protected abstract T readFromSource(Class<? extends T> paramClass, HttpHeaders paramHttpHeaders, Source paramSource)
/* 46:   */     throws IOException;
/* 47:   */   
/* 48:   */   protected abstract void writeToResult(T paramT, HttpHeaders paramHttpHeaders, Result paramResult)
/* 49:   */     throws IOException;
/* 50:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.converter.xml.AbstractXmlHttpMessageConverter
 * JD-Core Version:    0.7.0.1
 */