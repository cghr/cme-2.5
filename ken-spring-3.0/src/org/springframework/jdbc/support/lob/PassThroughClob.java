/*   1:    */ package org.springframework.jdbc.support.lob;
/*   2:    */ 
/*   3:    */ import java.io.ByteArrayInputStream;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.InputStream;
/*   6:    */ import java.io.InputStreamReader;
/*   7:    */ import java.io.OutputStream;
/*   8:    */ import java.io.Reader;
/*   9:    */ import java.io.StringReader;
/*  10:    */ import java.io.UnsupportedEncodingException;
/*  11:    */ import java.io.Writer;
/*  12:    */ import java.sql.Clob;
/*  13:    */ import java.sql.SQLException;
/*  14:    */ import org.springframework.util.FileCopyUtils;
/*  15:    */ 
/*  16:    */ class PassThroughClob
/*  17:    */   implements Clob
/*  18:    */ {
/*  19:    */   private String content;
/*  20:    */   private Reader characterStream;
/*  21:    */   private InputStream asciiStream;
/*  22:    */   private long contentLength;
/*  23:    */   
/*  24:    */   public PassThroughClob(String content)
/*  25:    */   {
/*  26: 52 */     this.content = content;
/*  27: 53 */     this.contentLength = content.length();
/*  28:    */   }
/*  29:    */   
/*  30:    */   public PassThroughClob(Reader characterStream, long contentLength)
/*  31:    */   {
/*  32: 57 */     this.characterStream = characterStream;
/*  33: 58 */     this.contentLength = contentLength;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public PassThroughClob(InputStream asciiStream, long contentLength)
/*  37:    */   {
/*  38: 62 */     this.asciiStream = asciiStream;
/*  39: 63 */     this.contentLength = contentLength;
/*  40:    */   }
/*  41:    */   
/*  42:    */   public long length()
/*  43:    */     throws SQLException
/*  44:    */   {
/*  45: 68 */     return this.contentLength;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public Reader getCharacterStream()
/*  49:    */     throws SQLException
/*  50:    */   {
/*  51:    */     try
/*  52:    */     {
/*  53: 73 */       if (this.content != null) {
/*  54: 74 */         return new StringReader(this.content);
/*  55:    */       }
/*  56: 76 */       if (this.characterStream != null) {
/*  57: 77 */         return this.characterStream;
/*  58:    */       }
/*  59: 80 */       return new InputStreamReader(this.asciiStream, "US-ASCII");
/*  60:    */     }
/*  61:    */     catch (UnsupportedEncodingException ex)
/*  62:    */     {
/*  63: 84 */       throw new SQLException("US-ASCII encoding not supported: " + ex);
/*  64:    */     }
/*  65:    */   }
/*  66:    */   
/*  67:    */   public InputStream getAsciiStream()
/*  68:    */     throws SQLException
/*  69:    */   {
/*  70:    */     try
/*  71:    */     {
/*  72: 90 */       if (this.content != null) {
/*  73: 91 */         return new ByteArrayInputStream(this.content.getBytes("US-ASCII"));
/*  74:    */       }
/*  75: 93 */       if (this.characterStream != null)
/*  76:    */       {
/*  77: 94 */         String tempContent = FileCopyUtils.copyToString(this.characterStream);
/*  78: 95 */         return new ByteArrayInputStream(tempContent.getBytes("US-ASCII"));
/*  79:    */       }
/*  80: 98 */       return this.asciiStream;
/*  81:    */     }
/*  82:    */     catch (UnsupportedEncodingException ex)
/*  83:    */     {
/*  84:102 */       throw new SQLException("US-ASCII encoding not supported: " + ex);
/*  85:    */     }
/*  86:    */     catch (IOException ex)
/*  87:    */     {
/*  88:105 */       throw new SQLException("Failed to read stream content: " + ex);
/*  89:    */     }
/*  90:    */   }
/*  91:    */   
/*  92:    */   public Reader getCharacterStream(long pos, long length)
/*  93:    */     throws SQLException
/*  94:    */   {
/*  95:111 */     throw new UnsupportedOperationException();
/*  96:    */   }
/*  97:    */   
/*  98:    */   public Writer setCharacterStream(long pos)
/*  99:    */     throws SQLException
/* 100:    */   {
/* 101:115 */     throw new UnsupportedOperationException();
/* 102:    */   }
/* 103:    */   
/* 104:    */   public OutputStream setAsciiStream(long pos)
/* 105:    */     throws SQLException
/* 106:    */   {
/* 107:119 */     throw new UnsupportedOperationException();
/* 108:    */   }
/* 109:    */   
/* 110:    */   public String getSubString(long pos, int length)
/* 111:    */     throws SQLException
/* 112:    */   {
/* 113:123 */     throw new UnsupportedOperationException();
/* 114:    */   }
/* 115:    */   
/* 116:    */   public int setString(long pos, String str)
/* 117:    */     throws SQLException
/* 118:    */   {
/* 119:127 */     throw new UnsupportedOperationException();
/* 120:    */   }
/* 121:    */   
/* 122:    */   public int setString(long pos, String str, int offset, int len)
/* 123:    */     throws SQLException
/* 124:    */   {
/* 125:131 */     throw new UnsupportedOperationException();
/* 126:    */   }
/* 127:    */   
/* 128:    */   public long position(String searchstr, long start)
/* 129:    */     throws SQLException
/* 130:    */   {
/* 131:135 */     throw new UnsupportedOperationException();
/* 132:    */   }
/* 133:    */   
/* 134:    */   public long position(Clob searchstr, long start)
/* 135:    */     throws SQLException
/* 136:    */   {
/* 137:139 */     throw new UnsupportedOperationException();
/* 138:    */   }
/* 139:    */   
/* 140:    */   public void truncate(long len)
/* 141:    */     throws SQLException
/* 142:    */   {
/* 143:143 */     throw new UnsupportedOperationException();
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void free()
/* 147:    */     throws SQLException
/* 148:    */   {}
/* 149:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.lob.PassThroughClob
 * JD-Core Version:    0.7.0.1
 */