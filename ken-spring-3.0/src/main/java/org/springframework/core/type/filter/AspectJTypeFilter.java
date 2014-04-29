/*  1:   */ package org.springframework.core.type.filter;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import org.aspectj.bridge.IMessageHandler;
/*  5:   */ import org.aspectj.weaver.ResolvedType;
/*  6:   */ import org.aspectj.weaver.World;
/*  7:   */ import org.aspectj.weaver.bcel.BcelWorld;
/*  8:   */ import org.aspectj.weaver.patterns.Bindings;
/*  9:   */ import org.aspectj.weaver.patterns.FormalBinding;
/* 10:   */ import org.aspectj.weaver.patterns.IScope;
/* 11:   */ import org.aspectj.weaver.patterns.PatternParser;
/* 12:   */ import org.aspectj.weaver.patterns.SimpleScope;
/* 13:   */ import org.aspectj.weaver.patterns.TypePattern;
/* 14:   */ import org.springframework.core.type.ClassMetadata;
/* 15:   */ import org.springframework.core.type.classreading.MetadataReader;
/* 16:   */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/* 17:   */ 
/* 18:   */ public class AspectJTypeFilter
/* 19:   */   implements TypeFilter
/* 20:   */ {
/* 21:   */   private final World world;
/* 22:   */   private final TypePattern typePattern;
/* 23:   */   
/* 24:   */   public AspectJTypeFilter(String typePatternExpression, ClassLoader classLoader)
/* 25:   */   {
/* 26:53 */     this.world = new BcelWorld(classLoader, IMessageHandler.THROW, null);
/* 27:54 */     this.world.setBehaveInJava5Way(true);
/* 28:55 */     PatternParser patternParser = new PatternParser(typePatternExpression);
/* 29:56 */     TypePattern typePattern = patternParser.parseTypePattern();
/* 30:57 */     typePattern.resolve(this.world);
/* 31:58 */     IScope scope = new SimpleScope(this.world, new FormalBinding[0]);
/* 32:59 */     this.typePattern = typePattern.resolveBindings(scope, Bindings.NONE, false, false);
/* 33:   */   }
/* 34:   */   
/* 35:   */   public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
/* 36:   */     throws IOException
/* 37:   */   {
/* 38:66 */     String className = metadataReader.getClassMetadata().getClassName();
/* 39:67 */     ResolvedType resolvedType = this.world.resolve(className);
/* 40:68 */     return this.typePattern.matchesStatically(resolvedType);
/* 41:   */   }
/* 42:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.filter.AspectJTypeFilter
 * JD-Core Version:    0.7.0.1
 */