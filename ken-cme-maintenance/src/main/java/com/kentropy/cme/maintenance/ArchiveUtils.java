/*   1:    */ package com.kentropy.cme.maintenance;
/*   2:    */ 
/*   3:    */ import com.kentropy.util.DbUtil;
/*   4:    */ import com.kentropy.util.SpringApplicationContext;
/*   5:    */ import com.kentropy.util.SpringUtils;
/*   6:    */ import com.kentropy.util.ZIPUtils;
/*   7:    */ import java.io.PrintStream;
/*   8:    */ import java.text.SimpleDateFormat;
/*   9:    */ import java.util.ArrayList;
/*  10:    */ import java.util.Date;
/*  11:    */ import java.util.List;
/*  12:    */ import javax.servlet.http.HttpServletRequest;
/*  13:    */ import javax.servlet.http.HttpSession;
/*  14:    */ import org.springframework.jdbc.core.JdbcTemplate;
/*  15:    */ import org.springframework.jdbc.support.rowset.SqlRowSet;
/*  16:    */ 
/*  17:    */ public class ArchiveUtils
/*  18:    */ {
/*  19: 30 */   JdbcTemplate jt = new SpringUtils().getJdbcTemplate();
/*  20: 31 */   ZIPUtils zipUtils = new ZIPUtils();
/*  21: 32 */   String basepath = (String)SpringApplicationContext.getBean("basepath");
/*  22:    */   HttpServletRequest request;
/*  23:    */   HttpSession session;
/*  24: 35 */   String imageArchivePath = (String)SpringApplicationContext.getBean("image_archive");
/*  25: 36 */   DbUtil db = new DbUtil();
/*  26:    */   
/*  27:    */   public HttpServletRequest getRequest()
/*  28:    */   {
/*  29: 40 */     return this.request;
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setRequest(HttpServletRequest request)
/*  33:    */   {
/*  34: 45 */     this.request = request;
/*  35: 46 */     this.session = this.request.getSession();
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void archiveCMERecordsIncludingImages(String year, String month)
/*  39:    */   {
/*  40: 58 */     String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
/*  41:    */     
/*  42: 60 */     String forYearMonth = year + "-" + month;
/*  43: 61 */     String archiveTable = "`keyvalue_" + forYearMonth + "_" + date + "`";
/*  44: 62 */     String imageArchiveName = "images_" + forYearMonth + "_" + date + ".zip";
/*  45:    */     
/*  46:    */ 
/*  47:    */ 
/*  48:    */ 
/*  49:    */ 
/*  50:    */ 
/*  51: 69 */     List records = this.db.getDataAsListofStringBufferArrays("cme_report  a LEFT JOIN `process` b ON a.uniqno=b.pid", "uniqno", " b.stateMachine LIKE '%complete%'  AND DATE_FORMAT(a.time1,'%Y-%m')=?  GROUP BY  uniqno  ", new Object[] { forYearMonth });
/*  52:    */     
/*  53:    */ 
/*  54:    */ 
/*  55: 73 */     int rowcount = 0;
/*  56: 74 */     archiveKeyvalue(records, archiveTable);
/*  57: 75 */     archiveImages(records, this.imageArchivePath, imageArchiveName);
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void archiveKeyvalue(List records, String archiveTable)
/*  61:    */   {
/*  62: 91 */     String sql = "";
/*  63: 92 */     int current_count = 0;
/*  64: 93 */     int total_count = records.size();
/*  65:    */     
/*  66: 95 */     this.jt.execute("CREATE TABLE IF NOT EXISTS  " + archiveTable + " LIKE keyvalue");
/*  67: 97 */     for (int i = 0; i < records.size(); i++)
/*  68:    */     {
/*  69:100 */       StringBuffer uniqno = ((StringBuffer[])records.get(i))[0];
/*  70:101 */       sql = "INSERT IGNORE INTO   " + archiveTable + " SELECT * FROM keyvalue WHERE  key1 LIKE '%/" + uniqno + "/%'";
/*  71:102 */       this.jt.update(sql);
/*  72:    */       
/*  73:104 */       current_count++;
/*  74:105 */       this.session.setAttribute("archive_status", " keyvalue for cme_records -" + current_count + " - " + total_count);
/*  75:106 */       System.out.println(current_count + " " + total_count);
/*  76:    */     }
/*  77:110 */     System.out.println("Archiving keyvalue data completed...");
/*  78:    */   }
/*  79:    */   
/*  80:    */   public void deleteKeyvalue(String uniqno)
/*  81:    */   {
/*  82:119 */     String sql = "DELETE FROM keyvalue WHERE  key1 LIKE '%/" + uniqno + "/%'";
/*  83:    */     
/*  84:121 */     this.jt.update(sql);
/*  85:    */   }
/*  86:    */   
/*  87:    */   public void archiveImages(List records, String archivePath, String imageArchiveName)
/*  88:    */   {
/*  89:128 */     System.out.println("Starting Archiving Images ...");
/*  90:    */     
/*  91:130 */     String pathToImages = (String)SpringApplicationContext.getBean("basepath") + "/data/split/";
/*  92:131 */     List allImages = new ArrayList();
/*  93:132 */     String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
/*  94:135 */     for (int i = 0; i < records.size(); i++)
/*  95:    */     {
/*  96:137 */       StringBuffer uniqno = ((StringBuffer[])records.get(i))[0];
/*  97:    */       
/*  98:139 */       allImages.add(uniqno + "_0_blank.png");
/*  99:140 */       allImages.add(uniqno + "_1_blank.png");
/* 100:141 */       allImages.add(uniqno + "_cod.png");
/* 101:    */     }
/* 102:147 */     this.zipUtils.createZIP(" Images ", this.imageArchivePath, imageArchiveName, pathToImages, allImages, this.session);
/* 103:    */   }
/* 104:    */   
/* 105:    */   public void archiveMqueueXmlFiles(String year, String month)
/* 106:    */   {
/* 107:158 */     String forYearMonth = year + "-" + month;
/* 108:159 */     String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
/* 109:    */     
/* 110:161 */     String pathToZipFileFolder = (String)SpringApplicationContext.getBean("mqueue_archive");
/* 111:    */     
/* 112:163 */     String zipArchiveName = "mqueue_" + forYearMonth + "_" + date + ".zip";
/* 113:    */     
/* 114:    */ 
/* 115:166 */     String pathtoXmlFilesFolder = this.basepath + "/mbox/";
/* 116:167 */     String sql = "SELECT message FROM mqueue WHERE message LIKE '%.xml' AND ( (DATE_FORMAT(delivertime,'%Y-%m')=? OR DATE_FORMAT(downloadtime,'%Y-%m')=?)) ";
/* 117:    */     
/* 118:169 */     SqlRowSet rs = this.jt.queryForRowSet(sql, new Object[] { forYearMonth, forYearMonth });
/* 119:170 */     List fileNames = new ArrayList();
/* 120:174 */     while (rs.next())
/* 121:    */     {
/* 122:176 */       fileNames.add(rs.getString("message"));
/* 123:177 */       System.out.println(rs.getString("message"));
/* 124:    */     }
/* 125:180 */     this.zipUtils.createZIP("Mqueue XML Files", pathToZipFileFolder, zipArchiveName, pathtoXmlFilesFolder, fileNames, this.session);
/* 126:181 */     System.gc();
/* 127:    */   }
/* 128:    */   
/* 129:    */   public static void main(String[] args)
/* 130:    */   {
/* 131:194 */     ArchiveUtils arch = new ArchiveUtils();
/* 132:195 */     arch.archiveCMERecordsIncludingImages("2012", "07");
/* 133:    */   }
/* 134:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-cme-maintenance\ken-cme-maintenance.jar
 * Qualified Name:     com.kentropy.cme.maintenance.ArchiveUtils
 * JD-Core Version:    0.7.0.1
 */