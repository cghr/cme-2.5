/*  1:   */ package org.springframework.jdbc.core.simple;
/*  2:   */ 
/*  3:   */ import org.springframework.jdbc.core.SingleColumnRowMapper;
/*  4:   */ 
/*  5:   */ public class ParameterizedSingleColumnRowMapper<T>
/*  6:   */   extends SingleColumnRowMapper<T>
/*  7:   */   implements ParameterizedRowMapper<T>
/*  8:   */ {
/*  9:   */   public static <T> ParameterizedSingleColumnRowMapper<T> newInstance(Class<T> requiredType)
/* 10:   */   {
/* 11:45 */     ParameterizedSingleColumnRowMapper<T> rm = new ParameterizedSingleColumnRowMapper();
/* 12:46 */     rm.setRequiredType(requiredType);
/* 13:47 */     return rm;
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.simple.ParameterizedSingleColumnRowMapper
 * JD-Core Version:    0.7.0.1
 */