/*  1:   */ package org.springframework.validation;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import java.util.Map;
/*  5:   */ import org.springframework.util.Assert;
/*  6:   */ 
/*  7:   */ public class MapBindingResult
/*  8:   */   extends AbstractBindingResult
/*  9:   */   implements Serializable
/* 10:   */ {
/* 11:   */   private final Map<?, ?> target;
/* 12:   */   
/* 13:   */   public MapBindingResult(Map<?, ?> target, String objectName)
/* 14:   */   {
/* 15:48 */     super(objectName);
/* 16:49 */     Assert.notNull(target, "Target Map must not be null");
/* 17:50 */     this.target = target;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public final Map<?, ?> getTargetMap()
/* 21:   */   {
/* 22:55 */     return this.target;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public final Object getTarget()
/* 26:   */   {
/* 27:60 */     return this.target;
/* 28:   */   }
/* 29:   */   
/* 30:   */   protected Object getActualFieldValue(String field)
/* 31:   */   {
/* 32:65 */     return this.target.get(field);
/* 33:   */   }
/* 34:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.MapBindingResult
 * JD-Core Version:    0.7.0.1
 */