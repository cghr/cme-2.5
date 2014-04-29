/*   1:    */ package org.springframework.jdbc.support.xml;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import java.io.Reader;
/*   6:    */ import java.sql.Connection;
/*   7:    */ import java.sql.PreparedStatement;
/*   8:    */ import java.sql.ResultSet;
/*   9:    */ import java.sql.SQLException;
/*  10:    */ import java.sql.SQLXML;
/*  11:    */ import javax.xml.transform.Source;
/*  12:    */ import javax.xml.transform.dom.DOMResult;
/*  13:    */ import javax.xml.transform.dom.DOMSource;
/*  14:    */ import org.springframework.dao.DataAccessResourceFailureException;
/*  15:    */ import org.w3c.dom.Document;
/*  16:    */ 
/*  17:    */ public class Jdbc4SqlXmlHandler
/*  18:    */   implements SqlXmlHandler
/*  19:    */ {
/*  20:    */   public String getXmlAsString(ResultSet rs, String columnName)
/*  21:    */     throws SQLException
/*  22:    */   {
/*  23: 54 */     return rs.getSQLXML(columnName).getString();
/*  24:    */   }
/*  25:    */   
/*  26:    */   public String getXmlAsString(ResultSet rs, int columnIndex)
/*  27:    */     throws SQLException
/*  28:    */   {
/*  29: 58 */     return rs.getSQLXML(columnIndex).getString();
/*  30:    */   }
/*  31:    */   
/*  32:    */   public InputStream getXmlAsBinaryStream(ResultSet rs, String columnName)
/*  33:    */     throws SQLException
/*  34:    */   {
/*  35: 62 */     return rs.getSQLXML(columnName).getBinaryStream();
/*  36:    */   }
/*  37:    */   
/*  38:    */   public InputStream getXmlAsBinaryStream(ResultSet rs, int columnIndex)
/*  39:    */     throws SQLException
/*  40:    */   {
/*  41: 66 */     return rs.getSQLXML(columnIndex).getBinaryStream();
/*  42:    */   }
/*  43:    */   
/*  44:    */   public Reader getXmlAsCharacterStream(ResultSet rs, String columnName)
/*  45:    */     throws SQLException
/*  46:    */   {
/*  47: 70 */     return rs.getSQLXML(columnName).getCharacterStream();
/*  48:    */   }
/*  49:    */   
/*  50:    */   public Reader getXmlAsCharacterStream(ResultSet rs, int columnIndex)
/*  51:    */     throws SQLException
/*  52:    */   {
/*  53: 74 */     return rs.getSQLXML(columnIndex).getCharacterStream();
/*  54:    */   }
/*  55:    */   
/*  56:    */   public Source getXmlAsSource(ResultSet rs, String columnName, Class sourceClass)
/*  57:    */     throws SQLException
/*  58:    */   {
/*  59: 79 */     return rs.getSQLXML(columnName).getSource(sourceClass != null ? sourceClass : DOMSource.class);
/*  60:    */   }
/*  61:    */   
/*  62:    */   public Source getXmlAsSource(ResultSet rs, int columnIndex, Class sourceClass)
/*  63:    */     throws SQLException
/*  64:    */   {
/*  65: 84 */     return rs.getSQLXML(columnIndex).getSource(sourceClass != null ? sourceClass : DOMSource.class);
/*  66:    */   }
/*  67:    */   
/*  68:    */   public SqlXmlValue newSqlXmlValue(final String value)
/*  69:    */   {
/*  70: 93 */     new AbstractJdbc4SqlXmlValue(value)
/*  71:    */     {
/*  72:    */       protected void provideXml(SQLXML xmlObject)
/*  73:    */         throws SQLException, IOException
/*  74:    */       {
/*  75: 96 */         xmlObject.setString(value);
/*  76:    */       }
/*  77:    */     };
/*  78:    */   }
/*  79:    */   
/*  80:    */   public SqlXmlValue newSqlXmlValue(final XmlBinaryStreamProvider provider)
/*  81:    */   {
/*  82:102 */     new AbstractJdbc4SqlXmlValue(provider)
/*  83:    */     {
/*  84:    */       protected void provideXml(SQLXML xmlObject)
/*  85:    */         throws SQLException, IOException
/*  86:    */       {
/*  87:105 */         provider.provideXml(xmlObject.setBinaryStream());
/*  88:    */       }
/*  89:    */     };
/*  90:    */   }
/*  91:    */   
/*  92:    */   public SqlXmlValue newSqlXmlValue(final XmlCharacterStreamProvider provider)
/*  93:    */   {
/*  94:111 */     new AbstractJdbc4SqlXmlValue(provider)
/*  95:    */     {
/*  96:    */       protected void provideXml(SQLXML xmlObject)
/*  97:    */         throws SQLException, IOException
/*  98:    */       {
/*  99:114 */         provider.provideXml(xmlObject.setCharacterStream());
/* 100:    */       }
/* 101:    */     };
/* 102:    */   }
/* 103:    */   
/* 104:    */   public SqlXmlValue newSqlXmlValue(final Class resultClass, final XmlResultProvider provider)
/* 105:    */   {
/* 106:120 */     new AbstractJdbc4SqlXmlValue(provider)
/* 107:    */     {
/* 108:    */       protected void provideXml(SQLXML xmlObject)
/* 109:    */         throws SQLException, IOException
/* 110:    */       {
/* 111:124 */         provider.provideXml(xmlObject.setResult(resultClass));
/* 112:    */       }
/* 113:    */     };
/* 114:    */   }
/* 115:    */   
/* 116:    */   public SqlXmlValue newSqlXmlValue(final Document document)
/* 117:    */   {
/* 118:130 */     new AbstractJdbc4SqlXmlValue(document)
/* 119:    */     {
/* 120:    */       protected void provideXml(SQLXML xmlObject)
/* 121:    */         throws SQLException, IOException
/* 122:    */       {
/* 123:133 */         ((DOMResult)xmlObject.setResult(DOMResult.class)).setNode(document);
/* 124:    */       }
/* 125:    */     };
/* 126:    */   }
/* 127:    */   
/* 128:    */   private static abstract class AbstractJdbc4SqlXmlValue
/* 129:    */     implements SqlXmlValue
/* 130:    */   {
/* 131:    */     private SQLXML xmlObject;
/* 132:    */     
/* 133:    */     public void setValue(PreparedStatement ps, int paramIndex)
/* 134:    */       throws SQLException
/* 135:    */     {
/* 136:147 */       this.xmlObject = ps.getConnection().createSQLXML();
/* 137:    */       try
/* 138:    */       {
/* 139:149 */         provideXml(this.xmlObject);
/* 140:    */       }
/* 141:    */       catch (IOException ex)
/* 142:    */       {
/* 143:152 */         throw new DataAccessResourceFailureException("Failure encountered while providing XML", ex);
/* 144:    */       }
/* 145:154 */       ps.setSQLXML(paramIndex, this.xmlObject);
/* 146:    */     }
/* 147:    */     
/* 148:    */     public void cleanup()
/* 149:    */     {
/* 150:    */       try
/* 151:    */       {
/* 152:159 */         this.xmlObject.free();
/* 153:    */       }
/* 154:    */       catch (SQLException ex)
/* 155:    */       {
/* 156:162 */         throw new DataAccessResourceFailureException("Could not free SQLXML object", ex);
/* 157:    */       }
/* 158:    */     }
/* 159:    */     
/* 160:    */     protected abstract void provideXml(SQLXML paramSQLXML)
/* 161:    */       throws SQLException, IOException;
/* 162:    */   }
/* 163:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.xml.Jdbc4SqlXmlHandler
 * JD-Core Version:    0.7.0.1
 */