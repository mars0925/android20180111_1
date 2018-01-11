package tw.com.pcschool.dd2018011004;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    Myadapte adapter;

    //MyHandler dataHandler;改在這裡宣告才能被onItemClick裡面抓到
    MyHandler dataHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView)findViewById(R.id.listView);
        //listview設OnItemClickListener 當點選時送出intente給webactivity那一頁

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(MainActivity.this,webviewActivity.class);
                it.putExtra("link",dataHandler.newsItems.get(i).link);
                startActivity(it);
            }
        });
    }

    @Override
    //
    //建立menu裝自訂的menu 建立前要先建立menu資料夾以及檔案
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //按下menu item 下載網路資料
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case  R.id.menu_reload:
                new Thread(){
                @Override
                public void run() {
                    super.run();
                    String str_url = "https://www.mobile01.com/rss/news.xml";
                    URL url = null;
                    try {
                        url = new URL(str_url);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.connect();
                        InputStream inputStream = conn.getInputStream();
                        InputStreamReader isr = new InputStreamReader(inputStream);
                        BufferedReader br = new BufferedReader(isr);
                        StringBuilder sb = new StringBuilder();
                        String str;

                        while ((str = br.readLine()) != null)
                        {
                            sb.append(str);
                        }
                        String str1 = sb.toString();
                        Log.d("NET", str1);
                        dataHandler = new MyHandler();
                        // SAX 來解析 XML 格式文件
                    /*
                   在解析之前我們還必須定義一個類別或 Handler (DataHandler).
                    用來當作 XMLReader 的回呼函式. 在回呼函式中每當 Parser 解析完一個 Document
                     或 Element 便會透過對應的回呼函式 ( startElement() , endElement() etc )
                     通知我們進行處理, 這時我們便可以取出有興趣的 Element
                     並過濾掉不需要的 Element. 而該類別或 Handler 需繼承 DefaultHandler 類別.
                     */
                        //* 產生SAXParser物件 */
                        SAXParserFactory spf = SAXParserFactory.newInstance();

                        SAXParser sp = spf.newSAXParser();
                    /* 產生XMLReader物件 */
                        XMLReader xr = sp.getXMLReader();
                     /* 設定自定義的MyHandler給XMLReader */
                        xr.setContentHandler(dataHandler);
                     /* 解析XML */
                        xr.parse(new InputSource(new StringReader(str1)));

                        br.close();
                        isr.close();
                        inputStream.close();
                        //這裡面都是在 Thread()裡面跑 如果要再讓主執行緒的UI動要用runOnUiThread()
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                String data[] = new String[dataHandler.newsItems.size()];
                                //用迴圈把裝Mobile01news物件的ArrayList的資料叫出來
                                for(int i = 0; i<data.length;i++)
                                {
                                    data[i]=dataHandler.newsItems.get(i).title;
                                }


                                adapter =  new Myadapte(MainActivity.this,dataHandler.newsItems);

                                lv.setAdapter(adapter);
                            }
                        });
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    }

                }
            }.start();
             break;
        }
        return super.onOptionsItemSelected(item);
    }


}
