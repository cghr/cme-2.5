/*  1:   */ package org.springframework.beans.factory.parsing;
/*  2:   */ 
/*  3:   */ import java.util.LinkedList;
/*  4:   */ import java.util.List;
/*  5:   */ import org.springframework.util.Assert;
/*  6:   */ 
/*  7:   */ public class CompositeComponentDefinition
/*  8:   */   extends AbstractComponentDefinition
/*  9:   */ {
/* 10:   */   private final String name;
/* 11:   */   private final Object source;
/* 12:39 */   private final List<ComponentDefinition> nestedComponents = new LinkedList();
/* 13:   */   
/* 14:   */   public CompositeComponentDefinition(String name, Object source)
/* 15:   */   {
/* 16:48 */     Assert.notNull(name, "Name must not be null");
/* 17:49 */     this.name = name;
/* 18:50 */     this.source = source;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public String getName()
/* 22:   */   {
/* 23:55 */     return this.name;
/* 24:   */   }
/* 25:   */   
/* 26:   */   public Object getSource()
/* 27:   */   {
/* 28:59 */     return this.source;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public void addNestedComponent(ComponentDefinition component)
/* 32:   */   {
/* 33:68 */     Assert.notNull(component, "ComponentDefinition must not be null");
/* 34:69 */     this.nestedComponents.add(component);
/* 35:   */   }
/* 36:   */   
/* 37:   */   public ComponentDefinition[] getNestedComponents()
/* 38:   */   {
/* 39:77 */     return (ComponentDefinition[])this.nestedComponents.toArray(new ComponentDefinition[this.nestedComponents.size()]);
/* 40:   */   }
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.parsing.CompositeComponentDefinition
 * JD-Core Version:    0.7.0.1
 */