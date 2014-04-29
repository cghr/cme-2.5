/*   1:    */ package org.springframework.util.xml;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.springframework.util.Assert;
/*   6:    */ import org.w3c.dom.Document;
/*   7:    */ import org.w3c.dom.Element;
/*   8:    */ import org.w3c.dom.Node;
/*   9:    */ import org.w3c.dom.ProcessingInstruction;
/*  10:    */ import org.w3c.dom.Text;
/*  11:    */ import org.xml.sax.Attributes;
/*  12:    */ import org.xml.sax.ContentHandler;
/*  13:    */ import org.xml.sax.Locator;
/*  14:    */ import org.xml.sax.SAXException;
/*  15:    */ 
/*  16:    */ class DomContentHandler
/*  17:    */   implements ContentHandler
/*  18:    */ {
/*  19:    */   private final Document document;
/*  20: 45 */   private final List<Element> elements = new ArrayList();
/*  21:    */   private final Node node;
/*  22:    */   
/*  23:    */   DomContentHandler(Node node)
/*  24:    */   {
/*  25: 55 */     Assert.notNull(node, "node must not be null");
/*  26: 56 */     this.node = node;
/*  27: 57 */     if ((node instanceof Document)) {
/*  28: 58 */       this.document = ((Document)node);
/*  29:    */     } else {
/*  30: 61 */       this.document = node.getOwnerDocument();
/*  31:    */     }
/*  32: 63 */     Assert.notNull(this.document, "document must not be null");
/*  33:    */   }
/*  34:    */   
/*  35:    */   private Node getParent()
/*  36:    */   {
/*  37: 67 */     if (!this.elements.isEmpty()) {
/*  38: 68 */       return (Node)this.elements.get(this.elements.size() - 1);
/*  39:    */     }
/*  40: 71 */     return this.node;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void startElement(String uri, String localName, String qName, Attributes attributes)
/*  44:    */     throws SAXException
/*  45:    */   {
/*  46: 76 */     Node parent = getParent();
/*  47: 77 */     Element element = this.document.createElementNS(uri, qName);
/*  48: 78 */     for (int i = 0; i < attributes.getLength(); i++)
/*  49:    */     {
/*  50: 79 */       String attrUri = attributes.getURI(i);
/*  51: 80 */       String attrQname = attributes.getQName(i);
/*  52: 81 */       String value = attributes.getValue(i);
/*  53: 82 */       if (!attrQname.startsWith("xmlns")) {
/*  54: 83 */         element.setAttributeNS(attrUri, attrQname, value);
/*  55:    */       }
/*  56:    */     }
/*  57: 86 */     element = (Element)parent.appendChild(element);
/*  58: 87 */     this.elements.add(element);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void endElement(String uri, String localName, String qName)
/*  62:    */     throws SAXException
/*  63:    */   {
/*  64: 91 */     this.elements.remove(this.elements.size() - 1);
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void characters(char[] ch, int start, int length)
/*  68:    */     throws SAXException
/*  69:    */   {
/*  70: 95 */     String data = new String(ch, start, length);
/*  71: 96 */     Node parent = getParent();
/*  72: 97 */     Node lastChild = parent.getLastChild();
/*  73: 98 */     if ((lastChild != null) && (lastChild.getNodeType() == 3))
/*  74:    */     {
/*  75: 99 */       ((Text)lastChild).appendData(data);
/*  76:    */     }
/*  77:    */     else
/*  78:    */     {
/*  79:102 */       Text text = this.document.createTextNode(data);
/*  80:103 */       parent.appendChild(text);
/*  81:    */     }
/*  82:    */   }
/*  83:    */   
/*  84:    */   public void processingInstruction(String target, String data)
/*  85:    */     throws SAXException
/*  86:    */   {
/*  87:108 */     Node parent = getParent();
/*  88:109 */     ProcessingInstruction pi = this.document.createProcessingInstruction(target, data);
/*  89:110 */     parent.appendChild(pi);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public void setDocumentLocator(Locator locator) {}
/*  93:    */   
/*  94:    */   public void startDocument()
/*  95:    */     throws SAXException
/*  96:    */   {}
/*  97:    */   
/*  98:    */   public void endDocument()
/*  99:    */     throws SAXException
/* 100:    */   {}
/* 101:    */   
/* 102:    */   public void startPrefixMapping(String prefix, String uri)
/* 103:    */     throws SAXException
/* 104:    */   {}
/* 105:    */   
/* 106:    */   public void endPrefixMapping(String prefix)
/* 107:    */     throws SAXException
/* 108:    */   {}
/* 109:    */   
/* 110:    */   public void ignorableWhitespace(char[] ch, int start, int length)
/* 111:    */     throws SAXException
/* 112:    */   {}
/* 113:    */   
/* 114:    */   public void skippedEntity(String name)
/* 115:    */     throws SAXException
/* 116:    */   {}
/* 117:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.xml.DomContentHandler
 * JD-Core Version:    0.7.0.1
 */