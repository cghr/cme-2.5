/*   1:    */ package org.springframework.util.xml;
/*   2:    */ 
/*   3:    */ import java.io.BufferedReader;
/*   4:    */ import java.io.CharConversionException;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.io.InputStream;
/*   7:    */ import java.io.InputStreamReader;
/*   8:    */ import org.springframework.util.StringUtils;
/*   9:    */ 
/*  10:    */ public class XmlValidationModeDetector
/*  11:    */ {
/*  12:    */   public static final int VALIDATION_NONE = 0;
/*  13:    */   public static final int VALIDATION_AUTO = 1;
/*  14:    */   public static final int VALIDATION_DTD = 2;
/*  15:    */   public static final int VALIDATION_XSD = 3;
/*  16:    */   private static final String DOCTYPE = "DOCTYPE";
/*  17:    */   private static final String START_COMMENT = "<!--";
/*  18:    */   private static final String END_COMMENT = "-->";
/*  19:    */   private boolean inComment;
/*  20:    */   
/*  21:    */   public int detectValidationMode(InputStream inputStream)
/*  22:    */     throws IOException
/*  23:    */   {
/*  24: 91 */     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
/*  25:    */     try
/*  26:    */     {
/*  27: 93 */       boolean isDtdValidated = false;
/*  28:    */       String content;
/*  29: 95 */       while ((content = reader.readLine()) != null)
/*  30:    */       {
/*  31: 96 */         String content = consumeCommentTokens(content);
/*  32: 97 */         if ((!this.inComment) && (StringUtils.hasText(content))) {
/*  33:100 */           if (hasDoctype(content)) {
/*  34:101 */             isDtdValidated = true;
/*  35:    */           } else {
/*  36:104 */             if (hasOpeningTag(content)) {
/*  37:    */               break;
/*  38:    */             }
/*  39:    */           }
/*  40:    */         }
/*  41:    */       }
/*  42:109 */       return isDtdValidated ? 2 : 3;
/*  43:    */     }
/*  44:    */     catch (CharConversionException localCharConversionException)
/*  45:    */     {
/*  46:114 */       return 1;
/*  47:    */     }
/*  48:    */     finally
/*  49:    */     {
/*  50:117 */       reader.close();
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   private boolean hasDoctype(String content)
/*  55:    */   {
/*  56:126 */     return content.indexOf("DOCTYPE") > -1;
/*  57:    */   }
/*  58:    */   
/*  59:    */   private boolean hasOpeningTag(String content)
/*  60:    */   {
/*  61:135 */     if (this.inComment) {
/*  62:136 */       return false;
/*  63:    */     }
/*  64:138 */     int openTagIndex = content.indexOf('<');
/*  65:139 */     return (openTagIndex > -1) && (content.length() > openTagIndex) && (Character.isLetter(content.charAt(openTagIndex + 1)));
/*  66:    */   }
/*  67:    */   
/*  68:    */   private String consumeCommentTokens(String line)
/*  69:    */   {
/*  70:149 */     if ((line.indexOf("<!--") == -1) && (line.indexOf("-->") == -1)) {
/*  71:150 */       return line;
/*  72:    */     }
/*  73:152 */     while ((line = consume(line)) != null) {
/*  74:153 */       if ((!this.inComment) && (!line.trim().startsWith("<!--"))) {
/*  75:154 */         return line;
/*  76:    */       }
/*  77:    */     }
/*  78:157 */     return line;
/*  79:    */   }
/*  80:    */   
/*  81:    */   private String consume(String line)
/*  82:    */   {
/*  83:165 */     int index = this.inComment ? endComment(line) : startComment(line);
/*  84:166 */     return index == -1 ? null : line.substring(index);
/*  85:    */   }
/*  86:    */   
/*  87:    */   private int startComment(String line)
/*  88:    */   {
/*  89:174 */     return commentToken(line, "<!--", true);
/*  90:    */   }
/*  91:    */   
/*  92:    */   private int endComment(String line)
/*  93:    */   {
/*  94:178 */     return commentToken(line, "-->", false);
/*  95:    */   }
/*  96:    */   
/*  97:    */   private int commentToken(String line, String token, boolean inCommentIfPresent)
/*  98:    */   {
/*  99:187 */     int index = line.indexOf(token);
/* 100:188 */     if (index > -1) {
/* 101:189 */       this.inComment = inCommentIfPresent;
/* 102:    */     }
/* 103:191 */     return index == -1 ? index : index + token.length();
/* 104:    */   }
/* 105:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.xml.XmlValidationModeDetector
 * JD-Core Version:    0.7.0.1
 */