package ro.webappspringdemo.db2.enums;

public enum TotalSpecii {
    RASINOASE("RASINOASE", "RASINOASE"),
    FAG("FAG", "FAG"),
    QVERCINEE("QVERCINEE", "QVERCINEE"),
    FR_PA_CI_NU("FR_PA_CI_NU", "FR_PA_CI_NU"),
    DT("DT", "DT"),
    DM("DM", "DM"),
    ALTELE("ALTELE", "ALTELE");

    TotalSpecii() {
    }

    private String value;
    private String uiValue;

    TotalSpecii(String value, String uiValue) {
        this.value = value;
        this.uiValue = uiValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUiValue() {
        return uiValue;
    }

    public void setUiValue(String uiValue) {
        this.uiValue = uiValue;
    }

    public static String getValueByUiValue(String uiValue) {
        for (TotalSpecii entry : TotalSpecii.class.getEnumConstants()) {
            if (entry.getUiValue().equals(uiValue)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
