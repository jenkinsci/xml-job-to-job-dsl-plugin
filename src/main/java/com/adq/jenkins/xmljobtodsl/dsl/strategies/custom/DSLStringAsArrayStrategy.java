package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLArrayStrategy;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.IValueStrategy;

public class DSLStringAsArrayStrategy extends DSLArrayStrategy implements IValueStrategy {

	public DSLStringAsArrayStrategy(int tabs, PropertyDescriptor descriptor, String name) {
		super(tabs, descriptor, name);
	}

	@Override
	public String toDSL() {
		return String.format(getSyntax("syntax.array"), toStringArray(((PropertyDescriptor) getDescriptor()).getValue()));
	}

	private String toStringArray(String text) {
		String[] lines = text.split("(\r\n)|(\n)");
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < lines.length; i++) {
			builder.append(lines[i].replace("=", ": \""));
			builder.append("\"");
			if (i < lines.length - 1) {
				builder.append(String.format(",%n"));
			}
		}

		return builder.toString();
	}
}
