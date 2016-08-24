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
import java.net.URLEncoder;
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
                    postProduto(7, "Fanta Uva", "Promoção da fanta uva", 10.5);
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

    public boolean postRecurso(URL url)
    {
        HttpsURLConnection conn;

        try
        {
            //Configura conexao
            conn = (HttpsURLConnection) url.openConnection();

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept-Charset", "UTF-8");

            OutputStream os = conn.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");

            //Envia dados
            writer.write("");
            writer.flush();
            writer.close();

            int responseCode = conn.getResponseCode();

            Log.d(TAG, "Response code: " + responseCode);

            //Verifica resposta do servidor
            if (responseCode == HttpsURLConnection.HTTP_OK)
            {
                return true;
            }
        }
        catch (Exception ex)
        {
            Log.d(TAG, ex.toString());
        }

        return false;
    }

    public boolean postSupermercado(Integer id, String nome, String observacao, Double preco) throws IOException
    {
        //URL base do recurso Produtos
        String url_base = "https://1-dot-kibaratu-141212.appspot.com/_ah/api/supermercadosendpoint/v1/supermercados/?";

        //Montagem dos parametros
        StringBuilder params = new StringBuilder();

        params.append("id=" + id.toString());
        params.append("&nome=" + URLEncoder.encode(nome, "UTF-8"));
        params.append("&observacao=" + URLEncoder.encode(observacao, "UTF-8"));
        params.append("&preco=" + preco.toString());

        String encode_url = url_base + params.toString();

        Log.d(TAG, "URL: " + encode_url);

        //Converte a string para o padrao URL
        URL url = new URL(encode_url);

        return postRecurso(url);
    }

    public boolean postProduto(Integer id, String nome, String observacao, Double preco) throws IOException
    {
        //URL base do recurso Produtos
        String url_base = "https://1-dot-kibaratu-141212.appspot.com/_ah/api/produtosendpoint/v1/produtos/?";

        //Montagem dos parametros
        StringBuilder params = new StringBuilder();

        params.append("id=" + id.toString());
        params.append("&nome=" + URLEncoder.encode(nome, "UTF-8"));
        params.append("&observacao=" + URLEncoder.encode(observacao, "UTF-8"));
        params.append("&preco=" + preco.toString());

        String encode_url = url_base + params.toString();

        Log.d(TAG, "URL: " + encode_url);

        //Converte a string para o padrao URL
        URL url = new URL(encode_url);

        return postRecurso(url);
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
