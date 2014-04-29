/*  1:   */ package org.springframework.jdbc.core.simple;
/*  2:   */ 
/*  3:   */ import org.springframework.jdbc.core.BeanPropertyRowMapper;
/*  4:   */ 
/*  5:   */ public class ParameterizedBeanPropertyRowMapper<T>
/*  6:   */   extends BeanPropertyRowMapper<T>
/*  7:   */   implements ParameterizedRowMapper<T>
/*  8:   */ {
/*  9:   */   public static <T> ParameterizedBeanPropertyRowMapper<T> newInstance(Class<T> mappedClass)
/* 10:   */   {
/* 11:62 */     ParameterizedBeanPropertyRowMapper<T> newInstance = new ParameterizedBeanPropertyRowMapper();
/* 12:63 */     newInstance.setMappedClass(mappedClass);
/* 13:64 */     return newInstance;
/* 14:   */   }
/* 15:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper
 * JD-Core Version:    0.7.0.1
 */