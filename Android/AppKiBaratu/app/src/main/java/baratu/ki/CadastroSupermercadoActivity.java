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

import java.util.ArrayList;
import java.util.List;

public class CadastroSupermercadoActivity extends Activity {

    private static final String TAG = "KIBARATU";

    private static final int VIBRATE_CLICK = 25;

    private ApiKiBaratu api = new ApiKiBaratu();

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cadastro_supermercado);

        Button btnSalvarSupermercado = (Button)findViewById(R.id.btnSalvarSupermercado);

        btnSalvarSupermercado.setOnClickListener(btnSalvarOnClick);
    }

    public View.OnClickListener btnSalvarOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

            vibrator.vibrate(VIBRATE_CLICK);

            dialog = new ProgressDialog(CadastroSupermercadoActivity.this);

            EditText editNome = (EditText)findViewById(R.id.editNomeSupermercado);
            EditText editUnidade = (EditText)findViewById(R.id.editUnidade);
            EditText editEndereco = (EditText)findViewById(R.id.editEndereco);
            EditText editLocalizacao = (EditText)findViewById(R.id.editLocalizacao);

            Supermercado supermercado = new Supermercado();

            supermercado.setNome(editNome.getText().toString());
            supermercado.setUnidade(editUnidade.getText().toString());
            supermercado.setEndereco(editEndereco.getText().toString());
            supermercado.setLocalizacao(editLocalizacao.getText().toString());

            Log.d(TAG, "Nome: " + supermercado.getNome());
            Log.d(TAG, "Unidade: " + supermercado.getUnidade());
            Log.d(TAG, "Endereco: " + supermercado.getEndereco());
            Log.d(TAG, "Localizacao: " + supermercado.getLocalizacao());

            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Salvando...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            //POST Execute
            new CadastroAsyncTask().execute(supermercado);

        }
    };

    private class CadastroAsyncTask extends AsyncTask<Supermercado, String, String> {

        @Override
        protected String doInBackground(Supermercado... params) {

            try
            {
                Supermercado supermercado = params[0];

                Integer novoId = api.getNovoIdSupermercado();

                api.postSupermercado(novoId, supermercado.getNome(), supermercado.getUnidade(), supermercado.getEndereco(), supermercado.getLocalizacao());

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
