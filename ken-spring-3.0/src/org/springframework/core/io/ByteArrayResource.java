/*   1:    */ package org.springframework.core.io;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.util.Arrays;
/*   7:    */ 
/*   8:    */ public class ByteArrayResource
/*   9:    */   extends AbstractResource
/*  10:    */ {
/*  11:    */   private final byte[] byteArray;
/*  12:    */   private final String description;
/*  13:    */   
/*  14:    */   public ByteArrayResource(byte[] byteArray)
/*  15:    */   {
/*  16: 51 */     this(byteArray, "resource loaded from byte array");
/*  17:    */   }
/*  18:    */   
/*  19:    */   public ByteArrayResource(byte[] byteArray, String description)
/*  20:    */   {
/*  21: 60 */     if (byteArray == null) {
/*  22: 61 */       throw new IllegalArgumentException("Byte array must not be null");
/*  23:    */     }
/*  24: 63 */     this.byteArray = byteArray;
/*  25: 64 */     this.description = (description != null ? description : "");
/*  26:    */   }
/*  27:    */   
/*  28:    */   public final byte[] getByteArray()
/*  29:    */   {
/*  30: 71 */     return this.byteArray;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public boolean exists()
/*  34:    */   {
/*  35: 80 */     return true;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public InputStream getInputStream()
/*  39:    */     throws IOException
/*  40:    */   {
/*  41: 89 */     return new ByteArrayInputStream(this.byteArray);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public String getDescription()
/*  45:    */   {
/*  46: 96 */     return this.description;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public boolean equals(Object obj)
/*  50:    */   {
/*  51:107 */     return (obj == this) || (((obj instanceof ByteArrayResource)) && (Arrays.equals(((ByteArrayResource)obj).byteArray, this.byteArray)));
/*  52:    */   }
/*  53:    */   
/*  54:    */   public int hashCode()
/*  55:    */   {
/*  56:116 */     return [B.class.hashCode() * 29 * this.byteArray.length;
/*  57:    */   }
/*  58:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.ByteArrayResource
 * JD-Core Version:    0.7.0.1
 */