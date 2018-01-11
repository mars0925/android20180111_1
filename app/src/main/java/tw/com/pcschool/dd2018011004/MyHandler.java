package tw.com.pcschool.dd2018011004;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Student on 2018/1/10.
 */

public class MyHandler extends DefaultHandler {
    //用布林陣列定位item 以及title
    boolean isTitle = false;
    boolean isItem = false;
    boolean islink = false;
    boolean isDescription = false;
    StringBuilder linkSB = new StringBuilder();
    StringBuilder descSB = new StringBuilder();
    //用Arraylsit放抓下來的標題
   //public ArrayList<String> titles = new ArrayList<>();
   // public ArrayList<String> links = new ArrayList<>();
    //使用ArrayList來放每一筆新聞的物件內容
    public ArrayList<Mobile01news> newsItems = new ArrayList<>();
    Mobile01news item;
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        //Log.d("NET", qName);
        //如果qName是title以及item開頭就變成true
        //java7.以後 switch 不只可以放整數 也可以放字串
        switch(qName)
        {
            case "title":
                isTitle = true;
                break;
            case "item":
                isItem = true;
                item = new Mobile01news();
                break;
            case "link":
                islink = true;
                break;
            case "description":
                isDescription = true;
                descSB = new StringBuilder();
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        //如果qName是title以及item結尾變成false
        switch(qName)
        {
            case "title":
                isTitle = false;
                break;
            case "item":
                isItem = false;
                Log.d("NET", "When add item, imgurl:" + item.imgurl);
                newsItems.add(item);
                break;
            case "link":
                islink = false;
                if (isItem)
                {
                    item.link = linkSB.toString();
                    linkSB = new StringBuilder();
                }
                break;
            case "description":
                isDescription = false;
                if (isItem)
                {
                    String str = descSB.toString();
                    Log.d("NET", "end Element str:" + str);
                    //正則表達式
                    Pattern pattern = Pattern.compile("https.*jpg");
                    Matcher m = pattern.matcher(str);
                    String imgurl = "";
                    if (m.find())
                    {
                        imgurl = m.group(0);
                    }
                    //找出<img.*/>的字串然後取代成空字串
                    str = str.replaceAll("<img.*/>", "");
                    item.description = str;
                    item.imgurl = imgurl;
                    Log.d("NET", "In Handler: Item.desc:" + item.description);
                    Log.d("NET", "In Handler: Item.imgurl:" + item.imgurl);
                }

                break;
        }
    }
    /* 取得Element的開頭結尾中間夾的字串 */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        //兩個布林陣列都要同時為true才放到ArrayList中
        if (isTitle && isItem)
        {
            Log.d("NET", new String(ch, start, length));
            item.title = new String(ch, start, length);
        }
        if (islink && isItem)
        {
            Log.d("NET", new String(ch, start, length));
            linkSB.append(new String(ch, start, length));
        }
        if (isDescription && isItem)
        {
            descSB.append(new String(ch, start, length));
        }

    }
}
