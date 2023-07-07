package kumarijaya.bitcoinhistoricprice.response;

import kumarijaya.bitcoinhistoricprice.response.coindesk.CoindeskBPIResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(toBuilder = true)
@Getter
@Setter
public class BitcoinHistoricPriceResponse {
    private String startDate;
    private String endDate;
    private String currency;
    private CoindeskBPIResponse.BitcoinPriceIndex bpi;
    private String highestPriceDate;
    private String lowestPriceDate;
}
