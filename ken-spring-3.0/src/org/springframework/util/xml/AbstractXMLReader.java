/*   1:    */ package org.springframework.util.xml;
/*   2:    */ 
/*   3:    */ import org.xml.sax.ContentHandler;
/*   4:    */ import org.xml.sax.DTDHandler;
/*   5:    */ import org.xml.sax.EntityResolver;
/*   6:    */ import org.xml.sax.ErrorHandler;
/*   7:    */ import org.xml.sax.SAXNotRecognizedException;
/*   8:    */ import org.xml.sax.SAXNotSupportedException;
/*   9:    */ import org.xml.sax.XMLReader;
/*  10:    */ import org.xml.sax.ext.LexicalHandler;
/*  11:    */ 
/*  12:    */ abstract class AbstractXMLReader
/*  13:    */   implements XMLReader
/*  14:    */ {
/*  15:    */   private DTDHandler dtdHandler;
/*  16:    */   private ContentHandler contentHandler;
/*  17:    */   private EntityResolver entityResolver;
/*  18:    */   private ErrorHandler errorHandler;
/*  19:    */   private LexicalHandler lexicalHandler;
/*  20:    */   
/*  21:    */   public ContentHandler getContentHandler()
/*  22:    */   {
/*  23: 52 */     return this.contentHandler;
/*  24:    */   }
/*  25:    */   
/*  26:    */   public void setContentHandler(ContentHandler contentHandler)
/*  27:    */   {
/*  28: 56 */     this.contentHandler = contentHandler;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void setDTDHandler(DTDHandler dtdHandler)
/*  32:    */   {
/*  33: 60 */     this.dtdHandler = dtdHandler;
/*  34:    */   }
/*  35:    */   
/*  36:    */   public DTDHandler getDTDHandler()
/*  37:    */   {
/*  38: 64 */     return this.dtdHandler;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public EntityResolver getEntityResolver()
/*  42:    */   {
/*  43: 68 */     return this.entityResolver;
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void setEntityResolver(EntityResolver entityResolver)
/*  47:    */   {
/*  48: 72 */     this.entityResolver = entityResolver;
/*  49:    */   }
/*  50:    */   
/*  51:    */   public ErrorHandler getErrorHandler()
/*  52:    */   {
/*  53: 76 */     return this.errorHandler;
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void setErrorHandler(ErrorHandler errorHandler)
/*  57:    */   {
/*  58: 80 */     this.errorHandler = errorHandler;
/*  59:    */   }
/*  60:    */   
/*  61:    */   protected LexicalHandler getLexicalHandler()
/*  62:    */   {
/*  63: 84 */     return this.lexicalHandler;
/*  64:    */   }
/*  65:    */   
/*  66:    */   public boolean getFeature(String name)
/*  67:    */     throws SAXNotRecognizedException, SAXNotSupportedException
/*  68:    */   {
/*  69: 94 */     throw new SAXNotRecognizedException(name);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setFeature(String name, boolean value)
/*  73:    */     throws SAXNotRecognizedException, SAXNotSupportedException
/*  74:    */   {
/*  75:103 */     throw new SAXNotRecognizedException(name);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public Object getProperty(String name)
/*  79:    */     throws SAXNotRecognizedException, SAXNotSupportedException
/*  80:    */   {
/*  81:111 */     if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
/*  82:112 */       return this.lexicalHandler;
/*  83:    */     }
/*  84:115 */     throw new SAXNotRecognizedException(name);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void setProperty(String name, Object value)
/*  88:    */     throws SAXNotRecognizedException, SAXNotSupportedException
/*  89:    */   {
/*  90:124 */     if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
/*  91:125 */       this.lexicalHandler = ((LexicalHandler)value);
/*  92:    */     } else {
/*  93:128 */       throw new SAXNotRecognizedException(name);
/*  94:    */     }
/*  95:    */   }
/*  96:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.util.xml.AbstractXMLReader
 * JD-Core Version:    0.7.0.1
 */