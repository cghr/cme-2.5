/*   1:    */ package org.springframework.util.xml;
/*   2:    */ 
/*   3:    */ import javax.xml.namespace.QName;
/*   4:    */ import javax.xml.stream.Location;
/*   5:    */ import javax.xml.stream.XMLStreamException;
/*   6:    */ import javax.xml.stream.XMLStreamReader;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ import org.springframework.util.StringUtils;
/*   9:    */ import org.xml.sax.Attributes;
/*  10:    */ import org.xml.sax.ContentHandler;
/*  11:    */ import org.xml.sax.SAXException;
/*  12:    */ import org.xml.sax.ext.LexicalHandler;
/*  13:    */ import org.xml.sax.ext.Locator2;
/*  14:    */ import org.xml.sax.helpers.AttributesImpl;
/*  15:    */ 
/*  16:    */ class StaxStreamXMLReader
/*  17:    */   extends AbstractStaxXMLReader
/*  18:    */ {
/*  19:    */   private static final String DEFAULT_XML_VERSION = "1.0";
/*  20:    */   private final XMLStreamReader reader;
/*  21: 51 */   private String xmlVersion = "1.0";
/*  22:    */   private String encoding;
/*  23:    */   
/*  24:    */   StaxStreamXMLReader(XMLStreamReader reader)
/*  25:    */   {
/*  26: 64 */     Assert.notNull(reader, "'reader' must not be null");
/*  27: 65 */     int event = reader.getEventType();
/*  28: 66 */     if ((event != 7) && (event != 1)) {
/*  29: 67 */       throw new IllegalStateException("XMLEventReader not at start of document or element");
/*  30:    */     }
/*  31: 69 */     this.reader = reader;
/*  32:    */   }
/*  33:    */   
/*  34:    */   protected void parseInternal()
/*  35:    */     throws SAXException, XMLStreamException
/*  36:    */   {
/*  37: 74 */     boolean documentStarted = false;
/*  38: 75 */     boolean documentEnded = false;
/*  39: 76 */     int elementDepth = 0;
/*  40: 77 */     int eventType = this.reader.getEventType();
/*  41:    */     for (;;)
/*  42:    */     {
/*  43: 79 */       if ((eventType != 7) && (eventType != 8) && 
/*  44: 80 */         (!documentStarted))
/*  45:    */       {
/*  46: 81 */         handleStartDocument();
/*  47: 82 */         documentStarted = true;
/*  48:    */       }
/*  49: 84 */       switch (eventType)
/*  50:    */       {
/*  51:    */       case 1: 
/*  52: 86 */         elementDepth++;
/*  53: 87 */         handleStartElement();
/*  54: 88 */         break;
/*  55:    */       case 2: 
/*  56: 90 */         elementDepth--;
/*  57: 91 */         if (elementDepth >= 0) {
/*  58: 92 */           handleEndElement();
/*  59:    */         }
/*  60: 94 */         break;
/*  61:    */       case 3: 
/*  62: 96 */         handleProcessingInstruction();
/*  63: 97 */         break;
/*  64:    */       case 4: 
/*  65:    */       case 6: 
/*  66:    */       case 12: 
/*  67:101 */         handleCharacters();
/*  68:102 */         break;
/*  69:    */       case 7: 
/*  70:104 */         handleStartDocument();
/*  71:105 */         documentStarted = true;
/*  72:106 */         break;
/*  73:    */       case 8: 
/*  74:108 */         handleEndDocument();
/*  75:109 */         documentEnded = true;
/*  76:110 */         break;
/*  77:    */       case 5: 
/*  78:112 */         handleComment();
/*  79:113 */         break;
/*  80:    */       case 11: 
/*  81:115 */         handleDtd();
/*  82:116 */         break;
/*  83:    */       case 9: 
/*  84:118 */         handleEntityReference();
/*  85:    */       }
/*  86:121 */       if ((!this.reader.hasNext()) || (elementDepth < 0)) {
/*  87:    */         break;
/*  88:    */       }
/*  89:122 */       eventType = this.reader.next();
/*  90:    */     }
/*  91:128 */     if (!documentEnded) {
/*  92:129 */       handleEndDocument();
/*  93:    */     }
/*  94:    */   }
/*  95:    */   
/*  96:    */   private void handleStartDocument()
/*  97:    */     throws SAXException
/*  98:    */   {
/*  99:134 */     if (7 == this.reader.getEventType())
/* 100:    */     {
/* 101:135 */       String xmlVersion = this.reader.getVersion();
/* 102:136 */       if (StringUtils.hasLength(xmlVersion)) {
/* 103:137 */         this.xmlVersion = xmlVersion;
/* 104:    */       }
/* 105:139 */       this.encoding = this.reader.getCharacterEncodingScheme();
/* 106:    */     }
/* 107:142 */     if (getContentHandler() != null)
/* 108:    */     {
/* 109:143 */       final Location location = this.reader.getLocation();
/* 110:    */       
/* 111:145 */       getContentHandler().setDocumentLocator(new Locator2()
/* 112:    */       {
/* 113:    */         public int getColumnNumber()
/* 114:    */         {
/* 115:148 */           return location != null ? location.getColumnNumber() : -1;
/* 116:    */         }
/* 117:    */         
/* 118:    */         public int getLineNumber()
/* 119:    */         {
/* 120:152 */           return location != null ? location.getLineNumber() : -1;
/* 121:    */         }
/* 122:    */         
/* 123:    */         public String getPublicId()
/* 124:    */         {
/* 125:156 */           return location != null ? location.getPublicId() : null;
/* 126:    */         }
/* 127:    */         
/* 128:    */         public String getSystemId()
/* 129:    */         {
/* 130:160 */           return location != null ? location.getSystemId() : null;
/* 131:    */         }
/* 132:    */         
/* 133:    */         public String getXMLVersion()
/* 134:    */         {
/* 135:164 */           return StaxStreamXMLReader.this.xmlVersion;
/* 136:    */         }
/* 137:    */         
/* 138:    */         public String getEncoding()
/* 139:    */         {
/* 140:168 */           return StaxStreamXMLReader.this.encoding;
/* 141:    */         }
/* 142:170 */       });
/* 143:171 */       getContentHandler().startDocument();
/* 144:172 */       if (this.reader.standaloneSet()) {
/* 145:173 */         setStandalone(this.reader.isStandalone());
/* 146:    */       }
/* 147:    */     }
/* 148:    */   }
/* 149:    */   
/* 150:    */   private void handleStartElement()
/* 151:    */     throws SAXException
/* 152:    */   {
/* 153:179 */     if (getContentHandler() != null)
/* 154:    */     {
/* 155:180 */       QName qName = this.reader.getName();
/* 156:181 */       if (hasNamespacesFeature())
/* 157:    */       {
/* 158:182 */         for (int i = 0; i < this.reader.getNamespaceCount(); i++) {
/* 159:183 */           startPrefixMapping(this.reader.getNamespacePrefix(i), this.reader.getNamespaceURI(i));
/* 160:    */         }
/* 161:185 */         for (int i = 0; i < this.reader.getAttributeCount(); i++)
/* 162:    */         {
/* 163:186 */           String prefix = this.reader.getAttributePrefix(i);
/* 164:187 */           String namespace = this.reader.getAttributeNamespace(i);
/* 165:188 */           if (StringUtils.hasLength(namespace)) {
/* 166:189 */             startPrefixMapping(prefix, namespace);
/* 167:    */           }
/* 168:    */         }
/* 169:192 */         getContentHandler().startElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName), 
/* 170:193 */           getAttributes());
/* 171:    */       }
/* 172:    */       else
/* 173:    */       {
/* 174:196 */         getContentHandler().startElement("", "", toQualifiedName(qName), getAttributes());
/* 175:    */       }
/* 176:    */     }
/* 177:    */   }
/* 178:    */   
/* 179:    */   private void handleEndElement()
/* 180:    */     throws SAXException
/* 181:    */   {
/* 182:202 */     if (getContentHandler() != null)
/* 183:    */     {
/* 184:203 */       QName qName = this.reader.getName();
/* 185:204 */       if (hasNamespacesFeature())
/* 186:    */       {
/* 187:205 */         getContentHandler().endElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName));
/* 188:206 */         for (int i = 0; i < this.reader.getNamespaceCount(); i++)
/* 189:    */         {
/* 190:207 */           String prefix = this.reader.getNamespacePrefix(i);
/* 191:208 */           if (prefix == null) {
/* 192:209 */             prefix = "";
/* 193:    */           }
/* 194:211 */           endPrefixMapping(prefix);
/* 195:    */         }
/* 196:    */       }
/* 197:    */       else
/* 198:    */       {
/* 199:215 */         getContentHandler().endElement("", "", toQualifiedName(qName));
/* 200:    */       }
/* 201:    */     }
/* 202:    */   }
/* 203:    */   
/* 204:    */   private void handleCharacters()
/* 205:    */     throws SAXException
/* 206:    */   {
/* 207:221 */     if ((getContentHandler() != null) && (this.reader.isWhiteSpace()))
/* 208:    */     {
/* 209:223 */       getContentHandler().ignorableWhitespace(this.reader.getTextCharacters(), this.reader.getTextStart(), this.reader.getTextLength());
/* 210:224 */       return;
/* 211:    */     }
/* 212:226 */     if ((12 == this.reader.getEventType()) && (getLexicalHandler() != null)) {
/* 213:227 */       getLexicalHandler().startCDATA();
/* 214:    */     }
/* 215:229 */     if (getContentHandler() != null) {
/* 216:230 */       getContentHandler().characters(this.reader.getTextCharacters(), this.reader.getTextStart(), this.reader.getTextLength());
/* 217:    */     }
/* 218:232 */     if ((12 == this.reader.getEventType()) && (getLexicalHandler() != null)) {
/* 219:233 */       getLexicalHandler().endCDATA();
/* 220:    */     }
/* 221:    */   }
/* 222:    */   
/* 223:    */   private void handleComment()
/* 224:    */     throws SAXException
/* 225:    */   {
/* 226:238 */     if (getLexicalHandler() != null) {
/* 227:239 */       getLexicalHandler().comment(this.reader.getTextCharacters(), this.reader.getTextStart(), this.reader.getTextLength());
/* 228:    */     }
/* 229:    */   }
/* 230:    */   
/* 231:    */   private void handleDtd()
/* 232:    */     throws SAXException
/* 233:    */   {
/* 234:244 */     if (getLexicalHandler() != null)
/* 235:    */     {
/* 236:245 */       Location location = this.reader.getLocation();
/* 237:246 */       getLexicalHandler().startDTD(null, location.getPublicId(), location.getSystemId());
/* 238:    */     }
/* 239:248 */     if (getLexicalHandler() != null) {
/* 240:249 */       getLexicalHandler().endDTD();
/* 241:    */     }
/* 242:    */   }
/* 243:    */   
/* 244:    */   private void handleEntityReference()
/* 245:    */     throws SAXException
/* 246:    */   {
/* 247:254 */     if (getLexicalHandler() != null) {
/* 248:255 */       getLexicalHandler().startEntity(this.reader.getLocalName());
/* 249:    */     }
/* 250:257 */     if (getLexicalHandler() != null) {
/* 251:258 */       getLexicalHandler().endEntity(this.reader.getLocalName());
/* 252:    */     }
/* 253:    */   }
/* 254:    */   
/* 255:    */   private void handleEndDocument()
/* 256:    */     throws SAXException
/* 257:    */   {
/* 258:263 */     if (getContentHandler() != null) {
/* 259:264 */       getContentHandler().endDocument();
/* 260:    */     }
/* 261:    */   }
/* 262:    */   
/* 263:    */   private void handleProcessingInstruction()
/* 264:    */     throws SAXException
/* 265:    */   {
/* 266:269 */     if (getContentHandler() != null) {
/* 267:270 */       getContentHandler().processingInstruction(this.reader.getPITarget(), this.reader.getPIData());
/* 268:    */     }
/* 269:    */   }
/* 270:    */   
/* 271:    */   private Attributes getAttributes()
/* 272:    */   {
/* 273:275 */     AttributesImpl attributes = new AttributesImpl();
/* 274:277 */     for (int i = 0; i < this.reader.getAttributeCount(); i++)
/* 275:    */     {
/* 276:278 */       String namespace = this.reader.getAttributeNamespace(i);
/* 277:279 */       if ((namespace == null) || (!hasNamespacesFeature())) {
/* 278:280 */         namespace = "";
/* 279:    */       }
/* 280:282 */       String type = this.reader.getAttributeType(i);
/* 281:283 */       if (type == null) {
/* 282:284 */         type = "CDATA";
/* 283:    */       }
/* 284:286 */       attributes.addAttribute(namespace, this.reader.getAttributeLocalName(i), 
/* 285:287 */         toQualifiedName(this.reader.getAttributeName(i)), type, this.reader.getAttributeValue(i));
/* 286:    */     }
/* 287:289 */     if (hasNamespacePrefixesFeature()) {
/* 288:290 */       for (int i = 0; i < this.reader.getNamespaceCount(); i++)
/* 289:    */       {
/* 290:291 */         String prefix = this.reader.getNamespacePrefix(i);
/* 291:292 */         String namespaceUri = this.reader.getNamespaceURI(i);
/* 292:    */         String qName;
/* 293:    */         String qName;
/* 294:294 */         if (StringUtils.hasLength(prefix)) {
/* 295:295 */           qName = "xmlns:" + prefix;
/* 296:    */         } else {
/* 297:298 */           qName = "xmlns";
/* 298:    */         }
/* 299:300 */         attributes.addAttribute("", "", qName, "CDATA", namespaceUri);
/* 300:    */       }
/* 301:    */     }
/* 302:304 */     return attributes;
/* 303:    */   }
/* 304:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.xml.StaxStreamXMLReader
 * JD-Core Version:    0.7.0.1
 */