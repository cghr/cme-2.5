/*  1:   */ package org.springframework.core.type.filter;
/*  2:   */ 
/*  3:   */ import java.util.regex.Matcher;
/*  4:   */ import java.util.regex.Pattern;
/*  5:   */ import org.springframework.core.type.ClassMetadata;
/*  6:   */ import org.springframework.util.Assert;
/*  7:   */ 
/*  8:   */ public class RegexPatternTypeFilter
/*  9:   */   extends AbstractClassTestingTypeFilter
/* 10:   */ {
/* 11:   */   private final Pattern pattern;
/* 12:   */   
/* 13:   */   public RegexPatternTypeFilter(Pattern pattern)
/* 14:   */   {
/* 15:37 */     Assert.notNull(pattern, "Pattern must not be null");
/* 16:38 */     this.pattern = pattern;
/* 17:   */   }
/* 18:   */   
/* 19:   */   protected boolean match(ClassMetadata metadata)
/* 20:   */   {
/* 21:44 */     return this.pattern.matcher(metadata.getClassName()).matches();
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.filter.RegexPatternTypeFilter
 * JD-Core Version:    0.7.0.1
 */