/*   1:    */ package org.springframework.web.servlet.view;
/*   2:    */ 
/*   3:    */ public class AbstractTemplateViewResolver
/*   4:    */   extends UrlBasedViewResolver
/*   5:    */ {
/*   6: 35 */   private boolean exposeRequestAttributes = false;
/*   7: 37 */   private boolean allowRequestOverride = false;
/*   8: 39 */   private boolean exposeSessionAttributes = false;
/*   9: 41 */   private boolean allowSessionOverride = false;
/*  10: 43 */   private boolean exposeSpringMacroHelpers = true;
/*  11:    */   
/*  12:    */   protected Class requiredViewClass()
/*  13:    */   {
/*  14: 48 */     return AbstractTemplateView.class;
/*  15:    */   }
/*  16:    */   
/*  17:    */   public void setExposeRequestAttributes(boolean exposeRequestAttributes)
/*  18:    */   {
/*  19: 57 */     this.exposeRequestAttributes = exposeRequestAttributes;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public void setAllowRequestOverride(boolean allowRequestOverride)
/*  23:    */   {
/*  24: 68 */     this.allowRequestOverride = allowRequestOverride;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void setExposeSessionAttributes(boolean exposeSessionAttributes)
/*  28:    */   {
/*  29: 77 */     this.exposeSessionAttributes = exposeSessionAttributes;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setAllowSessionOverride(boolean allowSessionOverride)
/*  33:    */   {
/*  34: 88 */     this.allowSessionOverride = allowSessionOverride;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setExposeSpringMacroHelpers(boolean exposeSpringMacroHelpers)
/*  38:    */   {
/*  39: 97 */     this.exposeSpringMacroHelpers = exposeSpringMacroHelpers;
/*  40:    */   }
/*  41:    */   
/*  42:    */   protected AbstractUrlBasedView buildView(String viewName)
/*  43:    */     throws Exception
/*  44:    */   {
/*  45:103 */     AbstractTemplateView view = (AbstractTemplateView)super.buildView(viewName);
/*  46:104 */     view.setExposeRequestAttributes(this.exposeRequestAttributes);
/*  47:105 */     view.setAllowRequestOverride(this.allowRequestOverride);
/*  48:106 */     view.setExposeSessionAttributes(this.exposeSessionAttributes);
/*  49:107 */     view.setAllowSessionOverride(this.allowSessionOverride);
/*  50:108 */     view.setExposeSpringMacroHelpers(this.exposeSpringMacroHelpers);
/*  51:109 */     return view;
/*  52:    */   }
/*  53:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.AbstractTemplateViewResolver
 * JD-Core Version:    0.7.0.1
 */