/*   1:    */ package org.springframework.beans.propertyeditors;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditorSupport;
/*   4:    */ import java.text.NumberFormat;
/*   5:    */ import org.springframework.util.NumberUtils;
/*   6:    */ import org.springframework.util.StringUtils;
/*   7:    */ 
/*   8:    */ public class CustomNumberEditor
/*   9:    */   extends PropertyEditorSupport
/*  10:    */ {
/*  11:    */   private final Class<? extends Number> numberClass;
/*  12:    */   private final NumberFormat numberFormat;
/*  13:    */   private final boolean allowEmpty;
/*  14:    */   
/*  15:    */   public CustomNumberEditor(Class<? extends Number> numberClass, boolean allowEmpty)
/*  16:    */     throws IllegalArgumentException
/*  17:    */   {
/*  18: 71 */     this(numberClass, null, allowEmpty);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public CustomNumberEditor(Class<? extends Number> numberClass, NumberFormat numberFormat, boolean allowEmpty)
/*  22:    */     throws IllegalArgumentException
/*  23:    */   {
/*  24: 91 */     if ((numberClass == null) || (!Number.class.isAssignableFrom(numberClass))) {
/*  25: 92 */       throw new IllegalArgumentException("Property class must be a subclass of Number");
/*  26:    */     }
/*  27: 94 */     this.numberClass = numberClass;
/*  28: 95 */     this.numberFormat = numberFormat;
/*  29: 96 */     this.allowEmpty = allowEmpty;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setAsText(String text)
/*  33:    */     throws IllegalArgumentException
/*  34:    */   {
/*  35:105 */     if ((this.allowEmpty) && (!StringUtils.hasText(text))) {
/*  36:107 */       setValue(null);
/*  37:109 */     } else if (this.numberFormat != null) {
/*  38:111 */       setValue(NumberUtils.parseNumber(text, this.numberClass, this.numberFormat));
/*  39:    */     } else {
/*  40:115 */       setValue(NumberUtils.parseNumber(text, this.numberClass));
/*  41:    */     }
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setValue(Object value)
/*  45:    */   {
/*  46:124 */     if ((value instanceof Number)) {
/*  47:125 */       super.setValue(NumberUtils.convertNumberToTargetClass((Number)value, this.numberClass));
/*  48:    */     } else {
/*  49:128 */       super.setValue(value);
/*  50:    */     }
/*  51:    */   }
/*  52:    */   
/*  53:    */   public String getAsText()
/*  54:    */   {
/*  55:137 */     Object value = getValue();
/*  56:138 */     if (value == null) {
/*  57:139 */       return "";
/*  58:    */     }
/*  59:141 */     if (this.numberFormat != null) {
/*  60:143 */       return this.numberFormat.format(value);
/*  61:    */     }
/*  62:147 */     return value.toString();
/*  63:    */   }
/*  64:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.CustomNumberEditor
 * JD-Core Version:    0.7.0.1
 */