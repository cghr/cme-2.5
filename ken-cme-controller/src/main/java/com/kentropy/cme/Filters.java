/*  1:   */ package com.kentropy.cme;
/*  2:   */ 
/*  3:   */ import com.kentropy.util.SpringUtils;
/*  4:   */ import org.springframework.jdbc.core.JdbcTemplate;
/*  5:   */ 
/*  6:   */ public class Filters
/*  7:   */ {
/*  8:13 */   JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  9:   */   
/* 10:   */   public void applyFilterForAbove70Yrs()
/* 11:   */   {
/* 12:18 */     StringBuffer sql = new StringBuffer();
/* 13:   */     
/* 14:20 */     sql.append("UPDATE cme_records SET status='filtered',comments='Above 70YRS'  WHERE status  IS NULL AND ( ");
/* 15:22 */     for (int i = 70; i <= 100; i++) {
/* 16:24 */       if (i != 100) {
/* 17:25 */         sql.append("`value1` LIKE   '%death_age=" + i + "%' OR ");
/* 18:   */       } else {
/* 19:27 */         sql.append("`value1` LIKE   '%death_age=" + i + "%')");
/* 20:   */       }
/* 21:   */     }
/* 22:32 */     this.jt.update(sql.toString());
/* 23:   */   }
/* 24:   */   
/* 25:   */   public static void main(String[] args)
/* 26:   */   {
/* 27:42 */     new Filters().applyFilterForAbove70Yrs();
/* 28:   */   }
/* 29:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-cme-controller\ken-cme-controller.jar
 * Qualified Name:     com.kentropy.cme.Filters
 * JD-Core Version:    0.7.0.1
 */