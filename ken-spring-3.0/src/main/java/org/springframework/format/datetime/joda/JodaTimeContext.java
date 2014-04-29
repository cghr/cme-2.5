/*  1:   */ package org.springframework.format.datetime.joda;
/*  2:   */ 
/*  3:   */ import org.joda.time.Chronology;
/*  4:   */ import org.joda.time.DateTimeZone;
/*  5:   */ import org.joda.time.format.DateTimeFormatter;
/*  6:   */ 
/*  7:   */ public class JodaTimeContext
/*  8:   */ {
/*  9:   */   private Chronology chronology;
/* 10:   */   private DateTimeZone timeZone;
/* 11:   */   
/* 12:   */   public void setChronology(Chronology chronology)
/* 13:   */   {
/* 14:42 */     this.chronology = chronology;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public Chronology getChronology()
/* 18:   */   {
/* 19:50 */     return this.chronology;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void setTimeZone(DateTimeZone timeZone)
/* 23:   */   {
/* 24:57 */     this.timeZone = timeZone;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public DateTimeZone getTimeZone()
/* 28:   */   {
/* 29:65 */     return this.timeZone;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public DateTimeFormatter getFormatter(DateTimeFormatter formatter)
/* 33:   */   {
/* 34:75 */     if (this.chronology != null) {
/* 35:76 */       formatter = formatter.withChronology(this.chronology);
/* 36:   */     }
/* 37:78 */     if (this.timeZone != null) {
/* 38:79 */       formatter = formatter.withZone(this.timeZone);
/* 39:   */     }
/* 40:81 */     return formatter;
/* 41:   */   }
/* 42:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.datetime.joda.JodaTimeContext
 * JD-Core Version:    0.7.0.1
 */