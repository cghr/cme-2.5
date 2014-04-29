/*  1:   */ package org.springframework.expression.spel.standard;
/*  2:   */ 
/*  3:   */ import org.springframework.expression.ParseException;
/*  4:   */ import org.springframework.expression.ParserContext;
/*  5:   */ import org.springframework.expression.common.TemplateAwareExpressionParser;
/*  6:   */ import org.springframework.expression.spel.SpelParserConfiguration;
/*  7:   */ import org.springframework.util.Assert;
/*  8:   */ 
/*  9:   */ public class SpelExpressionParser
/* 10:   */   extends TemplateAwareExpressionParser
/* 11:   */ {
/* 12:   */   private final SpelParserConfiguration configuration;
/* 13:   */   
/* 14:   */   public SpelExpressionParser()
/* 15:   */   {
/* 16:41 */     this.configuration = new SpelParserConfiguration(false, false);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public SpelExpressionParser(SpelParserConfiguration configuration)
/* 20:   */   {
/* 21:49 */     Assert.notNull(configuration, "SpelParserConfiguration must not be null");
/* 22:50 */     this.configuration = configuration;
/* 23:   */   }
/* 24:   */   
/* 25:   */   protected SpelExpression doParseExpression(String expressionString, ParserContext context)
/* 26:   */     throws ParseException
/* 27:   */   {
/* 28:56 */     return new InternalSpelExpressionParser(this.configuration).doParseExpression(expressionString, context);
/* 29:   */   }
/* 30:   */   
/* 31:   */   public SpelExpression parseRaw(String expressionString)
/* 32:   */     throws ParseException
/* 33:   */   {
/* 34:60 */     return doParseExpression(expressionString, null);
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.standard.SpelExpressionParser
 * JD-Core Version:    0.7.0.1
 */