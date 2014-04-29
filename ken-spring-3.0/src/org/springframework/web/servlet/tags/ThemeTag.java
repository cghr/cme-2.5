/*  1:   */ package org.springframework.web.servlet.tags;
/*  2:   */ 
/*  3:   */ import org.springframework.context.MessageSource;
/*  4:   */ import org.springframework.context.NoSuchMessageException;
/*  5:   */ import org.springframework.ui.context.Theme;
/*  6:   */ import org.springframework.web.servlet.support.RequestContext;
/*  7:   */ 
/*  8:   */ public class ThemeTag
/*  9:   */   extends MessageTag
/* 10:   */ {
/* 11:   */   protected MessageSource getMessageSource()
/* 12:   */   {
/* 13:50 */     return getRequestContext().getTheme().getMessageSource();
/* 14:   */   }
/* 15:   */   
/* 16:   */   protected String getNoSuchMessageExceptionDescription(NoSuchMessageException ex)
/* 17:   */   {
/* 18:58 */     return "Theme '" + getRequestContext().getTheme().getName() + "': " + ex.getMessage();
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.tags.ThemeTag
 * JD-Core Version:    0.7.0.1
 */