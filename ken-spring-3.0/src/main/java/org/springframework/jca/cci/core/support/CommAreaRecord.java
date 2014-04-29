/*  1:   */ package org.springframework.jca.cci.core.support;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.io.InputStream;
/*  5:   */ import java.io.OutputStream;
/*  6:   */ import javax.resource.cci.Record;
/*  7:   */ import javax.resource.cci.Streamable;
/*  8:   */ import org.springframework.util.FileCopyUtils;
/*  9:   */ 
/* 10:   */ public class CommAreaRecord
/* 11:   */   implements Record, Streamable
/* 12:   */ {
/* 13:   */   private byte[] bytes;
/* 14:   */   private String recordName;
/* 15:   */   private String recordShortDescription;
/* 16:   */   
/* 17:   */   public CommAreaRecord() {}
/* 18:   */   
/* 19:   */   public CommAreaRecord(byte[] bytes)
/* 20:   */   {
/* 21:57 */     this.bytes = bytes;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void setRecordName(String recordName)
/* 25:   */   {
/* 26:62 */     this.recordName = recordName;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public String getRecordName()
/* 30:   */   {
/* 31:66 */     return this.recordName;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public void setRecordShortDescription(String recordShortDescription)
/* 35:   */   {
/* 36:70 */     this.recordShortDescription = recordShortDescription;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public String getRecordShortDescription()
/* 40:   */   {
/* 41:74 */     return this.recordShortDescription;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public void read(InputStream in)
/* 45:   */     throws IOException
/* 46:   */   {
/* 47:79 */     this.bytes = FileCopyUtils.copyToByteArray(in);
/* 48:   */   }
/* 49:   */   
/* 50:   */   public void write(OutputStream out)
/* 51:   */     throws IOException
/* 52:   */   {
/* 53:83 */     out.write(this.bytes);
/* 54:84 */     out.flush();
/* 55:   */   }
/* 56:   */   
/* 57:   */   public byte[] toByteArray()
/* 58:   */   {
/* 59:88 */     return this.bytes;
/* 60:   */   }
/* 61:   */   
/* 62:   */   public Object clone()
/* 63:   */   {
/* 64:94 */     return new CommAreaRecord(this.bytes);
/* 65:   */   }
/* 66:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.cci.core.support.CommAreaRecord
 * JD-Core Version:    0.7.0.1
 */