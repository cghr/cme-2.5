/*  1:   */ package org.springframework.core.enums;
/*  2:   */ 
/*  3:   */ @Deprecated
/*  4:   */ public abstract class AbstractLabeledEnum
/*  5:   */   implements LabeledEnum
/*  6:   */ {
/*  7:   */   public Class getType()
/*  8:   */   {
/*  9:39 */     boolean isAnonymous = (getClass().getDeclaringClass() == null) && (getClass().getName().indexOf('$') != -1);
/* 10:40 */     return isAnonymous ? getClass().getSuperclass() : getClass();
/* 11:   */   }
/* 12:   */   
/* 13:   */   public int compareTo(Object obj)
/* 14:   */   {
/* 15:44 */     if (!(obj instanceof LabeledEnum)) {
/* 16:45 */       throw new ClassCastException("You may only compare LabeledEnums");
/* 17:   */     }
/* 18:47 */     LabeledEnum that = (LabeledEnum)obj;
/* 19:48 */     if (!getType().equals(that.getType())) {
/* 20:49 */       throw new ClassCastException("You may only compare LabeledEnums of the same type");
/* 21:   */     }
/* 22:51 */     return getCode().compareTo(that.getCode());
/* 23:   */   }
/* 24:   */   
/* 25:   */   public boolean equals(Object obj)
/* 26:   */   {
/* 27:56 */     if (this == obj) {
/* 28:57 */       return true;
/* 29:   */     }
/* 30:59 */     if (!(obj instanceof LabeledEnum)) {
/* 31:60 */       return false;
/* 32:   */     }
/* 33:62 */     LabeledEnum other = (LabeledEnum)obj;
/* 34:63 */     return (getType().equals(other.getType())) && (getCode().equals(other.getCode()));
/* 35:   */   }
/* 36:   */   
/* 37:   */   public int hashCode()
/* 38:   */   {
/* 39:68 */     return getType().hashCode() * 29 + getCode().hashCode();
/* 40:   */   }
/* 41:   */   
/* 42:   */   public String toString()
/* 43:   */   {
/* 44:73 */     return getLabel();
/* 45:   */   }
/* 46:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.enums.AbstractLabeledEnum
 * JD-Core Version:    0.7.0.1
 */