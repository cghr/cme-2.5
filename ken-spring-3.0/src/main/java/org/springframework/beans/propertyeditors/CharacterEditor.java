/*   1:    */ package org.springframework.beans.propertyeditors;
/*   2:    */ 
/*   3:    */ import java.beans.PropertyEditorSupport;
/*   4:    */ import org.springframework.util.StringUtils;
/*   5:    */ 
/*   6:    */ public class CharacterEditor
/*   7:    */   extends PropertyEditorSupport
/*   8:    */ {
/*   9:    */   private static final String UNICODE_PREFIX = "\\u";
/*  10:    */   private static final int UNICODE_LENGTH = 6;
/*  11:    */   private final boolean allowEmpty;
/*  12:    */   
/*  13:    */   public CharacterEditor(boolean allowEmpty)
/*  14:    */   {
/*  15: 68 */     this.allowEmpty = allowEmpty;
/*  16:    */   }
/*  17:    */   
/*  18:    */   public void setAsText(String text)
/*  19:    */     throws IllegalArgumentException
/*  20:    */   {
/*  21: 74 */     if ((this.allowEmpty) && (!StringUtils.hasLength(text)))
/*  22:    */     {
/*  23: 76 */       setValue(null);
/*  24:    */     }
/*  25:    */     else
/*  26:    */     {
/*  27: 78 */       if (text == null) {
/*  28: 79 */         throw new IllegalArgumentException("null String cannot be converted to char type");
/*  29:    */       }
/*  30: 81 */       if (isUnicodeCharacterSequence(text))
/*  31:    */       {
/*  32: 82 */         setAsUnicode(text);
/*  33:    */       }
/*  34:    */       else
/*  35:    */       {
/*  36: 84 */         if (text.length() != 1) {
/*  37: 85 */           throw new IllegalArgumentException("String [" + text + "] with length " + 
/*  38: 86 */             text.length() + " cannot be converted to char type");
/*  39:    */         }
/*  40: 89 */         setValue(new Character(text.charAt(0)));
/*  41:    */       }
/*  42:    */     }
/*  43:    */   }
/*  44:    */   
/*  45:    */   public String getAsText()
/*  46:    */   {
/*  47: 95 */     Object value = getValue();
/*  48: 96 */     return value != null ? value.toString() : "";
/*  49:    */   }
/*  50:    */   
/*  51:    */   private boolean isUnicodeCharacterSequence(String sequence)
/*  52:    */   {
/*  53:101 */     return (sequence.startsWith("\\u")) && (sequence.length() == 6);
/*  54:    */   }
/*  55:    */   
/*  56:    */   private void setAsUnicode(String text)
/*  57:    */   {
/*  58:105 */     int code = Integer.parseInt(text.substring("\\u".length()), 16);
/*  59:106 */     setValue(new Character((char)code));
/*  60:    */   }
/*  61:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.propertyeditors.CharacterEditor
 * JD-Core Version:    0.7.0.1
 */