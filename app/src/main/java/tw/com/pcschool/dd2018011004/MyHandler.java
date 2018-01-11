package tw.com.pcschool.dd2018011004;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created by Student on 2018/1/10.
 */

public class MyHandler extends DefaultHandler {
    //用布林陣列定位item 以及title
    boolean isTitle = false;
    boolean isItem = false;
    boolean islink = false;
    StringBuilder linkSB = new StringBuilder();
    //用Arraylsit放抓下來的標題
    public ArrayList<String> titles = new ArrayList<>();
    public ArrayList<String> links = new ArrayList<>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        //Log.d("NET", qName);
        //如果qName是title以及item開頭就變成true

        if (qName.equals("title"))
        {
            isTitle = true;
        }
        if (qName.equals("item"))
        {
            isItem = true;
        }
        if (qName.equals("link"))
        {
            islink = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        //如果qName是title以及item結尾變成false
        if (qName.equals("title"))
        {
            isTitle = false;
        }
        if (qName.equals("item"))
        {
            isItem = false;
        }
        if (qName.equals("link"))
        {
            islink = false;
            //Mobile01 本站新聞最上面的title也有link ,所以要檢查 是Item裡面的link才要加
            if (isItem)
            {
                links.add(linkSB.toString());
                linkSB = new StringBuilder();
            }

        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        //兩個布林陣列都要同時為true才放到ArrayList中
        if (isTitle && isItem)
        {
            Log.d("NET", new String(ch, start, length));
            titles.add(new String(ch,start,length));
        }
        if (islink && isItem)
        {
            linkSB.append(new String(ch,start,length));
        }

    }
}
