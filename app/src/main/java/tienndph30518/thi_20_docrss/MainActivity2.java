package tienndph30518.thi_20_docrss;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import tienndph30518.thi_20_docrss.Service.MyService;


public class MainActivity2 extends AppCompatActivity {
    private DB_XML db_xml;
    private Apdapter adapter;
    RecyclerView recyclerView;
    private ArrayList<Item> arr = new ArrayList<>() ;
//    public static final String URL="https://vnexpress.net/rss/giao-duc.rss";
    public static final String URL="https://vnexpress.net/rss/suc-khoe.rss";
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        recyclerView = findViewById(R.id.id_recycView);
//        Button btnCount = findViewById(R.id.btnCount);
//        btnSave = findViewById(R.id.btnSave);
        ConnectivityManager connectivityManager =(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo check3G = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo checkWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (checkWifi.isConnected() || check3G.isConnected()){

            db_xml = new DB_XML(MainActivity2.this);
            if(!db_xml.getAllDS().isEmpty()){

                arr = db_xml.getAllDS();

                // Sắp xếp danh sách các mục dựa trên trạng thái
                Collections.sort(arr, new Comparator<Item>() {
                    @Override
                    public int compare(Item item1, Item item2) {
                        return Integer.compare(item2.getTrangThai(), item1.getTrangThai());
                    }
                });
                LinearLayoutManager manager = new LinearLayoutManager(MainActivity2.this);
                recyclerView.setLayoutManager(manager);
                adapter = new Apdapter(MainActivity2.this, new ArrayList<>(arr));
                recyclerView.setAdapter(adapter);
            }else {

                new DownloadXMLTask().execute(URL);
            }

        } else {
            Toast.makeText(this, "Bạn Đang Ở Chế Độ Xem off like", Toast.LENGTH_SHORT).show();
            db_xml = new DB_XML(MainActivity2.this);
            arr = db_xml.getAllDS();
            Collections.sort(arr, new Comparator<Item>() {
                @Override
                public int compare(Item item1, Item item2) {
                    return Integer.compare(item2.getTrangThai(), item1.getTrangThai());
                }
            });

            LinearLayoutManager manager = new LinearLayoutManager(MainActivity2.this);
            recyclerView.setLayoutManager(manager);
            adapter = new Apdapter(MainActivity2.this,arr);
            recyclerView.setAdapter(adapter);

        }

//        btnCount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Count();
//            }
//        });
    }


    class DownloadXMLTask extends AsyncTask<String, Void, List<Item>> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity2.this);
            progressDialog.setMessage("Vui lòng chờ...");
            progressDialog.show();
        }

        @Override
        protected List<Item> doInBackground(String... strings) {
            InputStream stream = null;
            List<Item> dataResult = null;
            try {
                stream = downloadUrl(strings[0]);
                XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
                XML_Paer saxHandler = new XML_Paer();
                xmlReader.setContentHandler(saxHandler);
                xmlReader.parse(new InputSource(stream));
                dataResult = saxHandler.getList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return dataResult;
        }

        @Override
        protected void onPostExecute(List<Item> list) {
            super.onPostExecute(list);
            if (list != null && !list.isEmpty()) {

                adapter = new Apdapter(MainActivity2.this, new ArrayList<>(list));
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
                recyclerView.setAdapter(adapter);

                progressDialog.dismiss();
                db_xml = new DB_XML(MainActivity2.this);
                db_xml.delete();
                for (Item item : list) {
                    db_xml.inset(item);
                }



                Toast.makeText(MainActivity2.this, "Thêm dữ liệu thành công!", Toast.LENGTH_SHORT).show();

                //Them vao database
//                btnSave.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Save(new ArrayList<>(list));
//                    }
//                });

            }else {
                Toast.makeText(MainActivity2.this, "Không có dữ liệu!", Toast.LENGTH_SHORT).show();
            }


            }
        }


        private InputStream downloadUrl(String urlString) throws IOException {
            java.net.URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream stream = conn.getInputStream();
            return stream;
        }


        private void Save(ArrayList<Item> list){
            DB_XML xmlParserDAO = new DB_XML(MainActivity2.this);
            xmlParserDAO.delete();

            for (int i = 0; i < list.size(); i++){
                if((i+1) % 3 == 0){
                    Item itemXml = list.get(i);
                    xmlParserDAO.inset(itemXml);
                }
            }
            Toast.makeText(MainActivity2.this, "Lưu data thành công", Toast.LENGTH_SHORT).show();

            int nhac = R.raw.waiting_for_you;
            Intent intent = new Intent(MainActivity2.this, MyService.class);
            Bundle bundle = new Bundle();
            bundle.putInt("nhac", nhac);
            intent.putExtras(bundle);
            startService(intent);
            //Code here
        }
        private void Count(){
            DB_XML xmlParserDAO = new DB_XML(MainActivity2.this);
            ArrayList<Item> list = xmlParserDAO.getAllDS();
            if (list.size() != 0){
                Toast.makeText(MainActivity2.this, "Count = " + list.size(), Toast.LENGTH_SHORT).show();
            }
        }
    }
