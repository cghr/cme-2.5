/*  1:   */ package org.springframework.jmx.support;
/*  2:   */ 
/*  3:   */ import java.util.Hashtable;
/*  4:   */ import javax.management.MalformedObjectNameException;
/*  5:   */ import javax.management.ObjectName;
/*  6:   */ 
/*  7:   */ public class ObjectNameManager
/*  8:   */ {
/*  9:   */   public static ObjectName getInstance(Object objectName)
/* 10:   */     throws MalformedObjectNameException
/* 11:   */   {
/* 12:43 */     if ((objectName instanceof ObjectName)) {
/* 13:44 */       return (ObjectName)objectName;
/* 14:   */     }
/* 15:46 */     if (!(objectName instanceof String)) {
/* 16:47 */       throw new MalformedObjectNameException("Invalid ObjectName value type [" + 
/* 17:48 */         objectName.getClass().getName() + "]: only ObjectName and String supported.");
/* 18:   */     }
/* 19:50 */     return getInstance((String)objectName);
/* 20:   */   }
/* 21:   */   
/* 22:   */   public static ObjectName getInstance(String objectName)
/* 23:   */     throws MalformedObjectNameException
/* 24:   */   {
/* 25:62 */     return ObjectName.getInstance(objectName);
/* 26:   */   }
/* 27:   */   
/* 28:   */   public static ObjectName getInstance(String domainName, String key, String value)
/* 29:   */     throws MalformedObjectNameException
/* 30:   */   {
/* 31:79 */     return ObjectName.getInstance(domainName, key, value);
/* 32:   */   }
/* 33:   */   
/* 34:   */   public static ObjectName getInstance(String domainName, Hashtable<String, String> properties)
/* 35:   */     throws MalformedObjectNameException
/* 36:   */   {
/* 37:95 */     return ObjectName.getInstance(domainName, properties);
/* 38:   */   }
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.support.ObjectNameManager
 * JD-Core Version:    0.7.0.1
 */