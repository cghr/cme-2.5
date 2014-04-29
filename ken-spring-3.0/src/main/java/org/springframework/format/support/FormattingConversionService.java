/*   1:    */ package org.springframework.format.support;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.text.ParseException;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Set;
/*   8:    */ import java.util.concurrent.ConcurrentHashMap;
/*   9:    */ import org.springframework.context.EmbeddedValueResolverAware;
/*  10:    */ import org.springframework.context.i18n.LocaleContextHolder;
/*  11:    */ import org.springframework.core.GenericTypeResolver;
/*  12:    */ import org.springframework.core.convert.ConversionService;
/*  13:    */ import org.springframework.core.convert.TypeDescriptor;
/*  14:    */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*  15:    */ import org.springframework.core.convert.converter.GenericConverter;
/*  16:    */ import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;
/*  17:    */ import org.springframework.core.convert.support.GenericConversionService;
/*  18:    */ import org.springframework.format.AnnotationFormatterFactory;
/*  19:    */ import org.springframework.format.Formatter;
/*  20:    */ import org.springframework.format.FormatterRegistry;
/*  21:    */ import org.springframework.format.Parser;
/*  22:    */ import org.springframework.format.Printer;
/*  23:    */ import org.springframework.util.StringUtils;
/*  24:    */ import org.springframework.util.StringValueResolver;
/*  25:    */ 
/*  26:    */ public class FormattingConversionService
/*  27:    */   extends GenericConversionService
/*  28:    */   implements FormatterRegistry, EmbeddedValueResolverAware
/*  29:    */ {
/*  30:    */   private StringValueResolver embeddedValueResolver;
/*  31: 56 */   private final Map<AnnotationConverterKey, GenericConverter> cachedPrinters = new ConcurrentHashMap();
/*  32: 59 */   private final Map<AnnotationConverterKey, GenericConverter> cachedParsers = new ConcurrentHashMap();
/*  33:    */   
/*  34:    */   public void setEmbeddedValueResolver(StringValueResolver resolver)
/*  35:    */   {
/*  36: 63 */     this.embeddedValueResolver = resolver;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public void addFormatter(Formatter<?> formatter)
/*  40:    */   {
/*  41: 68 */     Class<?> fieldType = GenericTypeResolver.resolveTypeArgument(formatter.getClass(), Formatter.class);
/*  42: 69 */     if (fieldType == null) {
/*  43: 70 */       throw new IllegalArgumentException("Unable to extract parameterized field type argument from Formatter [" + 
/*  44: 71 */         formatter.getClass().getName() + "]; does the formatter parameterize the <T> generic type?");
/*  45:    */     }
/*  46: 73 */     addFormatterForFieldType(fieldType, formatter);
/*  47:    */   }
/*  48:    */   
/*  49:    */   public void addFormatterForFieldType(Class<?> fieldType, Formatter<?> formatter)
/*  50:    */   {
/*  51: 77 */     addConverter(new PrinterConverter(fieldType, formatter, this));
/*  52: 78 */     addConverter(new ParserConverter(fieldType, formatter, this));
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void addFormatterForFieldType(Class<?> fieldType, Printer<?> printer, Parser<?> parser)
/*  56:    */   {
/*  57: 82 */     addConverter(new PrinterConverter(fieldType, printer, this));
/*  58: 83 */     addConverter(new ParserConverter(fieldType, parser, this));
/*  59:    */   }
/*  60:    */   
/*  61:    */   public void addFormatterForFieldAnnotation(AnnotationFormatterFactory annotationFormatterFactory)
/*  62:    */   {
/*  63: 88 */     Class<? extends Annotation> annotationType = 
/*  64: 89 */       GenericTypeResolver.resolveTypeArgument(annotationFormatterFactory.getClass(), AnnotationFormatterFactory.class);
/*  65: 90 */     if (annotationType == null) {
/*  66: 91 */       throw new IllegalArgumentException("Unable to extract parameterized Annotation type argument from AnnotationFormatterFactory [" + 
/*  67: 92 */         annotationFormatterFactory.getClass().getName() + "]; does the factory parameterize the <A extends Annotation> generic type?");
/*  68:    */     }
/*  69: 94 */     if ((this.embeddedValueResolver != null) && ((annotationFormatterFactory instanceof EmbeddedValueResolverAware))) {
/*  70: 95 */       ((EmbeddedValueResolverAware)annotationFormatterFactory).setEmbeddedValueResolver(this.embeddedValueResolver);
/*  71:    */     }
/*  72: 97 */     Set<Class<?>> fieldTypes = annotationFormatterFactory.getFieldTypes();
/*  73: 98 */     for (Class<?> fieldType : fieldTypes)
/*  74:    */     {
/*  75: 99 */       addConverter(new AnnotationPrinterConverter(annotationType, annotationFormatterFactory, fieldType));
/*  76:100 */       addConverter(new AnnotationParserConverter(annotationType, annotationFormatterFactory, fieldType));
/*  77:    */     }
/*  78:    */   }
/*  79:    */   
/*  80:    */   private static class PrinterConverter
/*  81:    */     implements GenericConverter
/*  82:    */   {
/*  83:    */     private Class<?> fieldType;
/*  84:    */     private TypeDescriptor printerObjectType;
/*  85:    */     private Printer printer;
/*  86:    */     private ConversionService conversionService;
/*  87:    */     
/*  88:    */     public PrinterConverter(Class<?> fieldType, Printer<?> printer, ConversionService conversionService)
/*  89:    */     {
/*  90:117 */       this.fieldType = fieldType;
/*  91:118 */       this.printerObjectType = TypeDescriptor.valueOf(resolvePrinterObjectType(printer));
/*  92:119 */       this.printer = printer;
/*  93:120 */       this.conversionService = conversionService;
/*  94:    */     }
/*  95:    */     
/*  96:    */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/*  97:    */     {
/*  98:124 */       return Collections.singleton(new GenericConverter.ConvertiblePair(this.fieldType, String.class));
/*  99:    */     }
/* 100:    */     
/* 101:    */     public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 102:    */     {
/* 103:129 */       if (source == null) {
/* 104:130 */         return "";
/* 105:    */       }
/* 106:132 */       if (!sourceType.isAssignableTo(this.printerObjectType)) {
/* 107:133 */         source = this.conversionService.convert(source, sourceType, this.printerObjectType);
/* 108:    */       }
/* 109:135 */       return this.printer.print(source, LocaleContextHolder.getLocale());
/* 110:    */     }
/* 111:    */     
/* 112:    */     private Class<?> resolvePrinterObjectType(Printer<?> printer)
/* 113:    */     {
/* 114:139 */       return GenericTypeResolver.resolveTypeArgument(printer.getClass(), Printer.class);
/* 115:    */     }
/* 116:    */     
/* 117:    */     public String toString()
/* 118:    */     {
/* 119:143 */       return this.fieldType.getName() + " -> " + String.class.getName() + " : " + this.printer;
/* 120:    */     }
/* 121:    */   }
/* 122:    */   
/* 123:    */   private static class ParserConverter
/* 124:    */     implements GenericConverter
/* 125:    */   {
/* 126:    */     private Class<?> fieldType;
/* 127:    */     private Parser<?> parser;
/* 128:    */     private ConversionService conversionService;
/* 129:    */     
/* 130:    */     public ParserConverter(Class<?> fieldType, Parser<?> parser, ConversionService conversionService)
/* 131:    */     {
/* 132:157 */       this.fieldType = fieldType;
/* 133:158 */       this.parser = parser;
/* 134:159 */       this.conversionService = conversionService;
/* 135:    */     }
/* 136:    */     
/* 137:    */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/* 138:    */     {
/* 139:163 */       return Collections.singleton(new GenericConverter.ConvertiblePair(String.class, this.fieldType));
/* 140:    */     }
/* 141:    */     
/* 142:    */     public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 143:    */     {
/* 144:167 */       String text = (String)source;
/* 145:168 */       if (!StringUtils.hasText(text)) {
/* 146:169 */         return null;
/* 147:    */       }
/* 148:    */       try
/* 149:    */       {
/* 150:173 */         result = this.parser.parse(text, LocaleContextHolder.getLocale());
/* 151:    */       }
/* 152:    */       catch (ParseException ex)
/* 153:    */       {
/* 154:    */         Object result;
/* 155:176 */         throw new IllegalArgumentException("Unable to parse '" + text + "'", ex);
/* 156:    */       }
/* 157:    */       Object result;
/* 158:178 */       if (result == null) {
/* 159:179 */         throw new IllegalStateException("Parsers are not allowed to return null");
/* 160:    */       }
/* 161:181 */       TypeDescriptor resultType = TypeDescriptor.valueOf(result.getClass());
/* 162:182 */       if (!resultType.isAssignableTo(targetType)) {
/* 163:183 */         result = this.conversionService.convert(result, resultType, targetType);
/* 164:    */       }
/* 165:185 */       return result;
/* 166:    */     }
/* 167:    */     
/* 168:    */     public String toString()
/* 169:    */     {
/* 170:189 */       return String.class.getName() + " -> " + this.fieldType.getName() + ": " + this.parser;
/* 171:    */     }
/* 172:    */   }
/* 173:    */   
/* 174:    */   private class AnnotationPrinterConverter
/* 175:    */     implements ConditionalGenericConverter
/* 176:    */   {
/* 177:    */     private Class<? extends Annotation> annotationType;
/* 178:    */     private AnnotationFormatterFactory annotationFormatterFactory;
/* 179:    */     private Class<?> fieldType;
/* 180:    */     
/* 181:    */     public AnnotationPrinterConverter(AnnotationFormatterFactory annotationType, Class<?> annotationFormatterFactory)
/* 182:    */     {
/* 183:204 */       this.annotationType = annotationType;
/* 184:205 */       this.annotationFormatterFactory = annotationFormatterFactory;
/* 185:206 */       this.fieldType = fieldType;
/* 186:    */     }
/* 187:    */     
/* 188:    */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/* 189:    */     {
/* 190:210 */       return Collections.singleton(new GenericConverter.ConvertiblePair(this.fieldType, String.class));
/* 191:    */     }
/* 192:    */     
/* 193:    */     public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 194:    */     {
/* 195:214 */       return sourceType.getAnnotation(this.annotationType) != null;
/* 196:    */     }
/* 197:    */     
/* 198:    */     public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 199:    */     {
/* 200:218 */       FormattingConversionService.AnnotationConverterKey converterKey = new FormattingConversionService.AnnotationConverterKey(sourceType.getAnnotation(this.annotationType), sourceType.getObjectType());
/* 201:219 */       GenericConverter converter = (GenericConverter)FormattingConversionService.this.cachedPrinters.get(converterKey);
/* 202:220 */       if (converter == null)
/* 203:    */       {
/* 204:221 */         Printer<?> printer = this.annotationFormatterFactory.getPrinter(converterKey.getAnnotation(), converterKey.getFieldType());
/* 205:222 */         converter = new FormattingConversionService.PrinterConverter(this.fieldType, printer, FormattingConversionService.this);
/* 206:223 */         FormattingConversionService.this.cachedPrinters.put(converterKey, converter);
/* 207:    */       }
/* 208:225 */       return converter.convert(source, sourceType, targetType);
/* 209:    */     }
/* 210:    */     
/* 211:    */     public String toString()
/* 212:    */     {
/* 213:229 */       return "@" + this.annotationType.getName() + " " + this.fieldType.getName() + " -> " + String.class.getName() + ": " + this.annotationFormatterFactory;
/* 214:    */     }
/* 215:    */   }
/* 216:    */   
/* 217:    */   private class AnnotationParserConverter
/* 218:    */     implements ConditionalGenericConverter
/* 219:    */   {
/* 220:    */     private Class<? extends Annotation> annotationType;
/* 221:    */     private AnnotationFormatterFactory annotationFormatterFactory;
/* 222:    */     private Class<?> fieldType;
/* 223:    */     
/* 224:    */     public AnnotationParserConverter(AnnotationFormatterFactory<?> annotationType, Class<?> annotationFormatterFactory)
/* 225:    */     {
/* 226:244 */       this.annotationType = annotationType;
/* 227:245 */       this.annotationFormatterFactory = annotationFormatterFactory;
/* 228:246 */       this.fieldType = fieldType;
/* 229:    */     }
/* 230:    */     
/* 231:    */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes()
/* 232:    */     {
/* 233:250 */       return Collections.singleton(new GenericConverter.ConvertiblePair(String.class, this.fieldType));
/* 234:    */     }
/* 235:    */     
/* 236:    */     public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType)
/* 237:    */     {
/* 238:254 */       return targetType.getAnnotation(this.annotationType) != null;
/* 239:    */     }
/* 240:    */     
/* 241:    */     public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
/* 242:    */     {
/* 243:258 */       FormattingConversionService.AnnotationConverterKey converterKey = new FormattingConversionService.AnnotationConverterKey(targetType.getAnnotation(this.annotationType), targetType.getObjectType());
/* 244:259 */       GenericConverter converter = (GenericConverter)FormattingConversionService.this.cachedParsers.get(converterKey);
/* 245:260 */       if (converter == null)
/* 246:    */       {
/* 247:261 */         Parser<?> parser = this.annotationFormatterFactory.getParser(converterKey.getAnnotation(), converterKey.getFieldType());
/* 248:262 */         converter = new FormattingConversionService.ParserConverter(this.fieldType, parser, FormattingConversionService.this);
/* 249:263 */         FormattingConversionService.this.cachedParsers.put(converterKey, converter);
/* 250:    */       }
/* 251:265 */       return converter.convert(source, sourceType, targetType);
/* 252:    */     }
/* 253:    */     
/* 254:    */     public String toString()
/* 255:    */     {
/* 256:269 */       return String.class.getName() + " -> @" + this.annotationType.getName() + " " + this.fieldType.getName() + ": " + this.annotationFormatterFactory;
/* 257:    */     }
/* 258:    */   }
/* 259:    */   
/* 260:    */   private static class AnnotationConverterKey
/* 261:    */   {
/* 262:    */     private final Annotation annotation;
/* 263:    */     private final Class<?> fieldType;
/* 264:    */     
/* 265:    */     public AnnotationConverterKey(Annotation annotation, Class<?> fieldType)
/* 266:    */     {
/* 267:281 */       this.annotation = annotation;
/* 268:282 */       this.fieldType = fieldType;
/* 269:    */     }
/* 270:    */     
/* 271:    */     public Annotation getAnnotation()
/* 272:    */     {
/* 273:286 */       return this.annotation;
/* 274:    */     }
/* 275:    */     
/* 276:    */     public Class<?> getFieldType()
/* 277:    */     {
/* 278:290 */       return this.fieldType;
/* 279:    */     }
/* 280:    */     
/* 281:    */     public boolean equals(Object o)
/* 282:    */     {
/* 283:294 */       if (!(o instanceof AnnotationConverterKey)) {
/* 284:295 */         return false;
/* 285:    */       }
/* 286:297 */       AnnotationConverterKey key = (AnnotationConverterKey)o;
/* 287:298 */       return (this.annotation.equals(key.annotation)) && (this.fieldType.equals(key.fieldType));
/* 288:    */     }
/* 289:    */     
/* 290:    */     public int hashCode()
/* 291:    */     {
/* 292:302 */       return this.annotation.hashCode() + 29 * this.fieldType.hashCode();
/* 293:    */     }
/* 294:    */   }
/* 295:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.format.support.FormattingConversionService
 * JD-Core Version:    0.7.0.1
 */