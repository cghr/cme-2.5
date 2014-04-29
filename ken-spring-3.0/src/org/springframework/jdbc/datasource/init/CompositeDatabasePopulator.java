/*  1:   */ package org.springframework.jdbc.datasource.init;
/*  2:   */ 
/*  3:   */ import java.sql.Connection;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ import java.util.ArrayList;
/*  6:   */ import java.util.Arrays;
/*  7:   */ import java.util.Collection;
/*  8:   */ import java.util.List;
/*  9:   */ 
/* 10:   */ public class CompositeDatabasePopulator
/* 11:   */   implements DatabasePopulator
/* 12:   */ {
/* 13:34 */   private List<DatabasePopulator> populators = new ArrayList();
/* 14:   */   
/* 15:   */   public void setPopulators(DatabasePopulator... populators)
/* 16:   */   {
/* 17:41 */     this.populators.clear();
/* 18:42 */     this.populators.addAll((Collection)Arrays.asList(populators));
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void addPopulators(DatabasePopulator... populators)
/* 22:   */   {
/* 23:49 */     this.populators.addAll((Collection)Arrays.asList(populators));
/* 24:   */   }
/* 25:   */   
/* 26:   */   public void populate(Connection connection)
/* 27:   */     throws SQLException
/* 28:   */   {
/* 29:54 */     for (DatabasePopulator populator : this.populators) {
/* 30:55 */       populator.populate(connection);
/* 31:   */     }
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.datasource.init.CompositeDatabasePopulator
 * JD-Core Version:    0.7.0.1
 */