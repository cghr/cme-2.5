/*   1:    */ package org.springframework.jmx.support;
/*   2:    */ 
/*   3:    */ import java.util.LinkedHashSet;
/*   4:    */ import java.util.Set;
/*   5:    */ import javax.management.InstanceAlreadyExistsException;
/*   6:    */ import javax.management.InstanceNotFoundException;
/*   7:    */ import javax.management.JMException;
/*   8:    */ import javax.management.MBeanServer;
/*   9:    */ import javax.management.ObjectInstance;
/*  10:    */ import javax.management.ObjectName;
/*  11:    */ import org.apache.commons.logging.Log;
/*  12:    */ import org.apache.commons.logging.LogFactory;
/*  13:    */ import org.springframework.core.Constants;
/*  14:    */ 
/*  15:    */ public class MBeanRegistrationSupport
/*  16:    */ {
/*  17:    */   public static final int REGISTRATION_FAIL_ON_EXISTING = 0;
/*  18:    */   public static final int REGISTRATION_IGNORE_EXISTING = 1;
/*  19:    */   public static final int REGISTRATION_REPLACE_EXISTING = 2;
/*  20: 92 */   private static final Constants constants = new Constants(MBeanRegistrationSupport.class);
/*  21: 97 */   protected final Log logger = LogFactory.getLog(getClass());
/*  22:    */   protected MBeanServer server;
/*  23:107 */   protected final Set<ObjectName> registeredBeans = new LinkedHashSet();
/*  24:113 */   private int registrationBehavior = 0;
/*  25:    */   
/*  26:    */   public void setServer(MBeanServer server)
/*  27:    */   {
/*  28:122 */     this.server = server;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public final MBeanServer getServer()
/*  32:    */   {
/*  33:129 */     return this.server;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setRegistrationBehaviorName(String registrationBehavior)
/*  37:    */   {
/*  38:141 */     setRegistrationBehavior(constants.asNumber(registrationBehavior).intValue());
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setRegistrationBehavior(int registrationBehavior)
/*  42:    */   {
/*  43:154 */     this.registrationBehavior = registrationBehavior;
/*  44:    */   }
/*  45:    */   
/*  46:    */   protected void doRegister(Object mbean, ObjectName objectName)
/*  47:    */     throws JMException
/*  48:    */   {
/*  49:167 */     ObjectInstance registeredBean = null;
/*  50:    */     try
/*  51:    */     {
/*  52:169 */       registeredBean = this.server.registerMBean(mbean, objectName);
/*  53:    */     }
/*  54:    */     catch (InstanceAlreadyExistsException ex)
/*  55:    */     {
/*  56:172 */       if (this.registrationBehavior == 1)
/*  57:    */       {
/*  58:173 */         if (this.logger.isDebugEnabled()) {
/*  59:174 */           this.logger.debug("Ignoring existing MBean at [" + objectName + "]");
/*  60:    */         }
/*  61:    */       }
/*  62:177 */       else if (this.registrationBehavior == 2) {
/*  63:    */         try
/*  64:    */         {
/*  65:179 */           if (this.logger.isDebugEnabled()) {
/*  66:180 */             this.logger.debug("Replacing existing MBean at [" + objectName + "]");
/*  67:    */           }
/*  68:182 */           this.server.unregisterMBean(objectName);
/*  69:183 */           registeredBean = this.server.registerMBean(mbean, objectName);
/*  70:    */         }
/*  71:    */         catch (InstanceNotFoundException ex2)
/*  72:    */         {
/*  73:186 */           this.logger.error("Unable to replace existing MBean at [" + objectName + "]", ex2);
/*  74:187 */           throw ex;
/*  75:    */         }
/*  76:    */       } else {
/*  77:191 */         throw ex;
/*  78:    */       }
/*  79:    */     }
/*  80:196 */     ObjectName actualObjectName = registeredBean != null ? registeredBean.getObjectName() : null;
/*  81:197 */     if (actualObjectName == null) {
/*  82:198 */       actualObjectName = objectName;
/*  83:    */     }
/*  84:200 */     this.registeredBeans.add(actualObjectName);
/*  85:201 */     onRegister(actualObjectName, mbean);
/*  86:    */   }
/*  87:    */   
/*  88:    */   protected void unregisterBeans()
/*  89:    */   {
/*  90:208 */     for (ObjectName objectName : this.registeredBeans) {
/*  91:209 */       doUnregister(objectName);
/*  92:    */     }
/*  93:211 */     this.registeredBeans.clear();
/*  94:    */   }
/*  95:    */   
/*  96:    */   protected void doUnregister(ObjectName objectName)
/*  97:    */   {
/*  98:    */     try
/*  99:    */     {
/* 100:221 */       if (this.server.isRegistered(objectName))
/* 101:    */       {
/* 102:222 */         this.server.unregisterMBean(objectName);
/* 103:223 */         onUnregister(objectName);
/* 104:    */       }
/* 105:226 */       else if (this.logger.isWarnEnabled())
/* 106:    */       {
/* 107:227 */         this.logger.warn("Could not unregister MBean [" + objectName + "] as said MBean " + 
/* 108:228 */           "is not registered (perhaps already unregistered by an external process)");
/* 109:    */       }
/* 110:    */     }
/* 111:    */     catch (JMException ex)
/* 112:    */     {
/* 113:233 */       if (this.logger.isErrorEnabled()) {
/* 114:234 */         this.logger.error("Could not unregister MBean [" + objectName + "]", ex);
/* 115:    */       }
/* 116:    */     }
/* 117:    */   }
/* 118:    */   
/* 119:    */   protected final ObjectName[] getRegisteredObjectNames()
/* 120:    */   {
/* 121:243 */     return (ObjectName[])this.registeredBeans.toArray(new ObjectName[this.registeredBeans.size()]);
/* 122:    */   }
/* 123:    */   
/* 124:    */   protected void onRegister(ObjectName objectName, Object mbean)
/* 125:    */   {
/* 126:255 */     onRegister(objectName);
/* 127:    */   }
/* 128:    */   
/* 129:    */   protected void onRegister(ObjectName objectName) {}
/* 130:    */   
/* 131:    */   protected void onUnregister(ObjectName objectName) {}
/* 132:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.support.MBeanRegistrationSupport
 * JD-Core Version:    0.7.0.1
 */