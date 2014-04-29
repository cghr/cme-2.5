/*   1:    */ package org.springframework.web.util;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.util.Enumeration;
/*   6:    */ import java.util.HashMap;
/*   7:    */ import java.util.Map;
/*   8:    */ import java.util.Properties;
/*   9:    */ import org.springframework.util.Assert;
/*  10:    */ 
/*  11:    */ class HtmlCharacterEntityReferences
/*  12:    */ {
/*  13:    */   private static final String PROPERTIES_FILE = "HtmlCharacterEntityReferences.properties";
/*  14:    */   static final char REFERENCE_START = '&';
/*  15:    */   static final String DECIMAL_REFERENCE_START = "&#";
/*  16:    */   static final String HEX_REFERENCE_START = "&#x";
/*  17:    */   static final char REFERENCE_END = ';';
/*  18:    */   static final char CHAR_NULL = 'èøø';
/*  19: 54 */   private final String[] characterToEntityReferenceMap = new String[3000];
/*  20: 56 */   private final Map<String, Character> entityReferenceToCharacterMap = new HashMap(252);
/*  21:    */   
/*  22:    */   public HtmlCharacterEntityReferences()
/*  23:    */   {
/*  24: 63 */     Properties entityReferences = new Properties();
/*  25:    */     
/*  26:    */ 
/*  27: 66 */     InputStream is = HtmlCharacterEntityReferences.class.getResourceAsStream("HtmlCharacterEntityReferences.properties");
/*  28: 67 */     if (is == null) {
/*  29: 68 */       throw new IllegalStateException(
/*  30: 69 */         "Cannot find reference definition file [HtmlCharacterEntityReferences.properties] as class path resource");
/*  31:    */     }
/*  32:    */     try
/*  33:    */     {
/*  34:    */       try
/*  35:    */       {
/*  36: 73 */         entityReferences.load(is);
/*  37:    */       }
/*  38:    */       finally
/*  39:    */       {
/*  40: 76 */         is.close();
/*  41:    */       }
/*  42:    */     }
/*  43:    */     catch (IOException ex)
/*  44:    */     {
/*  45: 80 */       throw new IllegalStateException(
/*  46: 81 */         "Failed to parse reference definition file [HtmlCharacterEntityReferences.properties]: " + ex.getMessage());
/*  47:    */     }
/*  48: 85 */     Enumeration keys = entityReferences.propertyNames();
/*  49: 86 */     while (keys.hasMoreElements())
/*  50:    */     {
/*  51: 87 */       String key = (String)keys.nextElement();
/*  52: 88 */       int referredChar = Integer.parseInt(key);
/*  53: 89 */       Assert.isTrue((referredChar < 1000) || ((referredChar >= 8000) && (referredChar < 10000)), 
/*  54: 90 */         "Invalid reference to special HTML entity: " + referredChar);
/*  55: 91 */       int index = referredChar < 1000 ? referredChar : referredChar - 7000;
/*  56: 92 */       String reference = entityReferences.getProperty(key);
/*  57: 93 */       this.characterToEntityReferenceMap[index] = ('&' + reference + ';');
/*  58: 94 */       this.entityReferenceToCharacterMap.put(reference, new Character((char)referredChar));
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   public int getSupportedReferenceCount()
/*  63:    */   {
/*  64:103 */     return this.entityReferenceToCharacterMap.size();
/*  65:    */   }
/*  66:    */   
/*  67:    */   public boolean isMappedToReference(char character)
/*  68:    */   {
/*  69:110 */     return convertToReference(character) != null;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public String convertToReference(char character)
/*  73:    */   {
/*  74:117 */     if ((character < 'œ®') || ((character >= '·ΩÄ') && (character < '‚úê')))
/*  75:    */     {
/*  76:118 */       int index = character < 'œ®' ? character : character - '·≠ò';
/*  77:119 */       String entityReference = this.characterToEntityReferenceMap[index];
/*  78:120 */       if (entityReference != null) {
/*  79:121 */         return entityReference;
/*  80:    */       }
/*  81:    */     }
/*  82:124 */     return null;
/*  83:    */   }
/*  84:    */   
/*  85:    */   public char convertToCharacter(String entityReference)
/*  86:    */   {
/*  87:131 */     Character referredCharacter = (Character)this.entityReferenceToCharacterMap.get(entityReference);
/*  88:132 */     if (referredCharacter != null) {
/*  89:133 */       return referredCharacter.charValue();
/*  90:    */     }
/*  91:135 */     return 65535;
/*  92:    */   }
/*  93:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.util.HtmlCharacterEntityReferences
 * JD-Core Version:    0.7.0.1
 */