/*  1:   */ package org.springframework.scripting.config;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
/*  4:   */ 
/*  5:   */ public class LangNamespaceHandler
/*  6:   */   extends NamespaceHandlerSupport
/*  7:   */ {
/*  8:   */   public void init()
/*  9:   */   {
/* 10:43 */     registerScriptBeanDefinitionParser("groovy", "org.springframework.scripting.groovy.GroovyScriptFactory");
/* 11:44 */     registerScriptBeanDefinitionParser("jruby", "org.springframework.scripting.jruby.JRubyScriptFactory");
/* 12:45 */     registerScriptBeanDefinitionParser("bsh", "org.springframework.scripting.bsh.BshScriptFactory");
/* 13:46 */     registerBeanDefinitionParser("defaults", new ScriptingDefaultsParser());
/* 14:   */   }
/* 15:   */   
/* 16:   */   private void registerScriptBeanDefinitionParser(String key, String scriptFactoryClassName)
/* 17:   */   {
/* 18:50 */     registerBeanDefinitionParser(key, new ScriptBeanDefinitionParser(scriptFactoryClassName));
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scripting.config.LangNamespaceHandler
 * JD-Core Version:    0.7.0.1
 */