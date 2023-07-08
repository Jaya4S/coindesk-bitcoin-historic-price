package kumarijaya.bitcoinhistoricprice.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import kumarijaya.bitcoinhistoricprice.clients.CoindeskRestClient;
import kumarijaya.bitcoinhistoricprice.response.BitcoinHistoricPriceResponse;
import kumarijaya.bitcoinhistoricprice.response.coindesk.CoindeskBPIResponse;
import kumarijaya.bitcoinhistoricprice.response.coindesk.CoindeskSupportedCurrenciesResponse;
import kumarijaya.bitcoinhistoricprice.services.BitcoinPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class CoindeskBitcoinPriceServiceImpl implements BitcoinPriceService {

    @Autowired
    private CoindeskRestClient coindeskRestClient;

    public ResponseEntity<BitcoinHistoricPriceResponse> getHistoricBitcoinPrices(String startDate, String endDate, String currency) throws IOException {
        BitcoinHistoricPriceResponse processedResp = null;
        try {
            CoindeskBPIResponse response = this.coindeskRestClient.getHistoricBitcoinPrices(startDate, endDate, currency);
            processedResp = this.processBitcoinHistoricPriceResp(response, startDate, endDate, currency);
        } catch (HttpStatusCodeException httpEx) {
            throw httpEx;
        } catch (IOException ioe) {
            throw ioe;
        }
        return ResponseEntity.ok(processedResp);
    }

    private BitcoinHistoricPriceResponse processBitcoinHistoricPriceResp(CoindeskBPIResponse response, String startDate, String endDate, String currency) {

        BitcoinHistoricPriceResponse bitcoinHistoricPriceResponse = BitcoinHistoricPriceResponse.builder()
                .bpi(response.getBpi())
                .startDate(startDate)
                .endDate(endDate)
                .currency(currency).build();

        if(Objects.nonNull(response.getBpi()) && !response.getBpi().getIndices().isEmpty()) {
            LocalDate highestIndexDate = response.getBpi().getIndices()
                    .entrySet()
                    .parallelStream()
                    .max(Map.Entry.comparingByValue()).get().getKey();
            LocalDate lowestIndexDate = response.getBpi().getIndices()
                    .entrySet()
                    .parallelStream()
                    .min(Map.Entry.comparingByValue()).get().getKey();

            bitcoinHistoricPriceResponse = bitcoinHistoricPriceResponse.toBuilder()
                .highestPriceDate(String.valueOf(highestIndexDate))
                    .lowestPriceDate(String.valueOf(lowestIndexDate)).build();
        }

        return bitcoinHistoricPriceResponse;
    }

    @Override
    public List<CoindeskSupportedCurrenciesResponse> getSupportedCurrencies() {
        List<CoindeskSupportedCurrenciesResponse> response = null;
        try {
            response = this.coindeskRestClient.getSupportedCurrencies();
        } catch (Exception exception) {
            log.error("Unable to fetch list of supported currencies from coindesk due to error :{}", exception.getMessage());
            log.info("Validating against static in-memory currency list");

            return this.getInMemorySupportedCurrencies();
        }
        return response;
    }

    public boolean validateForSupportedCurrencies(String currency) {
        List<CoindeskSupportedCurrenciesResponse> response = getSupportedCurrencies();
        return response.parallelStream()
                .anyMatch(curr -> curr.getCurrency().equalsIgnoreCase(currency));
    }

    private List<CoindeskSupportedCurrenciesResponse> getInMemorySupportedCurrencies() {
        try {
            CoindeskSupportedCurrenciesResponse[] currencies = new ObjectMapper().readValue(new ClassPathResource("templates/coindesk-bpi-supported-currencies.json")
                    .getFile(), CoindeskSupportedCurrenciesResponse[].class);
            return Arrays.asList(currencies);
        } catch (IOException ex) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,"Error occurred during fetching supported currencies");
        }
    }
}
