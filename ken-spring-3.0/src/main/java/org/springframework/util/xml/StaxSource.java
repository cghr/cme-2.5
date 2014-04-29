/*   1:    */ package org.springframework.util.xml;
/*   2:    */ 
/*   3:    */ import javax.xml.stream.XMLEventReader;
/*   4:    */ import javax.xml.stream.XMLStreamReader;
/*   5:    */ import javax.xml.transform.sax.SAXSource;
/*   6:    */ import org.xml.sax.InputSource;
/*   7:    */ import org.xml.sax.XMLReader;
/*   8:    */ 
/*   9:    */ class StaxSource
/*  10:    */   extends SAXSource
/*  11:    */ {
/*  12:    */   private XMLEventReader eventReader;
/*  13:    */   private XMLStreamReader streamReader;
/*  14:    */   
/*  15:    */   StaxSource(XMLStreamReader streamReader)
/*  16:    */   {
/*  17: 61 */     super(new StaxStreamXMLReader(streamReader), new InputSource());
/*  18: 62 */     this.streamReader = streamReader;
/*  19:    */   }
/*  20:    */   
/*  21:    */   StaxSource(XMLEventReader eventReader)
/*  22:    */   {
/*  23: 74 */     super(new StaxEventXMLReader(eventReader), new InputSource());
/*  24: 75 */     this.eventReader = eventReader;
/*  25:    */   }
/*  26:    */   
/*  27:    */   XMLEventReader getXMLEventReader()
/*  28:    */   {
/*  29: 86 */     return this.eventReader;
/*  30:    */   }
/*  31:    */   
/*  32:    */   XMLStreamReader getXMLStreamReader()
/*  33:    */   {
/*  34: 97 */     return this.streamReader;
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setInputSource(InputSource inputSource)
/*  38:    */   {
/*  39:107 */     throw new UnsupportedOperationException("setInputSource is not supported");
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setXMLReader(XMLReader reader)
/*  43:    */   {
/*  44:117 */     throw new UnsupportedOperationException("setXMLReader is not supported");
/*  45:    */   }
/*  46:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.xml.StaxSource
 * JD-Core Version:    0.7.0.1
 */