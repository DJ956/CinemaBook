package Cinema;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import UserItem.*;
public class CinemaBook implements IUserItems {
	public static final String TAG_NAME = "CinemaBook";
	private Converter converter = Converter.getInstance();
	private LinkedHashMap<Integer,IUserItem> map;
	private int index = 0;
	
	public int getIndex(){
		return this.index;
	}
	
	public void addItem(IUserItem item) {
		index++;
		map.put(index, item);
	}

	public void clearItems() {
		map.clear();
		this.index = 0;
	}

	public IUserItem getItem(int id) {
		return map.get(id);
	}
	
	public int getId(IUserItem item){
		List<Object> itemList = new LinkedList<>(map.values());
		int machIndex = -1;
		for(int index = 0; index < itemList.size(); index++){
			if(itemList.get(index).equals(item)){
				machIndex = index;
			}
		}
		LinkedList<Integer> idList = new LinkedList<>(map.keySet());
		return idList.get(machIndex);
	}
	
	public IUserItem indexOf(int index){
		List<Integer> id_list = new LinkedList<>(map.keySet());
		int id = id_list.get(index);
		return map.get(id);
	}

	public IUserItem removeItem(int id) {
		return map.remove(id);
	}

	public void set(int id, IUserItem item) {
		map.put(id, item);
	}
	
	public int size() {
		return map.size();
	}
	
	public void readXml(File file) throws IOException, ParserConfigurationException, SAXException { //xmlに書き込まれたデータをCinemaBookに読み込ませる
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(file);
		
		Element rootElement = document.getDocumentElement();
		NodeList rootList = rootElement.getChildNodes();
		
		for(int i = 0; i < rootList.getLength(); i++) {
			Node node = (Node)rootList.item(i);
			if(node.getNodeName().equals(TAG_NAME) && node.getNodeType() == Node.ELEMENT_NODE) {
				NodeList cinemaNodeList = node.getChildNodes();
				IUserItem cinemaItem = new CinemaItem(CinemaItem.COLUMNNAME_ARRAY);
				
				for(int index = 0; index < cinemaNodeList.getLength(); index++)	{
					Node cinemaNode = cinemaNodeList.item(index);
					if(cinemaNode.getNodeType() == Node.ELEMENT_NODE) {
						String nodeName = cinemaNode.getNodeName();
						if(nodeName.equals(CinemaItem.REVIEW)) {
							cinemaItem.putColumnValue(CinemaItem.REVIEW, Integer.valueOf(cinemaNode.getTextContent()));
						}
						else if(nodeName.equals(CinemaItem.IMAGE)){
							String imageString = cinemaNode.getTextContent();
							BufferedImage image = converter.convertStringToImage(imageString);
							cinemaItem.putColumnValue(CinemaItem.IMAGE, image);
						}
						else {
							cinemaItem.putColumnValue(nodeName, cinemaNode.getTextContent());
						}
					}
				}
				addItem(cinemaItem);
			}
		}
	}
	
	public void writeXml(File file) throws ParserConfigurationException, TransformerConfigurationException, TransformerException { //fileにCinemaBookクラスのデータをxmlで書き込む
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.getDOMImplementation().createDocument("", "Root", null);
		Element rootElement = document.getDocumentElement();
		
		for(int i = 0; i < size(); i++)	{
			IUserItem cinemaItem = (IUserItem)indexOf(i);
			
			Element cinemaElement = document.createElement(TAG_NAME);
			for(int index = 0; index < cinemaItem.getColumnIndex(); index++) {
				Element element = document.createElement(cinemaItem.getColumnName(index));
				if(element.getTagName().equals(CinemaItem.IMAGE)){
					String imageString = converter.converetImageToString((BufferedImage)cinemaItem.getColumnValue(CinemaItem.IMAGE));
					element.appendChild(document.createTextNode(imageString));
				}
				else{
					element.appendChild(document.createTextNode(cinemaItem.getColumnValue(index).toString()));
				}
				
				cinemaElement.appendChild(element);
			}
			rootElement.appendChild(cinemaElement);
		}
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty("encoding", "utf-8");
		transformer.setOutputProperty("indent", "yes");
		
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(file);
		
		transformer.transform(source, result);
	}
	
	public CinemaBook(){
		this.map = new LinkedHashMap<>();
	}
}