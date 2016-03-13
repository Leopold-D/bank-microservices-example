package transactionservice.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import transactionservice.data.TransactionData;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionWrapper {
	Long transaction_id;	
	TransactionData transaction;
}
