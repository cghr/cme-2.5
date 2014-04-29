/*   1:    */ package org.springframework.context.support;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import org.springframework.context.MessageSourceResolvable;
/*   5:    */ import org.springframework.util.ObjectUtils;
/*   6:    */ import org.springframework.util.StringUtils;
/*   7:    */ 
/*   8:    */ public class DefaultMessageSourceResolvable
/*   9:    */   implements MessageSourceResolvable, Serializable
/*  10:    */ {
/*  11:    */   private final String[] codes;
/*  12:    */   private final Object[] arguments;
/*  13:    */   private final String defaultMessage;
/*  14:    */   
/*  15:    */   public DefaultMessageSourceResolvable(String code)
/*  16:    */   {
/*  17: 48 */     this(new String[] { code }, null, null);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public DefaultMessageSourceResolvable(String[] codes)
/*  21:    */   {
/*  22: 56 */     this(codes, null, null);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public DefaultMessageSourceResolvable(String[] codes, String defaultMessage)
/*  26:    */   {
/*  27: 65 */     this(codes, null, defaultMessage);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public DefaultMessageSourceResolvable(String[] codes, Object[] arguments)
/*  31:    */   {
/*  32: 74 */     this(codes, arguments, null);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public DefaultMessageSourceResolvable(String[] codes, Object[] arguments, String defaultMessage)
/*  36:    */   {
/*  37: 84 */     this.codes = codes;
/*  38: 85 */     this.arguments = arguments;
/*  39: 86 */     this.defaultMessage = defaultMessage;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public DefaultMessageSourceResolvable(MessageSourceResolvable resolvable)
/*  43:    */   {
/*  44: 94 */     this(resolvable.getCodes(), resolvable.getArguments(), resolvable.getDefaultMessage());
/*  45:    */   }
/*  46:    */   
/*  47:    */   public String[] getCodes()
/*  48:    */   {
/*  49: 99 */     return this.codes;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public String getCode()
/*  53:    */   {
/*  54:107 */     return (this.codes != null) && (this.codes.length > 0) ? this.codes[(this.codes.length - 1)] : null;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public Object[] getArguments()
/*  58:    */   {
/*  59:111 */     return this.arguments;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public String getDefaultMessage()
/*  63:    */   {
/*  64:115 */     return this.defaultMessage;
/*  65:    */   }
/*  66:    */   
/*  67:    */   protected final String resolvableToString()
/*  68:    */   {
/*  69:124 */     StringBuilder result = new StringBuilder();
/*  70:125 */     result.append("codes [").append(StringUtils.arrayToDelimitedString(this.codes, ","));
/*  71:126 */     result.append("]; arguments [" + StringUtils.arrayToDelimitedString(this.arguments, ","));
/*  72:127 */     result.append("]; default message [").append(this.defaultMessage).append(']');
/*  73:128 */     return result.toString();
/*  74:    */   }
/*  75:    */   
/*  76:    */   public String toString()
/*  77:    */   {
/*  78:139 */     return getClass().getName() + ": " + resolvableToString();
/*  79:    */   }
/*  80:    */   
/*  81:    */   public boolean equals(Object other)
/*  82:    */   {
/*  83:145 */     if (this == other) {
/*  84:146 */       return true;
/*  85:    */     }
/*  86:148 */     if (!(other instanceof MessageSourceResolvable)) {
/*  87:149 */       return false;
/*  88:    */     }
/*  89:151 */     MessageSourceResolvable otherResolvable = (MessageSourceResolvable)other;
/*  90:    */     
/*  91:    */ 
/*  92:154 */     return (ObjectUtils.nullSafeEquals(getCodes(), otherResolvable.getCodes())) && (ObjectUtils.nullSafeEquals(getArguments(), otherResolvable.getArguments())) && (ObjectUtils.nullSafeEquals(getDefaultMessage(), otherResolvable.getDefaultMessage()));
/*  93:    */   }
/*  94:    */   
/*  95:    */   public int hashCode()
/*  96:    */   {
/*  97:159 */     int hashCode = ObjectUtils.nullSafeHashCode(getCodes());
/*  98:160 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(getArguments());
/*  99:161 */     hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(getDefaultMessage());
/* 100:162 */     return hashCode;
/* 101:    */   }
/* 102:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.DefaultMessageSourceResolvable
 * JD-Core Version:    0.7.0.1
 */