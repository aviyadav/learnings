package com.scireum.open.xml;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

public class XMLReaderTest {

    @Test
    public void parse_parses_text_elements_correctly() throws IOException, SAXException, ParserConfigurationException {

        XMLReader reader = new XMLReader();

        reader.addHandler("book", new NodeHandler() {
            @Override
            public void process(StructuredNode node) {

                String text = null;
                String lnr = null;
                try {
                    text = node.queryString("description/text()");
                } catch (XPathExpressionException e) {
                    fail(e.getMessage());
                }
                System.out.println(text);
                assertTrue(text.equals("Description"));
            }
        });

        reader.parse(getClass().getResourceAsStream("/books.xml"));
    }

}
