package baratu.ki;

import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "KIBARATU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.add_supermercado:
                return true;

            case R.id.add_produto:

                try {
                    postProduct(4, "Feijao", "NovaPromo", 3.55);
                }
                catch (Exception ex)
                {}

                return true;

            case R.id.refresh_list:

                refreshListaProdutos();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static boolean postProduct(Integer id, String nome, String observacao, Double preco) throws IOException
    {
        HttpsURLConnection conn;

//        Post URL
//        https://1-dot-kibaratu-141212.appspot.com/_ah/api/produtosendpoint/v1/produtos/?id=3&nome=Macarrão&observacao=Promoção até domingo&preco=4.30

        try
        {
            URL url = new URL("https://1-dot-kibaratu-141212.appspot.com/_ah/api/produtosendpoint/v1/produtos");

            conn = (HttpsURLConnection) url.openConnection();

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.connect();

            Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("id", id.toString())
                .appendQueryParameter("nome", nome)
                .appendQueryParameter("observacao", observacao)
                .appendQueryParameter("preco", preco.toString());

            String query = builder.build().getEncodedQuery();

            Log.d(TAG, query);

            //OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            OutputStream os = conn.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");

            //Sending data
            writer.write(query);
            writer.flush();
            writer.close();

            BufferedReader br = new BufferedReader(new InputStreamReader( conn.getInputStream(),"utf-8"));

            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            br.close();

            Log.d(TAG, "Response: " + sb.toString());
        }
        catch (Exception ex)
        {
            Log.d(TAG, ex.toString());
        }
        finally
        {
            Log.d(TAG, "Finally");
            //conn.disconnect();
        }

        return true;
    }

    private void refreshListaProdutos()
    {
        //Get View
        ListView listProdutos = (ListView)findViewById(R.id.listProdutos);

        //Create list with data
        List<String> list = getListaProdutos();

        //Create adapter to populate ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        listProdutos.setAdapter(adapter);
    }

    private List<String> getListaProdutos()
    {
        List<String> list = new ArrayList<String>();

        try
        {
            JSONObject jsonObject = getJSONObjectFromURL("https://1-dot-kibaratu-141212.appspot.com/_ah/api/produtosendpoint/v1/produtos/");

            JSONArray jsonArray = jsonObject.getJSONArray("items");

            for ( int i = 0; i < jsonArray.length() ; i++)
            {
                //this object inside array you can do whatever you want
                JSONObject item = jsonArray.getJSONObject(i);

                String nome = item.getString("nome");
                Double preco = item.getDouble("preco");

                String texto = "Produto: " + nome + " - Preço: R$ " + preco.toString();

                Log.d(TAG, texto);

                list.add(texto);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return list;
    }

    public static JSONObject getJSONObjectFromURL(String urlString) throws IOException, JSONException {

        StringBuilder result = new StringBuilder();

        URL url = new URL(urlString);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try
        {
            InputStream response = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(response));

            String line;

            while ((line = reader.readLine()) != null)
            {
                result.append(line);
            }

        }
        finally
        {
            urlConnection.disconnect();
        }

        Log.d(TAG, result.toString());

        String jsonString = result.toString();

        return new JSONObject(jsonString);
    }
}
