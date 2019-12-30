package com.ashesha.sbservicesadmin.Utils;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ashesha.sbservicesadmin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class ApiCallingFlow {
    private ViewGroup parentLayout;
    private Context mContext;
    private View progressLayout;
    //  private SimpleArcDialog mDialog;
    private boolean isTransparent;
    private boolean isNetworkAvailable;
    private ConnectivityManager connectivityManager;

    /**
     * Constructor used to initialize this functionality
     *
     * @param context       context
     * @param parentLayout  parentLayout
     * @param isTransparent isTransparent
     */
    public ApiCallingFlow(Context context, ViewGroup parentLayout, boolean isTransparent) {
        this.parentLayout = parentLayout;
        mContext = context;
        this.isTransparent = isTransparent;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        inflateAndSetUpLayout();
    }

    public ApiCallingFlow(Context context){
        this.mContext = context;
    }

    private void inflateAndSetUpLayout() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        progressLayout = layoutInflater.inflate(R.layout.layout_api_calling_flow, parentLayout, false);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        progressLayout.setLayoutParams(layoutParams);
        ButterKnife.bind(this, progressLayout);
        //Calling function to initialize required views.
        initializeViews();
        if (parentLayout != null) {
            removeProgressView();
            parentLayout.addView(progressLayout);
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        // if no network is available networkInfo will be null, otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            isNetworkAvailable = true;
            btnTryAgain.setVisibility(View.GONE);
            tvApiError.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            netWorkErrorLayout.setVisibility(View.GONE);
        } else {
            isNetworkAvailable = false;
            btnTryAgain.setVisibility(View.VISIBLE);
            tvApiError.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            netWorkErrorLayout.setVisibility(View.VISIBLE);
        }
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                // if no network is available networkInfo will be null, otherwise check if we are connected
                if (networkInfo != null && networkInfo.isConnected()) {
                    isNetworkAvailable = true;
                    removeProgressView();
                    callCurrentApiHere();
                } else {
                    tvApiError.setVisibility(View.GONE);
                    netWorkErrorLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @BindView(R.id.btnTryAgain)
    Button btnTryAgain;
    @BindView(R.id.tvApiError)
    ImageView tvApiError;
    @BindView(R.id.tvEnableNetwork)
    TextView tvEnableNetwork;
    @BindView(R.id.rl_base_layout)
    RelativeLayout rlBaseLayout;
    @BindView(R.id.network_error_layout)
    RelativeLayout netWorkErrorLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     */
    private void initializeViews() {
        btnTryAgain.setVisibility(View.GONE);

        tvApiError.setVisibility(View.GONE);
        netWorkErrorLayout.setVisibility(View.GONE);
        if (isTransparent) {
            rlBaseLayout.setBackgroundColor(Color.TRANSPARENT);
        } else {
            rlBaseLayout.setBackgroundColor(Color.WHITE);
        }
    }

    /**
     * function to be called when API need to be retried.
     */
    public abstract void callCurrentApiHere();

    /**
     * remove inflated layout
     */
    private void removeProgressView() {
        if (parentLayout != null && progressLayout.getParent() == parentLayout) {
            progressBar.setVisibility(View.GONE);
            parentLayout.removeView(progressLayout);
            //  parentLayout.removeAllViews();
        }
    }

    /**
     * function to be called when API failed.
     */
    public void onErrorResponse() {
        progressBar.setVisibility(View.GONE);
        rlBaseLayout.setBackgroundColor(Color.WHITE);
        tvApiError.setVisibility(View.VISIBLE);
        btnTryAgain.setVisibility(View.VISIBLE);
    }


    /**
     * function to be called when API succeed.
     */
    public void onSuccessResponse() {
        removeProgressView();
    }

    public boolean getNetworkState() {
        return isNetworkAvailable;
    }
}