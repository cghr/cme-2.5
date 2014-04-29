/*   1:    */ package org.springframework.context.support;
/*   2:    */ 
/*   3:    */ import java.util.Locale;
/*   4:    */ import org.springframework.context.MessageSource;
/*   5:    */ import org.springframework.context.MessageSourceResolvable;
/*   6:    */ import org.springframework.context.NoSuchMessageException;
/*   7:    */ import org.springframework.context.i18n.LocaleContextHolder;
/*   8:    */ 
/*   9:    */ public class MessageSourceAccessor
/*  10:    */ {
/*  11:    */   private final MessageSource messageSource;
/*  12:    */   private final Locale defaultLocale;
/*  13:    */   
/*  14:    */   public MessageSourceAccessor(MessageSource messageSource)
/*  15:    */   {
/*  16: 50 */     this.messageSource = messageSource;
/*  17: 51 */     this.defaultLocale = null;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public MessageSourceAccessor(MessageSource messageSource, Locale defaultLocale)
/*  21:    */   {
/*  22: 60 */     this.messageSource = messageSource;
/*  23: 61 */     this.defaultLocale = defaultLocale;
/*  24:    */   }
/*  25:    */   
/*  26:    */   protected Locale getDefaultLocale()
/*  27:    */   {
/*  28: 73 */     return this.defaultLocale != null ? this.defaultLocale : LocaleContextHolder.getLocale();
/*  29:    */   }
/*  30:    */   
/*  31:    */   public String getMessage(String code, String defaultMessage)
/*  32:    */   {
/*  33: 83 */     return this.messageSource.getMessage(code, null, defaultMessage, getDefaultLocale());
/*  34:    */   }
/*  35:    */   
/*  36:    */   public String getMessage(String code, String defaultMessage, Locale locale)
/*  37:    */   {
/*  38: 94 */     return this.messageSource.getMessage(code, null, defaultMessage, locale);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public String getMessage(String code, Object[] args, String defaultMessage)
/*  42:    */   {
/*  43:105 */     return this.messageSource.getMessage(code, args, defaultMessage, getDefaultLocale());
/*  44:    */   }
/*  45:    */   
/*  46:    */   public String getMessage(String code, Object[] args, String defaultMessage, Locale locale)
/*  47:    */   {
/*  48:117 */     return this.messageSource.getMessage(code, args, defaultMessage, locale);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public String getMessage(String code)
/*  52:    */     throws NoSuchMessageException
/*  53:    */   {
/*  54:127 */     return this.messageSource.getMessage(code, null, getDefaultLocale());
/*  55:    */   }
/*  56:    */   
/*  57:    */   public String getMessage(String code, Locale locale)
/*  58:    */     throws NoSuchMessageException
/*  59:    */   {
/*  60:138 */     return this.messageSource.getMessage(code, null, locale);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public String getMessage(String code, Object[] args)
/*  64:    */     throws NoSuchMessageException
/*  65:    */   {
/*  66:149 */     return this.messageSource.getMessage(code, args, getDefaultLocale());
/*  67:    */   }
/*  68:    */   
/*  69:    */   public String getMessage(String code, Object[] args, Locale locale)
/*  70:    */     throws NoSuchMessageException
/*  71:    */   {
/*  72:161 */     return this.messageSource.getMessage(code, args, locale);
/*  73:    */   }
/*  74:    */   
/*  75:    */   public String getMessage(MessageSourceResolvable resolvable)
/*  76:    */     throws NoSuchMessageException
/*  77:    */   {
/*  78:172 */     return this.messageSource.getMessage(resolvable, getDefaultLocale());
/*  79:    */   }
/*  80:    */   
/*  81:    */   public String getMessage(MessageSourceResolvable resolvable, Locale locale)
/*  82:    */     throws NoSuchMessageException
/*  83:    */   {
/*  84:184 */     return this.messageSource.getMessage(resolvable, locale);
/*  85:    */   }
/*  86:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.MessageSourceAccessor
 * JD-Core Version:    0.7.0.1
 */