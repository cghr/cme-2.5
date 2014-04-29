/*  1:   */ package org.springframework.jndi;
/*  2:   */ 
/*  3:   */ import javax.naming.NamingException;
/*  4:   */ 
/*  5:   */ public class JndiLocatorDelegate
/*  6:   */   extends JndiLocatorSupport
/*  7:   */ {
/*  8:   */   public Object lookup(String jndiName)
/*  9:   */     throws NamingException
/* 10:   */   {
/* 11:32 */     return super.lookup(jndiName);
/* 12:   */   }
/* 13:   */   
/* 14:   */   public <T> T lookup(String jndiName, Class<T> requiredType)
/* 15:   */     throws NamingException
/* 16:   */   {
/* 17:37 */     return super.lookup(jndiName, requiredType);
/* 18:   */   }
/* 19:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jndi.JndiLocatorDelegate
 * JD-Core Version:    0.7.0.1
 */