package urlmapper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class URLElement {
	private Long Id;
	private String string;
	
	  @Override
	  public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    return string.equals(((URLElement) o).string);
	  }

	  @Override
	  public int hashCode() {
	    return string.hashCode();
	  }
}
