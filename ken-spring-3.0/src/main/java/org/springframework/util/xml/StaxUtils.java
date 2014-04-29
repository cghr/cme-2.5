/*   1:    */ package org.springframework.util.xml;
/*   2:    */ 
/*   3:    */ import javax.xml.stream.XMLEventFactory;
/*   4:    */ import javax.xml.stream.XMLEventReader;
/*   5:    */ import javax.xml.stream.XMLEventWriter;
/*   6:    */ import javax.xml.stream.XMLStreamException;
/*   7:    */ import javax.xml.stream.XMLStreamReader;
/*   8:    */ import javax.xml.stream.XMLStreamWriter;
/*   9:    */ import javax.xml.transform.Result;
/*  10:    */ import javax.xml.transform.Source;
/*  11:    */ import javax.xml.transform.stax.StAXResult;
/*  12:    */ import javax.xml.transform.stax.StAXSource;
/*  13:    */ import org.springframework.util.Assert;
/*  14:    */ import org.springframework.util.ClassUtils;
/*  15:    */ import org.xml.sax.ContentHandler;
/*  16:    */ import org.xml.sax.XMLReader;
/*  17:    */ 
/*  18:    */ public abstract class StaxUtils
/*  19:    */ {
/*  20: 49 */   private static boolean jaxp14Available = ClassUtils.isPresent("javax.xml.transform.stax.StAXSource", StaxUtils.class.getClassLoader());
/*  21:    */   
/*  22:    */   public static Source createCustomStaxSource(XMLStreamReader streamReader)
/*  23:    */   {
/*  24: 60 */     return new StaxSource(streamReader);
/*  25:    */   }
/*  26:    */   
/*  27:    */   public static Source createStaxSource(XMLStreamReader streamReader)
/*  28:    */   {
/*  29: 73 */     if (jaxp14Available) {
/*  30: 74 */       return Jaxp14StaxHandler.createStaxSource(streamReader);
/*  31:    */     }
/*  32: 77 */     return createCustomStaxSource(streamReader);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static Source createCustomStaxSource(XMLEventReader eventReader)
/*  36:    */   {
/*  37: 88 */     return new StaxSource(eventReader);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public static Source createStaxSource(XMLEventReader eventReader)
/*  41:    */     throws XMLStreamException
/*  42:    */   {
/*  43:102 */     if (jaxp14Available) {
/*  44:103 */       return Jaxp14StaxHandler.createStaxSource(eventReader);
/*  45:    */     }
/*  46:106 */     return createCustomStaxSource(eventReader);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public static boolean isStaxSource(Source source)
/*  50:    */   {
/*  51:116 */     return ((source instanceof StaxSource)) || ((jaxp14Available) && (Jaxp14StaxHandler.isStaxSource(source)));
/*  52:    */   }
/*  53:    */   
/*  54:    */   public static Result createCustomStaxResult(XMLStreamWriter streamWriter)
/*  55:    */   {
/*  56:128 */     return new StaxResult(streamWriter);
/*  57:    */   }
/*  58:    */   
/*  59:    */   public static Result createStaxResult(XMLStreamWriter streamWriter)
/*  60:    */   {
/*  61:141 */     if (jaxp14Available) {
/*  62:142 */       return Jaxp14StaxHandler.createStaxResult(streamWriter);
/*  63:    */     }
/*  64:145 */     return createCustomStaxResult(streamWriter);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public static Result createCustomStaxResult(XMLEventWriter eventWriter)
/*  68:    */   {
/*  69:156 */     return new StaxResult(eventWriter);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static Result createStaxResult(XMLEventWriter eventWriter)
/*  73:    */     throws XMLStreamException
/*  74:    */   {
/*  75:170 */     if (jaxp14Available) {
/*  76:171 */       return Jaxp14StaxHandler.createStaxResult(eventWriter);
/*  77:    */     }
/*  78:174 */     return createCustomStaxResult(eventWriter);
/*  79:    */   }
/*  80:    */   
/*  81:    */   public static boolean isStaxResult(Result result)
/*  82:    */   {
/*  83:184 */     return ((result instanceof StaxResult)) || ((jaxp14Available) && (Jaxp14StaxHandler.isStaxResult(result)));
/*  84:    */   }
/*  85:    */   
/*  86:    */   public static XMLStreamReader getXMLStreamReader(Source source)
/*  87:    */   {
/*  88:196 */     if ((source instanceof StaxSource)) {
/*  89:197 */       return ((StaxSource)source).getXMLStreamReader();
/*  90:    */     }
/*  91:199 */     if (jaxp14Available) {
/*  92:200 */       return Jaxp14StaxHandler.getXMLStreamReader(source);
/*  93:    */     }
/*  94:203 */     throw new IllegalArgumentException("Source '" + source + "' is neither StaxSource nor StAXSource");
/*  95:    */   }
/*  96:    */   
/*  97:    */   public static XMLEventReader getXMLEventReader(Source source)
/*  98:    */   {
/*  99:216 */     if ((source instanceof StaxSource)) {
/* 100:217 */       return ((StaxSource)source).getXMLEventReader();
/* 101:    */     }
/* 102:219 */     if (jaxp14Available) {
/* 103:220 */       return Jaxp14StaxHandler.getXMLEventReader(source);
/* 104:    */     }
/* 105:223 */     throw new IllegalArgumentException("Source '" + source + "' is neither StaxSource nor StAXSource");
/* 106:    */   }
/* 107:    */   
/* 108:    */   public static XMLStreamWriter getXMLStreamWriter(Result result)
/* 109:    */   {
/* 110:236 */     if ((result instanceof StaxResult)) {
/* 111:237 */       return ((StaxResult)result).getXMLStreamWriter();
/* 112:    */     }
/* 113:239 */     if (jaxp14Available) {
/* 114:240 */       return Jaxp14StaxHandler.getXMLStreamWriter(result);
/* 115:    */     }
/* 116:243 */     throw new IllegalArgumentException("Result '" + result + "' is neither StaxResult nor StAXResult");
/* 117:    */   }
/* 118:    */   
/* 119:    */   public static XMLEventWriter getXMLEventWriter(Result result)
/* 120:    */   {
/* 121:256 */     if ((result instanceof StaxResult)) {
/* 122:257 */       return ((StaxResult)result).getXMLEventWriter();
/* 123:    */     }
/* 124:259 */     if (jaxp14Available) {
/* 125:260 */       return Jaxp14StaxHandler.getXMLEventWriter(result);
/* 126:    */     }
/* 127:263 */     throw new IllegalArgumentException("Result '" + result + "' is neither StaxResult nor StAXResult");
/* 128:    */   }
/* 129:    */   
/* 130:    */   public static ContentHandler createContentHandler(XMLStreamWriter streamWriter)
/* 131:    */   {
/* 132:273 */     return new StaxStreamContentHandler(streamWriter);
/* 133:    */   }
/* 134:    */   
/* 135:    */   public static ContentHandler createContentHandler(XMLEventWriter eventWriter)
/* 136:    */   {
/* 137:282 */     return new StaxEventContentHandler(eventWriter);
/* 138:    */   }
/* 139:    */   
/* 140:    */   public static XMLReader createXMLReader(XMLStreamReader streamReader)
/* 141:    */   {
/* 142:291 */     return new StaxStreamXMLReader(streamReader);
/* 143:    */   }
/* 144:    */   
/* 145:    */   public static XMLReader createXMLReader(XMLEventReader eventReader)
/* 146:    */   {
/* 147:300 */     return new StaxEventXMLReader(eventReader);
/* 148:    */   }
/* 149:    */   
/* 150:    */   public static XMLStreamReader createEventStreamReader(XMLEventReader eventReader)
/* 151:    */     throws XMLStreamException
/* 152:    */   {
/* 153:309 */     return new XMLEventStreamReader(eventReader);
/* 154:    */   }
/* 155:    */   
/* 156:    */   public static XMLStreamWriter createEventStreamWriter(XMLEventWriter eventWriter, XMLEventFactory eventFactory)
/* 157:    */   {
/* 158:318 */     return new XMLEventStreamWriter(eventWriter, eventFactory);
/* 159:    */   }
/* 160:    */   
/* 161:    */   private static class Jaxp14StaxHandler
/* 162:    */   {
/* 163:    */     private static Source createStaxSource(XMLStreamReader streamReader)
/* 164:    */     {
/* 165:327 */       return new StAXSource(streamReader);
/* 166:    */     }
/* 167:    */     
/* 168:    */     private static Source createStaxSource(XMLEventReader eventReader)
/* 169:    */       throws XMLStreamException
/* 170:    */     {
/* 171:331 */       return new StAXSource(eventReader);
/* 172:    */     }
/* 173:    */     
/* 174:    */     private static Result createStaxResult(XMLStreamWriter streamWriter)
/* 175:    */     {
/* 176:335 */       return new StAXResult(streamWriter);
/* 177:    */     }
/* 178:    */     
/* 179:    */     private static Result createStaxResult(XMLEventWriter eventWriter)
/* 180:    */     {
/* 181:339 */       return new StAXResult(eventWriter);
/* 182:    */     }
/* 183:    */     
/* 184:    */     private static boolean isStaxSource(Source source)
/* 185:    */     {
/* 186:343 */       return source instanceof StAXSource;
/* 187:    */     }
/* 188:    */     
/* 189:    */     private static boolean isStaxResult(Result result)
/* 190:    */     {
/* 191:347 */       return result instanceof StAXResult;
/* 192:    */     }
/* 193:    */     
/* 194:    */     private static XMLStreamReader getXMLStreamReader(Source source)
/* 195:    */     {
/* 196:351 */       Assert.isInstanceOf(StAXSource.class, source);
/* 197:352 */       return ((StAXSource)source).getXMLStreamReader();
/* 198:    */     }
/* 199:    */     
/* 200:    */     private static XMLEventReader getXMLEventReader(Source source)
/* 201:    */     {
/* 202:356 */       Assert.isInstanceOf(StAXSource.class, source);
/* 203:357 */       return ((StAXSource)source).getXMLEventReader();
/* 204:    */     }
/* 205:    */     
/* 206:    */     private static XMLStreamWriter getXMLStreamWriter(Result result)
/* 207:    */     {
/* 208:361 */       Assert.isInstanceOf(StAXResult.class, result);
/* 209:362 */       return ((StAXResult)result).getXMLStreamWriter();
/* 210:    */     }
/* 211:    */     
/* 212:    */     private static XMLEventWriter getXMLEventWriter(Result result)
/* 213:    */     {
/* 214:366 */       Assert.isInstanceOf(StAXResult.class, result);
/* 215:367 */       return ((StAXResult)result).getXMLEventWriter();
/* 216:    */     }
/* 217:    */   }
/* 218:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.xml.StaxUtils
 * JD-Core Version:    0.7.0.1
 */