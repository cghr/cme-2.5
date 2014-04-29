/*   1:    */ package org.springframework.beans.propertyeditors;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditorSupport;
/*   4:    */ import java.text.DateFormat;
/*   5:    */ import java.text.ParseException;
/*   6:    */ import java.util.Date;
/*   7:    */ import org.springframework.util.StringUtils;
/*   8:    */ 
/*   9:    */ public class CustomDateEditor
/*  10:    */   extends PropertyEditorSupport
/*  11:    */ {
/*  12:    */   private final DateFormat dateFormat;
/*  13:    */   private final boolean allowEmpty;
/*  14:    */   private final int exactDateLength;
/*  15:    */   
/*  16:    */   public CustomDateEditor(DateFormat dateFormat, boolean allowEmpty)
/*  17:    */   {
/*  18: 65 */     this.dateFormat = dateFormat;
/*  19: 66 */     this.allowEmpty = allowEmpty;
/*  20: 67 */     this.exactDateLength = -1;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public CustomDateEditor(DateFormat dateFormat, boolean allowEmpty, int exactDateLength)
/*  24:    */   {
/*  25: 89 */     this.dateFormat = dateFormat;
/*  26: 90 */     this.allowEmpty = allowEmpty;
/*  27: 91 */     this.exactDateLength = exactDateLength;
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void setAsText(String text)
/*  31:    */     throws IllegalArgumentException
/*  32:    */   {
/*  33:100 */     if ((this.allowEmpty) && (!StringUtils.hasText(text)))
/*  34:    */     {
/*  35:102 */       setValue(null);
/*  36:    */     }
/*  37:    */     else
/*  38:    */     {
/*  39:104 */       if ((text != null) && (this.exactDateLength >= 0) && (text.length() != this.exactDateLength)) {
/*  40:105 */         throw new IllegalArgumentException(
/*  41:106 */           "Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
/*  42:    */       }
/*  43:    */       try
/*  44:    */       {
/*  45:110 */         setValue(this.dateFormat.parse(text));
/*  46:    */       }
/*  47:    */       catch (ParseException ex)
/*  48:    */       {
/*  49:113 */         throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
/*  50:    */       }
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   public String getAsText()
/*  55:    */   {
/*  56:123 */     Date value = (Date)getValue();
/*  57:124 */     return value != null ? this.dateFormat.format(value) : "";
/*  58:    */   }
/*  59:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.CustomDateEditor
 * JD-Core Version:    0.7.0.1
 */