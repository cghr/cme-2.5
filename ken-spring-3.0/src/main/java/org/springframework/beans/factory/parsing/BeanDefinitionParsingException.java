/*  1:   */ package org.springframework.beans.factory.parsing;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*  4:   */ 
/*  5:   */ public class BeanDefinitionParsingException
/*  6:   */   extends BeanDefinitionStoreException
/*  7:   */ {
/*  8:   */   public BeanDefinitionParsingException(Problem problem)
/*  9:   */   {
/* 10:36 */     super(problem.getResourceDescription(), problem.toString(), problem.getRootCause());
/* 11:   */   }
/* 12:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.BeanDefinitionParsingException
 * JD-Core Version:    0.7.0.1
 */