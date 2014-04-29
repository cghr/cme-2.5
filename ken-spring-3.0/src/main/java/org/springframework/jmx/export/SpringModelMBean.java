/*   1:    */ package org.springframework.jmx.export;
/*   2:    */ 
/*   3:    */ import javax.management.Attribute;
/*   4:    */ import javax.management.AttributeList;
/*   5:    */ import javax.management.AttributeNotFoundException;
/*   6:    */ import javax.management.InstanceNotFoundException;
/*   7:    */ import javax.management.InvalidAttributeValueException;
/*   8:    */ import javax.management.MBeanException;
/*   9:    */ import javax.management.ReflectionException;
/*  10:    */ import javax.management.RuntimeOperationsException;
/*  11:    */ import javax.management.modelmbean.InvalidTargetObjectTypeException;
/*  12:    */ import javax.management.modelmbean.ModelMBeanInfo;
/*  13:    */ import javax.management.modelmbean.RequiredModelMBean;
/*  14:    */ 
/*  15:    */ public class SpringModelMBean
/*  16:    */   extends RequiredModelMBean
/*  17:    */ {
/*  18: 46 */   private ClassLoader managedResourceClassLoader = Thread.currentThread().getContextClassLoader();
/*  19:    */   
/*  20:    */   public SpringModelMBean()
/*  21:    */     throws MBeanException, RuntimeOperationsException
/*  22:    */   {}
/*  23:    */   
/*  24:    */   public SpringModelMBean(ModelMBeanInfo mbi)
/*  25:    */     throws MBeanException, RuntimeOperationsException
/*  26:    */   {
/*  27: 62 */     super(mbi);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setManagedResource(Object managedResource, String managedResourceType)
/*  31:    */     throws MBeanException, InstanceNotFoundException, InvalidTargetObjectTypeException
/*  32:    */   {
/*  33: 73 */     this.managedResourceClassLoader = managedResource.getClass().getClassLoader();
/*  34: 74 */     super.setManagedResource(managedResource, managedResourceType);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public Object invoke(String opName, Object[] opArgs, String[] sig)
/*  38:    */     throws MBeanException, ReflectionException
/*  39:    */   {
/*  40: 87 */     ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
/*  41:    */     try
/*  42:    */     {
/*  43: 89 */       Thread.currentThread().setContextClassLoader(this.managedResourceClassLoader);
/*  44: 90 */       return super.invoke(opName, opArgs, sig);
/*  45:    */     }
/*  46:    */     finally
/*  47:    */     {
/*  48: 93 */       Thread.currentThread().setContextClassLoader(currentClassLoader);
/*  49:    */     }
/*  50:    */   }
/*  51:    */   
/*  52:    */   public Object getAttribute(String attrName)
/*  53:    */     throws AttributeNotFoundException, MBeanException, ReflectionException
/*  54:    */   {
/*  55:106 */     ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
/*  56:    */     try
/*  57:    */     {
/*  58:108 */       Thread.currentThread().setContextClassLoader(this.managedResourceClassLoader);
/*  59:109 */       return super.getAttribute(attrName);
/*  60:    */     }
/*  61:    */     finally
/*  62:    */     {
/*  63:112 */       Thread.currentThread().setContextClassLoader(currentClassLoader);
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   public AttributeList getAttributes(String[] attrNames)
/*  68:    */   {
/*  69:123 */     ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
/*  70:    */     try
/*  71:    */     {
/*  72:125 */       Thread.currentThread().setContextClassLoader(this.managedResourceClassLoader);
/*  73:126 */       return super.getAttributes(attrNames);
/*  74:    */     }
/*  75:    */     finally
/*  76:    */     {
/*  77:129 */       Thread.currentThread().setContextClassLoader(currentClassLoader);
/*  78:    */     }
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setAttribute(Attribute attribute)
/*  82:    */     throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException
/*  83:    */   {
/*  84:142 */     ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
/*  85:    */     try
/*  86:    */     {
/*  87:144 */       Thread.currentThread().setContextClassLoader(this.managedResourceClassLoader);
/*  88:145 */       super.setAttribute(attribute);
/*  89:    */     }
/*  90:    */     finally
/*  91:    */     {
/*  92:148 */       Thread.currentThread().setContextClassLoader(currentClassLoader);
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   public AttributeList setAttributes(AttributeList attributes)
/*  97:    */   {
/*  98:159 */     ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
/*  99:    */     try
/* 100:    */     {
/* 101:161 */       Thread.currentThread().setContextClassLoader(this.managedResourceClassLoader);
/* 102:162 */       return super.setAttributes(attributes);
/* 103:    */     }
/* 104:    */     finally
/* 105:    */     {
/* 106:165 */       Thread.currentThread().setContextClassLoader(currentClassLoader);
/* 107:    */     }
/* 108:    */   }
/* 109:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.SpringModelMBean
 * JD-Core Version:    0.7.0.1
 */