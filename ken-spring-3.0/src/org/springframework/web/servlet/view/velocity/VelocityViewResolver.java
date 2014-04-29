/*   1:    */ package org.springframework.web.servlet.view.velocity;
/*   2:    */ 
/*   3:    */ import org.apache.commons.logging.Log;
/*   4:    */ import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
/*   5:    */ import org.springframework.web.servlet.view.AbstractUrlBasedView;
/*   6:    */ 
/*   7:    */ public class VelocityViewResolver
/*   8:    */   extends AbstractTemplateViewResolver
/*   9:    */ {
/*  10:    */   private String dateToolAttribute;
/*  11:    */   private String numberToolAttribute;
/*  12:    */   private String toolboxConfigLocation;
/*  13:    */   
/*  14:    */   public VelocityViewResolver()
/*  15:    */   {
/*  16: 54 */     setViewClass(requiredViewClass());
/*  17:    */   }
/*  18:    */   
/*  19:    */   protected Class requiredViewClass()
/*  20:    */   {
/*  21: 62 */     return VelocityView.class;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public void setDateToolAttribute(String dateToolAttribute)
/*  25:    */   {
/*  26: 73 */     this.dateToolAttribute = dateToolAttribute;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setNumberToolAttribute(String numberToolAttribute)
/*  30:    */   {
/*  31: 83 */     this.numberToolAttribute = numberToolAttribute;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setToolboxConfigLocation(String toolboxConfigLocation)
/*  35:    */   {
/*  36:100 */     this.toolboxConfigLocation = toolboxConfigLocation;
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected void initApplicationContext()
/*  40:    */   {
/*  41:106 */     super.initApplicationContext();
/*  42:108 */     if (this.toolboxConfigLocation != null) {
/*  43:109 */       if (VelocityView.class.equals(getViewClass()))
/*  44:    */       {
/*  45:110 */         this.logger.info("Using VelocityToolboxView instead of default VelocityView due to specified toolboxConfigLocation");
/*  46:    */         
/*  47:112 */         setViewClass(VelocityToolboxView.class);
/*  48:    */       }
/*  49:114 */       else if (!VelocityToolboxView.class.isAssignableFrom(getViewClass()))
/*  50:    */       {
/*  51:115 */         throw new IllegalArgumentException(
/*  52:116 */           "Given view class [" + getViewClass().getName() + 
/*  53:117 */           "] is not of type [" + VelocityToolboxView.class.getName() + 
/*  54:118 */           "], which it needs to be in case of a specified toolboxConfigLocation");
/*  55:    */       }
/*  56:    */     }
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected AbstractUrlBasedView buildView(String viewName)
/*  60:    */     throws Exception
/*  61:    */   {
/*  62:126 */     VelocityView view = (VelocityView)super.buildView(viewName);
/*  63:127 */     view.setDateToolAttribute(this.dateToolAttribute);
/*  64:128 */     view.setNumberToolAttribute(this.numberToolAttribute);
/*  65:129 */     if (this.toolboxConfigLocation != null) {
/*  66:130 */       ((VelocityToolboxView)view).setToolboxConfigLocation(this.toolboxConfigLocation);
/*  67:    */     }
/*  68:132 */     return view;
/*  69:    */   }
/*  70:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.velocity.VelocityViewResolver
 * JD-Core Version:    0.7.0.1
 */