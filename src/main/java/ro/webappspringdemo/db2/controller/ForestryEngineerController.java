package ro.webappspringdemo.db2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ro.webappspringdemo.db2.dtos.AllClientsDto;
import ro.webappspringdemo.db2.enums.TipPortie;
import ro.webappspringdemo.db2.service.forestryEngineer.ForestryEngineerService;
import ro.webappspringdemo.excel.export.ExcelGenerator;
import ro.webappspringdemo.utils.Utils;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api/forestry-engineer")
public class ForestryEngineerController {

    public final ForestryEngineerService forestryEngineerService;

    public ForestryEngineerController(ForestryEngineerService forestryEngineerService) {
        this.forestryEngineerService = forestryEngineerService;
    }

    @GetMapping("/all-clients")
    public ResponseEntity getAllClients(Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(forestryEngineerService.getAllClients(Utils.getUserNameFromPrincipal(principal)));
    }

    @GetMapping("/all-clients/export")
    public ModelAndView getReportAllClients(Principal principal) {
        return new ModelAndView(new ExcelGenerator(), ExcelGenerator.EXPORT_LIST_NAME, forestryEngineerService.getAllClients(Utils.getUserNameFromPrincipal(principal)));
    }

    @GetMapping("/expire-today")
    public ResponseEntity getTodayExpire(Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(forestryEngineerService.getTodayExpire(Utils.getUserNameFromPrincipal(principal)));
    }

    @GetMapping("/expire-today/export")
    public ModelAndView getReportTodayExpire(Principal principal) {
        return new ModelAndView(new ExcelGenerator(), ExcelGenerator.EXPORT_LIST_NAME, forestryEngineerService.getTodayExpire(Utils.getUserNameFromPrincipal(principal)));
    }

    @GetMapping("/expire-by-given-date")
    public ResponseEntity getExpirebyGivenDate(@RequestParam(name = "date") Long date, Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(forestryEngineerService.getExpireByGivenDate(date, Utils.getUserNameFromPrincipal(principal)));
    }

    @GetMapping("/expire-by-given-date/export")
    public ModelAndView getReportExpirebyGivenDate(@RequestParam(name = "date") Long date, Principal principal) {
        return new ModelAndView(new ExcelGenerator(), ExcelGenerator.EXPORT_LIST_NAME, forestryEngineerService.getExpireByGivenDate(date, Utils.getUserNameFromPrincipal(principal)));
    }

    @PostMapping("/new-client")
    public ResponseEntity insertNewClient(@RequestBody AllClientsDto allClientsDto, Principal principal) throws ParseException {
        return ResponseEntity.status(HttpStatus.OK).body(forestryEngineerService.insertNewClient(allClientsDto, Utils.getUserNameFromPrincipal(principal)));
    }

    @PutMapping("/ridicare-portie")
    public ResponseEntity giveAllQuantity(@RequestParam(name = "CNP") String cnp, Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(forestryEngineerService.giveAllQuantity(cnp, Utils.getUserNameFromPrincipal(principal)));
    }

    @PutMapping("/ridicare-bucata-portie")
    public ResponseEntity givePartQuantity(@RequestParam(name = "CNP") String cnp,
                                           @RequestParam(name = "portieMcPredata") BigDecimal portieMcPredata,
                                           @RequestParam(name = "anii_restanta")String aniiRestanta,
                                           Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(forestryEngineerService.givePartQuantity(cnp, portieMcPredata, Utils.getUserNameFromPrincipal(principal)));
    }

    @PutMapping("/prelungire-data-scadenta-portie")
    public ResponseEntity extendDueDate(@RequestParam(name = "CNP") String cnp,
                                        @RequestParam(name = "data") Long date,
                                        Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(forestryEngineerService.extendDueDate(cnp, date, Utils.getUserNameFromPrincipal(principal)));
    }

    @PutMapping("/actualizare-anii-restanta")
    public ResponseEntity updateRemainingYears(@RequestParam(name = "CNP") String cnp,
                                               @RequestParam(name = "aniiRestanta") String aniiRestanta,
                                               Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(forestryEngineerService.updateRemainingYears(cnp, aniiRestanta, Utils.getUserNameFromPrincipal(principal)));
    }

    @GetMapping("/tip-portie")
    public ResponseEntity getTipPortie(){
        List<TipPortie> sEnum = Arrays.asList(TipPortie.values());
        return ResponseEntity.status(HttpStatus.OK).body(sEnum);
    }

    @GetMapping("/anii-restanta")
    public ResponseEntity getAniiRestanta(@RequestParam(name = "cnp")String cnp){
        return ResponseEntity.status(HttpStatus.OK).body(forestryEngineerService.getAniiRestanta(cnp));
    }
    @PutMapping("/edit-client")
    public ResponseEntity insertNewClient(
            @RequestParam(name = "nume", required = false) String nume,
            @RequestParam(name = "prenume", required = false) String prenume,
            @RequestParam(name = "cnp") String cnp,
            @RequestParam(name = "dataRidicarePortie", required = false) Long dataRidicarePortie,
            @RequestParam(name = "dataScadentaRidicarePortie", required = false) Long dataScadentaRidicarePortie,
            @RequestParam(name = "cantitateTotala", required = false) BigDecimal cantitateTotala,
            @RequestParam(name = "um", required = false) String um,
            @RequestParam(name = "imputernicit", required = false) String imputernicit,
            @RequestParam(name = "portieAn", required = false) BigDecimal portieAn,
            @RequestParam(name = "portieRamasaAniiAnteriori", required = false) BigDecimal portieRamasaAniiAnteriori,
            @RequestParam(name = "hectareDetinute", required = false) BigDecimal hectareDetinute,
            @RequestParam(name = "portieRestanta", required = false) BigDecimal portieRestanta,
            @RequestParam(name = "cnpImputernicit", required = false) String cnpImputernicit,
            @RequestParam(name = "aniiRestanta", required = false) String aniiRestanta,
            Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(forestryEngineerService.updateClient(nume, prenume, cnp, dataRidicarePortie,
                dataScadentaRidicarePortie, cantitateTotala, um, imputernicit, portieAn, portieRamasaAniiAnteriori, hectareDetinute,
                portieRestanta, cnpImputernicit, aniiRestanta, Utils.getUserNameFromPrincipal(principal)));
    }

    @PostMapping(value = "/clients-from-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity importClientsFromExcel(@RequestParam(name = "file") MultipartFile file, Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(forestryEngineerService.importClientsFromExcel(file, Utils.getUserNameFromPrincipal(principal)));
    }

//    @GetMapping("/getStatement")
//    public ResponseEntity getStatement(@RequestParam(name = "seria", required = false) String seria,
//                                       @RequestParam(name = "nr", required = false) String nr,
//                                       @RequestParam(name = "expeditor", required = false) String expeditor,
//                                       @RequestParam(name = "cuiExpeditor", required = false) String cuiExpeditor,
//                                       @RequestParam(name = "sediuExpeditor", required = false) String sediuExpeditor,
//                                       @RequestParam(name = "punctDeIncarcareExpeditor", required = false) String punctDeIncarcareExpeditor,
//                                       @RequestParam(name = "docProvenienta", required = false) String docProvenienta,
//                                       @RequestParam(name = "codUnic", required = false) String codUnic,
//                                       @RequestParam(name = "dataOraGenerare", required = false) String dataOraGenerare,
//                                       @RequestParam(name = "destinatar", required = false) String destinatar,
//                                       @RequestParam(name = "cnpCuiDestinatar", required = false) String cnpCuiDestinatar,
//                                       @RequestParam(name = "sediuDestinatar", required = false) String sediuDestinatar,
//                                       @RequestParam(name = "punctDeDescarcareDestinatar", required = false) String punctDeDescarcareDestinatar,
//                                       @RequestParam(name = "indicativDestinatar", required = false) String indicativDestinatar,
//                                       @RequestParam(name = "transportIntrerupt", required = false) Boolean transportIntrerupt,
//                                       @RequestParam(name = "cauzaIntrerupere", required = false) String cauzaIntrerupere,
//                                       @RequestParam(name = "perioadaIntrerupere", required = false) String perioadaIntrerupere,
//                                       @RequestParam(name = "viza", required = false) String viza,
//                                       @RequestParam(name = "vizaPostPolitie", required = false) String vizaPostPolitie,
//                                       @RequestParam(name = "nrCrt", required = false) BigDecimal nrCrt,
//                                       @RequestParam(name = "nrBuc", required = false) BigDecimal nrBuc,
//                                       @RequestParam(name = "sortiment", required = false) String sortiment,
//                                       @RequestParam(name = "lungime", required = false) BigDecimal lungime,
//                                       @RequestParam(name = "diametru", required = false) BigDecimal diametru,
//                                       @RequestParam(name = "volum", required = false) BigDecimal volum,
//                                       @RequestParam(name = "rasinoase", required = false) BigDecimal rasinoase,
//                                       @RequestParam(name = "fag", required = false) BigDecimal fag,
//                                       @RequestParam(name = "frPaCiNu", required = false) BigDecimal frPaCiNu,
//                                       @RequestParam(name = "dt", required = false) BigDecimal dt,
//                                       @RequestParam(name = "dm", required = false) BigDecimal dm,
//                                       @RequestParam(name = "altele", required = false) String altele,
//                                       @RequestParam(name = "alteleCantitate", required = false) BigDecimal alteleCantitate,
//                                       @RequestParam(name = "totalGeneral", required = false) BigDecimal totalGeneral,
//                                       @RequestParam(name = "dataOraPlecareTransport", required = false) String dataOraPlecareTransport,
//                                       @RequestParam(name = "codOffline", required = false) String codOffline,
//                                       @RequestParam(name = "semnatura1", required = false) String semnatura1,
//                                       @RequestParam(name = "semnatura2", required = false) String semnatura2,
//                                       @RequestParam(name = "dataOraSosireTransport", required = false) String dataOraSosireTransport,
//                                       @RequestParam(name = "emitent", required = false) String emitent,
//                                       @RequestParam(name = "numePrenumeImputernicit", required = false) String numePrenumeImputernicit,
//                                       @RequestParam(name = "semnaturaImputernicit", required = false) String semnaturaImputernicit,
//                                       @RequestParam(name = "mijlocDeTransport", required = false) String mijlocDeTransport,
//                                       @RequestParam(name = "numeSiPrenumeConducator", required = false) String numeSiPrenumeConducator,
//                                       @RequestParam(name = "cnp", required = false) String cnp,
//                                       @RequestParam(name = "semnaturaConducator", required = false) String semnaturaConducator,
//                                       @RequestParam(name = "dateIdentificarePermisVamal", required = false) String dateIdentificarePermisVamal,
//                                       @RequestParam(name = "numePrenumeReceptioner", required = false) String numePrenumeReceptioner,
//                                       @RequestParam(name = "semnaturaReceptioner", required = false) String semnaturaReceptioner) {
//
//        byte[] statementInBytes = forestryEngineerService.generatePdfData(seria,nr,expeditor,cuiExpeditor,sediuExpeditor,punctDeIncarcareExpeditor,docProvenienta,codUnic,dataOraGenerare,destinatar,cnpCuiDestinatar,sediuDestinatar,punctDeDescarcareDestinatar,
//                indicativDestinatar,transportIntrerupt,cauzaIntrerupere,perioadaIntrerupere,viza,vizaPostPolitie,nrCrt,nrBuc,sortiment,lungime,diametru,volum,rasinoase,
//                fag,frPaCiNu,dt,dm,altele,alteleCantitate,totalGeneral,dataOraPlecareTransport,codOffline,semnatura1,semnatura2,dataOraSosireTransport,emitent,numePrenumeImputernicit,
//                semnaturaImputernicit,mijlocDeTransport,numeSiPrenumeConducator,cnp,semnaturaConducator,dateIdentificarePermisVamal,numePrenumeReceptioner,semnaturaReceptioner);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentDispositionFormData("file", "Extras_.pdf");
//        return new ResponseEntity<>(statementInBytes, headers, HttpStatus.OK);
//    }

}