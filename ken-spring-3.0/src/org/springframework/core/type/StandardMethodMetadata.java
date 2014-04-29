/*   1:    */ package org.springframework.core.type;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.lang.reflect.Method;
/*   5:    */ import java.lang.reflect.Modifier;
/*   6:    */ import java.util.Map;
/*   7:    */ import org.springframework.core.annotation.AnnotationUtils;
/*   8:    */ import org.springframework.util.Assert;
/*   9:    */ 
/*  10:    */ public class StandardMethodMetadata
/*  11:    */   implements MethodMetadata
/*  12:    */ {
/*  13:    */   private final Method introspectedMethod;
/*  14:    */   
/*  15:    */   public StandardMethodMetadata(Method introspectedMethod)
/*  16:    */   {
/*  17: 46 */     Assert.notNull(introspectedMethod, "Method must not be null");
/*  18: 47 */     this.introspectedMethod = introspectedMethod;
/*  19:    */   }
/*  20:    */   
/*  21:    */   public final Method getIntrospectedMethod()
/*  22:    */   {
/*  23: 54 */     return this.introspectedMethod;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public String getMethodName()
/*  27:    */   {
/*  28: 59 */     return this.introspectedMethod.getName();
/*  29:    */   }
/*  30:    */   
/*  31:    */   public String getDeclaringClassName()
/*  32:    */   {
/*  33: 63 */     return this.introspectedMethod.getDeclaringClass().getName();
/*  34:    */   }
/*  35:    */   
/*  36:    */   public boolean isStatic()
/*  37:    */   {
/*  38: 67 */     return Modifier.isStatic(this.introspectedMethod.getModifiers());
/*  39:    */   }
/*  40:    */   
/*  41:    */   public boolean isFinal()
/*  42:    */   {
/*  43: 71 */     return Modifier.isFinal(this.introspectedMethod.getModifiers());
/*  44:    */   }
/*  45:    */   
/*  46:    */   public boolean isOverridable()
/*  47:    */   {
/*  48: 75 */     return (!isStatic()) && (!isFinal()) && (!Modifier.isPrivate(this.introspectedMethod.getModifiers()));
/*  49:    */   }
/*  50:    */   
/*  51:    */   public boolean isAnnotated(String annotationType)
/*  52:    */   {
/*  53: 79 */     Annotation[] anns = this.introspectedMethod.getAnnotations();
/*  54: 80 */     for (Annotation ann : anns)
/*  55:    */     {
/*  56: 81 */       if (ann.annotationType().getName().equals(annotationType)) {
/*  57: 82 */         return true;
/*  58:    */       }
/*  59: 84 */       for (Annotation metaAnn : ann.annotationType().getAnnotations()) {
/*  60: 85 */         if (metaAnn.annotationType().getName().equals(annotationType)) {
/*  61: 86 */           return true;
/*  62:    */         }
/*  63:    */       }
/*  64:    */     }
/*  65: 90 */     return false;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public Map<String, Object> getAnnotationAttributes(String annotationType)
/*  69:    */   {
/*  70: 94 */     Annotation[] anns = this.introspectedMethod.getAnnotations();
/*  71: 95 */     for (Annotation ann : anns)
/*  72:    */     {
/*  73: 96 */       if (ann.annotationType().getName().equals(annotationType)) {
/*  74: 97 */         return AnnotationUtils.getAnnotationAttributes(ann, true);
/*  75:    */       }
/*  76: 99 */       for (Annotation metaAnn : ann.annotationType().getAnnotations()) {
/*  77:100 */         if (metaAnn.annotationType().getName().equals(annotationType)) {
/*  78:101 */           return AnnotationUtils.getAnnotationAttributes(metaAnn, true);
/*  79:    */         }
/*  80:    */       }
/*  81:    */     }
/*  82:105 */     return null;
/*  83:    */   }
/*  84:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.StandardMethodMetadata
 * JD-Core Version:    0.7.0.1
 */