/*   1:    */ package org.springframework.context.weaving;
/*   2:    */ 
/*   3:    */ import java.lang.instrument.ClassFileTransformer;
/*   4:    */ import java.lang.instrument.IllegalClassFormatException;
/*   5:    */ import java.security.ProtectionDomain;
/*   6:    */ import org.aspectj.weaver.loadtime.ClassPreProcessorAgentAdapter;
/*   7:    */ import org.springframework.beans.BeansException;
/*   8:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*   9:    */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*  10:    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*  11:    */ import org.springframework.core.Ordered;
/*  12:    */ import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
/*  13:    */ import org.springframework.instrument.classloading.LoadTimeWeaver;
/*  14:    */ 
/*  15:    */ public class AspectJWeavingEnabler
/*  16:    */   implements BeanFactoryPostProcessor, BeanClassLoaderAware, LoadTimeWeaverAware, Ordered
/*  17:    */ {
/*  18:    */   private ClassLoader beanClassLoader;
/*  19:    */   private LoadTimeWeaver loadTimeWeaver;
/*  20:    */   public static final String ASPECTJ_AOP_XML_RESOURCE = "META-INF/aop.xml";
/*  21:    */   
/*  22:    */   public void setBeanClassLoader(ClassLoader classLoader)
/*  23:    */   {
/*  24: 54 */     this.beanClassLoader = classLoader;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public void setLoadTimeWeaver(LoadTimeWeaver loadTimeWeaver)
/*  28:    */   {
/*  29: 58 */     this.loadTimeWeaver = loadTimeWeaver;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public int getOrder()
/*  33:    */   {
/*  34: 62 */     return -2147483648;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*  38:    */     throws BeansException
/*  39:    */   {
/*  40: 66 */     enableAspectJWeaving(this.loadTimeWeaver, this.beanClassLoader);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public static void enableAspectJWeaving(LoadTimeWeaver weaverToUse, ClassLoader beanClassLoader)
/*  44:    */   {
/*  45: 70 */     if (weaverToUse == null) {
/*  46: 71 */       if (InstrumentationLoadTimeWeaver.isInstrumentationAvailable()) {
/*  47: 72 */         weaverToUse = new InstrumentationLoadTimeWeaver(beanClassLoader);
/*  48:    */       } else {
/*  49: 75 */         throw new IllegalStateException("No LoadTimeWeaver available");
/*  50:    */       }
/*  51:    */     }
/*  52: 78 */     weaverToUse.addTransformer(new AspectJClassBypassingClassFileTransformer(
/*  53: 79 */       new ClassPreProcessorAgentAdapter()));
/*  54:    */   }
/*  55:    */   
/*  56:    */   private static class AspectJClassBypassingClassFileTransformer
/*  57:    */     implements ClassFileTransformer
/*  58:    */   {
/*  59:    */     private final ClassFileTransformer delegate;
/*  60:    */     
/*  61:    */     public AspectJClassBypassingClassFileTransformer(ClassFileTransformer delegate)
/*  62:    */     {
/*  63: 95 */       this.delegate = delegate;
/*  64:    */     }
/*  65:    */     
/*  66:    */     public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
/*  67:    */       throws IllegalClassFormatException
/*  68:    */     {
/*  69:101 */       if ((className.startsWith("org.aspectj")) || (className.startsWith("org/aspectj"))) {
/*  70:102 */         return classfileBuffer;
/*  71:    */       }
/*  72:104 */       return this.delegate.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
/*  73:    */     }
/*  74:    */   }
/*  75:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.weaving.AspectJWeavingEnabler
 * JD-Core Version:    0.7.0.1
 */