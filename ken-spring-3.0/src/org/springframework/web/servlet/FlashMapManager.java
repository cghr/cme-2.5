/*  1:   */ package org.springframework.web.servlet;
/*  2:   */ 
/*  3:   */ import javax.servlet.http.HttpServletRequest;
/*  4:   */ 
/*  5:   */ public abstract interface FlashMapManager
/*  6:   */ {
/*  7:47 */   public static final String INPUT_FLASH_MAP_ATTRIBUTE = FlashMapManager.class.getName() + ".INPUT_FLASH_MAP";
/*  8:54 */   public static final String OUTPUT_FLASH_MAP_ATTRIBUTE = FlashMapManager.class.getName() + ".OUTPUT_FLASH_MAP";
/*  9:   */   
/* 10:   */   public abstract void requestStarted(HttpServletRequest paramHttpServletRequest);
/* 11:   */   
/* 12:   */   public abstract void requestCompleted(HttpServletRequest paramHttpServletRequest);
/* 13:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.FlashMapManager
 * JD-Core Version:    0.7.0.1
 */