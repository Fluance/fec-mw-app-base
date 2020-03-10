/**
 * 
 */
package net.fluance.app.web.util;

import java.util.HashMap;
import java.util.Map;

public class MimeUtils {

	private final Map<String, String> MIMETypes = new HashMap<>();
	private final Map<String, String> imageMIMETypesExtensions = new HashMap<>();
	private final Map<String, String> imageMIMETypes = new HashMap<>();
	private final Map<String, String> documentMIMETypes = new HashMap<>();
	private final Map<String, String> documentMIMETypesExtensions = new HashMap<>();
	
    /**
     * 
     */
    public MimeUtils() {
    	initMIMETypes();
    	initImageMIMETypes();
    	initImageMIMETypesExtensions();
    	initDocumentMIMETypes();
    	initDocumentMIMETypesExtensions();
    }

    
    
    private void initMIMETypes() {
    	//Mime Content Types used in OpenOffice.org2.0 / StarOffice 8 and later
    	MIMETypes.put("odt", "application/vnd.oasis.opendocument.text");
    	MIMETypes.put("application/vnd.oasis.opendocument.text-template", "ott");
    	MIMETypes.put("application/vnd.oasis.opendocument.text-web", "oth");
    	MIMETypes.put("application/vnd.oasis.opendocument.text-master", "odm");
    	MIMETypes.put("application/vnd.oasis.opendocument.graphics", "odg");
    	MIMETypes.put("application/vnd.oasis.opendocument.graphics-template","otg");
    	MIMETypes.put("application/vnd.oasis.opendocument.presentation", "odp");
    	MIMETypes.put("application/vnd.oasis.opendocument.presentation-template", "otp");
    	MIMETypes.put("application/vnd.oasis.opendocument.spreadsheet", "ods");
    	MIMETypes.put("application/vnd.oasis.opendocument.spreadsheet-template", "ots");
    	MIMETypes.put("application/vnd.oasis.opendocument.chart", "odc");
    	MIMETypes.put("application/vnd.oasis.opendocument.formula", "odf");
    	MIMETypes.put("application/vnd.oasis.opendocument.database", "odb");
    	MIMETypes.put("application/vnd.oasis.opendocument.image", "odi");
    	MIMETypes.put("application/vnd.openofficeorg.extension", "oxt");
    	
    	//Mime Content Types used in OpenOffice.org1.0 / StarOffice6.0 and later
    	MIMETypes.put("application/vnd.sun.xml.writer", "sxw");
    	MIMETypes.put("application/vnd.sun.xml.writer.template", "stw");
    	MIMETypes.put("application/vnd.sun.xml.calc", "sxc");
    	MIMETypes.put("application/vnd.sun.xml.calc.template", "stc");
    	MIMETypes.put("application/vnd.sun.xml.draw", "sxd");
    	MIMETypes.put("application/vnd.sun.xml.draw.template", "std");
    	MIMETypes.put("application/vnd.sun.xml.impress", "sxi");
    	MIMETypes.put("application/vnd.sun.xml.impress.template", "sti");
    	MIMETypes.put("application/vnd.sun.xml.writer.global", "sxg");
    	MIMETypes.put("application/vnd.sun.xml.math", "sxm");
    	//Mime Content Types used in StarOffice 5.x
    	MIMETypes.put("application/vnd.stardivision.writer", "sdw");
    	MIMETypes.put("application/vnd.stardivision.writer-global", "sgl");
    	MIMETypes.put("application/vnd.stardivision.calc", "sdc");
    	MIMETypes.put("application/vnd.stardivision.draw", "sda");
    	MIMETypes.put("application/vnd.stardivision.impress", "sdd");
    	MIMETypes.put("application/vnd.stardivision.impress-packed", "sdp");
    	MIMETypes.put("application/vnd.stardivision.chart", "sds");
    	MIMETypes.put("application/vnd.stardivision.math", "smf");
    	MIMETypes.put("application/vnd.stardivision.mail", "sdm");
    	//Mime Content Types used in StarOffice 4.x
    	MIMETypes.put("application/x-starwriter", "sdw");
    	MIMETypes.put("application/x-starcalc", "sdc");
    	MIMETypes.put("application/x-stardraw", "sda");
    	MIMETypes.put("application/x-starimpress", "sdd");
    	MIMETypes.put("application/x-starmath", "smf");
    	MIMETypes.put("application/x-starchart", "sds");
    	//Content Types for Open XML Documents
    	MIMETypes.put("application/vnd.ms-word.document.macroEnabled.12", "docm");
    	MIMETypes.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
    	MIMETypes.put("application/vnd.ms-word.template.macroEnabled.12", "dotm");
    	MIMETypes.put("application/vnd.openxmlformats-officedocument.wordprocessingml.template", "dotx");
    	MIMETypes.put("application/vnd.ms-powerpoint.slideshow.macroEnabled.12", "ppsm");
    	MIMETypes.put("application/vnd.openxmlformats-officedocument.presentationml.slideshow", "ppsx");
    	MIMETypes.put("application/vnd.ms-powerpoint.presentation.macroEnabled.12", "pptm");
    	MIMETypes.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx");
    	MIMETypes.put("application/vnd.ms-excel.sheet.binary.macroEnabled.12", "xlsb");
    	MIMETypes.put("application/vnd.ms-excel.sheet.macroEnabled.12", "xlsm");
    	MIMETypes.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx");
    	MIMETypes.put("application/vnd.ms-xpsdocument", "xps");
    	
    	
    	MIMETypes.put("application/vnd.ms-powerpoint", "ppt");
    	MIMETypes.put("application/x-mspowerpoint","ppt");
    	MIMETypes.put("application/mspowerpoint","ppt");
    	MIMETypes.put("application/powerpoint","ppt");
    	MIMETypes.put("application/msword","doc");
    	MIMETypes.put("application/msword","dot");
    	MIMETypes.put("text/richtext","rt");
    	MIMETypes.put("application/x-rtf","rtf");
    	MIMETypes.put("application/rtf","rtf");
    	MIMETypes.put("application/xml","xml");
    	MIMETypes.put("text/html","html");
    	MIMETypes.put("text/html","xhtml");
    	MIMETypes.put("text/html","htm");
    	MIMETypes.put("text/xml","xml");
    	MIMETypes.put("text/plain","txt");
    	MIMETypes.put("application/pdf","pdf");
    	MIMETypes.put("application/x-tex","tex");
    	MIMETypes.put("application/excel","xl");
    	MIMETypes.put("application/excel","xls");
    	MIMETypes.put("application/x-excel","xla");
    	MIMETypes.put("application/x-excel","xls");
    	MIMETypes.put("application/vnd.ms-excel","xls");
    	MIMETypes.put("text/vnd.rn-realtext","rt");
    	
    	
    	MIMETypes.put("image/x-tiff","tif");
    	MIMETypes.put("image/x-tiff","tiff");
    	MIMETypes.put("image/xpm","xpm");
    	MIMETypes.put("image/x-xpixmap","xpm");
    	MIMETypes.put("image/png","png");
    	MIMETypes.put("image/gif","gif");
    	MIMETypes.put("image/x-xwd","xwd");
    	MIMETypes.put("image/x-xwindowdump","xwd");
    	MIMETypes.put("image/x-windows-bmp","bmp");
    	MIMETypes.put("image/bmp","bmp");
    	MIMETypes.put("image/vnd.dwg","dwg");
    	MIMETypes.put("image/x-dwg","dwg");

    	MIMETypes.put("image/fif","fif");
    	MIMETypes.put("image/vnd.fpx","fpx");
    	MIMETypes.put("image/vnd.net-fpx","fpx");
    	MIMETypes.put("image/x-icon","ico");
    	MIMETypes.put("image/jpeg","jpeg");
    }

    private void initDocumentMIMETypes() {
    	documentMIMETypes.put("odt", "application/vnd.oasis.opendocument.text");
    	documentMIMETypes.put("application/vnd.oasis.opendocument.text-template", "ott");
    	documentMIMETypes.put("application/vnd.oasis.opendocument.text-web", "oth");
    	documentMIMETypes.put("application/vnd.oasis.opendocument.text-master", "odm");
    	documentMIMETypes.put("application/vnd.oasis.opendocument.graphics", "odg");
    	documentMIMETypes.put("application/vnd.oasis.opendocument.graphics-template","otg");
    	documentMIMETypes.put("application/vnd.oasis.opendocument.presentation", "odp");
    	documentMIMETypes.put("application/vnd.oasis.opendocument.presentation-template", "otp");
    	documentMIMETypes.put("application/vnd.oasis.opendocument.spreadsheet", "ods");
    	documentMIMETypes.put("application/vnd.oasis.opendocument.spreadsheet-template", "ots");
    	documentMIMETypes.put("application/vnd.oasis.opendocument.chart", "odc");
    	documentMIMETypes.put("application/vnd.oasis.opendocument.formula", "odf");
    	documentMIMETypes.put("application/vnd.oasis.opendocument.database", "odb");
    	documentMIMETypes.put("application/vnd.oasis.opendocument.image", "odi");
    	documentMIMETypes.put("application/vnd.openofficeorg.extension", "oxt");
    	//Mime Content Types used in OpenOffice.org1.0 / StarOffice6.0 and later
    	documentMIMETypes.put("application/vnd.sun.xml.writer", "sxw");
    	documentMIMETypes.put("application/vnd.sun.xml.writer.template", "stw");
    	documentMIMETypes.put("application/vnd.sun.xml.calc", "sxc");
    	documentMIMETypes.put("application/vnd.sun.xml.calc.template", "stc");
    	documentMIMETypes.put("application/vnd.sun.xml.draw", "sxd");
    	documentMIMETypes.put("application/vnd.sun.xml.draw.template", "std");
    	documentMIMETypes.put("application/vnd.sun.xml.impress", "sxi");
    	documentMIMETypes.put("application/vnd.sun.xml.impress.template", "sti");
    	documentMIMETypes.put("application/vnd.sun.xml.writer.global", "sxg");
    	documentMIMETypes.put("application/vnd.sun.xml.math", "sxm");
    	//Mime Content Types used in StarOffice 5.x
    	documentMIMETypes.put("application/vnd.stardivision.writer", "sdw");
    	documentMIMETypes.put("application/vnd.stardivision.writer-global", "sgl");
    	documentMIMETypes.put("application/vnd.stardivision.calc", "sdc");
    	documentMIMETypes.put("application/vnd.stardivision.draw", "sda");
    	documentMIMETypes.put("application/vnd.stardivision.impress", "sdd");
    	documentMIMETypes.put("application/vnd.stardivision.impress-packed", "sdp");
    	documentMIMETypes.put("application/vnd.stardivision.chart", "sds");
    	documentMIMETypes.put("application/vnd.stardivision.math", "smf");
    	documentMIMETypes.put("application/vnd.stardivision.mail", "sdm");
    	//Mime Content Types used in StarOffice 4.x
    	documentMIMETypes.put("application/x-starwriter", "sdw");
    	documentMIMETypes.put("application/x-starcalc", "sdc");
    	documentMIMETypes.put("application/x-stardraw", "sda");
    	documentMIMETypes.put("application/x-starimpress", "sdd");
    	documentMIMETypes.put("application/x-starmath", "smf");
    	documentMIMETypes.put("application/x-starchart", "sds");
    	//Content Types for Open XML Documents
    	documentMIMETypes.put("application/vnd.ms-word.document.macroEnabled.12", "docm");
    	documentMIMETypes.put("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx");
    	documentMIMETypes.put("application/vnd.ms-word.template.macroEnabled.12", "dotm");
    	documentMIMETypes.put("application/vnd.openxmlformats-officedocument.wordprocessingml.template", "dotx");
    	documentMIMETypes.put("application/vnd.ms-powerpoint.slideshow.macroEnabled.12", "ppsm");
    	documentMIMETypes.put("application/vnd.openxmlformats-officedocument.presentationml.slideshow", "ppsx");
    	documentMIMETypes.put("application/vnd.ms-powerpoint.presentation.macroEnabled.12", "pptm");
    	documentMIMETypes.put("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx");
    	documentMIMETypes.put("application/vnd.ms-excel.sheet.binary.macroEnabled.12", "xlsb");
    	documentMIMETypes.put("application/vnd.ms-excel.sheet.macroEnabled.12", "xlsm");
    	documentMIMETypes.put("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx");
    	documentMIMETypes.put("application/vnd.ms-xpsdocument", "xps");
    	documentMIMETypes.put("application/vnd.ms-powerpoint", "ppt");
    	documentMIMETypes.put("application/x-mspowerpoint","ppt");
    	documentMIMETypes.put("application/mspowerpoint","ppt");
    	documentMIMETypes.put("application/powerpoint","ppt");
    	documentMIMETypes.put("application/msword","doc");
    	documentMIMETypes.put("application/msword","dot");
    	//Autres
    	documentMIMETypes.put("text/richtext","rt");
    	documentMIMETypes.put("application/x-rtf","rtf");
    	documentMIMETypes.put("application/rtf","rtf");
    	documentMIMETypes.put("application/xml","xml");
    	documentMIMETypes.put("text/html","html");
    	documentMIMETypes.put("text/html","xhtml");
    	documentMIMETypes.put("text/html","htm");
    	documentMIMETypes.put("text/xml","xml");
    	documentMIMETypes.put("text/plain","txt");
    	documentMIMETypes.put("application/pdf","pdf");
    	documentMIMETypes.put("application/x-tex","tex");
    	documentMIMETypes.put("application/excel","xl");
    	documentMIMETypes.put("application/excel","xls");
    	documentMIMETypes.put("application/x-excel","xla");
    	documentMIMETypes.put("application/x-excel","xls");
    	documentMIMETypes.put("application/vnd.ms-excel","xls");
    	documentMIMETypes.put("text/vnd.rn-realtext","rt");
    	
    }

    private void initDocumentMIMETypesExtensions() {
    	documentMIMETypesExtensions.put("xls", "application/vnd.ms-excel");
    	documentMIMETypesExtensions.put("std", "application/vnd.sun.xml.draw.template");
    	documentMIMETypesExtensions.put("sdw", "application/vnd.stardivision.writer");
    	documentMIMETypesExtensions.put("tex", "application/x-tex");
    	documentMIMETypesExtensions.put("sxc", "application/vnd.sun.xml.calc");
    	documentMIMETypesExtensions.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    	documentMIMETypesExtensions.put("xml", "text/xml");
    	documentMIMETypesExtensions.put("sxi", "application/vnd.sun.xml.impress");
    	documentMIMETypesExtensions.put("ppt", "application/mspowerpoint");
    	documentMIMETypesExtensions.put("ppt", "application/vnd.ms-powerpoint");
    	documentMIMETypesExtensions.put("sxg", "application/vnd.sun.xml.writer.global");
    	documentMIMETypesExtensions.put("odb", "application/vnd.oasis.opendocument.database");
    	documentMIMETypesExtensions.put("odf", "application/vnd.oasis.opendocument.formula");
    	documentMIMETypesExtensions.put("sdd", "application/x-starimpress");
    	documentMIMETypesExtensions.put("sti", "application/vnd.sun.xml.impress.template");
    	documentMIMETypesExtensions.put("ppsm", "application/vnd.ms-powerpoint.slideshow.macroEnabled.12");
    	documentMIMETypesExtensions.put("htm", "text/html");
    	documentMIMETypesExtensions.put("rtf", "application/x-rtf");
    	documentMIMETypesExtensions.put("sds", "application/vnd.stardivision.chart");
    	documentMIMETypesExtensions.put("sdm", "application/vnd.stardivision.mail");
    	documentMIMETypesExtensions.put("stw", "application/vnd.sun.xml.writer.template");
    	documentMIMETypesExtensions.put("sds", "application/x-starchart");
    	documentMIMETypesExtensions.put("sxd", "application/vnd.sun.xml.draw");
    	documentMIMETypesExtensions.put("xps", "application/vnd.ms-xpsdocument");
    	documentMIMETypesExtensions.put("application/vnd.oasis.opendocument.text", "odt");
    	documentMIMETypesExtensions.put("xml", "application/xml");
    	documentMIMETypesExtensions.put("sdp", "application/vnd.stardivision.impress-packed");
    	documentMIMETypesExtensions.put("ppt", "application/x-mspowerpoint");
    	documentMIMETypesExtensions.put("stc", "application/vnd.sun.xml.calc.template");
    	documentMIMETypesExtensions.put("odg", "application/vnd.oasis.opendocument.graphics");
    	documentMIMETypesExtensions.put("otg", "application/vnd.oasis.opendocument.graphics-template");
    	documentMIMETypesExtensions.put("xlsb", "application/vnd.ms-excel.sheet.binary.macroEnabled.12");
    	documentMIMETypesExtensions.put("odi", "application/vnd.oasis.opendocument.image");
    	documentMIMETypesExtensions.put("sdw", "application/x-starwriter");
    	documentMIMETypesExtensions.put("odc", "application/vnd.oasis.opendocument.chart");
    	documentMIMETypesExtensions.put("rtf", "application/rtf");
    	documentMIMETypesExtensions.put("ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow");
    	documentMIMETypesExtensions.put("sxw", "application/vnd.sun.xml.writer");
    	documentMIMETypesExtensions.put("ppt", "application/powerpoint");
    	documentMIMETypesExtensions.put("sgl", "application/vnd.stardivision.writer-global");
    	documentMIMETypesExtensions.put("sdc", "application/vnd.stardivision.calc");
    	documentMIMETypesExtensions.put("xls", "application/excel");
    	documentMIMETypesExtensions.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    	documentMIMETypesExtensions.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
    	documentMIMETypesExtensions.put("odp", "application/vnd.oasis.opendocument.presentation");
    	documentMIMETypesExtensions.put("sda", "application/x-stardraw");
    	documentMIMETypesExtensions.put("rt", "text/vnd.rn-realtext");
    	documentMIMETypesExtensions.put("otp", "application/vnd.oasis.opendocument.presentation-template");
    	documentMIMETypesExtensions.put("dotx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template");
    	documentMIMETypesExtensions.put("oxt", "application/vnd.openofficeorg.extension");
    	documentMIMETypesExtensions.put("ots", "application/vnd.oasis.opendocument.spreadsheet-template");
    	documentMIMETypesExtensions.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
    	documentMIMETypesExtensions.put("oth", "application/vnd.oasis.opendocument.text-web");
    	documentMIMETypesExtensions.put("dotm", "application/vnd.ms-word.template.macroEnabled.12");
    	documentMIMETypesExtensions.put("dot", "application/msword");
    	documentMIMETypesExtensions.put("ott", "application/vnd.oasis.opendocument.text-template");
    	documentMIMETypesExtensions.put("xls", "application/x-excel");
    	documentMIMETypesExtensions.put("rt", "text/richtext");
    	documentMIMETypesExtensions.put("sdd", "application/vnd.stardivision.impress");
    	documentMIMETypesExtensions.put("smf", "application/x-starmath");
    	documentMIMETypesExtensions.put("odm", "application/vnd.oasis.opendocument.text-master");
    	documentMIMETypesExtensions.put("sdc", "application/x-starcalc");
    	documentMIMETypesExtensions.put("pptm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12");
    	documentMIMETypesExtensions.put("sda", "application/vnd.stardivision.draw");
    	documentMIMETypesExtensions.put("txt", "text/plain");
    	documentMIMETypesExtensions.put("docm", "application/vnd.ms-word.document.macroEnabled.12");
    	documentMIMETypesExtensions.put("pdf", "application/pdf");
    	documentMIMETypesExtensions.put("sxm", "application/vnd.sun.xml.math");
    	documentMIMETypesExtensions.put("xlsm", "application/vnd.ms-excel.sheet.macroEnabled.12");
    	documentMIMETypesExtensions.put("smf", "application/vnd.stardivision.math");
    	
    }

    private void initImageMIMETypesExtensions() {
    	imageMIMETypesExtensions.put("tif", "image/x-tiff");
    	imageMIMETypesExtensions.put("tiff", "image/x-tiff");
    	imageMIMETypesExtensions.put("xpm", "image/xpm");
    	imageMIMETypesExtensions.put("xpm", "image/x-xpixmap");
    	imageMIMETypesExtensions.put("png", "image/png");
    	imageMIMETypesExtensions.put("gif", "image/gif");
    	imageMIMETypesExtensions.put("xwd", "image/x-xwd");
    	imageMIMETypesExtensions.put("xwd", "image/x-xwindowdump");
    	imageMIMETypesExtensions.put("bmp", "image/x-windows-bmp");
    	imageMIMETypesExtensions.put("bmp", "image/bmp");
    	imageMIMETypesExtensions.put("dwg", "image/vnd.dwg");
    	imageMIMETypesExtensions.put("dwg", "image/x-dwg");
    	
    	imageMIMETypesExtensions.put("fif", "image/fif");
    	imageMIMETypesExtensions.put("fpx", "image/vnd.fpx");
    	imageMIMETypesExtensions.put("fpx", "image/vnd.net-fpx");
    	imageMIMETypesExtensions.put("ico", "image/x-icon");
    	imageMIMETypesExtensions.put("jpeg", "image/jpeg");
    	imageMIMETypesExtensions.put("jpg", "image/jpeg");
    }

    private void initImageMIMETypes() {
    	imageMIMETypes.put("image/x-tiff","tif");
    	imageMIMETypes.put("image/x-tiff","tiff");
    	imageMIMETypes.put("image/xpm","xpm");
    	imageMIMETypes.put("image/x-xpixmap","xpm");
    	imageMIMETypes.put("image/png","png");
    	imageMIMETypes.put("image/gif","gif");
    	imageMIMETypes.put("image/x-xwd","xwd");
    	imageMIMETypes.put("image/x-xwindowdump","xwd");
    	imageMIMETypes.put("image/x-windows-bmp","bmp");
    	imageMIMETypes.put("image/bmp","bmp");
    	imageMIMETypes.put("image/vnd.dwg","dwg");
    	imageMIMETypes.put("image/x-dwg","dwg");

    	imageMIMETypes.put("image/fif","fif");
    	imageMIMETypes.put("image/vnd.fpx","fpx");
    	imageMIMETypes.put("image/vnd.net-fpx","fpx");
    	imageMIMETypes.put("image/x-icon","ico");
    	imageMIMETypes.put("image/jpeg","jpeg");
    }

    public Map<String, String> getMIMETypes() {
    	return MIMETypes;
    }

    public Map<String, String> getImageMIMETypesExtensions() {
    	return imageMIMETypesExtensions;
    }

    public Map<String, String> getMIMETypesExtensions() {
    	return imageMIMETypesExtensions;
    }

    public Map<String, String> getImageMIMETypes() {
    	return imageMIMETypes;
    }

    public Map<String, String> getDocumentMIMETypes() {
    	return documentMIMETypes;
    }

    public Map<String, String> getDocumentMIMETypesExtensions() {
    	return documentMIMETypesExtensions;
    }
    
}
