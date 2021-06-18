 package zenfileprintserver;
 
 import java.io.FileOutputStream;
 import java.io.OutputStream;
 import java.util.HashMap;
 import javax.xml.stream.XMLEventFactory;
 import javax.xml.stream.XMLEventWriter;
 import javax.xml.stream.XMLOutputFactory;
 import javax.xml.stream.XMLStreamException;
 import javax.xml.stream.events.Attribute;
 import javax.xml.stream.events.Characters;
 import javax.xml.stream.events.EndDocument;
 import javax.xml.stream.events.EndElement;
 import javax.xml.stream.events.StartDocument;
 import javax.xml.stream.events.StartElement;
 import javax.xml.stream.events.XMLEvent;
 
 public class XmlFileWriter
 {
   private String outputFile;
   HashMap<String, PrinterMap> data;
   static final String PRINTER = "printer";
   static final String KEY = "key";
   static final String FILEPATH = "filePath";
   static final String ITEM = "item";
   
   public void setFile(String outputFile, HashMap<String, PrinterMap> data) {
     this.outputFile = outputFile;
     this.data = data;
   }
 
   
   public void saveConfig() throws Exception {
     XMLOutputFactory xmlOutFactory = XMLOutputFactory.newInstance();
     OutputStream outputStream = new FileOutputStream(this.outputFile);
     XMLEventWriter eventWriter = xmlOutFactory.createXMLEventWriter(outputStream);
     
     XMLEventFactory eventFactory = XMLEventFactory.newInstance();
     XMLEvent end = createNewLine(eventFactory);
     XMLEvent tab = createTab(eventFactory);
 
     
     StartDocument startDocument = eventFactory.createStartDocument();
     EndDocument endDocument = eventFactory.createEndDocument();
     eventWriter.add(startDocument);
 
     
     eventWriter.add(end);
     StartElement configStartElement = eventFactory.createStartElement("", "", "config");
     eventWriter.add(configStartElement);
     eventWriter.add(end);
 
     
     for (String key : this.data.keySet()) {
       PrinterMap km = this.data.get(key);
       eventWriter.add(tab);
       StartElement itemStartElement = eventFactory.createStartElement("", "", "item");
       eventWriter.add(itemStartElement);
       Attribute itemDateAttribute = eventFactory.createAttribute("key", key);
       eventWriter.add(itemDateAttribute);
       eventWriter.add(end);
       
       eventWriter.add(tab);
       eventWriter.add(tab);
       createItemNode(eventFactory, eventWriter, "printer", km.getPrinter());
       eventWriter.add(tab);
       eventWriter.add(tab);
       createItemNode(eventFactory, eventWriter, "filePath", km.getFilePath());
       
       eventWriter.add(tab);
       EndElement itemEndElement = eventFactory.createEndElement("", "", "item");
       eventWriter.add(itemEndElement);
       eventWriter.add(end);
     } 
     
     EndElement configEndElement = eventFactory.createEndElement("", "", "config");
     eventWriter.add(configEndElement);
     eventWriter.add(end);
     
     eventWriter.add(endDocument);
     eventWriter.close();
   }
   
   public static void createItemNode(XMLEventFactory eventFactory, XMLEventWriter eventWriter, String elementName, String value) throws XMLStreamException {
     XMLEvent end = eventFactory.createDTD("\n");
     StartElement startElement = eventFactory.createStartElement("", "", elementName);
     eventWriter.add(startElement);
     Characters characters = eventFactory.createCharacters(value);
     eventWriter.add(characters);
     EndElement endElement = eventFactory.createEndElement("", "", elementName);
     eventWriter.add(endElement);
     eventWriter.add(end);
   }
   
   public static XMLEvent createTab(XMLEventFactory eventFactory) {
     return eventFactory.createDTD("\t");
   }
   
   public static XMLEvent createNewLine(XMLEventFactory eventFactory) {
     return eventFactory.createDTD("\n");
   }
 }
