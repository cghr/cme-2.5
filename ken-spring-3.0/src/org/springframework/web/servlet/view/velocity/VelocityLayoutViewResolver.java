/*   1:    */ package org.springframework.web.servlet.view.velocity;
/*   2:    */ 
/*   3:    */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
/*   4:    */ 
/*   5:    */ public class VelocityLayoutViewResolver
/*   6:    */   extends VelocityViewResolver
/*   7:    */ {
/*   8:    */   private String layoutUrl;
/*   9:    */   private String layoutKey;
/*  10:    */   private String screenContentKey;
/*  11:    */   
/*  12:    */   protected Class requiredViewClass()
/*  13:    */   {
/*  14: 50 */     return VelocityLayoutView.class;
/*  15:    */   }
/*  16:    */   
/*  17:    */   public void setLayoutUrl(String layoutUrl)
/*  18:    */   {
/*  19: 60 */     this.layoutUrl = layoutUrl;
/*  20:    */   }
/*  21:    */   
/*  22:    */   public void setLayoutKey(String layoutKey)
/*  23:    */   {
/*  24: 75 */     this.layoutKey = layoutKey;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void setScreenContentKey(String screenContentKey)
/*  28:    */   {
/*  29: 88 */     this.screenContentKey = screenContentKey;
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected AbstractUrlBasedView buildView(String viewName)
/*  33:    */     throws Exception
/*  34:    */   {
/*  35: 94 */     VelocityLayoutView view = (VelocityLayoutView)super.buildView(viewName);
/*  36: 96 */     if (this.layoutUrl != null) {
/*  37: 97 */       view.setLayoutUrl(this.layoutUrl);
/*  38:    */     }
/*  39: 99 */     if (this.layoutKey != null) {
/*  40:100 */       view.setLayoutKey(this.layoutKey);
/*  41:    */     }
/*  42:102 */     if (this.screenContentKey != null) {
/*  43:103 */       view.setScreenContentKey(this.screenContentKey);
/*  44:    */     }
/*  45:105 */     return view;
/*  46:    */   }
/*  47:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver
 * JD-Core Version:    0.7.0.1
 */