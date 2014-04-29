/*   1:    */ package org.springframework.context.access;
/*   2:    */ 
/*   3:    */ import javax.naming.NamingException;
/*   4:    */ import org.apache.commons.logging.Log;
/*   5:    */ import org.springframework.beans.BeansException;
/*   6:    */ import org.springframework.beans.factory.access.BeanFactoryLocator;
/*   7:    */ import org.springframework.beans.factory.access.BeanFactoryReference;
/*   8:    */ import org.springframework.beans.factory.access.BootstrapException;
/*   9:    */ import org.springframework.context.ApplicationContext;
/*  10:    */ import org.springframework.context.support.ClassPathXmlApplicationContext;
/*  11:    */ import org.springframework.jndi.JndiLocatorSupport;
/*  12:    */ import org.springframework.util.StringUtils;
/*  13:    */ 
/*  14:    */ public class ContextJndiBeanFactoryLocator
/*  15:    */   extends JndiLocatorSupport
/*  16:    */   implements BeanFactoryLocator
/*  17:    */ {
/*  18:    */   public static final String BEAN_FACTORY_PATH_DELIMITERS = ",; \t\n";
/*  19:    */   
/*  20:    */   public BeanFactoryReference useBeanFactory(String factoryKey)
/*  21:    */     throws BeansException
/*  22:    */   {
/*  23:    */     try
/*  24:    */     {
/*  25: 62 */       String beanFactoryPath = (String)lookup(factoryKey, String.class);
/*  26: 63 */       if (this.logger.isTraceEnabled()) {
/*  27: 64 */         this.logger.trace("Bean factory path from JNDI environment variable [" + factoryKey + 
/*  28: 65 */           "] is: " + beanFactoryPath);
/*  29:    */       }
/*  30: 67 */       String[] paths = StringUtils.tokenizeToStringArray(beanFactoryPath, ",; \t\n");
/*  31: 68 */       return createBeanFactory(paths);
/*  32:    */     }
/*  33:    */     catch (NamingException ex)
/*  34:    */     {
/*  35: 71 */       throw new BootstrapException("Define an environment variable [" + factoryKey + "] containing " + 
/*  36: 72 */         "the class path locations of XML bean definition files", ex);
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   protected BeanFactoryReference createBeanFactory(String[] resources)
/*  41:    */     throws BeansException
/*  42:    */   {
/*  43: 90 */     ApplicationContext ctx = createApplicationContext(resources);
/*  44: 91 */     return new ContextBeanFactoryReference(ctx);
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected ApplicationContext createApplicationContext(String[] resources)
/*  48:    */     throws BeansException
/*  49:    */   {
/*  50:102 */     return new ClassPathXmlApplicationContext(resources);
/*  51:    */   }
/*  52:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.access.ContextJndiBeanFactoryLocator
 * JD-Core Version:    0.7.0.1
 */