package ro.webappspringdemo.db2.model.forestryEngineer.pdf;

import lombok.Data;

import java.util.Date;

@Data
public class Expeditor {
    private String expeditor;
    private String cuiExpeditor;
    private String sediuExpeditor;
    private String punctDeIncarcareExpeditor;
    private String docProvenienta;
    private String codUnic;
    private Date dataOraGenerare;
}
