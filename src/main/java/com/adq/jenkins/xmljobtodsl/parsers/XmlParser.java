package com.adq.jenkins.xmljobtodsl.parsers;

import org.w3c.dom.*;
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
    private String jobName;
    private String promotedBuildsXml;

    public XmlParser(String jobName, String xml) {
        this.jobName = jobName;
        this.xml = xml;
        prepareXml();
    }

    public XmlParser(String jobName, String xml, String promotedBuildsXml) {
        this.jobName = jobName;
        this.xml = xml;
        this.promotedBuildsXml = promotedBuildsXml;
        prepareXml();
        preparePromotedBuilds();
    }

    private String prepareXml() {
        return this.xml = this.xml.replaceAll(">%n", "")
                .replaceAll("\\s*<", "<")
                .replaceAll("&#xd;", String.format("%n"));
    }

    private String preparePromotedBuilds(){
        return this.promotedBuildsXml = this.promotedBuildsXml.replaceAll(">%n", "")
                .replaceAll("\\s*<", "<")
                .replaceAll("&#xd;", String.format("%n"));
    }

    public JobDescriptor parse() throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        InputSource is = new InputSource(new StringReader(xml));
        Document doc = docBuilder.parse(is);
        doc.getDocumentElement().normalize();

        InputSource promotedBuildsInputSource = new InputSource(new StringReader(promotedBuildsXml));
        Document promotedBuildsDoc = docBuilder.parse(promotedBuildsInputSource);
        promotedBuildsDoc.getDocumentElement().normalize();

        List<PropertyDescriptor> properties = new ArrayList<>();

        properties.addAll(getChildNodes(null, doc.getChildNodes(), promotedBuildsDoc.getChildNodes()));

        return new JobDescriptor(jobName, properties);
    }

    public List<PropertyDescriptor> getChildNodes(PropertyDescriptor parent, NodeList childNodes) {
        List<PropertyDescriptor> properties = new ArrayList<>();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String name = node.getNodeName();

            Map<String, String> attributes = null;
            if (node.hasAttributes()) {
                attributes = getAttributes(node.getAttributes());
            }

            if (!node.hasChildNodes()) {
                PropertyDescriptor descriptor = new PropertyDescriptor(name, parent, attributes);
                properties.add(descriptor);
                continue;
            }

            Node firstChild = node.getFirstChild();

            if (firstChild.getNodeType() == Node.TEXT_NODE && !((Text) firstChild).isElementContentWhitespace()) {
                String value = node.getFirstChild().getNodeValue();
                value = value.replaceAll("\n", String.format("%n"));
                PropertyDescriptor descriptor = new PropertyDescriptor(name, parent, value, attributes);
                properties.add(descriptor);
                continue;
            }

            List<PropertyDescriptor> childProperties = new ArrayList<>();
            PropertyDescriptor descriptor = new PropertyDescriptor(name, parent, childProperties, attributes);
            if (firstChild.getNodeType() == Node.ELEMENT_NODE) {
                childProperties.addAll(getChildNodes(descriptor, node.getChildNodes()));
            }
            properties.add(descriptor);
        }
        return properties;
    }

    public List<PropertyDescriptor> getChildNodes(PropertyDescriptor parent, NodeList childNodes, NodeList promotedBuildsChildNodes) {
        List<PropertyDescriptor> properties = new ArrayList<>();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String name = node.getNodeName();

            Map<String, String> attributes = null;
            if (node.hasAttributes()) {
                attributes = getAttributes(node.getAttributes());
            }

            if (!node.hasChildNodes()) {
                PropertyDescriptor descriptor = new PropertyDescriptor(name, parent, attributes);
                properties.add(descriptor);
                continue;
            }

            Node firstChild = node.getFirstChild();

            if (firstChild.getNodeType() == Node.TEXT_NODE && !((Text) firstChild).isElementContentWhitespace()) {
                String value = node.getFirstChild().getNodeValue();
                value = value.replaceAll("\n", String.format("%n"));
                PropertyDescriptor descriptor = new PropertyDescriptor(name, parent, value, attributes);
                properties.add(descriptor);
                continue;
            }

            List<PropertyDescriptor> childProperties = new ArrayList<>();
            PropertyDescriptor descriptor = new PropertyDescriptor(name, parent, childProperties, attributes);
            if(name.equals("hudson.plugins.promoted__builds.JobPropertyImpl")){
                childProperties.addAll(getChildNodes(descriptor, promotedBuildsChildNodes));
            }

            if (firstChild.getNodeType() == Node.ELEMENT_NODE) {
                childProperties.addAll(getChildNodes(descriptor, node.getChildNodes(), promotedBuildsChildNodes));
            }
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