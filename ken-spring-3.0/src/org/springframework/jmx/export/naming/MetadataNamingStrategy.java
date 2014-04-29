/*   1:    */ package org.springframework.jmx.export.naming;
/*   2:    */ 
/*   3:    */ import java.util.Hashtable;
/*   4:    */ import javax.management.MalformedObjectNameException;
/*   5:    */ import javax.management.ObjectName;
/*   6:    */ import org.springframework.aop.support.AopUtils;
/*   7:    */ import org.springframework.beans.factory.InitializingBean;
/*   8:    */ import org.springframework.jmx.export.metadata.JmxAttributeSource;
/*   9:    */ import org.springframework.jmx.export.metadata.ManagedResource;
/*  10:    */ import org.springframework.jmx.support.ObjectNameManager;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.util.ClassUtils;
/*  13:    */ import org.springframework.util.StringUtils;
/*  14:    */ 
/*  15:    */ public class MetadataNamingStrategy
/*  16:    */   implements ObjectNamingStrategy, InitializingBean
/*  17:    */ {
/*  18:    */   private JmxAttributeSource attributeSource;
/*  19:    */   private String defaultDomain;
/*  20:    */   
/*  21:    */   public MetadataNamingStrategy() {}
/*  22:    */   
/*  23:    */   public MetadataNamingStrategy(JmxAttributeSource attributeSource)
/*  24:    */   {
/*  25: 72 */     Assert.notNull(attributeSource, "JmxAttributeSource must not be null");
/*  26: 73 */     this.attributeSource = attributeSource;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setAttributeSource(JmxAttributeSource attributeSource)
/*  30:    */   {
/*  31: 82 */     Assert.notNull(attributeSource, "JmxAttributeSource must not be null");
/*  32: 83 */     this.attributeSource = attributeSource;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void setDefaultDomain(String defaultDomain)
/*  36:    */   {
/*  37: 94 */     this.defaultDomain = defaultDomain;
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void afterPropertiesSet()
/*  41:    */   {
/*  42: 98 */     if (this.attributeSource == null) {
/*  43: 99 */       throw new IllegalArgumentException("Property 'attributeSource' is required");
/*  44:    */     }
/*  45:    */   }
/*  46:    */   
/*  47:    */   public ObjectName getObjectName(Object managedBean, String beanKey)
/*  48:    */     throws MalformedObjectNameException
/*  49:    */   {
/*  50:109 */     Class managedClass = AopUtils.getTargetClass(managedBean);
/*  51:110 */     ManagedResource mr = this.attributeSource.getManagedResource(managedClass);
/*  52:113 */     if ((mr != null) && (StringUtils.hasText(mr.getObjectName()))) {
/*  53:114 */       return ObjectNameManager.getInstance(mr.getObjectName());
/*  54:    */     }
/*  55:    */     try
/*  56:    */     {
/*  57:118 */       return ObjectNameManager.getInstance(beanKey);
/*  58:    */     }
/*  59:    */     catch (MalformedObjectNameException localMalformedObjectNameException)
/*  60:    */     {
/*  61:121 */       String domain = this.defaultDomain;
/*  62:122 */       if (domain == null) {
/*  63:123 */         domain = ClassUtils.getPackageName(managedClass);
/*  64:    */       }
/*  65:125 */       Hashtable<String, String> properties = new Hashtable();
/*  66:126 */       properties.put("type", ClassUtils.getShortName(managedClass));
/*  67:127 */       properties.put("name", beanKey);
/*  68:128 */       return ObjectNameManager.getInstance(domain, properties);
/*  69:    */     }
/*  70:    */   }
/*  71:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.naming.MetadataNamingStrategy
 * JD-Core Version:    0.7.0.1
 */