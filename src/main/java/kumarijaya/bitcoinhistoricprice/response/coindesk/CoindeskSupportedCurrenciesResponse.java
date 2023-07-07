package kumarijaya.bitcoinhistoricprice.response.coindesk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoindeskSupportedCurrenciesResponse {

    private String currency;
}
