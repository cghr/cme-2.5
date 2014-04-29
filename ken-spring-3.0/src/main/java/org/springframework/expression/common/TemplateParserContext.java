/*  1:   */ package org.springframework.expression.common;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.ParserContext;
/*  4:   */ 
/*  5:   */ public class TemplateParserContext
/*  6:   */   implements ParserContext
/*  7:   */ {
/*  8:   */   private final String expressionPrefix;
/*  9:   */   private final String expressionSuffix;
/* 10:   */   
/* 11:   */   public TemplateParserContext()
/* 12:   */   {
/* 13:39 */     this("#{", "}");
/* 14:   */   }
/* 15:   */   
/* 16:   */   public TemplateParserContext(String expressionPrefix, String expressionSuffix)
/* 17:   */   {
/* 18:48 */     this.expressionPrefix = expressionPrefix;
/* 19:49 */     this.expressionSuffix = expressionSuffix;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public final boolean isTemplate()
/* 23:   */   {
/* 24:54 */     return true;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public final String getExpressionPrefix()
/* 28:   */   {
/* 29:58 */     return this.expressionPrefix;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public final String getExpressionSuffix()
/* 33:   */   {
/* 34:62 */     return this.expressionSuffix;
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.common.TemplateParserContext
 * JD-Core Version:    0.7.0.1
 */