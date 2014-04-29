/*  1:   */ package org.springframework.validation;
/*  2:   */ 
/*  3:   */ import org.springframework.context.support.DefaultMessageSourceResolvable;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ 
/*  6:   */ public class ObjectError
/*  7:   */   extends DefaultMessageSourceResolvable
/*  8:   */ {
/*  9:   */   private final String objectName;
/* 10:   */   
/* 11:   */   public ObjectError(String objectName, String defaultMessage)
/* 12:   */   {
/* 13:46 */     this(objectName, null, null, defaultMessage);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public ObjectError(String objectName, String[] codes, Object[] arguments, String defaultMessage)
/* 17:   */   {
/* 18:57 */     super(codes, arguments, defaultMessage);
/* 19:58 */     Assert.notNull(objectName, "Object name must not be null");
/* 20:59 */     this.objectName = objectName;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public String getObjectName()
/* 24:   */   {
/* 25:67 */     return this.objectName;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public String toString()
/* 29:   */   {
/* 30:73 */     return "Error in object '" + this.objectName + "': " + resolvableToString();
/* 31:   */   }
/* 32:   */   
/* 33:   */   public boolean equals(Object other)
/* 34:   */   {
/* 35:78 */     if (this == other) {
/* 36:79 */       return true;
/* 37:   */     }
/* 38:81 */     if ((!getClass().equals(other.getClass())) || (!super.equals(other))) {
/* 39:82 */       return false;
/* 40:   */     }
/* 41:84 */     ObjectError otherError = (ObjectError)other;
/* 42:85 */     return getObjectName().equals(otherError.getObjectName());
/* 43:   */   }
/* 44:   */   
/* 45:   */   public int hashCode()
/* 46:   */   {
/* 47:90 */     return super.hashCode() * 29 + getObjectName().hashCode();
/* 48:   */   }
/* 49:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.ObjectError
 * JD-Core Version:    0.7.0.1
 */