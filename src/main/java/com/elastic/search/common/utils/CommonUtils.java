package com.elastic.search.common.utils;

import com.google.gson.JsonArray;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;

/**
 * @author niuzhiwei
 */
public class CommonUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

    public static String getIPAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null) {
            String[] ips = ip.split(",");
            ip = ips[0];
        }
        if (StringUtils.isEmpty(ip)) {
            ip = request.getHeader("x-real-ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static boolean isNotBlank(String... str) {
        for (int i = 0; i < str.length; i++) {
            if (StringUtils.isBlank(str[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotExistNullParam(Object... obj) {
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] == null) {
                return false;
            }
        }
        return true;
    }

    public static String unGzip(InputStream ins) throws Exception {
        GZIPInputStream gis = new GZIPInputStream(ins);
        byte[] b = new byte[512];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int read = 0;
        while ((read = gis.read(b)) != -1) {
            bos.write(b, 0, read);
        }
        byte[] outbyte = bos.toByteArray();
        bos.flush();
        bos.close();
        gis.close();
        ins.close();

        String result = new String(outbyte, "utf-8");
        return result;
    }

    public static String inputStream2String(InputStream in) throws IOException {
        byte[] b = new byte[512];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int read = 0;
        while ((read = in.read(b)) != -1) {
            bos.write(b, 0, read);
        }
        byte[] outbyte = bos.toByteArray();
        bos.flush();
        bos.close();
        if (in != null) {
            in.close();
        }

        String result = new String(outbyte, "utf-8");
        return result;
    }

    public static final byte[] inputsteam2byte(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int size = 512;
        byte[] buff = new byte[size];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, size)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        swapStream.close();
        return in2b;
    }

    public static String getMD5(String checkSum) {
        StringBuilder checkSumMD5 = new StringBuilder();
        if (checkSum != null && checkSum.length() > 0) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                byte[] bytes = md5.digest(checkSum.getBytes("UTF-8"));
                for (int i = 0; i < bytes.length; i++) {
                    String hex = Integer.toHexString(bytes[i] & 0xFF);
                    if (hex.length() == 1) {
                        checkSumMD5.append("0");
                    }
                    checkSumMD5.append(hex);
                }
            } catch (Exception e) {
                throw new RuntimeException("Signature Generate Error!");
            }
            return checkSumMD5.toString();
        }
        return null;
    }

    public final static DecimalFormat priceFormat = new DecimalFormat("#.##");

    public static String urlEncoder(String sourse) {
        try {
            return URLEncoder.encode(sourse, "utf-8");
        } catch (Exception e) {
            return sourse;
        }

    }

    public static byte[] stream2Bytes(InputStream inStream) {
        try {

            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            // buff用于存放循环读取的临时数据
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            byte[] in_b = swapStream.toByteArray();
            return in_b;
        } catch (Exception e) {
            LOGGER.error("stream2Bytes", e);
            return null;
        }
    }

    public static String cleanInvalidXmlChars(String text) {
        if (null == text) {
            return "";
        }

        String re = "[^\\x0D\\x20-\\xD7FF\\xE000-\\xFFFD\\x10000-x10FFFF\\u4e00-\\u9fa5\\u3000-\\u301e\\ufe10-\\ufe19"
                + "\\ufe30-\\ufe44\\ufe50-\\ufe6b\\uff01-\\uffee]";
        return text.replaceAll(re, "");
    }

    private static String[] USER_AGENT = {"240x320", "acer", "acoon", "acs-", "abacho", "ahong", "airness", "alcatel",
            "amoi", "android", "anywhereyougo.com", "asus", "audio", "au-mic", "avantogo", "becker", "benq", "bilbo",
            "bird", "blackberry", "blazer", "bleu", "cdm-", "compal", "coolpad", "danger", "dbtel", "dopod", "elaine",
            "eric", "etouch", "fly ", "fly_", "fly-", "go.web", "goodaccess", "gradiente", "grundig", "haier", "hedy",
            "hitachi", "htc", "huawei", "hutchison", "inno", "ipad", "ipaq", "ipod", "jbrowser", "kddi", "kgt", "kwc",
            "lenovo", "lg ", "lg2", "lg3", "lg4", "lg5", "lg7", "lg8", "lg9", "lg-", "lge-", "lge9", "longcos", "maemo",
            "mercator", "meridian", "micromax", "midp", "mini", "mitsu", "mmm", "mmp", "mobi", "mot-", "moto", "nec-",
            "netfront", "newgen", "nexian", "nf-browser", "nintendo", "nitro", "nokia", "nook", "novarra", "obigo",
            "palm", "panasonic", "pantech", "philips", "phone", "pg-", "playstation", "pocket", "pt-", "qc-", "qtek",
            "rover", "sagem", "sama", "samu", "sanyo", "samsung", "sch-", "scooter", "sec-", "sendo", "sgh-", "sharp",
            "siemens", "sie-", "softbank", "sony", "spice", "sprint", "spv", "symbian", "talkabout", "tcl-", "teleca",
            "telit", "tianyu", "tim-", "toshiba", "tsm", "up.browser", "utec", "utstar", "verykool", "virgin", "vk-",
            "voda", "voxtel", "vx", "wap", "wellco", "wig browser", "wii", "windows ce", "wireless", "xda", "xde",
            "zte"};

    public static boolean isMobileBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            for (String ua : USER_AGENT) {
                if (userAgent.indexOf(ua) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isIPad(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if ((userAgent != null) && (userAgent.toLowerCase().indexOf("ipad") > 0)) {
            return true;
        }
        return false;
    }

    public static boolean isIPhone(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if ((userAgent != null) && (userAgent.toLowerCase().indexOf("iphone") > 0)) {
            return true;
        }
        return false;
    }

    public static boolean isAndroid(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if ((userAgent != null) && (userAgent.toLowerCase().indexOf("android") > 0)) {
            return true;
        }
        return false;
    }

    /**
     * 根据UA获取设备平台
     * <p>
     * iphone,ipad,android,mobileBrowser
     * </p>
     */
    public static String getPlatform(HttpServletRequest request) {
        if (isIPhone(request)) {
            return "iphone";
        } else if (isIPad(request)) {
            return "ipad";
        } else if (isAndroid(request)) {
            return "android";
        } else if (isMobileBrowser(request)) {
            return "mobileBrowser";
        } else {
            return "unknown";
        }
    }

    public static boolean isSupportJsonType(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        if (accept != null) {
            if (accept.indexOf("application/json") != -1) {
                return true;
            } else if (accept.indexOf("text/html") != -1 || accept.indexOf("application/xhtml+xml") != -1) {
                return false;
            } else {
                LOGGER.warn("requestURI:{}|accept:{}", accept, request.getRequestURI());
                return false;
            }
        } else {
            return true;
        }
    }

    public static String toString(Object[] args) {
        if (args == null || args.length == 0) {
            return "[]";
        } else {
            JsonArray out = new JsonArray();
            for (int i = 0; i < args.length; i++) {
                Object value = args[i];
                if (value == null) {
                    out.add("null");
                } else {
                    if (value instanceof String) {
                        String text = (String) value;
                        if (text.length() > 100) {
                            out.add(text.substring(0, 97) + "...");
                        } else {
                            out.add(text);
                        }
                    } else if (value instanceof Number) {
                        out.add((Number) value);
                    } else if (value instanceof Date) {
                        String str = DateUtils.formatDateTime((Date) value);
                        out.add(str);
                    } else if (value instanceof Boolean) {
                        out.add((Boolean) value);
                    } else if (value instanceof InputStream) {
                        out.add("<InputStream>");
                    } else if (value instanceof NClob) {
                        out.add("<NClob>");
                    } else if (value instanceof Clob) {
                        out.add("<Clob>");
                    } else if (value instanceof Blob) {
                        out.add("<Blob>");
                    } else {
                        out.add('<' + value.getClass().getName() + '>');
                    }
                }
            }
            return out.toString();
        }
    }
}
