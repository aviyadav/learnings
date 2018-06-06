package examples;

import java.io.FileInputStream;

import javax.xml.xpath.XPathExpressionException;

import com.scireum.open.xml.NodeHandler;
import com.scireum.open.xml.StructuredNode;
import com.scireum.open.xml.XMLReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExampleXML {

    public static void main(String[] args) throws Exception {
        XMLReader r = new XMLReader();
        r.addHandler("node", (StructuredNode node) -> {
            try {
                System.out.println(node.queryString("name"));
                System.out.println(node.queryValue("price").asDouble(0d));
                if (!node.isEmpty("resources/resource[@type='test']")) {
                    System.out.println(node.queryString("resources/resource[@type='test']"));
                }
            } catch (XPathExpressionException ex) {
                Logger.getLogger(ExampleXML.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        r.parse(new FileInputStream("src/examples/test.xml"));
    }
}
