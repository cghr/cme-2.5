/*   1:    */ package org.springframework.context.support;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.BeansException;
/*   4:    */ import org.springframework.context.ApplicationContext;
/*   5:    */ import org.springframework.core.io.ClassPathResource;
/*   6:    */ import org.springframework.core.io.Resource;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ 
/*   9:    */ public class ClassPathXmlApplicationContext
/*  10:    */   extends AbstractXmlApplicationContext
/*  11:    */ {
/*  12:    */   private Resource[] configResources;
/*  13:    */   
/*  14:    */   public ClassPathXmlApplicationContext() {}
/*  15:    */   
/*  16:    */   public ClassPathXmlApplicationContext(ApplicationContext parent)
/*  17:    */   {
/*  18: 73 */     super(parent);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public ClassPathXmlApplicationContext(String configLocation)
/*  22:    */     throws BeansException
/*  23:    */   {
/*  24: 83 */     this(new String[] { configLocation }, true, null);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public ClassPathXmlApplicationContext(String... configLocations)
/*  28:    */     throws BeansException
/*  29:    */   {
/*  30: 93 */     this(configLocations, true, null);
/*  31:    */   }
/*  32:    */   
/*  33:    */   public ClassPathXmlApplicationContext(String[] configLocations, ApplicationContext parent)
/*  34:    */     throws BeansException
/*  35:    */   {
/*  36:105 */     this(configLocations, true, parent);
/*  37:    */   }
/*  38:    */   
/*  39:    */   public ClassPathXmlApplicationContext(String[] configLocations, boolean refresh)
/*  40:    */     throws BeansException
/*  41:    */   {
/*  42:119 */     this(configLocations, refresh, null);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public ClassPathXmlApplicationContext(String[] configLocations, boolean refresh, ApplicationContext parent)
/*  46:    */     throws BeansException
/*  47:    */   {
/*  48:136 */     super(parent);
/*  49:137 */     setConfigLocations(configLocations);
/*  50:138 */     if (refresh) {
/*  51:139 */       refresh();
/*  52:    */     }
/*  53:    */   }
/*  54:    */   
/*  55:    */   public ClassPathXmlApplicationContext(String path, Class clazz)
/*  56:    */     throws BeansException
/*  57:    */   {
/*  58:158 */     this(new String[] { path }, clazz);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public ClassPathXmlApplicationContext(String[] paths, Class clazz)
/*  62:    */     throws BeansException
/*  63:    */   {
/*  64:172 */     this(paths, clazz, null);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public ClassPathXmlApplicationContext(String[] paths, Class clazz, ApplicationContext parent)
/*  68:    */     throws BeansException
/*  69:    */   {
/*  70:190 */     super(parent);
/*  71:191 */     Assert.notNull(paths, "Path array must not be null");
/*  72:192 */     Assert.notNull(clazz, "Class argument must not be null");
/*  73:193 */     this.configResources = new Resource[paths.length];
/*  74:194 */     for (int i = 0; i < paths.length; i++) {
/*  75:195 */       this.configResources[i] = new ClassPathResource(paths[i], clazz);
/*  76:    */     }
/*  77:197 */     refresh();
/*  78:    */   }
/*  79:    */   
/*  80:    */   protected Resource[] getConfigResources()
/*  81:    */   {
/*  82:203 */     return this.configResources;
/*  83:    */   }
/*  84:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.ClassPathXmlApplicationContext
 * JD-Core Version:    0.7.0.1
 */