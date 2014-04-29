/*  1:   */ package org.springframework.web.servlet.tags.form;
/*  2:   */ 
/*  3:   */ import javax.servlet.jsp.JspException;
/*  4:   */ 
/*  5:   */ public class PasswordInputTag
/*  6:   */   extends InputTag
/*  7:   */ {
/*  8:31 */   private boolean showPassword = false;
/*  9:   */   
/* 10:   */   public boolean isShowPassword()
/* 11:   */   {
/* 12:39 */     return this.showPassword;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public void setShowPassword(boolean showPassword)
/* 16:   */   {
/* 17:47 */     this.showPassword = showPassword;
/* 18:   */   }
/* 19:   */   
/* 20:   */   protected String getType()
/* 21:   */   {
/* 22:56 */     return "password";
/* 23:   */   }
/* 24:   */   
/* 25:   */   protected void writeValue(TagWriter tagWriter)
/* 26:   */     throws JspException
/* 27:   */   {
/* 28:66 */     if (this.showPassword) {
/* 29:67 */       super.writeValue(tagWriter);
/* 30:   */     } else {
/* 31:69 */       tagWriter.writeAttribute("value", processFieldValue(getName(), "", getType()));
/* 32:   */     }
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.form.PasswordInputTag
 * JD-Core Version:    0.7.0.1
 */