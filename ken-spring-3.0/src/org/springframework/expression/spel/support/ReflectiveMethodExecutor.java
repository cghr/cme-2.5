/*  1:   */ package org.springframework.expression.spel.support;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Method;
/*  4:   */ import org.springframework.core.MethodParameter;
/*  5:   */ import org.springframework.core.convert.TypeDescriptor;
/*  6:   */ import org.springframework.expression.AccessException;
/*  7:   */ import org.springframework.expression.EvaluationContext;
/*  8:   */ import org.springframework.expression.MethodExecutor;
/*  9:   */ import org.springframework.expression.TypedValue;
/* 10:   */ import org.springframework.util.ReflectionUtils;
/* 11:   */ 
/* 12:   */ class ReflectiveMethodExecutor
/* 13:   */   implements MethodExecutor
/* 14:   */ {
/* 15:   */   private final Method method;
/* 16:   */   private final Integer varargsPosition;
/* 17:   */   private final int[] argsRequiringConversion;
/* 18:   */   
/* 19:   */   public ReflectiveMethodExecutor(Method theMethod, int[] argumentsRequiringConversion)
/* 20:   */   {
/* 21:46 */     this.method = theMethod;
/* 22:47 */     if (theMethod.isVarArgs())
/* 23:   */     {
/* 24:48 */       Class[] paramTypes = theMethod.getParameterTypes();
/* 25:49 */       this.varargsPosition = Integer.valueOf(paramTypes.length - 1);
/* 26:   */     }
/* 27:   */     else
/* 28:   */     {
/* 29:52 */       this.varargsPosition = null;
/* 30:   */     }
/* 31:54 */     this.argsRequiringConversion = argumentsRequiringConversion;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public TypedValue execute(EvaluationContext context, Object target, Object... arguments)
/* 35:   */     throws AccessException
/* 36:   */   {
/* 37:   */     try
/* 38:   */     {
/* 39:60 */       if (arguments != null) {
/* 40:61 */         ReflectionHelper.convertArguments(
/* 41:62 */           context.getTypeConverter(), arguments, this.method, 
/* 42:63 */           this.argsRequiringConversion, this.varargsPosition);
/* 43:   */       }
/* 44:65 */       if (this.method.isVarArgs()) {
/* 45:66 */         arguments = ReflectionHelper.setupArgumentsForVarargsInvocation(this.method.getParameterTypes(), arguments);
/* 46:   */       }
/* 47:68 */       ReflectionUtils.makeAccessible(this.method);
/* 48:69 */       Object value = this.method.invoke(target, arguments);
/* 49:70 */       return new TypedValue(value, new TypeDescriptor(new MethodParameter(this.method, -1)).narrow(value));
/* 50:   */     }
/* 51:   */     catch (Exception ex)
/* 52:   */     {
/* 53:73 */       throw new AccessException("Problem invoking method: " + this.method, ex);
/* 54:   */     }
/* 55:   */   }
/* 56:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.support.ReflectiveMethodExecutor
 * JD-Core Version:    0.7.0.1
 */