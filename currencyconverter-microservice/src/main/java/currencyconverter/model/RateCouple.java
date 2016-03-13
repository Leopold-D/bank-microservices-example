package currencyconverter.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RateCouple {
	private String from;
	private String to;
	
	  @Override
	  public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    return from.equals(((RateCouple) o).from) && to.equals(((RateCouple) o).to);
	  }

	  @Override
	  public int hashCode() {
	    return (from+to).hashCode();
	  }
}
