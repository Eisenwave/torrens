package net.grian.torrens.xml;

import net.grian.torrens.io.Deserializer;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 *     A deserializer for <b>Extensible Markup Language (.xtl)</b> files.
 * </p>
 * <p>
 *     In this particular case, the <b>DOM Parser</b> is being used which is part of the JDK.
 * </p>
 */
public class DeserializerXML implements Deserializer<Document> {
    
    @Override
    public Document fromStream(InputStream stream) throws IOException {
        Document doc;
        try {
             DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.parse(stream);
        } catch (ParserConfigurationException|SAXException ex) {
            throw new IOException(ex);
        }
        doc.getDocumentElement().normalize();
    
        return doc;
    }
    
}
