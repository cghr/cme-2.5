/*  1:   */ package org.springframework.beans.factory.support;
/*  2:   */ 
/*  3:   */ import java.io.FileNotFoundException;
/*  4:   */ import java.io.IOException;
/*  5:   */ import java.io.InputStream;
/*  6:   */ import org.springframework.beans.factory.config.BeanDefinition;
/*  7:   */ import org.springframework.core.io.AbstractResource;
/*  8:   */ import org.springframework.util.Assert;
/*  9:   */ 
/* 10:   */ class BeanDefinitionResource
/* 11:   */   extends AbstractResource
/* 12:   */ {
/* 13:   */   private final BeanDefinition beanDefinition;
/* 14:   */   
/* 15:   */   public BeanDefinitionResource(BeanDefinition beanDefinition)
/* 16:   */   {
/* 17:45 */     Assert.notNull(beanDefinition, "BeanDefinition must not be null");
/* 18:46 */     this.beanDefinition = beanDefinition;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public final BeanDefinition getBeanDefinition()
/* 22:   */   {
/* 23:53 */     return this.beanDefinition;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public boolean exists()
/* 27:   */   {
/* 28:59 */     return false;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public boolean isReadable()
/* 32:   */   {
/* 33:64 */     return false;
/* 34:   */   }
/* 35:   */   
/* 36:   */   public InputStream getInputStream()
/* 37:   */     throws IOException
/* 38:   */   {
/* 39:68 */     throw new FileNotFoundException(
/* 40:69 */       "Resource cannot be opened because it points to " + getDescription());
/* 41:   */   }
/* 42:   */   
/* 43:   */   public String getDescription()
/* 44:   */   {
/* 45:73 */     return "BeanDefinition defined in " + this.beanDefinition.getResourceDescription();
/* 46:   */   }
/* 47:   */   
/* 48:   */   public boolean equals(Object obj)
/* 49:   */   {
/* 50:84 */     return (obj == this) || (((obj instanceof BeanDefinitionResource)) && (((BeanDefinitionResource)obj).beanDefinition.equals(this.beanDefinition)));
/* 51:   */   }
/* 52:   */   
/* 53:   */   public int hashCode()
/* 54:   */   {
/* 55:92 */     return this.beanDefinition.hashCode();
/* 56:   */   }
/* 57:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.BeanDefinitionResource
 * JD-Core Version:    0.7.0.1
 */