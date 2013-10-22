package net.sitecore.android.sdk.api;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;

import static net.sitecore.android.sdk.api.LogUtils.LOGD;

class RsaPublicKeyResponseListener implements Response.Listener<String> {

    private String mUrl;
    private String mName;
    private String mPassword;

    private Response.Listener<ScApiSession> mOnSuccess;
    private Response.ErrorListener mOnError;

    RsaPublicKeyResponseListener(final String url,
            final String name,
            final String password,
            final Response.Listener<ScApiSession> onSuccess,
            final Response.ErrorListener onError) {
        mUrl = url;
        mName = name;
        mPassword = password;
        mOnSuccess = onSuccess;
        mOnError = onError;
    }

    @Override
    public void onResponse(String response) {
        try {
            LOGD("RSA public key received:" + response);
            RSAPublicKey pub = CryptoUtils.getPublicKey(response);
            ScApiSession scContext = new ScApiSessionImpl(mUrl, pub, mName, mPassword);
            mOnSuccess.onResponse(scContext);
        } catch (InvalidKeySpecException e) {
            sendError(e);
        } catch (NoSuchAlgorithmException e) {
            sendError(e);
        }
    }

    private void sendError(Throwable e) {
        if (mOnError != null) mOnError.onErrorResponse(new VolleyError(e));
    }
}