package mdelacalle.com.cartasnauticas;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AemetAPI {

    public static final String API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZGVsYWNhbGxlQGdtYWlsLmNvbSIsImp0aSI6IjA0YmM5NTNhLTE3YmMtNDkwNS04YTczLWU2YTYwNTUzMTU4ZiIsImlzcyI6IkFFTUVUIiwiaWF0IjoxNTQ2NTA1NzE3LCJ1c2VySWQiOiIwNGJjOTUzYS0xN2JjLTQ5MDUtOGE3My1lNmE2MDU1MzE1OGYiLCJyb2xlIjoiIn0.s9anASRAOO0BW0BlM2awjYeMpQW29YdeH96DvyOTEAw";

    public static void getPrediccionCosteraByArea(final String area, final AemetAPIListener listener) {

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


                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onError();
                    }
                }
            }
        });
     }
}


