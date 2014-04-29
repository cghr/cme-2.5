/*  1:   */ package org.springframework.cache.interceptor;
/*  2:   */ 
/*  3:   */ import java.beans.PropertyEditorSupport;
/*  4:   */ import org.springframework.util.StringUtils;
/*  5:   */ 
/*  6:   */ public class CacheOperationEditor
/*  7:   */   extends PropertyEditorSupport
/*  8:   */ {
/*  9:   */   public void setAsText(String text)
/* 10:   */     throws IllegalArgumentException
/* 11:   */   {
/* 12:49 */     if (StringUtils.hasLength(text))
/* 13:   */     {
/* 14:51 */       String[] tokens = StringUtils.commaDelimitedListToStringArray(text);
/* 15:52 */       if (tokens.length < 2) {
/* 16:53 */         throw new IllegalArgumentException(
/* 17:54 */           "too little arguments found, at least the cache action and cache name are required");
/* 18:   */       }
/* 19:   */       CacheOperation op;
/* 20:59 */       if ("cacheable".contains(tokens[0]))
/* 21:   */       {
/* 22:60 */         op = new CacheUpdateOperation();
/* 23:   */       }
/* 24:   */       else
/* 25:   */       {
/* 26:   */         CacheOperation op;
/* 27:63 */         if ("evict".contains(tokens[0])) {
/* 28:64 */           op = new CacheEvictOperation();
/* 29:   */         } else {
/* 30:66 */           throw new IllegalArgumentException("Invalid cache action specified " + tokens[0]);
/* 31:   */         }
/* 32:   */       }
/* 33:   */       CacheOperation op;
/* 34:69 */       op.setCacheNames(StringUtils.delimitedListToStringArray(tokens[1], ";"));
/* 35:71 */       if (tokens.length > 2) {
/* 36:72 */         op.setKey(tokens[2]);
/* 37:   */       }
/* 38:75 */       if (tokens.length > 3) {
/* 39:76 */         op.setCondition(tokens[3]);
/* 40:   */       }
/* 41:79 */       setValue(op);
/* 42:   */     }
/* 43:   */     else
/* 44:   */     {
/* 45:81 */       setValue(null);
/* 46:   */     }
/* 47:   */   }
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.interceptor.CacheOperationEditor
 * JD-Core Version:    0.7.0.1
 */