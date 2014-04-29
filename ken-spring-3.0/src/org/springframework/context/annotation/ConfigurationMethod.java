/*  1:   */ package org.springframework.context.annotation;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.parsing.Location;
/*  4:   */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*  5:   */ import org.springframework.core.type.MethodMetadata;
/*  6:   */ 
/*  7:   */ abstract class ConfigurationMethod
/*  8:   */ {
/*  9:   */   protected final MethodMetadata metadata;
/* 10:   */   protected final ConfigurationClass configurationClass;
/* 11:   */   
/* 12:   */   public ConfigurationMethod(MethodMetadata metadata, ConfigurationClass configurationClass)
/* 13:   */   {
/* 14:35 */     this.metadata = metadata;
/* 15:36 */     this.configurationClass = configurationClass;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public MethodMetadata getMetadata()
/* 19:   */   {
/* 20:40 */     return this.metadata;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public ConfigurationClass getConfigurationClass()
/* 24:   */   {
/* 25:44 */     return this.configurationClass;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public Location getResourceLocation()
/* 29:   */   {
/* 30:48 */     return new Location(this.configurationClass.getResource(), this.metadata);
/* 31:   */   }
/* 32:   */   
/* 33:   */   public void validate(ProblemReporter problemReporter) {}
/* 34:   */   
/* 35:   */   public String toString()
/* 36:   */   {
/* 37:56 */     return String.format("[%s:name=%s,declaringClass=%s]", new Object[] {
/* 38:57 */       getClass().getSimpleName(), getMetadata().getMethodName(), getMetadata().getDeclaringClassName() });
/* 39:   */   }
/* 40:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.ConfigurationMethod
 * JD-Core Version:    0.7.0.1
 */