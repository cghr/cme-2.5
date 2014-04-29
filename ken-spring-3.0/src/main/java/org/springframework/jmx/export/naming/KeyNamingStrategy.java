/*   1:    */ package org.springframework.jmx.export.naming;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.Properties;
/*   5:    */ import javax.management.MalformedObjectNameException;
/*   6:    */ import javax.management.ObjectName;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ import org.springframework.beans.factory.InitializingBean;
/*  10:    */ import org.springframework.core.io.Resource;
/*  11:    */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*  12:    */ import org.springframework.jmx.support.ObjectNameManager;
/*  13:    */ import org.springframework.util.CollectionUtils;
/*  14:    */ 
/*  15:    */ public class KeyNamingStrategy
/*  16:    */   implements ObjectNamingStrategy, InitializingBean
/*  17:    */ {
/*  18: 58 */   protected final Log logger = LogFactory.getLog(getClass());
/*  19:    */   private Properties mappings;
/*  20:    */   private Resource[] mappingLocations;
/*  21:    */   private Properties mergedMappings;
/*  22:    */   
/*  23:    */   public void setMappings(Properties mappings)
/*  24:    */   {
/*  25: 85 */     this.mappings = mappings;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setMappingLocation(Resource location)
/*  29:    */   {
/*  30: 93 */     this.mappingLocations = new Resource[] { location };
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setMappingLocations(Resource[] mappingLocations)
/*  34:    */   {
/*  35:101 */     this.mappingLocations = mappingLocations;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void afterPropertiesSet()
/*  39:    */     throws IOException
/*  40:    */   {
/*  41:112 */     this.mergedMappings = new Properties();
/*  42:    */     
/*  43:114 */     CollectionUtils.mergePropertiesIntoMap(this.mappings, this.mergedMappings);
/*  44:116 */     if (this.mappingLocations != null) {
/*  45:117 */       for (int i = 0; i < this.mappingLocations.length; i++)
/*  46:    */       {
/*  47:118 */         Resource location = this.mappingLocations[i];
/*  48:119 */         if (this.logger.isInfoEnabled()) {
/*  49:120 */           this.logger.info("Loading JMX object name mappings file from " + location);
/*  50:    */         }
/*  51:122 */         PropertiesLoaderUtils.fillProperties(this.mergedMappings, location);
/*  52:    */       }
/*  53:    */     }
/*  54:    */   }
/*  55:    */   
/*  56:    */   public ObjectName getObjectName(Object managedBean, String beanKey)
/*  57:    */     throws MalformedObjectNameException
/*  58:    */   {
/*  59:133 */     String objectName = null;
/*  60:134 */     if (this.mergedMappings != null) {
/*  61:135 */       objectName = this.mergedMappings.getProperty(beanKey);
/*  62:    */     }
/*  63:137 */     if (objectName == null) {
/*  64:138 */       objectName = beanKey;
/*  65:    */     }
/*  66:140 */     return ObjectNameManager.getInstance(objectName);
/*  67:    */   }
/*  68:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jmx.export.naming.KeyNamingStrategy
 * JD-Core Version:    0.7.0.1
 */