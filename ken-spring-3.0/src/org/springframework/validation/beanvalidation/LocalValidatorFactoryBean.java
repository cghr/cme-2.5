/*   1:    */ package org.springframework.validation.beanvalidation;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.Map.Entry;
/*   7:    */ import java.util.Properties;
/*   8:    */ import javax.validation.Configuration;
/*   9:    */ import javax.validation.ConstraintValidatorFactory;
/*  10:    */ import javax.validation.MessageInterpolator;
/*  11:    */ import javax.validation.TraversableResolver;
/*  12:    */ import javax.validation.Validation;
/*  13:    */ import javax.validation.Validator;
/*  14:    */ import javax.validation.ValidatorContext;
/*  15:    */ import javax.validation.ValidatorFactory;
/*  16:    */ import javax.validation.bootstrap.GenericBootstrap;
/*  17:    */ import javax.validation.bootstrap.ProviderSpecificBootstrap;
/*  18:    */ import javax.validation.spi.ValidationProvider;
/*  19:    */ import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
/*  20:    */ import org.springframework.beans.factory.InitializingBean;
/*  21:    */ import org.springframework.context.ApplicationContext;
/*  22:    */ import org.springframework.context.ApplicationContextAware;
/*  23:    */ import org.springframework.context.MessageSource;
/*  24:    */ import org.springframework.core.io.Resource;
/*  25:    */ import org.springframework.util.CollectionUtils;
/*  26:    */ 
/*  27:    */ public class LocalValidatorFactoryBean
/*  28:    */   extends SpringValidatorAdapter
/*  29:    */   implements ValidatorFactory, ApplicationContextAware, InitializingBean
/*  30:    */ {
/*  31:    */   private Class providerClass;
/*  32:    */   private MessageInterpolator messageInterpolator;
/*  33:    */   private TraversableResolver traversableResolver;
/*  34:    */   private ConstraintValidatorFactory constraintValidatorFactory;
/*  35:    */   private Resource[] mappingLocations;
/*  36: 76 */   private final Map<String, String> validationPropertyMap = new HashMap();
/*  37:    */   private ApplicationContext applicationContext;
/*  38:    */   private ValidatorFactory validatorFactory;
/*  39:    */   
/*  40:    */   public void setProviderClass(Class<? extends ValidationProvider> providerClass)
/*  41:    */   {
/*  42: 91 */     this.providerClass = providerClass;
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setMessageInterpolator(MessageInterpolator messageInterpolator)
/*  46:    */   {
/*  47: 99 */     this.messageInterpolator = messageInterpolator;
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void setValidationMessageSource(MessageSource messageSource)
/*  51:    */   {
/*  52:118 */     this.messageInterpolator = HibernateValidatorDelegate.buildMessageInterpolator(messageSource);
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setTraversableResolver(TraversableResolver traversableResolver)
/*  56:    */   {
/*  57:126 */     this.traversableResolver = traversableResolver;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setConstraintValidatorFactory(ConstraintValidatorFactory constraintValidatorFactory)
/*  61:    */   {
/*  62:135 */     this.constraintValidatorFactory = constraintValidatorFactory;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setMappingLocations(Resource[] mappingLocations)
/*  66:    */   {
/*  67:142 */     this.mappingLocations = mappingLocations;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void setValidationProperties(Properties jpaProperties)
/*  71:    */   {
/*  72:152 */     CollectionUtils.mergePropertiesIntoMap(jpaProperties, this.validationPropertyMap);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void setValidationPropertyMap(Map<String, String> validationProperties)
/*  76:    */   {
/*  77:161 */     if (validationProperties != null) {
/*  78:162 */       this.validationPropertyMap.putAll(validationProperties);
/*  79:    */     }
/*  80:    */   }
/*  81:    */   
/*  82:    */   public Map<String, String> getValidationPropertyMap()
/*  83:    */   {
/*  84:172 */     return this.validationPropertyMap;
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void setApplicationContext(ApplicationContext applicationContext)
/*  88:    */   {
/*  89:176 */     this.applicationContext = applicationContext;
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void afterPropertiesSet()
/*  93:    */   {
/*  94:183 */     Configuration configuration = this.providerClass != null ? 
/*  95:184 */       Validation.byProvider(this.providerClass).configure() : 
/*  96:185 */       Validation.byDefaultProvider().configure();
/*  97:    */     
/*  98:187 */     MessageInterpolator targetInterpolator = this.messageInterpolator;
/*  99:188 */     if (targetInterpolator == null) {
/* 100:189 */       targetInterpolator = configuration.getDefaultMessageInterpolator();
/* 101:    */     }
/* 102:191 */     configuration.messageInterpolator(new LocaleContextMessageInterpolator(targetInterpolator));
/* 103:193 */     if (this.traversableResolver != null) {
/* 104:194 */       configuration.traversableResolver(this.traversableResolver);
/* 105:    */     }
/* 106:197 */     ConstraintValidatorFactory targetConstraintValidatorFactory = this.constraintValidatorFactory;
/* 107:198 */     if ((targetConstraintValidatorFactory == null) && (this.applicationContext != null)) {
/* 108:199 */       targetConstraintValidatorFactory = 
/* 109:200 */         new SpringConstraintValidatorFactory(this.applicationContext.getAutowireCapableBeanFactory());
/* 110:    */     }
/* 111:202 */     if (targetConstraintValidatorFactory != null) {
/* 112:203 */       configuration.constraintValidatorFactory(targetConstraintValidatorFactory);
/* 113:    */     }
/* 114:206 */     if (this.mappingLocations != null) {
/* 115:207 */       for (Resource location : this.mappingLocations) {
/* 116:    */         try
/* 117:    */         {
/* 118:209 */           configuration.addMapping(location.getInputStream());
/* 119:    */         }
/* 120:    */         catch (IOException localIOException)
/* 121:    */         {
/* 122:212 */           throw new IllegalStateException("Cannot read mapping resource: " + location);
/* 123:    */         }
/* 124:    */       }
/* 125:    */     }
/* 126:217 */     for (Map.Entry<String, String> entry : this.validationPropertyMap.entrySet()) {
/* 127:218 */       configuration.addProperty((String)entry.getKey(), (String)entry.getValue());
/* 128:    */     }
/* 129:221 */     this.validatorFactory = configuration.buildValidatorFactory();
/* 130:222 */     setTargetValidator(this.validatorFactory.getValidator());
/* 131:    */   }
/* 132:    */   
/* 133:    */   public Validator getValidator()
/* 134:    */   {
/* 135:227 */     return this.validatorFactory.getValidator();
/* 136:    */   }
/* 137:    */   
/* 138:    */   public ValidatorContext usingContext()
/* 139:    */   {
/* 140:231 */     return this.validatorFactory.usingContext();
/* 141:    */   }
/* 142:    */   
/* 143:    */   public MessageInterpolator getMessageInterpolator()
/* 144:    */   {
/* 145:235 */     return this.validatorFactory.getMessageInterpolator();
/* 146:    */   }
/* 147:    */   
/* 148:    */   public TraversableResolver getTraversableResolver()
/* 149:    */   {
/* 150:239 */     return this.validatorFactory.getTraversableResolver();
/* 151:    */   }
/* 152:    */   
/* 153:    */   public ConstraintValidatorFactory getConstraintValidatorFactory()
/* 154:    */   {
/* 155:243 */     return this.validatorFactory.getConstraintValidatorFactory();
/* 156:    */   }
/* 157:    */   
/* 158:    */   private static class HibernateValidatorDelegate
/* 159:    */   {
/* 160:    */     public static MessageInterpolator buildMessageInterpolator(MessageSource messageSource)
/* 161:    */     {
/* 162:253 */       return new ResourceBundleMessageInterpolator(new MessageSourceResourceBundleLocator(messageSource));
/* 163:    */     }
/* 164:    */   }
/* 165:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
 * JD-Core Version:    0.7.0.1
 */