package com.adq.jenkins.xmljobtodsl.dsl.strategies;

import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.lang.reflect.Constructor;

public class DSLStrategyFactory {

	public static final String TYPE_CONST = "CONST";
	public static final String TYPE_INNER = "INNER";
	public static final String TYPE_OBJECT = "OBJECT";
	public static final String TYPE_METHOD = "METHOD";
	public static final String TYPE_PARAMETER = "PARAMETER";
	public static final String TYPE_PROPERTY = "PROPERTY";
	public static final String TYPE_ARRAY = "ARRAY";
	public static final String TYPE_VALUE = "VALUE";
	public static final String TYPE_INNER_PARAMETER = "INNER_PARAMETER";
	public static final String TYPE_CLOSURE = "CLOSURE";
	public static final String TYPE_CONFIGURE = "CONFIGURE";

	public DSLStrategy getDSLStrategy(String type, PropertyDescriptor propertyDescriptor, String property, int tabs) {
		switch (type) {
			case TYPE_INNER:
				return new DSLInnerStrategy(tabs - 1, propertyDescriptor);
			case TYPE_CONST:
				return new DSLConstantStrategy(propertyDescriptor, property);
			case TYPE_OBJECT:
				return new DSLObjectStrategy(tabs, propertyDescriptor, property);
			case TYPE_PARAMETER:
				return new DSLParameterStrategy(propertyDescriptor);
			case TYPE_PROPERTY:
				return new DSLPropertyStrategy(tabs, propertyDescriptor, property);
			case TYPE_ARRAY:
				return new DSLArrayStrategy(propertyDescriptor);
			case TYPE_VALUE:
				return new DSLValueStrategy(propertyDescriptor);
			case TYPE_INNER_PARAMETER:
				return new DSLInnerParameterStrategy(propertyDescriptor);
			case TYPE_METHOD:
				return new DSLMethodStrategy(tabs, propertyDescriptor, property);
			case TYPE_CLOSURE:
				return new DSLClosureStrategy(tabs, propertyDescriptor, property);
			case TYPE_CONFIGURE:
				return new DSLConfigureBlockStrategy(tabs, propertyDescriptor, property);
			default:
				try {
					Class<?> clazz = Class.forName(type);
					Constructor<?> constructor = clazz.getConstructor(int.class, PropertyDescriptor.class, String.class);
					return (DSLStrategy) constructor.newInstance(tabs, propertyDescriptor, property);
				} catch (ReflectiveOperationException e) {
					e.printStackTrace();
					return null;
				}
		}
	}
}
