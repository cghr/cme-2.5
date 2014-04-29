/*   1:    */ package org.springframework.http.converter.xml;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.nio.charset.Charset;
/*   5:    */ import javax.xml.bind.JAXBElement;
/*   6:    */ import javax.xml.bind.JAXBException;
/*   7:    */ import javax.xml.bind.MarshalException;
/*   8:    */ import javax.xml.bind.Marshaller;
/*   9:    */ import javax.xml.bind.PropertyException;
/*  10:    */ import javax.xml.bind.UnmarshalException;
/*  11:    */ import javax.xml.bind.Unmarshaller;
/*  12:    */ import javax.xml.bind.annotation.XmlRootElement;
/*  13:    */ import javax.xml.bind.annotation.XmlType;
/*  14:    */ import javax.xml.transform.Result;
/*  15:    */ import javax.xml.transform.Source;
/*  16:    */ import org.springframework.core.annotation.AnnotationUtils;
/*  17:    */ import org.springframework.http.HttpHeaders;
/*  18:    */ import org.springframework.http.MediaType;
/*  19:    */ import org.springframework.http.converter.HttpMessageConversionException;
/*  20:    */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*  21:    */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*  22:    */ import org.springframework.util.ClassUtils;
/*  23:    */ 
/*  24:    */ public class Jaxb2RootElementHttpMessageConverter
/*  25:    */   extends AbstractJaxb2HttpMessageConverter<Object>
/*  26:    */ {
/*  27:    */   public boolean canRead(Class<?> clazz, MediaType mediaType)
/*  28:    */   {
/*  29: 55 */     return ((clazz.isAnnotationPresent(XmlRootElement.class)) || (clazz.isAnnotationPresent(XmlType.class))) && (canRead(mediaType));
/*  30:    */   }
/*  31:    */   
/*  32:    */   public boolean canWrite(Class<?> clazz, MediaType mediaType)
/*  33:    */   {
/*  34: 60 */     return (AnnotationUtils.findAnnotation(clazz, XmlRootElement.class) != null) && (canWrite(mediaType));
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected boolean supports(Class<?> clazz)
/*  38:    */   {
/*  39: 66 */     throw new UnsupportedOperationException();
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected Object readFromSource(Class<?> clazz, HttpHeaders headers, Source source)
/*  43:    */     throws IOException
/*  44:    */   {
/*  45:    */     try
/*  46:    */     {
/*  47: 72 */       Unmarshaller unmarshaller = createUnmarshaller(clazz);
/*  48: 73 */       if (clazz.isAnnotationPresent(XmlRootElement.class)) {
/*  49: 74 */         return unmarshaller.unmarshal(source);
/*  50:    */       }
/*  51: 77 */       JAXBElement jaxbElement = unmarshaller.unmarshal(source, clazz);
/*  52: 78 */       return jaxbElement.getValue();
/*  53:    */     }
/*  54:    */     catch (UnmarshalException ex)
/*  55:    */     {
/*  56: 82 */       throw new HttpMessageNotReadableException("Could not unmarshal to [" + clazz + "]: " + ex.getMessage(), ex);
/*  57:    */     }
/*  58:    */     catch (JAXBException ex)
/*  59:    */     {
/*  60: 86 */       throw new HttpMessageConversionException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
/*  61:    */     }
/*  62:    */   }
/*  63:    */   
/*  64:    */   protected void writeToResult(Object o, HttpHeaders headers, Result result)
/*  65:    */     throws IOException
/*  66:    */   {
/*  67:    */     try
/*  68:    */     {
/*  69: 93 */       Class clazz = ClassUtils.getUserClass(o);
/*  70: 94 */       Marshaller marshaller = createMarshaller(clazz);
/*  71: 95 */       setCharset(headers.getContentType(), marshaller);
/*  72: 96 */       marshaller.marshal(o, result);
/*  73:    */     }
/*  74:    */     catch (MarshalException ex)
/*  75:    */     {
/*  76: 99 */       throw new HttpMessageNotWritableException("Could not marshal [" + o + "]: " + ex.getMessage(), ex);
/*  77:    */     }
/*  78:    */     catch (JAXBException ex)
/*  79:    */     {
/*  80:102 */       throw new HttpMessageConversionException("Could not instantiate JAXBContext: " + ex.getMessage(), ex);
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   private void setCharset(MediaType contentType, Marshaller marshaller)
/*  85:    */     throws PropertyException
/*  86:    */   {
/*  87:107 */     if ((contentType != null) && (contentType.getCharSet() != null)) {
/*  88:108 */       marshaller.setProperty("jaxb.encoding", contentType.getCharSet().name());
/*  89:    */     }
/*  90:    */   }
/*  91:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter
 * JD-Core Version:    0.7.0.1
 */