/*  1:   */ package org.springframework.core.enums;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Field;
/*  4:   */ import java.lang.reflect.Modifier;
/*  5:   */ import java.util.Set;
/*  6:   */ import java.util.TreeSet;
/*  7:   */ import org.apache.commons.logging.Log;
/*  8:   */ import org.springframework.util.Assert;
/*  9:   */ 
/* 10:   */ @Deprecated
/* 11:   */ public class StaticLabeledEnumResolver
/* 12:   */   extends AbstractCachingLabeledEnumResolver
/* 13:   */ {
/* 14:42 */   private static final StaticLabeledEnumResolver INSTANCE = new StaticLabeledEnumResolver();
/* 15:   */   
/* 16:   */   public static StaticLabeledEnumResolver instance()
/* 17:   */   {
/* 18:51 */     return INSTANCE;
/* 19:   */   }
/* 20:   */   
/* 21:   */   protected Set<LabeledEnum> findLabeledEnums(Class type)
/* 22:   */   {
/* 23:57 */     Set<LabeledEnum> typeEnums = new TreeSet();
/* 24:58 */     for (Field field : type.getFields()) {
/* 25:59 */       if ((Modifier.isStatic(field.getModifiers())) && (Modifier.isPublic(field.getModifiers())) && 
/* 26:60 */         (type.isAssignableFrom(field.getType()))) {
/* 27:   */         try
/* 28:   */         {
/* 29:62 */           Object value = field.get(null);
/* 30:63 */           Assert.isTrue(value instanceof LabeledEnum, "Field value must be a LabeledEnum instance");
/* 31:64 */           typeEnums.add((LabeledEnum)value);
/* 32:   */         }
/* 33:   */         catch (IllegalAccessException ex)
/* 34:   */         {
/* 35:67 */           this.logger.warn("Unable to access field value: " + field, ex);
/* 36:   */         }
/* 37:   */       }
/* 38:   */     }
/* 39:72 */     return typeEnums;
/* 40:   */   }
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.enums.StaticLabeledEnumResolver
 * JD-Core Version:    0.7.0.1
 */