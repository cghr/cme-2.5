/*   1:    */ package org.springframework.beans.factory.parsing;
/*   2:    */ 
/*   3:    */ import org.springframework.core.io.Resource;
/*   4:    */ 
/*   5:    */ public class ReaderContext
/*   6:    */ {
/*   7:    */   private final Resource resource;
/*   8:    */   private final ProblemReporter problemReporter;
/*   9:    */   private final ReaderEventListener eventListener;
/*  10:    */   private final SourceExtractor sourceExtractor;
/*  11:    */   
/*  12:    */   public ReaderContext(Resource resource, ProblemReporter problemReporter, ReaderEventListener eventListener, SourceExtractor sourceExtractor)
/*  13:    */   {
/*  14: 43 */     this.resource = resource;
/*  15: 44 */     this.problemReporter = problemReporter;
/*  16: 45 */     this.eventListener = eventListener;
/*  17: 46 */     this.sourceExtractor = sourceExtractor;
/*  18:    */   }
/*  19:    */   
/*  20:    */   public final Resource getResource()
/*  21:    */   {
/*  22: 50 */     return this.resource;
/*  23:    */   }
/*  24:    */   
/*  25:    */   public void fatal(String message, Object source)
/*  26:    */   {
/*  27: 55 */     fatal(message, source, null, null);
/*  28:    */   }
/*  29:    */   
/*  30:    */   public void fatal(String message, Object source, Throwable ex)
/*  31:    */   {
/*  32: 59 */     fatal(message, source, null, ex);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public void fatal(String message, Object source, ParseState parseState)
/*  36:    */   {
/*  37: 63 */     fatal(message, source, parseState, null);
/*  38:    */   }
/*  39:    */   
/*  40:    */   public void fatal(String message, Object source, ParseState parseState, Throwable cause)
/*  41:    */   {
/*  42: 67 */     Location location = new Location(getResource(), source);
/*  43: 68 */     this.problemReporter.fatal(new Problem(message, location, parseState, cause));
/*  44:    */   }
/*  45:    */   
/*  46:    */   public void error(String message, Object source)
/*  47:    */   {
/*  48: 72 */     error(message, source, null, null);
/*  49:    */   }
/*  50:    */   
/*  51:    */   public void error(String message, Object source, Throwable ex)
/*  52:    */   {
/*  53: 76 */     error(message, source, null, ex);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public void error(String message, Object source, ParseState parseState)
/*  57:    */   {
/*  58: 80 */     error(message, source, parseState, null);
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void error(String message, Object source, ParseState parseState, Throwable cause)
/*  62:    */   {
/*  63: 84 */     Location location = new Location(getResource(), source);
/*  64: 85 */     this.problemReporter.error(new Problem(message, location, parseState, cause));
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void warning(String message, Object source)
/*  68:    */   {
/*  69: 89 */     warning(message, source, null, null);
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void warning(String message, Object source, Throwable ex)
/*  73:    */   {
/*  74: 93 */     warning(message, source, null, ex);
/*  75:    */   }
/*  76:    */   
/*  77:    */   public void warning(String message, Object source, ParseState parseState)
/*  78:    */   {
/*  79: 97 */     warning(message, source, parseState, null);
/*  80:    */   }
/*  81:    */   
/*  82:    */   public void warning(String message, Object source, ParseState parseState, Throwable cause)
/*  83:    */   {
/*  84:101 */     Location location = new Location(getResource(), source);
/*  85:102 */     this.problemReporter.warning(new Problem(message, location, parseState, cause));
/*  86:    */   }
/*  87:    */   
/*  88:    */   public void fireDefaultsRegistered(DefaultsDefinition defaultsDefinition)
/*  89:    */   {
/*  90:107 */     this.eventListener.defaultsRegistered(defaultsDefinition);
/*  91:    */   }
/*  92:    */   
/*  93:    */   public void fireComponentRegistered(ComponentDefinition componentDefinition)
/*  94:    */   {
/*  95:111 */     this.eventListener.componentRegistered(componentDefinition);
/*  96:    */   }
/*  97:    */   
/*  98:    */   public void fireAliasRegistered(String beanName, String alias, Object source)
/*  99:    */   {
/* 100:115 */     this.eventListener.aliasRegistered(new AliasDefinition(beanName, alias, source));
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void fireImportProcessed(String importedResource, Object source)
/* 104:    */   {
/* 105:119 */     this.eventListener.importProcessed(new ImportDefinition(importedResource, source));
/* 106:    */   }
/* 107:    */   
/* 108:    */   public void fireImportProcessed(String importedResource, Resource[] actualResources, Object source)
/* 109:    */   {
/* 110:123 */     this.eventListener.importProcessed(new ImportDefinition(importedResource, actualResources, source));
/* 111:    */   }
/* 112:    */   
/* 113:    */   public SourceExtractor getSourceExtractor()
/* 114:    */   {
/* 115:128 */     return this.sourceExtractor;
/* 116:    */   }
/* 117:    */   
/* 118:    */   public Object extractSource(Object sourceCandidate)
/* 119:    */   {
/* 120:132 */     return this.sourceExtractor.extractSource(sourceCandidate, this.resource);
/* 121:    */   }
/* 122:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.ReaderContext
 * JD-Core Version:    0.7.0.1
 */