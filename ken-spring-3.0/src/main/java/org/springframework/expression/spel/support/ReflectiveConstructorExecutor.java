/*  1:   */ package org.springframework.expression.spel.support;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Constructor;
/*  4:   */ import org.springframework.expression.AccessException;
/*  5:   */ import org.springframework.expression.ConstructorExecutor;
/*  6:   */ import org.springframework.expression.EvaluationContext;
/*  7:   */ import org.springframework.expression.TypedValue;
/*  8:   */ import org.springframework.util.ReflectionUtils;
/*  9:   */ 
/* 10:   */ class ReflectiveConstructorExecutor
/* 11:   */   implements ConstructorExecutor
/* 12:   */ {
/* 13:   */   private final Constructor<?> ctor;
/* 14:   */   private final Integer varargsPosition;
/* 15:   */   private final int[] argsRequiringConversion;
/* 16:   */   
/* 17:   */   public ReflectiveConstructorExecutor(Constructor<?> ctor, int[] argsRequiringConversion)
/* 18:   */   {
/* 19:46 */     this.ctor = ctor;
/* 20:47 */     if (ctor.isVarArgs())
/* 21:   */     {
/* 22:48 */       Class[] paramTypes = ctor.getParameterTypes();
/* 23:49 */       this.varargsPosition = Integer.valueOf(paramTypes.length - 1);
/* 24:   */     }
/* 25:   */     else
/* 26:   */     {
/* 27:52 */       this.varargsPosition = null;
/* 28:   */     }
/* 29:54 */     this.argsRequiringConversion = argsRequiringConversion;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public TypedValue execute(EvaluationContext context, Object... arguments)
/* 33:   */     throws AccessException
/* 34:   */   {
/* 35:   */     try
/* 36:   */     {
/* 37:59 */       if (arguments != null) {
/* 38:60 */         ReflectionHelper.convertArguments(context.getTypeConverter(), arguments, 
/* 39:61 */           this.ctor, this.argsRequiringConversion, this.varargsPosition);
/* 40:   */       }
/* 41:63 */       if (this.ctor.isVarArgs()) {
/* 42:64 */         arguments = ReflectionHelper.setupArgumentsForVarargsInvocation(this.ctor.getParameterTypes(), arguments);
/* 43:   */       }
/* 44:66 */       ReflectionUtils.makeAccessible(this.ctor);
/* 45:67 */       return new TypedValue(this.ctor.newInstance(arguments));
/* 46:   */     }
/* 47:   */     catch (Exception ex)
/* 48:   */     {
/* 49:70 */       throw new AccessException("Problem invoking constructor: " + this.ctor, ex);
/* 50:   */     }
/* 51:   */   }
/* 52:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.support.ReflectiveConstructorExecutor
 * JD-Core Version:    0.7.0.1
 */