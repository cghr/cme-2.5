/*  1:   */ package org.springframework.beans.factory.parsing;
/*  2:   */ 
/*  3:   */ import org.apache.commons.logging.Log;
/*  4:   */ import org.apache.commons.logging.LogFactory;
/*  5:   */ 
/*  6:   */ public class FailFastProblemReporter
/*  7:   */   implements ProblemReporter
/*  8:   */ {
/*  9:39 */   private Log logger = LogFactory.getLog(getClass());
/* 10:   */   
/* 11:   */   public void setLogger(Log logger)
/* 12:   */   {
/* 13:49 */     this.logger = (logger != null ? logger : LogFactory.getLog(getClass()));
/* 14:   */   }
/* 15:   */   
/* 16:   */   public void fatal(Problem problem)
/* 17:   */   {
/* 18:59 */     throw new BeanDefinitionParsingException(problem);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void error(Problem problem)
/* 22:   */   {
/* 23:68 */     throw new BeanDefinitionParsingException(problem);
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void warning(Problem problem)
/* 27:   */   {
/* 28:76 */     this.logger.warn(problem, problem.getRootCause());
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.FailFastProblemReporter
 * JD-Core Version:    0.7.0.1
 */