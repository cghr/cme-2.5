/*  1:   */ package org.springframework.context.support;
/*  2:   */ 
/*  3:   */ import java.text.MessageFormat;
/*  4:   */ import java.util.HashMap;
/*  5:   */ import java.util.Locale;
/*  6:   */ import java.util.Map;
/*  7:   */ import java.util.Map.Entry;
/*  8:   */ import org.apache.commons.logging.Log;
/*  9:   */ import org.springframework.util.Assert;
/* 10:   */ 
/* 11:   */ public class StaticMessageSource
/* 12:   */   extends AbstractMessageSource
/* 13:   */ {
/* 14:39 */   private final Map<String, String> messages = new HashMap();
/* 15:41 */   private final Map<String, MessageFormat> cachedMessageFormats = new HashMap();
/* 16:   */   
/* 17:   */   protected String resolveCodeWithoutArguments(String code, Locale locale)
/* 18:   */   {
/* 19:46 */     return (String)this.messages.get(code + "_" + locale.toString());
/* 20:   */   }
/* 21:   */   
/* 22:   */   protected MessageFormat resolveCode(String code, Locale locale)
/* 23:   */   {
/* 24:51 */     String key = code + "_" + locale.toString();
/* 25:52 */     String msg = (String)this.messages.get(key);
/* 26:53 */     if (msg == null) {
/* 27:54 */       return null;
/* 28:   */     }
/* 29:56 */     synchronized (this.cachedMessageFormats)
/* 30:   */     {
/* 31:57 */       MessageFormat messageFormat = (MessageFormat)this.cachedMessageFormats.get(key);
/* 32:58 */       if (messageFormat == null)
/* 33:   */       {
/* 34:59 */         messageFormat = createMessageFormat(msg, locale);
/* 35:60 */         this.cachedMessageFormats.put(key, messageFormat);
/* 36:   */       }
/* 37:62 */       return messageFormat;
/* 38:   */     }
/* 39:   */   }
/* 40:   */   
/* 41:   */   public void addMessage(String code, Locale locale, String msg)
/* 42:   */   {
/* 43:73 */     Assert.notNull(code, "Code must not be null");
/* 44:74 */     Assert.notNull(locale, "Locale must not be null");
/* 45:75 */     Assert.notNull(msg, "Message must not be null");
/* 46:76 */     this.messages.put(code + "_" + locale.toString(), msg);
/* 47:77 */     if (this.logger.isDebugEnabled()) {
/* 48:78 */       this.logger.debug("Added message [" + msg + "] for code [" + code + "] and Locale [" + locale + "]");
/* 49:   */     }
/* 50:   */   }
/* 51:   */   
/* 52:   */   public void addMessages(Map<String, String> messages, Locale locale)
/* 53:   */   {
/* 54:89 */     Assert.notNull(messages, "Messages Map must not be null");
/* 55:90 */     for (Map.Entry<String, String> entry : messages.entrySet()) {
/* 56:91 */       addMessage((String)entry.getKey(), locale, (String)entry.getValue());
/* 57:   */     }
/* 58:   */   }
/* 59:   */   
/* 60:   */   public String toString()
/* 61:   */   {
/* 62:98 */     return getClass().getName() + ": " + this.messages;
/* 63:   */   }
/* 64:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.StaticMessageSource
 * JD-Core Version:    0.7.0.1
 */