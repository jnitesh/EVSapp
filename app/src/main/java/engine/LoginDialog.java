/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package engine;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * A subclass of {@link Dialog} for performing login procedure against various authentication providers.
 */
public class LoginDialog extends Dialog {

    static final FrameLayout.LayoutParams FILL =
            new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.FILL_PARENT);
    private String mUrl;
    private LoginListener mListener;
    private ProgressDialog mSpinner;
    private Button cancelButton;
    private WebView mWebView;
    private FrameLayout frameLayout;
    private LinearLayout content;
    private LinearLayout buttonContainer;

    /**
     * Constructs a LoginDialog.
     * @param context Activity context.
     * @param url URL of the authentication service. URLs are provided by {@link LeanEngine#getFacebookLoginUri()},
     * {@link LeanEngine#getGoogleLoginUri()}, etc..
     * @param listener {@link LoginListener} that will be invoked when authentication procedure finishes or in case
     * of errors.
     */
    public LoginDialog(Context context, String url, LoginListener listener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        mUrl = url;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSpinner = new ProgressDialog(getContext());
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        frameLayout = new FrameLayout(getContext());
        content = new LinearLayout(getContext());
        content.setOrientation(LinearLayout.VERTICAL);
        content.setLayoutParams(FILL);
        content.setBackgroundColor(Color.LTGRAY);

        createCancelButton();

        setUpWebView(5);

        frameLayout.addView(content, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        frameLayout.setPadding(5, 5, 5, 5);
        addContentView(frameLayout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    }

    private void createCancelButton() {
        buttonContainer = new LinearLayout(getContext());
        buttonContainer.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        buttonContainer.setBackgroundColor(Color.LTGRAY);
        buttonContainer.setPadding(5, 5, 5, 0);
        buttonContainer.setGravity(Gravity.CENTER_VERTICAL);

        cancelButton = new Button(getContext());
        cancelButton.setText("Cancel");
        cancelButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancel();
                LoginDialog.this.dismiss();
            }
        });

        buttonContainer.addView(cancelButton, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        content.addView(buttonContainer);
        buttonContainer.setVisibility(View.INVISIBLE);
    }

    private void setUpWebView(int margin) {
        LinearLayout webViewContainer = new LinearLayout(getContext());
        mWebView = new WebView(getContext());
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new InternalWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);
        mWebView.setLayoutParams(FILL);
        mWebView.setVisibility(View.INVISIBLE);

        webViewContainer.setPadding(margin, margin, margin, margin);
        webViewContainer.addView(mWebView);
        content.addView(webViewContainer, FILL);
    }

    private class InternalWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url != null && url.startsWith("leanengine://")) {
                UrlQuerySanitizer query = new UrlQuerySanitizer(url);

                String token = query.getValue("auth_token");
                if (token != null) {
                    LoginDialog.this.dismiss();
                    LeanEngine.saveAuthData(token);
                    mListener.onSuccess();
                } else {
                    String errorCode = query.getValue("errorcode");
                    String errorMsg = null;
                    try {
                        errorMsg = URLDecoder.decode(query.getValue("errormsg"), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // should not happen - UTF-8 is always supported
                    }
                    LoginDialog.this.dismiss();
                    mListener.onError(new LeanError(LeanError.Type.NotAuthorizedError,
                            "Authorization error: error. errorCode=" + errorCode + " errorMsg=" + errorMsg));
                }
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mListener.onError(new LeanError(LeanError.Type.ServerError, " Could not connect to Facebook authorization server."));
            LoginDialog.this.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mSpinner.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mSpinner.dismiss();

            frameLayout.setBackgroundColor(Color.TRANSPARENT);
            mWebView.setVisibility(View.VISIBLE);
            buttonContainer.setVisibility(View.VISIBLE);
        }
    }

}
