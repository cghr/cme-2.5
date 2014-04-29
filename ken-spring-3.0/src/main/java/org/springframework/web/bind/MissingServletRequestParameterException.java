/*  1:   */ package org.springframework.web.bind;
/*  2:   */ 
/*  3:   */ public class MissingServletRequestParameterException
/*  4:   */   extends ServletRequestBindingException
/*  5:   */ {
/*  6:   */   private final String parameterName;
/*  7:   */   private final String parameterType;
/*  8:   */   
/*  9:   */   public MissingServletRequestParameterException(String parameterName, String parameterType)
/* 10:   */   {
/* 11:38 */     super("");
/* 12:39 */     this.parameterName = parameterName;
/* 13:40 */     this.parameterType = parameterType;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public String getMessage()
/* 17:   */   {
/* 18:46 */     return "Required " + this.parameterType + " parameter '" + this.parameterName + "' is not present";
/* 19:   */   }
/* 20:   */   
/* 21:   */   public final String getParameterName()
/* 22:   */   {
/* 23:53 */     return this.parameterName;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public final String getParameterType()
/* 27:   */   {
/* 28:60 */     return this.parameterType;
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.bind.MissingServletRequestParameterException
 * JD-Core Version:    0.7.0.1
 */