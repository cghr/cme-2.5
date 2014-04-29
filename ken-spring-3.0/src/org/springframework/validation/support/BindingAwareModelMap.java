/*  1:   */ package org.springframework.validation.support;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import java.util.Map.Entry;
/*  5:   */ import org.springframework.ui.ExtendedModelMap;
/*  6:   */ import org.springframework.validation.BindingResult;
/*  7:   */ 
/*  8:   */ public class BindingAwareModelMap
/*  9:   */   extends ExtendedModelMap
/* 10:   */ {
/* 11:   */   public Object put(String key, Object value)
/* 12:   */   {
/* 13:40 */     removeBindingResultIfNecessary(key, value);
/* 14:41 */     return super.put(key, value);
/* 15:   */   }
/* 16:   */   
/* 17:   */   public void putAll(Map<? extends String, ?> map)
/* 18:   */   {
/* 19:46 */     for (Map.Entry entry : map.entrySet()) {
/* 20:47 */       removeBindingResultIfNecessary(entry.getKey(), entry.getValue());
/* 21:   */     }
/* 22:49 */     super.putAll(map);
/* 23:   */   }
/* 24:   */   
/* 25:   */   private void removeBindingResultIfNecessary(Object key, Object value)
/* 26:   */   {
/* 27:53 */     if ((key instanceof String))
/* 28:   */     {
/* 29:54 */       String attributeName = (String)key;
/* 30:55 */       if (!attributeName.startsWith(BindingResult.MODEL_KEY_PREFIX))
/* 31:   */       {
/* 32:56 */         String bindingResultKey = BindingResult.MODEL_KEY_PREFIX + attributeName;
/* 33:57 */         BindingResult bindingResult = (BindingResult)get(bindingResultKey);
/* 34:58 */         if ((bindingResult != null) && (bindingResult.getTarget() != value)) {
/* 35:59 */           remove(bindingResultKey);
/* 36:   */         }
/* 37:   */       }
/* 38:   */     }
/* 39:   */   }
/* 40:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.support.BindingAwareModelMap
 * JD-Core Version:    0.7.0.1
 */