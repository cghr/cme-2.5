/*  1:   */ package org.springframework.core.env;
/*  2:   */ 
/*  3:   */ import java.util.LinkedHashSet;
/*  4:   */ import java.util.Set;
/*  5:   */ 
/*  6:   */ public class MissingRequiredPropertiesException
/*  7:   */   extends IllegalStateException
/*  8:   */ {
/*  9:34 */   private final Set<String> missingRequiredProperties = new LinkedHashSet();
/* 10:   */   
/* 11:   */   public Set<String> getMissingRequiredProperties()
/* 12:   */   {
/* 13:43 */     return this.missingRequiredProperties;
/* 14:   */   }
/* 15:   */   
/* 16:   */   void addMissingRequiredProperty(String key)
/* 17:   */   {
/* 18:47 */     this.missingRequiredProperties.add(key);
/* 19:   */   }
/* 20:   */   
/* 21:   */   public String getMessage()
/* 22:   */   {
/* 23:52 */     return String.format(
/* 24:53 */       "The following properties were declared as required but could not be resolved: %s", new Object[] {
/* 25:54 */       getMissingRequiredProperties() });
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.env.MissingRequiredPropertiesException
 * JD-Core Version:    0.7.0.1
 */