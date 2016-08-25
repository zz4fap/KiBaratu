package baratu.ki;

import android.util.Log;

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

public class ApiKiBaratu {

    private static final String TAG = "KIBARATU";

    private static final String URL_BASE_PRODUTO = "https://1-dot-kibaratu-141102.appspot.com/_ah/api/produtoendpoint/v1/produto/";
    private static final String URL_BASE_SUPERMERCADO = "https://1-dot-kibaratu-141102.appspot.com/_ah/api/supermercadoendpoint/v1/supermercado/";

    public ApiKiBaratu(){}

    public String getSupermercadoNome(Integer id, List<Supermercado> listaSupermercado) {

        String nome;

        for (Supermercado s: listaSupermercado) {

            Log.d(TAG, "S: " + s.getId().toString() + " == " + id.toString());

            if(s.getId() == id)
            {
                return s.getNome();
            }
        }

        return null;
    }

    public boolean postSupermercado(Integer id, String nome, String unidade, String endereco, String localizacao) throws IOException {

        //Montagem dos parametros
        StringBuilder params = new StringBuilder();

        params.append("?id=" + id.toString());
        params.append("&nome=" + URLEncoder.encode(nome, "UTF-8"));
        params.append("&unidade=" + URLEncoder.encode(unidade, "UTF-8"));
        params.append("&endereco=" + URLEncoder.encode(endereco, "UTF-8"));
        params.append("&localizacao=" + URLEncoder.encode(localizacao, "UTF-8"));

        String encode_url = URL_BASE_SUPERMERCADO + params.toString();

        Log.d(TAG, "URL: " + encode_url);

        //Converte a string para o padrao URL
        URL url = new URL(encode_url);

        return postRecurso(url);
    }

    public boolean postProduto(Integer id, String nome, String marca, Double preco, Integer supermercadoId) throws IOException {

        //Montagem dos parametros
        StringBuilder params = new StringBuilder();

        params.append("?id=" + id.toString());
        params.append("&nome=" + URLEncoder.encode(nome, "UTF-8"));
        params.append("&marca=" + URLEncoder.encode(marca, "UTF-8"));
        params.append("&preco=" + preco.toString());
        params.append("&tipo=");
        params.append("&peso=");
        params.append("&quantidade=");
        params.append("&supermercadoId=" + supermercadoId.toString());

        String encode_url = URL_BASE_PRODUTO + params.toString();

        Log.d(TAG, "URL: " + encode_url);

        //Converte a string para o padrao URL
        URL url = new URL(encode_url);

        return postRecurso(url);
    }

    //Retorna um novo ID de Produto com base no ultimo ID salvo
    public Integer getNovoIdProduto() {

        Integer ultimoId = 0;

        List<Produto> listaProduto = getListaProdutos();

        //Carrega dados no adaptador
        for (Produto p:listaProduto)
        {
            if(p.getId() > ultimoId)
            {
                ultimoId = p.getId();
            }
        }

        return (ultimoId + 1);
    }

    //Retorna um novo ID de Supermercado com base no ultimo ID salvo
    public Integer getNovoIdSupermercado() {

        Integer ultimoId = 0;

        List<Supermercado> listaSupermercado = getListaSupermercados();

        //Carrega dados no adaptador
        for (Supermercado s:listaSupermercado)
        {
            if(s.getId() > ultimoId)
            {
                ultimoId = s.getId();
            }
        }

        return (ultimoId + 1);
    }

    //Retorna Lista de Produtos
    public List<Produto> getListaProdutos() {

        List<Supermercado> listaSupermercado = getListaSupermercados();
        List<Produto> listaProduto = new ArrayList<Produto>();

        try
        {
            JSONObject jsonObject = getRecurso(URL_BASE_PRODUTO);

            JSONArray jsonArray = jsonObject.getJSONArray("items");

            for ( int i = 0; i < jsonArray.length() ; i++)
            {
                //this object inside array you can do whatever you want
                JSONObject item = jsonArray.getJSONObject(i);

                Produto produto = new Produto();

                produto.setId(item.getInt("id"));
                produto.setNome(item.optString("nome"));
                produto.setPreco(item.optDouble("preco"));
                produto.setMarca(item.optString("marca"));
                produto.setSupermercadoId(item.optInt("supermercadoId"));
                produto.setSupermercadoNome(getSupermercadoNome(item.optInt("supermercadoId"), listaSupermercado));

                listaProduto.add(produto);
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

        return listaProduto;
    }

    //Retorna um Produto especifico
    public Produto getProdutoById(Integer id){

        Produto produto = new Produto();

        try
        {
            JSONObject item = getRecurso(URL_BASE_PRODUTO + id);

            produto.setId(item.getInt("id"));
            produto.setNome(item.optString("nome"));
            produto.setPreco(item.optDouble("preco"));
            produto.setMarca(item.optString("marca"));
            produto.setSupermercadoId(item.optInt("supermercadoId"));
            produto.setSupermercadoNome(getSupermercadoById(id).getNome());

            Log.d(TAG, produto.getNome());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return produto;
    }

    //Retorna uma lista de Supermercado
    public List<Supermercado> getListaSupermercados() {

        List<Supermercado> list = new ArrayList<Supermercado>();

        try
        {
            JSONObject jsonObject = getRecurso(URL_BASE_SUPERMERCADO);

            JSONArray jsonArray = jsonObject.getJSONArray("items");

            for ( int i = 0; i < jsonArray.length() ; i++)
            {
                //this object inside array you can do whatever you want
                JSONObject item = jsonArray.getJSONObject(i);

                Supermercado supermercado = new Supermercado();

                supermercado.setId(item.getInt("id"));
                supermercado.setNome(item.optString("nome"));
                supermercado.setUnidade(item.optString("unidade"));
                supermercado.setEndereco(item.optString("endereco"));
                supermercado.setLocalizacao(item.optString("localizacao"));

                Log.d(TAG, supermercado.getNome());

                list.add(supermercado);
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

    //Retorna um Supermercado especifico
    public Supermercado getSupermercadoById(Integer id){

        Supermercado supermercado = new Supermercado();

        try
        {
            JSONObject item = getRecurso(URL_BASE_SUPERMERCADO + id);

            supermercado.setId(item.getInt("id"));
            supermercado.setNome(item.optString("nome"));
            supermercado.setUnidade(item.optString("unidade"));
            supermercado.setEndereco(item.optString("endereco"));
            supermercado.setLocalizacao(item.optString("localizacao"));

            Log.d(TAG, supermercado.getNome());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return supermercado;
    }

    private boolean postRecurso(URL url) {

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

    private static JSONObject getRecurso(String urlString) throws IOException, JSONException {

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
