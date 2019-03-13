package UserItem;
import java.io.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import org.xml.sax.SAXException;
public interface IUserItems {
	void addItem(IUserItem userItem);
	void set(int index, IUserItem userItem);
	void clearItems();
	IUserItem getItem(int index);
	IUserItem removeItem(int index);
	int size();
	void readXml(File file) throws ParserConfigurationException, IOException, SAXException;
	void writeXml(File file) throws ParserConfigurationException, TransformerConfigurationException, TransformerException;
}