package zap.com.example.app.appzap;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import zap.com.example.app.appzap.controller.ServiceRest;

import java.util.ArrayList;
import java.util.HashMap;

public class AplicationActivity extends AppCompatActivity {

    private String TAG = AplicationActivity.class.getSimpleName();
    //URL REST
    private static String url = "http://demo4573903.mockable.io/imoveis";

    private ProgressDialog dialog;
    ArrayList<HashMap<String, String>> imovelList;
    private ListView list;

    private TextView textView;

    private ServiceRest service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplication);

        imovelList = new ArrayList<>();
        list = (ListView) findViewById(R.id.list);

        new GetRequest().execute();

    }

    private class GetRequest extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AplicationActivity.this);
            dialog.setMessage("Aguarde ...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            service = new ServiceRest();

            String jsonString = service.serviceCall(url);
            //Log.v(TAG, "jsonString URL <? @=@ ?>: " + jsonString);// Mostra a Url do Rest
            Log.e(TAG, "Resposnse from URL: "+ jsonString);
            if(jsonString != null){
                try {
                        JSONObject jsonObject = new JSONObject(jsonString);
                        //Log.v(TAG, "jsonObject String : " + jsonObject);// Objeto com a String da URL
                        JSONArray itens = jsonObject.getJSONArray("Imoveis");
                        //Log.v(TAG, "itens do Objeto : " + itens);// Itens que contem o objeto
                        for (int i=0; i< itens.length(); i++){
                            JSONObject jObject = itens.getJSONObject(i);
                            Log.i("TAG i >","Objeto do for : \n" + i);
                            //Log.v(TAG, "jObject subItens do Objeto : " + itens.getJSONObject(i));// SubItens do Objeto
                            //int codImovel  = jObject.getInt("CodImovel");
                            String tipoImovel = jObject.getString("TipoImovel");
                            JSONObject endereco   = jObject.getJSONObject("Endereco");
                            String numero     = endereco.getString("Numero");
                            String cep        = endereco.getString("CEP");
                            String bairro     = endereco.getString("Bairro");
                            String cidade     = endereco.getString("Cidade");
                            String estado     = endereco.getString("Estado");
                            String zona       = endereco.getString("Zona");
                            int precoVenda = jObject.getInt("PrecoVenda");
                            int dormitorios = jObject.getInt("Dormitorios");
                            int suites     = jObject.getInt("Suites");
                            int vagas      = jObject.getInt("Vagas");
                            int areaUtil   = jObject.getInt("AreaUtil");
                            int areaTotal  = jObject.getInt("AreaTotal");
                            String dataAtualizacao = jObject.getString("DataAtualizacao");
                            JSONObject cliente     = jObject.getJSONObject("Cliente");
                            String codCliente = cliente.getString("CodCliente");
                            String nomeFantasia = cliente.getString("NomeFantasia");
                            //JSONObject urlImagem  = jObject.getJSONObject("UrlImagem");
//                            String subTipoOferta = urlImagem.getString("SubTipoOferta");
//                            String subTipoImovel = urlImagem.getString("SubtipoImovel");

                            HashMap<String, String> imoveis = new HashMap<>();
                            //imoveis.put("CodImovel", String.valueOf(codImovel));
                            imoveis.put("TipoImovel", tipoImovel);
                            imoveis.put("Endereco", String.valueOf(endereco));
                            imoveis.put("Numero", numero);
                            imoveis.put("CEP", cep);
                            imoveis.put("Bairro", bairro);
                            imoveis.put("Cidade", cidade);
                            imoveis.put("Estado", estado);
                            imoveis.put("Zona", zona);
                            imoveis.put("PrecoVenda", String.valueOf(precoVenda));
                            imoveis.put("Dormitorios", String.valueOf(dormitorios));
                            imoveis.put("Suites", String.valueOf(suites));
                            imoveis.put("Vagas", String.valueOf(vagas));
                            imoveis.put("AreaUtil", String.valueOf(areaUtil));
                            imoveis.put("AreaTotal", String.valueOf(areaTotal));
                            imoveis.put("DataAtualizacao", dataAtualizacao);
                            imoveis.put("Cliente", String.valueOf(cliente));
                            imoveis.put("CodCliente", codCliente);
                            imoveis.put("NomeFantasia", nomeFantasia);
                            //imoveis.put("UrlImagem", String.valueOf(urlImagem));
//                            imoveis.put("SubTipoOferta", subTipoOferta);
//                            imoveis.put("SubTipoImovel", subTipoImovel);

                            imovelList.add(imoveis);
                        }
                    } catch (final JSONException e) {
                        Log.e(TAG,"JSON Erro Parsin " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Erro JSON Parsin !\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "Not did get json server." );
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Not did get json server. ", Toast.LENGTH_LONG).show();
                        }
                    });
             }

            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dialog.isShowing())
                dialog.dismiss();
            ListAdapter adapter = new SimpleAdapter(
                    AplicationActivity.this, imovelList,
                    R.layout.list_item, new String[]{/*"CodImovel",*/
                    "TipoImovel", "Endereco", "Numero",
                    "CEP", "Bairro", "Cidade",
                    "Estado", "Zona", "PrecoVenda",
                    "Dormitorios", "Suites", "AreaUtil",
                    "AreaTotal", "DataAtualizacao",
                    "CodCliente", "Nomefantasia",
                    "Cliente", "CodCliente", "NomeFantasia"}, new int[]{/*R.id.codIovel,*/
                    R.id.tipoImovel, R.id.endereco, R.id.numero,
                    R.id.cep,  R.id.bairro, R.id.cidade,
                    R.id.estado, R.id.zona, R.id.precoVenda,
                    R.id.dormitorios, R.id.suites, R.id.areaUtil,
                    R.id.areaTotal, R.id.dataAtualizacao,
                    R.id.codCliente, R.id.nomeFantasia,
                    R.id.cliente, R.id.codCliente, R.id.nomeFantasia});

            list.setAdapter(adapter);
        }
    }
}
