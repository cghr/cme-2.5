/*  1:   */ package org.springframework.context.support;
/*  2:   */ 
/*  3:   */ import java.util.Set;
/*  4:   */ import org.springframework.beans.factory.FactoryBean;
/*  5:   */ import org.springframework.beans.factory.InitializingBean;
/*  6:   */ import org.springframework.core.convert.ConversionService;
/*  7:   */ import org.springframework.core.convert.support.ConversionServiceFactory;
/*  8:   */ import org.springframework.core.convert.support.DefaultConversionService;
/*  9:   */ import org.springframework.core.convert.support.GenericConversionService;
/* 10:   */ 
/* 11:   */ public class ConversionServiceFactoryBean
/* 12:   */   implements FactoryBean<ConversionService>, InitializingBean
/* 13:   */ {
/* 14:   */   private Set<?> converters;
/* 15:   */   private GenericConversionService conversionService;
/* 16:   */   
/* 17:   */   public void setConverters(Set<?> converters)
/* 18:   */   {
/* 19:64 */     this.converters = converters;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void afterPropertiesSet()
/* 23:   */   {
/* 24:68 */     this.conversionService = createConversionService();
/* 25:69 */     ConversionServiceFactory.registerConverters(this.converters, this.conversionService);
/* 26:   */   }
/* 27:   */   
/* 28:   */   protected GenericConversionService createConversionService()
/* 29:   */   {
/* 30:79 */     return new DefaultConversionService();
/* 31:   */   }
/* 32:   */   
/* 33:   */   public ConversionService getObject()
/* 34:   */   {
/* 35:86 */     return this.conversionService;
/* 36:   */   }
/* 37:   */   
/* 38:   */   public Class<? extends ConversionService> getObjectType()
/* 39:   */   {
/* 40:90 */     return GenericConversionService.class;
/* 41:   */   }
/* 42:   */   
/* 43:   */   public boolean isSingleton()
/* 44:   */   {
/* 45:94 */     return true;
/* 46:   */   }
/* 47:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.ConversionServiceFactoryBean
 * JD-Core Version:    0.7.0.1
 */