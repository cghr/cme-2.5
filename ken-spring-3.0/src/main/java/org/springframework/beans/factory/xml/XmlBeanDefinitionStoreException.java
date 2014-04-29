/*  1:   */ package org.springframework.beans.factory.xml;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*  4:   */ import org.xml.sax.SAXException;
/*  5:   */ import org.xml.sax.SAXParseException;
/*  6:   */ 
/*  7:   */ public class XmlBeanDefinitionStoreException
/*  8:   */   extends BeanDefinitionStoreException
/*  9:   */ {
/* 10:   */   public XmlBeanDefinitionStoreException(String resourceDescription, String msg, SAXException cause)
/* 11:   */   {
/* 12:44 */     super(resourceDescription, msg, cause);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public int getLineNumber()
/* 16:   */   {
/* 17:53 */     Throwable cause = getCause();
/* 18:54 */     if ((cause instanceof SAXParseException)) {
/* 19:55 */       return ((SAXParseException)cause).getLineNumber();
/* 20:   */     }
/* 21:57 */     return -1;
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.XmlBeanDefinitionStoreException
 * JD-Core Version:    0.7.0.1
 */