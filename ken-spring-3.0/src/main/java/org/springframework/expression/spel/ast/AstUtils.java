/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import org.springframework.expression.PropertyAccessor;
/*  6:   */ import org.springframework.expression.spel.ExpressionState;
/*  7:   */ 
/*  8:   */ public class AstUtils
/*  9:   */ {
/* 10:   */   public static List<PropertyAccessor> getPropertyAccessorsToTry(Class<?> targetType, ExpressionState state)
/* 11:   */   {
/* 12:45 */     List<PropertyAccessor> specificAccessors = new ArrayList();
/* 13:46 */     List<PropertyAccessor> generalAccessors = new ArrayList();
/* 14:47 */     for (PropertyAccessor resolver : state.getPropertyAccessors())
/* 15:   */     {
/* 16:48 */       Class[] targets = resolver.getSpecificTargetClasses();
/* 17:49 */       if (targets == null)
/* 18:   */       {
/* 19:50 */         generalAccessors.add(resolver);
/* 20:   */       }
/* 21:53 */       else if (targetType != null)
/* 22:   */       {
/* 23:54 */         int pos = 0;
/* 24:55 */         for (Class<?> clazz : targets) {
/* 25:56 */           if (clazz == targetType) {
/* 26:57 */             specificAccessors.add(pos++, resolver);
/* 27:59 */           } else if (clazz.isAssignableFrom(targetType)) {
/* 28:61 */             generalAccessors.add(resolver);
/* 29:   */           }
/* 30:   */         }
/* 31:   */       }
/* 32:   */     }
/* 33:67 */     List<PropertyAccessor> resolvers = new ArrayList();
/* 34:68 */     resolvers.addAll(specificAccessors);
/* 35:69 */     resolvers.addAll(generalAccessors);
/* 36:70 */     return resolvers;
/* 37:   */   }
/* 38:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.AstUtils
 * JD-Core Version:    0.7.0.1
 */