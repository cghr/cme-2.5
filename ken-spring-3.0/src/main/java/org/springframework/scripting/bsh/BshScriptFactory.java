/*   1:    */ package org.springframework.scripting.bsh;
/*   2:    */ 
/*   3:    */ import bsh.EvalError;
/*   4:    */ import java.io.IOException;
/*   5:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*   6:    */ import org.springframework.scripting.ScriptCompilationException;
/*   7:    */ import org.springframework.scripting.ScriptFactory;
/*   8:    */ import org.springframework.scripting.ScriptSource;
/*   9:    */ import org.springframework.util.Assert;
/*  10:    */ import org.springframework.util.ClassUtils;
/*  11:    */ 
/*  12:    */ public class BshScriptFactory
/*  13:    */   implements ScriptFactory, BeanClassLoaderAware
/*  14:    */ {
/*  15:    */   private final String scriptSourceLocator;
/*  16:    */   private final Class[] scriptInterfaces;
/*  17: 50 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  18:    */   private Class scriptClass;
/*  19: 54 */   private final Object scriptClassMonitor = new Object();
/*  20: 56 */   private boolean wasModifiedForTypeCheck = false;
/*  21:    */   
/*  22:    */   public BshScriptFactory(String scriptSourceLocator)
/*  23:    */   {
/*  24: 67 */     this(scriptSourceLocator, null);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public BshScriptFactory(String scriptSourceLocator, Class[] scriptInterfaces)
/*  28:    */   {
/*  29: 82 */     Assert.hasText(scriptSourceLocator, "'scriptSourceLocator' must not be empty");
/*  30: 83 */     this.scriptSourceLocator = scriptSourceLocator;
/*  31: 84 */     this.scriptInterfaces = scriptInterfaces;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setBeanClassLoader(ClassLoader classLoader)
/*  35:    */   {
/*  36: 88 */     this.beanClassLoader = classLoader;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String getScriptSourceLocator()
/*  40:    */   {
/*  41: 93 */     return this.scriptSourceLocator;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public Class[] getScriptInterfaces()
/*  45:    */   {
/*  46: 97 */     return this.scriptInterfaces;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public boolean requiresConfigInterface()
/*  50:    */   {
/*  51:104 */     return true;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public Object getScriptedObject(ScriptSource scriptSource, Class[] actualInterfaces)
/*  55:    */     throws IOException, ScriptCompilationException
/*  56:    */   {
/*  57:    */     try
/*  58:    */     {
/*  59:115 */       Class clazz = null;
/*  60:117 */       synchronized (this.scriptClassMonitor)
/*  61:    */       {
/*  62:118 */         boolean requiresScriptEvaluation = (this.wasModifiedForTypeCheck) && (this.scriptClass == null);
/*  63:119 */         this.wasModifiedForTypeCheck = false;
/*  64:121 */         if ((scriptSource.isModified()) || (requiresScriptEvaluation))
/*  65:    */         {
/*  66:123 */           Object result = BshScriptUtils.evaluateBshScript(
/*  67:124 */             scriptSource.getScriptAsString(), actualInterfaces, this.beanClassLoader);
/*  68:125 */           if ((result instanceof Class)) {
/*  69:128 */             this.scriptClass = ((Class)result);
/*  70:    */           } else {
/*  71:135 */             return result;
/*  72:    */           }
/*  73:    */         }
/*  74:138 */         clazz = this.scriptClass;
/*  75:    */       }
/*  76:141 */       if (clazz != null) {
/*  77:    */         try
/*  78:    */         {
/*  79:144 */           return clazz.newInstance();
/*  80:    */         }
/*  81:    */         catch (Throwable ex)
/*  82:    */         {
/*  83:147 */           throw new ScriptCompilationException(
/*  84:148 */             scriptSource, "Could not instantiate script class: " + clazz.getName(), ex);
/*  85:    */         }
/*  86:    */       }
/*  87:153 */       return BshScriptUtils.createBshObject(
/*  88:154 */         scriptSource.getScriptAsString(), actualInterfaces, this.beanClassLoader);
/*  89:    */     }
/*  90:    */     catch (EvalError ex)
/*  91:    */     {
/*  92:158 */       throw new ScriptCompilationException(scriptSource, ex);
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   /* Error */
/*  97:    */   public Class getScriptedObjectType(ScriptSource scriptSource)
/*  98:    */     throws IOException, ScriptCompilationException
/*  99:    */   {
/* 100:    */     // Byte code:
/* 101:    */     //   0: aload_0
/* 102:    */     //   1: getfield 42	org/springframework/scripting/bsh/BshScriptFactory:scriptClassMonitor	Ljava/lang/Object;
/* 103:    */     //   4: dup
/* 104:    */     //   5: astore_2
/* 105:    */     //   6: monitorenter
/* 106:    */     //   7: aload_1
/* 107:    */     //   8: invokeinterface 76 1 0
/* 108:    */     //   13: ifeq +21 -> 34
/* 109:    */     //   16: aload_0
/* 110:    */     //   17: iconst_1
/* 111:    */     //   18: putfield 44	org/springframework/scripting/bsh/BshScriptFactory:wasModifiedForTypeCheck	Z
/* 112:    */     //   21: aload_0
/* 113:    */     //   22: aload_1
/* 114:    */     //   23: invokeinterface 81 1 0
/* 115:    */     //   28: invokestatic 136	org/springframework/scripting/bsh/BshScriptUtils:determineBshObjectType	(Ljava/lang/String;)Ljava/lang/Class;
/* 116:    */     //   31: putfield 74	org/springframework/scripting/bsh/BshScriptFactory:scriptClass	Ljava/lang/Class;
/* 117:    */     //   34: aload_0
/* 118:    */     //   35: getfield 74	org/springframework/scripting/bsh/BshScriptFactory:scriptClass	Ljava/lang/Class;
/* 119:    */     //   38: aload_2
/* 120:    */     //   39: monitorexit
/* 121:    */     //   40: areturn
/* 122:    */     //   41: aload_2
/* 123:    */     //   42: monitorexit
/* 124:    */     //   43: athrow
/* 125:    */     //   44: astore_2
/* 126:    */     //   45: new 72	org/springframework/scripting/ScriptCompilationException
/* 127:    */     //   48: dup
/* 128:    */     //   49: aload_1
/* 129:    */     //   50: aload_2
/* 130:    */     //   51: invokespecial 118	org/springframework/scripting/ScriptCompilationException:<init>	(Lorg/springframework/scripting/ScriptSource;Ljava/lang/Throwable;)V
/* 131:    */     //   54: athrow
/* 132:    */     // Line number table:
/* 133:    */     //   Java source line #166	-> byte code offset #0
/* 134:    */     //   Java source line #167	-> byte code offset #7
/* 135:    */     //   Java source line #169	-> byte code offset #16
/* 136:    */     //   Java source line #170	-> byte code offset #21
/* 137:    */     //   Java source line #172	-> byte code offset #34
/* 138:    */     //   Java source line #166	-> byte code offset #41
/* 139:    */     //   Java source line #175	-> byte code offset #44
/* 140:    */     //   Java source line #176	-> byte code offset #45
/* 141:    */     // Local variable table:
/* 142:    */     //   start	length	slot	name	signature
/* 143:    */     //   0	55	0	this	BshScriptFactory
/* 144:    */     //   0	55	1	scriptSource	ScriptSource
/* 145:    */     //   44	7	2	ex	EvalError
/* 146:    */     // Exception table:
/* 147:    */     //   from	to	target	type
/* 148:    */     //   7	40	41	finally
/* 149:    */     //   41	43	41	finally
/* 150:    */     //   0	40	44	bsh/EvalError
/* 151:    */     //   41	44	44	bsh/EvalError
/* 152:    */   }
/* 153:    */   
/* 154:    */   public boolean requiresScriptedObjectRefresh(ScriptSource scriptSource)
/* 155:    */   {
/* 156:181 */     synchronized (this.scriptClassMonitor)
/* 157:    */     {
/* 158:182 */       return (scriptSource.isModified()) || (this.wasModifiedForTypeCheck);
/* 159:    */     }
/* 160:    */   }
/* 161:    */   
/* 162:    */   public String toString()
/* 163:    */   {
/* 164:189 */     return "BshScriptFactory: script source locator [" + this.scriptSourceLocator + "]";
/* 165:    */   }
/* 166:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scripting.bsh.BshScriptFactory
 * JD-Core Version:    0.7.0.1
 */