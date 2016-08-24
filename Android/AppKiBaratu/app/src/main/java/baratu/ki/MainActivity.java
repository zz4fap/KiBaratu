package baratu.ki;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "KIBARATU";

    private static final int VIBRATE_CLICK = 25;
    private static final int VIBRATE_LONG_CLICK = 35;

    private ProdutoArrayAdapter produtoArrayAdapter;

    private ListView listProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        produtoArrayAdapter = new ProdutoArrayAdapter(getApplicationContext(), R.layout.layout_listview_produto);

        //Get View
        listProdutos = (ListView)findViewById(R.id.listProdutos);

        listProdutos.setClickable(true);

        //Set custom adapter
        listProdutos.setAdapter(produtoArrayAdapter);

        //refreshListaProdutos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        ApiKiBaratu api;

        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        vibrator.vibrate(VIBRATE_CLICK);

        switch (item.getItemId())
        {
//            case R.id.list_supermercado:
//                Toast.makeText(getApplicationContext(), "Spinner Click", Toast.LENGTH_SHORT).show();
//                return true;

            case R.id.add_supermercado:

                intent = new Intent(MainActivity.this, CadastroSupermercadoActivity.class);

                startActivity(intent);

//                api = new ApiKiBaratu();
//
//                try {
//                    Integer novoId = api.getNovoIdSupermercado();
//
//                    api.postSupermercado(novoId, "Pão de Açucar", "", "Campinas", "200,355");
//                }
//                catch (Exception ex)
//                {}

                return true;

            case R.id.add_produto:

                intent = new Intent(MainActivity.this, CadastroProdutoActivity.class);

                startActivity(intent);

//                api = new ApiKiBaratu();
//
//                try {
//                    Integer novoId = api.getNovoIdProduto();
//
//                    api.postProduto(novoId, "Tomate", "???", 6.40, 2);
//                }
//                catch (Exception ex)
//                {}

                return true;

            case R.id.refresh_list:

                refreshListaProdutos();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshListaProdutos()
    {
        List<Produto> listProduto = new ArrayList<Produto>();

        //Recupera lista de produtos
        listProduto = new ApiKiBaratu().getListaProdutos();

        //Limpa adaptador
        produtoArrayAdapter.clear();

        //Carrega dados no adaptador
        for (Produto p:listProduto)
        {
            Log.d(TAG, p.getNome());

            produtoArrayAdapter.add(p);
        }

        //Atualiza lista
        produtoArrayAdapter.notifyDataSetChanged();
    }

}
