/*   1:    */ package org.springframework.web.multipart.commons;
/*   2:    */ 
/*   3:    */ import java.io.File;
/*   4:    */ import java.io.IOException;
/*   5:    */ import java.io.UnsupportedEncodingException;
/*   6:    */ import java.nio.charset.Charset;
/*   7:    */ import java.util.Collection;
/*   8:    */ import java.util.HashMap;
/*   9:    */ import java.util.Iterator;
/*  10:    */ import java.util.List;
/*  11:    */ import java.util.Map;
/*  12:    */ import org.apache.commons.fileupload.FileItem;
/*  13:    */ import org.apache.commons.fileupload.FileItemFactory;
/*  14:    */ import org.apache.commons.fileupload.FileUpload;
/*  15:    */ import org.apache.commons.fileupload.disk.DiskFileItemFactory;
/*  16:    */ import org.apache.commons.logging.Log;
/*  17:    */ import org.apache.commons.logging.LogFactory;
/*  18:    */ import org.springframework.core.io.Resource;
/*  19:    */ import org.springframework.http.MediaType;
/*  20:    */ import org.springframework.util.LinkedMultiValueMap;
/*  21:    */ import org.springframework.util.MultiValueMap;
/*  22:    */ import org.springframework.util.StringUtils;
/*  23:    */ import org.springframework.web.multipart.MultipartFile;
/*  24:    */ 
/*  25:    */ public abstract class CommonsFileUploadSupport
/*  26:    */ {
/*  27: 63 */   protected final Log logger = LogFactory.getLog(getClass());
/*  28:    */   private final DiskFileItemFactory fileItemFactory;
/*  29:    */   private final FileUpload fileUpload;
/*  30: 69 */   private boolean uploadTempDirSpecified = false;
/*  31:    */   
/*  32:    */   public CommonsFileUploadSupport()
/*  33:    */   {
/*  34: 79 */     this.fileItemFactory = newFileItemFactory();
/*  35: 80 */     this.fileUpload = newFileUpload(getFileItemFactory());
/*  36:    */   }
/*  37:    */   
/*  38:    */   public DiskFileItemFactory getFileItemFactory()
/*  39:    */   {
/*  40: 90 */     return this.fileItemFactory;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public FileUpload getFileUpload()
/*  44:    */   {
/*  45: 99 */     return this.fileUpload;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setMaxUploadSize(long maxUploadSize)
/*  49:    */   {
/*  50:109 */     this.fileUpload.setSizeMax(maxUploadSize);
/*  51:    */   }
/*  52:    */   
/*  53:    */   public void setMaxInMemorySize(int maxInMemorySize)
/*  54:    */   {
/*  55:120 */     this.fileItemFactory.setSizeThreshold(maxInMemorySize);
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setDefaultEncoding(String defaultEncoding)
/*  59:    */   {
/*  60:138 */     this.fileUpload.setHeaderEncoding(defaultEncoding);
/*  61:    */   }
/*  62:    */   
/*  63:    */   protected String getDefaultEncoding()
/*  64:    */   {
/*  65:142 */     String encoding = getFileUpload().getHeaderEncoding();
/*  66:143 */     if (encoding == null) {
/*  67:144 */       encoding = "ISO-8859-1";
/*  68:    */     }
/*  69:146 */     return encoding;
/*  70:    */   }
/*  71:    */   
/*  72:    */   public void setUploadTempDir(Resource uploadTempDir)
/*  73:    */     throws IOException
/*  74:    */   {
/*  75:155 */     if ((!uploadTempDir.exists()) && (!uploadTempDir.getFile().mkdirs())) {
/*  76:156 */       throw new IllegalArgumentException("Given uploadTempDir [" + uploadTempDir + "] could not be created");
/*  77:    */     }
/*  78:158 */     this.fileItemFactory.setRepository(uploadTempDir.getFile());
/*  79:159 */     this.uploadTempDirSpecified = true;
/*  80:    */   }
/*  81:    */   
/*  82:    */   protected boolean isUploadTempDirSpecified()
/*  83:    */   {
/*  84:163 */     return this.uploadTempDirSpecified;
/*  85:    */   }
/*  86:    */   
/*  87:    */   protected DiskFileItemFactory newFileItemFactory()
/*  88:    */   {
/*  89:174 */     return new DiskFileItemFactory();
/*  90:    */   }
/*  91:    */   
/*  92:    */   protected abstract FileUpload newFileUpload(FileItemFactory paramFileItemFactory);
/*  93:    */   
/*  94:    */   protected FileUpload prepareFileUpload(String encoding)
/*  95:    */   {
/*  96:195 */     FileUpload fileUpload = getFileUpload();
/*  97:196 */     FileUpload actualFileUpload = fileUpload;
/*  98:200 */     if ((encoding != null) && (!encoding.equals(fileUpload.getHeaderEncoding())))
/*  99:    */     {
/* 100:201 */       actualFileUpload = newFileUpload(getFileItemFactory());
/* 101:202 */       actualFileUpload.setSizeMax(fileUpload.getSizeMax());
/* 102:203 */       actualFileUpload.setHeaderEncoding(encoding);
/* 103:    */     }
/* 104:206 */     return actualFileUpload;
/* 105:    */   }
/* 106:    */   
/* 107:    */   protected MultipartParsingResult parseFileItems(List<FileItem> fileItems, String encoding)
/* 108:    */   {
/* 109:218 */     MultiValueMap<String, MultipartFile> multipartFiles = new LinkedMultiValueMap();
/* 110:219 */     Map<String, String[]> multipartParameters = new HashMap();
/* 111:220 */     Map<String, String> multipartParameterContentTypes = new HashMap();
/* 112:223 */     for (FileItem fileItem : fileItems) {
/* 113:224 */       if (fileItem.isFormField())
/* 114:    */       {
/* 115:226 */         String partEncoding = determineEncoding(fileItem.getContentType(), encoding);
/* 116:    */         String value;
/* 117:227 */         if (partEncoding != null)
/* 118:    */         {
/* 119:    */           String value;
/* 120:    */           try
/* 121:    */           {
/* 122:229 */             value = fileItem.getString(partEncoding);
/* 123:    */           }
/* 124:    */           catch (UnsupportedEncodingException localUnsupportedEncodingException)
/* 125:    */           {
/* 126:    */             String value;
/* 127:232 */             if (this.logger.isWarnEnabled()) {
/* 128:233 */               this.logger.warn("Could not decode multipart item '" + fileItem.getFieldName() + 
/* 129:234 */                 "' with encoding '" + partEncoding + "': using platform default");
/* 130:    */             }
/* 131:236 */             value = fileItem.getString();
/* 132:    */           }
/* 133:    */         }
/* 134:    */         else
/* 135:    */         {
/* 136:240 */           value = fileItem.getString();
/* 137:    */         }
/* 138:242 */         String[] curParam = (String[])multipartParameters.get(fileItem.getFieldName());
/* 139:243 */         if (curParam == null)
/* 140:    */         {
/* 141:245 */           multipartParameters.put(fileItem.getFieldName(), new String[] { value });
/* 142:    */         }
/* 143:    */         else
/* 144:    */         {
/* 145:249 */           String[] newParam = StringUtils.addStringToArray(curParam, value);
/* 146:250 */           multipartParameters.put(fileItem.getFieldName(), newParam);
/* 147:    */         }
/* 148:252 */         multipartParameterContentTypes.put(fileItem.getFieldName(), fileItem.getContentType());
/* 149:    */       }
/* 150:    */       else
/* 151:    */       {
/* 152:256 */         CommonsMultipartFile file = new CommonsMultipartFile(fileItem);
/* 153:257 */         multipartFiles.add(file.getName(), file);
/* 154:258 */         if (this.logger.isDebugEnabled()) {
/* 155:259 */           this.logger.debug("Found multipart file [" + file.getName() + "] of size " + file.getSize() + 
/* 156:260 */             " bytes with original filename [" + file.getOriginalFilename() + "], stored " + 
/* 157:261 */             file.getStorageDescription());
/* 158:    */         }
/* 159:    */       }
/* 160:    */     }
/* 161:265 */     return new MultipartParsingResult(multipartFiles, multipartParameters, multipartParameterContentTypes);
/* 162:    */   }
/* 163:    */   
/* 164:    */   protected void cleanupFileItems(MultiValueMap<String, MultipartFile> multipartFiles)
/* 165:    */   {
/* 166:    */     Iterator localIterator2;
/* 167:276 */     for (Iterator localIterator1 = multipartFiles.values().iterator(); localIterator1.hasNext(); localIterator2.hasNext())
/* 168:    */     {
/* 169:276 */       List<MultipartFile> files = (List)localIterator1.next();
/* 170:277 */       localIterator2 = files.iterator(); continue;MultipartFile file = (MultipartFile)localIterator2.next();
/* 171:278 */       if ((file instanceof CommonsMultipartFile))
/* 172:    */       {
/* 173:279 */         CommonsMultipartFile cmf = (CommonsMultipartFile)file;
/* 174:280 */         cmf.getFileItem().delete();
/* 175:281 */         if (this.logger.isDebugEnabled()) {
/* 176:282 */           this.logger.debug("Cleaning up multipart file [" + cmf.getName() + "] with original filename [" + 
/* 177:283 */             cmf.getOriginalFilename() + "], stored " + cmf.getStorageDescription());
/* 178:    */         }
/* 179:    */       }
/* 180:    */     }
/* 181:    */   }
/* 182:    */   
/* 183:    */   private String determineEncoding(String contentTypeHeader, String defaultEncoding)
/* 184:    */   {
/* 185:291 */     if (!StringUtils.hasText(contentTypeHeader)) {
/* 186:292 */       return defaultEncoding;
/* 187:    */     }
/* 188:294 */     MediaType contentType = MediaType.parseMediaType(contentTypeHeader);
/* 189:295 */     Charset charset = contentType.getCharSet();
/* 190:296 */     return charset != null ? charset.name() : defaultEncoding;
/* 191:    */   }
/* 192:    */   
/* 193:    */   protected static class MultipartParsingResult
/* 194:    */   {
/* 195:    */     private final MultiValueMap<String, MultipartFile> multipartFiles;
/* 196:    */     private final Map<String, String[]> multipartParameters;
/* 197:    */     private final Map<String, String> multipartParameterContentTypes;
/* 198:    */     
/* 199:    */     public MultipartParsingResult(MultiValueMap<String, MultipartFile> mpFiles, Map<String, String[]> mpParams, Map<String, String> mpParamContentTypes)
/* 200:    */     {
/* 201:314 */       this.multipartFiles = mpFiles;
/* 202:315 */       this.multipartParameters = mpParams;
/* 203:316 */       this.multipartParameterContentTypes = mpParamContentTypes;
/* 204:    */     }
/* 205:    */     
/* 206:    */     public MultiValueMap<String, MultipartFile> getMultipartFiles()
/* 207:    */     {
/* 208:320 */       return this.multipartFiles;
/* 209:    */     }
/* 210:    */     
/* 211:    */     public Map<String, String[]> getMultipartParameters()
/* 212:    */     {
/* 213:324 */       return this.multipartParameters;
/* 214:    */     }
/* 215:    */     
/* 216:    */     public Map<String, String> getMultipartParameterContentTypes()
/* 217:    */     {
/* 218:328 */       return this.multipartParameterContentTypes;
/* 219:    */     }
/* 220:    */   }
/* 221:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.multipart.commons.CommonsFileUploadSupport
 * JD-Core Version:    0.7.0.1
 */