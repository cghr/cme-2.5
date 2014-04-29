/*   1:    */ package org.springframework.jndi;
/*   2:    */ 
/*   3:    */ import javax.naming.NamingException;
/*   4:    */ import org.apache.commons.logging.Log;
/*   5:    */ import org.springframework.util.Assert;
/*   6:    */ 
/*   7:    */ public abstract class JndiLocatorSupport
/*   8:    */   extends JndiAccessor
/*   9:    */ {
/*  10:    */   public static final String CONTAINER_PREFIX = "java:comp/env/";
/*  11: 46 */   private boolean resourceRef = false;
/*  12:    */   
/*  13:    */   public void setResourceRef(boolean resourceRef)
/*  14:    */   {
/*  15: 56 */     this.resourceRef = resourceRef;
/*  16:    */   }
/*  17:    */   
/*  18:    */   public boolean isResourceRef()
/*  19:    */   {
/*  20: 63 */     return this.resourceRef;
/*  21:    */   }
/*  22:    */   
/*  23:    */   protected Object lookup(String jndiName)
/*  24:    */     throws NamingException
/*  25:    */   {
/*  26: 77 */     return lookup(jndiName, null);
/*  27:    */   }
/*  28:    */   
/*  29:    */   protected <T> T lookup(String jndiName, Class<T> requiredType)
/*  30:    */     throws NamingException
/*  31:    */   {
/*  32: 91 */     Assert.notNull(jndiName, "'jndiName' must not be null");
/*  33: 92 */     String convertedName = convertJndiName(jndiName);
/*  34:    */     try
/*  35:    */     {
/*  36: 95 */       jndiObject = getJndiTemplate().lookup(convertedName, requiredType);
/*  37:    */     }
/*  38:    */     catch (NamingException ex)
/*  39:    */     {
/*  40:    */       T jndiObject;
/*  41:    */       T jndiObject;
/*  42: 98 */       if (!convertedName.equals(jndiName))
/*  43:    */       {
/*  44:100 */         if (this.logger.isDebugEnabled()) {
/*  45:101 */           this.logger.debug("Converted JNDI name [" + convertedName + 
/*  46:102 */             "] not found - trying original name [" + jndiName + "]. " + ex);
/*  47:    */         }
/*  48:104 */         jndiObject = getJndiTemplate().lookup(jndiName, requiredType);
/*  49:    */       }
/*  50:    */       else
/*  51:    */       {
/*  52:107 */         throw ex;
/*  53:    */       }
/*  54:    */     }
/*  55:    */     T jndiObject;
/*  56:110 */     if (this.logger.isDebugEnabled()) {
/*  57:111 */       this.logger.debug("Located object with JNDI name [" + convertedName + "]");
/*  58:    */     }
/*  59:113 */     return jndiObject;
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected String convertJndiName(String jndiName)
/*  63:    */   {
/*  64:127 */     if ((isResourceRef()) && (!jndiName.startsWith("java:comp/env/")) && (jndiName.indexOf(':') == -1)) {
/*  65:128 */       jndiName = "java:comp/env/" + jndiName;
/*  66:    */     }
/*  67:130 */     return jndiName;
/*  68:    */   }
/*  69:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jndi.JndiLocatorSupport
 * JD-Core Version:    0.7.0.1
 */