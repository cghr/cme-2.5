/*  1:   */ package org.springframework.expression.spel;
/*  2:   */ 
/*  3:   */ public class SpelParserConfiguration
/*  4:   */ {
/*  5:   */   private final boolean autoGrowNullReferences;
/*  6:   */   private final boolean autoGrowCollections;
/*  7:   */   
/*  8:   */   public SpelParserConfiguration(boolean autoGrowNullReferences, boolean autoGrowCollections)
/*  9:   */   {
/* 10:34 */     this.autoGrowNullReferences = autoGrowNullReferences;
/* 11:35 */     this.autoGrowCollections = autoGrowCollections;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public boolean isAutoGrowNullReferences()
/* 15:   */   {
/* 16:40 */     return this.autoGrowNullReferences;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public boolean isAutoGrowCollections()
/* 20:   */   {
/* 21:44 */     return this.autoGrowCollections;
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.SpelParserConfiguration
 * JD-Core Version:    0.7.0.1
 */