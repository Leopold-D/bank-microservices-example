package transactionservice.data;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionData {
	@NotNull
	Double amount;
	@NotNull
	String type;
	@Nullable
	Long parent_id;
}
