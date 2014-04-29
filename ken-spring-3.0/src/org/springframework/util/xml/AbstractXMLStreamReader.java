/*   1:    */ package org.springframework.util.xml;
/*   2:    */ 
/*   3:    */ import javax.xml.namespace.NamespaceContext;
/*   4:    */ import javax.xml.namespace.QName;
/*   5:    */ import javax.xml.stream.XMLStreamException;
/*   6:    */ import javax.xml.stream.XMLStreamReader;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ 
/*   9:    */ abstract class AbstractXMLStreamReader
/*  10:    */   implements XMLStreamReader
/*  11:    */ {
/*  12:    */   public String getElementText()
/*  13:    */     throws XMLStreamException
/*  14:    */   {
/*  15: 35 */     if (getEventType() != 1) {
/*  16: 36 */       throw new XMLStreamException("parser must be on START_ELEMENT to read next text", getLocation());
/*  17:    */     }
/*  18: 38 */     int eventType = next();
/*  19: 39 */     StringBuilder builder = new StringBuilder();
/*  20: 40 */     while (eventType != 2)
/*  21:    */     {
/*  22: 41 */       if ((eventType == 4) || (eventType == 12) || 
/*  23: 42 */         (eventType == 6) || (eventType == 9))
/*  24:    */       {
/*  25: 43 */         builder.append(getText());
/*  26:    */       }
/*  27: 45 */       else if ((eventType != 3) && 
/*  28: 46 */         (eventType != 5))
/*  29:    */       {
/*  30: 49 */         if (eventType == 8) {
/*  31: 50 */           throw new XMLStreamException("unexpected end of document when reading element text content", 
/*  32: 51 */             getLocation());
/*  33:    */         }
/*  34: 53 */         if (eventType == 1) {
/*  35: 54 */           throw new XMLStreamException("element text content may not contain START_ELEMENT", getLocation());
/*  36:    */         }
/*  37: 57 */         throw new XMLStreamException("Unexpected event type " + eventType, getLocation());
/*  38:    */       }
/*  39: 59 */       eventType = next();
/*  40:    */     }
/*  41: 61 */     return builder.toString();
/*  42:    */   }
/*  43:    */   
/*  44:    */   public String getAttributeLocalName(int index)
/*  45:    */   {
/*  46: 65 */     return getAttributeName(index).getLocalPart();
/*  47:    */   }
/*  48:    */   
/*  49:    */   public String getAttributeNamespace(int index)
/*  50:    */   {
/*  51: 69 */     return getAttributeName(index).getNamespaceURI();
/*  52:    */   }
/*  53:    */   
/*  54:    */   public String getAttributePrefix(int index)
/*  55:    */   {
/*  56: 73 */     return getAttributeName(index).getPrefix();
/*  57:    */   }
/*  58:    */   
/*  59:    */   public String getNamespaceURI()
/*  60:    */   {
/*  61: 77 */     int eventType = getEventType();
/*  62: 78 */     if ((eventType == 1) || (eventType == 2)) {
/*  63: 79 */       return getName().getNamespaceURI();
/*  64:    */     }
/*  65: 82 */     throw new IllegalStateException("parser must be on START_ELEMENT or END_ELEMENT state");
/*  66:    */   }
/*  67:    */   
/*  68:    */   public String getNamespaceURI(String prefix)
/*  69:    */   {
/*  70: 87 */     Assert.notNull(prefix, "No prefix given");
/*  71: 88 */     return getNamespaceContext().getNamespaceURI(prefix);
/*  72:    */   }
/*  73:    */   
/*  74:    */   public boolean hasText()
/*  75:    */   {
/*  76: 92 */     int eventType = getEventType();
/*  77:    */     
/*  78:    */ 
/*  79: 95 */     return (eventType == 6) || (eventType == 4) || (eventType == 5) || (eventType == 12) || (eventType == 9);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public String getPrefix()
/*  83:    */   {
/*  84: 99 */     int eventType = getEventType();
/*  85:100 */     if ((eventType == 1) || (eventType == 2)) {
/*  86:101 */       return getName().getPrefix();
/*  87:    */     }
/*  88:104 */     throw new IllegalStateException("parser must be on START_ELEMENT or END_ELEMENT state");
/*  89:    */   }
/*  90:    */   
/*  91:    */   public boolean hasName()
/*  92:    */   {
/*  93:109 */     int eventType = getEventType();
/*  94:110 */     return (eventType == 1) || (eventType == 2);
/*  95:    */   }
/*  96:    */   
/*  97:    */   public boolean isWhiteSpace()
/*  98:    */   {
/*  99:114 */     return getEventType() == 6;
/* 100:    */   }
/* 101:    */   
/* 102:    */   public boolean isStartElement()
/* 103:    */   {
/* 104:118 */     return getEventType() == 1;
/* 105:    */   }
/* 106:    */   
/* 107:    */   public boolean isEndElement()
/* 108:    */   {
/* 109:122 */     return getEventType() == 2;
/* 110:    */   }
/* 111:    */   
/* 112:    */   public boolean isCharacters()
/* 113:    */   {
/* 114:126 */     return getEventType() == 4;
/* 115:    */   }
/* 116:    */   
/* 117:    */   public int nextTag()
/* 118:    */     throws XMLStreamException
/* 119:    */   {
/* 120:130 */     int eventType = next();
/* 121:131 */     while (((eventType == 4) && (isWhiteSpace())) || 
/* 122:132 */       ((eventType == 12) && (isWhiteSpace())) || (eventType == 6) || 
/* 123:133 */       (eventType == 3) || (eventType == 5)) {
/* 124:134 */       eventType = next();
/* 125:    */     }
/* 126:136 */     if ((eventType != 1) && (eventType != 2)) {
/* 127:137 */       throw new XMLStreamException("expected start or end tag", getLocation());
/* 128:    */     }
/* 129:139 */     return eventType;
/* 130:    */   }
/* 131:    */   
/* 132:    */   public void require(int expectedType, String namespaceURI, String localName)
/* 133:    */     throws XMLStreamException
/* 134:    */   {
/* 135:143 */     int eventType = getEventType();
/* 136:144 */     if (eventType != expectedType) {
/* 137:145 */       throw new XMLStreamException("Expected [" + expectedType + "] but read [" + eventType + "]");
/* 138:    */     }
/* 139:    */   }
/* 140:    */   
/* 141:    */   public String getAttributeValue(String namespaceURI, String localName)
/* 142:    */   {
/* 143:150 */     for (int i = 0; i < getAttributeCount(); i++)
/* 144:    */     {
/* 145:151 */       QName name = getAttributeName(i);
/* 146:152 */       if ((name.getLocalPart().equals(localName)) && (
/* 147:153 */         (namespaceURI == null) || (name.getNamespaceURI().equals(namespaceURI)))) {
/* 148:154 */         return getAttributeValue(i);
/* 149:    */       }
/* 150:    */     }
/* 151:157 */     return null;
/* 152:    */   }
/* 153:    */   
/* 154:    */   public boolean hasNext()
/* 155:    */     throws XMLStreamException
/* 156:    */   {
/* 157:161 */     return getEventType() != 8;
/* 158:    */   }
/* 159:    */   
/* 160:    */   public String getLocalName()
/* 161:    */   {
/* 162:165 */     return getName().getLocalPart();
/* 163:    */   }
/* 164:    */   
/* 165:    */   public char[] getTextCharacters()
/* 166:    */   {
/* 167:169 */     return getText().toCharArray();
/* 168:    */   }
/* 169:    */   
/* 170:    */   public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length)
/* 171:    */     throws XMLStreamException
/* 172:    */   {
/* 173:174 */     char[] source = getTextCharacters();
/* 174:175 */     length = Math.min(length, source.length);
/* 175:176 */     System.arraycopy(source, sourceStart, target, targetStart, length);
/* 176:177 */     return length;
/* 177:    */   }
/* 178:    */   
/* 179:    */   public int getTextLength()
/* 180:    */   {
/* 181:181 */     return getText().length();
/* 182:    */   }
/* 183:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.xml.AbstractXMLStreamReader
 * JD-Core Version:    0.7.0.1
 */