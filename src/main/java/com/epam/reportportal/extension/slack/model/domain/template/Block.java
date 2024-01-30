package com.epam.reportportal.extension.slack.model.domain.template;

import com.epam.reportportal.extension.slack.model.domain.template.text.Element;
import com.epam.reportportal.extension.slack.model.domain.template.text.Field;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Block {

	private String type;
	private List<Field> fields;
	private List<Element> elements;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public List<Element> getElements() {
		return elements;
	}

	public void setElements(List<Element> elements) {
		this.elements = elements;
	}
}
