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
}
