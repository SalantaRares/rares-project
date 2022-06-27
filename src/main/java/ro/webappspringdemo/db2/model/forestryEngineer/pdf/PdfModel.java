package ro.webappspringdemo.db2.model.forestryEngineer.pdf;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PdfModel {
    //private Header header;
    private String seria;
    private String nr;

    //private Expeditor expeditor;
    private String expeditor;
    private String cuiExpeditor;
    private String sediuExpeditor;
    private String punctDeIncarcareExpeditor;
    private String docProvenienta;
    private String codUnic;
    private Date dataOraGenerare;

    //  private Destinatar destinatar;
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

    //private List<Specificatia> specificatia;
    private BigDecimal nrCrt;
    private BigDecimal nrBuc;
    private String sortiment;
    private BigDecimal lungime;
    private BigDecimal diametru;
    private BigDecimal volum;

    private BigDecimal rasinoase;
    private BigDecimal fag;
    private BigDecimal frPaCiNu;
    private BigDecimal dt;
    private BigDecimal dm;
    private String altele;
    private BigDecimal alteleCantitate;
    private BigDecimal totalGeneral;
    private Date dataOraPlecareTransport;
    private String codOffline;
    private String semnatura1;
    private String semnatura2;
    private Date dataOraSosireTransport;
    private String emitent;
    private String numePrenumeImputernicit;
    private String semnaturaImputernicit;
    private String mijlocDeTransport;
    private String numeSiPrenumeConducator;
    private String cnp;
    private String semnaturaConducator;
    private String dateIdentificarePermisVamal;
    private String numePrenumeReceptioner;
    private String semnaturaReceptioner;

    public PdfModel(String seria,
                    String nr,
                    String expeditor,
                    String cuiExpeditor,
                    String sediuExpeditor,
                    String punctDeIncarcareExpeditor,
                    String docProvenienta,
                    String codUnic,
                    Date dataOraGenerare,
                    String destinatar,
                    String cnpCuiDestinatar,
                    String sediuDestinatar,
                    String punctDeDescarcareDestinatar,
                    String indicativDestinatar,
                    Boolean transportIntrerupt,
                    String cauzaIntrerupere,
                    String perioadaIntrerupere,
                    String viza,
                    String vizaPostPolitie,
                    BigDecimal nrCrt,
                    BigDecimal nrBuc,
                    String sortiment,
                    BigDecimal lungime,
                    BigDecimal diametru,
                    BigDecimal volum,
                    BigDecimal rasinoase,
                    BigDecimal fag,
                    BigDecimal frPaCiNu,
                    BigDecimal dt,
                    BigDecimal dm,
                    String altele,
                    BigDecimal alteleCantitate,
                    BigDecimal totalGeneral,
                    Date dataOraPlecareTransport,
                    String codOffline,
                    String semnatura1,
                    String semnatura2,
                    Date dataOraSosireTransport,
                    String emitent,
                    String numePrenumeImputernicit,
                    String semnaturaImputernicit,
                    String mijlocDeTransport,
                    String numeSiPrenumeConducator,
                    String cnp,
                    String semnaturaConducator,
                    String dateIdentificarePermisVamal,
                    String numePrenumeReceptioner,
                    String semnaturaReceptioner) {
        this.seria = seria;
        this.nr = nr;
        this.expeditor = expeditor;
        this.cuiExpeditor = cuiExpeditor;
        this.sediuExpeditor = sediuExpeditor;
        this.punctDeIncarcareExpeditor = punctDeIncarcareExpeditor;
        this.docProvenienta = docProvenienta;
        this.codUnic = codUnic;
        this.dataOraGenerare = dataOraGenerare;
        this.destinatar = destinatar;
        this.cnpCuiDestinatar = cnpCuiDestinatar;
        this.sediuDestinatar = sediuDestinatar;
        this.punctDeDescarcareDestinatar = punctDeDescarcareDestinatar;
        this.indicativDestinatar = indicativDestinatar;
        this.transportIntrerupt = transportIntrerupt;
        this.cauzaIntrerupere = cauzaIntrerupere;
        this.perioadaIntrerupere = perioadaIntrerupere;
        this.viza = viza;
        this.vizaPostPolitie = vizaPostPolitie;
        this.nrCrt = nrCrt;
        this.nrBuc = nrBuc;
        this.sortiment = sortiment;
        this.lungime = lungime;
        this.diametru = diametru;
        this.volum = volum;
        this.rasinoase = rasinoase;
        this.fag = fag;
        this.frPaCiNu = frPaCiNu;
        this.dt = dt;
        this.dm = dm;
        this.altele = altele;
        this.alteleCantitate = alteleCantitate;
        this.totalGeneral = totalGeneral;
        this.dataOraPlecareTransport = dataOraPlecareTransport;
        this.codOffline = codOffline;
        this.semnatura1 = semnatura1;
        this.semnatura2 = semnatura2;
        this.dataOraSosireTransport = dataOraSosireTransport;
        this.emitent = emitent;
        this.numePrenumeImputernicit = numePrenumeImputernicit;
        this.semnaturaImputernicit = semnaturaImputernicit;
        this.mijlocDeTransport = mijlocDeTransport;
        this.numeSiPrenumeConducator = numeSiPrenumeConducator;
        this.cnp = cnp;
        this.semnaturaConducator = semnaturaConducator;
        this.dateIdentificarePermisVamal = dateIdentificarePermisVamal;
        this.numePrenumeReceptioner = numePrenumeReceptioner;
        this.semnaturaReceptioner = semnaturaReceptioner;
    }
}
