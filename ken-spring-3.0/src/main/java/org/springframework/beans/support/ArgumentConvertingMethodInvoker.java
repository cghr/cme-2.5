/*   1:    */ package org.springframework.beans.support;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditor;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import org.springframework.beans.PropertyEditorRegistry;
/*   6:    */ import org.springframework.beans.SimpleTypeConverter;
/*   7:    */ import org.springframework.beans.TypeConverter;
/*   8:    */ import org.springframework.beans.TypeMismatchException;
/*   9:    */ import org.springframework.util.MethodInvoker;
/*  10:    */ import org.springframework.util.ReflectionUtils;
/*  11:    */ 
/*  12:    */ public class ArgumentConvertingMethodInvoker
/*  13:    */   extends MethodInvoker
/*  14:    */ {
/*  15:    */   private TypeConverter typeConverter;
/*  16: 44 */   private boolean useDefaultConverter = true;
/*  17:    */   
/*  18:    */   public void setTypeConverter(TypeConverter typeConverter)
/*  19:    */   {
/*  20: 56 */     this.typeConverter = typeConverter;
/*  21: 57 */     this.useDefaultConverter = false;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public TypeConverter getTypeConverter()
/*  25:    */   {
/*  26: 68 */     if ((this.typeConverter == null) && (this.useDefaultConverter)) {
/*  27: 69 */       this.typeConverter = getDefaultTypeConverter();
/*  28:    */     }
/*  29: 71 */     return this.typeConverter;
/*  30:    */   }
/*  31:    */   
/*  32:    */   protected TypeConverter getDefaultTypeConverter()
/*  33:    */   {
/*  34: 82 */     return new SimpleTypeConverter();
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void registerCustomEditor(Class requiredType, PropertyEditor propertyEditor)
/*  38:    */   {
/*  39: 96 */     TypeConverter converter = getTypeConverter();
/*  40: 97 */     if (!(converter instanceof PropertyEditorRegistry)) {
/*  41: 98 */       throw new IllegalStateException(
/*  42: 99 */         "TypeConverter does not implement PropertyEditorRegistry interface: " + converter);
/*  43:    */     }
/*  44:101 */     ((PropertyEditorRegistry)converter).registerCustomEditor(requiredType, propertyEditor);
/*  45:    */   }
/*  46:    */   
/*  47:    */   protected Method findMatchingMethod()
/*  48:    */   {
/*  49:111 */     Method matchingMethod = super.findMatchingMethod();
/*  50:113 */     if (matchingMethod == null) {
/*  51:115 */       matchingMethod = doFindMatchingMethod(getArguments());
/*  52:    */     }
/*  53:117 */     if (matchingMethod == null) {
/*  54:119 */       matchingMethod = doFindMatchingMethod(new Object[] { getArguments() });
/*  55:    */     }
/*  56:121 */     return matchingMethod;
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected Method doFindMatchingMethod(Object[] arguments)
/*  60:    */   {
/*  61:131 */     TypeConverter converter = getTypeConverter();
/*  62:132 */     if (converter != null)
/*  63:    */     {
/*  64:133 */       String targetMethod = getTargetMethod();
/*  65:134 */       Method matchingMethod = null;
/*  66:135 */       int argCount = arguments.length;
/*  67:136 */       Method[] candidates = ReflectionUtils.getAllDeclaredMethods(getTargetClass());
/*  68:137 */       int minTypeDiffWeight = 2147483647;
/*  69:138 */       Object[] argumentsToUse = (Object[])null;
/*  70:139 */       for (Method candidate : candidates) {
/*  71:140 */         if (candidate.getName().equals(targetMethod))
/*  72:    */         {
/*  73:142 */           Class[] paramTypes = candidate.getParameterTypes();
/*  74:143 */           if (paramTypes.length == argCount)
/*  75:    */           {
/*  76:144 */             Object[] convertedArguments = new Object[argCount];
/*  77:145 */             boolean match = true;
/*  78:146 */             for (int j = 0; (j < argCount) && (match); j++) {
/*  79:    */               try
/*  80:    */               {
/*  81:149 */                 convertedArguments[j] = converter.convertIfNecessary(arguments[j], paramTypes[j]);
/*  82:    */               }
/*  83:    */               catch (TypeMismatchException localTypeMismatchException)
/*  84:    */               {
/*  85:153 */                 match = false;
/*  86:    */               }
/*  87:    */             }
/*  88:156 */             if (match)
/*  89:    */             {
/*  90:157 */               int typeDiffWeight = getTypeDifferenceWeight(paramTypes, convertedArguments);
/*  91:158 */               if (typeDiffWeight < minTypeDiffWeight)
/*  92:    */               {
/*  93:159 */                 minTypeDiffWeight = typeDiffWeight;
/*  94:160 */                 matchingMethod = candidate;
/*  95:161 */                 argumentsToUse = convertedArguments;
/*  96:    */               }
/*  97:    */             }
/*  98:    */           }
/*  99:    */         }
/* 100:    */       }
/* 101:167 */       if (matchingMethod != null)
/* 102:    */       {
/* 103:168 */         setArguments(argumentsToUse);
/* 104:169 */         return matchingMethod;
/* 105:    */       }
/* 106:    */     }
/* 107:172 */     return null;
/* 108:    */   }
/* 109:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.support.ArgumentConvertingMethodInvoker
 * JD-Core Version:    0.7.0.1
 */