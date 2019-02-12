package mdelacalle.com.cartasnauticas;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AemetAPI {

    public static final String API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZGVsYWNhbGxlQGdtYWlsLmNvbSIsImp0aSI6IjA0YmM5NTNhLTE3YmMtNDkwNS04YTczLWU2YTYwNTUzMTU4ZiIsImlzcyI6IkFFTUVUIiwiaWF0IjoxNTQ2NTA1NzE3LCJ1c2VySWQiOiIwNGJjOTUzYS0xN2JjLTQ5MDUtOGE3My1lNmE2MDU1MzE1OGYiLCJyb2xlIjoiIn0.s9anASRAOO0BW0BlM2awjYeMpQW29YdeH96DvyOTEAw";

    public static void getPrediccionCosteraByArea(final int area, final AemetAPIListener listener) {

        final String urlAPIAemetGetAvisos = "https://opendata.aemet.es/opendata/api/prediccion/maritima/costera/costa/" + area;

        HttpUrl.Builder urlBuilder = HttpUrl.parse(urlAPIAemetGetAvisos)
                .newBuilder();
        String url = urlBuilder
                .build()
                .toString();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("api_key",
                        API_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("***", "Something very bad was happen retrieving data from Aemet:"+urlAPIAemetGetAvisos);
                e.printStackTrace();
                if(listener!=null) {
                    listener.onError();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseSt;
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {

                    responseSt = response.body().string();
                    Log.e("***", "RESPONSE:" + responseSt);

                    try {
                        JSONObject jsonObject = new JSONObject(responseSt);
                        String datos = jsonObject.getString("datos");
                        getPrediccionAndSaveInDatabase(datos,area,listener);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }
                }
            }
        });
     }

    private static void getPrediccionAndSaveInDatabase(final String datos, int area,final AemetAPIListener listener) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(datos)
                .newBuilder();
        String url = urlBuilder
                .build()
                .toString();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json; charset=iso-8859-1")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("***", "Something very bad was happen retrieving data from Aemet:" + datos);
                e.printStackTrace();
                listener.onError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseSt;
                if (!response.isSuccessful()) {
                    listener.onError();
                    throw new IOException("Unexpected code " + response);
                } else {
                     responseSt = response.body().string();

                    try {
                        JSONArray jsonarrayresponse = new JSONArray(responseSt);

                        Realm  realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        realm.where(PrediccionCosteraArea.class).findAll().deleteAllFromRealm();

                        PrediccionCosteraArea prediccion = new PrediccionCosteraArea();

                        prediccion.setArea(area);
                        JSONObject jsonObject = jsonarrayresponse.getJSONObject(0);
                        prediccion.setNombre(jsonObject.getString("nombre"));
                        JSONObject origenJO = jsonObject.getJSONObject("origen");
                        prediccion.setProductor(origenJO.getString("productor"));
                        prediccion.setElaborado(origenJO.getString("elaborado"));
                        prediccion.setNotaLegal(origenJO.getString("notaLegal"));
                        prediccion.setInicio(origenJO.getString("inicio"));
                        prediccion.setFin(origenJO.getString("fin"));
                        JSONObject avisoJO = jsonObject.getJSONObject("aviso");

                        prediccion.setAviso(new Aviso(
                                avisoJO.getString("inicio") ,
                                avisoJO.getString("fin"),
                                avisoJO.getString("texto")
                        ));

                        JSONObject situacionJO = jsonObject.getJSONObject("situacion");

                        prediccion.setSituacion(new Situacion(
                                situacionJO.getString("analisis"),
                                situacionJO.getString("inicio") ,
                                situacionJO.getString("fin"),
                                situacionJO.getString("texto"),
                                situacionJO.getString("nombre")
                        ));

                        JSONObject prediccionJO = jsonObject.getJSONObject("prediccion");

                        JSONArray zonaArrayJO = prediccionJO.getJSONArray("zona");

                        RealmList<Zona> zonas = new RealmList<Zona>();

                        for(int i = 0; i < zonaArrayJO.length() ; i++ ){
                            JSONObject subzona = zonaArrayJO.getJSONObject(i).getJSONObject("subzona");
                            zonas.add(new Zona(
                                    subzona.getString("texto"),
                                    subzona.getString("nombre")
                            ));

                        }

                        prediccion.setPrediccion(new Prediccion(
                                prediccionJO.getString("inicio"),
                                prediccionJO.getString("fin"),
                                zonas
                        ));


                        JSONObject tendenciaJO = jsonObject.getJSONObject("tendencia");

                        prediccion.setTendencia(new Tendencia(
                                tendenciaJO.getString("inicio"),
                                tendenciaJO.getString("fin"),
                                tendenciaJO.getString("texto")
                        ));

                        realm.copyToRealm(prediccion);
                        realm.commitTransaction();
                        realm.refresh();
                        realm.close();

                        listener.onSuccess();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }
                }

            }
        });
    }
}


