package currencyconverter.data;

import currencyconverter.model.URLElemCouple;
import currencyconverter.model.RateValue;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultData {
	Double result;
	URLElemCouple rateCouple;
	RateValue rateValue;
}
