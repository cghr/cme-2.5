/*  1:   */ package org.springframework.format.datetime.joda;
/*  2:   */ 
/*  3:   */ import java.util.Locale;
/*  4:   */ import org.joda.time.format.DateTimeFormatter;
/*  5:   */ import org.springframework.core.NamedThreadLocal;
/*  6:   */ 
/*  7:   */ public final class JodaTimeContextHolder
/*  8:   */ {
/*  9:35 */   private static final ThreadLocal<JodaTimeContext> jodaTimeContextHolder = new NamedThreadLocal("JodaTime Context");
/* 10:   */   
/* 11:   */   public static void resetJodaTimeContext()
/* 12:   */   {
/* 13:42 */     jodaTimeContextHolder.remove();
/* 14:   */   }
/* 15:   */   
/* 16:   */   public static void setJodaTimeContext(JodaTimeContext jodaTimeContext)
/* 17:   */   {
/* 18:51 */     if (jodaTimeContext == null) {
/* 19:52 */       resetJodaTimeContext();
/* 20:   */     } else {
/* 21:55 */       jodaTimeContextHolder.set(jodaTimeContext);
/* 22:   */     }
/* 23:   */   }
/* 24:   */   
/* 25:   */   public static JodaTimeContext getJodaTimeContext()
/* 26:   */   {
/* 27:64 */     return (JodaTimeContext)jodaTimeContextHolder.get();
/* 28:   */   }
/* 29:   */   
/* 30:   */   public static DateTimeFormatter getFormatter(DateTimeFormatter formatter, Locale locale)
/* 31:   */   {
/* 32:76 */     DateTimeFormatter formatterToUse = locale != null ? formatter.withLocale(locale) : formatter;
/* 33:77 */     JodaTimeContext context = getJodaTimeContext();
/* 34:78 */     return context != null ? context.getFormatter(formatterToUse) : formatterToUse;
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.datetime.joda.JodaTimeContextHolder
 * JD-Core Version:    0.7.0.1
 */