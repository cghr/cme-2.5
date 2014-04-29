/*   1:    */ package org.springframework.util.xml;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.List;
/*   6:    */ import javax.xml.namespace.NamespaceContext;
/*   7:    */ import javax.xml.namespace.QName;
/*   8:    */ import javax.xml.stream.XMLEventFactory;
/*   9:    */ import javax.xml.stream.XMLEventWriter;
/*  10:    */ import javax.xml.stream.XMLStreamException;
/*  11:    */ import javax.xml.stream.XMLStreamWriter;
/*  12:    */ import javax.xml.stream.events.EndElement;
/*  13:    */ import javax.xml.stream.events.Namespace;
/*  14:    */ import javax.xml.stream.events.StartElement;
/*  15:    */ import org.springframework.util.Assert;
/*  16:    */ 
/*  17:    */ class XMLEventStreamWriter
/*  18:    */   implements XMLStreamWriter
/*  19:    */ {
/*  20:    */   private static final String DEFAULT_ENCODING = "UTF-8";
/*  21:    */   private final XMLEventWriter eventWriter;
/*  22:    */   private final XMLEventFactory eventFactory;
/*  23: 49 */   private List<EndElement> endElements = new ArrayList();
/*  24:    */   
/*  25:    */   public XMLEventStreamWriter(XMLEventWriter eventWriter, XMLEventFactory eventFactory)
/*  26:    */   {
/*  27: 52 */     Assert.notNull(eventWriter, "'eventWriter' must not be null");
/*  28: 53 */     Assert.notNull(eventFactory, "'eventFactory' must not be null");
/*  29:    */     
/*  30: 55 */     this.eventWriter = eventWriter;
/*  31: 56 */     this.eventFactory = eventFactory;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public NamespaceContext getNamespaceContext()
/*  35:    */   {
/*  36: 60 */     return this.eventWriter.getNamespaceContext();
/*  37:    */   }
/*  38:    */   
/*  39:    */   public String getPrefix(String uri)
/*  40:    */     throws XMLStreamException
/*  41:    */   {
/*  42: 64 */     return this.eventWriter.getPrefix(uri);
/*  43:    */   }
/*  44:    */   
/*  45:    */   public void setPrefix(String prefix, String uri)
/*  46:    */     throws XMLStreamException
/*  47:    */   {
/*  48: 68 */     this.eventWriter.setPrefix(prefix, uri);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void setDefaultNamespace(String uri)
/*  52:    */     throws XMLStreamException
/*  53:    */   {
/*  54: 72 */     this.eventWriter.setDefaultNamespace(uri);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void setNamespaceContext(NamespaceContext context)
/*  58:    */     throws XMLStreamException
/*  59:    */   {
/*  60: 76 */     this.eventWriter.setNamespaceContext(context);
/*  61:    */   }
/*  62:    */   
/*  63:    */   public void writeStartDocument()
/*  64:    */     throws XMLStreamException
/*  65:    */   {
/*  66: 80 */     this.eventWriter.add(this.eventFactory.createStartDocument());
/*  67:    */   }
/*  68:    */   
/*  69:    */   public void writeStartDocument(String version)
/*  70:    */     throws XMLStreamException
/*  71:    */   {
/*  72: 84 */     this.eventWriter.add(this.eventFactory.createStartDocument("UTF-8", version));
/*  73:    */   }
/*  74:    */   
/*  75:    */   public void writeStartDocument(String encoding, String version)
/*  76:    */     throws XMLStreamException
/*  77:    */   {
/*  78: 88 */     this.eventWriter.add(this.eventFactory.createStartDocument(encoding, version));
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void writeStartElement(String localName)
/*  82:    */     throws XMLStreamException
/*  83:    */   {
/*  84: 92 */     writeStartElement(this.eventFactory.createStartElement(new QName(localName), null, null));
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void writeStartElement(String namespaceURI, String localName)
/*  88:    */     throws XMLStreamException
/*  89:    */   {
/*  90: 96 */     writeStartElement(this.eventFactory.createStartElement(new QName(namespaceURI, localName), null, null));
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void writeStartElement(String prefix, String localName, String namespaceURI)
/*  94:    */     throws XMLStreamException
/*  95:    */   {
/*  96:100 */     writeStartElement(this.eventFactory.createStartElement(new QName(namespaceURI, localName, prefix), null, null));
/*  97:    */   }
/*  98:    */   
/*  99:    */   public void writeEmptyElement(String localName)
/* 100:    */     throws XMLStreamException
/* 101:    */   {
/* 102:104 */     writeStartElement(localName);
/* 103:105 */     writeEndElement();
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void writeEmptyElement(String namespaceURI, String localName)
/* 107:    */     throws XMLStreamException
/* 108:    */   {
/* 109:109 */     writeStartElement(namespaceURI, localName);
/* 110:110 */     writeEndElement();
/* 111:    */   }
/* 112:    */   
/* 113:    */   public void writeEmptyElement(String prefix, String localName, String namespaceURI)
/* 114:    */     throws XMLStreamException
/* 115:    */   {
/* 116:114 */     writeStartElement(prefix, localName, namespaceURI);
/* 117:115 */     writeEndElement();
/* 118:    */   }
/* 119:    */   
/* 120:    */   public void writeEndElement()
/* 121:    */     throws XMLStreamException
/* 122:    */   {
/* 123:119 */     int last = this.endElements.size() - 1;
/* 124:120 */     EndElement lastEndElement = (EndElement)this.endElements.get(last);
/* 125:121 */     this.eventWriter.add(lastEndElement);
/* 126:122 */     this.endElements.remove(last);
/* 127:    */   }
/* 128:    */   
/* 129:    */   public void writeAttribute(String localName, String value)
/* 130:    */     throws XMLStreamException
/* 131:    */   {
/* 132:126 */     this.eventWriter.add(this.eventFactory.createAttribute(localName, value));
/* 133:    */   }
/* 134:    */   
/* 135:    */   public void writeAttribute(String namespaceURI, String localName, String value)
/* 136:    */     throws XMLStreamException
/* 137:    */   {
/* 138:130 */     this.eventWriter.add(this.eventFactory.createAttribute(new QName(namespaceURI, localName), value));
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void writeAttribute(String prefix, String namespaceURI, String localName, String value)
/* 142:    */     throws XMLStreamException
/* 143:    */   {
/* 144:135 */     this.eventWriter.add(this.eventFactory.createAttribute(prefix, namespaceURI, localName, value));
/* 145:    */   }
/* 146:    */   
/* 147:    */   public void writeNamespace(String prefix, String namespaceURI)
/* 148:    */     throws XMLStreamException
/* 149:    */   {
/* 150:139 */     writeNamespace(this.eventFactory.createNamespace(prefix, namespaceURI));
/* 151:    */   }
/* 152:    */   
/* 153:    */   public void writeDefaultNamespace(String namespaceURI)
/* 154:    */     throws XMLStreamException
/* 155:    */   {
/* 156:143 */     writeNamespace(this.eventFactory.createNamespace(namespaceURI));
/* 157:    */   }
/* 158:    */   
/* 159:    */   public void writeCharacters(String text)
/* 160:    */     throws XMLStreamException
/* 161:    */   {
/* 162:147 */     this.eventWriter.add(this.eventFactory.createCharacters(text));
/* 163:    */   }
/* 164:    */   
/* 165:    */   public void writeCharacters(char[] text, int start, int len)
/* 166:    */     throws XMLStreamException
/* 167:    */   {
/* 168:151 */     this.eventWriter.add(this.eventFactory.createCharacters(new String(text, start, len)));
/* 169:    */   }
/* 170:    */   
/* 171:    */   public void writeCData(String data)
/* 172:    */     throws XMLStreamException
/* 173:    */   {
/* 174:155 */     this.eventWriter.add(this.eventFactory.createCData(data));
/* 175:    */   }
/* 176:    */   
/* 177:    */   public void writeComment(String data)
/* 178:    */     throws XMLStreamException
/* 179:    */   {
/* 180:159 */     this.eventWriter.add(this.eventFactory.createComment(data));
/* 181:    */   }
/* 182:    */   
/* 183:    */   public void writeProcessingInstruction(String target)
/* 184:    */     throws XMLStreamException
/* 185:    */   {
/* 186:163 */     this.eventWriter.add(this.eventFactory.createProcessingInstruction(target, ""));
/* 187:    */   }
/* 188:    */   
/* 189:    */   public void writeProcessingInstruction(String target, String data)
/* 190:    */     throws XMLStreamException
/* 191:    */   {
/* 192:167 */     this.eventWriter.add(this.eventFactory.createProcessingInstruction(target, data));
/* 193:    */   }
/* 194:    */   
/* 195:    */   public void writeDTD(String dtd)
/* 196:    */     throws XMLStreamException
/* 197:    */   {
/* 198:171 */     this.eventWriter.add(this.eventFactory.createDTD(dtd));
/* 199:    */   }
/* 200:    */   
/* 201:    */   public void writeEntityRef(String name)
/* 202:    */     throws XMLStreamException
/* 203:    */   {
/* 204:175 */     this.eventWriter.add(this.eventFactory.createEntityReference(name, null));
/* 205:    */   }
/* 206:    */   
/* 207:    */   public void writeEndDocument()
/* 208:    */     throws XMLStreamException
/* 209:    */   {
/* 210:179 */     this.eventWriter.add(this.eventFactory.createEndDocument());
/* 211:    */   }
/* 212:    */   
/* 213:    */   public Object getProperty(String name)
/* 214:    */     throws IllegalArgumentException
/* 215:    */   {
/* 216:183 */     throw new IllegalArgumentException();
/* 217:    */   }
/* 218:    */   
/* 219:    */   public void flush()
/* 220:    */     throws XMLStreamException
/* 221:    */   {
/* 222:187 */     this.eventWriter.flush();
/* 223:    */   }
/* 224:    */   
/* 225:    */   public void close()
/* 226:    */     throws XMLStreamException
/* 227:    */   {
/* 228:191 */     this.eventWriter.close();
/* 229:    */   }
/* 230:    */   
/* 231:    */   private void writeStartElement(StartElement startElement)
/* 232:    */     throws XMLStreamException
/* 233:    */   {
/* 234:195 */     this.eventWriter.add(startElement);
/* 235:196 */     this.endElements.add(this.eventFactory.createEndElement(startElement.getName(), null));
/* 236:    */   }
/* 237:    */   
/* 238:    */   private void writeNamespace(Namespace namespace)
/* 239:    */     throws XMLStreamException
/* 240:    */   {
/* 241:200 */     int last = this.endElements.size() - 1;
/* 242:201 */     EndElement oldEndElement = (EndElement)this.endElements.get(last);
/* 243:202 */     Iterator oldNamespaces = oldEndElement.getNamespaces();
/* 244:203 */     List<Namespace> newNamespaces = new ArrayList();
/* 245:204 */     while (oldNamespaces.hasNext())
/* 246:    */     {
/* 247:205 */       Namespace oldNamespace = (Namespace)oldNamespaces.next();
/* 248:206 */       newNamespaces.add(oldNamespace);
/* 249:    */     }
/* 250:208 */     newNamespaces.add(namespace);
/* 251:209 */     EndElement newEndElement = this.eventFactory.createEndElement(oldEndElement.getName(), newNamespaces.iterator());
/* 252:210 */     this.eventWriter.add(namespace);
/* 253:211 */     this.endElements.set(last, newEndElement);
/* 254:    */   }
/* 255:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.xml.XMLEventStreamWriter
 * JD-Core Version:    0.7.0.1
 */