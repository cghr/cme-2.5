/*   1:    */ package org.springframework.util.xml;
/*   2:    */ 
/*   3:    */ import java.util.Iterator;
/*   4:    */ import java.util.LinkedHashMap;
/*   5:    */ import java.util.Map;
/*   6:    */ import javax.xml.namespace.QName;
/*   7:    */ import javax.xml.stream.Location;
/*   8:    */ import javax.xml.stream.XMLEventReader;
/*   9:    */ import javax.xml.stream.XMLStreamException;
/*  10:    */ import javax.xml.stream.events.Attribute;
/*  11:    */ import javax.xml.stream.events.Characters;
/*  12:    */ import javax.xml.stream.events.Comment;
/*  13:    */ import javax.xml.stream.events.DTD;
/*  14:    */ import javax.xml.stream.events.EndElement;
/*  15:    */ import javax.xml.stream.events.EntityDeclaration;
/*  16:    */ import javax.xml.stream.events.EntityReference;
/*  17:    */ import javax.xml.stream.events.Namespace;
/*  18:    */ import javax.xml.stream.events.NotationDeclaration;
/*  19:    */ import javax.xml.stream.events.ProcessingInstruction;
/*  20:    */ import javax.xml.stream.events.StartDocument;
/*  21:    */ import javax.xml.stream.events.StartElement;
/*  22:    */ import javax.xml.stream.events.XMLEvent;
/*  23:    */ import org.springframework.util.Assert;
/*  24:    */ import org.springframework.util.StringUtils;
/*  25:    */ import org.xml.sax.Attributes;
/*  26:    */ import org.xml.sax.ContentHandler;
/*  27:    */ import org.xml.sax.DTDHandler;
/*  28:    */ import org.xml.sax.SAXException;
/*  29:    */ import org.xml.sax.ext.LexicalHandler;
/*  30:    */ import org.xml.sax.ext.Locator2;
/*  31:    */ import org.xml.sax.helpers.AttributesImpl;
/*  32:    */ 
/*  33:    */ class StaxEventXMLReader
/*  34:    */   extends AbstractStaxXMLReader
/*  35:    */ {
/*  36:    */   private static final String DEFAULT_XML_VERSION = "1.0";
/*  37:    */   private final XMLEventReader reader;
/*  38: 67 */   private final Map<String, String> namespaces = new LinkedHashMap();
/*  39: 69 */   private String xmlVersion = "1.0";
/*  40:    */   private String encoding;
/*  41:    */   
/*  42:    */   StaxEventXMLReader(XMLEventReader reader)
/*  43:    */   {
/*  44: 82 */     Assert.notNull(reader, "'reader' must not be null");
/*  45:    */     try
/*  46:    */     {
/*  47: 84 */       XMLEvent event = reader.peek();
/*  48: 85 */       if ((event != null) && (!event.isStartDocument()) && (!event.isStartElement())) {
/*  49: 86 */         throw new IllegalStateException("XMLEventReader not at start of document or element");
/*  50:    */       }
/*  51:    */     }
/*  52:    */     catch (XMLStreamException ex)
/*  53:    */     {
/*  54: 90 */       throw new IllegalStateException("Could not read first element: " + ex.getMessage());
/*  55:    */     }
/*  56: 93 */     this.reader = reader;
/*  57:    */   }
/*  58:    */   
/*  59:    */   protected void parseInternal()
/*  60:    */     throws SAXException, XMLStreamException
/*  61:    */   {
/*  62: 98 */     boolean documentStarted = false;
/*  63: 99 */     boolean documentEnded = false;
/*  64:100 */     int elementDepth = 0;
/*  65:101 */     while ((this.reader.hasNext()) && (elementDepth >= 0))
/*  66:    */     {
/*  67:102 */       XMLEvent event = this.reader.nextEvent();
/*  68:103 */       if ((!event.isStartDocument()) && (!event.isEndDocument()) && (!documentStarted))
/*  69:    */       {
/*  70:104 */         handleStartDocument(event);
/*  71:105 */         documentStarted = true;
/*  72:    */       }
/*  73:107 */       switch (event.getEventType())
/*  74:    */       {
/*  75:    */       case 7: 
/*  76:109 */         handleStartDocument(event);
/*  77:110 */         documentStarted = true;
/*  78:111 */         break;
/*  79:    */       case 1: 
/*  80:113 */         elementDepth++;
/*  81:114 */         handleStartElement(event.asStartElement());
/*  82:115 */         break;
/*  83:    */       case 2: 
/*  84:117 */         elementDepth--;
/*  85:118 */         if (elementDepth >= 0) {
/*  86:119 */           handleEndElement(event.asEndElement());
/*  87:    */         }
/*  88:121 */         break;
/*  89:    */       case 3: 
/*  90:123 */         handleProcessingInstruction((ProcessingInstruction)event);
/*  91:124 */         break;
/*  92:    */       case 4: 
/*  93:    */       case 6: 
/*  94:    */       case 12: 
/*  95:128 */         handleCharacters(event.asCharacters());
/*  96:129 */         break;
/*  97:    */       case 8: 
/*  98:131 */         handleEndDocument();
/*  99:132 */         documentEnded = true;
/* 100:133 */         break;
/* 101:    */       case 14: 
/* 102:135 */         handleNotationDeclaration((NotationDeclaration)event);
/* 103:136 */         break;
/* 104:    */       case 15: 
/* 105:138 */         handleEntityDeclaration((EntityDeclaration)event);
/* 106:139 */         break;
/* 107:    */       case 5: 
/* 108:141 */         handleComment((Comment)event);
/* 109:142 */         break;
/* 110:    */       case 11: 
/* 111:144 */         handleDtd((DTD)event);
/* 112:145 */         break;
/* 113:    */       case 9: 
/* 114:147 */         handleEntityReference((EntityReference)event);
/* 115:    */       }
/* 116:    */     }
/* 117:151 */     if ((documentStarted) && (!documentEnded)) {
/* 118:152 */       handleEndDocument();
/* 119:    */     }
/* 120:    */   }
/* 121:    */   
/* 122:    */   private void handleStartDocument(XMLEvent event)
/* 123:    */     throws SAXException
/* 124:    */   {
/* 125:158 */     if (event.isStartDocument())
/* 126:    */     {
/* 127:159 */       StartDocument startDocument = (StartDocument)event;
/* 128:160 */       String xmlVersion = startDocument.getVersion();
/* 129:161 */       if (StringUtils.hasLength(xmlVersion)) {
/* 130:162 */         this.xmlVersion = xmlVersion;
/* 131:    */       }
/* 132:164 */       if (startDocument.encodingSet()) {
/* 133:165 */         this.encoding = startDocument.getCharacterEncodingScheme();
/* 134:    */       }
/* 135:    */     }
/* 136:169 */     if (getContentHandler() != null)
/* 137:    */     {
/* 138:170 */       final Location location = event.getLocation();
/* 139:171 */       getContentHandler().setDocumentLocator(new Locator2()
/* 140:    */       {
/* 141:    */         public int getColumnNumber()
/* 142:    */         {
/* 143:174 */           return location != null ? location.getColumnNumber() : -1;
/* 144:    */         }
/* 145:    */         
/* 146:    */         public int getLineNumber()
/* 147:    */         {
/* 148:178 */           return location != null ? location.getLineNumber() : -1;
/* 149:    */         }
/* 150:    */         
/* 151:    */         public String getPublicId()
/* 152:    */         {
/* 153:182 */           return location != null ? location.getPublicId() : null;
/* 154:    */         }
/* 155:    */         
/* 156:    */         public String getSystemId()
/* 157:    */         {
/* 158:186 */           return location != null ? location.getSystemId() : null;
/* 159:    */         }
/* 160:    */         
/* 161:    */         public String getXMLVersion()
/* 162:    */         {
/* 163:190 */           return StaxEventXMLReader.this.xmlVersion;
/* 164:    */         }
/* 165:    */         
/* 166:    */         public String getEncoding()
/* 167:    */         {
/* 168:194 */           return StaxEventXMLReader.this.encoding;
/* 169:    */         }
/* 170:197 */       });
/* 171:198 */       getContentHandler().startDocument();
/* 172:    */     }
/* 173:    */   }
/* 174:    */   
/* 175:    */   private void handleStartElement(StartElement startElement)
/* 176:    */     throws SAXException
/* 177:    */   {
/* 178:203 */     if (getContentHandler() != null)
/* 179:    */     {
/* 180:204 */       QName qName = startElement.getName();
/* 181:205 */       if (hasNamespacesFeature())
/* 182:    */       {
/* 183:206 */         for (Iterator i = startElement.getNamespaces(); i.hasNext();)
/* 184:    */         {
/* 185:207 */           Namespace namespace = (Namespace)i.next();
/* 186:208 */           startPrefixMapping(namespace.getPrefix(), namespace.getNamespaceURI());
/* 187:    */         }
/* 188:210 */         for (Iterator i = startElement.getAttributes(); i.hasNext();)
/* 189:    */         {
/* 190:211 */           Attribute attribute = (Attribute)i.next();
/* 191:212 */           QName attributeName = attribute.getName();
/* 192:213 */           startPrefixMapping(attributeName.getPrefix(), attributeName.getNamespaceURI());
/* 193:    */         }
/* 194:216 */         getContentHandler().startElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName), 
/* 195:217 */           getAttributes(startElement));
/* 196:    */       }
/* 197:    */       else
/* 198:    */       {
/* 199:220 */         getContentHandler().startElement("", "", toQualifiedName(qName), getAttributes(startElement));
/* 200:    */       }
/* 201:    */     }
/* 202:    */   }
/* 203:    */   
/* 204:    */   private void handleCharacters(Characters characters)
/* 205:    */     throws SAXException
/* 206:    */   {
/* 207:226 */     char[] data = characters.getData().toCharArray();
/* 208:227 */     if ((getContentHandler() != null) && (characters.isIgnorableWhiteSpace()))
/* 209:    */     {
/* 210:228 */       getContentHandler().ignorableWhitespace(data, 0, data.length);
/* 211:229 */       return;
/* 212:    */     }
/* 213:231 */     if ((characters.isCData()) && (getLexicalHandler() != null)) {
/* 214:232 */       getLexicalHandler().startCDATA();
/* 215:    */     }
/* 216:234 */     if (getContentHandler() != null) {
/* 217:235 */       getContentHandler().characters(data, 0, data.length);
/* 218:    */     }
/* 219:237 */     if ((characters.isCData()) && (getLexicalHandler() != null)) {
/* 220:238 */       getLexicalHandler().endCDATA();
/* 221:    */     }
/* 222:    */   }
/* 223:    */   
/* 224:    */   private void handleEndElement(EndElement endElement)
/* 225:    */     throws SAXException
/* 226:    */   {
/* 227:243 */     if (getContentHandler() != null)
/* 228:    */     {
/* 229:244 */       QName qName = endElement.getName();
/* 230:245 */       if (hasNamespacesFeature())
/* 231:    */       {
/* 232:246 */         getContentHandler().endElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName));
/* 233:247 */         for (Iterator i = endElement.getNamespaces(); i.hasNext();)
/* 234:    */         {
/* 235:248 */           Namespace namespace = (Namespace)i.next();
/* 236:249 */           endPrefixMapping(namespace.getPrefix());
/* 237:    */         }
/* 238:    */       }
/* 239:    */       else
/* 240:    */       {
/* 241:253 */         getContentHandler().endElement("", "", toQualifiedName(qName));
/* 242:    */       }
/* 243:    */     }
/* 244:    */   }
/* 245:    */   
/* 246:    */   private void handleEndDocument()
/* 247:    */     throws SAXException
/* 248:    */   {
/* 249:260 */     if (getContentHandler() != null) {
/* 250:261 */       getContentHandler().endDocument();
/* 251:    */     }
/* 252:    */   }
/* 253:    */   
/* 254:    */   private void handleNotationDeclaration(NotationDeclaration declaration)
/* 255:    */     throws SAXException
/* 256:    */   {
/* 257:266 */     if (getDTDHandler() != null) {
/* 258:267 */       getDTDHandler().notationDecl(declaration.getName(), declaration.getPublicId(), declaration.getSystemId());
/* 259:    */     }
/* 260:    */   }
/* 261:    */   
/* 262:    */   private void handleEntityDeclaration(EntityDeclaration entityDeclaration)
/* 263:    */     throws SAXException
/* 264:    */   {
/* 265:272 */     if (getDTDHandler() != null) {
/* 266:273 */       getDTDHandler().unparsedEntityDecl(entityDeclaration.getName(), entityDeclaration.getPublicId(), 
/* 267:274 */         entityDeclaration.getSystemId(), entityDeclaration.getNotationName());
/* 268:    */     }
/* 269:    */   }
/* 270:    */   
/* 271:    */   private void handleProcessingInstruction(ProcessingInstruction pi)
/* 272:    */     throws SAXException
/* 273:    */   {
/* 274:279 */     if (getContentHandler() != null) {
/* 275:280 */       getContentHandler().processingInstruction(pi.getTarget(), pi.getData());
/* 276:    */     }
/* 277:    */   }
/* 278:    */   
/* 279:    */   private void handleComment(Comment comment)
/* 280:    */     throws SAXException
/* 281:    */   {
/* 282:285 */     if (getLexicalHandler() != null)
/* 283:    */     {
/* 284:286 */       char[] ch = comment.getText().toCharArray();
/* 285:287 */       getLexicalHandler().comment(ch, 0, ch.length);
/* 286:    */     }
/* 287:    */   }
/* 288:    */   
/* 289:    */   private void handleDtd(DTD dtd)
/* 290:    */     throws SAXException
/* 291:    */   {
/* 292:292 */     if (getLexicalHandler() != null)
/* 293:    */     {
/* 294:293 */       Location location = dtd.getLocation();
/* 295:294 */       getLexicalHandler().startDTD(null, location.getPublicId(), location.getSystemId());
/* 296:    */     }
/* 297:296 */     if (getLexicalHandler() != null) {
/* 298:297 */       getLexicalHandler().endDTD();
/* 299:    */     }
/* 300:    */   }
/* 301:    */   
/* 302:    */   private void handleEntityReference(EntityReference reference)
/* 303:    */     throws SAXException
/* 304:    */   {
/* 305:303 */     if (getLexicalHandler() != null) {
/* 306:304 */       getLexicalHandler().startEntity(reference.getName());
/* 307:    */     }
/* 308:306 */     if (getLexicalHandler() != null) {
/* 309:307 */       getLexicalHandler().endEntity(reference.getName());
/* 310:    */     }
/* 311:    */   }
/* 312:    */   
/* 313:    */   private Attributes getAttributes(StartElement event)
/* 314:    */   {
/* 315:313 */     AttributesImpl attributes = new AttributesImpl();
/* 316:315 */     for (Iterator i = event.getAttributes(); i.hasNext();)
/* 317:    */     {
/* 318:316 */       Attribute attribute = (Attribute)i.next();
/* 319:317 */       QName qName = attribute.getName();
/* 320:318 */       String namespace = qName.getNamespaceURI();
/* 321:319 */       if ((namespace == null) || (!hasNamespacesFeature())) {
/* 322:320 */         namespace = "";
/* 323:    */       }
/* 324:322 */       String type = attribute.getDTDType();
/* 325:323 */       if (type == null) {
/* 326:324 */         type = "CDATA";
/* 327:    */       }
/* 328:327 */       attributes.addAttribute(namespace, qName.getLocalPart(), toQualifiedName(qName), type, attribute.getValue());
/* 329:    */     }
/* 330:329 */     if (hasNamespacePrefixesFeature()) {
/* 331:330 */       for (Iterator i = event.getNamespaces(); i.hasNext();)
/* 332:    */       {
/* 333:331 */         Namespace namespace = (Namespace)i.next();
/* 334:332 */         String prefix = namespace.getPrefix();
/* 335:333 */         String namespaceUri = namespace.getNamespaceURI();
/* 336:    */         String qName;
/* 337:    */         String qName;
/* 338:335 */         if (StringUtils.hasLength(prefix)) {
/* 339:336 */           qName = "xmlns:" + prefix;
/* 340:    */         } else {
/* 341:339 */           qName = "xmlns";
/* 342:    */         }
/* 343:341 */         attributes.addAttribute("", "", qName, "CDATA", namespaceUri);
/* 344:    */       }
/* 345:    */     }
/* 346:345 */     return attributes;
/* 347:    */   }
/* 348:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.xml.StaxEventXMLReader
 * JD-Core Version:    0.7.0.1
 */