/*   1:    */ package org.springframework.scripting.bsh;
/*   2:    */ 
/*   3:    */ import bsh.EvalError;
/*   4:    */ import bsh.Interpreter;
/*   5:    */ import bsh.Primitive;
/*   6:    */ import bsh.XThis;
/*   7:    */ import java.lang.reflect.InvocationHandler;
/*   8:    */ import java.lang.reflect.Method;
/*   9:    */ import java.lang.reflect.Proxy;
/*  10:    */ import org.springframework.core.NestedRuntimeException;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.util.ClassUtils;
/*  13:    */ import org.springframework.util.ReflectionUtils;
/*  14:    */ 
/*  15:    */ public abstract class BshScriptUtils
/*  16:    */ {
/*  17:    */   public static Object createBshObject(String scriptSource)
/*  18:    */     throws EvalError
/*  19:    */   {
/*  20: 51 */     return createBshObject(scriptSource, null, null);
/*  21:    */   }
/*  22:    */   
/*  23:    */   public static Object createBshObject(String scriptSource, Class[] scriptInterfaces)
/*  24:    */     throws EvalError
/*  25:    */   {
/*  26: 70 */     return createBshObject(scriptSource, scriptInterfaces, ClassUtils.getDefaultClassLoader());
/*  27:    */   }
/*  28:    */   
/*  29:    */   public static Object createBshObject(String scriptSource, Class[] scriptInterfaces, ClassLoader classLoader)
/*  30:    */     throws EvalError
/*  31:    */   {
/*  32: 90 */     Object result = evaluateBshScript(scriptSource, scriptInterfaces, classLoader);
/*  33: 91 */     if ((result instanceof Class))
/*  34:    */     {
/*  35: 92 */       Class clazz = (Class)result;
/*  36:    */       try
/*  37:    */       {
/*  38: 94 */         return clazz.newInstance();
/*  39:    */       }
/*  40:    */       catch (Throwable ex)
/*  41:    */       {
/*  42: 97 */         throw new IllegalStateException("Could not instantiate script class [" + 
/*  43: 98 */           clazz.getName() + "]. Root cause is " + ex);
/*  44:    */       }
/*  45:    */     }
/*  46:102 */     return result;
/*  47:    */   }
/*  48:    */   
/*  49:    */   static Class determineBshObjectType(String scriptSource)
/*  50:    */     throws EvalError
/*  51:    */   {
/*  52:117 */     Assert.hasText(scriptSource, "Script source must not be empty");
/*  53:118 */     Interpreter interpreter = new Interpreter();
/*  54:119 */     Object result = interpreter.eval(scriptSource);
/*  55:120 */     if ((result instanceof Class)) {
/*  56:121 */       return (Class)result;
/*  57:    */     }
/*  58:123 */     if (result != null) {
/*  59:124 */       return result.getClass();
/*  60:    */     }
/*  61:127 */     return null;
/*  62:    */   }
/*  63:    */   
/*  64:    */   static Object evaluateBshScript(String scriptSource, Class[] scriptInterfaces, ClassLoader classLoader)
/*  65:    */     throws EvalError
/*  66:    */   {
/*  67:149 */     Assert.hasText(scriptSource, "Script source must not be empty");
/*  68:150 */     Interpreter interpreter = new Interpreter();
/*  69:151 */     Object result = interpreter.eval(scriptSource);
/*  70:152 */     if (result != null) {
/*  71:153 */       return result;
/*  72:    */     }
/*  73:157 */     Assert.notEmpty(scriptInterfaces, 
/*  74:158 */       "Given script requires a script proxy: At least one script interface is required.");
/*  75:159 */     XThis xt = (XThis)interpreter.eval("return this");
/*  76:160 */     return Proxy.newProxyInstance(classLoader, scriptInterfaces, new BshObjectInvocationHandler(xt));
/*  77:    */   }
/*  78:    */   
/*  79:    */   private static class BshObjectInvocationHandler
/*  80:    */     implements InvocationHandler
/*  81:    */   {
/*  82:    */     private final XThis xt;
/*  83:    */     
/*  84:    */     public BshObjectInvocationHandler(XThis xt)
/*  85:    */     {
/*  86:173 */       this.xt = xt;
/*  87:    */     }
/*  88:    */     
/*  89:    */     public Object invoke(Object proxy, Method method, Object[] args)
/*  90:    */       throws Throwable
/*  91:    */     {
/*  92:177 */       if (ReflectionUtils.isEqualsMethod(method)) {
/*  93:178 */         return Boolean.valueOf(isProxyForSameBshObject(args[0]));
/*  94:    */       }
/*  95:180 */       if (ReflectionUtils.isHashCodeMethod(method)) {
/*  96:181 */         return Integer.valueOf(this.xt.hashCode());
/*  97:    */       }
/*  98:183 */       if (ReflectionUtils.isToStringMethod(method)) {
/*  99:184 */         return "BeanShell object [" + this.xt + "]";
/* 100:    */       }
/* 101:    */       try
/* 102:    */       {
/* 103:187 */         Object result = this.xt.invokeMethod(method.getName(), args);
/* 104:188 */         if ((result == Primitive.NULL) || (result == Primitive.VOID)) {
/* 105:189 */           return null;
/* 106:    */         }
/* 107:191 */         if ((result instanceof Primitive)) {
/* 108:192 */           return ((Primitive)result).getValue();
/* 109:    */         }
/* 110:194 */         return result;
/* 111:    */       }
/* 112:    */       catch (EvalError ex)
/* 113:    */       {
/* 114:197 */         throw new BshScriptUtils.BshExecutionException(ex, null);
/* 115:    */       }
/* 116:    */     }
/* 117:    */     
/* 118:    */     private boolean isProxyForSameBshObject(Object other)
/* 119:    */     {
/* 120:202 */       if (!Proxy.isProxyClass(other.getClass())) {
/* 121:203 */         return false;
/* 122:    */       }
/* 123:205 */       InvocationHandler ih = Proxy.getInvocationHandler(other);
/* 124:    */       
/* 125:207 */       return ((ih instanceof BshObjectInvocationHandler)) && (this.xt.equals(((BshObjectInvocationHandler)ih).xt));
/* 126:    */     }
/* 127:    */   }
/* 128:    */   
/* 129:    */   public static class BshExecutionException
/* 130:    */     extends NestedRuntimeException
/* 131:    */   {
/* 132:    */     private BshExecutionException(EvalError ex)
/* 133:    */     {
/* 134:218 */       super(ex);
/* 135:    */     }
/* 136:    */   }
/* 137:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scripting.bsh.BshScriptUtils
 * JD-Core Version:    0.7.0.1
 */