/*  1:   */ package org.springframework.core.env;
/*  2:   */ 
/*  3:   */ public class StandardEnvironment
/*  4:   */   extends AbstractEnvironment
/*  5:   */ {
/*  6:   */   public static final String SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME = "systemEnvironment";
/*  7:   */   public static final String SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME = "systemProperties";
/*  8:   */   
/*  9:   */   protected void customizePropertySources(MutablePropertySources propertySources)
/* 10:   */   {
/* 11:73 */     propertySources.addLast(new MapPropertySource("systemProperties", getSystemProperties()));
/* 12:74 */     propertySources.addLast(new MapPropertySource("systemEnvironment", getSystemEnvironment()));
/* 13:   */   }
/* 14:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.StandardEnvironment
 * JD-Core Version:    0.7.0.1
 */