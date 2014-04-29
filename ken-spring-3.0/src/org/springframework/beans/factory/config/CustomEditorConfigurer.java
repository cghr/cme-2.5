/*   1:    */ package org.springframework.beans.factory.config;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditor;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.Map.Entry;
/*   6:    */ import org.apache.commons.logging.Log;
/*   7:    */ import org.apache.commons.logging.LogFactory;
/*   8:    */ import org.springframework.beans.BeansException;
/*   9:    */ import org.springframework.beans.FatalBeanException;
/*  10:    */ import org.springframework.beans.PropertyEditorRegistrar;
/*  11:    */ import org.springframework.beans.PropertyEditorRegistry;
/*  12:    */ import org.springframework.beans.PropertyEditorRegistrySupport;
/*  13:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*  14:    */ import org.springframework.core.Ordered;
/*  15:    */ import org.springframework.util.Assert;
/*  16:    */ import org.springframework.util.ClassUtils;
/*  17:    */ 
/*  18:    */ public class CustomEditorConfigurer
/*  19:    */   implements BeanFactoryPostProcessor, BeanClassLoaderAware, Ordered
/*  20:    */ {
/*  21:105 */   protected final Log logger = LogFactory.getLog(getClass());
/*  22:107 */   private int order = 2147483647;
/*  23:    */   private PropertyEditorRegistrar[] propertyEditorRegistrars;
/*  24:    */   private Map<String, ?> customEditors;
/*  25:113 */   private boolean ignoreUnresolvableEditors = false;
/*  26:115 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  27:    */   
/*  28:    */   public void setOrder(int order)
/*  29:    */   {
/*  30:119 */     this.order = order;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public int getOrder()
/*  34:    */   {
/*  35:123 */     return this.order;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setPropertyEditorRegistrars(PropertyEditorRegistrar[] propertyEditorRegistrars)
/*  39:    */   {
/*  40:137 */     this.propertyEditorRegistrars = propertyEditorRegistrars;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setCustomEditors(Map<String, ?> customEditors)
/*  44:    */   {
/*  45:149 */     this.customEditors = customEditors;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setIgnoreUnresolvableEditors(boolean ignoreUnresolvableEditors)
/*  49:    */   {
/*  50:161 */     this.ignoreUnresolvableEditors = ignoreUnresolvableEditors;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setBeanClassLoader(ClassLoader beanClassLoader)
/*  54:    */   {
/*  55:165 */     this.beanClassLoader = beanClassLoader;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*  59:    */     throws BeansException
/*  60:    */   {
/*  61:171 */     if (this.propertyEditorRegistrars != null) {
/*  62:172 */       for (PropertyEditorRegistrar propertyEditorRegistrar : this.propertyEditorRegistrars) {
/*  63:173 */         beanFactory.addPropertyEditorRegistrar(propertyEditorRegistrar);
/*  64:    */       }
/*  65:    */     }
/*  66:177 */     if (this.customEditors != null) {
/*  67:178 */       for (Map.Entry<String, ?> entry : this.customEditors.entrySet())
/*  68:    */       {
/*  69:179 */         String key = (String)entry.getKey();
/*  70:180 */         Object value = entry.getValue();
/*  71:181 */         Class requiredType = null;
/*  72:    */         try
/*  73:    */         {
/*  74:184 */           requiredType = ClassUtils.forName(key, this.beanClassLoader);
/*  75:185 */           if ((value instanceof PropertyEditor))
/*  76:    */           {
/*  77:186 */             if (this.logger.isWarnEnabled()) {
/*  78:187 */               this.logger.warn("Passing PropertyEditor instances into CustomEditorConfigurer is deprecated: use PropertyEditorRegistrars or PropertyEditor class names instead. Offending key [" + 
/*  79:    */               
/*  80:189 */                 key + "; offending editor instance: " + value);
/*  81:    */             }
/*  82:191 */             beanFactory.addPropertyEditorRegistrar(
/*  83:192 */               new SharedPropertyEditorRegistrar(requiredType, (PropertyEditor)value));
/*  84:    */           }
/*  85:194 */           else if ((value instanceof Class))
/*  86:    */           {
/*  87:195 */             beanFactory.registerCustomEditor(requiredType, (Class)value);
/*  88:    */           }
/*  89:197 */           else if ((value instanceof String))
/*  90:    */           {
/*  91:198 */             Class editorClass = ClassUtils.forName((String)value, this.beanClassLoader);
/*  92:199 */             Assert.isAssignable(PropertyEditor.class, editorClass);
/*  93:200 */             beanFactory.registerCustomEditor(requiredType, editorClass);
/*  94:    */           }
/*  95:    */           else
/*  96:    */           {
/*  97:203 */             throw new IllegalArgumentException("Mapped value [" + value + "] for custom editor key [" + 
/*  98:204 */               key + "] is not of required type [" + PropertyEditor.class.getName() + 
/*  99:205 */               "] or a corresponding Class or String value indicating a PropertyEditor implementation");
/* 100:    */           }
/* 101:    */         }
/* 102:    */         catch (ClassNotFoundException ex)
/* 103:    */         {
/* 104:209 */           if (this.ignoreUnresolvableEditors) {
/* 105:210 */             this.logger.info("Skipping editor [" + value + "] for required type [" + key + "]: " + (
/* 106:211 */               requiredType != null ? "editor" : "required type") + " class not found.");
/* 107:    */           } else {
/* 108:214 */             throw new FatalBeanException(
/* 109:215 */               (requiredType != null ? "Editor" : "Required type") + " class not found", ex);
/* 110:    */           }
/* 111:    */         }
/* 112:    */       }
/* 113:    */     }
/* 114:    */   }
/* 115:    */   
/* 116:    */   private static class SharedPropertyEditorRegistrar
/* 117:    */     implements PropertyEditorRegistrar
/* 118:    */   {
/* 119:    */     private final Class requiredType;
/* 120:    */     private final PropertyEditor sharedEditor;
/* 121:    */     
/* 122:    */     public SharedPropertyEditorRegistrar(Class requiredType, PropertyEditor sharedEditor)
/* 123:    */     {
/* 124:233 */       this.requiredType = requiredType;
/* 125:234 */       this.sharedEditor = sharedEditor;
/* 126:    */     }
/* 127:    */     
/* 128:    */     public void registerCustomEditors(PropertyEditorRegistry registry)
/* 129:    */     {
/* 130:238 */       if (!(registry instanceof PropertyEditorRegistrySupport)) {
/* 131:239 */         throw new IllegalArgumentException("Cannot registered shared editor on non-PropertyEditorRegistrySupport registry: " + 
/* 132:240 */           registry);
/* 133:    */       }
/* 134:242 */       ((PropertyEditorRegistrySupport)registry).registerSharedEditor(this.requiredType, this.sharedEditor);
/* 135:    */     }
/* 136:    */   }
/* 137:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.CustomEditorConfigurer
 * JD-Core Version:    0.7.0.1
 */