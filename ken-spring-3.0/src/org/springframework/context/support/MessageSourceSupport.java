/*   1:    */ package org.springframework.context.support;
/*   2:    */ 
/*   3:    */ import java.text.MessageFormat;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Locale;
/*   6:    */ import java.util.Map;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ import org.springframework.util.ObjectUtils;
/*  10:    */ 
/*  11:    */ public abstract class MessageSourceSupport
/*  12:    */ {
/*  13: 43 */   private static final MessageFormat INVALID_MESSAGE_FORMAT = new MessageFormat("");
/*  14: 46 */   protected final Log logger = LogFactory.getLog(getClass());
/*  15: 48 */   private boolean alwaysUseMessageFormat = false;
/*  16: 55 */   private final Map<String, MessageFormat> cachedMessageFormats = new HashMap();
/*  17:    */   
/*  18:    */   public void setAlwaysUseMessageFormat(boolean alwaysUseMessageFormat)
/*  19:    */   {
/*  20: 73 */     this.alwaysUseMessageFormat = alwaysUseMessageFormat;
/*  21:    */   }
/*  22:    */   
/*  23:    */   protected boolean isAlwaysUseMessageFormat()
/*  24:    */   {
/*  25: 81 */     return this.alwaysUseMessageFormat;
/*  26:    */   }
/*  27:    */   
/*  28:    */   protected String renderDefaultMessage(String defaultMessage, Object[] args, Locale locale)
/*  29:    */   {
/*  30:100 */     return formatMessage(defaultMessage, args, locale);
/*  31:    */   }
/*  32:    */   
/*  33:    */   protected String formatMessage(String msg, Object[] args, Locale locale)
/*  34:    */   {
/*  35:114 */     if ((msg == null) || ((!this.alwaysUseMessageFormat) && (ObjectUtils.isEmpty(args)))) {
/*  36:115 */       return msg;
/*  37:    */     }
/*  38:118 */     synchronized (this.cachedMessageFormats)
/*  39:    */     {
/*  40:119 */       MessageFormat messageFormat = (MessageFormat)this.cachedMessageFormats.get(msg);
/*  41:120 */       if (messageFormat == null)
/*  42:    */       {
/*  43:    */         try
/*  44:    */         {
/*  45:122 */           messageFormat = createMessageFormat(msg, locale);
/*  46:    */         }
/*  47:    */         catch (IllegalArgumentException ex)
/*  48:    */         {
/*  49:127 */           if (this.alwaysUseMessageFormat) {
/*  50:128 */             throw ex;
/*  51:    */           }
/*  52:131 */           messageFormat = INVALID_MESSAGE_FORMAT;
/*  53:    */         }
/*  54:133 */         this.cachedMessageFormats.put(msg, messageFormat);
/*  55:    */       }
/*  56:    */     }
/*  57:    */     MessageFormat messageFormat;
/*  58:136 */     if (messageFormat == INVALID_MESSAGE_FORMAT) {
/*  59:137 */       return msg;
/*  60:    */     }
/*  61:139 */     synchronized (messageFormat)
/*  62:    */     {
/*  63:140 */       return messageFormat.format(resolveArguments(args, locale));
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   protected MessageFormat createMessageFormat(String msg, Locale locale)
/*  68:    */   {
/*  69:151 */     return new MessageFormat(msg != null ? msg : "", locale);
/*  70:    */   }
/*  71:    */   
/*  72:    */   protected Object[] resolveArguments(Object[] args, Locale locale)
/*  73:    */   {
/*  74:164 */     return args;
/*  75:    */   }
/*  76:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.MessageSourceSupport
 * JD-Core Version:    0.7.0.1
 */