/*  1:   */ package org.springframework.core.io;
/*  2:   */ 
/*  3:   */ import java.io.FileNotFoundException;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.InputStream;
/*  6:   */ 
/*  7:   */ public class DescriptiveResource
/*  8:   */   extends AbstractResource
/*  9:   */ {
/* 10:   */   private final String description;
/* 11:   */   
/* 12:   */   public DescriptiveResource(String description)
/* 13:   */   {
/* 14:43 */     this.description = (description != null ? description : "");
/* 15:   */   }
/* 16:   */   
/* 17:   */   public boolean exists()
/* 18:   */   {
/* 19:49 */     return false;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public boolean isReadable()
/* 23:   */   {
/* 24:54 */     return false;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public InputStream getInputStream()
/* 28:   */     throws IOException
/* 29:   */   {
/* 30:58 */     throw new FileNotFoundException(
/* 31:59 */       getDescription() + " cannot be opened because it does not point to a readable resource");
/* 32:   */   }
/* 33:   */   
/* 34:   */   public String getDescription()
/* 35:   */   {
/* 36:63 */     return this.description;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public boolean equals(Object obj)
/* 40:   */   {
/* 41:73 */     return (obj == this) || (((obj instanceof DescriptiveResource)) && (((DescriptiveResource)obj).description.equals(this.description)));
/* 42:   */   }
/* 43:   */   
/* 44:   */   public int hashCode()
/* 45:   */   {
/* 46:81 */     return this.description.hashCode();
/* 47:   */   }
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.DescriptiveResource
 * JD-Core Version:    0.7.0.1
 */