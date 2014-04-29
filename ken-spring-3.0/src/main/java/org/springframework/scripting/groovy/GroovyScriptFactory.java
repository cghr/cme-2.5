/*   1:    */ package org.springframework.scripting.groovy;
/*   2:    */ 
/*   3:    */ import groovy.lang.GroovyClassLoader;
/*   4:    */ import groovy.lang.GroovyObject;
/*   5:    */ import groovy.lang.MetaClass;
/*   6:    */ import groovy.lang.Script;
/*   7:    */ import java.io.IOException;
/*   8:    */ import org.codehaus.groovy.control.CompilationFailedException;
/*   9:    */ import org.springframework.beans.BeansException;
/*  10:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  11:    */ import org.springframework.beans.factory.BeanFactory;
/*  12:    */ import org.springframework.beans.factory.BeanFactoryAware;
/*  13:    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*  14:    */ import org.springframework.scripting.ScriptCompilationException;
/*  15:    */ import org.springframework.scripting.ScriptFactory;
/*  16:    */ import org.springframework.scripting.ScriptSource;
/*  17:    */ import org.springframework.util.Assert;
/*  18:    */ import org.springframework.util.ClassUtils;
/*  19:    */ 
/*  20:    */ public class GroovyScriptFactory
/*  21:    */   implements ScriptFactory, BeanFactoryAware, BeanClassLoaderAware
/*  22:    */ {
/*  23:    */   private final String scriptSourceLocator;
/*  24:    */   private final GroovyObjectCustomizer groovyObjectCustomizer;
/*  25:    */   private GroovyClassLoader groovyClassLoader;
/*  26:    */   private Class scriptClass;
/*  27:    */   private Class scriptResultClass;
/*  28:    */   private CachedResultHolder cachedResult;
/*  29: 67 */   private final Object scriptClassMonitor = new Object();
/*  30: 69 */   private boolean wasModifiedForTypeCheck = false;
/*  31:    */   
/*  32:    */   public GroovyScriptFactory(String scriptSourceLocator)
/*  33:    */   {
/*  34: 80 */     this(scriptSourceLocator, null);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public GroovyScriptFactory(String scriptSourceLocator, GroovyObjectCustomizer groovyObjectCustomizer)
/*  38:    */   {
/*  39: 96 */     Assert.hasText(scriptSourceLocator, "'scriptSourceLocator' must not be empty");
/*  40: 97 */     this.scriptSourceLocator = scriptSourceLocator;
/*  41: 98 */     this.groovyObjectCustomizer = groovyObjectCustomizer;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setBeanFactory(BeanFactory beanFactory)
/*  45:    */     throws BeansException
/*  46:    */   {
/*  47:103 */     if ((beanFactory instanceof ConfigurableListableBeanFactory)) {
/*  48:104 */       ((ConfigurableListableBeanFactory)beanFactory).ignoreDependencyType(MetaClass.class);
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void setBeanClassLoader(ClassLoader classLoader)
/*  53:    */   {
/*  54:109 */     this.groovyClassLoader = new GroovyClassLoader(classLoader);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public GroovyClassLoader getGroovyClassLoader()
/*  58:    */   {
/*  59:116 */     synchronized (this.scriptClassMonitor)
/*  60:    */     {
/*  61:117 */       if (this.groovyClassLoader == null) {
/*  62:118 */         this.groovyClassLoader = new GroovyClassLoader(ClassUtils.getDefaultClassLoader());
/*  63:    */       }
/*  64:120 */       return this.groovyClassLoader;
/*  65:    */     }
/*  66:    */   }
/*  67:    */   
/*  68:    */   public String getScriptSourceLocator()
/*  69:    */   {
/*  70:126 */     return this.scriptSourceLocator;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public Class[] getScriptInterfaces()
/*  74:    */   {
/*  75:135 */     return null;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public boolean requiresConfigInterface()
/*  79:    */   {
/*  80:143 */     return false;
/*  81:    */   }
/*  82:    */   
/*  83:    */   public Object getScriptedObject(ScriptSource scriptSource, Class[] actualInterfaces)
/*  84:    */     throws IOException, ScriptCompilationException
/*  85:    */   {
/*  86:    */     try
/*  87:    */     {
/*  88:155 */       Class scriptClassToExecute = null;
/*  89:157 */       synchronized (this.scriptClassMonitor)
/*  90:    */       {
/*  91:158 */         this.wasModifiedForTypeCheck = false;
/*  92:160 */         if (this.cachedResult != null)
/*  93:    */         {
/*  94:161 */           Object result = this.cachedResult.object;
/*  95:162 */           this.cachedResult = null;
/*  96:163 */           return result;
/*  97:    */         }
/*  98:166 */         if ((this.scriptClass == null) || (scriptSource.isModified()))
/*  99:    */         {
/* 100:168 */           this.scriptClass = getGroovyClassLoader()
/* 101:169 */             .parseClass(scriptSource.getScriptAsString(), scriptSource.suggestedClassName());
/* 102:171 */           if (Script.class.isAssignableFrom(this.scriptClass))
/* 103:    */           {
/* 104:173 */             Object result = executeScript(scriptSource, this.scriptClass);
/* 105:174 */             this.scriptResultClass = (result != null ? result.getClass() : null);
/* 106:175 */             return result;
/* 107:    */           }
/* 108:178 */           this.scriptResultClass = this.scriptClass;
/* 109:    */         }
/* 110:181 */         scriptClassToExecute = this.scriptClass;
/* 111:    */       }
/* 112:185 */       return executeScript(scriptSource, scriptClassToExecute);
/* 113:    */     }
/* 114:    */     catch (CompilationFailedException ex)
/* 115:    */     {
/* 116:188 */       throw new ScriptCompilationException(scriptSource, ex);
/* 117:    */     }
/* 118:    */   }
/* 119:    */   
/* 120:    */   /* Error */
/* 121:    */   public Class getScriptedObjectType(ScriptSource scriptSource)
/* 122:    */     throws IOException, ScriptCompilationException
/* 123:    */   {
/* 124:    */     // Byte code:
/* 125:    */     //   0: aload_0
/* 126:    */     //   1: getfield 39	org/springframework/scripting/groovy/GroovyScriptFactory:scriptClassMonitor	Ljava/lang/Object;
/* 127:    */     //   4: dup
/* 128:    */     //   5: astore_2
/* 129:    */     //   6: monitorenter
/* 130:    */     //   7: aload_0
/* 131:    */     //   8: getfield 107	org/springframework/scripting/groovy/GroovyScriptFactory:scriptClass	Ljava/lang/Class;
/* 132:    */     //   11: ifnull +12 -> 23
/* 133:    */     //   14: aload_1
/* 134:    */     //   15: invokeinterface 109 1 0
/* 135:    */     //   20: ifeq +92 -> 112
/* 136:    */     //   23: aload_0
/* 137:    */     //   24: iconst_1
/* 138:    */     //   25: putfield 41	org/springframework/scripting/groovy/GroovyScriptFactory:wasModifiedForTypeCheck	Z
/* 139:    */     //   28: aload_0
/* 140:    */     //   29: aload_0
/* 141:    */     //   30: invokevirtual 114	org/springframework/scripting/groovy/GroovyScriptFactory:getGroovyClassLoader	()Lgroovy/lang/GroovyClassLoader;
/* 142:    */     //   33: aload_1
/* 143:    */     //   34: invokeinterface 116 1 0
/* 144:    */     //   39: aload_1
/* 145:    */     //   40: invokeinterface 119 1 0
/* 146:    */     //   45: invokevirtual 122	groovy/lang/GroovyClassLoader:parseClass	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Class;
/* 147:    */     //   48: putfield 107	org/springframework/scripting/groovy/GroovyScriptFactory:scriptClass	Ljava/lang/Class;
/* 148:    */     //   51: ldc 126
/* 149:    */     //   53: aload_0
/* 150:    */     //   54: getfield 107	org/springframework/scripting/groovy/GroovyScriptFactory:scriptClass	Ljava/lang/Class;
/* 151:    */     //   57: invokevirtual 128	java/lang/Class:isAssignableFrom	(Ljava/lang/Class;)Z
/* 152:    */     //   60: ifeq +44 -> 104
/* 153:    */     //   63: aload_0
/* 154:    */     //   64: aload_1
/* 155:    */     //   65: aload_0
/* 156:    */     //   66: getfield 107	org/springframework/scripting/groovy/GroovyScriptFactory:scriptClass	Ljava/lang/Class;
/* 157:    */     //   69: invokevirtual 134	org/springframework/scripting/groovy/GroovyScriptFactory:executeScript	(Lorg/springframework/scripting/ScriptSource;Ljava/lang/Class;)Ljava/lang/Object;
/* 158:    */     //   72: astore_3
/* 159:    */     //   73: aload_0
/* 160:    */     //   74: aload_3
/* 161:    */     //   75: ifnull +10 -> 85
/* 162:    */     //   78: aload_3
/* 163:    */     //   79: invokevirtual 138	java/lang/Object:getClass	()Ljava/lang/Class;
/* 164:    */     //   82: goto +4 -> 86
/* 165:    */     //   85: aconst_null
/* 166:    */     //   86: putfield 142	org/springframework/scripting/groovy/GroovyScriptFactory:scriptResultClass	Ljava/lang/Class;
/* 167:    */     //   89: aload_0
/* 168:    */     //   90: new 103	org/springframework/scripting/groovy/GroovyScriptFactory$CachedResultHolder
/* 169:    */     //   93: dup
/* 170:    */     //   94: aload_3
/* 171:    */     //   95: invokespecial 159	org/springframework/scripting/groovy/GroovyScriptFactory$CachedResultHolder:<init>	(Ljava/lang/Object;)V
/* 172:    */     //   98: putfield 100	org/springframework/scripting/groovy/GroovyScriptFactory:cachedResult	Lorg/springframework/scripting/groovy/GroovyScriptFactory$CachedResultHolder;
/* 173:    */     //   101: goto +11 -> 112
/* 174:    */     //   104: aload_0
/* 175:    */     //   105: aload_0
/* 176:    */     //   106: getfield 107	org/springframework/scripting/groovy/GroovyScriptFactory:scriptClass	Ljava/lang/Class;
/* 177:    */     //   109: putfield 142	org/springframework/scripting/groovy/GroovyScriptFactory:scriptResultClass	Ljava/lang/Class;
/* 178:    */     //   112: aload_0
/* 179:    */     //   113: getfield 142	org/springframework/scripting/groovy/GroovyScriptFactory:scriptResultClass	Ljava/lang/Class;
/* 180:    */     //   116: aload_2
/* 181:    */     //   117: monitorexit
/* 182:    */     //   118: areturn
/* 183:    */     //   119: aload_2
/* 184:    */     //   120: monitorexit
/* 185:    */     //   121: athrow
/* 186:    */     //   122: astore_2
/* 187:    */     //   123: new 98	org/springframework/scripting/ScriptCompilationException
/* 188:    */     //   126: dup
/* 189:    */     //   127: aload_1
/* 190:    */     //   128: aload_2
/* 191:    */     //   129: invokespecial 144	org/springframework/scripting/ScriptCompilationException:<init>	(Lorg/springframework/scripting/ScriptSource;Ljava/lang/Throwable;)V
/* 192:    */     //   132: athrow
/* 193:    */     // Line number table:
/* 194:    */     //   Java source line #196	-> byte code offset #0
/* 195:    */     //   Java source line #197	-> byte code offset #7
/* 196:    */     //   Java source line #199	-> byte code offset #23
/* 197:    */     //   Java source line #200	-> byte code offset #28
/* 198:    */     //   Java source line #201	-> byte code offset #33
/* 199:    */     //   Java source line #200	-> byte code offset #48
/* 200:    */     //   Java source line #203	-> byte code offset #51
/* 201:    */     //   Java source line #205	-> byte code offset #63
/* 202:    */     //   Java source line #206	-> byte code offset #73
/* 203:    */     //   Java source line #207	-> byte code offset #89
/* 204:    */     //   Java source line #210	-> byte code offset #104
/* 205:    */     //   Java source line #213	-> byte code offset #112
/* 206:    */     //   Java source line #196	-> byte code offset #119
/* 207:    */     //   Java source line #216	-> byte code offset #122
/* 208:    */     //   Java source line #217	-> byte code offset #123
/* 209:    */     // Local variable table:
/* 210:    */     //   start	length	slot	name	signature
/* 211:    */     //   0	133	0	this	GroovyScriptFactory
/* 212:    */     //   0	133	1	scriptSource	ScriptSource
/* 213:    */     //   122	7	2	ex	CompilationFailedException
/* 214:    */     //   72	23	3	result	Object
/* 215:    */     // Exception table:
/* 216:    */     //   from	to	target	type
/* 217:    */     //   7	118	119	finally
/* 218:    */     //   119	121	119	finally
/* 219:    */     //   0	118	122	org/codehaus/groovy/control/CompilationFailedException
/* 220:    */     //   119	122	122	org/codehaus/groovy/control/CompilationFailedException
/* 221:    */   }
/* 222:    */   
/* 223:    */   public boolean requiresScriptedObjectRefresh(ScriptSource scriptSource)
/* 224:    */   {
/* 225:222 */     synchronized (this.scriptClassMonitor)
/* 226:    */     {
/* 227:223 */       return (scriptSource.isModified()) || (this.wasModifiedForTypeCheck);
/* 228:    */     }
/* 229:    */   }
/* 230:    */   
/* 231:    */   protected Object executeScript(ScriptSource scriptSource, Class scriptClass)
/* 232:    */     throws ScriptCompilationException
/* 233:    */   {
/* 234:    */     try
/* 235:    */     {
/* 236:238 */       GroovyObject goo = (GroovyObject)scriptClass.newInstance();
/* 237:240 */       if (this.groovyObjectCustomizer != null) {
/* 238:242 */         this.groovyObjectCustomizer.customize(goo);
/* 239:    */       }
/* 240:245 */       if ((goo instanceof Script)) {
/* 241:247 */         return ((Script)goo).run();
/* 242:    */       }
/* 243:251 */       return goo;
/* 244:    */     }
/* 245:    */     catch (InstantiationException ex)
/* 246:    */     {
/* 247:255 */       throw new ScriptCompilationException(
/* 248:256 */         scriptSource, "Could not instantiate Groovy script class: " + scriptClass.getName(), ex);
/* 249:    */     }
/* 250:    */     catch (IllegalAccessException ex)
/* 251:    */     {
/* 252:259 */       throw new ScriptCompilationException(
/* 253:260 */         scriptSource, "Could not access Groovy script constructor: " + scriptClass.getName(), ex);
/* 254:    */     }
/* 255:    */   }
/* 256:    */   
/* 257:    */   public String toString()
/* 258:    */   {
/* 259:267 */     return "GroovyScriptFactory: script source locator [" + this.scriptSourceLocator + "]";
/* 260:    */   }
/* 261:    */   
/* 262:    */   private static class CachedResultHolder
/* 263:    */   {
/* 264:    */     public final Object object;
/* 265:    */     
/* 266:    */     public CachedResultHolder(Object object)
/* 267:    */     {
/* 268:279 */       this.object = object;
/* 269:    */     }
/* 270:    */   }
/* 271:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scripting.groovy.GroovyScriptFactory
 * JD-Core Version:    0.7.0.1
 */