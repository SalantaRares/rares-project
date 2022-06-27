package ro.webappspringdemo.db2.model.forestryEngineer.pdf;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Recapitulatie {
private BigDecimal rasinoase;
private BigDecimal fag;
private BigDecimal frPaCiNu;
private BigDecimal dt;
private BigDecimal dm;
private String altele;
private BigDecimal alteleCantitate;
private BigDecimal totalGeneral;
}
