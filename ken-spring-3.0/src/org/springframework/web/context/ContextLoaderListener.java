/*   1:    */ package org.springframework.web.context;
/*   2:    */ 
/*   3:    */ import javax.servlet.ServletContextEvent;
/*   4:    */ import javax.servlet.ServletContextListener;
/*   5:    */ 
/*   6:    */ public class ContextLoaderListener
/*   7:    */   extends ContextLoader
/*   8:    */   implements ServletContextListener
/*   9:    */ {
/*  10:    */   private ContextLoader contextLoader;
/*  11:    */   
/*  12:    */   public ContextLoaderListener() {}
/*  13:    */   
/*  14:    */   public ContextLoaderListener(WebApplicationContext context)
/*  15:    */   {
/*  16:100 */     super(context);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public void contextInitialized(ServletContextEvent event)
/*  20:    */   {
/*  21:107 */     this.contextLoader = createContextLoader();
/*  22:108 */     if (this.contextLoader == null) {
/*  23:109 */       this.contextLoader = this;
/*  24:    */     }
/*  25:111 */     this.contextLoader.initWebApplicationContext(event.getServletContext());
/*  26:    */   }
/*  27:    */   
/*  28:    */   @Deprecated
/*  29:    */   protected ContextLoader createContextLoader()
/*  30:    */   {
/*  31:122 */     return null;
/*  32:    */   }
/*  33:    */   
/*  34:    */   @Deprecated
/*  35:    */   public ContextLoader getContextLoader()
/*  36:    */   {
/*  37:133 */     return this.contextLoader;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void contextDestroyed(ServletContextEvent event)
/*  41:    */   {
/*  42:141 */     if (this.contextLoader != null) {
/*  43:142 */       this.contextLoader.closeWebApplicationContext(event.getServletContext());
/*  44:    */     }
/*  45:144 */     ContextCleanupListener.cleanupAttributes(event.getServletContext());
/*  46:    */   }
/*  47:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.ContextLoaderListener
 * JD-Core Version:    0.7.0.1
 */