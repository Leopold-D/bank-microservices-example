package transactionservice.service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import transactionservice.data.TransactionData;
import transactionservice.data.TransactionSum;
import transactionservice.model.TransactionWrapper;

@RestController
public class TransactionServiceCompositeService {

	private static final Logger LOG = LoggerFactory.getLogger(TransactionServiceCompositeService.class);
	
	@Autowired
	Util util;

	Map<Long, TransactionWrapper> transactions = new HashMap<Long, TransactionWrapper>();
	Multimap<String, Long> types = ArrayListMultimap.create();
	
	@RequestMapping("/")
	public String mHello() {
		return "{\"timestamp\":\"" + new Date() + "\",\"content\":\"Hello from Transaction Service\"}";
	}

	@RequestMapping(value = "/transaction/{transaction_id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<Object> mAddTransaction(@PathVariable("transaction_id") Long transaction_id,
			@RequestBody TransactionData body) {
		try {
			if (!transactions.containsKey(transaction_id) && body.getAmount() > 0) {
				transactions.put(transaction_id, (new TransactionWrapper(transaction_id, body)));
				types.put(body.getType(), transaction_id);
			} else {
				return util.createResponse(null, HttpStatus.BAD_REQUEST);
			}
			return util.createResponse(HttpStatus.OK.toString(), HttpStatus.OK);
		} catch (RuntimeException e) {
			return util.createResponse(null, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	/**
	 * Complexity : Usually O(1), max O(log n)
	 * @param transaction_id
	 * @return
	 */
	@RequestMapping(value = "/transaction/{transaction_id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<TransactionData> mGetTransaction(@PathVariable("transaction_id") Long transaction_id) {
		try {
			return util.createResponse(transactions.get(transaction_id).getTransaction(), HttpStatus.ACCEPTED);
		} catch (Exception e) {
			return util.createResponse(null, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	/**
	 * Complexity : Usually O(1), max O(log n)
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/types/{type}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<Collection<Long>> mGetTransactionsForType(@PathVariable("type") String type) {
		return util.createResponse(types.get(type), HttpStatus.OK);
	}

	/**
	 * Complexity : ~ Linear
	 * @param transaction_id
	 * @return
	 */
	@RequestMapping(value = "/sum/{transaction_id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<TransactionSum> mGetTransactionSum(@PathVariable("transaction_id") Long transaction_id) {

		try {
			return util.createResponse((new TransactionSum(sumOfTransactions(transaction_id, 0d))), HttpStatus.OK);
		} catch (Exception e) {
			return util.createResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private Double sumOfTransactions(Long transaction_id, Double sum) {
		if (transactions.get(transaction_id).getTransaction().getParent_id() == null)
			return sum + transactions.get(transaction_id).getTransaction().getAmount();
		else
			return sum + sumOfTransactions(transactions.get(transaction_id).getTransaction().getParent_id(),
					transactions.get(transaction_id).getTransaction().getAmount());
	}
}
