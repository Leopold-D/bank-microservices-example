package currencyconverter.data;

import currencyconverter.model.RateCouple;
import currencyconverter.model.RateValue;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultData {
	Double result;
	RateCouple rateCouple;
	RateValue rateValue;
}
