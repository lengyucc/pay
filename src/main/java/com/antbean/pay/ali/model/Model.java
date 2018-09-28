package com.antbean.pay.ali.model;

import java.util.HashMap;
import java.util.Map;

public abstract class Model {

	protected Map<String, Object> optionFields = new HashMap<String, Object>();

	public void addOptionField(String fieldName, String value) {
		optionFields.put(fieldName, value);
	}

	public Map<String, Object> getOptionFields() {
		return optionFields;
	}

	public boolean hasOptionField() {
		return optionFields != null && optionFields.size() > 0;
	}
}
