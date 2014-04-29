/*   1:    */ package org.springframework.util.xml;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.Iterator;
/*   5:    */ import java.util.List;
/*   6:    */ import javax.xml.namespace.QName;
/*   7:    */ import javax.xml.stream.Location;
/*   8:    */ import javax.xml.stream.XMLEventFactory;
/*   9:    */ import javax.xml.stream.XMLStreamException;
/*  10:    */ import javax.xml.stream.events.Attribute;
/*  11:    */ import javax.xml.stream.events.Namespace;
/*  12:    */ import javax.xml.stream.events.XMLEvent;
/*  13:    */ import javax.xml.stream.util.XMLEventConsumer;
/*  14:    */ import org.springframework.util.Assert;
/*  15:    */ import org.springframework.util.StringUtils;
/*  16:    */ import org.xml.sax.Attributes;
/*  17:    */ import org.xml.sax.Locator;
/*  18:    */ 
/*  19:    */ class StaxEventContentHandler
/*  20:    */   extends AbstractStaxContentHandler
/*  21:    */ {
/*  22:    */   private final XMLEventFactory eventFactory;
/*  23:    */   private final XMLEventConsumer eventConsumer;
/*  24:    */   
/*  25:    */   StaxEventContentHandler(XMLEventConsumer consumer)
/*  26:    */   {
/*  27: 60 */     Assert.notNull(consumer, "'consumer' must not be null");
/*  28: 61 */     this.eventFactory = XMLEventFactory.newInstance();
/*  29: 62 */     this.eventConsumer = consumer;
/*  30:    */   }
/*  31:    */   
/*  32:    */   StaxEventContentHandler(XMLEventConsumer consumer, XMLEventFactory factory)
/*  33:    */   {
/*  34: 73 */     this.eventFactory = factory;
/*  35: 74 */     this.eventConsumer = consumer;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setDocumentLocator(final Locator locator)
/*  39:    */   {
/*  40: 78 */     if (locator != null) {
/*  41: 79 */       this.eventFactory.setLocation(new Location()
/*  42:    */       {
/*  43:    */         public int getLineNumber()
/*  44:    */         {
/*  45: 82 */           return locator.getLineNumber();
/*  46:    */         }
/*  47:    */         
/*  48:    */         public int getColumnNumber()
/*  49:    */         {
/*  50: 86 */           return locator.getColumnNumber();
/*  51:    */         }
/*  52:    */         
/*  53:    */         public int getCharacterOffset()
/*  54:    */         {
/*  55: 90 */           return -1;
/*  56:    */         }
/*  57:    */         
/*  58:    */         public String getPublicId()
/*  59:    */         {
/*  60: 94 */           return locator.getPublicId();
/*  61:    */         }
/*  62:    */         
/*  63:    */         public String getSystemId()
/*  64:    */         {
/*  65: 98 */           return locator.getSystemId();
/*  66:    */         }
/*  67:    */       });
/*  68:    */     }
/*  69:    */   }
/*  70:    */   
/*  71:    */   protected void startDocumentInternal()
/*  72:    */     throws XMLStreamException
/*  73:    */   {
/*  74:106 */     consumeEvent(this.eventFactory.createStartDocument());
/*  75:    */   }
/*  76:    */   
/*  77:    */   protected void endDocumentInternal()
/*  78:    */     throws XMLStreamException
/*  79:    */   {
/*  80:111 */     consumeEvent(this.eventFactory.createEndDocument());
/*  81:    */   }
/*  82:    */   
/*  83:    */   protected void startElementInternal(QName name, Attributes atts, SimpleNamespaceContext namespaceContext)
/*  84:    */     throws XMLStreamException
/*  85:    */   {
/*  86:117 */     List attributes = getAttributes(atts);
/*  87:118 */     List namespaces = createNamespaces(namespaceContext);
/*  88:119 */     consumeEvent(this.eventFactory.createStartElement(name, attributes.iterator(), namespaces != null ? namespaces.iterator() : null));
/*  89:    */   }
/*  90:    */   
/*  91:    */   protected void endElementInternal(QName name, SimpleNamespaceContext namespaceContext)
/*  92:    */     throws XMLStreamException
/*  93:    */   {
/*  94:124 */     List namespaces = createNamespaces(namespaceContext);
/*  95:125 */     consumeEvent(this.eventFactory.createEndElement(name, namespaces != null ? namespaces.iterator() : null));
/*  96:    */   }
/*  97:    */   
/*  98:    */   protected void charactersInternal(char[] ch, int start, int length)
/*  99:    */     throws XMLStreamException
/* 100:    */   {
/* 101:130 */     consumeEvent(this.eventFactory.createCharacters(new String(ch, start, length)));
/* 102:    */   }
/* 103:    */   
/* 104:    */   protected void ignorableWhitespaceInternal(char[] ch, int start, int length)
/* 105:    */     throws XMLStreamException
/* 106:    */   {
/* 107:135 */     consumeEvent(this.eventFactory.createIgnorableSpace(new String(ch, start, length)));
/* 108:    */   }
/* 109:    */   
/* 110:    */   protected void processingInstructionInternal(String target, String data)
/* 111:    */     throws XMLStreamException
/* 112:    */   {
/* 113:140 */     consumeEvent(this.eventFactory.createProcessingInstruction(target, data));
/* 114:    */   }
/* 115:    */   
/* 116:    */   private void consumeEvent(XMLEvent event)
/* 117:    */     throws XMLStreamException
/* 118:    */   {
/* 119:144 */     this.eventConsumer.add(event);
/* 120:    */   }
/* 121:    */   
/* 122:    */   private List<Namespace> createNamespaces(SimpleNamespaceContext namespaceContext)
/* 123:    */   {
/* 124:149 */     if (namespaceContext == null) {
/* 125:150 */       return null;
/* 126:    */     }
/* 127:153 */     List<Namespace> namespaces = new ArrayList();
/* 128:154 */     String defaultNamespaceUri = namespaceContext.getNamespaceURI("");
/* 129:155 */     if (StringUtils.hasLength(defaultNamespaceUri)) {
/* 130:156 */       namespaces.add(this.eventFactory.createNamespace(defaultNamespaceUri));
/* 131:    */     }
/* 132:158 */     for (Iterator iterator = namespaceContext.getBoundPrefixes(); iterator.hasNext();)
/* 133:    */     {
/* 134:159 */       String prefix = (String)iterator.next();
/* 135:160 */       String namespaceUri = namespaceContext.getNamespaceURI(prefix);
/* 136:161 */       namespaces.add(this.eventFactory.createNamespace(prefix, namespaceUri));
/* 137:    */     }
/* 138:163 */     return namespaces;
/* 139:    */   }
/* 140:    */   
/* 141:    */   private List<Attribute> getAttributes(Attributes attributes)
/* 142:    */   {
/* 143:167 */     List<Attribute> list = new ArrayList();
/* 144:168 */     for (int i = 0; i < attributes.getLength(); i++)
/* 145:    */     {
/* 146:169 */       QName name = toQName(attributes.getURI(i), attributes.getQName(i));
/* 147:170 */       if ((!"xmlns".equals(name.getLocalPart())) && (!"xmlns".equals(name.getPrefix()))) {
/* 148:171 */         list.add(this.eventFactory.createAttribute(name, attributes.getValue(i)));
/* 149:    */       }
/* 150:    */     }
/* 151:174 */     return list;
/* 152:    */   }
/* 153:    */   
/* 154:    */   protected void skippedEntityInternal(String name)
/* 155:    */     throws XMLStreamException
/* 156:    */   {}
/* 157:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.xml.StaxEventContentHandler
 * JD-Core Version:    0.7.0.1
 */