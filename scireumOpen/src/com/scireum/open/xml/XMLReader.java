package com.scireum.open.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLReader extends DefaultHandler {

    private boolean isTextNode = false;
    private StringBuilder textNode = new StringBuilder();
    private Map<String, NodeHandler> handlers = new TreeMap<String, NodeHandler>();
    private List<SAX2DOMHandler> activeHandlers = new ArrayList<SAX2DOMHandler>();

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        isTextNode = true;
        textNode.append(ch, start, length);
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        // Delegate to active handlers and deletes them if they are finished...

        if (isTextNode) {
            String data = textNode.toString();
            for (SAX2DOMHandler handler : activeHandlers) {
                handler.text(data);
            }
            textNode = new StringBuilder();
            isTextNode = false;
        }

        Iterator<SAX2DOMHandler> iter = activeHandlers.iterator();
        while (iter.hasNext()) {
            SAX2DOMHandler handler = iter.next();
            if (handler.endElement(uri, name)) {
                iter.remove();
            }
        }
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        // Delegate to active handlers...
        for (SAX2DOMHandler handler : activeHandlers) {
            handler.processingInstruction(target, data);
        }
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        // Delegate to active handlers...
        for (SAX2DOMHandler handler : activeHandlers) {
            handler.startElement(uri, name, attributes);
        }
        // Start a new handler is necessary
        try {
            // QName qualifiedName = new QName(uri, localName);
            NodeHandler handler = handlers.get(name);
            if (handler != null) {
                activeHandlers.add(new SAX2DOMHandler(handler, uri, name,
                        attributes));
            }
        } catch (ParserConfigurationException e) {
            throw new SAXException(e);
        }
    }

    public void addHandler(String name, NodeHandler handler) {

        handlers.put(name, handler);
    }

    public static StructuredNode convert(Node node) {

        return new XMLNodeImpl(node);
    }

    class UserInterruptException extends RuntimeException {

        private static final long serialVersionUID = -7454219131982518216L;
    }

    public void parse(InputStream stream) throws ParserConfigurationException, SAXException, IOException {

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            org.xml.sax.XMLReader reader = saxParser.getXMLReader();
            reader.setEntityResolver(new EntityResolver() {

                public InputSource resolveEntity(String publicId,
                        String systemId) throws SAXException, IOException {

                    URL url = new URL(systemId);
                    // Check if file is local
                    if ("file".equals(url.getProtocol())) {
                        // Check if file exists
                        File file = new File(url.getFile());
                        if (file.exists()) {
                            return new InputSource(new FileInputStream(file));
                        }
                    }
                    return null;
                }
            });
            reader.setContentHandler(this);
            reader.parse(new InputSource(stream));
        } catch (UserInterruptException e) {
        } finally {
            stream.close();
        }
    }
}
