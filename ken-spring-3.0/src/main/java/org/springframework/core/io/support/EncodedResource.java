/*   1:    */ package org.springframework.core.io.support;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStreamReader;
/*   5:    */ import java.io.Reader;
/*   6:    */ import org.springframework.core.io.Resource;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ import org.springframework.util.ObjectUtils;
/*   9:    */ 
/*  10:    */ public class EncodedResource
/*  11:    */ {
/*  12:    */   private final Resource resource;
/*  13:    */   private final String encoding;
/*  14:    */   
/*  15:    */   public EncodedResource(Resource resource)
/*  16:    */   {
/*  17: 51 */     this(resource, null);
/*  18:    */   }
/*  19:    */   
/*  20:    */   public EncodedResource(Resource resource, String encoding)
/*  21:    */   {
/*  22: 61 */     Assert.notNull(resource, "Resource must not be null");
/*  23: 62 */     this.resource = resource;
/*  24: 63 */     this.encoding = encoding;
/*  25:    */   }
/*  26:    */   
/*  27:    */   public final Resource getResource()
/*  28:    */   {
/*  29: 71 */     return this.resource;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public final String getEncoding()
/*  33:    */   {
/*  34: 79 */     return this.encoding;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public Reader getReader()
/*  38:    */     throws IOException
/*  39:    */   {
/*  40: 88 */     if (this.encoding != null) {
/*  41: 89 */       return new InputStreamReader(this.resource.getInputStream(), this.encoding);
/*  42:    */     }
/*  43: 92 */     return new InputStreamReader(this.resource.getInputStream());
/*  44:    */   }
/*  45:    */   
/*  46:    */   public boolean equals(Object obj)
/*  47:    */   {
/*  48: 99 */     if (obj == this) {
/*  49:100 */       return true;
/*  50:    */     }
/*  51:102 */     if ((obj instanceof EncodedResource))
/*  52:    */     {
/*  53:103 */       EncodedResource otherRes = (EncodedResource)obj;
/*  54:    */       
/*  55:105 */       return (this.resource.equals(otherRes.resource)) && (ObjectUtils.nullSafeEquals(this.encoding, otherRes.encoding));
/*  56:    */     }
/*  57:107 */     return false;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public int hashCode()
/*  61:    */   {
/*  62:112 */     return this.resource.hashCode();
/*  63:    */   }
/*  64:    */   
/*  65:    */   public String toString()
/*  66:    */   {
/*  67:117 */     return this.resource.toString();
/*  68:    */   }
/*  69:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.io.support.EncodedResource
 * JD-Core Version:    0.7.0.1
 */