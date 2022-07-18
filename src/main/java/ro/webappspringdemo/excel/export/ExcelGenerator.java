package ro.webappspringdemo.excel.export;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.view.document.AbstractXlsxStreamingView;
import ro.webappspringdemo.excel.export.annotations.ExcelCustomColumnName;
import ro.webappspringdemo.excel.export.annotations.ExcelFormatOptions;
import ro.webappspringdemo.excel.export.annotations.ExcelIgnoreParam;
import ro.webappspringdemo.exceptions.CustomException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExcelGenerator extends AbstractXlsxStreamingView {

    public static final String EXPORT_LIST_NAME = "objects";
    public static final String EXPORT_CUSTOM_ATTRIBUTES = "ATTRIBUTES MAPPING";
    public static final String EXPORT_SMALL_SIZE_COLUMNS = "SMALL COLUMNS";
    public static final String MULTIPLE_DATA_LIST = "MULTIPLE DATA LIST";
    public static final String CENTER_ALIGNMENT = "CENTER";
    public static final String LEFT_ALIGNMENT = "LEFT";
    public static final String RIGHT_ALIGNMENT = "RIGHT";

    public static final int CUSTOM_DATA_FORMAT_STYLE = -1;
    public static final int DEFAULT_DATA_FORMAT_STYLE = 0;
    public static final int STRING_DATA_FORMAT_STYLE = 1;
    public static final int DATE_DATA_FORMAT_STYLE = 2;
    public static final int INTEGER_DATA_FORMAT_STYLE = 3;
    public static final int BIGDECIMAL_DATA_FORMAT_STYLE = 4;
    public static final int DATE_TIME_DATA_FORMAT_STYLE = 5;

    protected final String COMPOSED_ID_ANNOTATION = javax.persistence.EmbeddedId.class.getName();
    protected final String EXCEL_IGNORED_PARAMETER_ANNOTATION = ExcelIgnoreParam.class.getName();
    protected final String EXCEL_CUSTOM_COLUMN_NAME_ANNOTATION = ExcelCustomColumnName.class.getName();
    protected final Class<ExcelFormatOptions> EXCEL_FORMAT_OPTIONS_ANNOTATION = ExcelFormatOptions.class;

    protected final short HEIGHT_FACTOR = 20;
    protected final short HEADER_ROW_HEIGHT = 32 * HEIGHT_FACTOR;
    protected final short ROW_HEIGHT = 15 * HEIGHT_FACTOR;

    protected static final short WIDTH_FACTOR = 263;
    public static final short DEFAULT_COLUMN_WIDTH = 30 * WIDTH_FACTOR;
    public static final short SMALL_COLUMN_WIDTH = 11 * WIDTH_FACTOR;
    public static final short MEDIUM_COLUMN_WIDTH = 60 * WIDTH_FACTOR;
    public static final short BIG_COLUMN_WIDTH = 90 * WIDTH_FACTOR;


    protected final int HEADER_ROW_INDEX = 0;
    protected final int DATA_START_ROW_INDEX = 1;

    protected final HorizontalAlignment ALIGN_CENTER = HorizontalAlignment.CENTER;
    protected final VerticalAlignment ALIGN_CENTER_VERTICAL = VerticalAlignment.CENTER;
    protected final HorizontalAlignment ALIGN_LEFT = HorizontalAlignment.LEFT;
    protected final HorizontalAlignment ALIGN_RIGHT = HorizontalAlignment.RIGHT;


    //REPORT DATA OBJECTS
    protected List<Object> dataList = null;
    protected List<String> customAttributes = null;
    protected Map<String, List> multipleDataLists = null;

    //EXCEL RELATED ATTRIBUTES
    protected Workbook workbook = null;
    protected final int SHEET_NAME_NO_START_INDEX = 2;
    protected int SHEET_NAME_NO = SHEET_NAME_NO_START_INDEX;
    protected final int MAXROWSHEET = 1000000;
    protected List<FieldOptions> referenceObjectFieldsOptions = new ArrayList<>();
    protected List<String> header = new ArrayList<>();
    protected int currentRowIndex = DATA_START_ROW_INDEX;
    protected int currentSheetIndex = 0;
    protected static String EMPTY = "";

    protected boolean globalNrGroupSeparation = true;

    protected CellStyle headerStyle;

    protected List<String> SMALL_SIZE_COLUMNS;
    protected List<String> MEDIUM_SIZE_COLUMNS;
    protected List<String> BIG_SIZE_COLUMNS;

    // messages
    protected final String FIELD_VALUE_EXTRACTION_ERROR = "Eroare la extragerea valorilor din obiecte!";
    protected final String DEFULT_SEET_NAME = "Sheet";

    private final String DATE_DATA_FORMAT = "dd/mm/yyyy ";
    private final String DATE_TIME_DATA_FORMAT = "dd/mm/yyyy  h:mm:ss";
    private final String INTEGER_DATA_FORMAT = "#,##0";
    private final String BIGDECIMAL_DATA_FORMAT = "#,##0.00";


    public ExcelGenerator() {
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        this.workbook = workbook;
        init();
        fetchDataFromInput(model);
        initializeCellStyles();
        if (this.dataList != null && !this.dataList.isEmpty()) {
            populateWorkbook(workbook, this.dataList, DEFULT_SEET_NAME);
        } else if (this.multipleDataLists != null && !this.multipleDataLists.isEmpty()) {
            for (Map.Entry<String, List> entry : this.multipleDataLists.entrySet()) {
                populateWorkbook(workbook, entry.getValue(), entry.getKey());
                resetGlobalData();
            }
        } else {
            workbook.createSheet(DEFULT_SEET_NAME);
        }
    }

    private void resetGlobalData() {
        this.header = new ArrayList<>();
        this.referenceObjectFieldsOptions = new ArrayList<>();
        currentSheetIndex++;
        SHEET_NAME_NO = SHEET_NAME_NO_START_INDEX;
        this.currentRowIndex = DATA_START_ROW_INDEX;
    }

    private void populateWorkbook(Workbook workbook, List dataList, String sheetName) {
        if (dataList != null && !dataList.isEmpty()) {
            setFieldsOptionsAndHeader(dataList.get(0).getClass().getDeclaredFields(), false);
        }
        generateReport(dataList, sheetName);
        workbook = this.workbook;
    }

    private void fetchDataFromInput(Map<String, Object> model) {
        this.dataList = (List<Object>) model.get(EXPORT_LIST_NAME);
        if (model.get(EXPORT_CUSTOM_ATTRIBUTES) != null) {
            customAttributes = (List<String>) model.get(EXPORT_CUSTOM_ATTRIBUTES);
        }

        if (model.get(MULTIPLE_DATA_LIST) != null) {
            multipleDataLists = (Map<String, List>) model.get(MULTIPLE_DATA_LIST);
        }
    }

    private void setFieldsOptionsAndHeader(Field[] fields, boolean isInComposite) {
        for (Field field : fields) {
            field.setAccessible(true);
            if (isFieldAnnotated(field, EXCEL_IGNORED_PARAMETER_ANNOTATION)) continue;
            if (!isFieldAnnotated(field, COMPOSED_ID_ANNOTATION)) {
                if (!isAttributeOfInterest(field.getName())) {
                    continue;
                }
                setFieldOptionsAndHeader(field, isInComposite);
            } else {
                setFieldsOptionsAndHeader(field.getType().getDeclaredFields(), true);
            }
        }
    }

    private boolean isAttributeOfInterest(String fieldName) {
        return customAttributes == null || customAttributes.isEmpty() || customAttributes.contains(fieldName);
    }

    private void setFieldOptionsAndHeader(Field field, boolean isInComposite) {
        processFieldsOptions(field, isInComposite);
        header.add(getHeaderEntryFromField(field));
    }

    private void processFieldsOptions(Field field, boolean isInComposite) {
        ExcelFormatOptions excelFormatOptionsAnnotation = (ExcelFormatOptions) getAnnotation(field, EXCEL_FORMAT_OPTIONS_ANNOTATION.getName());
        FieldOptions fieldOptions = getCellStyleFromOptions(excelFormatOptionsAnnotation, field.getType());
        fieldOptions.setInComposite(isInComposite);
        fieldOptions.setFieldName(field.getName());
        if (excelFormatOptionsAnnotation != null && excelFormatOptionsAnnotation.dimension() == SMALL_COLUMN_WIDTH) {
            SMALL_SIZE_COLUMNS.add(getHeaderEntryFromField(field));
        }
        if (excelFormatOptionsAnnotation != null && excelFormatOptionsAnnotation.dimension() == MEDIUM_COLUMN_WIDTH) {
            MEDIUM_SIZE_COLUMNS.add(getHeaderEntryFromField(field));
        }
        if (excelFormatOptionsAnnotation != null && excelFormatOptionsAnnotation.dimension() == BIG_COLUMN_WIDTH) {
            BIG_SIZE_COLUMNS.add(getHeaderEntryFromField(field));
        }
        this.referenceObjectFieldsOptions.add(fieldOptions);
    }

    private FieldOptions getCellStyleFromOptions(ExcelFormatOptions excelFormatOptions, Class objectType) {
        FieldOptions fieldOptions = new FieldOptions();
        String alignment = "";
        if (excelFormatOptions != null) {
            alignment = excelFormatOptions.alignment();
            fieldOptions.setNrGroupSeparation(excelFormatOptions.nrGroupSeparation());
        }

        if (excelFormatOptions != null && excelFormatOptions.format() != DEFAULT_DATA_FORMAT_STYLE) {
            if (excelFormatOptions.format() == BIGDECIMAL_DATA_FORMAT_STYLE) {
                fieldOptions.setStyle(getAlignedBigDecimalStyle(alignment));
            } else if (excelFormatOptions.format() == DATE_DATA_FORMAT_STYLE) {
                fieldOptions.setStyle(getAlignedDateStyle(alignment));
            } else if (excelFormatOptions.format() == DATE_TIME_DATA_FORMAT_STYLE) {
                fieldOptions.setStyle(getAlignedDateTimeStyle(alignment));
            } else if (excelFormatOptions.format() == INTEGER_DATA_FORMAT_STYLE) {
                fieldOptions.setStyle(getAlignedIntegerStyle(alignment));
            } else if (excelFormatOptions.format() == CUSTOM_DATA_FORMAT_STYLE && !excelFormatOptions.customFormatStyle().isEmpty()) {
                fieldOptions.setStyle(getAlignedSimpleStyle(alignment, excelFormatOptions.customFormatStyle()));
            } else {
                fieldOptions.setNrGroupSeparation(false);
                fieldOptions.setStyle(getAlignedSimpleStyle(alignment, null));
            }
        } else {
            if (objectType.isPrimitive()) {
                if (fieldOptions.isNrGroupSeparation()) {
                    if (objectType.equals(double.class) || objectType.equals(float.class)) {
                        fieldOptions.setStyle(getAlignedBigDecimalStyle(alignment));
                    } else if (objectType.equals(short.class) || objectType.equals(int.class) || objectType.equals(long.class)) {
                        fieldOptions.setStyle(getAlignedIntegerStyle(alignment));
                    } else {
                        fieldOptions.setStyle(getAlignedSimpleStyle(alignment, null));
                    }
                } else {
                    if (objectType.equals(double.class) || objectType.equals(float.class)
                            || objectType.equals(short.class) || objectType.equals(int.class) || objectType.equals(long.class)) {
                        fieldOptions.setStyle(getAlignedSimpleNumberStyle(alignment, null));
                    } else {
                        fieldOptions.setStyle(getAlignedSimpleStyle(alignment, null));
                    }
                }
            } else if (objectType.getSuperclass().equals(Number.class)) {
                if (fieldOptions.isNrGroupSeparation()) {
                    if (objectType.equals(Integer.class) || objectType.equals(Long.class) || objectType.equals(Short.class)) {
                        fieldOptions.setStyle(getAlignedIntegerStyle(alignment));
                    } else {
                        fieldOptions.setStyle(getAlignedBigDecimalStyle(alignment));
                    }
                } else {
                    fieldOptions.setStyle(getAlignedSimpleNumberStyle(alignment, null));
                }

            } else if (objectType.equals(Date.class)) {
                fieldOptions.setStyle(getAlignedDateStyle(alignment));
            } else {
                fieldOptions.setStyle(getAlignedSimpleStyle(alignment, null));
            }
        }
        return fieldOptions;
    }

    private CellStyle getAlignedSimpleStyle(String alignment, String format) {
        if (alignment == null || alignment.isEmpty()) {
            alignment = CENTER_ALIGNMENT;
        }
        return getAlignedStyle(alignment, format);
    }

    private CellStyle getAlignedSimpleNumberStyle(String alignment, String format) {
        if (alignment == null || alignment.isEmpty()) {
            alignment = RIGHT_ALIGNMENT;
        }
        return getAlignedStyle(alignment, format);
    }

    private CellStyle getAlignedBigDecimalStyle(String alignment) {
        if (alignment == null || alignment.isEmpty()) {
            alignment = RIGHT_ALIGNMENT;
        }
        return getAlignedStyle(alignment, BIGDECIMAL_DATA_FORMAT);
    }

    private CellStyle getAlignedIntegerStyle(String alignment) {
        if (alignment == null || alignment.isEmpty()) {
            alignment = RIGHT_ALIGNMENT;
        }
        return getAlignedStyle(alignment, INTEGER_DATA_FORMAT);
    }


    private CellStyle getAlignedDateStyle(String alignment) {
        if (alignment == null || alignment.isEmpty()) {
            alignment = CENTER_ALIGNMENT;
        }
        return getAlignedStyle(alignment, DATE_DATA_FORMAT);
    }

    private CellStyle getAlignedDateTimeStyle(String alignment) {
        if (alignment == null || alignment.isEmpty()) {
            alignment = CENTER_ALIGNMENT;
        }
        return getAlignedStyle(alignment, DATE_TIME_DATA_FORMAT);
    }

    private CellStyle getAlignedStyle(String alignment, String formatStyle) {
        Short format;
        if (formatStyle == null) {
            format = null;
        } else {
            format = workbook.getCreationHelper().createDataFormat().getFormat(formatStyle);
        }
        if (alignment != null && alignment.equals(RIGHT_ALIGNMENT)) {
            return createCellStyle(format, ALIGN_RIGHT);
        } else if (alignment != null && alignment.equals(LEFT_ALIGNMENT)) {
            return createCellStyle(format, ALIGN_LEFT);
        } else {
            return createCellStyle(format, ALIGN_CENTER);
        }
    }

    private CellStyle createCellStyle(Short format, HorizontalAlignment alignment) {
        CellStyle style = ExcelUtils.createFormat(this.workbook, (short) 11, IndexedColors.WHITE.getIndex(), false, false, alignment, ALIGN_CENTER_VERTICAL);
        if (format != null) style.setDataFormat(format);
        return style;
    }


    private void generateReport(List dataList, String sheetName) {
        createSheetAndSetHeader(sheetName);
        if (dataList != null) {
            for (Object obj : dataList) {
                addRow(getValuesFromObject(obj), sheetName);
            }
        }
    }

    private void createSheetAndSetHeader(String sheetName) {
        Sheet sheet = this.workbook.createSheet(sheetName);
        setHeader(sheet);
    }

    /**
     * Gets the values from  attribute of interest from object
     * If an attribute is a composite id, gets the values from it
     * And associates a style
     *
     * @param object - from where to get the attributes values
     * @return - list of object values and cell styles
     */
    private List<RowDetails> getValuesFromObject(Object object) {
        List<RowDetails> rowDetails = new ArrayList<>();
        for (FieldOptions fieldOptions : referenceObjectFieldsOptions) {
            RowDetails rowValue = new RowDetails();
            try {
                if (fieldOptions.isInComposite) {
                    Object composite = getCompositeIdFromOject(object);
                    if (composite != null) {
                        rowValue.setObject(getObjectValueByField(composite.getClass().getDeclaredField(fieldOptions.fieldName), composite, fieldOptions.isNrGroupSeparation()));
                    } else {
                        rowValue.setObject(null);
                    }
                } else {
                    rowValue.setObject(getObjectValueByField(object.getClass().getDeclaredField(fieldOptions.fieldName), object, fieldOptions.isNrGroupSeparation()));
                }
                rowValue.setStyle(fieldOptions.getStyle());
                rowDetails.add(rowValue);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new CustomException(FIELD_VALUE_EXTRACTION_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return rowDetails;
    }

    private Object getCompositeIdFromOject(Object object) throws IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (isFieldAnnotated(field, COMPOSED_ID_ANNOTATION)) {
                return field.get(object);
            }
        }
        return null;

    }


    private Object getObjectValueByField(Field field, Object object, boolean nrGroupSeparation) {
        try {
            field.setAccessible(true);
            if (field.get(object) instanceof Number) {
                if (globalNrGroupSeparation && nrGroupSeparation) {
                    return field.get(object);
                } else {
                    return field.get(object).toString();
                }
            } else {
                return field.get(object);
            }
        } catch (IllegalAccessException e) {
            throw new CustomException(FIELD_VALUE_EXTRACTION_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void addRow(List<RowDetails> rowElements, String sheetName) {
        if (rowElements == null || rowElements.isEmpty()) return;
        if ((this.currentRowIndex - MAXROWSHEET) == 0) {
            this.currentSheetIndex++;
            this.currentRowIndex = DATA_START_ROW_INDEX;
            createSheetAndSetHeader(sheetName + (SHEET_NAME_NO++));
        }

        int column = 0;
        Row row = this.workbook.getSheetAt(this.currentSheetIndex).createRow(this.currentRowIndex++);
        row.setHeight(ROW_HEIGHT);
        for (RowDetails element : rowElements) {
            createAndAddCell(element.getObject(), row, column++, element.getStyle());
        }
        setColumnsWidth();
    }

    private void createAndAddCell(Object input, Row row, int column, CellStyle cellStyle) {
        Cell cell = row.createCell(column);
        cell.setCellStyle(cellStyle);
        if (input == null) {
            cell.setCellValue(EMPTY);
        } else {
            if (input instanceof BigDecimal) {
                cell.setCellValue(((BigDecimal) input).doubleValue());
            } else if (input instanceof Integer) {
                cell.setCellValue((Integer) input);
            } else if (input instanceof Double) {
                cell.setCellValue((Double) input);
            } else if (input instanceof Long) {
                cell.setCellValue((Long) input);
            } else if (input instanceof Float) {
                cell.setCellValue((Float) input);
            } else if (input instanceof Short) {
                cell.setCellValue((Short) input);
            } else if (input instanceof Date) {
                cell.setCellValue((Date) input);
            } else {
                cell.setCellValue(String.valueOf(input));
            }
        }
    }

    private void setHeader(Sheet sheet) {
        Row row = sheet.createRow(HEADER_ROW_INDEX);
        row.setHeight(HEADER_ROW_HEIGHT);
        for (int column = 0; column < this.header.size(); column++) {
            createAndAddCell(header.get(column), row, column, headerStyle);
        }
        sheet.createFreezePane(0, 1); //FREEZE ONLY FIRST ROW WITH HEADER
    }

    private void initializeCellStyles() {
        headerStyle = ExcelUtils.createFormat(this.workbook, (short) 11, IndexedColors.GREY_25_PERCENT.getIndex(), false, true, ALIGN_CENTER, ALIGN_CENTER_VERTICAL);
    }

    /**
     * set every column width  from sheet
     */
    private void setColumnsWidth() {
        for (int i = 0; i <= header.size() - 1; i++) {
            if (header.get(i) != null && SMALL_SIZE_COLUMNS.contains(header.get(i))) {
                this.workbook.getSheetAt(currentSheetIndex).setColumnWidth(i, SMALL_COLUMN_WIDTH);
            } else if (header.get(i) != null && MEDIUM_SIZE_COLUMNS.contains(header.get(i))) {
                this.workbook.getSheetAt(currentSheetIndex).setColumnWidth(i, MEDIUM_COLUMN_WIDTH);
            } else if (header.get(i) != null && BIG_SIZE_COLUMNS.contains(header.get(i))) {
                this.workbook.getSheetAt(currentSheetIndex).setColumnWidth(i, BIG_COLUMN_WIDTH);
            } else {
                this.workbook.getSheetAt(currentSheetIndex).setColumnWidth(i, DEFAULT_COLUMN_WIDTH);
            }
        }
    }


    private String getHeaderEntryFromField(Field field) {
        String name = field.getName();
        String columnName = formatCamelCaseString(name);
        if (isFieldAnnotated(field, EXCEL_CUSTOM_COLUMN_NAME_ANNOTATION)) {
            columnName = getCustomColumnName(field);
        }
        return columnName;
    }

    private boolean isFieldAnnotated(Field field, String annotationFullPath) {
        return getAnnotation(field, annotationFullPath) != null;
    }

    private Annotation getAnnotation(Field field, String annotationFullPath) {
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getName().equals(annotationFullPath)) {
                return annotation;
            }
        }
        return null;
    }


    private String getCustomColumnName(Field field) {
        final ExcelCustomColumnName annotation = field.getAnnotation(ExcelCustomColumnName.class);
        return annotation.name();
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setGlobalNrGroupSeparation(boolean globalNrGroupSeparation) {
        this.globalNrGroupSeparation = globalNrGroupSeparation;
    }

    public static String formatCamelCaseString(String string) {
        StringBuilder splitString = new StringBuilder();
        String[] splitVector = string.split("(?<=[a-z])(?=[A-Z])");
        for (String word : splitVector) {
            splitString.append(word.toUpperCase()).append(" ");
        }
        return splitString.toString();
    }

    private void init() {
        this.SMALL_SIZE_COLUMNS = new ArrayList<>();
        this.MEDIUM_SIZE_COLUMNS = new ArrayList<>();
        this.BIG_SIZE_COLUMNS = new ArrayList<>();
    }

    @Getter
    @Setter
    static class RowDetails {
        private CellStyle style;
        Object object;
    }

    @Getter
    @Setter
    class FieldOptions {
        String fieldName;
        private CellStyle style;
        private boolean nrGroupSeparation;
        private boolean isInComposite;

        private FieldOptions() {
            this.nrGroupSeparation = globalNrGroupSeparation;
        }
    }
}
