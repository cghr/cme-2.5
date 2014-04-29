/*  1:   */ package org.springframework.context.annotation;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.parsing.Problem;
/*  4:   */ import org.springframework.beans.factory.parsing.ProblemReporter;
/*  5:   */ import org.springframework.core.type.AnnotationMetadata;
/*  6:   */ import org.springframework.core.type.MethodMetadata;
/*  7:   */ 
/*  8:   */ final class BeanMethod
/*  9:   */   extends ConfigurationMethod
/* 10:   */ {
/* 11:   */   public BeanMethod(MethodMetadata metadata, ConfigurationClass configurationClass)
/* 12:   */   {
/* 13:37 */     super(metadata, configurationClass);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public void validate(ProblemReporter problemReporter)
/* 17:   */   {
/* 18:42 */     if (getMetadata().isStatic()) {
/* 19:44 */       return;
/* 20:   */     }
/* 21:47 */     if ((this.configurationClass.getMetadata().isAnnotated(Configuration.class.getName())) && 
/* 22:48 */       (!getMetadata().isOverridable())) {
/* 23:50 */       problemReporter.error(new NonOverridableMethodError());
/* 24:   */     }
/* 25:   */   }
/* 26:   */   
/* 27:   */   private class NonOverridableMethodError
/* 28:   */     extends Problem
/* 29:   */   {
/* 30:   */     public NonOverridableMethodError()
/* 31:   */     {
/* 32:60 */       super(BeanMethod.this.getResourceLocation());
/* 33:   */     }
/* 34:   */   }
/* 35:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.BeanMethod
 * JD-Core Version:    0.7.0.1
 */