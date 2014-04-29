/*  1:   */ package org.springframework.beans.factory.xml;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ import org.xml.sax.EntityResolver;
/*  6:   */ import org.xml.sax.InputSource;
/*  7:   */ import org.xml.sax.SAXException;
/*  8:   */ 
/*  9:   */ public class DelegatingEntityResolver
/* 10:   */   implements EntityResolver
/* 11:   */ {
/* 12:   */   public static final String DTD_SUFFIX = ".dtd";
/* 13:   */   public static final String XSD_SUFFIX = ".xsd";
/* 14:   */   private final EntityResolver dtdResolver;
/* 15:   */   private final EntityResolver schemaResolver;
/* 16:   */   
/* 17:   */   public DelegatingEntityResolver(ClassLoader classLoader)
/* 18:   */   {
/* 19:61 */     this.dtdResolver = new BeansDtdResolver();
/* 20:62 */     this.schemaResolver = new PluggableSchemaResolver(classLoader);
/* 21:   */   }
/* 22:   */   
/* 23:   */   public DelegatingEntityResolver(EntityResolver dtdResolver, EntityResolver schemaResolver)
/* 24:   */   {
/* 25:72 */     Assert.notNull(dtdResolver, "'dtdResolver' is required");
/* 26:73 */     Assert.notNull(schemaResolver, "'schemaResolver' is required");
/* 27:74 */     this.dtdResolver = dtdResolver;
/* 28:75 */     this.schemaResolver = schemaResolver;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public InputSource resolveEntity(String publicId, String systemId)
/* 32:   */     throws SAXException, IOException
/* 33:   */   {
/* 34:80 */     if (systemId != null)
/* 35:   */     {
/* 36:81 */       if (systemId.endsWith(".dtd")) {
/* 37:82 */         return this.dtdResolver.resolveEntity(publicId, systemId);
/* 38:   */       }
/* 39:84 */       if (systemId.endsWith(".xsd")) {
/* 40:85 */         return this.schemaResolver.resolveEntity(publicId, systemId);
/* 41:   */       }
/* 42:   */     }
/* 43:88 */     return null;
/* 44:   */   }
/* 45:   */   
/* 46:   */   public String toString()
/* 47:   */   {
/* 48:94 */     return 
/* 49:95 */       "EntityResolver delegating .xsd to " + this.schemaResolver + " and " + ".dtd" + " to " + this.dtdResolver;
/* 50:   */   }
/* 51:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.DelegatingEntityResolver
 * JD-Core Version:    0.7.0.1
 */