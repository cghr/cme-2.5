/*   1:    */ package org.springframework.util.xml;
/*   2:    */ 
/*   3:    */ import java.util.Iterator;
/*   4:    */ import javax.xml.namespace.NamespaceContext;
/*   5:    */ import javax.xml.namespace.QName;
/*   6:    */ import javax.xml.stream.Location;
/*   7:    */ import javax.xml.stream.XMLEventReader;
/*   8:    */ import javax.xml.stream.XMLStreamException;
/*   9:    */ import javax.xml.stream.events.Attribute;
/*  10:    */ import javax.xml.stream.events.Characters;
/*  11:    */ import javax.xml.stream.events.Comment;
/*  12:    */ import javax.xml.stream.events.EndElement;
/*  13:    */ import javax.xml.stream.events.Namespace;
/*  14:    */ import javax.xml.stream.events.ProcessingInstruction;
/*  15:    */ import javax.xml.stream.events.StartDocument;
/*  16:    */ import javax.xml.stream.events.StartElement;
/*  17:    */ import javax.xml.stream.events.XMLEvent;
/*  18:    */ 
/*  19:    */ class XMLEventStreamReader
/*  20:    */   extends AbstractXMLStreamReader
/*  21:    */ {
/*  22:    */   private XMLEvent event;
/*  23:    */   private final XMLEventReader eventReader;
/*  24:    */   
/*  25:    */   XMLEventStreamReader(XMLEventReader eventReader)
/*  26:    */     throws XMLStreamException
/*  27:    */   {
/*  28: 48 */     this.eventReader = eventReader;
/*  29: 49 */     this.event = eventReader.nextEvent();
/*  30:    */   }
/*  31:    */   
/*  32:    */   public boolean isStandalone()
/*  33:    */   {
/*  34: 53 */     if (this.event.isStartDocument()) {
/*  35: 54 */       return ((StartDocument)this.event).isStandalone();
/*  36:    */     }
/*  37: 57 */     throw new IllegalStateException();
/*  38:    */   }
/*  39:    */   
/*  40:    */   public String getVersion()
/*  41:    */   {
/*  42: 62 */     if (this.event.isStartDocument()) {
/*  43: 63 */       return ((StartDocument)this.event).getVersion();
/*  44:    */     }
/*  45: 66 */     return null;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public int getTextStart()
/*  49:    */   {
/*  50: 71 */     return 0;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public String getText()
/*  54:    */   {
/*  55: 75 */     if (this.event.isCharacters()) {
/*  56: 76 */       return this.event.asCharacters().getData();
/*  57:    */     }
/*  58: 78 */     if (this.event.getEventType() == 5) {
/*  59: 79 */       return ((Comment)this.event).getText();
/*  60:    */     }
/*  61: 82 */     throw new IllegalStateException();
/*  62:    */   }
/*  63:    */   
/*  64:    */   public String getPITarget()
/*  65:    */   {
/*  66: 87 */     if (this.event.isProcessingInstruction()) {
/*  67: 88 */       return ((ProcessingInstruction)this.event).getTarget();
/*  68:    */     }
/*  69: 91 */     throw new IllegalStateException();
/*  70:    */   }
/*  71:    */   
/*  72:    */   public String getPIData()
/*  73:    */   {
/*  74: 96 */     if (this.event.isProcessingInstruction()) {
/*  75: 97 */       return ((ProcessingInstruction)this.event).getData();
/*  76:    */     }
/*  77:100 */     throw new IllegalStateException();
/*  78:    */   }
/*  79:    */   
/*  80:    */   public int getNamespaceCount()
/*  81:    */   {
/*  82:    */     Iterator namespaces;
/*  83:106 */     if (this.event.isStartElement())
/*  84:    */     {
/*  85:107 */       namespaces = this.event.asStartElement().getNamespaces();
/*  86:    */     }
/*  87:    */     else
/*  88:    */     {
/*  89:    */       Iterator namespaces;
/*  90:109 */       if (this.event.isEndElement()) {
/*  91:110 */         namespaces = this.event.asEndElement().getNamespaces();
/*  92:    */       } else {
/*  93:113 */         throw new IllegalStateException();
/*  94:    */       }
/*  95:    */     }
/*  96:    */     Iterator namespaces;
/*  97:115 */     return countIterator(namespaces);
/*  98:    */   }
/*  99:    */   
/* 100:    */   public NamespaceContext getNamespaceContext()
/* 101:    */   {
/* 102:119 */     if (this.event.isStartElement()) {
/* 103:120 */       return this.event.asStartElement().getNamespaceContext();
/* 104:    */     }
/* 105:123 */     throw new IllegalStateException();
/* 106:    */   }
/* 107:    */   
/* 108:    */   public QName getName()
/* 109:    */   {
/* 110:128 */     if (this.event.isStartElement()) {
/* 111:129 */       return this.event.asStartElement().getName();
/* 112:    */     }
/* 113:131 */     if (this.event.isEndElement()) {
/* 114:132 */       return this.event.asEndElement().getName();
/* 115:    */     }
/* 116:135 */     throw new IllegalStateException();
/* 117:    */   }
/* 118:    */   
/* 119:    */   public Location getLocation()
/* 120:    */   {
/* 121:140 */     return this.event.getLocation();
/* 122:    */   }
/* 123:    */   
/* 124:    */   public int getEventType()
/* 125:    */   {
/* 126:144 */     return this.event.getEventType();
/* 127:    */   }
/* 128:    */   
/* 129:    */   public String getEncoding()
/* 130:    */   {
/* 131:148 */     return null;
/* 132:    */   }
/* 133:    */   
/* 134:    */   public String getCharacterEncodingScheme()
/* 135:    */   {
/* 136:152 */     return null;
/* 137:    */   }
/* 138:    */   
/* 139:    */   public int getAttributeCount()
/* 140:    */   {
/* 141:156 */     if (!this.event.isStartElement()) {
/* 142:157 */       throw new IllegalStateException();
/* 143:    */     }
/* 144:159 */     Iterator attributes = this.event.asStartElement().getAttributes();
/* 145:160 */     return countIterator(attributes);
/* 146:    */   }
/* 147:    */   
/* 148:    */   public void close()
/* 149:    */     throws XMLStreamException
/* 150:    */   {
/* 151:164 */     this.eventReader.close();
/* 152:    */   }
/* 153:    */   
/* 154:    */   public QName getAttributeName(int index)
/* 155:    */   {
/* 156:168 */     return getAttribute(index).getName();
/* 157:    */   }
/* 158:    */   
/* 159:    */   public String getAttributeType(int index)
/* 160:    */   {
/* 161:172 */     return getAttribute(index).getDTDType();
/* 162:    */   }
/* 163:    */   
/* 164:    */   public String getAttributeValue(int index)
/* 165:    */   {
/* 166:176 */     return getAttribute(index).getValue();
/* 167:    */   }
/* 168:    */   
/* 169:    */   public String getNamespacePrefix(int index)
/* 170:    */   {
/* 171:180 */     return getNamespace(index).getPrefix();
/* 172:    */   }
/* 173:    */   
/* 174:    */   public String getNamespaceURI(int index)
/* 175:    */   {
/* 176:184 */     return getNamespace(index).getNamespaceURI();
/* 177:    */   }
/* 178:    */   
/* 179:    */   public Object getProperty(String name)
/* 180:    */     throws IllegalArgumentException
/* 181:    */   {
/* 182:188 */     return this.eventReader.getProperty(name);
/* 183:    */   }
/* 184:    */   
/* 185:    */   public boolean isAttributeSpecified(int index)
/* 186:    */   {
/* 187:192 */     return getAttribute(index).isSpecified();
/* 188:    */   }
/* 189:    */   
/* 190:    */   public int next()
/* 191:    */     throws XMLStreamException
/* 192:    */   {
/* 193:196 */     this.event = this.eventReader.nextEvent();
/* 194:197 */     return this.event.getEventType();
/* 195:    */   }
/* 196:    */   
/* 197:    */   public boolean standaloneSet()
/* 198:    */   {
/* 199:201 */     if (this.event.isStartDocument()) {
/* 200:202 */       return ((StartDocument)this.event).standaloneSet();
/* 201:    */     }
/* 202:205 */     throw new IllegalStateException();
/* 203:    */   }
/* 204:    */   
/* 205:    */   private int countIterator(Iterator iterator)
/* 206:    */   {
/* 207:210 */     int count = 0;
/* 208:211 */     while (iterator.hasNext())
/* 209:    */     {
/* 210:212 */       iterator.next();
/* 211:213 */       count++;
/* 212:    */     }
/* 213:215 */     return count;
/* 214:    */   }
/* 215:    */   
/* 216:    */   private Attribute getAttribute(int index)
/* 217:    */   {
/* 218:219 */     if (!this.event.isStartElement()) {
/* 219:220 */       throw new IllegalStateException();
/* 220:    */     }
/* 221:222 */     int count = 0;
/* 222:223 */     Iterator attributes = this.event.asStartElement().getAttributes();
/* 223:224 */     while (attributes.hasNext())
/* 224:    */     {
/* 225:225 */       Attribute attribute = (Attribute)attributes.next();
/* 226:226 */       if (count == index) {
/* 227:227 */         return attribute;
/* 228:    */       }
/* 229:230 */       count++;
/* 230:    */     }
/* 231:233 */     throw new IllegalArgumentException();
/* 232:    */   }
/* 233:    */   
/* 234:    */   private Namespace getNamespace(int index)
/* 235:    */   {
/* 236:    */     Iterator namespaces;
/* 237:238 */     if (this.event.isStartElement())
/* 238:    */     {
/* 239:239 */       namespaces = this.event.asStartElement().getNamespaces();
/* 240:    */     }
/* 241:    */     else
/* 242:    */     {
/* 243:    */       Iterator namespaces;
/* 244:241 */       if (this.event.isEndElement()) {
/* 245:242 */         namespaces = this.event.asEndElement().getNamespaces();
/* 246:    */       } else {
/* 247:245 */         throw new IllegalStateException();
/* 248:    */       }
/* 249:    */     }
/* 250:    */     Iterator namespaces;
/* 251:247 */     int count = 0;
/* 252:248 */     while (namespaces.hasNext())
/* 253:    */     {
/* 254:249 */       Namespace namespace = (Namespace)namespaces.next();
/* 255:250 */       if (count == index) {
/* 256:251 */         return namespace;
/* 257:    */       }
/* 258:254 */       count++;
/* 259:    */     }
/* 260:257 */     throw new IllegalArgumentException();
/* 261:    */   }
/* 262:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.xml.XMLEventStreamReader
 * JD-Core Version:    0.7.0.1
 */