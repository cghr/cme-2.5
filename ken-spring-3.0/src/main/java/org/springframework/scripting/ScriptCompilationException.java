/*  1:   */ package org.springframework.scripting;
/*  2:   */ 
/*  3:   */ import org.springframework.core.NestedRuntimeException;
/*  4:   */ 
/*  5:   */ public class ScriptCompilationException
/*  6:   */   extends NestedRuntimeException
/*  7:   */ {
/*  8:   */   private ScriptSource scriptSource;
/*  9:   */   
/* 10:   */   public ScriptCompilationException(String msg)
/* 11:   */   {
/* 12:37 */     super(msg);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public ScriptCompilationException(String msg, Throwable cause)
/* 16:   */   {
/* 17:47 */     super(msg, cause);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public ScriptCompilationException(ScriptSource scriptSource, Throwable cause)
/* 21:   */   {
/* 22:57 */     super("Could not compile script", cause);
/* 23:58 */     this.scriptSource = scriptSource;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public ScriptCompilationException(ScriptSource scriptSource, String msg, Throwable cause)
/* 27:   */   {
/* 28:69 */     super("Could not compile script [" + scriptSource + "]: " + msg, cause);
/* 29:70 */     this.scriptSource = scriptSource;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public ScriptSource getScriptSource()
/* 33:   */   {
/* 34:79 */     return this.scriptSource;
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scripting.ScriptCompilationException
 * JD-Core Version:    0.7.0.1
 */