/*  1:   */ package org.springframework.context.annotation;
/*  2:   */ 
/*  3:   */ import java.util.Map;
/*  4:   */ import org.springframework.core.type.AnnotationMetadata;
/*  5:   */ 
/*  6:   */ class ProfileHelper
/*  7:   */ {
/*  8:   */   static boolean isProfileAnnotationPresent(AnnotationMetadata metadata)
/*  9:   */   {
/* 10:27 */     return metadata.isAnnotated(Profile.class.getName());
/* 11:   */   }
/* 12:   */   
/* 13:   */   static String[] getCandidateProfiles(AnnotationMetadata metadata)
/* 14:   */   {
/* 15:34 */     return (String[])metadata.getAnnotationAttributes(Profile.class.getName()).get("value");
/* 16:   */   }
/* 17:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.ProfileHelper
 * JD-Core Version:    0.7.0.1
 */