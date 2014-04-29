/*   1:    */ package org.springframework.web.servlet.view;
/*   2:    */ 
/*   3:    */ import org.springframework.util.ClassUtils;
/*   4:    */ 
/*   5:    */ public class InternalResourceViewResolver
/*   6:    */   extends UrlBasedViewResolver
/*   7:    */ {
/*   8: 50 */   private static final boolean jstlPresent = ClassUtils.isPresent(
/*   9: 51 */     "javax.servlet.jsp.jstl.core.Config", InternalResourceViewResolver.class.getClassLoader());
/*  10:    */   private Boolean alwaysInclude;
/*  11:    */   private Boolean exposeContextBeansAsAttributes;
/*  12:    */   private String[] exposedContextBeanNames;
/*  13:    */   
/*  14:    */   public InternalResourceViewResolver()
/*  15:    */   {
/*  16: 66 */     Class viewClass = requiredViewClass();
/*  17: 67 */     if ((viewClass.equals(InternalResourceView.class)) && (jstlPresent)) {
/*  18: 68 */       viewClass = JstlView.class;
/*  19:    */     }
/*  20: 70 */     setViewClass(viewClass);
/*  21:    */   }
/*  22:    */   
/*  23:    */   protected Class requiredViewClass()
/*  24:    */   {
/*  25: 78 */     return InternalResourceView.class;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setAlwaysInclude(boolean alwaysInclude)
/*  29:    */   {
/*  30: 89 */     this.alwaysInclude = Boolean.valueOf(alwaysInclude);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setExposeContextBeansAsAttributes(boolean exposeContextBeansAsAttributes)
/*  34:    */   {
/*  35:102 */     this.exposeContextBeansAsAttributes = Boolean.valueOf(exposeContextBeansAsAttributes);
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setExposedContextBeanNames(String[] exposedContextBeanNames)
/*  39:    */   {
/*  40:112 */     this.exposedContextBeanNames = exposedContextBeanNames;
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected AbstractUrlBasedView buildView(String viewName)
/*  44:    */     throws Exception
/*  45:    */   {
/*  46:118 */     InternalResourceView view = (InternalResourceView)super.buildView(viewName);
/*  47:119 */     if (this.alwaysInclude != null) {
/*  48:120 */       view.setAlwaysInclude(this.alwaysInclude.booleanValue());
/*  49:    */     }
/*  50:122 */     if (this.exposeContextBeansAsAttributes != null) {
/*  51:123 */       view.setExposeContextBeansAsAttributes(this.exposeContextBeansAsAttributes.booleanValue());
/*  52:    */     }
/*  53:125 */     if (this.exposedContextBeanNames != null) {
/*  54:126 */       view.setExposedContextBeanNames(this.exposedContextBeanNames);
/*  55:    */     }
/*  56:128 */     view.setPreventDispatchLoop(true);
/*  57:129 */     return view;
/*  58:    */   }
/*  59:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.InternalResourceViewResolver
 * JD-Core Version:    0.7.0.1
 */