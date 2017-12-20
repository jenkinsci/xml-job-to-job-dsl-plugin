package com.adq.jenkins.xmljobtodsl;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlParser {

    private String xml;

    public XmlParser(String xml) {
        this.xml = xml;
        prepareXml();
    }

    public String prepareXml() {
        return this.xml = this.xml.replaceAll("\n", "").replaceAll("\\s*<", "<");
    }

    public JobDescriptor parse() throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document doc = docBuilder.parse(is);

        List<PropertyDescriptor> properties = new ArrayList<>();

        properties.addAll(getChildNodes(doc.getChildNodes()));

        return new JobDescriptor(properties);
    }

    public List<PropertyDescriptor> getChildNodes(NodeList childNodes) {
        List<PropertyDescriptor> properties = new ArrayList<>();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            String name = child.getNodeName();

            Map<String, String> attributes = null;
            if (child.hasAttributes()) {
                attributes = getAttributes(child.getAttributes());
            }

            List<PropertyDescriptor> childProperties = new ArrayList<>();
            if (child.hasChildNodes()) {
                childProperties.addAll(getChildNodes(child.getChildNodes()));
            } else {
                String value = child.getNodeValue();
                PropertyDescriptor descriptor = new PropertyDescriptor(name, value, attributes);
                properties.add(descriptor);
                continue;
            }

            PropertyDescriptor descriptor = new PropertyDescriptor(name, childProperties, attributes);
            properties.add(descriptor);
        }
        return properties;
    }

    public Map<String, String> getAttributes(NamedNodeMap attributes) {
        Map<String, String> attributesMap = new HashMap<>();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            attributesMap.put(item.getNodeName(), attributes.item(i).getNodeValue());
        }
        return attributesMap;
    }
}