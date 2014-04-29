/*  1:   */ package org.springframework.core.style;
/*  2:   */ 
/*  3:   */ import org.springframework.util.Assert;
/*  4:   */ import org.springframework.util.ClassUtils;
/*  5:   */ import org.springframework.util.ObjectUtils;
/*  6:   */ 
/*  7:   */ public class DefaultToStringStyler
/*  8:   */   implements ToStringStyler
/*  9:   */ {
/* 10:   */   private final ValueStyler valueStyler;
/* 11:   */   
/* 12:   */   public DefaultToStringStyler(ValueStyler valueStyler)
/* 13:   */   {
/* 14:43 */     Assert.notNull(valueStyler, "ValueStyler must not be null");
/* 15:44 */     this.valueStyler = valueStyler;
/* 16:   */   }
/* 17:   */   
/* 18:   */   protected final ValueStyler getValueStyler()
/* 19:   */   {
/* 20:51 */     return this.valueStyler;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public void styleStart(StringBuilder buffer, Object obj)
/* 24:   */   {
/* 25:56 */     if (!obj.getClass().isArray())
/* 26:   */     {
/* 27:57 */       buffer.append('[').append(ClassUtils.getShortName(obj.getClass()));
/* 28:58 */       styleIdentityHashCode(buffer, obj);
/* 29:   */     }
/* 30:   */     else
/* 31:   */     {
/* 32:61 */       buffer.append('[');
/* 33:62 */       styleIdentityHashCode(buffer, obj);
/* 34:63 */       buffer.append(' ');
/* 35:64 */       styleValue(buffer, obj);
/* 36:   */     }
/* 37:   */   }
/* 38:   */   
/* 39:   */   private void styleIdentityHashCode(StringBuilder buffer, Object obj)
/* 40:   */   {
/* 41:69 */     buffer.append('@');
/* 42:70 */     buffer.append(ObjectUtils.getIdentityHexString(obj));
/* 43:   */   }
/* 44:   */   
/* 45:   */   public void styleEnd(StringBuilder buffer, Object o)
/* 46:   */   {
/* 47:74 */     buffer.append(']');
/* 48:   */   }
/* 49:   */   
/* 50:   */   public void styleField(StringBuilder buffer, String fieldName, Object value)
/* 51:   */   {
/* 52:78 */     styleFieldStart(buffer, fieldName);
/* 53:79 */     styleValue(buffer, value);
/* 54:80 */     styleFieldEnd(buffer, fieldName);
/* 55:   */   }
/* 56:   */   
/* 57:   */   protected void styleFieldStart(StringBuilder buffer, String fieldName)
/* 58:   */   {
/* 59:84 */     buffer.append(' ').append(fieldName).append(" = ");
/* 60:   */   }
/* 61:   */   
/* 62:   */   protected void styleFieldEnd(StringBuilder buffer, String fieldName) {}
/* 63:   */   
/* 64:   */   public void styleValue(StringBuilder buffer, Object value)
/* 65:   */   {
/* 66:91 */     buffer.append(this.valueStyler.style(value));
/* 67:   */   }
/* 68:   */   
/* 69:   */   public void styleFieldSeparator(StringBuilder buffer)
/* 70:   */   {
/* 71:95 */     buffer.append(',');
/* 72:   */   }
/* 73:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.style.DefaultToStringStyler
 * JD-Core Version:    0.7.0.1
 */