/*   1:    */ package org.springframework.context.weaving;
/*   2:    */ 
/*   3:    */ import java.lang.instrument.ClassFileTransformer;
/*   4:    */ import org.apache.commons.logging.Log;
/*   5:    */ import org.apache.commons.logging.LogFactory;
/*   6:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*   7:    */ import org.springframework.beans.factory.DisposableBean;
/*   8:    */ import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
/*   9:    */ import org.springframework.instrument.classloading.LoadTimeWeaver;
/*  10:    */ import org.springframework.instrument.classloading.ReflectiveLoadTimeWeaver;
/*  11:    */ import org.springframework.instrument.classloading.glassfish.GlassFishLoadTimeWeaver;
/*  12:    */ import org.springframework.instrument.classloading.jboss.JBossLoadTimeWeaver;
/*  13:    */ import org.springframework.instrument.classloading.oc4j.OC4JLoadTimeWeaver;
/*  14:    */ import org.springframework.instrument.classloading.weblogic.WebLogicLoadTimeWeaver;
/*  15:    */ import org.springframework.instrument.classloading.websphere.WebSphereLoadTimeWeaver;
/*  16:    */ 
/*  17:    */ public class DefaultContextLoadTimeWeaver
/*  18:    */   implements LoadTimeWeaver, BeanClassLoaderAware, DisposableBean
/*  19:    */ {
/*  20: 61 */   protected final Log logger = LogFactory.getLog(getClass());
/*  21:    */   private LoadTimeWeaver loadTimeWeaver;
/*  22:    */   
/*  23:    */   public DefaultContextLoadTimeWeaver() {}
/*  24:    */   
/*  25:    */   public DefaultContextLoadTimeWeaver(ClassLoader beanClassLoader)
/*  26:    */   {
/*  27: 70 */     setBeanClassLoader(beanClassLoader);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setBeanClassLoader(ClassLoader classLoader)
/*  31:    */   {
/*  32: 74 */     LoadTimeWeaver serverSpecificLoadTimeWeaver = createServerSpecificLoadTimeWeaver(classLoader);
/*  33: 75 */     if (serverSpecificLoadTimeWeaver != null)
/*  34:    */     {
/*  35: 76 */       if (this.logger.isInfoEnabled()) {
/*  36: 77 */         this.logger.info("Determined server-specific load-time weaver: " + 
/*  37: 78 */           serverSpecificLoadTimeWeaver.getClass().getName());
/*  38:    */       }
/*  39: 80 */       this.loadTimeWeaver = serverSpecificLoadTimeWeaver;
/*  40:    */     }
/*  41: 82 */     else if (InstrumentationLoadTimeWeaver.isInstrumentationAvailable())
/*  42:    */     {
/*  43: 83 */       this.logger.info("Found Spring's JVM agent for instrumentation");
/*  44: 84 */       this.loadTimeWeaver = new InstrumentationLoadTimeWeaver(classLoader);
/*  45:    */     }
/*  46:    */     else
/*  47:    */     {
/*  48:    */       try
/*  49:    */       {
/*  50: 88 */         this.loadTimeWeaver = new ReflectiveLoadTimeWeaver(classLoader);
/*  51: 89 */         this.logger.info("Using a reflective load-time weaver for class loader: " + 
/*  52: 90 */           this.loadTimeWeaver.getInstrumentableClassLoader().getClass().getName());
/*  53:    */       }
/*  54:    */       catch (IllegalStateException ex)
/*  55:    */       {
/*  56: 93 */         throw new IllegalStateException(ex.getMessage() + " Specify a custom LoadTimeWeaver or start your " + 
/*  57: 94 */           "Java virtual machine with Spring's agent: -javaagent:org.springframework.instrument.jar");
/*  58:    */       }
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected LoadTimeWeaver createServerSpecificLoadTimeWeaver(ClassLoader classLoader)
/*  63:    */   {
/*  64:109 */     String name = classLoader.getClass().getName();
/*  65:    */     try
/*  66:    */     {
/*  67:111 */       if (name.startsWith("weblogic")) {
/*  68:112 */         return new WebLogicLoadTimeWeaver(classLoader);
/*  69:    */       }
/*  70:114 */       if (name.startsWith("oracle")) {
/*  71:115 */         return new OC4JLoadTimeWeaver(classLoader);
/*  72:    */       }
/*  73:117 */       if ((name.startsWith("com.sun.enterprise")) || (name.startsWith("org.glassfish"))) {
/*  74:118 */         return new GlassFishLoadTimeWeaver(classLoader);
/*  75:    */       }
/*  76:120 */       if (name.startsWith("org.jboss")) {
/*  77:121 */         return new JBossLoadTimeWeaver(classLoader);
/*  78:    */       }
/*  79:123 */       if (name.startsWith("com.ibm")) {
/*  80:124 */         return new WebSphereLoadTimeWeaver(classLoader);
/*  81:    */       }
/*  82:    */     }
/*  83:    */     catch (IllegalStateException ex)
/*  84:    */     {
/*  85:128 */       this.logger.info("Could not obtain server-specific LoadTimeWeaver: " + ex.getMessage());
/*  86:    */     }
/*  87:130 */     return null;
/*  88:    */   }
/*  89:    */   
/*  90:    */   public void destroy()
/*  91:    */   {
/*  92:134 */     if ((this.loadTimeWeaver instanceof InstrumentationLoadTimeWeaver))
/*  93:    */     {
/*  94:135 */       this.logger.info("Removing all registered transformers for class loader: " + 
/*  95:136 */         this.loadTimeWeaver.getInstrumentableClassLoader().getClass().getName());
/*  96:137 */       ((InstrumentationLoadTimeWeaver)this.loadTimeWeaver).removeTransformers();
/*  97:    */     }
/*  98:    */   }
/*  99:    */   
/* 100:    */   public void addTransformer(ClassFileTransformer transformer)
/* 101:    */   {
/* 102:143 */     this.loadTimeWeaver.addTransformer(transformer);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public ClassLoader getInstrumentableClassLoader()
/* 106:    */   {
/* 107:147 */     return this.loadTimeWeaver.getInstrumentableClassLoader();
/* 108:    */   }
/* 109:    */   
/* 110:    */   public ClassLoader getThrowawayClassLoader()
/* 111:    */   {
/* 112:151 */     return this.loadTimeWeaver.getThrowawayClassLoader();
/* 113:    */   }
/* 114:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.weaving.DefaultContextLoadTimeWeaver
 * JD-Core Version:    0.7.0.1
 */