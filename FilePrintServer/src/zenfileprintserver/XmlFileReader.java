 package zenfileprintserver;
 
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileNotFoundException;
 import java.io.InputStream;
 import java.util.HashMap;
 import java.util.Iterator;
 import javax.xml.stream.XMLEventReader;
 import javax.xml.stream.XMLInputFactory;
 import javax.xml.stream.XMLStreamException;
 import javax.xml.stream.events.Attribute;
 import javax.xml.stream.events.EndElement;
 import javax.xml.stream.events.StartElement;
 import javax.xml.stream.events.XMLEvent;
 

 
 public class XmlFileReader
 {
   static final String KEY = "key";
   static final String PRINTER = "printer";
   static final String FILEPATH = "filePath";
   static final String ITEM = "item";
   
   public HashMap<String, PrinterMap> readConfig(String configFile) {
     HashMap<String, PrinterMap> items = new HashMap<>();
     try {
       File test = new File(configFile);
       if (!test.exists()) {
         return items;
       }
       System.out.println(configFile);
 
       
       XMLInputFactory inputFactory = XMLInputFactory.newInstance();
       
       InputStream in = new FileInputStream(configFile);
       XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
       
       PrinterMap item = null;
       String key = "";
       
       while (eventReader.hasNext()) {
         XMLEvent event = eventReader.nextEvent();
         
         if (event.isStartElement()) {
           StartElement startElement = event.asStartElement();
           
           if (startElement.getName().getLocalPart() == "item") {
             item = new PrinterMap();
             key = "";
 
             
             Iterator<Attribute> attributes = startElement.getAttributes();
             
             while (attributes.hasNext()) {
               Attribute attribute = attributes.next();
               if (attribute.getName().toString().equals("key")) {
                 key = attribute.getValue();
                 item.setKey(key);
               } 
             } 
           } 
 
           
           if (event.isStartElement() && 
             event.asStartElement().getName().getLocalPart().equals("printer")) {
             event = eventReader.nextEvent();
             try {
               item.setPrinter(event.asCharacters().getData());
             } catch (Exception ex) {
               item.setPrinter("");
               System.out.println(event.getClass());
               System.out.println(event.getClass().getName());
             } 
             
             continue;
           } 
           if (event.asStartElement().getName().getLocalPart().equals("filePath")) {
             
             event = eventReader.nextEvent();
             item.setFilePath(event.asCharacters().getData());
             
             continue;
           } 
         } 
         if (event.isEndElement()) {
           EndElement endElement = event.asEndElement();
           if (endElement.getName().getLocalPart() == "item") {
             items.put(key, item);
           }
         }
       
       } 
     } catch (FileNotFoundException e) {
       e.printStackTrace();
     } catch (XMLStreamException e) {
       e.printStackTrace();
     } 
     return items;
   }
 }