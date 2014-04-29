/*  1:   */ package org.springframework.jdbc.config;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.net.URL;
/*  5:   */ import java.util.ArrayList;
/*  6:   */ import java.util.Arrays;
/*  7:   */ import java.util.Collection;
/*  8:   */ import java.util.Collections;
/*  9:   */ import java.util.Comparator;
/* 10:   */ import java.util.Iterator;
/* 11:   */ import java.util.List;
/* 12:   */ import org.springframework.beans.factory.config.AbstractFactoryBean;
/* 13:   */ import org.springframework.context.ResourceLoaderAware;
/* 14:   */ import org.springframework.core.io.Resource;
/* 15:   */ import org.springframework.core.io.ResourceLoader;
/* 16:   */ import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
/* 17:   */ import org.springframework.core.io.support.ResourcePatternResolver;
/* 18:   */ import org.springframework.core.io.support.ResourcePatternUtils;
/* 19:   */ 
/* 20:   */ public class SortedResourcesFactoryBean
/* 21:   */   extends AbstractFactoryBean<Resource[]>
/* 22:   */   implements ResourceLoaderAware
/* 23:   */ {
/* 24:   */   private final List<String> locations;
/* 25:   */   private ResourcePatternResolver resourcePatternResolver;
/* 26:   */   
/* 27:   */   public SortedResourcesFactoryBean(List<String> locations)
/* 28:   */   {
/* 29:52 */     this.locations = locations;
/* 30:53 */     this.resourcePatternResolver = new PathMatchingResourcePatternResolver();
/* 31:   */   }
/* 32:   */   
/* 33:   */   public SortedResourcesFactoryBean(ResourceLoader resourceLoader, List<String> locations)
/* 34:   */   {
/* 35:57 */     this.locations = locations;
/* 36:58 */     this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
/* 37:   */   }
/* 38:   */   
/* 39:   */   public void setResourceLoader(ResourceLoader resourceLoader)
/* 40:   */   {
/* 41:63 */     this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
/* 42:   */   }
/* 43:   */   
/* 44:   */   public Class<? extends Resource[]> getObjectType()
/* 45:   */   {
/* 46:69 */     return [Lorg.springframework.core.io.Resource.class;
/* 47:   */   }
/* 48:   */   
/* 49:   */   protected Resource[] createInstance()
/* 50:   */     throws Exception
/* 51:   */   {
/* 52:74 */     List<Resource> scripts = new ArrayList();
/* 53:   */     Iterator localIterator2;
/* 54:75 */     for (Iterator localIterator1 = this.locations.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/* 55:   */     {
/* 56:75 */       String location = (String)localIterator1.next();
/* 57:76 */       List<Resource> resources = new ArrayList(
/* 58:77 */         (Collection)Arrays.asList(this.resourcePatternResolver.getResources(location)));
/* 59:78 */       Collections.sort(resources, new Comparator()
/* 60:   */       {
/* 61:   */         public int compare(Resource r1, Resource r2)
/* 62:   */         {
/* 63:   */           try
/* 64:   */           {
/* 65:81 */             return r1.getURL().toString().compareTo(r2.getURL().toString());
/* 66:   */           }
/* 67:   */           catch (IOException localIOException) {}
/* 68:84 */           return 0;
/* 69:   */         }
/* 70:87 */       });
/* 71:88 */       localIterator2 = resources.iterator(); continue;Resource resource = (Resource)localIterator2.next();
/* 72:89 */       scripts.add(resource);
/* 73:   */     }
/* 74:92 */     return (Resource[])scripts.toArray(new Resource[scripts.size()]);
/* 75:   */   }
/* 76:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.config.SortedResourcesFactoryBean
 * JD-Core Version:    0.7.0.1
 */