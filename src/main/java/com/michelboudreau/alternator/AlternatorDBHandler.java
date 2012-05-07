package com.michelboudreau.alternator;

import com.michelboudreau.alternator.models.Item;
import com.michelboudreau.alternator.parsers.DynamoDBRequestParser;
import com.michelboudreau.alternator.models.Table;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

class AlternatorDBHandler {

	private final Logger logger = LoggerFactory.getLogger(AlternatorDBHandler.class);
	private List<Table> tables = new ArrayList<Table>();

	public AlternatorDBHandler() {
		// Should we save the results
		/*ObjectMapper mapper = new ObjectMapper();
		if (new File(dbName).exists()) {
			this.models = mapper.readValue(new File(dbName), AlternatorDB.class);
		}
		mapper.writeValue(new File(dbName), models);*/
	}

	public Map<String, Object> handle(HttpServletRequest request) throws IOException, ServletException {
		DynamoDBRequestParser object = new DynamoDBRequestParser(request);

		switch (object.getType()) {
			case PUT:
				putItem(object);
				break;
			case GET:
				getItem(object);
				break;
			case QUERY:
				query(object);
				break;
			case SCAN:
				scan(object);
				break;
			case CREATE_TABLE:
				createTable(object);
				break;
			case DELETE_TABLE:
				break;
			default:
				logger.warn("The Request Type '" + object.getType() + "' does not exist.");
				break;
		}
		return null;
	}

	protected Map<String, Object> scan(DynamoDBRequestParser obj) {
		/*List<HashMap<String, Map<String, String>>> result = new ArrayList<HashMap<String, Map<String, String>>>();
		Map<String, Object> map = new HashMap<String, Object>();
		JsonNode data = obj.getData();
		try {
			String tableName = data.path("TableName").getTextValue();
			String limit = null;
			if (!data.path("limit").isNull()) {
				limit = "" + data.path("limit").getIntValue();
			}
			if (data.path("ScanFilter").getTextValue() != null) {
				if (getTable(tableName).isHasRangeKey()) {
					String comparator = data.path("ScanFilter").path("ComparisonOperator").getTextValue();
					String rangeKey = tableGetRangeKey(tableName);
					String rangeKeyType = getTable(tableName).getRangeKeyType();
					if ("BETWEEN".equals(comparator)) {
						String lowerBound = data.path("ScanFilter").path(rangeKey).path("AttributeValueList").path(0).getTextValue();
						String upperBound = data.path("ScanFilter").path(rangeKey).path("AttributeValueList").path(1).getTextValue();
						for (Item itm : getTable(tableName).getItems()) {
							if ((lowerBound.compareTo(itm.getAttributes().get(itm.getRangeKey()).get(rangeKeyType)) < 0) && (upperBound.compareTo(itm.getAttributes().get(itm.getRangeKey()).get(rangeKeyType)) > 0)) {
								result.add(itm.getAttributes());
							}
						}
					}
					if ("LT".equals(comparator)) {
						String bound = data.path("ScanFilter").path(rangeKey).path("AttributeValueList").path(0).getTextValue();
						for (Item itm : getTable(tableName).getItems()) {
							if ((bound.compareTo(itm.getAttributes().get(itm.getRangeKey()).get(rangeKeyType)) > 0)) {
								result.add(itm.getAttributes());
							}
						}
					}
					if ("LE".equals(comparator)) {
						String bound = data.path("ScanFilter").path(rangeKey).path("AttributeValueList").path(0).getTextValue();
						for (Item itm : getTable(tableName).getItems()) {
							if ((bound.compareTo(itm.getAttributes().get(itm.getRangeKey()).get(rangeKeyType)) >= 0)) {
								result.add(itm.getAttributes());
							}
						}
					}
					if ("GT".equals(comparator)) {
						String bound = data.path("ScanFilter").path(rangeKey).path("AttributeValueList").path(0).getTextValue();
						for (Item itm : getTable(tableName).getItems()) {
							if ((bound.compareTo(itm.getAttributes().get(itm.getRangeKey()).get(rangeKeyType)) < 0)) {
								result.add(itm.getAttributes());
							}
						}
					}
					if ("GE".equals(comparator)) {
						String bound = data.path("ScanFilter").path(rangeKey).path("AttributeValueList").path(0).getTextValue();
						for (Item itm : getTable(tableName).getItems()) {
							if ((bound.compareTo(itm.getAttributes().get(itm.getRangeKey()).get(rangeKeyType)) <= 0)) {
								result.add(itm.getAttributes());
							}
						}
					}
				} else {
					throw new RuntimeException("RangeKeyCondition with no rangekey on the table");
				}
			} else {
				for (Item itm : getTable(tableName).getItems()) {
					result.add(itm.getAttributes());
				}

			}
			map.put("ConsumedCapacityUnits", 1);
			map.put("Count", 0);
			map.put("ScannedCount", 1);
			map.put("Items", result);

		} catch (RuntimeException e) {
			logger.debug("table wasn't created correctly : " + e);
		}
		System.out.println(map.toString());
		return map;*/
		return null;
	}

	public Map<String, Object> query(DynamoDBRequestParser obj) {
		/*List<HashMap<String, Map<String, String>>> result = new ArrayList<HashMap<String, Map<String, String>>>();
		Map<String, Object> map = new HashMap<String, Object>();
		JsonNode data = obj.getData();
		try {
			String tableName = data.path("TableName").getTextValue();
			if (data.path("RangeKeyCondition").getTextValue() != null) {
				if (getTable(tableName).isHasRangeKey()) {
					String comparator = data.path("RangeKeyCondition").path("ComparisonOperator").getTextValue();
					String rangeKey = tableGetRangeKey(tableName);
					String rangeKeyType = getTable(tableName).getRangeKeyType();
					String hashKey = data.path("HashKeyValue").path(getTable(tableName).getHashKeyType()).getTextValue();
					if ("BETWEEN".equals(comparator)) {
						String lowerBound = data.path("RangeKeyCondition").path(rangeKey).path("AttributeValueList").path(0).getTextValue();
						String upperBound = data.path("RangeKeyCondition").path(rangeKey).path("AttributeValueList").path(1).getTextValue();
						for (Item itm : getTable(tableName).getItemsWithKey(hashKey)) {
							if ((lowerBound.compareTo(itm.getAttributes().get(itm.getRangeKey()).get(rangeKeyType)) < 0) && (upperBound.compareTo(itm.getAttributes().get(itm.getRangeKey()).get(rangeKeyType)) > 0)) {
								result.add(itm.getAttributes());
							}
						}
					}
					if ("LT".equals(comparator)) {
						String bound = data.path("RangeKeyCondition").path(rangeKey).path("AttributeValueList").path(0).getTextValue();
						for (Item itm : getTable(tableName).getItemsWithKey(hashKey)) {
							if ((bound.compareTo(itm.getAttributes().get(itm.getRangeKey()).get(rangeKeyType)) > 0)) {
								result.add(itm.getAttributes());
							}
						}
					}
					if ("LE".equals(comparator)) {
						String bound = data.path("RangeKeyCondition").path(rangeKey).path("AttributeValueList").path(0).getTextValue();
						for (Item itm : getTable(tableName).getItemsWithKey(hashKey)) {
							if ((bound.compareTo(itm.getAttributes().get(itm.getRangeKey()).get(rangeKeyType)) >= 0)) {
								result.add(itm.getAttributes());
							}
						}
					}
					if ("GT".equals(comparator)) {
						String bound = data.path("RangeKeyCondition").path(rangeKey).path("AttributeValueList").path(0).getTextValue();
						for (Item itm : getTable(tableName).getItemsWithKey(hashKey)) {
							if ((bound.compareTo(itm.getAttributes().get(itm.getRangeKey()).get(rangeKeyType)) < 0)) {
								result.add(itm.getAttributes());
							}
						}
					}
					if ("GE".equals(comparator)) {
						String bound = data.path("RangeKeyCondition").path(rangeKey).path("AttributeValueList").path(0).getTextValue();
						for (Item itm : getTable(tableName).getItemsWithKey(hashKey)) {
							if ((bound.compareTo(itm.getAttributes().get(itm.getRangeKey()).get(rangeKeyType)) <= 0)) {
								result.add(itm.getAttributes());
							}
						}
					}
				} else {
					throw new RuntimeException("RangeKeyCondition with no rangekey on the table");
				}
			} else {
				for (Item itm : getTable(tableName).getItems()) {
					result.add(itm.getAttributes());
				}

			}
			map.put("ConsumedCapacityUnits", 1);
			map.put("Count", 0);
			map.put("ScannedCount", 1);
			map.put("Items", result);

		} catch (RuntimeException e) {
			logger.debug("table wasn't created correctly : " + e);
		}
		System.out.println(map.toString());
		return map;*/
		return null;
	}

	public Boolean createTable(DynamoDBRequestParser data) {
		String hashKey = null;
		String rangeKey = null;
		String rangeKeyType = null;

/*
		hashKey = actualObj.path("KeySchema").path("HashKeyElement").path("AttributeName").getTextValue();
		String hashKeyType = actualObj.path("KeySchema").path("HashKeyElement").path("AttributeType").getTextValue();
		if (!actualObj.path("KeySchema").path("RangeKeyElement").path("AttributeName").isNull()) {
			rangeKey = actualObj.path("KeySchema").path("RangeKeyElement").path("AttributeName").getTextValue();
			rangeKeyType = actualObj.path("KeySchema").path("RangeKeyElement").path("AttributeType").getTextValue();
		}
		if (getTable(tableName) == null) {
			Table table = new Table(hashKey, rangeKey, tableName, hashKeyType, rangeKeyType);
			getTables().add(table);
		}*/

		return true;
	}

	public void putItem(DynamoDBRequestParser data) {
		/*try {
			JsonNode actualObj = data.getData();
			String tableName = actualObj.path("TableName").getTextValue();
			Iterator itr = actualObj.path("Item").getFieldNames();
			HashMap<String, Map<String, String>> attributes = new HashMap<String, Map<String, String>>();
			while (itr.hasNext()) {
				String attrName = itr.next().toString();
				Map<String, String> schema = new HashMap<String, String>();
				if (actualObj.path("Item").path(attrName).path("S").getTextValue() != null) {
					schema.put("S", actualObj.path("Item").path(attrName).path("S").getTextValue());
					attributes.put(attrName, schema);
				} else if (actualObj.path("Item").path(attrName).path("N").getTextValue() != null) {
					schema.put("N", actualObj.path("Item").path(attrName).path("N").getTextValue());
					attributes.put(attrName, schema);
				}
			}
			if (getTable(tableName) == null) {
				throw new IOException("table doesn't exist");
			}
			if (findItemByAttributes(attributes, tableName) != null) {
				getTable(tableName).removeItem(findItemByAttributes(attributes, tableName));
			}
			if (attributes != null && !attributes.isEmpty()) {
				Item item = new Item(tableName, tableGetHashKey(tableName), tableGetRangeKey(tableName), attributes);
				getTable(tableName).addItem(item);
			} else {
				throw new IOException("item empty");
			}
		} catch (IOException e) {
			logger.debug("item wasn't put correctly : " + e);
		}*/
	}

	public Map<String, Object> getItem(DynamoDBRequestParser data) {
		String key = null;
		String tableName = null;
		Map<String, Object> response = new HashMap<String, Object>();
/*
		try {
			JsonNode actualObj = data.getData();
			tableName = actualObj.path("TableName").getTextValue();
			if (!actualObj.path("Key").path("HashKeyElement").path("S").isNull()) {
				key = actualObj.path("Key").path("HashKeyElement").path("S").getTextValue();
			} else if (!actualObj.path("Key").path("HashKeyElement").path("N").isNull()) {
				key = actualObj.path("Key").path("HashKeyElement").path("N").getTextValue();
			}

			if (key == null) {
				throw new IOException("Bad request in getItem");
			}


			if (getTable(tableName) == null) {
				throw new IOException("table doesn't exist");
			}

			response.put("ConsumedCapacityUnits", 1);
			List<Item> items = findItemByKey(key, tableName);
			if (items != null) {
				for (Item itm : items) {
					response.put("Item", itm.getAttributes());
				}
			}
		} catch (IOException e) {
			logger.debug("item wasn't put correctly : " + e);
		}*/
		return response;
	}

	public Table getTable(String tableName) {
		Table result = null;
		int count = 0;
		for (Table table : getTables()) {
			if (tableName.equals(table.getName())) {
				result = table;
				count++;
			}
		}
		if (count > 1) {
			logger.debug("Error several tables with the same name");
		}
		return result;
	}

	public String tableGetHashKey(String tableName) {
		String result = null;
		for (Table table : getTables()) {
			if (tableName.equals(table.getName())) {
				result = table.getHashKey();
			}
		}
		return result;
	}

	public String tableGetRangeKey(String tableName) {
		String result = null;
		for (Table table : getTables()) {
			if (tableName.equals(table.getName())) {
				result = table.getRangeKey();
			}
		}
		return result;
	}

	public Item findItemByAttributes(HashMap<String, Map<String, String>> attr, String tableName) {
		Item result = null;
		for (Table table : getTables()) {
			if (tableName.equals(table.getName())) {
				if (table.getItems() != null) {
					for (Item itm : table.getItems()) {
						if (valuesFromAttributes(attr).equals(valuesFromAttributes(itm.getAttributes()))) {
							result = itm;
						}
					}
				}
			}
		}
		return result;
	}

	public List<Item> findItemByKey(String key, String tableName) throws IOException {
		List<Item> result = new ArrayList<Item>();
		for (Table table : getTables()) {
			if (tableName.equals(table.getName())) {
				if (table.getItems() != null) {
					for (Item itm : table.getItems()) {
						if (key != null) {
							if (!itm.getAttributes().get(table.getHashKey()).get("S").isEmpty()) {
								if (key.equals(itm.getAttributes().get(table.getHashKey()).get("S"))) {
									result.add(itm);
								}
							} else if (!itm.getAttributes().get(table.getHashKey()).get("N").isEmpty()) {
								if (key.equals(itm.getAttributes().get(table.getHashKey()).get("N"))) {
									result.add(itm);
								}
							}
						} else {
							throw new IOException("bad requests in findItemByKey");
						}
					}
				}
			}
		}
		return result;
	}


	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	public String convertor(List<Item> items) {
		String result = "[{";
		for (Item itm : items) {
		}
		return result;
	}

	public HashSet<String> valuesFromAttributes(HashMap<String, Map<String, String>> map) {
		HashSet<String> result = new HashSet<String>();
		for (Map<String, String> mp : map.values()) {
			for (String attr : mp.values()) {
				result.add(attr);
			}
		}
		return result;
	}
}