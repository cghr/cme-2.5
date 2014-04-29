/*   1:    */ package org.springframework.util.xml;
/*   2:    */ 
/*   3:    */ import javax.xml.namespace.QName;
/*   4:    */ import javax.xml.stream.XMLStreamException;
/*   5:    */ import org.xml.sax.Attributes;
/*   6:    */ import org.xml.sax.ContentHandler;
/*   7:    */ import org.xml.sax.SAXException;
/*   8:    */ 
/*   9:    */ abstract class AbstractStaxContentHandler
/*  10:    */   implements ContentHandler
/*  11:    */ {
/*  12: 36 */   private SimpleNamespaceContext namespaceContext = new SimpleNamespaceContext();
/*  13: 38 */   private boolean namespaceContextChanged = false;
/*  14:    */   
/*  15:    */   public final void startDocument()
/*  16:    */     throws SAXException
/*  17:    */   {
/*  18: 41 */     this.namespaceContext.clear();
/*  19: 42 */     this.namespaceContextChanged = false;
/*  20:    */     try
/*  21:    */     {
/*  22: 44 */       startDocumentInternal();
/*  23:    */     }
/*  24:    */     catch (XMLStreamException ex)
/*  25:    */     {
/*  26: 47 */       throw new SAXException("Could not handle startDocument: " + ex.getMessage(), ex);
/*  27:    */     }
/*  28:    */   }
/*  29:    */   
/*  30:    */   protected abstract void startDocumentInternal()
/*  31:    */     throws XMLStreamException;
/*  32:    */   
/*  33:    */   public final void endDocument()
/*  34:    */     throws SAXException
/*  35:    */   {
/*  36: 54 */     this.namespaceContext.clear();
/*  37: 55 */     this.namespaceContextChanged = false;
/*  38:    */     try
/*  39:    */     {
/*  40: 57 */       endDocumentInternal();
/*  41:    */     }
/*  42:    */     catch (XMLStreamException ex)
/*  43:    */     {
/*  44: 60 */       throw new SAXException("Could not handle startDocument: " + ex.getMessage(), ex);
/*  45:    */     }
/*  46:    */   }
/*  47:    */   
/*  48:    */   protected abstract void endDocumentInternal()
/*  49:    */     throws XMLStreamException;
/*  50:    */   
/*  51:    */   public final void startPrefixMapping(String prefix, String uri)
/*  52:    */   {
/*  53: 72 */     this.namespaceContext.bindNamespaceUri(prefix, uri);
/*  54: 73 */     this.namespaceContextChanged = true;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public final void endPrefixMapping(String prefix)
/*  58:    */   {
/*  59: 82 */     this.namespaceContext.removeBinding(prefix);
/*  60: 83 */     this.namespaceContextChanged = true;
/*  61:    */   }
/*  62:    */   
/*  63:    */   public final void startElement(String uri, String localName, String qName, Attributes atts)
/*  64:    */     throws SAXException
/*  65:    */   {
/*  66:    */     try
/*  67:    */     {
/*  68: 88 */       startElementInternal(toQName(uri, qName), atts, this.namespaceContextChanged ? this.namespaceContext : null);
/*  69: 89 */       this.namespaceContextChanged = false;
/*  70:    */     }
/*  71:    */     catch (XMLStreamException ex)
/*  72:    */     {
/*  73: 92 */       throw new SAXException("Could not handle startElement: " + ex.getMessage(), ex);
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   protected abstract void startElementInternal(QName paramQName, Attributes paramAttributes, SimpleNamespaceContext paramSimpleNamespaceContext)
/*  78:    */     throws XMLStreamException;
/*  79:    */   
/*  80:    */   public final void endElement(String uri, String localName, String qName)
/*  81:    */     throws SAXException
/*  82:    */   {
/*  83:    */     try
/*  84:    */     {
/*  85:101 */       endElementInternal(toQName(uri, qName), this.namespaceContextChanged ? this.namespaceContext : null);
/*  86:102 */       this.namespaceContextChanged = false;
/*  87:    */     }
/*  88:    */     catch (XMLStreamException ex)
/*  89:    */     {
/*  90:105 */       throw new SAXException("Could not handle endElement: " + ex.getMessage(), ex);
/*  91:    */     }
/*  92:    */   }
/*  93:    */   
/*  94:    */   protected abstract void endElementInternal(QName paramQName, SimpleNamespaceContext paramSimpleNamespaceContext)
/*  95:    */     throws XMLStreamException;
/*  96:    */   
/*  97:    */   public final void characters(char[] ch, int start, int length)
/*  98:    */     throws SAXException
/*  99:    */   {
/* 100:    */     try
/* 101:    */     {
/* 102:114 */       charactersInternal(ch, start, length);
/* 103:    */     }
/* 104:    */     catch (XMLStreamException ex)
/* 105:    */     {
/* 106:117 */       throw new SAXException("Could not handle characters: " + ex.getMessage(), ex);
/* 107:    */     }
/* 108:    */   }
/* 109:    */   
/* 110:    */   protected abstract void charactersInternal(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/* 111:    */     throws XMLStreamException;
/* 112:    */   
/* 113:    */   public final void ignorableWhitespace(char[] ch, int start, int length)
/* 114:    */     throws SAXException
/* 115:    */   {
/* 116:    */     try
/* 117:    */     {
/* 118:125 */       ignorableWhitespaceInternal(ch, start, length);
/* 119:    */     }
/* 120:    */     catch (XMLStreamException ex)
/* 121:    */     {
/* 122:128 */       throw new SAXException("Could not handle ignorableWhitespace:" + ex.getMessage(), ex);
/* 123:    */     }
/* 124:    */   }
/* 125:    */   
/* 126:    */   protected abstract void ignorableWhitespaceInternal(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/* 127:    */     throws XMLStreamException;
/* 128:    */   
/* 129:    */   public final void processingInstruction(String target, String data)
/* 130:    */     throws SAXException
/* 131:    */   {
/* 132:    */     try
/* 133:    */     {
/* 134:136 */       processingInstructionInternal(target, data);
/* 135:    */     }
/* 136:    */     catch (XMLStreamException ex)
/* 137:    */     {
/* 138:139 */       throw new SAXException("Could not handle processingInstruction: " + ex.getMessage(), ex);
/* 139:    */     }
/* 140:    */   }
/* 141:    */   
/* 142:    */   protected abstract void processingInstructionInternal(String paramString1, String paramString2)
/* 143:    */     throws XMLStreamException;
/* 144:    */   
/* 145:    */   public final void skippedEntity(String name)
/* 146:    */     throws SAXException
/* 147:    */   {
/* 148:    */     try
/* 149:    */     {
/* 150:147 */       skippedEntityInternal(name);
/* 151:    */     }
/* 152:    */     catch (XMLStreamException ex)
/* 153:    */     {
/* 154:150 */       throw new SAXException("Could not handle skippedEntity: " + ex.getMessage(), ex);
/* 155:    */     }
/* 156:    */   }
/* 157:    */   
/* 158:    */   protected QName toQName(String namespaceUri, String qualifiedName)
/* 159:    */   {
/* 160:163 */     int idx = qualifiedName.indexOf(':');
/* 161:164 */     if (idx == -1) {
/* 162:165 */       return new QName(namespaceUri, qualifiedName);
/* 163:    */     }
/* 164:168 */     String prefix = qualifiedName.substring(0, idx);
/* 165:169 */     String localPart = qualifiedName.substring(idx + 1);
/* 166:170 */     return new QName(namespaceUri, localPart, prefix);
/* 167:    */   }
/* 168:    */   
/* 169:    */   protected abstract void skippedEntityInternal(String paramString)
/* 170:    */     throws XMLStreamException;
/* 171:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.xml.AbstractStaxContentHandler
 * JD-Core Version:    0.7.0.1
 */