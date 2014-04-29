/*   1:    */ package org.springframework.web.jsf;
/*   2:    */ 
/*   3:    */ import javax.faces.application.NavigationHandler;
/*   4:    */ import javax.faces.context.FacesContext;
/*   5:    */ 
/*   6:    */ public abstract class DecoratingNavigationHandler
/*   7:    */   extends NavigationHandler
/*   8:    */ {
/*   9:    */   private NavigationHandler decoratedNavigationHandler;
/*  10:    */   
/*  11:    */   protected DecoratingNavigationHandler() {}
/*  12:    */   
/*  13:    */   protected DecoratingNavigationHandler(NavigationHandler originalNavigationHandler)
/*  14:    */   {
/*  15: 53 */     this.decoratedNavigationHandler = originalNavigationHandler;
/*  16:    */   }
/*  17:    */   
/*  18:    */   public final NavigationHandler getDecoratedNavigationHandler()
/*  19:    */   {
/*  20: 61 */     return this.decoratedNavigationHandler;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public final void handleNavigation(FacesContext facesContext, String fromAction, String outcome)
/*  24:    */   {
/*  25: 73 */     handleNavigation(facesContext, fromAction, outcome, this.decoratedNavigationHandler);
/*  26:    */   }
/*  27:    */   
/*  28:    */   public abstract void handleNavigation(FacesContext paramFacesContext, String paramString1, String paramString2, NavigationHandler paramNavigationHandler);
/*  29:    */   
/*  30:    */   protected final void callNextHandlerInChain(FacesContext facesContext, String fromAction, String outcome, NavigationHandler originalNavigationHandler)
/*  31:    */   {
/*  32:131 */     NavigationHandler decoratedNavigationHandler = getDecoratedNavigationHandler();
/*  33:133 */     if ((decoratedNavigationHandler instanceof DecoratingNavigationHandler))
/*  34:    */     {
/*  35:136 */       DecoratingNavigationHandler decHandler = (DecoratingNavigationHandler)decoratedNavigationHandler;
/*  36:137 */       decHandler.handleNavigation(facesContext, fromAction, outcome, originalNavigationHandler);
/*  37:    */     }
/*  38:139 */     else if (decoratedNavigationHandler != null)
/*  39:    */     {
/*  40:143 */       decoratedNavigationHandler.handleNavigation(facesContext, fromAction, outcome);
/*  41:    */     }
/*  42:145 */     else if (originalNavigationHandler != null)
/*  43:    */     {
/*  44:148 */       originalNavigationHandler.handleNavigation(facesContext, fromAction, outcome);
/*  45:    */     }
/*  46:    */   }
/*  47:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.jsf.DecoratingNavigationHandler
 * JD-Core Version:    0.7.0.1
 */