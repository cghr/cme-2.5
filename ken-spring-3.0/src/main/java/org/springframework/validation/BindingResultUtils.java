/*  1:   */ package org.springframework.validation;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ 
/*  6:   */ public abstract class BindingResultUtils
/*  7:   */ {
/*  8:   */   public static BindingResult getBindingResult(Map<?, ?> model, String name)
/*  9:   */   {
/* 10:40 */     Assert.notNull(model, "Model map must not be null");
/* 11:41 */     Assert.notNull(name, "Name must not be null");
/* 12:42 */     Object attr = model.get(BindingResult.MODEL_KEY_PREFIX + name);
/* 13:43 */     if ((attr != null) && (!(attr instanceof BindingResult))) {
/* 14:44 */       throw new IllegalStateException("BindingResult attribute is not of type BindingResult: " + attr);
/* 15:   */     }
/* 16:46 */     return (BindingResult)attr;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public static BindingResult getRequiredBindingResult(Map<?, ?> model, String name)
/* 20:   */   {
/* 21:57 */     BindingResult bindingResult = getBindingResult(model, name);
/* 22:58 */     if (bindingResult == null) {
/* 23:59 */       throw new IllegalStateException("No BindingResult attribute found for name '" + name + 
/* 24:60 */         "'- have you exposed the correct model?");
/* 25:   */     }
/* 26:62 */     return bindingResult;
/* 27:   */   }
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.BindingResultUtils
 * JD-Core Version:    0.7.0.1
 */