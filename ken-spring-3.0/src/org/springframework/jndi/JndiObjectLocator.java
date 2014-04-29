/*   1:    */ package org.springframework.jndi;
/*   2:    */ 
/*   3:    */ import javax.naming.NamingException;
/*   4:    */ import org.springframework.beans.factory.InitializingBean;
/*   5:    */ import org.springframework.util.StringUtils;
/*   6:    */ 
/*   7:    */ public abstract class JndiObjectLocator
/*   8:    */   extends JndiLocatorSupport
/*   9:    */   implements InitializingBean
/*  10:    */ {
/*  11:    */   private String jndiName;
/*  12:    */   private Class<?> expectedType;
/*  13:    */   
/*  14:    */   public void setJndiName(String jndiName)
/*  15:    */   {
/*  16: 62 */     this.jndiName = jndiName;
/*  17:    */   }
/*  18:    */   
/*  19:    */   public String getJndiName()
/*  20:    */   {
/*  21: 69 */     return this.jndiName;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setExpectedType(Class<?> expectedType)
/*  25:    */   {
/*  26: 77 */     this.expectedType = expectedType;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public Class<?> getExpectedType()
/*  30:    */   {
/*  31: 85 */     return this.expectedType;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void afterPropertiesSet()
/*  35:    */     throws IllegalArgumentException, NamingException
/*  36:    */   {
/*  37: 89 */     if (!StringUtils.hasLength(getJndiName())) {
/*  38: 90 */       throw new IllegalArgumentException("Property 'jndiName' is required");
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected Object lookup()
/*  43:    */     throws NamingException
/*  44:    */   {
/*  45:105 */     return lookup(getJndiName(), getExpectedType());
/*  46:    */   }
/*  47:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jndi.JndiObjectLocator
 * JD-Core Version:    0.7.0.1
 */