/*   1:    */ package org.springframework.beans.factory.xml;
/*   2:    */ 
/*   3:    */ import javax.xml.parsers.DocumentBuilder;
/*   4:    */ import javax.xml.parsers.DocumentBuilderFactory;
/*   5:    */ import javax.xml.parsers.ParserConfigurationException;
/*   6:    */ import org.apache.commons.logging.Log;
/*   7:    */ import org.apache.commons.logging.LogFactory;
/*   8:    */ import org.w3c.dom.Document;
/*   9:    */ import org.xml.sax.EntityResolver;
/*  10:    */ import org.xml.sax.ErrorHandler;
/*  11:    */ import org.xml.sax.InputSource;
/*  12:    */ 
/*  13:    */ public class DefaultDocumentLoader
/*  14:    */   implements DocumentLoader
/*  15:    */ {
/*  16:    */   private static final String SCHEMA_LANGUAGE_ATTRIBUTE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
/*  17:    */   private static final String XSD_SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";
/*  18: 60 */   private static final Log logger = LogFactory.getLog(DefaultDocumentLoader.class);
/*  19:    */   
/*  20:    */   public Document loadDocument(InputSource inputSource, EntityResolver entityResolver, ErrorHandler errorHandler, int validationMode, boolean namespaceAware)
/*  21:    */     throws Exception
/*  22:    */   {
/*  23: 70 */     DocumentBuilderFactory factory = createDocumentBuilderFactory(validationMode, namespaceAware);
/*  24: 71 */     if (logger.isDebugEnabled()) {
/*  25: 72 */       logger.debug("Using JAXP provider [" + factory.getClass().getName() + "]");
/*  26:    */     }
/*  27: 74 */     DocumentBuilder builder = createDocumentBuilder(factory, entityResolver, errorHandler);
/*  28: 75 */     return builder.parse(inputSource);
/*  29:    */   }
/*  30:    */   
/*  31:    */   protected DocumentBuilderFactory createDocumentBuilderFactory(int validationMode, boolean namespaceAware)
/*  32:    */     throws ParserConfigurationException
/*  33:    */   {
/*  34: 89 */     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*  35: 90 */     factory.setNamespaceAware(namespaceAware);
/*  36: 92 */     if (validationMode != 0)
/*  37:    */     {
/*  38: 93 */       factory.setValidating(true);
/*  39: 95 */       if (validationMode == 3)
/*  40:    */       {
/*  41: 97 */         factory.setNamespaceAware(true);
/*  42:    */         try
/*  43:    */         {
/*  44: 99 */           factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
/*  45:    */         }
/*  46:    */         catch (IllegalArgumentException ex)
/*  47:    */         {
/*  48:102 */           ParserConfigurationException pcex = new ParserConfigurationException(
/*  49:103 */             "Unable to validate using XSD: Your JAXP provider [" + factory + 
/*  50:104 */             "] does not support XML Schema. Are you running on Java 1.4 with Apache Crimson? " + 
/*  51:105 */             "Upgrade to Apache Xerces (or Java 1.5) for full XSD support.");
/*  52:106 */           pcex.initCause(ex);
/*  53:107 */           throw pcex;
/*  54:    */         }
/*  55:    */       }
/*  56:    */     }
/*  57:112 */     return factory;
/*  58:    */   }
/*  59:    */   
/*  60:    */   protected DocumentBuilder createDocumentBuilder(DocumentBuilderFactory factory, EntityResolver entityResolver, ErrorHandler errorHandler)
/*  61:    */     throws ParserConfigurationException
/*  62:    */   {
/*  63:130 */     DocumentBuilder docBuilder = factory.newDocumentBuilder();
/*  64:131 */     if (entityResolver != null) {
/*  65:132 */       docBuilder.setEntityResolver(entityResolver);
/*  66:    */     }
/*  67:134 */     if (errorHandler != null) {
/*  68:135 */       docBuilder.setErrorHandler(errorHandler);
/*  69:    */     }
/*  70:137 */     return docBuilder;
/*  71:    */   }
/*  72:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.DefaultDocumentLoader
 * JD-Core Version:    0.7.0.1
 */