/*   1:    */ package org.springframework.jndi.support;
/*   2:    */ 
/*   3:    */ import java.util.Arrays;
/*   4:    */ import java.util.Collection;
/*   5:    */ import java.util.HashMap;
/*   6:    */ import java.util.HashSet;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Set;
/*   9:    */ import javax.naming.NameNotFoundException;
/*  10:    */ import javax.naming.NamingException;
/*  11:    */ import org.springframework.beans.BeansException;
/*  12:    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*  13:    */ import org.springframework.beans.factory.BeanFactory;
/*  14:    */ import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
/*  15:    */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*  16:    */ import org.springframework.jndi.JndiLocatorSupport;
/*  17:    */ import org.springframework.jndi.TypeMismatchNamingException;
/*  18:    */ 
/*  19:    */ public class SimpleJndiBeanFactory
/*  20:    */   extends JndiLocatorSupport
/*  21:    */   implements BeanFactory
/*  22:    */ {
/*  23: 63 */   private final Set<String> shareableResources = new HashSet();
/*  24: 66 */   private final Map<String, Object> singletonObjects = new HashMap();
/*  25: 69 */   private final Map<String, Class> resourceTypes = new HashMap();
/*  26:    */   
/*  27:    */   public SimpleJndiBeanFactory()
/*  28:    */   {
/*  29: 73 */     setResourceRef(true);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setShareableResources(String[] shareableResources)
/*  33:    */   {
/*  34: 84 */     this.shareableResources.addAll((Collection)Arrays.asList(shareableResources));
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void addShareableResource(String shareableResource)
/*  38:    */   {
/*  39: 94 */     this.shareableResources.add(shareableResource);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public Object getBean(String name)
/*  43:    */     throws BeansException
/*  44:    */   {
/*  45:104 */     return getBean(name, Object.class);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public <T> T getBean(String name, Class<T> requiredType)
/*  49:    */     throws BeansException
/*  50:    */   {
/*  51:    */     try
/*  52:    */     {
/*  53:109 */       if (isSingleton(name)) {
/*  54:110 */         return doGetSingleton(name, requiredType);
/*  55:    */       }
/*  56:113 */       return lookup(name, requiredType);
/*  57:    */     }
/*  58:    */     catch (NameNotFoundException localNameNotFoundException)
/*  59:    */     {
/*  60:117 */       throw new NoSuchBeanDefinitionException(name, "not found in JNDI environment");
/*  61:    */     }
/*  62:    */     catch (TypeMismatchNamingException ex)
/*  63:    */     {
/*  64:120 */       throw new BeanNotOfRequiredTypeException(name, ex.getRequiredType(), ex.getActualType());
/*  65:    */     }
/*  66:    */     catch (NamingException ex)
/*  67:    */     {
/*  68:123 */       throw new BeanDefinitionStoreException("JNDI environment", name, "JNDI lookup failed", ex);
/*  69:    */     }
/*  70:    */   }
/*  71:    */   
/*  72:    */   public <T> T getBean(Class<T> requiredType)
/*  73:    */     throws BeansException
/*  74:    */   {
/*  75:128 */     return getBean(requiredType.getSimpleName(), requiredType);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public Object getBean(String name, Object... args)
/*  79:    */     throws BeansException
/*  80:    */   {
/*  81:132 */     if (args != null) {
/*  82:133 */       throw new UnsupportedOperationException(
/*  83:134 */         "SimpleJndiBeanFactory does not support explicit bean creation arguments)");
/*  84:    */     }
/*  85:136 */     return getBean(name);
/*  86:    */   }
/*  87:    */   
/*  88:    */   public boolean containsBean(String name)
/*  89:    */   {
/*  90:140 */     if ((this.singletonObjects.containsKey(name)) || (this.resourceTypes.containsKey(name))) {
/*  91:141 */       return true;
/*  92:    */     }
/*  93:    */     try
/*  94:    */     {
/*  95:144 */       doGetType(name);
/*  96:145 */       return true;
/*  97:    */     }
/*  98:    */     catch (NamingException localNamingException) {}
/*  99:148 */     return false;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public boolean isSingleton(String name)
/* 103:    */     throws NoSuchBeanDefinitionException
/* 104:    */   {
/* 105:153 */     return this.shareableResources.contains(name);
/* 106:    */   }
/* 107:    */   
/* 108:    */   public boolean isPrototype(String name)
/* 109:    */     throws NoSuchBeanDefinitionException
/* 110:    */   {
/* 111:157 */     return !this.shareableResources.contains(name);
/* 112:    */   }
/* 113:    */   
/* 114:    */   public boolean isTypeMatch(String name, Class targetType)
/* 115:    */     throws NoSuchBeanDefinitionException
/* 116:    */   {
/* 117:161 */     Class type = getType(name);
/* 118:162 */     return (targetType == null) || ((type != null) && (targetType.isAssignableFrom(type)));
/* 119:    */   }
/* 120:    */   
/* 121:    */   public Class<?> getType(String name)
/* 122:    */     throws NoSuchBeanDefinitionException
/* 123:    */   {
/* 124:    */     try
/* 125:    */     {
/* 126:167 */       return doGetType(name);
/* 127:    */     }
/* 128:    */     catch (NameNotFoundException localNameNotFoundException)
/* 129:    */     {
/* 130:170 */       throw new NoSuchBeanDefinitionException(name, "not found in JNDI environment");
/* 131:    */     }
/* 132:    */     catch (NamingException localNamingException) {}
/* 133:173 */     return null;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public String[] getAliases(String name)
/* 137:    */   {
/* 138:178 */     return new String[0];
/* 139:    */   }
/* 140:    */   
/* 141:    */   private <T> T doGetSingleton(String name, Class<T> requiredType)
/* 142:    */     throws NamingException
/* 143:    */   {
/* 144:184 */     synchronized (this.singletonObjects)
/* 145:    */     {
/* 146:185 */       if (this.singletonObjects.containsKey(name))
/* 147:    */       {
/* 148:186 */         Object jndiObject = this.singletonObjects.get(name);
/* 149:187 */         if ((requiredType != null) && (!requiredType.isInstance(jndiObject))) {
/* 150:188 */           throw new TypeMismatchNamingException(
/* 151:189 */             convertJndiName(name), requiredType, jndiObject != null ? jndiObject.getClass() : null);
/* 152:    */         }
/* 153:191 */         return jndiObject;
/* 154:    */       }
/* 155:193 */       T jndiObject = lookup(name, requiredType);
/* 156:194 */       this.singletonObjects.put(name, jndiObject);
/* 157:195 */       return jndiObject;
/* 158:    */     }
/* 159:    */   }
/* 160:    */   
/* 161:    */   private Class doGetType(String name)
/* 162:    */     throws NamingException
/* 163:    */   {
/* 164:200 */     if (isSingleton(name))
/* 165:    */     {
/* 166:201 */       Object jndiObject = doGetSingleton(name, null);
/* 167:202 */       return jndiObject != null ? jndiObject.getClass() : null;
/* 168:    */     }
/* 169:205 */     synchronized (this.resourceTypes)
/* 170:    */     {
/* 171:206 */       if (this.resourceTypes.containsKey(name)) {
/* 172:207 */         return (Class)this.resourceTypes.get(name);
/* 173:    */       }
/* 174:210 */       Object jndiObject = lookup(name, null);
/* 175:211 */       Class type = jndiObject != null ? jndiObject.getClass() : null;
/* 176:212 */       this.resourceTypes.put(name, type);
/* 177:213 */       return type;
/* 178:    */     }
/* 179:    */   }
/* 180:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jndi.support.SimpleJndiBeanFactory
 * JD-Core Version:    0.7.0.1
 */