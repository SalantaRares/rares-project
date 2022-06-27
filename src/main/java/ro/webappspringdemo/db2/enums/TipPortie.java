package ro.webappspringdemo.db2.enums;

public enum TipPortie {
    CANTITATE_TOTALA("CANTITATE_TOTALA"),
    PORTIE_RAMASA_ANII_ANTERIORI("PORTIE_RAMASA_ANII_ANTERIORI"),
    PORTIE_RESTANTA("PORTIE_RESTANTA");

    TipPortie(String value) {
        this.value = value;
    }

    TipPortie() {
    }
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
