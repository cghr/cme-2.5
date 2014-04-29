/*  1:   */ package org.springframework.jdbc.support.lob;
/*  2:   */ 
/*  3:   */ import java.io.ByteArrayInputStream;
/*  4:   */ import java.io.InputStream;
/*  5:   */ import java.io.OutputStream;
/*  6:   */ import java.sql.Blob;
/*  7:   */ import java.sql.SQLException;
/*  8:   */ 
/*  9:   */ class PassThroughBlob
/* 10:   */   implements Blob
/* 11:   */ {
/* 12:   */   private byte[] content;
/* 13:   */   private InputStream binaryStream;
/* 14:   */   private long contentLength;
/* 15:   */   
/* 16:   */   public PassThroughBlob(byte[] content)
/* 17:   */   {
/* 18:42 */     this.content = content;
/* 19:43 */     this.contentLength = content.length;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public PassThroughBlob(InputStream binaryStream, long contentLength)
/* 23:   */   {
/* 24:47 */     this.binaryStream = binaryStream;
/* 25:48 */     this.contentLength = contentLength;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public long length()
/* 29:   */     throws SQLException
/* 30:   */   {
/* 31:53 */     return this.contentLength;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public InputStream getBinaryStream()
/* 35:   */     throws SQLException
/* 36:   */   {
/* 37:57 */     return this.content != null ? new ByteArrayInputStream(this.content) : this.binaryStream;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public InputStream getBinaryStream(long pos, long length)
/* 41:   */     throws SQLException
/* 42:   */   {
/* 43:62 */     throw new UnsupportedOperationException();
/* 44:   */   }
/* 45:   */   
/* 46:   */   public OutputStream setBinaryStream(long pos)
/* 47:   */     throws SQLException
/* 48:   */   {
/* 49:66 */     throw new UnsupportedOperationException();
/* 50:   */   }
/* 51:   */   
/* 52:   */   public byte[] getBytes(long pos, int length)
/* 53:   */     throws SQLException
/* 54:   */   {
/* 55:70 */     throw new UnsupportedOperationException();
/* 56:   */   }
/* 57:   */   
/* 58:   */   public int setBytes(long pos, byte[] bytes)
/* 59:   */     throws SQLException
/* 60:   */   {
/* 61:74 */     throw new UnsupportedOperationException();
/* 62:   */   }
/* 63:   */   
/* 64:   */   public int setBytes(long pos, byte[] bytes, int offset, int len)
/* 65:   */     throws SQLException
/* 66:   */   {
/* 67:78 */     throw new UnsupportedOperationException();
/* 68:   */   }
/* 69:   */   
/* 70:   */   public long position(byte[] pattern, long start)
/* 71:   */     throws SQLException
/* 72:   */   {
/* 73:82 */     throw new UnsupportedOperationException();
/* 74:   */   }
/* 75:   */   
/* 76:   */   public long position(Blob pattern, long start)
/* 77:   */     throws SQLException
/* 78:   */   {
/* 79:86 */     throw new UnsupportedOperationException();
/* 80:   */   }
/* 81:   */   
/* 82:   */   public void truncate(long len)
/* 83:   */     throws SQLException
/* 84:   */   {
/* 85:90 */     throw new UnsupportedOperationException();
/* 86:   */   }
/* 87:   */   
/* 88:   */   public void free()
/* 89:   */     throws SQLException
/* 90:   */   {}
/* 91:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.lob.PassThroughBlob
 * JD-Core Version:    0.7.0.1
 */