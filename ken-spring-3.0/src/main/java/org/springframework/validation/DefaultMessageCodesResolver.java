/*   1:    */ package org.springframework.validation;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.List;
/*   6:    */ import org.springframework.util.StringUtils;
/*   7:    */ 
/*   8:    */ public class DefaultMessageCodesResolver
/*   9:    */   implements MessageCodesResolver, Serializable
/*  10:    */ {
/*  11:    */   public static final String CODE_SEPARATOR = ".";
/*  12: 87 */   private String prefix = "";
/*  13:    */   
/*  14:    */   public void setPrefix(String prefix)
/*  15:    */   {
/*  16: 96 */     this.prefix = (prefix != null ? prefix : "");
/*  17:    */   }
/*  18:    */   
/*  19:    */   protected String getPrefix()
/*  20:    */   {
/*  21:104 */     return this.prefix;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public String[] resolveMessageCodes(String errorCode, String objectName)
/*  25:    */   {
/*  26:109 */     return new String[] {
/*  27:110 */       postProcessMessageCode(errorCode + "." + objectName), 
/*  28:111 */       postProcessMessageCode(errorCode) };
/*  29:    */   }
/*  30:    */   
/*  31:    */   public String[] resolveMessageCodes(String errorCode, String objectName, String field, Class<?> fieldType)
/*  32:    */   {
/*  33:124 */     List<String> codeList = new ArrayList();
/*  34:125 */     List<String> fieldList = new ArrayList();
/*  35:126 */     buildFieldList(field, fieldList);
/*  36:127 */     for (String fieldInList : fieldList) {
/*  37:128 */       codeList.add(postProcessMessageCode(errorCode + "." + objectName + "." + fieldInList));
/*  38:    */     }
/*  39:130 */     int dotIndex = field.lastIndexOf('.');
/*  40:131 */     if (dotIndex != -1) {
/*  41:132 */       buildFieldList(field.substring(dotIndex + 1), fieldList);
/*  42:    */     }
/*  43:134 */     for (String fieldInList : fieldList) {
/*  44:135 */       codeList.add(postProcessMessageCode(errorCode + "." + fieldInList));
/*  45:    */     }
/*  46:137 */     if (fieldType != null) {
/*  47:138 */       codeList.add(postProcessMessageCode(errorCode + "." + fieldType.getName()));
/*  48:    */     }
/*  49:140 */     codeList.add(postProcessMessageCode(errorCode));
/*  50:141 */     return StringUtils.toStringArray(codeList);
/*  51:    */   }
/*  52:    */   
/*  53:    */   protected void buildFieldList(String field, List<String> fieldList)
/*  54:    */   {
/*  55:149 */     fieldList.add(field);
/*  56:150 */     String plainField = field;
/*  57:151 */     int keyIndex = plainField.lastIndexOf('[');
/*  58:152 */     while (keyIndex != -1)
/*  59:    */     {
/*  60:153 */       int endKeyIndex = plainField.indexOf(']', keyIndex);
/*  61:154 */       if (endKeyIndex != -1)
/*  62:    */       {
/*  63:155 */         plainField = plainField.substring(0, keyIndex) + plainField.substring(endKeyIndex + 1);
/*  64:156 */         fieldList.add(plainField);
/*  65:157 */         keyIndex = plainField.lastIndexOf('[');
/*  66:    */       }
/*  67:    */       else
/*  68:    */       {
/*  69:160 */         keyIndex = -1;
/*  70:    */       }
/*  71:    */     }
/*  72:    */   }
/*  73:    */   
/*  74:    */   protected String postProcessMessageCode(String code)
/*  75:    */   {
/*  76:173 */     return getPrefix() + code;
/*  77:    */   }
/*  78:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.validation.DefaultMessageCodesResolver
 * JD-Core Version:    0.7.0.1
 */