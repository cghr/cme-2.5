/*  1:   */ package org.springframework.expression;
/*  2:   */ 
/*  3:   */ public abstract interface ParserContext
/*  4:   */ {
/*  5:63 */   public static final ParserContext TEMPLATE_EXPRESSION = new ParserContext()
/*  6:   */   {
/*  7:   */     public String getExpressionPrefix()
/*  8:   */     {
/*  9:66 */       return "#{";
/* 10:   */     }
/* 11:   */     
/* 12:   */     public String getExpressionSuffix()
/* 13:   */     {
/* 14:70 */       return "}";
/* 15:   */     }
/* 16:   */     
/* 17:   */     public boolean isTemplate()
/* 18:   */     {
/* 19:74 */       return true;
/* 20:   */     }
/* 21:   */   };
/* 22:   */   
/* 23:   */   public abstract boolean isTemplate();
/* 24:   */   
/* 25:   */   public abstract String getExpressionPrefix();
/* 26:   */   
/* 27:   */   public abstract String getExpressionSuffix();
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.ParserContext
 * JD-Core Version:    0.7.0.1
 */