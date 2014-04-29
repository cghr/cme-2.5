/*   1:    */ package org.springframework.util.xml;
/*   2:    */ 
/*   3:    */ import java.util.LinkedHashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import javax.xml.namespace.QName;
/*   6:    */ import javax.xml.stream.Location;
/*   7:    */ import javax.xml.stream.XMLStreamException;
/*   8:    */ import org.springframework.util.StringUtils;
/*   9:    */ import org.xml.sax.ContentHandler;
/*  10:    */ import org.xml.sax.ErrorHandler;
/*  11:    */ import org.xml.sax.InputSource;
/*  12:    */ import org.xml.sax.Locator;
/*  13:    */ import org.xml.sax.SAXException;
/*  14:    */ import org.xml.sax.SAXNotRecognizedException;
/*  15:    */ import org.xml.sax.SAXNotSupportedException;
/*  16:    */ import org.xml.sax.SAXParseException;
/*  17:    */ 
/*  18:    */ abstract class AbstractStaxXMLReader
/*  19:    */   extends AbstractXMLReader
/*  20:    */ {
/*  21:    */   private static final String NAMESPACES_FEATURE_NAME = "http://xml.org/sax/features/namespaces";
/*  22:    */   private static final String NAMESPACE_PREFIXES_FEATURE_NAME = "http://xml.org/sax/features/namespace-prefixes";
/*  23:    */   private static final String IS_STANDALONE_FEATURE_NAME = "http://xml.org/sax/features/is-standalone";
/*  24: 53 */   private boolean namespacesFeature = true;
/*  25: 55 */   private boolean namespacePrefixesFeature = false;
/*  26:    */   private Boolean isStandalone;
/*  27: 59 */   private final Map<String, String> namespaces = new LinkedHashMap();
/*  28:    */   
/*  29:    */   public boolean getFeature(String name)
/*  30:    */     throws SAXNotRecognizedException, SAXNotSupportedException
/*  31:    */   {
/*  32: 63 */     if ("http://xml.org/sax/features/namespaces".equals(name)) {
/*  33: 64 */       return this.namespacesFeature;
/*  34:    */     }
/*  35: 66 */     if ("http://xml.org/sax/features/namespace-prefixes".equals(name)) {
/*  36: 67 */       return this.namespacePrefixesFeature;
/*  37:    */     }
/*  38: 69 */     if ("http://xml.org/sax/features/is-standalone".equals(name))
/*  39:    */     {
/*  40: 70 */       if (this.isStandalone != null) {
/*  41: 71 */         return this.isStandalone.booleanValue();
/*  42:    */       }
/*  43: 74 */       throw new SAXNotSupportedException("startDocument() callback not completed yet");
/*  44:    */     }
/*  45: 78 */     return super.getFeature(name);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setFeature(String name, boolean value)
/*  49:    */     throws SAXNotRecognizedException, SAXNotSupportedException
/*  50:    */   {
/*  51: 84 */     if ("http://xml.org/sax/features/namespaces".equals(name)) {
/*  52: 85 */       this.namespacesFeature = value;
/*  53: 87 */     } else if ("http://xml.org/sax/features/namespace-prefixes".equals(name)) {
/*  54: 88 */       this.namespacePrefixesFeature = value;
/*  55:    */     } else {
/*  56: 91 */       super.setFeature(name, value);
/*  57:    */     }
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected void setStandalone(boolean standalone)
/*  61:    */   {
/*  62: 96 */     this.isStandalone = Boolean.valueOf(standalone);
/*  63:    */   }
/*  64:    */   
/*  65:    */   protected boolean hasNamespacesFeature()
/*  66:    */   {
/*  67:103 */     return this.namespacesFeature;
/*  68:    */   }
/*  69:    */   
/*  70:    */   protected boolean hasNamespacePrefixesFeature()
/*  71:    */   {
/*  72:110 */     return this.namespacePrefixesFeature;
/*  73:    */   }
/*  74:    */   
/*  75:    */   protected String toQualifiedName(QName qName)
/*  76:    */   {
/*  77:121 */     String prefix = qName.getPrefix();
/*  78:122 */     if (!StringUtils.hasLength(prefix)) {
/*  79:123 */       return qName.getLocalPart();
/*  80:    */     }
/*  81:126 */     return prefix + ":" + qName.getLocalPart();
/*  82:    */   }
/*  83:    */   
/*  84:    */   public final void parse(InputSource ignored)
/*  85:    */     throws SAXException
/*  86:    */   {
/*  87:138 */     parse();
/*  88:    */   }
/*  89:    */   
/*  90:    */   public final void parse(String ignored)
/*  91:    */     throws SAXException
/*  92:    */   {
/*  93:148 */     parse();
/*  94:    */   }
/*  95:    */   
/*  96:    */   private void parse()
/*  97:    */     throws SAXException
/*  98:    */   {
/*  99:    */     try
/* 100:    */     {
/* 101:153 */       parseInternal();
/* 102:    */     }
/* 103:    */     catch (XMLStreamException ex)
/* 104:    */     {
/* 105:156 */       Locator locator = null;
/* 106:157 */       if (ex.getLocation() != null) {
/* 107:158 */         locator = new StaxLocator(ex.getLocation());
/* 108:    */       }
/* 109:160 */       SAXParseException saxException = new SAXParseException(ex.getMessage(), locator, ex);
/* 110:161 */       if (getErrorHandler() != null) {
/* 111:162 */         getErrorHandler().fatalError(saxException);
/* 112:    */       } else {
/* 113:165 */         throw saxException;
/* 114:    */       }
/* 115:    */     }
/* 116:    */   }
/* 117:    */   
/* 118:    */   protected abstract void parseInternal()
/* 119:    */     throws SAXException, XMLStreamException;
/* 120:    */   
/* 121:    */   protected void startPrefixMapping(String prefix, String namespace)
/* 122:    */     throws SAXException
/* 123:    */   {
/* 124:180 */     if (getContentHandler() != null)
/* 125:    */     {
/* 126:181 */       if (prefix == null) {
/* 127:182 */         prefix = "";
/* 128:    */       }
/* 129:184 */       if (!StringUtils.hasLength(namespace)) {
/* 130:185 */         return;
/* 131:    */       }
/* 132:187 */       if (!namespace.equals(this.namespaces.get(prefix)))
/* 133:    */       {
/* 134:188 */         getContentHandler().startPrefixMapping(prefix, namespace);
/* 135:189 */         this.namespaces.put(prefix, namespace);
/* 136:    */       }
/* 137:    */     }
/* 138:    */   }
/* 139:    */   
/* 140:    */   protected void endPrefixMapping(String prefix)
/* 141:    */     throws SAXException
/* 142:    */   {
/* 143:199 */     if ((getContentHandler() != null) && 
/* 144:200 */       (this.namespaces.containsKey(prefix)))
/* 145:    */     {
/* 146:201 */       getContentHandler().endPrefixMapping(prefix);
/* 147:202 */       this.namespaces.remove(prefix);
/* 148:    */     }
/* 149:    */   }
/* 150:    */   
/* 151:    */   private static class StaxLocator
/* 152:    */     implements Locator
/* 153:    */   {
/* 154:    */     private Location location;
/* 155:    */     
/* 156:    */     protected StaxLocator(Location location)
/* 157:    */     {
/* 158:217 */       this.location = location;
/* 159:    */     }
/* 160:    */     
/* 161:    */     public String getPublicId()
/* 162:    */     {
/* 163:221 */       return this.location.getPublicId();
/* 164:    */     }
/* 165:    */     
/* 166:    */     public String getSystemId()
/* 167:    */     {
/* 168:225 */       return this.location.getSystemId();
/* 169:    */     }
/* 170:    */     
/* 171:    */     public int getLineNumber()
/* 172:    */     {
/* 173:229 */       return this.location.getLineNumber();
/* 174:    */     }
/* 175:    */     
/* 176:    */     public int getColumnNumber()
/* 177:    */     {
/* 178:233 */       return this.location.getColumnNumber();
/* 179:    */     }
/* 180:    */   }
/* 181:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.xml.AbstractStaxXMLReader
 * JD-Core Version:    0.7.0.1
 */