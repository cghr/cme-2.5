/*   1:    */ package org.springframework.beans.factory.annotation;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyDescriptor;
/*   4:    */ import java.lang.reflect.Field;
/*   5:    */ import java.lang.reflect.InvocationTargetException;
/*   6:    */ import java.lang.reflect.Member;
/*   7:    */ import java.lang.reflect.Method;
/*   8:    */ import java.util.Collection;
/*   9:    */ import java.util.Collections;
/*  10:    */ import java.util.Iterator;
/*  11:    */ import java.util.LinkedHashSet;
/*  12:    */ import java.util.Set;
/*  13:    */ import org.apache.commons.logging.Log;
/*  14:    */ import org.apache.commons.logging.LogFactory;
/*  15:    */ import org.springframework.beans.MutablePropertyValues;
/*  16:    */ import org.springframework.beans.PropertyValues;
/*  17:    */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*  18:    */ import org.springframework.util.ReflectionUtils;
/*  19:    */ 
/*  20:    */ public class InjectionMetadata
/*  21:    */ {
/*  22: 51 */   private final Log logger = LogFactory.getLog(InjectionMetadata.class);
/*  23:    */   private final Set<InjectedElement> injectedElements;
/*  24:    */   
/*  25:    */   public InjectionMetadata(Class targetClass, Collection<InjectedElement> elements)
/*  26:    */   {
/*  27: 57 */     this.injectedElements = Collections.synchronizedSet(new LinkedHashSet());
/*  28: 58 */     for (InjectedElement element : elements)
/*  29:    */     {
/*  30: 59 */       if (this.logger.isDebugEnabled()) {
/*  31: 60 */         this.logger.debug("Found injected element on class [" + targetClass.getName() + "]: " + element);
/*  32:    */       }
/*  33: 62 */       this.injectedElements.add(element);
/*  34:    */     }
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void checkConfigMembers(RootBeanDefinition beanDefinition)
/*  38:    */   {
/*  39: 67 */     synchronized (this.injectedElements)
/*  40:    */     {
/*  41: 68 */       for (Iterator<InjectedElement> it = this.injectedElements.iterator(); it.hasNext();)
/*  42:    */       {
/*  43: 69 */         Member member = ((InjectedElement)it.next()).getMember();
/*  44: 70 */         if (!beanDefinition.isExternallyManagedConfigMember(member)) {
/*  45: 71 */           beanDefinition.registerExternallyManagedConfigMember(member);
/*  46:    */         } else {
/*  47: 74 */           it.remove();
/*  48:    */         }
/*  49:    */       }
/*  50:    */     }
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void inject(Object target, String beanName, PropertyValues pvs)
/*  54:    */     throws Throwable
/*  55:    */   {
/*  56: 81 */     if (!this.injectedElements.isEmpty())
/*  57:    */     {
/*  58: 82 */       boolean debug = this.logger.isDebugEnabled();
/*  59: 83 */       for (InjectedElement element : this.injectedElements)
/*  60:    */       {
/*  61: 84 */         if (debug) {
/*  62: 85 */           this.logger.debug("Processing injected method of bean '" + beanName + "': " + element);
/*  63:    */         }
/*  64: 87 */         element.inject(target, beanName, pvs);
/*  65:    */       }
/*  66:    */     }
/*  67:    */   }
/*  68:    */   
/*  69:    */   public static abstract class InjectedElement
/*  70:    */   {
/*  71:    */     protected final Member member;
/*  72:    */     protected final boolean isField;
/*  73:    */     protected final PropertyDescriptor pd;
/*  74:    */     protected volatile Boolean skip;
/*  75:    */     
/*  76:    */     protected InjectedElement(Member member, PropertyDescriptor pd)
/*  77:    */     {
/*  78:104 */       this.member = member;
/*  79:105 */       this.isField = (member instanceof Field);
/*  80:106 */       this.pd = pd;
/*  81:    */     }
/*  82:    */     
/*  83:    */     public final Member getMember()
/*  84:    */     {
/*  85:110 */       return this.member;
/*  86:    */     }
/*  87:    */     
/*  88:    */     protected final Class getResourceType()
/*  89:    */     {
/*  90:114 */       if (this.isField) {
/*  91:115 */         return ((Field)this.member).getType();
/*  92:    */       }
/*  93:117 */       if (this.pd != null) {
/*  94:118 */         return this.pd.getPropertyType();
/*  95:    */       }
/*  96:121 */       return ((Method)this.member).getParameterTypes()[0];
/*  97:    */     }
/*  98:    */     
/*  99:    */     protected final void checkResourceType(Class resourceType)
/* 100:    */     {
/* 101:126 */       if (this.isField)
/* 102:    */       {
/* 103:127 */         Class fieldType = ((Field)this.member).getType();
/* 104:128 */         if ((!resourceType.isAssignableFrom(fieldType)) && (!fieldType.isAssignableFrom(resourceType))) {
/* 105:129 */           throw new IllegalStateException("Specified field type [" + fieldType + 
/* 106:130 */             "] is incompatible with resource type [" + resourceType.getName() + "]");
/* 107:    */         }
/* 108:    */       }
/* 109:    */       else
/* 110:    */       {
/* 111:134 */         Class paramType = 
/* 112:135 */           this.pd != null ? this.pd.getPropertyType() : ((Method)this.member).getParameterTypes()[0];
/* 113:136 */         if ((!resourceType.isAssignableFrom(paramType)) && (!paramType.isAssignableFrom(resourceType))) {
/* 114:137 */           throw new IllegalStateException("Specified parameter type [" + paramType + 
/* 115:138 */             "] is incompatible with resource type [" + resourceType.getName() + "]");
/* 116:    */         }
/* 117:    */       }
/* 118:    */     }
/* 119:    */     
/* 120:    */     protected void inject(Object target, String requestingBeanName, PropertyValues pvs)
/* 121:    */       throws Throwable
/* 122:    */     {
/* 123:147 */       if (this.isField)
/* 124:    */       {
/* 125:148 */         Field field = (Field)this.member;
/* 126:149 */         ReflectionUtils.makeAccessible(field);
/* 127:150 */         field.set(target, getResourceToInject(target, requestingBeanName));
/* 128:    */       }
/* 129:    */       else
/* 130:    */       {
/* 131:153 */         if (checkPropertySkipping(pvs)) {
/* 132:154 */           return;
/* 133:    */         }
/* 134:    */         try
/* 135:    */         {
/* 136:157 */           Method method = (Method)this.member;
/* 137:158 */           ReflectionUtils.makeAccessible(method);
/* 138:159 */           method.invoke(target, new Object[] { getResourceToInject(target, requestingBeanName) });
/* 139:    */         }
/* 140:    */         catch (InvocationTargetException ex)
/* 141:    */         {
/* 142:162 */           throw ex.getTargetException();
/* 143:    */         }
/* 144:    */       }
/* 145:    */     }
/* 146:    */     
/* 147:    */     protected boolean checkPropertySkipping(PropertyValues pvs)
/* 148:    */     {
/* 149:173 */       if (this.skip == null)
/* 150:    */       {
/* 151:174 */         if (pvs != null) {
/* 152:175 */           synchronized (pvs)
/* 153:    */           {
/* 154:176 */             if ((this.skip == null) && 
/* 155:177 */               (this.pd != null))
/* 156:    */             {
/* 157:178 */               if (pvs.contains(this.pd.getName()))
/* 158:    */               {
/* 159:180 */                 this.skip = Boolean.valueOf(true);
/* 160:181 */                 return true;
/* 161:    */               }
/* 162:183 */               if ((pvs instanceof MutablePropertyValues)) {
/* 163:184 */                 ((MutablePropertyValues)pvs).registerProcessedProperty(this.pd.getName());
/* 164:    */               }
/* 165:    */             }
/* 166:    */           }
/* 167:    */         }
/* 168:190 */         this.skip = Boolean.valueOf(false);
/* 169:    */       }
/* 170:192 */       return this.skip.booleanValue();
/* 171:    */     }
/* 172:    */     
/* 173:    */     protected Object getResourceToInject(Object target, String requestingBeanName)
/* 174:    */     {
/* 175:199 */       return null;
/* 176:    */     }
/* 177:    */     
/* 178:    */     public boolean equals(Object other)
/* 179:    */     {
/* 180:204 */       if (this == other) {
/* 181:205 */         return true;
/* 182:    */       }
/* 183:207 */       if (!(other instanceof InjectedElement)) {
/* 184:208 */         return false;
/* 185:    */       }
/* 186:210 */       InjectedElement otherElement = (InjectedElement)other;
/* 187:211 */       return this.member.equals(otherElement.member);
/* 188:    */     }
/* 189:    */     
/* 190:    */     public int hashCode()
/* 191:    */     {
/* 192:216 */       return this.member.getClass().hashCode() * 29 + this.member.getName().hashCode();
/* 193:    */     }
/* 194:    */     
/* 195:    */     public String toString()
/* 196:    */     {
/* 197:221 */       return getClass().getSimpleName() + " for " + this.member;
/* 198:    */     }
/* 199:    */   }
/* 200:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.annotation.InjectionMetadata
 * JD-Core Version:    0.7.0.1
 */