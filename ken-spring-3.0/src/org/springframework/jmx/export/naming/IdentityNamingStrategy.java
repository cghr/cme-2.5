/*  1:   */ package org.springframework.jmx.export.naming;
/*  2:   */ 
/*  3:   */ import java.util.Hashtable;
/*  4:   */ import javax.management.MalformedObjectNameException;
/*  5:   */ import javax.management.ObjectName;
/*  6:   */ import org.springframework.jmx.support.ObjectNameManager;
/*  7:   */ import org.springframework.util.ClassUtils;
/*  8:   */ import org.springframework.util.ObjectUtils;
/*  9:   */ 
/* 10:   */ public class IdentityNamingStrategy
/* 11:   */   implements ObjectNamingStrategy
/* 12:   */ {
/* 13:   */   public static final String TYPE_KEY = "type";
/* 14:   */   public static final String HASH_CODE_KEY = "hashCode";
/* 15:   */   
/* 16:   */   public ObjectName getObjectName(Object managedBean, String beanKey)
/* 17:   */     throws MalformedObjectNameException
/* 18:   */   {
/* 19:51 */     String domain = ClassUtils.getPackageName(managedBean.getClass());
/* 20:52 */     Hashtable keys = new Hashtable();
/* 21:53 */     keys.put("type", ClassUtils.getShortName(managedBean.getClass()));
/* 22:54 */     keys.put("hashCode", ObjectUtils.getIdentityHexString(managedBean));
/* 23:55 */     return ObjectNameManager.getInstance(domain, keys);
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.naming.IdentityNamingStrategy
 * JD-Core Version:    0.7.0.1
 */