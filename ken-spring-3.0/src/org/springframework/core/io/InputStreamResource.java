/*   1:    */ package org.springframework.core.io;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ 
/*   6:    */ public class InputStreamResource
/*   7:    */   extends AbstractResource
/*   8:    */ {
/*   9:    */   private final InputStream inputStream;
/*  10:    */   private final String description;
/*  11: 46 */   private boolean read = false;
/*  12:    */   
/*  13:    */   public InputStreamResource(InputStream inputStream)
/*  14:    */   {
/*  15: 54 */     this(inputStream, "resource loaded through InputStream");
/*  16:    */   }
/*  17:    */   
/*  18:    */   public InputStreamResource(InputStream inputStream, String description)
/*  19:    */   {
/*  20: 63 */     if (inputStream == null) {
/*  21: 64 */       throw new IllegalArgumentException("InputStream must not be null");
/*  22:    */     }
/*  23: 66 */     this.inputStream = inputStream;
/*  24: 67 */     this.description = (description != null ? description : "");
/*  25:    */   }
/*  26:    */   
/*  27:    */   public boolean exists()
/*  28:    */   {
/*  29: 76 */     return true;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public boolean isOpen()
/*  33:    */   {
/*  34: 84 */     return true;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public InputStream getInputStream()
/*  38:    */     throws IOException, IllegalStateException
/*  39:    */   {
/*  40: 92 */     if (this.read) {
/*  41: 93 */       throw new IllegalStateException("InputStream has already been read - do not use InputStreamResource if a stream needs to be read multiple times");
/*  42:    */     }
/*  43: 96 */     this.read = true;
/*  44: 97 */     return this.inputStream;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public String getDescription()
/*  48:    */   {
/*  49:104 */     return this.description;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public boolean equals(Object obj)
/*  53:    */   {
/*  54:114 */     return (obj == this) || (((obj instanceof InputStreamResource)) && (((InputStreamResource)obj).inputStream.equals(this.inputStream)));
/*  55:    */   }
/*  56:    */   
/*  57:    */   public int hashCode()
/*  58:    */   {
/*  59:122 */     return this.inputStream.hashCode();
/*  60:    */   }
/*  61:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.InputStreamResource
 * JD-Core Version:    0.7.0.1
 */