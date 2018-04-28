// Created By Piyush Sharma - 21-04-2018

package payments;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class PayviaStripe implements AsyncResponse{

    private String stripeToken;
    private String serverResponse;
    private double chargeUser;
    SendDeviceDetails sendresponse = new SendDeviceDetails(this);
    public PayviaStripe(String token,double totalCharge){
       this.stripeToken = token;
       this.chargeUser = totalCharge;
    }

    @Override
    public void AsyncResponse(String output) {
        this.serverResponse = output;
        //Log.d("serverResp",this.serverResponse);
    }

    @Override
    public String getAsyncResponse() {
        return this.serverResponse;
    }

    private class SendDeviceDetails  extends AsyncTask<String, Void, String> {


        private AsyncResponse delegate;

        public SendDeviceDetails(AsyncResponse delegate){
            this.delegate = delegate;
        }

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes("PostData=" + params[1]);
                wr.flush();
                wr.close();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            delegate.AsyncResponse(result);
        }
    }

    public String sendFormParms(){
        JSONObject postData = new JSONObject();
        String str_result;
        try {
            postData.put("token", this.stripeToken);
            postData.put("amount", this.chargeUser);
           // Log.d("token",postData.toString());
            str_result = sendresponse.execute("http://payviastripe.bazaarena.com", postData.toString()).get();
            try {
                Log.d("responsePass",str_result);
            }catch(Exception e){
                Log.d("errorStack", e.getMessage());
            }
            return str_result;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}


