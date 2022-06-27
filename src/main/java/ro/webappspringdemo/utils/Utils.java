package ro.webappspringdemo.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import ro.webappspringdemo.exceptions.CustomException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URLEncoder;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Utils {

    public static final String FILE_MAX_SIZE = "10MB";

    public static String toJSON(Object input) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(input);
        } catch (Exception ex) {
            throw new CustomException(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public static void verifyNullObjectNotFound(Object object, String message) {
        if (object == null) {
            throw new CustomException(message, HttpStatus.NOT_FOUND);
        }
    }

    public static void verifyNullObjectBadRequest(Object object, String message) {
        if (object == null) {
            throw new CustomException(message, HttpStatus.BAD_REQUEST);
        }
    }

    public static void verifyNullStringBadRequest(String object, String message) {
        if (object == null || object.isEmpty()) {
            throw new CustomException(message, HttpStatus.BAD_REQUEST);
        }
    }

    public static void verifyNotNullObjectBadRequest(Object object, String message) {
        if (object != null) {
            throw new CustomException(message, HttpStatus.BAD_REQUEST);
        }
    }

    public static String formatCamelCaseString(String string) {
        String splitString = "";
        String[] splitVector = string.split("(?<=[a-z])(?=[A-Z])");
        for (String word : splitVector) {
            splitString = splitString + word.toUpperCase() + " ";
        }
        return splitString;
    }

    public static String getUserNameFromPrincipal(Principal principal) {
        String username = principal.getName();
        return username.substring(0, username.indexOf("@"));
    }

    /**
     * Format the string to camelcase
     * Each value must be a string that contains words separated by space or other caracters from: [!"#$%&'()*+,./:;<=>?@\^_`{|}~-]
     *
     * @param value - The value from header
     * @return - the new value formatted
     */
    public static String formatStringtoCamelcase(String value) {
        value = value.replaceAll("[!\"#$%&'()*+,./:;<=>?@\\^_`{|}~-]", " ");
        String[] arr = value.split(" ");
        String newValue = "";
        for (String ss : arr) {
            if (!newValue.isEmpty()) {
                newValue = newValue + ss.substring(0, 1).toUpperCase() + ss.substring(1).toLowerCase();
            } else {
                newValue = ss.toLowerCase();
            }
        }
        return newValue;
    }

    public static String formateDateWithTimestamp(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format.format(date);
    }

    public static String formateDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }


    public static void validateDateFromDateTo(Long dateFrom, Long dateTo) {
        if (dateFrom > dateTo) {
            throw new CustomException("Data de inceput trebuie sa fie mai mica decat data de final!", HttpStatus.BAD_REQUEST);
        }
    }

    public static String dateToYYYYMMDD(Long date) {
        if (date == null) return null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(new Date(date));
    }

    public static Object extractFromExcelWithColumnOrderExtractionMode(MultipartFile excelFile, Class classToExtarct) {
        if (excelFile == null) {
            throw new CustomException(Messages.FILE_NOT_NULL, HttpStatus.BAD_REQUEST);
        }
        Excel excel;
        try {
            excel = new Excel(excelFile);
            return excel.getDataFromXlsx(classToExtarct, Excel.COLUMN_ORDER);
        } catch (IOException e) {
            throw new CustomException("Eroare la extragerea fisierului din MultipartFile", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //   }else throw new CustomException("Tipul de fisier nu este acceptat",HttpStatus.BAD_REQUEST);
    }

    public static Object extractFromExcelWithColumnOrderExtractionModeCheckType(MultipartFile excelFile, Class classToExtarct) {
        if (excelFile == null) {
            throw new CustomException(Messages.FILE_NOT_NULL, HttpStatus.BAD_REQUEST);
        }

        if (excelFile.getContentType().equals("application/vnd.ms-excel")) {
            ExcelXls excel;
            try {
                excel = new ExcelXls(excelFile.getInputStream());
                return excel.getDataFromXls(classToExtarct, ExcelXls.COLUMN_ORDER);
            } catch (org.apache.poi.poifs.filesystem.OfficeXmlFileException e) {
                throw new CustomException(Messages.WRONG_FORMAT + " Eroarea: " + e.getMessage(), HttpStatus.BAD_REQUEST);
            } catch (IOException e) {
                throw new CustomException("Eroare la extragerea fisierului din MultipartFile", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else if (excelFile.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            Excel excel;
            try {
                excel = new Excel(excelFile.getInputStream());
                return excel.getDataFromXlsx(classToExtarct, Excel.COLUMN_ORDER);
            } catch (org.apache.poi.poifs.filesystem.OfficeXmlFileException e) {
                throw new CustomException(Messages.WRONG_FORMAT + " Eroarea: " + e.getMessage(), HttpStatus.BAD_REQUEST);
            } catch (IOException e) {
                throw new CustomException("Eroare la extragerea fisierului din MultipartFile", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else throw new CustomException("Tipul de fisier nu este acceptat", HttpStatus.BAD_REQUEST);
    }


    private static String getResponseFromHttpsRequest(HttpsURLConnection httpsCon) {
        StringBuilder content = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpsCon.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine).append(" ");
            }
            in.close();
        } catch (Exception e) {
            return Messages.HTTPS_CALL_ERROR_MESSAGE;
        }
        return content.toString();
    }

    private static Integer getResponseStatusFromHttpsRequest(HttpsURLConnection httpsCon) {
        try {
            return httpsCon.getResponseCode();
        } catch (IOException e) {
            throw new CustomException(Messages.HTTPS_CALL_ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static void setParamsForHttpsRequest(HttpsURLConnection httpsCon, Map requestParams) throws IOException {
        DataOutputStream wr = new DataOutputStream(httpsCon.getOutputStream());
        wr.writeBytes(getParamsString(requestParams));
        wr.close();
    }

    private static void setHeadersForHttpsRequest(HttpsURLConnection httpsCon, String contentType, String basicAuthEncoding) {
        httpsCon.setRequestProperty("Content-Type", contentType);
        if (basicAuthEncoding != null) {
            httpsCon.setRequestProperty("Authorization", "Basic " + basicAuthEncoding);
        }

        httpsCon.setRequestProperty("Content-Language", "en-US");
        httpsCon.setUseCaches(false);
        httpsCon.setDoInput(true);
        httpsCon.setDoOutput(true);

    }

    public static String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }


    public static String getEmailFromUsername(String username) {
        return username + "@gmail.com";
    }

    @Data
    static
    public class HttpsResponse {
        Integer status;
        String content;
        String error;
    }

}