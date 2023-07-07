package kumarijaya.bitcoinhistoricprice.response.coindesk;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoindeskBPIResponse {

    @JsonProperty("bpi")
    private BitcoinPriceIndex bpi;

    public static class BitcoinPriceIndex {

        private Map<LocalDate, Double> indices = new HashMap<>();
        @JsonAnySetter
        void setIndices( String date, Double price) {
            LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            indices.put(parsedDate, price);
        }

        @JsonAnyGetter
        public Map<LocalDate, Double> getIndices() {
            return this.indices;
        }
    }


}
