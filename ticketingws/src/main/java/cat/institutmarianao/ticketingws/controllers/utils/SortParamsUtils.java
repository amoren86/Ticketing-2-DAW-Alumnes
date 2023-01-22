package cat.institutmarianao.ticketingws.controllers.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class SortParamsUtils {
	private SortParamsUtils() {
	}

	private static Sort.Direction getSortDirection(String direction) {
		if ("desc".equals(direction)) {
			return Sort.Direction.DESC;
		}
		return Sort.Direction.ASC;
	}

	public static List<Order> getOrders(String[] sortFields) {
		List<Order> orders = new ArrayList<>();
		if (sortFields != null) {
			if (sortFields[0].contains(",")) {
				// will sort more than 2 fields
				for (String sortField : sortFields) {
					// sortOrder=["field","direction"],["field","direction"]
					String[] sort = sortField.split(",");
					String field = sort[0];
					String direction = sort[1];
					orders.add(new Order(getSortDirection(direction), field));
				}
			} else {
				// sortOrder=["field"],["direction"]
				String field = sortFields[0];
				String direction = null;
				direction = sortFields.length > 1 ? sortFields[1] : null;
				orders.add(new Order(getSortDirection(direction), field));
			}
		}
		return orders;
	}

	public static String[] dtoToDbFields(Map<String, String> propertyMap, String[] dtoFields) {
		String[] dbFields = new String[dtoFields.length];

		int i = 0;
		for (String dtoField : dtoFields) {
			if (propertyMap.containsKey(dtoField)) {
				dbFields[i++] = propertyMap.get(dtoField);
			} else {
				dbFields[i++] = dtoField;
			}
		}
		return dbFields;
	}
}
