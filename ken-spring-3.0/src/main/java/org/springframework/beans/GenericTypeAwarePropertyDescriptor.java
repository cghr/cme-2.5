/*   1:    */ package org.springframework.beans;
/*   2:    */ 
/*   3:    */ import java.beans.IntrospectionException;
/*   4:    */ import java.beans.PropertyDescriptor;
/*   5:    */ import java.lang.reflect.Method;
/*   6:    */ import java.util.HashSet;
/*   7:    */ import java.util.Set;
/*   8:    */ import org.apache.commons.logging.Log;
/*   9:    */ import org.apache.commons.logging.LogFactory;
/*  10:    */ import org.springframework.core.BridgeMethodResolver;
/*  11:    */ import org.springframework.core.GenericTypeResolver;
/*  12:    */ import org.springframework.core.MethodParameter;
/*  13:    */ import org.springframework.util.ClassUtils;
/*  14:    */ import org.springframework.util.StringUtils;
/*  15:    */ 
/*  16:    */ class GenericTypeAwarePropertyDescriptor
/*  17:    */   extends PropertyDescriptor
/*  18:    */ {
/*  19:    */   private final Class beanClass;
/*  20:    */   private final Method readMethod;
/*  21:    */   private final Method writeMethod;
/*  22:    */   private final Class propertyEditorClass;
/*  23:    */   private volatile Set<Method> ambiguousWriteMethods;
/*  24:    */   private Class propertyType;
/*  25:    */   private MethodParameter writeMethodParameter;
/*  26:    */   
/*  27:    */   public GenericTypeAwarePropertyDescriptor(Class beanClass, String propertyName, Method readMethod, Method writeMethod, Class propertyEditorClass)
/*  28:    */     throws IntrospectionException
/*  29:    */   {
/*  30: 62 */     super(propertyName, null, null);
/*  31: 63 */     this.beanClass = beanClass;
/*  32: 64 */     this.propertyEditorClass = propertyEditorClass;
/*  33:    */     
/*  34: 66 */     Method readMethodToUse = BridgeMethodResolver.findBridgedMethod(readMethod);
/*  35: 67 */     Method writeMethodToUse = BridgeMethodResolver.findBridgedMethod(writeMethod);
/*  36: 68 */     if ((writeMethodToUse == null) && (readMethodToUse != null)) {
/*  37: 72 */       writeMethodToUse = ClassUtils.getMethodIfAvailable(this.beanClass, 
/*  38: 73 */         "set" + StringUtils.capitalize(getName()), new Class[] { readMethodToUse.getReturnType() });
/*  39:    */     }
/*  40: 75 */     this.readMethod = readMethodToUse;
/*  41: 76 */     this.writeMethod = writeMethodToUse;
/*  42: 78 */     if ((this.writeMethod != null) && (this.readMethod == null))
/*  43:    */     {
/*  44: 82 */       Set<Method> ambiguousCandidates = new HashSet();
/*  45: 83 */       for (Method method : beanClass.getMethods()) {
/*  46: 84 */         if ((method.getName().equals(writeMethodToUse.getName())) && 
/*  47: 85 */           (!method.equals(writeMethodToUse)) && (!method.isBridge())) {
/*  48: 86 */           ambiguousCandidates.add(method);
/*  49:    */         }
/*  50:    */       }
/*  51: 89 */       if (!ambiguousCandidates.isEmpty()) {
/*  52: 90 */         this.ambiguousWriteMethods = ambiguousCandidates;
/*  53:    */       }
/*  54:    */     }
/*  55:    */   }
/*  56:    */   
/*  57:    */   public Class<?> getBeanClass()
/*  58:    */   {
/*  59: 96 */     return this.beanClass;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public Method getReadMethod()
/*  63:    */   {
/*  64:101 */     return this.readMethod;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public Method getWriteMethod()
/*  68:    */   {
/*  69:106 */     return this.writeMethod;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public Method getWriteMethodForActualAccess()
/*  73:    */   {
/*  74:110 */     Set<Method> ambiguousCandidates = this.ambiguousWriteMethods;
/*  75:111 */     if (ambiguousCandidates != null)
/*  76:    */     {
/*  77:112 */       this.ambiguousWriteMethods = null;
/*  78:113 */       LogFactory.getLog(GenericTypeAwarePropertyDescriptor.class).warn("Invalid JavaBean property '" + 
/*  79:114 */         getName() + "' being accessed! Ambiguous write methods found next to actually used [" + 
/*  80:115 */         this.writeMethod + "]: " + ambiguousCandidates);
/*  81:    */     }
/*  82:117 */     return this.writeMethod;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public Class getPropertyEditorClass()
/*  86:    */   {
/*  87:122 */     return this.propertyEditorClass;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public synchronized Class getPropertyType()
/*  91:    */   {
/*  92:127 */     if (this.propertyType == null) {
/*  93:128 */       if (this.readMethod != null)
/*  94:    */       {
/*  95:129 */         this.propertyType = GenericTypeResolver.resolveReturnType(this.readMethod, this.beanClass);
/*  96:    */       }
/*  97:    */       else
/*  98:    */       {
/*  99:132 */         MethodParameter writeMethodParam = getWriteMethodParameter();
/* 100:133 */         if (writeMethodParam != null) {
/* 101:134 */           this.propertyType = writeMethodParam.getParameterType();
/* 102:    */         } else {
/* 103:137 */           this.propertyType = super.getPropertyType();
/* 104:    */         }
/* 105:    */       }
/* 106:    */     }
/* 107:141 */     return this.propertyType;
/* 108:    */   }
/* 109:    */   
/* 110:    */   public synchronized MethodParameter getWriteMethodParameter()
/* 111:    */   {
/* 112:145 */     if (this.writeMethod == null) {
/* 113:146 */       return null;
/* 114:    */     }
/* 115:148 */     if (this.writeMethodParameter == null)
/* 116:    */     {
/* 117:149 */       this.writeMethodParameter = new MethodParameter(this.writeMethod, 0);
/* 118:150 */       GenericTypeResolver.resolveParameterType(this.writeMethodParameter, this.beanClass);
/* 119:    */     }
/* 120:152 */     return this.writeMethodParameter;
/* 121:    */   }
/* 122:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.GenericTypeAwarePropertyDescriptor
 * JD-Core Version:    0.7.0.1
 */