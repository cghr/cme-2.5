/*   1:    */ package org.springframework.context.support;
/*   2:    */ 
/*   3:    */ import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
/*   4:    */ import org.springframework.core.env.ConfigurableEnvironment;
/*   5:    */ import org.springframework.core.io.ClassPathResource;
/*   6:    */ import org.springframework.core.io.Resource;
/*   7:    */ 
/*   8:    */ public class GenericXmlApplicationContext
/*   9:    */   extends GenericApplicationContext
/*  10:    */ {
/*  11: 43 */   private final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this);
/*  12:    */   
/*  13:    */   public GenericXmlApplicationContext()
/*  14:    */   {
/*  15: 51 */     this.reader.setEnvironment(getEnvironment());
/*  16:    */   }
/*  17:    */   
/*  18:    */   public GenericXmlApplicationContext(Resource... resources)
/*  19:    */   {
/*  20: 60 */     load(resources);
/*  21: 61 */     refresh();
/*  22:    */   }
/*  23:    */   
/*  24:    */   public GenericXmlApplicationContext(String... resourceLocations)
/*  25:    */   {
/*  26: 70 */     load(resourceLocations);
/*  27: 71 */     refresh();
/*  28:    */   }
/*  29:    */   
/*  30:    */   public GenericXmlApplicationContext(Class<?> relativeClass, String... resourceNames)
/*  31:    */   {
/*  32: 82 */     load(relativeClass, resourceNames);
/*  33: 83 */     refresh();
/*  34:    */   }
/*  35:    */   
/*  36:    */   public void setValidating(boolean validating)
/*  37:    */   {
/*  38: 90 */     this.reader.setValidating(validating);
/*  39:    */   }
/*  40:    */   
/*  41:    */   public void setEnvironment(ConfigurableEnvironment environment)
/*  42:    */   {
/*  43:100 */     super.setEnvironment(environment);
/*  44:101 */     this.reader.setEnvironment(getEnvironment());
/*  45:    */   }
/*  46:    */   
/*  47:    */   public void load(Resource... resources)
/*  48:    */   {
/*  49:109 */     this.reader.loadBeanDefinitions(resources);
/*  50:    */   }
/*  51:    */   
/*  52:    */   public void load(String... resourceLocations)
/*  53:    */   {
/*  54:117 */     this.reader.loadBeanDefinitions(resourceLocations);
/*  55:    */   }
/*  56:    */   
/*  57:    */   public void load(Class<?> relativeClass, String... resourceNames)
/*  58:    */   {
/*  59:127 */     Resource[] resources = new Resource[resourceNames.length];
/*  60:128 */     for (int i = 0; i < resourceNames.length; i++) {
/*  61:129 */       resources[i] = new ClassPathResource(resourceNames[i], relativeClass);
/*  62:    */     }
/*  63:131 */     load(resources);
/*  64:    */   }
/*  65:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.support.GenericXmlApplicationContext
 * JD-Core Version:    0.7.0.1
 */