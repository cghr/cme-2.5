/*   1:    */ package org.springframework.web.servlet.config.annotation;
/*   2:    */ 
/*   3:    */ import java.util.ArrayList;
/*   4:    */ import java.util.List;
/*   5:    */ import org.springframework.core.io.Resource;
/*   6:    */ import org.springframework.core.io.ResourceLoader;
/*   7:    */ import org.springframework.util.Assert;
/*   8:    */ import org.springframework.util.CollectionUtils;
/*   9:    */ import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
/*  10:    */ 
/*  11:    */ public class ResourceHandlerRegistration
/*  12:    */ {
/*  13:    */   private final ResourceLoader resourceLoader;
/*  14:    */   private final String[] pathPatterns;
/*  15: 42 */   private final List<Resource> locations = new ArrayList();
/*  16:    */   private Integer cachePeriod;
/*  17:    */   
/*  18:    */   public ResourceHandlerRegistration(ResourceLoader resourceLoader, String... pathPatterns)
/*  19:    */   {
/*  20: 52 */     Assert.notEmpty(pathPatterns, "At least one path pattern is required for resource handling.");
/*  21: 53 */     this.resourceLoader = resourceLoader;
/*  22: 54 */     this.pathPatterns = pathPatterns;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public ResourceHandlerRegistration addResourceLocations(String... resourceLocations)
/*  26:    */   {
/*  27: 67 */     for (String location : resourceLocations) {
/*  28: 68 */       this.locations.add(this.resourceLoader.getResource(location));
/*  29:    */     }
/*  30: 70 */     return this;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public ResourceHandlerRegistration setCachePeriod(Integer cachePeriod)
/*  34:    */   {
/*  35: 81 */     this.cachePeriod = cachePeriod;
/*  36: 82 */     return this;
/*  37:    */   }
/*  38:    */   
/*  39:    */   protected String[] getPathPatterns()
/*  40:    */   {
/*  41: 89 */     return this.pathPatterns;
/*  42:    */   }
/*  43:    */   
/*  44:    */   protected ResourceHttpRequestHandler getRequestHandler()
/*  45:    */   {
/*  46: 96 */     Assert.isTrue(!CollectionUtils.isEmpty(this.locations), "At least one location is required for resource handling.");
/*  47: 97 */     ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
/*  48: 98 */     requestHandler.setLocations(this.locations);
/*  49: 99 */     if (this.cachePeriod != null) {
/*  50:100 */       requestHandler.setCacheSeconds(this.cachePeriod.intValue());
/*  51:    */     }
/*  52:102 */     return requestHandler;
/*  53:    */   }
/*  54:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration
 * JD-Core Version:    0.7.0.1
 */