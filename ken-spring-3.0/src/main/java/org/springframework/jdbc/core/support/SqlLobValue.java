/*   1:    */ package org.springframework.jdbc.core.support;
/*   2:    */ 
/*   3:    */ import java.io.InputStream;
/*   4:    */ import java.io.Reader;
/*   5:    */ import java.sql.PreparedStatement;
/*   6:    */ import java.sql.SQLException;
/*   7:    */ import org.springframework.jdbc.core.DisposableSqlTypeValue;
/*   8:    */ import org.springframework.jdbc.support.lob.DefaultLobHandler;
/*   9:    */ import org.springframework.jdbc.support.lob.LobCreator;
/*  10:    */ import org.springframework.jdbc.support.lob.LobHandler;
/*  11:    */ 
/*  12:    */ public class SqlLobValue
/*  13:    */   implements DisposableSqlTypeValue
/*  14:    */ {
/*  15:    */   private final Object content;
/*  16:    */   private final int length;
/*  17:    */   private final LobCreator lobCreator;
/*  18:    */   
/*  19:    */   public SqlLobValue(byte[] bytes)
/*  20:    */   {
/*  21: 86 */     this(bytes, new DefaultLobHandler());
/*  22:    */   }
/*  23:    */   
/*  24:    */   public SqlLobValue(byte[] bytes, LobHandler lobHandler)
/*  25:    */   {
/*  26: 95 */     this.content = bytes;
/*  27: 96 */     this.length = (bytes != null ? bytes.length : 0);
/*  28: 97 */     this.lobCreator = lobHandler.getLobCreator();
/*  29:    */   }
/*  30:    */   
/*  31:    */   public SqlLobValue(String content)
/*  32:    */   {
/*  33:107 */     this(content, new DefaultLobHandler());
/*  34:    */   }
/*  35:    */   
/*  36:    */   public SqlLobValue(String content, LobHandler lobHandler)
/*  37:    */   {
/*  38:116 */     this.content = content;
/*  39:117 */     this.length = (content != null ? content.length() : 0);
/*  40:118 */     this.lobCreator = lobHandler.getLobCreator();
/*  41:    */   }
/*  42:    */   
/*  43:    */   public SqlLobValue(InputStream stream, int length)
/*  44:    */   {
/*  45:129 */     this(stream, length, new DefaultLobHandler());
/*  46:    */   }
/*  47:    */   
/*  48:    */   public SqlLobValue(InputStream stream, int length, LobHandler lobHandler)
/*  49:    */   {
/*  50:139 */     this.content = stream;
/*  51:140 */     this.length = length;
/*  52:141 */     this.lobCreator = lobHandler.getLobCreator();
/*  53:    */   }
/*  54:    */   
/*  55:    */   public SqlLobValue(Reader reader, int length)
/*  56:    */   {
/*  57:152 */     this(reader, length, new DefaultLobHandler());
/*  58:    */   }
/*  59:    */   
/*  60:    */   public SqlLobValue(Reader reader, int length, LobHandler lobHandler)
/*  61:    */   {
/*  62:162 */     this.content = reader;
/*  63:163 */     this.length = length;
/*  64:164 */     this.lobCreator = lobHandler.getLobCreator();
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void setTypeValue(PreparedStatement ps, int paramIndex, int sqlType, String typeName)
/*  68:    */     throws SQLException
/*  69:    */   {
/*  70:173 */     if (sqlType == 2004)
/*  71:    */     {
/*  72:174 */       if (((this.content instanceof byte[])) || (this.content == null)) {
/*  73:175 */         this.lobCreator.setBlobAsBytes(ps, paramIndex, (byte[])this.content);
/*  74:177 */       } else if ((this.content instanceof String)) {
/*  75:178 */         this.lobCreator.setBlobAsBytes(ps, paramIndex, ((String)this.content).getBytes());
/*  76:180 */       } else if ((this.content instanceof InputStream)) {
/*  77:181 */         this.lobCreator.setBlobAsBinaryStream(ps, paramIndex, (InputStream)this.content, this.length);
/*  78:    */       } else {
/*  79:184 */         throw new IllegalArgumentException(
/*  80:185 */           "Content type [" + this.content.getClass().getName() + "] not supported for BLOB columns");
/*  81:    */       }
/*  82:    */     }
/*  83:188 */     else if (sqlType == 2005)
/*  84:    */     {
/*  85:189 */       if (((this.content instanceof String)) || (this.content == null)) {
/*  86:190 */         this.lobCreator.setClobAsString(ps, paramIndex, (String)this.content);
/*  87:192 */       } else if ((this.content instanceof InputStream)) {
/*  88:193 */         this.lobCreator.setClobAsAsciiStream(ps, paramIndex, (InputStream)this.content, this.length);
/*  89:195 */       } else if ((this.content instanceof Reader)) {
/*  90:196 */         this.lobCreator.setClobAsCharacterStream(ps, paramIndex, (Reader)this.content, this.length);
/*  91:    */       } else {
/*  92:199 */         throw new IllegalArgumentException(
/*  93:200 */           "Content type [" + this.content.getClass().getName() + "] not supported for CLOB columns");
/*  94:    */       }
/*  95:    */     }
/*  96:    */     else {
/*  97:204 */       throw new IllegalArgumentException("SqlLobValue only supports SQL types BLOB and CLOB");
/*  98:    */     }
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void cleanup()
/* 102:    */   {
/* 103:212 */     this.lobCreator.close();
/* 104:    */   }
/* 105:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.core.support.SqlLobValue
 * JD-Core Version:    0.7.0.1
 */