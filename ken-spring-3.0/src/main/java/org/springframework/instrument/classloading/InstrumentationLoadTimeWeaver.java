/*   1:    */ package org.springframework.instrument.classloading;
/*   2:    */ 
/*   3:    */ import java.lang.instrument.ClassFileTransformer;
/*   4:    */ import java.lang.instrument.IllegalClassFormatException;
/*   5:    */ import java.lang.instrument.Instrumentation;
/*   6:    */ import java.security.ProtectionDomain;
/*   7:    */ import java.util.ArrayList;
/*   8:    */ import java.util.List;
/*   9:    */ import org.springframework.instrument.InstrumentationSavingAgent;
/*  10:    */ import org.springframework.util.Assert;
/*  11:    */ import org.springframework.util.ClassUtils;
/*  12:    */ 
/*  13:    */ public class InstrumentationLoadTimeWeaver
/*  14:    */   implements LoadTimeWeaver
/*  15:    */ {
/*  16: 51 */   private static final boolean AGENT_CLASS_PRESENT = ClassUtils.isPresent(
/*  17: 52 */     "org.springframework.instrument.InstrumentationSavingAgent", 
/*  18: 53 */     InstrumentationLoadTimeWeaver.class.getClassLoader());
/*  19:    */   private final ClassLoader classLoader;
/*  20:    */   private final Instrumentation instrumentation;
/*  21: 60 */   private final List<ClassFileTransformer> transformers = new ArrayList(4);
/*  22:    */   
/*  23:    */   public InstrumentationLoadTimeWeaver()
/*  24:    */   {
/*  25: 67 */     this(ClassUtils.getDefaultClassLoader());
/*  26:    */   }
/*  27:    */   
/*  28:    */   public InstrumentationLoadTimeWeaver(ClassLoader classLoader)
/*  29:    */   {
/*  30: 75 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/*  31: 76 */     this.classLoader = classLoader;
/*  32: 77 */     this.instrumentation = getInstrumentation();
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void addTransformer(ClassFileTransformer transformer)
/*  36:    */   {
/*  37: 82 */     Assert.notNull(transformer, "Transformer must not be null");
/*  38: 83 */     FilteringClassFileTransformer actualTransformer = 
/*  39: 84 */       new FilteringClassFileTransformer(transformer, this.classLoader);
/*  40: 85 */     synchronized (this.transformers)
/*  41:    */     {
/*  42: 86 */       if (this.instrumentation == null) {
/*  43: 87 */         throw new IllegalStateException(
/*  44: 88 */           "Must start with Java agent to use InstrumentationLoadTimeWeaver. See Spring documentation.");
/*  45:    */       }
/*  46: 90 */       this.instrumentation.addTransformer(actualTransformer);
/*  47: 91 */       this.transformers.add(actualTransformer);
/*  48:    */     }
/*  49:    */   }
/*  50:    */   
/*  51:    */   public ClassLoader getInstrumentableClassLoader()
/*  52:    */   {
/*  53:101 */     return this.classLoader;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public ClassLoader getThrowawayClassLoader()
/*  57:    */   {
/*  58:108 */     return new SimpleThrowawayClassLoader(getInstrumentableClassLoader());
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void removeTransformers()
/*  62:    */   {
/*  63:115 */     synchronized (this.transformers)
/*  64:    */     {
/*  65:116 */       if (!this.transformers.isEmpty())
/*  66:    */       {
/*  67:117 */         for (int i = this.transformers.size() - 1; i >= 0; i--) {
/*  68:118 */           this.instrumentation.removeTransformer((ClassFileTransformer)this.transformers.get(i));
/*  69:    */         }
/*  70:120 */         this.transformers.clear();
/*  71:    */       }
/*  72:    */     }
/*  73:    */   }
/*  74:    */   
/*  75:    */   public static boolean isInstrumentationAvailable()
/*  76:    */   {
/*  77:131 */     return getInstrumentation() != null;
/*  78:    */   }
/*  79:    */   
/*  80:    */   private static Instrumentation getInstrumentation()
/*  81:    */   {
/*  82:140 */     if (AGENT_CLASS_PRESENT) {
/*  83:141 */       return InstrumentationAccessor.getInstrumentation();
/*  84:    */     }
/*  85:144 */     return null;
/*  86:    */   }
/*  87:    */   
/*  88:    */   private static class InstrumentationAccessor
/*  89:    */   {
/*  90:    */     public static Instrumentation getInstrumentation()
/*  91:    */     {
/*  92:155 */       return InstrumentationSavingAgent.getInstrumentation();
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   private static class FilteringClassFileTransformer
/*  97:    */     implements ClassFileTransformer
/*  98:    */   {
/*  99:    */     private final ClassFileTransformer targetTransformer;
/* 100:    */     private final ClassLoader targetClassLoader;
/* 101:    */     
/* 102:    */     public FilteringClassFileTransformer(ClassFileTransformer targetTransformer, ClassLoader targetClassLoader)
/* 103:    */     {
/* 104:170 */       this.targetTransformer = targetTransformer;
/* 105:171 */       this.targetClassLoader = targetClassLoader;
/* 106:    */     }
/* 107:    */     
/* 108:    */     public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
/* 109:    */       throws IllegalClassFormatException
/* 110:    */     {
/* 111:177 */       if (!this.targetClassLoader.equals(loader)) {
/* 112:178 */         return null;
/* 113:    */       }
/* 114:180 */       return this.targetTransformer.transform(
/* 115:181 */         loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
/* 116:    */     }
/* 117:    */     
/* 118:    */     public String toString()
/* 119:    */     {
/* 120:186 */       return "FilteringClassFileTransformer for: " + this.targetTransformer.toString();
/* 121:    */     }
/* 122:    */   }
/* 123:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver
 * JD-Core Version:    0.7.0.1
 */