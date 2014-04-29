/*  1:   */ package org.springframework.scripting.support;
/*  2:   */ 
/*  3:   */ import org.springframework.scripting.ScriptSource;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ 
/*  6:   */ public class StaticScriptSource
/*  7:   */   implements ScriptSource
/*  8:   */ {
/*  9:   */   private String script;
/* 10:   */   private boolean modified;
/* 11:   */   private String className;
/* 12:   */   
/* 13:   */   public StaticScriptSource(String script)
/* 14:   */   {
/* 15:46 */     setScript(script);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public StaticScriptSource(String script, String className)
/* 19:   */   {
/* 20:56 */     setScript(script);
/* 21:57 */     this.className = className;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public synchronized void setScript(String script)
/* 25:   */   {
/* 26:65 */     Assert.hasText(script, "Script must not be empty");
/* 27:66 */     this.modified = (!script.equals(this.script));
/* 28:67 */     this.script = script;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public synchronized String getScriptAsString()
/* 32:   */   {
/* 33:72 */     this.modified = false;
/* 34:73 */     return this.script;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public synchronized boolean isModified()
/* 38:   */   {
/* 39:77 */     return this.modified;
/* 40:   */   }
/* 41:   */   
/* 42:   */   public String suggestedClassName()
/* 43:   */   {
/* 44:81 */     return this.className;
/* 45:   */   }
/* 46:   */   
/* 47:   */   public String toString()
/* 48:   */   {
/* 49:87 */     return "static script" + (this.className != null ? " [" + this.className + "]" : "");
/* 50:   */   }
/* 51:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scripting.support.StaticScriptSource
 * JD-Core Version:    0.7.0.1
 */