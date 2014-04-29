/*  1:   */ package org.springframework.ui.context.support;
/*  2:   */ 
/*  3:   */ import org.springframework.context.MessageSource;
/*  4:   */ import org.springframework.ui.context.Theme;
/*  5:   */ import org.springframework.util.Assert;
/*  6:   */ 
/*  7:   */ public class SimpleTheme
/*  8:   */   implements Theme
/*  9:   */ {
/* 10:   */   private final String name;
/* 11:   */   private final MessageSource messageSource;
/* 12:   */   
/* 13:   */   public SimpleTheme(String name, MessageSource messageSource)
/* 14:   */   {
/* 15:43 */     Assert.notNull(name, "Name must not be null");
/* 16:44 */     Assert.notNull(messageSource, "MessageSource must not be null");
/* 17:45 */     this.name = name;
/* 18:46 */     this.messageSource = messageSource;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public final String getName()
/* 22:   */   {
/* 23:51 */     return this.name;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public final MessageSource getMessageSource()
/* 27:   */   {
/* 28:55 */     return this.messageSource;
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ui.context.support.SimpleTheme
 * JD-Core Version:    0.7.0.1
 */