/*  1:   */ package org.springframework.http.converter.xml;
/*  2:   */ 
/*  3:   */ import java.util.concurrent.ConcurrentHashMap;
/*  4:   */ import java.util.concurrent.ConcurrentMap;
/*  5:   */ import javax.xml.bind.JAXBContext;
/*  6:   */ import javax.xml.bind.JAXBException;
/*  7:   */ import javax.xml.bind.Marshaller;
/*  8:   */ import javax.xml.bind.Unmarshaller;
/*  9:   */ import org.springframework.http.converter.HttpMessageConversionException;
/* 10:   */ import org.springframework.util.Assert;
/* 11:   */ 
/* 12:   */ public abstract class AbstractJaxb2HttpMessageConverter<T>
/* 13:   */   extends AbstractXmlHttpMessageConverter<T>
/* 14:   */ {
/* 15:38 */   private final ConcurrentMap<Class, JAXBContext> jaxbContexts = new ConcurrentHashMap();
/* 16:   */   
/* 17:   */   protected final Marshaller createMarshaller(Class clazz)
/* 18:   */   {
/* 19:   */     try
/* 20:   */     {
/* 21:49 */       JAXBContext jaxbContext = getJaxbContext(clazz);
/* 22:50 */       return jaxbContext.createMarshaller();
/* 23:   */     }
/* 24:   */     catch (JAXBException ex)
/* 25:   */     {
/* 26:53 */       throw new HttpMessageConversionException(
/* 27:54 */         "Could not create Marshaller for class [" + clazz + "]: " + ex.getMessage(), ex);
/* 28:   */     }
/* 29:   */   }
/* 30:   */   
/* 31:   */   protected final Unmarshaller createUnmarshaller(Class clazz)
/* 32:   */     throws JAXBException
/* 33:   */   {
/* 34:   */     try
/* 35:   */     {
/* 36:67 */       JAXBContext jaxbContext = getJaxbContext(clazz);
/* 37:68 */       return jaxbContext.createUnmarshaller();
/* 38:   */     }
/* 39:   */     catch (JAXBException ex)
/* 40:   */     {
/* 41:71 */       throw new HttpMessageConversionException(
/* 42:72 */         "Could not create Unmarshaller for class [" + clazz + "]: " + ex.getMessage(), ex);
/* 43:   */     }
/* 44:   */   }
/* 45:   */   
/* 46:   */   protected final JAXBContext getJaxbContext(Class clazz)
/* 47:   */   {
/* 48:84 */     Assert.notNull(clazz, "'clazz' must not be null");
/* 49:85 */     JAXBContext jaxbContext = (JAXBContext)this.jaxbContexts.get(clazz);
/* 50:86 */     if (jaxbContext == null) {
/* 51:   */       try
/* 52:   */       {
/* 53:88 */         jaxbContext = JAXBContext.newInstance(new Class[] { clazz });
/* 54:89 */         this.jaxbContexts.putIfAbsent(clazz, jaxbContext);
/* 55:   */       }
/* 56:   */       catch (JAXBException ex)
/* 57:   */       {
/* 58:92 */         throw new HttpMessageConversionException(
/* 59:93 */           "Could not instantiate JAXBContext for class [" + clazz + "]: " + ex.getMessage(), ex);
/* 60:   */       }
/* 61:   */     }
/* 62:96 */     return jaxbContext;
/* 63:   */   }
/* 64:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.http.converter.xml.AbstractJaxb2HttpMessageConverter
 * JD-Core Version:    0.7.0.1
 */