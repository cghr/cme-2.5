/*   1:    */ package org.springframework.util.xml;
/*   2:    */ 
/*   3:    */ import java.util.Iterator;
/*   4:    */ import javax.xml.namespace.QName;
/*   5:    */ import javax.xml.stream.XMLStreamException;
/*   6:    */ import javax.xml.stream.XMLStreamWriter;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ import org.springframework.util.StringUtils;
/*   9:    */ import org.xml.sax.Attributes;
/*  10:    */ import org.xml.sax.Locator;
/*  11:    */ 
/*  12:    */ class StaxStreamContentHandler
/*  13:    */   extends AbstractStaxContentHandler
/*  14:    */ {
/*  15:    */   private final XMLStreamWriter streamWriter;
/*  16:    */   
/*  17:    */   StaxStreamContentHandler(XMLStreamWriter streamWriter)
/*  18:    */   {
/*  19: 48 */     Assert.notNull(streamWriter, "'streamWriter' must not be null");
/*  20: 49 */     this.streamWriter = streamWriter;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void setDocumentLocator(Locator locator) {}
/*  24:    */   
/*  25:    */   protected void charactersInternal(char[] ch, int start, int length)
/*  26:    */     throws XMLStreamException
/*  27:    */   {
/*  28: 57 */     this.streamWriter.writeCharacters(ch, start, length);
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected void endDocumentInternal()
/*  32:    */     throws XMLStreamException
/*  33:    */   {
/*  34: 62 */     this.streamWriter.writeEndDocument();
/*  35:    */   }
/*  36:    */   
/*  37:    */   protected void endElementInternal(QName name, SimpleNamespaceContext namespaceContext)
/*  38:    */     throws XMLStreamException
/*  39:    */   {
/*  40: 67 */     this.streamWriter.writeEndElement();
/*  41:    */   }
/*  42:    */   
/*  43:    */   protected void ignorableWhitespaceInternal(char[] ch, int start, int length)
/*  44:    */     throws XMLStreamException
/*  45:    */   {
/*  46: 72 */     this.streamWriter.writeCharacters(ch, start, length);
/*  47:    */   }
/*  48:    */   
/*  49:    */   protected void processingInstructionInternal(String target, String data)
/*  50:    */     throws XMLStreamException
/*  51:    */   {
/*  52: 77 */     this.streamWriter.writeProcessingInstruction(target, data);
/*  53:    */   }
/*  54:    */   
/*  55:    */   protected void skippedEntityInternal(String name) {}
/*  56:    */   
/*  57:    */   protected void startDocumentInternal()
/*  58:    */     throws XMLStreamException
/*  59:    */   {
/*  60: 86 */     this.streamWriter.writeStartDocument();
/*  61:    */   }
/*  62:    */   
/*  63:    */   protected void startElementInternal(QName name, Attributes attributes, SimpleNamespaceContext namespaceContext)
/*  64:    */     throws XMLStreamException
/*  65:    */   {
/*  66: 92 */     this.streamWriter.writeStartElement(name.getPrefix(), name.getLocalPart(), name.getNamespaceURI());
/*  67: 93 */     if (namespaceContext != null)
/*  68:    */     {
/*  69: 94 */       String defaultNamespaceUri = namespaceContext.getNamespaceURI("");
/*  70: 95 */       if (StringUtils.hasLength(defaultNamespaceUri))
/*  71:    */       {
/*  72: 96 */         this.streamWriter.writeNamespace("", defaultNamespaceUri);
/*  73: 97 */         this.streamWriter.setDefaultNamespace(defaultNamespaceUri);
/*  74:    */       }
/*  75: 99 */       for (Iterator<String> iterator = namespaceContext.getBoundPrefixes(); iterator.hasNext();)
/*  76:    */       {
/*  77:100 */         String prefix = (String)iterator.next();
/*  78:101 */         this.streamWriter.writeNamespace(prefix, namespaceContext.getNamespaceURI(prefix));
/*  79:102 */         this.streamWriter.setPrefix(prefix, namespaceContext.getNamespaceURI(prefix));
/*  80:    */       }
/*  81:    */     }
/*  82:105 */     for (int i = 0; i < attributes.getLength(); i++)
/*  83:    */     {
/*  84:106 */       QName attrName = toQName(attributes.getURI(i), attributes.getQName(i));
/*  85:107 */       if ((!"xmlns".equals(attrName.getLocalPart())) && (!"xmlns".equals(attrName.getPrefix()))) {
/*  86:108 */         this.streamWriter.writeAttribute(attrName.getPrefix(), attrName.getNamespaceURI(), attrName.getLocalPart(), 
/*  87:109 */           attributes.getValue(i));
/*  88:    */       }
/*  89:    */     }
/*  90:    */   }
/*  91:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.xml.StaxStreamContentHandler
 * JD-Core Version:    0.7.0.1
 */