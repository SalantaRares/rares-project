package ro.webappspringdemo.db2.model.forestryEngineer.pdf;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Specificatia {
    private BigDecimal nrCrt;
    private BigDecimal nrBuc;
    private String sortiment;
    private BigDecimal lungime;
    private BigDecimal diametru;
    private BigDecimal volum;
}
