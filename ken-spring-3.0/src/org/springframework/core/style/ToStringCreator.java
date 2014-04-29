/*   1:    */ package org.springframework.core.style;
/*   2:    */ 
/*   3:    */ import org.springframework.util.Assert;
/*   4:    */ 
/*   5:    */ public class ToStringCreator
/*   6:    */ {
/*   7: 36 */   private static final ToStringStyler DEFAULT_TO_STRING_STYLER = new DefaultToStringStyler(StylerUtils.DEFAULT_VALUE_STYLER);
/*   8: 39 */   private StringBuilder buffer = new StringBuilder(512);
/*   9:    */   private ToStringStyler styler;
/*  10:    */   private Object object;
/*  11:    */   private boolean styledFirstField;
/*  12:    */   
/*  13:    */   public ToStringCreator(Object obj)
/*  14:    */   {
/*  15: 53 */     this(obj, null);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public ToStringCreator(Object obj, ValueStyler styler)
/*  19:    */   {
/*  20: 62 */     this(obj, new DefaultToStringStyler(styler != null ? styler : StylerUtils.DEFAULT_VALUE_STYLER));
/*  21:    */   }
/*  22:    */   
/*  23:    */   public ToStringCreator(Object obj, ToStringStyler styler)
/*  24:    */   {
/*  25: 71 */     Assert.notNull(obj, "The object to be styled must not be null");
/*  26: 72 */     this.object = obj;
/*  27: 73 */     this.styler = (styler != null ? styler : DEFAULT_TO_STRING_STYLER);
/*  28: 74 */     this.styler.styleStart(this.buffer, this.object);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public ToStringCreator append(String fieldName, byte value)
/*  32:    */   {
/*  33: 85 */     return append(fieldName, new Byte(value));
/*  34:    */   }
/*  35:    */   
/*  36:    */   public ToStringCreator append(String fieldName, short value)
/*  37:    */   {
/*  38: 95 */     return append(fieldName, new Short(value));
/*  39:    */   }
/*  40:    */   
/*  41:    */   public ToStringCreator append(String fieldName, int value)
/*  42:    */   {
/*  43:105 */     return append(fieldName, new Integer(value));
/*  44:    */   }
/*  45:    */   
/*  46:    */   public ToStringCreator append(String fieldName, long value)
/*  47:    */   {
/*  48:115 */     return append(fieldName, new Long(value));
/*  49:    */   }
/*  50:    */   
/*  51:    */   public ToStringCreator append(String fieldName, float value)
/*  52:    */   {
/*  53:125 */     return append(fieldName, new Float(value));
/*  54:    */   }
/*  55:    */   
/*  56:    */   public ToStringCreator append(String fieldName, double value)
/*  57:    */   {
/*  58:135 */     return append(fieldName, new Double(value));
/*  59:    */   }
/*  60:    */   
/*  61:    */   public ToStringCreator append(String fieldName, boolean value)
/*  62:    */   {
/*  63:145 */     return append(fieldName, Boolean.valueOf(value));
/*  64:    */   }
/*  65:    */   
/*  66:    */   public ToStringCreator append(String fieldName, Object value)
/*  67:    */   {
/*  68:155 */     printFieldSeparatorIfNecessary();
/*  69:156 */     this.styler.styleField(this.buffer, fieldName, value);
/*  70:157 */     return this;
/*  71:    */   }
/*  72:    */   
/*  73:    */   private void printFieldSeparatorIfNecessary()
/*  74:    */   {
/*  75:161 */     if (this.styledFirstField) {
/*  76:162 */       this.styler.styleFieldSeparator(this.buffer);
/*  77:    */     } else {
/*  78:165 */       this.styledFirstField = true;
/*  79:    */     }
/*  80:    */   }
/*  81:    */   
/*  82:    */   public ToStringCreator append(Object value)
/*  83:    */   {
/*  84:175 */     this.styler.styleValue(this.buffer, value);
/*  85:176 */     return this;
/*  86:    */   }
/*  87:    */   
/*  88:    */   public String toString()
/*  89:    */   {
/*  90:185 */     this.styler.styleEnd(this.buffer, this.object);
/*  91:186 */     return this.buffer.toString();
/*  92:    */   }
/*  93:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.style.ToStringCreator
 * JD-Core Version:    0.7.0.1
 */