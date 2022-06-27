package ro.webappspringdemo.db2.service.forestryEngineer;


import org.springframework.web.multipart.MultipartFile;
import ro.webappspringdemo.db2.dtos.AllClientsDto;
import ro.webappspringdemo.db2.model.forestryEngineer.AllClients;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

public interface ForestryEngineerService {
    List<AllClients> getAllClients(String user);

    List<AllClients> getTodayExpire(String user);

    List<AllClients> getExpireByGivenDate(Long date, String user);

    String insertNewClient(AllClientsDto allClientsDto, String user) throws ParseException;

    String updateClient(String nume,
                        String prenume,
                        String cnp,
                        Long dataRidicarePortie,
                        Long dataScadentaRidicarePortie,
                        BigDecimal cantitateTotala,
                        String um,
                        String imputernicit,
                        BigDecimal portieAn,
                        BigDecimal portieRamasaAniiAnteriori,
                        BigDecimal hectareDetinute,
                        BigDecimal portieRestanta,
                        String cnpImputernicit,
                        String aniiRestanta,
                        String user);

    String importClientsFromExcel(MultipartFile file, String user);

    String giveAllQuantity(String cnp, String user);

    String givePartQuantity(String cnp,BigDecimal portieMcPredata, String user);

    String extendDueDate(String cnp, Long date, String user);

    String updateRemainingYears(String cnp, String aniiRestanta, String user);

    List<String> getAniiRestanta(String cnp);
//    byte[] generatePdfData(String seria,
//                           String nr,
//                           String expeditor,
//                           String cuiExpeditor,
//                           String sediuExpeditor,
//                           String punctDeIncarcareExpeditor,
//                           String docProvenienta,
//                           String codUnic,
//                           String dataOraGenerare,
//                           String destinatar,
//                           String cnpCuiDestinatar,
//                           String sediuDestinatar,
//                           String punctDeDescarcareDestinatar,
//                           String indicativDestinatar,
//                           Boolean transportIntrerupt,
//                           String cauzaIntrerupere,
//                           String perioadaIntrerupere,
//                           String viza,
//                           String vizaPostPolitie,
//                           BigDecimal nrCrt,
//                           BigDecimal nrBuc,
//                           String sortiment,
//                           BigDecimal lungime,
//                           BigDecimal diametru,
//                           BigDecimal volum,
//                           BigDecimal rasinoase,
//                           BigDecimal fag,
//                           BigDecimal frPaCiNu,
//                           BigDecimal dt,
//                           BigDecimal dm,
//                           String altele,
//                           BigDecimal alteleCantitate,
//                           BigDecimal totalGeneral,
//                           String dataOraPlecareTransport,
//                           String codOffline,
//                           String semnatura1,
//                           String semnatura2,
//                           String dataOraSosireTransport,
//                           String emitent,
//                           String numePrenumeImputernicit,
//                           String semnaturaImputernicit,
//                           String mijlocDeTransport,
//                           String numeSiPrenumeConducator,
//                           String cnp,
//                           String semnaturaConducator,
//                           String dateIdentificarePermisVamal,
//                           String numePrenumeReceptioner,
//                           String semnaturaReceptioner);
}
