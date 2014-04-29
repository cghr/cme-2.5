/*   1:    */ package org.springframework.beans.propertyeditors;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditorSupport;
/*   4:    */ import org.springframework.util.ObjectUtils;
/*   5:    */ import org.springframework.util.StringUtils;
/*   6:    */ 
/*   7:    */ public class StringArrayPropertyEditor
/*   8:    */   extends PropertyEditorSupport
/*   9:    */ {
/*  10:    */   public static final String DEFAULT_SEPARATOR = ",";
/*  11:    */   private final String separator;
/*  12:    */   private final String charsToDelete;
/*  13:    */   private final boolean emptyArrayAsNull;
/*  14:    */   private final boolean trimValues;
/*  15:    */   
/*  16:    */   public StringArrayPropertyEditor()
/*  17:    */   {
/*  18: 59 */     this(",", null, false);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public StringArrayPropertyEditor(String separator)
/*  22:    */   {
/*  23: 68 */     this(separator, null, false);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public StringArrayPropertyEditor(String separator, boolean emptyArrayAsNull)
/*  27:    */   {
/*  28: 78 */     this(separator, null, emptyArrayAsNull);
/*  29:    */   }
/*  30:    */   
/*  31:    */   public StringArrayPropertyEditor(String separator, boolean emptyArrayAsNull, boolean trimValues)
/*  32:    */   {
/*  33: 90 */     this(separator, null, emptyArrayAsNull, trimValues);
/*  34:    */   }
/*  35:    */   
/*  36:    */   public StringArrayPropertyEditor(String separator, String charsToDelete, boolean emptyArrayAsNull)
/*  37:    */   {
/*  38:103 */     this(separator, charsToDelete, emptyArrayAsNull, true);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public StringArrayPropertyEditor(String separator, String charsToDelete, boolean emptyArrayAsNull, boolean trimValues)
/*  42:    */   {
/*  43:118 */     this.separator = separator;
/*  44:119 */     this.charsToDelete = charsToDelete;
/*  45:120 */     this.emptyArrayAsNull = emptyArrayAsNull;
/*  46:121 */     this.trimValues = trimValues;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void setAsText(String text)
/*  50:    */     throws IllegalArgumentException
/*  51:    */   {
/*  52:126 */     String[] array = StringUtils.delimitedListToStringArray(text, this.separator, this.charsToDelete);
/*  53:127 */     if (this.trimValues) {
/*  54:128 */       array = StringUtils.trimArrayElements(array);
/*  55:    */     }
/*  56:130 */     if ((this.emptyArrayAsNull) && (array.length == 0)) {
/*  57:131 */       setValue(null);
/*  58:    */     } else {
/*  59:134 */       setValue(array);
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   public String getAsText()
/*  64:    */   {
/*  65:140 */     return StringUtils.arrayToDelimitedString(ObjectUtils.toObjectArray(getValue()), this.separator);
/*  66:    */   }
/*  67:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.StringArrayPropertyEditor
 * JD-Core Version:    0.7.0.1
 */