/*  1:   */ package org.springframework.beans.factory.parsing;
/*  2:   */ 
/*  3:   */ import org.springframework.core.io.Resource;
/*  4:   */ 
/*  5:   */ public class NullSourceExtractor
/*  6:   */   implements SourceExtractor
/*  7:   */ {
/*  8:   */   public Object extractSource(Object sourceCandidate, Resource definitionResource)
/*  9:   */   {
/* 10:37 */     return null;
/* 11:   */   }
/* 12:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.NullSourceExtractor
 * JD-Core Version:    0.7.0.1
 */