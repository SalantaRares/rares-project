package ro.webappspringdemo.db2.service.forestryEngineer;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.webappspringdemo.db2.dtos.AllClientsDto;
import ro.webappspringdemo.db2.dtos.AllClientsDtoTest;
import ro.webappspringdemo.db2.entities.AllClientsEntity;
import ro.webappspringdemo.db2.entities.LogsUtilizatoriRaresEntity;
import ro.webappspringdemo.db2.model.forestryEngineer.AllClients;
import ro.webappspringdemo.db2.repository.dao.ForestryEngineerDaoImpl;
import ro.webappspringdemo.exceptions.CustomException;
import ro.webappspringdemo.utils.Utils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ForestryEngineerServiceImpl implements ForestryEngineerService {
    public final ForestryEngineerDaoImpl forestryEngineerDao;

    public ForestryEngineerServiceImpl(ForestryEngineerDaoImpl forestryEngineerDao) {
        this.forestryEngineerDao = forestryEngineerDao;
    }

    @Override
    public List<AllClients> getAllClients(String user) {
        LogsUtilizatoriRaresEntity logsUtilizatoriRaresEntity = new LogsUtilizatoriRaresEntity(forestryEngineerDao.requestId().toString(),
                "null", "null", "null", "GET ALL",
                user, (new Date()).toString(), "UTILIZATORI_RARES");
        try {
            forestryEngineerDao.insertObject(logsUtilizatoriRaresEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Probleme la inserarea in tabela de log uri. Eroarea: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return forestryEngineerDao.getAllClients();

    }

    @Override
    public List<AllClients> getTodayExpire(String user) {

        LogsUtilizatoriRaresEntity logsUtilizatoriRaresEntity = new LogsUtilizatoriRaresEntity(forestryEngineerDao.requestId().toString(),
                "null", "null", "null", "GET TODAY EXPIRE",
                user, (new Date()).toString(), "UTILIZATORI_RARES");
        try {
            forestryEngineerDao.insertObject(logsUtilizatoriRaresEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Probleme la inserarea in tabela de log uri. Eroarea: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return forestryEngineerDao.getTodayExpire();
    }

    @Override
    public List<AllClients> getExpireByGivenDate(Long date, String user) {
        LogsUtilizatoriRaresEntity logsUtilizatoriRaresEntity = new LogsUtilizatoriRaresEntity(forestryEngineerDao.requestId().toString(),
                "null", "null", "null", "GET EXPIRE BY GIVEN DATE",
                user, (new Date()).toString(), "UTILIZATORI_RARES");
        try {
            forestryEngineerDao.insertObject(logsUtilizatoriRaresEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Probleme la inserarea in tabela de log uri. Eroarea: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return forestryEngineerDao.getExpireByGivenDate(new Date(date));
    }

    @Override
    public String insertNewClient(AllClientsDto allClientsDto, String user) {
        AllClientsEntity entity = new AllClientsEntity(allClientsDto.getNume(), allClientsDto.getPrenume(), allClientsDto.getDataRidicarePortie(), allClientsDto.getDataScadentaRidicarePortie(), allClientsDto.getCantitateTotala(), allClientsDto.getUm(), allClientsDto.getImputernicit(), allClientsDto.getPortieAn(), allClientsDto.getPortieRamasaAniiAnteriori(),
                allClientsDto.getHectareDetinute(), allClientsDto.getPortieRestanta(), allClientsDto.getCnpImputernicit(), allClientsDto.getAniiRestanta());
        entity.setCnp(allClientsDto.getCnp());

        LogsUtilizatoriRaresEntity logsUtilizatoriRaresEntity = new LogsUtilizatoriRaresEntity(forestryEngineerDao.requestId().toString(),
                "a line", allClientsDto.toString(), entity.toString(), "INSERT NEW CLIENT",
                user, (new Date()).toString(), "UTILIZATORI_RARES");
        try {
            forestryEngineerDao.insertObject(logsUtilizatoriRaresEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Probleme la inserarea in tabela de log uri. Eroarea: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            forestryEngineerDao.insertObject(entity);
            return "Inserare cu succes";
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("A aparut o eroare" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public String updateClient(String nume,
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
                               String user) {
        AllClientsEntity entity = (AllClientsEntity) forestryEngineerDao.getObjectById(cnp, AllClientsEntity.class);
        AllClientsEntity entity1 = entity;
        if (Objects.nonNull(nume) && !(nume.equals(entity.getNume()))) {
            entity.setNume(nume);
        }
        if (Objects.nonNull(prenume) && (prenume.equals(entity.getPrenume()))) {
            entity.setPrenume(prenume);
        }
        if (dataRidicarePortie != null && (new Date(dataRidicarePortie) != entity.getDataRidicarePortie())) {
            entity.setDataRidicarePortie(new Date(dataRidicarePortie));
        }
        if (dataScadentaRidicarePortie != null && (new Date(dataScadentaRidicarePortie) != entity.getDataScadentaRidicarePortie())) {
            entity.setDataScadentaRidicarePortie(new Date(dataScadentaRidicarePortie));
        }
        if (cantitateTotala != null && (cantitateTotala != entity.getCantitateTotala())) {
            entity.setCantitateTotala(cantitateTotala);
        }
        if (Objects.nonNull(um) && !(um.equals(entity.getUm()))) {
            entity.setUm(um);
        }
        if (Objects.nonNull(imputernicit) && !(imputernicit.equals(entity.getImputernicit()))) {
            entity.setImputernicit(imputernicit);
        }
        if (portieAn != null && portieAn != entity.getPortieAn()) {
            entity.setPortieAn(portieAn);
        }
        if (portieRamasaAniiAnteriori != null && portieRamasaAniiAnteriori != entity.getPortieRamasaAniiAnteriori()) {
            entity.setPortieRamasaAniiAnteriori(portieRamasaAniiAnteriori);
        }
        if (hectareDetinute != null && hectareDetinute != entity.getHectareDetinute()) {
            entity.setHectareDetinute(hectareDetinute);
        }
        if (portieRestanta != null && portieRestanta != entity.getPortieRestanta()) {
            entity.setPortieRestanta(portieRestanta);
        }
        if (Objects.nonNull(cnpImputernicit) && !(cnpImputernicit.equals(entity.getCnpImputernicit()))) {
            entity.setCnpImputernicit(cnpImputernicit);
        }
        if (Objects.nonNull(aniiRestanta) && !(aniiRestanta.equals(entity.getAniiRestanta()))) {
            entity.setAniiRestanta(aniiRestanta);
        }

        LogsUtilizatoriRaresEntity logsUtilizatoriRaresEntity = new LogsUtilizatoriRaresEntity(forestryEngineerDao.requestId().toString(),
                "A LINE", entity1.toString(), entity.toString(), "UPDATE OBJECT",
                user, (new Date()).toString(), "UTILIZATORI_RARES");
        try {
            forestryEngineerDao.insertObject(logsUtilizatoriRaresEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Probleme la inserarea in tabela de log uri. Eroarea: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            forestryEngineerDao.updateObject(entity);
            return "Update cu succes";
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Am intampinat o eroare " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String importClientsFromExcel(MultipartFile file, String user) {
        List<AllClientsDtoTest> list = (List<AllClientsDtoTest>) Utils.extractFromExcelWithColumnOrderExtractionModeCheckType(file, AllClientsDtoTest.class);
        List<AllClientsEntity> allClientsEntityList = new ArrayList<>();
        List<AllClientsDtoTest> listWithErrors = new ArrayList<>();
        list.stream().distinct().collect(Collectors.toList());
        list.stream().forEach(entry -> {
            AllClientsEntity newEntry = new AllClientsEntity(entry);
            newEntry.setCnp(entry.getCnp());
            if (newEntry.isValid()) {
                listWithErrors.add(entry);
            } else {
                allClientsEntityList.add(newEntry);
            }
        });

        LogsUtilizatoriRaresEntity logsUtilizatoriRaresEntity = new LogsUtilizatoriRaresEntity(forestryEngineerDao.requestId().toString(),
                "INSERT FROM EXCEL", "null", allClientsEntityList.toString(), "INSERT",
                user, (new Date()).toString(), "UTILIZATORI_RARES");
        try {
            forestryEngineerDao.insertObject(logsUtilizatoriRaresEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Probleme la inserarea in tabela de log uri. Eroarea: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (!allClientsEntityList.isEmpty()) {
            try {
                forestryEngineerDao.insertClientsFromExcel(allClientsEntityList);
                return "Inserarea de date din excel a avut loc cu succes!";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String giveAllQuantity(String cnp, String user) {
        AllClientsEntity entity = (AllClientsEntity) forestryEngineerDao.getObjectById(cnp, AllClientsEntity.class);
        Date date = entity.getDataRidicarePortie();
        if (!(entity.getDataRidicarePortie().equals(new Date()))) {
            entity.setDataRidicarePortie(new Date());
            entity.setCantitateTotala(BigDecimal.ZERO);
        }

        LogsUtilizatoriRaresEntity logsUtilizatoriRaresEntity = new LogsUtilizatoriRaresEntity(forestryEngineerDao.requestId().toString(),
                "DATA_RIDICARE_PORTIE", date.toString(), entity.getDataRidicarePortie().toString(), "UPDATE",
                user, (new Date()).toString(), "UTILIZATORI_RARES");
        try {
            forestryEngineerDao.insertObject(logsUtilizatoriRaresEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Probleme la inserarea in tabela de log uri. Eroarea: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Date dateFromMillis = new Date();
        try {
            forestryEngineerDao.updateObject(entity);
            return "S-a predat o portie pentru clientul: " + entity.getNume() + " " + entity.getPrenume() + " azi, in data de: " + Utils.dateToYYYYMMDD(dateFromMillis.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("S-a produs o eroare la actualizarea datei: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String givePartQuantity(String cnp, BigDecimal portieMcPredata, String user) {
        AllClientsEntity entity = (AllClientsEntity) forestryEngineerDao.getObjectById(cnp, AllClientsEntity.class);
        Date date = entity.getDataRidicarePortie();
        if (!(entity.getDataRidicarePortie().equals(new Date()))) {
            entity.setDataRidicarePortie(new Date());
            if (entity.getCantitateTotala().compareTo(portieMcPredata) == 1) {
                entity.setCantitateTotala(entity.getCantitateTotala().subtract(portieMcPredata));
                if (entity.getPortieRamasaAniiAnteriori().compareTo(portieMcPredata) == 1) {
                    entity.setPortieRamasaAniiAnteriori(entity.getPortieRamasaAniiAnteriori().subtract(portieMcPredata));
                }
                if (entity.getPortieRestanta().compareTo(portieMcPredata) == 1) {
                    entity.setPortieRestanta(entity.getPortieRestanta().subtract(portieMcPredata));
                }
            } else {
                throw new CustomException("Portia predata este mai mare decat portia cuvenita", HttpStatus.BAD_REQUEST);
            }
        }

        List<LogsUtilizatoriRaresEntity> list = new ArrayList<>();
        LogsUtilizatoriRaresEntity logsUtilizatoriRaresEntity = new LogsUtilizatoriRaresEntity(forestryEngineerDao.requestId().toString(),
                "DATA_RIDICARE_PORTIE", date.toString(), entity.getDataRidicarePortie().toString(), "UPDATE",
                user, (new Date()).toString(), "UTILIZATORI_RARES");
        list.add(logsUtilizatoriRaresEntity);
        logsUtilizatoriRaresEntity = new LogsUtilizatoriRaresEntity(forestryEngineerDao.requestId().toString(),
                "CANTITATE_TOTALA", date.toString(), entity.getCantitateTotala().toString(), "UPDATE",
                user, (new Date()).toString(), "UTILIZATORI_RARES");
        list.add(logsUtilizatoriRaresEntity);
        try {
            list.stream().forEach(entry -> {
                forestryEngineerDao.insertObject(entry);
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Probleme la inserarea in tabela de log uri. Eroarea: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Date dateFromMillis = new Date();
        try {
            forestryEngineerDao.updateObject(entity);
            return "S-a predat o portie pentru clientul: " + entity.getNume() + " " + entity.getPrenume() + " azi, in data de: " + Utils.dateToYYYYMMDD(dateFromMillis.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("S-a produs o eroare la actualizarea datei: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String extendDueDate(String cnp, Long date, String user) {
        AllClientsEntity entity = (AllClientsEntity) forestryEngineerDao.getObjectById(cnp, AllClientsEntity.class);
        entity.setDataScadentaRidicarePortie(new Date(date));
        LogsUtilizatoriRaresEntity logsUtilizatoriRaresEntity = new LogsUtilizatoriRaresEntity(forestryEngineerDao.requestId().toString(),
                "DATA_SCADENTA_RIDICARE_PORTIE", date.toString(), entity.getDataScadentaRidicarePortie().toString(), "UPDATE",
                user, (new Date()).toString(), "UTILIZATORI_RARES");
        try {
            forestryEngineerDao.insertObject(logsUtilizatoriRaresEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Probleme la inserarea in tabela de log uri. Eroarea: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            forestryEngineerDao.updateObject(entity);
            return "S-a prelungit data scadenta ridicare portie pentru clientul: " + entity.getNume() + " " + entity.getPrenume() + " pana in data in data de: " + Utils.dateToYYYYMMDD(date);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("S-a produs o eroare la actualizarea datei: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public String updateRemainingYears(String cnp, String aniiRestanta, String user) {
        AllClientsEntity entity = (AllClientsEntity) forestryEngineerDao.getObjectById(cnp, AllClientsEntity.class);
        entity.setAniiRestanta(aniiRestanta);
        LogsUtilizatoriRaresEntity logsUtilizatoriRaresEntity = new LogsUtilizatoriRaresEntity(forestryEngineerDao.requestId().toString(),
                "ANII_RESTANTA", aniiRestanta.toString(), entity.getAniiRestanta(), "GET",
                user, (new Date()).toString(), "UTILIZATORI_RARES");
        try {
            forestryEngineerDao.insertObject(logsUtilizatoriRaresEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Probleme la inserarea in tabela de log uri. Eroarea: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            forestryEngineerDao.updateObject(entity);
            return "S-au actualizat anii restanta pentru clientul: " + entity.getNume() + " " + entity.getPrenume();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("S-a produs o eroare la actualizarea datei: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<String> getAniiRestanta(String cnp) {
        List<String> stringList = new ArrayList<>();
        forestryEngineerDao.getAniiRestanta(cnp).stream().forEach(entry ->{
            stringList.add(entry.getAniiRestanta());
        });
        return stringList;
    }

//    public String getTotalSpecii(String specie) {
//        return TotalSpecii.getValueByUiValue(specie);
//    }
//
//    public byte[] generatePdfData(String seria,
//                                  String nr,
//                                  String expeditor,
//                                  String cuiExpeditor,
//                                  String sediuExpeditor,
//                                  String punctDeIncarcareExpeditor,
//                                  String docProvenienta,
//                                  String codUnic,
//                                  String dataOraGenerare,
//                                  String destinatar,
//                                  String cnpCuiDestinatar,
//                                  String sediuDestinatar,
//                                  String punctDeDescarcareDestinatar,
//                                  String indicativDestinatar,
//                                  Boolean transportIntrerupt,
//                                  String cauzaIntrerupere,
//                                  String perioadaIntrerupere,
//                                  String viza,
//                                  String vizaPostPolitie,
//                                  BigDecimal nrCrt,
//                                  BigDecimal nrBuc,
//                                  String sortiment,
//                                  BigDecimal lungime,
//                                  BigDecimal diametru,
//                                  BigDecimal volum,
//                                  BigDecimal rasinoase,
//                                  BigDecimal fag,
//                                  BigDecimal frPaCiNu,
//                                  BigDecimal dt,
//                                  BigDecimal dm,
//                                  String altele,
//                                  BigDecimal alteleCantitate,
//                                  BigDecimal totalGeneral,
//                                  String dataOraPlecareTransport,
//                                  String codOffline,
//                                  String semnatura1,
//                                  String semnatura2,
//                                  String dataOraSosireTransport,
//                                  String emitent,
//                                  String numePrenumeImputernicit,
//                                  String semnaturaImputernicit,
//                                  String mijlocDeTransport,
//                                  String numeSiPrenumeConducator,
//                                  String cnp,
//                                  String semnaturaConducator,
//                                  String dateIdentificarePermisVamal,
//                                  String numePrenumeReceptioner,
//                                  String semnaturaReceptioner) {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        Date dataOraGenerarreToDate = null;
//        Date dataOraPlecareTransportToDate = null;
//        Date dataOraSosireTransportToDate = null;
//        try {
//            if (dataOraGenerare != null) dataOraGenerarreToDate = formatter.parse(dataOraGenerare);
//            if (dataOraPlecareTransport != null) dataOraPlecareTransportToDate = formatter.parse(dataOraPlecareTransport);
//            if (dataOraSosireTransport != null) dataOraSosireTransportToDate = formatter.parse(dataOraSosireTransport);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        PdfModel pdfModel = new PdfModel(seria,nr,expeditor,cuiExpeditor,sediuExpeditor,punctDeIncarcareExpeditor,docProvenienta,codUnic,dataOraGenerarreToDate,destinatar,cnpCuiDestinatar,sediuDestinatar,punctDeDescarcareDestinatar,
//                indicativDestinatar,transportIntrerupt,cauzaIntrerupere,perioadaIntrerupere,viza,vizaPostPolitie,nrCrt,nrBuc,sortiment,lungime,diametru,volum,rasinoase,
//                fag,frPaCiNu,dt,dm,altele,alteleCantitate,totalGeneral,dataOraPlecareTransportToDate,codOffline,semnatura1,semnatura2,dataOraSosireTransportToDate,emitent,numePrenumeImputernicit,
//                semnaturaImputernicit,mijlocDeTransport,numeSiPrenumeConducator,cnp,semnaturaConducator,dateIdentificarePermisVamal,numePrenumeReceptioner,semnaturaReceptioner);
//        System.out.println("pdfModel*********" + pdfModel);
//        try {
//            if (pdfModel !=null) {
//                String jsonValue = Utils.toJSON(pdfModel);
//                System.out.println("jsonValue*********" + jsonValue);
//                RtfGenerator rtfGen = new RtfGenerator();
//                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/TemplateFinal.rtf");
//                System.out.println("inputStream********" + inputStream);
//                try {
//                    return rtfGen.getStatement(inputStream, jsonValue);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                throw new CustomException("Nu exista date pentru a genera extrasul!", HttpStatus.BAD_REQUEST);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public PdfModel getStatment(String unit, String contract, String filterCriteria, BigDecimal swift, BigDecimal die, BigDecimal clientNotification) {
//
////        List<PdfData> pdfDataList = statisticalCodingDao.getAllPdfData(unit, contract, filterCriteria);
////        PdfView pdfView = new PdfView();
////        List<PdfViewRow> pdfViewRowList = new ArrayList<>();
////        pdfDataList.stream().forEach(entry -> {
////            PdfViewRow pdfViewRow = new PdfViewRow(entry.getContractRefNo(), entry.getBookDt(), entry.getUltBeneficiary2(), entry.getCustomerType(), entry.getShortName(),
////                    entry.getUltBeneficiary3(), entry.getUltBeneficiary4(), entry.getBranchName(), entry.getBranchCode(), entry.getCrCcy(), entry.getCrAmount(), entry.getCrValueDate(),
////                    entry.getByOrderOf2(), entry.getSender(), entry.getSenderName(), entry.getContractSrc(), entry.getOrderedAmt(), entry.getByOrderOf1(), entry.getByOrderOf3(),
////                    entry.getByOrderOf4(), entry.getByOrderOf5(), entry.getOrdInst(), entry.getOrdInstName(), entry.getUltBeneficiary1(), entry.getPaymentDetails1(), entry.getPaymentDetails2(),
////                    entry.getRemarks(), entry.getOver50k(), entry.getCodificare(), entry.getNecesitaCodificare(), entry.getDataProcesarii(), RomanianNumbersToWords.convert(entry.getCrAmount()),
////                    Utils.dateDDMMYYYToString(entry.getBookDt()), Utils.dateToString(entry.getBookDt()), Utils.dateDDMMYYYToString(entry.getBookDt()));
////            if (swift.equals(BigDecimal.ONE)) {
////                pdfViewRow.setSwift("Y");
////            }
////            if (clientNotification.equals(BigDecimal.ONE)) {
////                pdfViewRow.setNotification("Y");
////            }
////            if (die.equals(BigDecimal.ONE)) {
////                pdfViewRow.setDie("Y");
////            }
////            pdfViewRowList.add(pdfViewRow);
////        });
////        pdfView.setPdfViewRow(pdfViewRowList);
////        return pdfView;
//        return null;
//    }
}
