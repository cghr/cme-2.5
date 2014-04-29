/*   1:    */ package org.springframework.beans.propertyeditors;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditorSupport;
/*   4:    */ import org.springframework.util.StringUtils;
/*   5:    */ 
/*   6:    */ public class CustomBooleanEditor
/*   7:    */   extends PropertyEditorSupport
/*   8:    */ {
/*   9:    */   public static final String VALUE_TRUE = "true";
/*  10:    */   public static final String VALUE_FALSE = "false";
/*  11:    */   public static final String VALUE_ON = "on";
/*  12:    */   public static final String VALUE_OFF = "off";
/*  13:    */   public static final String VALUE_YES = "yes";
/*  14:    */   public static final String VALUE_NO = "no";
/*  15:    */   public static final String VALUE_1 = "1";
/*  16:    */   public static final String VALUE_0 = "0";
/*  17:    */   private final String trueString;
/*  18:    */   private final String falseString;
/*  19:    */   private final boolean allowEmpty;
/*  20:    */   
/*  21:    */   public CustomBooleanEditor(boolean allowEmpty)
/*  22:    */   {
/*  23: 71 */     this(null, null, allowEmpty);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public CustomBooleanEditor(String trueString, String falseString, boolean allowEmpty)
/*  27:    */   {
/*  28: 95 */     this.trueString = trueString;
/*  29: 96 */     this.falseString = falseString;
/*  30: 97 */     this.allowEmpty = allowEmpty;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setAsText(String text)
/*  34:    */     throws IllegalArgumentException
/*  35:    */   {
/*  36:102 */     String input = text != null ? text.trim() : null;
/*  37:103 */     if ((this.allowEmpty) && (!StringUtils.hasLength(input))) {
/*  38:105 */       setValue(null);
/*  39:107 */     } else if ((this.trueString != null) && (input.equalsIgnoreCase(this.trueString))) {
/*  40:108 */       setValue(Boolean.TRUE);
/*  41:110 */     } else if ((this.falseString != null) && (input.equalsIgnoreCase(this.falseString))) {
/*  42:111 */       setValue(Boolean.FALSE);
/*  43:113 */     } else if ((this.trueString == null) && (
/*  44:114 */       (input.equalsIgnoreCase("true")) || (input.equalsIgnoreCase("on")) || 
/*  45:115 */       (input.equalsIgnoreCase("yes")) || (input.equals("1")))) {
/*  46:116 */       setValue(Boolean.TRUE);
/*  47:118 */     } else if ((this.falseString == null) && (
/*  48:119 */       (input.equalsIgnoreCase("false")) || (input.equalsIgnoreCase("off")) || 
/*  49:120 */       (input.equalsIgnoreCase("no")) || (input.equals("0")))) {
/*  50:121 */       setValue(Boolean.FALSE);
/*  51:    */     } else {
/*  52:124 */       throw new IllegalArgumentException("Invalid boolean value [" + text + "]");
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   public String getAsText()
/*  57:    */   {
/*  58:130 */     if (Boolean.TRUE.equals(getValue())) {
/*  59:131 */       return this.trueString != null ? this.trueString : "true";
/*  60:    */     }
/*  61:133 */     if (Boolean.FALSE.equals(getValue())) {
/*  62:134 */       return this.falseString != null ? this.falseString : "false";
/*  63:    */     }
/*  64:137 */     return "";
/*  65:    */   }
/*  66:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.CustomBooleanEditor
 * JD-Core Version:    0.7.0.1
 */