/*  1:   */ package org.springframework.web.servlet.view;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import org.springframework.beans.factory.InitializingBean;
/*  5:   */ 
/*  6:   */ public abstract class AbstractUrlBasedView
/*  7:   */   extends AbstractView
/*  8:   */   implements InitializingBean
/*  9:   */ {
/* 10:   */   private String url;
/* 11:   */   
/* 12:   */   protected AbstractUrlBasedView() {}
/* 13:   */   
/* 14:   */   protected AbstractUrlBasedView(String url)
/* 15:   */   {
/* 16:46 */     this.url = url;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void setUrl(String url)
/* 20:   */   {
/* 21:55 */     this.url = url;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public String getUrl()
/* 25:   */   {
/* 26:62 */     return this.url;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public void afterPropertiesSet()
/* 30:   */     throws Exception
/* 31:   */   {
/* 32:66 */     if ((isUrlRequired()) && (getUrl() == null)) {
/* 33:67 */       throw new IllegalArgumentException("Property 'url' is required");
/* 34:   */     }
/* 35:   */   }
/* 36:   */   
/* 37:   */   protected boolean isUrlRequired()
/* 38:   */   {
/* 39:77 */     return true;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public boolean checkResource(Locale locale)
/* 43:   */     throws Exception
/* 44:   */   {
/* 45:89 */     return true;
/* 46:   */   }
/* 47:   */   
/* 48:   */   public String toString()
/* 49:   */   {
/* 50:94 */     StringBuilder sb = new StringBuilder(super.toString());
/* 51:95 */     sb.append("; URL [").append(getUrl()).append("]");
/* 52:96 */     return sb.toString();
/* 53:   */   }
/* 54:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.AbstractUrlBasedView
 * JD-Core Version:    0.7.0.1
 */