package ro.webappspringdemo.db2.model.forestryEngineer.pdf;

import lombok.Data;

@Data
public class Destinatar {
    private String destinatar;
    private String cnpCuiDestinatar;
    private String sediuDestinatar;
    private String punctDeDescarcareDestinatar;
    private String indicativDestinatar;
    private Boolean transportIntrerupt;
    private String cauzaIntrerupere;
    private String perioadaIntrerupere;
    private String viza;
    private String vizaPostPolitie;
}
