package project3_knives;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.validation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class Project3_Knives {
    private static File getFile(String message){
        String fileName;
        System.out.print(message);
        BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
        try {
            fileName = buf.readLine();
        } catch (IOException ex) {
            fileName="";
        }
        
        File file = new File(fileName);
        if (!file.exists()){
            System.out.println("Wrong path, file doesn't exist");
        }
        
        return file;
    }
    
    private static List<Knife> domDocumentToList(Document doc){
        String currentText;
        
        List<Knife> returnValue = new LinkedList<>();
        NodeList knifeNodes = doc.getElementsByTagName("knife");    
        for (int i=0;i<knifeNodes.getLength();i++){ // we got several "knife" elements in document;
            
            Node knifeNode = knifeNodes.item(i);
            
            Element knifeElement = (Element) knifeNode; //casting to Element enables us to use getElementsByTagName
            String id = knifeElement.getAttribute("id");
            
            Knife currentKnife = new Knife();
            returnValue.add(currentKnife);
            
            currentKnife.setId(id);
            
            //type
            currentText = ((Element)knifeElement.getElementsByTagName("type").item(0)).getTextContent(); //we have only ONE "type" element in "knife"
            currentKnife.setType(KnifeVariant.valueOf(currentText.toUpperCase()));
            
            //handy
            currentText = ((Element)knifeElement.getElementsByTagName("handy").item(0)).getTextContent(); //and only ONE "handy" in "knife"
            if ("1".equals(currentText)) currentKnife.setHandy(HandyType.ONE_HANDED);
            else currentKnife.setHandy(HandyType.TWO_HANDED);
            
            //origin
            currentText = ((Element)knifeElement.getElementsByTagName("origin").item(0)).getTextContent(); //and "origin" is only ONE
            currentKnife.setOrigin(currentText);
            
            Element visualElement = (Element)knifeElement.getElementsByTagName("visual").item(0); // yup. Still only ONE
            
            //length
            currentText = ((Element)visualElement.getElementsByTagName("length").item(0)).getTextContent(); //here too
            currentKnife.setLength(Double.parseDouble(currentText));
            
            //width
            currentText = ((Element)visualElement.getElementsByTagName("width").item(0)).getTextContent(); //and here
            currentKnife.setWidth(Double.parseDouble(currentText));
            
            //knifeMaterial
            currentText = ((Element)visualElement.getElementsByTagName("knifeMaterial").item(0)).getTextContent(); //solitude, you see... 
            currentKnife.setKnifeMaterial(KnifeMaterialType.valueOf(currentText.toUpperCase()));
            
            //handleMaterial
            Element handleElement = (Element)visualElement.getElementsByTagName("handlerMaterial").item(0); //it's so lonely..
            currentText = handleElement.getTextContent();
            currentKnife.setHandlerMaterial(HandlerMaterialType.valueOf(currentText.toUpperCase()));
            if (currentKnife.getHandlerMaterial()==HandlerMaterialType.WOOD){
                currentKnife.setWoodMaterial(WoodMaterialType.valueOf(handleElement.getAttribute("woodMaterial").toUpperCase())); // ONE
            }
            
            //hasBloodGroove
            currentText = ((Element)visualElement.getElementsByTagName("hasBloodGroove").item(0)).getTextContent(); // just ONE
            currentKnife.setHasBloodGroove(Boolean.valueOf(currentText)); 
            
            //value
            currentText = ((Element)knifeElement.getElementsByTagName("value").item(0)).getTextContent(); //it's sad
            currentKnife.setValue(Boolean.valueOf(currentText)); 

        }
        return returnValue;
    }
    
    
    public static void main(String[] args) {
        
        File schemaFile = getFile("Enter path to XML Shema (*.xsd): ");
        File dataFile = getFile("Enter path to XML Data (*.xml): ");
        
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        
        Schema schema; 
        SAXParser parser;
        
        boolean validated = true;
        try {
            schema = sf.newSchema(schemaFile);
            parserFactory.setSchema(schema);
            //parserFactory.setValidating(true);
            parserFactory.setNamespaceAware(true);

            parser = parserFactory.newSAXParser();
            parser.parse(dataFile, new ValidationHandler());
            
        } catch (SAXException | ParserConfigurationException | IOException ex) {
            validated=false;
            Logger.getLogger(Project3_Knives.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (validated){
            
            //sax reading
            SAXHandler handler = new SAXHandler();
            try {
                parser = parserFactory.newSAXParser();
                parser.parse(dataFile, handler);
                for (Knife knife:handler.getKnivesList()){
                    System.out.println("ID: "+knife.getId());
                    System.out.println("Type: "+knife.getType());
                }
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                Logger.getLogger(Project3_Knives.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
            //dom reading
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            
            try {
                DocumentBuilder domBuilder = dbf.newDocumentBuilder();
                
                Document doc = domBuilder.parse(dataFile);
                List<Knife> knivesList = domDocumentToList(doc);
                
                for (Knife knife:knivesList){
                    System.out.println("ID: "+knife.getId());
                    System.out.println("Type: "+knife.getType());
                }
                
                
            } catch (SAXException | IOException | ParserConfigurationException ex) {
                Logger.getLogger(Project3_Knives.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

class ValidationHandler extends DefaultHandler{

    private void printInfo(SAXParseException e) throws SAXException{
        
        System.out.println("   Public ID: "+e.getPublicId());
        System.out.println("   System ID: "+e.getSystemId());
        System.out.println("   Line number: "+e.getLineNumber());
        System.out.println("   Column number: "+e.getColumnNumber());
        System.out.println("   Message: "+e.getMessage());
        throw new SAXException(e);
      }
    
    @Override
    public void error(SAXParseException e) throws SAXException {
        System.out.println("Error:");
        printInfo(e);
    }

    @Override
    public void fatalError(SAXParseException e) throws SAXException {
        System.out.println("Fatal error: ");
        printInfo(e);
    }

    @Override
    public void warning(SAXParseException e) throws SAXException {
        System.out.println("Warning");
        printInfo(e);
    }
}

class SAXHandler extends DefaultHandler{
    
    private String currentElement;
    private Attributes curAttributes;
    private List<Knife> knivesList= new ArrayList<>();
    private Knife currentKnife;
    
    public List<Knife> getKnivesList(){
        List<Knife> returnValue = new LinkedList<>();
        for (Knife knife:knivesList) returnValue.add(knife);
        return returnValue;
    }
    
    @Override
    public void startDocument() throws SAXException {
        System.out.println("Start reading document");
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("End reading document");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentElement = localName;
        curAttributes=attributes;
        if (currentElement=="knife"){
            currentKnife = new Knife();
            currentKnife.setId(curAttributes.getValue("id"));
            knivesList.add(currentKnife);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        currentElement="";
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        switch(currentElement){
            case "type": currentKnife.setType(KnifeVariant.valueOf(new String(ch, start, length).toUpperCase())); break;
            case "handy":
                String value = new String(ch, start, length);
                if ("1".equals(value)) currentKnife.setHandy(HandyType.ONE_HANDED);
                else currentKnife.setHandy(HandyType.TWO_HANDED);
                break;
            case "origin": currentKnife.setOrigin(new String(ch, start, length)); break;
            case "length": currentKnife.setLength(Double.parseDouble(new String(ch, start, length))); break;
            case "width": currentKnife.setWidth(Double.parseDouble(new String(ch, start, length))); break;
            case "knifeMaterial": currentKnife.setKnifeMaterial(KnifeMaterialType.valueOf(new String(ch, start, length).toUpperCase())); break;
            case "handlerMaterial": currentKnife.setHandlerMaterial(HandlerMaterialType.valueOf(new String(ch, start, length).toUpperCase()));
                if (currentKnife.getHandlerMaterial()==HandlerMaterialType.WOOD){
                    currentKnife.setWoodMaterial(WoodMaterialType.valueOf(curAttributes.getValue("woodMaterial").toUpperCase()));
                }
                break;
            case "hasBloodGroove": currentKnife.setHasBloodGroove(Boolean.valueOf(new String(ch, start, length))); break;
            case "value": currentKnife.setValue(Boolean.valueOf(new String(ch, start, length))); break;
            default: break;
        }
    }
}


class Knife{
    private String id;
    private KnifeVariant type;
    private HandyType handy;
    private String origin;
    private double length;
    private double width;
    private KnifeMaterialType knifeMaterial;
    private HandlerMaterialType handlerMaterial;
    private WoodMaterialType woodMaterial;
    private boolean hasBloodGroove;
    private boolean value;

    public Knife() {
        this("id0",KnifeVariant.KNIFE,HandyType.ONE_HANDED,"UA",30,10,KnifeMaterialType.STEEL,HandlerMaterialType.PLASTIC,null,false,false);
    }

    public Knife(String id, KnifeVariant type, HandyType handy, String origin, double length, double width, KnifeMaterialType knifeMaterial, HandlerMaterialType handlerMaterial, WoodMaterialType woodMaterial, boolean hasBloodGroove, boolean value) {
        this.id = id;
        this.type = type;
        this.handy = handy;
        this.origin = origin;
        this.length = length;
        this.width = width;
        this.knifeMaterial = knifeMaterial;
        this.handlerMaterial = handlerMaterial;
        this.woodMaterial = woodMaterial;
        this.hasBloodGroove = hasBloodGroove;
        this.value = value;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the type
     */
    public KnifeVariant getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(KnifeVariant type) {
        this.type = type;
    }

    /**
     * @return the handy
     */
    public HandyType getHandy() {
        return handy;
    }

    /**
     * @param handy the handy to set
     */
    public void setHandy(HandyType handy) {
        this.handy = handy;
    }

    /**
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * @return the length
     */
    public double getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(double length) {
        this.length = length;
    }

    /**
     * @return the width
     */
    public double getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * @return the knifeMaterial
     */
    public KnifeMaterialType getKnifeMaterial() {
        return knifeMaterial;
    }

    /**
     * @param knifeMaterial the knifeMaterial to set
     */
    public void setKnifeMaterial(KnifeMaterialType knifeMaterial) {
        this.knifeMaterial = knifeMaterial;
    }

    /**
     * @return the handlerMaterial
     */
    public HandlerMaterialType getHandlerMaterial() {
        return handlerMaterial;
    }

    /**
     * @param handlerMaterial the handlerMaterial to set
     */
    public void setHandlerMaterial(HandlerMaterialType handlerMaterial) {
        this.handlerMaterial = handlerMaterial;
    }

    /**
     * @return the woodMaterial
     */
    public WoodMaterialType getWoodMaterial() {
        return woodMaterial;
    }

    /**
     * @param woodMaterial the woodMaterial to set
     */
    public void setWoodMaterial(WoodMaterialType woodMaterial) {
        this.woodMaterial = woodMaterial;
    }

    /**
     * @return the hasBloodGroove
     */
    public boolean isHasBloodGroove() {
        return hasBloodGroove;
    }

    /**
     * @param hasBloodGroove the hasBloodGroove to set
     */
    public void setHasBloodGroove(boolean hasBloodGroove) {
        this.hasBloodGroove = hasBloodGroove;
    }

    /**
     * @return the value
     */
    public boolean isValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(boolean value) {
        this.value = value;
    }
    
    
}

enum KnifeVariant{
    SABER,
    KNIFE,
    MESSER,
    MACHETE,
    AXE;
}

enum HandyType{
    ONE_HANDED,
    TWO_HANDED;
}

enum WoodMaterialType{
    OAK,
    FIR,
    ASH,
    POPLAR;
}
    
enum KnifeMaterialType{
    STEEL,
    COPPER,
    IRON;
}
    
enum HandlerMaterialType{
    WOOD,
    PLASTIC,
    METAL;
}