/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.Properties;
/*   5:    */ import org.springframework.beans.factory.FactoryBean;
/*   6:    */ import org.springframework.beans.factory.InitializingBean;
/*   7:    */ import org.springframework.core.io.support.PropertiesLoaderSupport;
/*   8:    */ 
/*   9:    */ public class PropertiesFactoryBean
/*  10:    */   extends PropertiesLoaderSupport
/*  11:    */   implements FactoryBean<Properties>, InitializingBean
/*  12:    */ {
/*  13: 48 */   private boolean singleton = true;
/*  14:    */   private Properties singletonInstance;
/*  15:    */   
/*  16:    */   public final void setSingleton(boolean singleton)
/*  17:    */   {
/*  18: 59 */     this.singleton = singleton;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public final boolean isSingleton()
/*  22:    */   {
/*  23: 63 */     return this.singleton;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public final void afterPropertiesSet()
/*  27:    */     throws IOException
/*  28:    */   {
/*  29: 68 */     if (this.singleton) {
/*  30: 69 */       this.singletonInstance = createProperties();
/*  31:    */     }
/*  32:    */   }
/*  33:    */   
/*  34:    */   public final Properties getObject()
/*  35:    */     throws IOException
/*  36:    */   {
/*  37: 74 */     if (this.singleton) {
/*  38: 75 */       return this.singletonInstance;
/*  39:    */     }
/*  40: 78 */     return createProperties();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public Class<Properties> getObjectType()
/*  44:    */   {
/*  45: 83 */     return Properties.class;
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected Properties createProperties()
/*  49:    */     throws IOException
/*  50:    */   {
/*  51: 98 */     return (Properties)createInstance();
/*  52:    */   }
/*  53:    */   
/*  54:    */   @Deprecated
/*  55:    */   protected Object createInstance()
/*  56:    */     throws IOException
/*  57:    */   {
/*  58:113 */     return mergeProperties();
/*  59:    */   }
/*  60:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.PropertiesFactoryBean
 * JD-Core Version:    0.7.0.1
 */