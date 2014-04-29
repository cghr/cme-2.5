/*   1:    */ package org.springframework.context.annotation;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.lang.annotation.Documented;
/*   5:    */ import java.lang.annotation.Retention;
/*   6:    */ import java.lang.annotation.RetentionPolicy;
/*   7:    */ import java.lang.annotation.Target;
/*   8:    */ 
/*   9:    */ @Target({java.lang.annotation.ElementType.TYPE})
/*  10:    */ @Retention(RetentionPolicy.RUNTIME)
/*  11:    */ @Documented
/*  12:    */ @Import({LoadTimeWeavingConfiguration.class})
/*  13:    */ public @interface EnableLoadTimeWeaving
/*  14:    */ {
/*  15:    */   AspectJWeaving aspectjWeaving() default AspectJWeaving.AUTODETECT;
/*  16:    */   
/*  17:    */   public static enum AspectJWeaving
/*  18:    */   {
/*  19:145 */     ENABLED,  DISABLED,  AUTODETECT;
/*  20:    */   }
/*  21:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.EnableLoadTimeWeaving
 * JD-Core Version:    0.7.0.1
 */