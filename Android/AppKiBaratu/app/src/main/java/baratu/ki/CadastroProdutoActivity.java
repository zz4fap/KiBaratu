package baratu.ki;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CadastroProdutoActivity extends Activity{

    private static final String TAG = "KIBARATU";

    private static final int VIBRATE_CLICK = 25;

    private ApiKiBaratu api = new ApiKiBaratu();

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cadastro_produto);

        //GET Supermercados
        List<Supermercado> listaSupermercados = api.getListaSupermercados();

        //Preenche Spinner com a lista de Supermercados
        List<String> supermercados = new ArrayList<String>();

        supermercados.add("-");

        for (Supermercado s:listaSupermercados) {

            supermercados.add(s.getNome());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, supermercados);

        Spinner spinnerSupermercados = (Spinner)findViewById(R.id.spinnerSupermercado);

        spinnerSupermercados.setAdapter(arrayAdapter);

        Button btnSalvar = (Button)findViewById(R.id.btnSalvar);

        btnSalvar.setOnClickListener(btnSalvarOnClick);
    }

    public View.OnClickListener btnSalvarOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

            vibrator.vibrate(VIBRATE_CLICK);

            dialog = new ProgressDialog(CadastroProdutoActivity.this);

            EditText editNome = (EditText)findViewById(R.id.editNome);
            EditText editMarca = (EditText)findViewById(R.id.editMarca);
            EditText editPreco = (EditText)findViewById(R.id.editPreco);

            Spinner spinnerSupermercado = (Spinner)findViewById(R.id.spinnerSupermercado);

            Produto produto = new Produto();

            produto.setNome(editNome.getText().toString());
            produto.setMarca(editMarca.getText().toString());
            produto.setPreco(Double.parseDouble(editPreco.getText().toString()));
            produto.setSupermercadoId(spinnerSupermercado.getSelectedItemPosition());

            Log.d(TAG, "Nome: " + produto.getNome());
            Log.d(TAG, "Marca: " + produto.getMarca());
            Log.d(TAG, "Preco: " + produto.getPreco());
            Log.d(TAG, "Supermercado Id: " + produto.getSupermercadoId());

            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Salvando...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            //POST Execute
            new CadastroAsyncTask().execute(produto);

          }
    };

    private class CadastroAsyncTask extends AsyncTask<Produto, String, String>{

        @Override
        protected String doInBackground(Produto... params) {

            try
            {
                Produto produto = params[0];

                Integer novoId = api.getNovoIdProduto();

                api.postProduto(novoId, produto.getNome(), produto.getMarca(), produto.getPreco(), produto.getSupermercadoId());

            }
            catch (Exception ex)
            {}

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.dismiss();

            finish();
        }
    }
}
