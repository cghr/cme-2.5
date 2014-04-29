/*   1:    */ package org.springframework.expression.spel.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.Constructor;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Arrays;
/*   6:    */ import java.util.Comparator;
/*   7:    */ import java.util.List;
/*   8:    */ import org.springframework.core.MethodParameter;
/*   9:    */ import org.springframework.core.convert.TypeDescriptor;
/*  10:    */ import org.springframework.expression.AccessException;
/*  11:    */ import org.springframework.expression.ConstructorExecutor;
/*  12:    */ import org.springframework.expression.ConstructorResolver;
/*  13:    */ import org.springframework.expression.EvaluationContext;
/*  14:    */ import org.springframework.expression.EvaluationException;
/*  15:    */ import org.springframework.expression.TypeConverter;
/*  16:    */ import org.springframework.expression.TypeLocator;
/*  17:    */ 
/*  18:    */ public class ReflectiveConstructorResolver
/*  19:    */   implements ConstructorResolver
/*  20:    */ {
/*  21:    */   public ConstructorExecutor resolve(EvaluationContext context, String typename, List<TypeDescriptor> argumentTypes)
/*  22:    */     throws AccessException
/*  23:    */   {
/*  24:    */     try
/*  25:    */     {
/*  26: 56 */       TypeConverter typeConverter = context.getTypeConverter();
/*  27: 57 */       Class<?> type = context.getTypeLocator().findType(typename);
/*  28: 58 */       Constructor[] ctors = type.getConstructors();
/*  29:    */       
/*  30: 60 */       Arrays.sort(ctors, new Comparator()
/*  31:    */       {
/*  32:    */         public int compare(Constructor c1, Constructor c2)
/*  33:    */         {
/*  34: 62 */           int c1pl = c1.getParameterTypes().length;
/*  35: 63 */           int c2pl = c2.getParameterTypes().length;
/*  36: 64 */           return new Integer(c1pl).compareTo(Integer.valueOf(c2pl));
/*  37:    */         }
/*  38: 67 */       });
/*  39: 68 */       Constructor closeMatch = null;
/*  40: 69 */       int[] argsToConvert = (int[])null;
/*  41: 70 */       Constructor matchRequiringConversion = null;
/*  42: 72 */       for (Constructor ctor : ctors)
/*  43:    */       {
/*  44: 73 */         Class[] paramTypes = ctor.getParameterTypes();
/*  45: 74 */         List<TypeDescriptor> paramDescriptors = new ArrayList(paramTypes.length);
/*  46: 75 */         for (int i = 0; i < paramTypes.length; i++) {
/*  47: 76 */           paramDescriptors.add(new TypeDescriptor(new MethodParameter(ctor, i)));
/*  48:    */         }
/*  49: 78 */         ReflectionHelper.ArgumentsMatchInfo matchInfo = null;
/*  50: 79 */         if ((ctor.isVarArgs()) && (argumentTypes.size() >= paramTypes.length - 1)) {
/*  51: 86 */           matchInfo = ReflectionHelper.compareArgumentsVarargs(paramDescriptors, argumentTypes, typeConverter);
/*  52: 88 */         } else if (paramTypes.length == argumentTypes.size()) {
/*  53: 90 */           matchInfo = ReflectionHelper.compareArguments(paramDescriptors, argumentTypes, typeConverter);
/*  54:    */         }
/*  55: 92 */         if (matchInfo != null)
/*  56:    */         {
/*  57: 93 */           if (matchInfo.kind == ReflectionHelper.ArgsMatchKind.EXACT) {
/*  58: 94 */             return new ReflectiveConstructorExecutor(ctor, null);
/*  59:    */           }
/*  60: 96 */           if (matchInfo.kind == ReflectionHelper.ArgsMatchKind.CLOSE)
/*  61:    */           {
/*  62: 97 */             closeMatch = ctor;
/*  63:    */           }
/*  64: 99 */           else if (matchInfo.kind == ReflectionHelper.ArgsMatchKind.REQUIRES_CONVERSION)
/*  65:    */           {
/*  66:100 */             argsToConvert = matchInfo.argsRequiringConversion;
/*  67:101 */             matchRequiringConversion = ctor;
/*  68:    */           }
/*  69:    */         }
/*  70:    */       }
/*  71:105 */       if (closeMatch != null) {
/*  72:106 */         return new ReflectiveConstructorExecutor(closeMatch, null);
/*  73:    */       }
/*  74:108 */       if (matchRequiringConversion != null) {
/*  75:109 */         return new ReflectiveConstructorExecutor(matchRequiringConversion, argsToConvert);
/*  76:    */       }
/*  77:112 */       return null;
/*  78:    */     }
/*  79:    */     catch (EvaluationException ex)
/*  80:    */     {
/*  81:116 */       throw new AccessException("Failed to resolve constructor", ex);
/*  82:    */     }
/*  83:    */   }
/*  84:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.support.ReflectiveConstructorResolver
 * JD-Core Version:    0.7.0.1
 */