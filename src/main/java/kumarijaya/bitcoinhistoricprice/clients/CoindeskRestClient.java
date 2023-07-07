package kumarijaya.bitcoinhistoricprice.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import kumarijaya.bitcoinhistoricprice.response.coindesk.CoindeskBPIResponse;
import kumarijaya.bitcoinhistoricprice.response.coindesk.CoindeskSupportedCurrenciesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class CoindeskRestClient {

    @Value("${coindesk-api:https://api.coindesk.com/v1/bpi}")
    private String coindeskBitcoinUrl;

    @Value("${historic-bitcoin-prices-path:/historical/close.json}")
    private String historicPriceIndexPath;

    @Value("${supported-currencies-path:/supported-currencies.json}")
    private String supportedCurrenciesPath;

    private static RestTemplate restTemplate = new RestTemplate();

    private static ObjectMapper objectMapper = new ObjectMapper();

    public CoindeskBPIResponse getHistoricBitcoinPrices(String startDate, String endDate, String currency) throws IOException {
        String API_URL = this.coindeskBitcoinUrl.concat(this.historicPriceIndexPath);
        String historicBPIUrl = String.format("%s?start=%s&end=%s&currency=%s", API_URL, startDate, endDate, currency);
        ResponseEntity<String> response =  restTemplate.getForEntity(historicBPIUrl, String.class);;
        CoindeskBPIResponse bpiResponse = objectMapper.readValue(response.getBody().getBytes(), CoindeskBPIResponse.class);
        return bpiResponse;
    }

    public List<CoindeskSupportedCurrenciesResponse> getSupportedCurrencies() throws IOException {
        String API_URL = this.coindeskBitcoinUrl.concat(this.supportedCurrenciesPath);
        ResponseEntity<String> response =  restTemplate.getForEntity(API_URL,  String.class);
        List<CoindeskSupportedCurrenciesResponse> supportedCurrencies = Arrays.asList(objectMapper.readValue(response.getBody().getBytes(),
                CoindeskSupportedCurrenciesResponse[].class));
        return supportedCurrencies;
    }
}
