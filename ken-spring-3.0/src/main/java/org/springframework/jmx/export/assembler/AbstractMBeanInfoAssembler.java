/*   1:    */ package org.springframework.jmx.export.assembler;
/*   2:    */ 
/*   3:    */ import javax.management.Descriptor;
/*   4:    */ import javax.management.JMException;
/*   5:    */ import javax.management.modelmbean.ModelMBeanAttributeInfo;
/*   6:    */ import javax.management.modelmbean.ModelMBeanConstructorInfo;
/*   7:    */ import javax.management.modelmbean.ModelMBeanInfo;
/*   8:    */ import javax.management.modelmbean.ModelMBeanInfoSupport;
/*   9:    */ import javax.management.modelmbean.ModelMBeanNotificationInfo;
/*  10:    */ import javax.management.modelmbean.ModelMBeanOperationInfo;
/*  11:    */ import org.springframework.aop.support.AopUtils;
/*  12:    */ import org.springframework.jmx.support.JmxUtils;
/*  13:    */ 
/*  14:    */ public abstract class AbstractMBeanInfoAssembler
/*  15:    */   implements MBeanInfoAssembler
/*  16:    */ {
/*  17:    */   public ModelMBeanInfo getMBeanInfo(Object managedBean, String beanKey)
/*  18:    */     throws JMException
/*  19:    */   {
/*  20: 63 */     checkManagedBean(managedBean);
/*  21: 64 */     ModelMBeanInfo info = new ModelMBeanInfoSupport(
/*  22: 65 */       getClassName(managedBean, beanKey), getDescription(managedBean, beanKey), 
/*  23: 66 */       getAttributeInfo(managedBean, beanKey), getConstructorInfo(managedBean, beanKey), 
/*  24: 67 */       getOperationInfo(managedBean, beanKey), getNotificationInfo(managedBean, beanKey));
/*  25: 68 */     Descriptor desc = info.getMBeanDescriptor();
/*  26: 69 */     populateMBeanDescriptor(desc, managedBean, beanKey);
/*  27: 70 */     info.setMBeanDescriptor(desc);
/*  28: 71 */     return info;
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected void checkManagedBean(Object managedBean)
/*  32:    */     throws IllegalArgumentException
/*  33:    */   {}
/*  34:    */   
/*  35:    */   protected Class getTargetClass(Object managedBean)
/*  36:    */   {
/*  37: 94 */     return AopUtils.getTargetClass(managedBean);
/*  38:    */   }
/*  39:    */   
/*  40:    */   protected Class<?> getClassToExpose(Object managedBean)
/*  41:    */   {
/*  42:106 */     return JmxUtils.getClassToExpose(managedBean);
/*  43:    */   }
/*  44:    */   
/*  45:    */   protected Class<?> getClassToExpose(Class<?> beanClass)
/*  46:    */   {
/*  47:117 */     return JmxUtils.getClassToExpose(beanClass);
/*  48:    */   }
/*  49:    */   
/*  50:    */   protected String getClassName(Object managedBean, String beanKey)
/*  51:    */     throws JMException
/*  52:    */   {
/*  53:131 */     return getTargetClass(managedBean).getName();
/*  54:    */   }
/*  55:    */   
/*  56:    */   protected String getDescription(Object managedBean, String beanKey)
/*  57:    */     throws JMException
/*  58:    */   {
/*  59:144 */     String targetClassName = getTargetClass(managedBean).getName();
/*  60:145 */     if (AopUtils.isAopProxy(managedBean)) {
/*  61:146 */       return "Proxy for " + targetClassName;
/*  62:    */     }
/*  63:148 */     return targetClassName;
/*  64:    */   }
/*  65:    */   
/*  66:    */   protected void populateMBeanDescriptor(Descriptor descriptor, Object managedBean, String beanKey)
/*  67:    */     throws JMException
/*  68:    */   {}
/*  69:    */   
/*  70:    */   protected ModelMBeanConstructorInfo[] getConstructorInfo(Object managedBean, String beanKey)
/*  71:    */     throws JMException
/*  72:    */   {
/*  73:179 */     return new ModelMBeanConstructorInfo[0];
/*  74:    */   }
/*  75:    */   
/*  76:    */   protected ModelMBeanNotificationInfo[] getNotificationInfo(Object managedBean, String beanKey)
/*  77:    */     throws JMException
/*  78:    */   {
/*  79:195 */     return new ModelMBeanNotificationInfo[0];
/*  80:    */   }
/*  81:    */   
/*  82:    */   protected abstract ModelMBeanAttributeInfo[] getAttributeInfo(Object paramObject, String paramString)
/*  83:    */     throws JMException;
/*  84:    */   
/*  85:    */   protected abstract ModelMBeanOperationInfo[] getOperationInfo(Object paramObject, String paramString)
/*  86:    */     throws JMException;
/*  87:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.assembler.AbstractMBeanInfoAssembler
 * JD-Core Version:    0.7.0.1
 */