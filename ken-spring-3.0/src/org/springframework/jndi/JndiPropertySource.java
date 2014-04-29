/*   1:    */ package org.springframework.jndi;
/*   2:    */ 
/*   3:    */ import javax.naming.NamingException;
/*   4:    */ import org.apache.commons.logging.Log;
/*   5:    */ import org.springframework.core.env.PropertySource;
/*   6:    */ 
/*   7:    */ public class JndiPropertySource
/*   8:    */   extends PropertySource<JndiLocatorDelegate>
/*   9:    */ {
/*  10:    */   public JndiPropertySource(String name)
/*  11:    */   {
/*  12: 61 */     this(name, createDefaultJndiLocator());
/*  13:    */   }
/*  14:    */   
/*  15:    */   public JndiPropertySource(String name, JndiLocatorDelegate jndiLocator)
/*  16:    */   {
/*  17: 69 */     super(name, jndiLocator);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public Object getProperty(String name)
/*  21:    */   {
/*  22:    */     try
/*  23:    */     {
/*  24: 82 */       Object value = ((JndiLocatorDelegate)this.source).lookup(name);
/*  25: 83 */       this.logger.debug("JNDI lookup for name [" + name + "] returned: [" + value + "]");
/*  26: 84 */       return value;
/*  27:    */     }
/*  28:    */     catch (NamingException ex)
/*  29:    */     {
/*  30: 86 */       this.logger.debug("JNDI lookup for name [" + name + "] threw NamingException " + 
/*  31: 87 */         "with message: " + ex.getMessage() + ". Returning null.");
/*  32:    */     }
/*  33: 88 */     return null;
/*  34:    */   }
/*  35:    */   
/*  36:    */   private static JndiLocatorDelegate createDefaultJndiLocator()
/*  37:    */   {
/*  38: 98 */     JndiLocatorDelegate jndiLocator = new JndiLocatorDelegate();
/*  39: 99 */     jndiLocator.setResourceRef(true);
/*  40:100 */     return jndiLocator;
/*  41:    */   }
/*  42:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jndi.JndiPropertySource
 * JD-Core Version:    0.7.0.1
 */