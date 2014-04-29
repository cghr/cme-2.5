/*  1:   */ package org.springframework.beans.factory.xml;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.BeansException;
/*  4:   */ import org.springframework.beans.factory.BeanFactory;
/*  5:   */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*  6:   */ import org.springframework.core.io.Resource;
/*  7:   */ 
/*  8:   */ @Deprecated
/*  9:   */ public class XmlBeanFactory
/* 10:   */   extends DefaultListableBeanFactory
/* 11:   */ {
/* 12:56 */   private final XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this);
/* 13:   */   
/* 14:   */   public XmlBeanFactory(Resource resource)
/* 15:   */     throws BeansException
/* 16:   */   {
/* 17:66 */     this(resource, null);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public XmlBeanFactory(Resource resource, BeanFactory parentBeanFactory)
/* 21:   */     throws BeansException
/* 22:   */   {
/* 23:77 */     super(parentBeanFactory);
/* 24:78 */     this.reader.loadBeanDefinitions(resource);
/* 25:   */   }
/* 26:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.xml.XmlBeanFactory
 * JD-Core Version:    0.7.0.1
 */