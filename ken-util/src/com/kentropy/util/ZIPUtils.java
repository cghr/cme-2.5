/*   1:    */ package com.kentropy.util;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.FileInputStream;
/*   5:    */ import java.io.FileNotFoundException;
/*   6:    */ import java.io.FileOutputStream;
/*   7:    */ import java.io.IOException;
/*   8:    */ import java.text.SimpleDateFormat;
/*   9:    */ import java.util.ArrayList;
/*  10:    */ import java.util.Date;
/*  11:    */ import java.util.HashMap;
/*  12:    */ import java.util.List;
/*  13:    */ import java.util.Map;
/*  14:    */ import java.util.zip.ZipEntry;
/*  15:    */ import java.util.zip.ZipOutputStream;
/*  16:    */ import javax.servlet.http.HttpSession;
/*  17:    */ 
/*  18:    */ public class ZIPUtils
/*  19:    */ {
/*  20: 23 */   DbUtil db = new DbUtil();
/*  21:    */   
/*  22:    */   public void createZIP(String pathToZIP, String parentFolder, String[] fileNames)
/*  23:    */     throws IOException
/*  24:    */   {
/*  25: 29 */     byte[] buffer = new byte[1024];
/*  26:    */     
/*  27:    */ 
/*  28: 32 */     FileOutputStream fout = new FileOutputStream(pathToZIP);
/*  29: 33 */     ZipOutputStream zout = new ZipOutputStream(fout);
/*  30: 35 */     for (int i = 0; i < fileNames.length; i++)
/*  31:    */     {
/*  32: 38 */       File file = new File(parentFolder, fileNames[i]);
/*  33:    */       
/*  34:    */ 
/*  35:    */ 
/*  36: 42 */       FileInputStream fin = new FileInputStream(file);
/*  37: 43 */       zout.putNextEntry(new ZipEntry(fileNames[i]));
/*  38:    */       int length;
/*  39: 46 */       while ((length = fin.read(buffer)) > 0)
/*  40:    */       {
/*  41:    */         int length;
/*  42: 48 */         zout.write(buffer, 0, length);
/*  43:    */       }
/*  44: 50 */       zout.closeEntry();
/*  45: 51 */       fin.close();
/*  46:    */     }
/*  47: 57 */     zout.close();
/*  48:    */   }
/*  49:    */   
/*  50:    */   public void createZIP(String archiveType, String pathToZipFileFolder, String zipFileName, String parentFolderForEntries, List fileNames, HttpSession session)
/*  51:    */   {
/*  52: 70 */     byte[] buffer = new byte[1024];
/*  53: 71 */     String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
/*  54: 72 */     FileOutputStream fout = null;
/*  55:    */     
/*  56: 74 */     session.setAttribute("archive_status", archiveType + "-0-0");
/*  57:    */     
/*  58: 76 */     Map map = new HashMap();
/*  59: 77 */     map.put("id", timestamp);
/*  60: 78 */     map.put("path_to_zip_file_folder", pathToZipFileFolder);
/*  61: 79 */     map.put("name", zipFileName);
/*  62: 80 */     map.put("path_to_zipentry_files_folder", parentFolderForEntries);
/*  63: 81 */     String zipCreationComments = "NO ERRORS";
/*  64:    */     
/*  65: 83 */     this.db.saveDataFromMap("zip_log", "id='" + timestamp + "'", map);
/*  66:    */     try
/*  67:    */     {
/*  68: 88 */       fout = new FileOutputStream(pathToZipFileFolder + zipFileName);
/*  69:    */     }
/*  70:    */     catch (Exception e)
/*  71:    */     {
/*  72: 91 */       map.clear();
/*  73: 92 */       map.put("id", timestamp);
/*  74: 93 */       map.put("comments", "Path to zip file doesn't exist");
/*  75: 94 */       map.put("status", "failed");
/*  76: 95 */       this.db.saveDataFromMap("zip_log", "id='" + timestamp + "'", map);
/*  77:    */       
/*  78: 97 */       e.printStackTrace();
/*  79:    */     }
/*  80:100 */     ZipOutputStream zout = new ZipOutputStream(fout);
/*  81:    */     
/*  82:102 */     int total_file_count = fileNames.size();
/*  83:103 */     int current_file_count = 0;
/*  84:105 */     for (int i = 0; i < fileNames.size(); i++)
/*  85:    */     {
/*  86:108 */       File file = new File(parentFolderForEntries, (String)fileNames.get(i));
/*  87:109 */       session.setAttribute("archive_status", archiveType + "-" + current_file_count + "-" + total_file_count);
/*  88:110 */       current_file_count++;
/*  89:    */       
/*  90:112 */       FileInputStream fin = null;
/*  91:    */       try
/*  92:    */       {
/*  93:115 */         fin = new FileInputStream(file);
/*  94:116 */         zout.putNextEntry(new ZipEntry((String)fileNames.get(i)));
/*  95:    */         int length;
/*  96:119 */         while ((length = fin.read(buffer)) > 0)
/*  97:    */         {
/*  98:    */           int length;
/*  99:121 */           zout.write(buffer, 0, length);
/* 100:    */         }
/* 101:123 */         zout.closeEntry();
/* 102:124 */         fin.close();
/* 103:    */         
/* 104:    */ 
/* 105:127 */         map.clear();
/* 106:128 */         map.put("ref_id", timestamp);
/* 107:129 */         map.put("name", fileNames.get(i));
/* 108:130 */         map.put("status", "success");
/* 109:    */         
/* 110:132 */         this.db.saveDataFromMap("zip_entry_log", null, map);
/* 111:    */       }
/* 112:    */       catch (FileNotFoundException e)
/* 113:    */       {
/* 114:139 */         map.clear();
/* 115:140 */         map.put("ref_id", timestamp);
/* 116:141 */         map.put("name", fileNames.get(i));
/* 117:142 */         map.put("status", "failure");
/* 118:143 */         zipCreationComments = "ERRORS";
/* 119:    */         
/* 120:145 */         this.db.saveDataFromMap("zip_entry_log", null, map);
/* 121:    */         
/* 122:    */ 
/* 123:148 */         e.printStackTrace();
/* 124:    */       }
/* 125:    */       catch (IOException e)
/* 126:    */       {
/* 127:152 */         e.printStackTrace();
/* 128:    */       }
/* 129:    */     }
/* 130:164 */     map.clear();
/* 131:165 */     map.put("id", timestamp);
/* 132:166 */     map.put("comments", zipCreationComments);
/* 133:167 */     map.put("status", "success");
/* 134:168 */     this.db.saveDataFromMap("zip_log", "id='" + timestamp + "'", map);
/* 135:    */     
/* 136:    */ 
/* 137:    */ 
/* 138:172 */     session.setAttribute("archive_status", archiveType + "-" + current_file_count + "-" + total_file_count);
/* 139:    */     try
/* 140:    */     {
/* 141:174 */       zout.close();
/* 142:    */     }
/* 143:    */     catch (IOException e)
/* 144:    */     {
/* 145:177 */       e.printStackTrace();
/* 146:    */     }
/* 147:    */   }
/* 148:    */   
/* 149:    */   public void logZip(String id, String pathToZipFile, String fileCount, String comments, String status)
/* 150:    */   {
/* 151:188 */     Map row = new HashMap();
/* 152:    */     
/* 153:190 */     row.put("id", id);
/* 154:191 */     row.put(pathToZipFile, pathToZipFile);
/* 155:    */   }
/* 156:    */   
/* 157:    */   public static void main(String[] args)
/* 158:    */     throws IOException
/* 159:    */   {
/* 160:199 */     ZIPUtils ziputil = new ZIPUtils();
/* 161:200 */     List files = new ArrayList();
/* 162:201 */     files.add("call.txt");
/* 163:202 */     files.add("cme_adult.csv");
/* 164:203 */     files.add("cme_child.csv");
/* 165:204 */     files.add("grid.xls");
/* 166:    */     
/* 167:    */ 
/* 168:    */ 
/* 169:208 */     ziputil.createZIP("images", "/home/navaneeth/zip/", "test.zip", "/home/navaneeth/zip/", files, null);
/* 170:    */   }
/* 171:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-util\ken-util.jar
 * Qualified Name:     com.kentropy.util.ZIPUtils
 * JD-Core Version:    0.7.0.1
 */