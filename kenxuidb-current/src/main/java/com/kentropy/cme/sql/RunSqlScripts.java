/*  1:   */ package com.kentropy.cme.sql;
/*  2:   */ 
/*  3:   */ import java.io.IOException;
/*  4:   */ import java.sql.SQLException;
/*  5:   */ import org.springframework.context.ApplicationContext;
/*  6:   */ import org.springframework.context.support.ClassPathXmlApplicationContext;
/*  7:   */ import org.springframework.jdbc.core.JdbcTemplate;
/*  8:   */ 
/*  9:   */ public class RunSqlScripts
/* 10:   */ {
/* 11:14 */   ApplicationContext context = new ClassPathXmlApplicationContext("appContext.xml");
/* 12:15 */   JdbcTemplate jt = (JdbcTemplate)this.context.getBean("jdbcTemp");
/* 13:   */   
/* 14:   */   public boolean executeDBScripts(String aSQLScriptFilePath)
/* 15:   */     throws IOException, SQLException
/* 16:   */   {
/* 17:27 */     throw new Error("Unresolved compilation problem: \n\tDuplicate local variable str\n");
/* 18:   */   }
/* 19:   */   
/* 20:   */   public static void main(String[] args)
/* 21:   */     throws IOException, SQLException
/* 22:   */   {
/* 23:43 */     new RunSqlScripts().executeDBScripts("/home/jaya/script.sql");
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\kenxuidb-current\kenxuidb-current.jar
 * Qualified Name:     com.kentropy.cme.sql.RunSqlScripts
 * JD-Core Version:    0.7.0.1
 */