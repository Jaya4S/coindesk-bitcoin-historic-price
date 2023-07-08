package kumarijaya.bitcoinhistoricprice.controllers;

import kumarijaya.bitcoinhistoricprice.response.coindesk.CoindeskSupportedCurrenciesResponse;
import kumarijaya.bitcoinhistoricprice.services.impl.CoindeskBitcoinPriceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BitcoinPriceController {

    @Autowired
    private CoindeskBitcoinPriceServiceImpl priceAggregatorService;
    @GetMapping(path="/bitcoin/prices", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBitcoinPrices(
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate,
            @RequestParam(value = "currency", required = false, defaultValue = "USD") String currency
    ) throws IOException {
        this.validateBitcoinPriceRequest(startDate, endDate, currency);
        return this.priceAggregatorService.getHistoricBitcoinPrices(startDate, endDate, currency);
    }

    @GetMapping(path="/supported-currencies", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CoindeskSupportedCurrenciesResponse>> getBitcoinPrices() {
        return ResponseEntity.ok(this.priceAggregatorService.getSupportedCurrencies());
    }

    private void validateBitcoinPriceRequest(String startDate, String endDate, String currency) {
        LocalDate parsedStartDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate parsedEndDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if(parsedStartDate.isAfter(parsedEndDate)) {
            throw new IllegalArgumentException(
                    String.format("Input end date: %s is before input start date: %s. Please check and try again.",startDate, endDate));
        }
        if(!this.priceAggregatorService.validateForSupportedCurrencies(currency)) {
            throw new IllegalArgumentException(String.format("Provided currency {%s} in request is not supported", currency));
        }
    }
}
