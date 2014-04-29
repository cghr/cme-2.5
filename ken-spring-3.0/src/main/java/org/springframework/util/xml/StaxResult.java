/*   1:    */ package org.springframework.util.xml;
/*   2:    */ 
/*   3:    */ import javax.xml.stream.XMLEventFactory;
/*   4:    */ import javax.xml.stream.XMLEventWriter;
/*   5:    */ import javax.xml.stream.XMLStreamWriter;
/*   6:    */ import javax.xml.transform.sax.SAXResult;
/*   7:    */ import org.xml.sax.ContentHandler;
/*   8:    */ 
/*   9:    */ class StaxResult
/*  10:    */   extends SAXResult
/*  11:    */ {
/*  12:    */   private XMLEventWriter eventWriter;
/*  13:    */   private XMLStreamWriter streamWriter;
/*  14:    */   
/*  15:    */   StaxResult(XMLStreamWriter streamWriter)
/*  16:    */   {
/*  17: 58 */     super.setHandler(new StaxStreamContentHandler(streamWriter));
/*  18: 59 */     this.streamWriter = streamWriter;
/*  19:    */   }
/*  20:    */   
/*  21:    */   StaxResult(XMLEventWriter eventWriter)
/*  22:    */   {
/*  23: 68 */     super.setHandler(new StaxEventContentHandler(eventWriter));
/*  24: 69 */     this.eventWriter = eventWriter;
/*  25:    */   }
/*  26:    */   
/*  27:    */   StaxResult(XMLEventWriter eventWriter, XMLEventFactory eventFactory)
/*  28:    */   {
/*  29: 80 */     super.setHandler(new StaxEventContentHandler(eventWriter, eventFactory));
/*  30: 81 */     this.eventWriter = eventWriter;
/*  31:    */   }
/*  32:    */   
/*  33:    */   XMLEventWriter getXMLEventWriter()
/*  34:    */   {
/*  35: 92 */     return this.eventWriter;
/*  36:    */   }
/*  37:    */   
/*  38:    */   XMLStreamWriter getXMLStreamWriter()
/*  39:    */   {
/*  40:103 */     return this.streamWriter;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setHandler(ContentHandler handler)
/*  44:    */   {
/*  45:113 */     throw new UnsupportedOperationException("setHandler is not supported");
/*  46:    */   }
/*  47:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.xml.StaxResult
 * JD-Core Version:    0.7.0.1
 */