/*   1:    */ package org.springframework.scripting.jruby;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import org.jruby.RubyException;
/*   5:    */ import org.jruby.exceptions.JumpException;
/*   6:    */ import org.jruby.exceptions.RaiseException;
/*   7:    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*   8:    */ import org.springframework.scripting.ScriptCompilationException;
/*   9:    */ import org.springframework.scripting.ScriptFactory;
/*  10:    */ import org.springframework.scripting.ScriptSource;
/*  11:    */ import org.springframework.util.Assert;
/*  12:    */ import org.springframework.util.ClassUtils;
/*  13:    */ 
/*  14:    */ public class JRubyScriptFactory
/*  15:    */   implements ScriptFactory, BeanClassLoaderAware
/*  16:    */ {
/*  17:    */   private final String scriptSourceLocator;
/*  18:    */   private final Class[] scriptInterfaces;
/*  19: 52 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*  20:    */   
/*  21:    */   public JRubyScriptFactory(String scriptSourceLocator, Class[] scriptInterfaces)
/*  22:    */   {
/*  23: 63 */     Assert.hasText(scriptSourceLocator, "'scriptSourceLocator' must not be empty");
/*  24: 64 */     Assert.notEmpty(scriptInterfaces, "'scriptInterfaces' must not be empty");
/*  25: 65 */     this.scriptSourceLocator = scriptSourceLocator;
/*  26: 66 */     this.scriptInterfaces = scriptInterfaces;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setBeanClassLoader(ClassLoader classLoader)
/*  30:    */   {
/*  31: 71 */     this.beanClassLoader = classLoader;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public String getScriptSourceLocator()
/*  35:    */   {
/*  36: 76 */     return this.scriptSourceLocator;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public Class[] getScriptInterfaces()
/*  40:    */   {
/*  41: 80 */     return this.scriptInterfaces;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public boolean requiresConfigInterface()
/*  45:    */   {
/*  46: 87 */     return true;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public Object getScriptedObject(ScriptSource scriptSource, Class[] actualInterfaces)
/*  50:    */     throws IOException, ScriptCompilationException
/*  51:    */   {
/*  52:    */     try
/*  53:    */     {
/*  54: 97 */       return JRubyScriptUtils.createJRubyObject(
/*  55: 98 */         scriptSource.getScriptAsString(), actualInterfaces, this.beanClassLoader);
/*  56:    */     }
/*  57:    */     catch (RaiseException ex)
/*  58:    */     {
/*  59:101 */       RubyException rubyEx = ex.getException();
/*  60:102 */       String msg = (rubyEx != null) && (rubyEx.message != null) ? 
/*  61:103 */         rubyEx.message.toString() : "Unexpected JRuby error";
/*  62:104 */       throw new ScriptCompilationException(scriptSource, msg, ex);
/*  63:    */     }
/*  64:    */     catch (JumpException ex)
/*  65:    */     {
/*  66:107 */       throw new ScriptCompilationException(scriptSource, ex);
/*  67:    */     }
/*  68:    */   }
/*  69:    */   
/*  70:    */   public Class getScriptedObjectType(ScriptSource scriptSource)
/*  71:    */     throws IOException, ScriptCompilationException
/*  72:    */   {
/*  73:114 */     return null;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public boolean requiresScriptedObjectRefresh(ScriptSource scriptSource)
/*  77:    */   {
/*  78:118 */     return scriptSource.isModified();
/*  79:    */   }
/*  80:    */   
/*  81:    */   public String toString()
/*  82:    */   {
/*  83:124 */     return "JRubyScriptFactory: script source locator [" + this.scriptSourceLocator + "]";
/*  84:    */   }
/*  85:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scripting.jruby.JRubyScriptFactory
 * JD-Core Version:    0.7.0.1
 */