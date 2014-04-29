/*  1:   */ package org.springframework.web.multipart;
/*  2:   */ 
/*  3:   */ public class MaxUploadSizeExceededException
/*  4:   */   extends MultipartException
/*  5:   */ {
/*  6:   */   private final long maxUploadSize;
/*  7:   */   
/*  8:   */   public MaxUploadSizeExceededException(long maxUploadSize)
/*  9:   */   {
/* 10:36 */     this(maxUploadSize, null);
/* 11:   */   }
/* 12:   */   
/* 13:   */   public MaxUploadSizeExceededException(long maxUploadSize, Throwable ex)
/* 14:   */   {
/* 15:45 */     super("Maximum upload size of " + maxUploadSize + " bytes exceeded", ex);
/* 16:46 */     this.maxUploadSize = maxUploadSize;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public long getMaxUploadSize()
/* 20:   */   {
/* 21:54 */     return this.maxUploadSize;
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.multipart.MaxUploadSizeExceededException
 * JD-Core Version:    0.7.0.1
 */