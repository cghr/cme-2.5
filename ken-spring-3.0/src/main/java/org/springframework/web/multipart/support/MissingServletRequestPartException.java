/*  1:   */ package org.springframework.web.multipart.support;
/*  2:   */ 
/*  3:   */ import javax.servlet.ServletException;
/*  4:   */ 
/*  5:   */ public class MissingServletRequestPartException
/*  6:   */   extends ServletException
/*  7:   */ {
/*  8:   */   private static final long serialVersionUID = -1255077391966870705L;
/*  9:   */   private final String partName;
/* 10:   */   
/* 11:   */   public MissingServletRequestPartException(String partName)
/* 12:   */   {
/* 13:43 */     super("Request part '" + partName + "' not found.");
/* 14:44 */     this.partName = partName;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public String getRequestPartName()
/* 18:   */   {
/* 19:48 */     return this.partName;
/* 20:   */   }
/* 21:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.multipart.support.MissingServletRequestPartException
 * JD-Core Version:    0.7.0.1
 */