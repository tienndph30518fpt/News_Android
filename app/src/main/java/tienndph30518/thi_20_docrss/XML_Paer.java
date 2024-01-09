package tienndph30518.thi_20_docrss;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class XML_Paer extends DefaultHandler {
    private ArrayList<Item> list = new ArrayList<>();
    private Item item;
    private String temp;
    private Boolean isCheck = false;

    public XML_Paer() {
    }

   public  ArrayList<Item>getList(){
        return list;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        //Phương thức này được gọi khi parser
        // tìm thấy các ký tự giữa các thẻ mở và đóng của một phần tử
        if (isCheck){
            temp = new String(ch,start,length);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        // Phương thức này được gọi khi parser tìm thấy một thẻ mở của một phần tử
    if (qName.equalsIgnoreCase("item")){
        item= new Item();
        isCheck = true;
    }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        // Phương thức này được gọi khi parser tìm thấy một thẻ đóng của một phần tử.
        if (qName.equalsIgnoreCase("item")){
            list.add(item);
            item = new Item();
            isCheck = false;
        } else if (isCheck) {
            if (qName.equalsIgnoreCase("title")){
                item.setTitle(temp);
            } else if (qName.equalsIgnoreCase("link")) {
                item.setLink(temp);
            } else if (qName.equalsIgnoreCase("pubDate")) {
                item.setPubData(temp);
            } else if (qName.equalsIgnoreCase("description")) {
                Document doc =  Jsoup.parse(temp);
                Elements elements = doc.select("img");
                for (Element img: elements){
                    String src = img.attr("src");
                    item.setImgAvata(src);
                }
                String nd = temp.replaceAll("<[^>]*>","");
                item.setDescription(nd);
            }

        }


    }
}
