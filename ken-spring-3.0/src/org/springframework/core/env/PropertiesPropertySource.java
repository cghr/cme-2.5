/*  1:   */ package org.springframework.core.env;
/*  2:   */ 
/*  3:   */ import java.util.Properties;
/*  4:   */ 
/*  5:   */ public class PropertiesPropertySource
/*  6:   */   extends MapPropertySource
/*  7:   */ {
/*  8:   */   public PropertiesPropertySource(String name, Properties source)
/*  9:   */   {
/* 10:38 */     super(name, source);
/* 11:   */   }
/* 12:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.PropertiesPropertySource
 * JD-Core Version:    0.7.0.1
 */