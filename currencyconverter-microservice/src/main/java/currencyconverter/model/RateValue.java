package currencyconverter.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RateValue {
	private Date date;
	private Double value;
	
}
