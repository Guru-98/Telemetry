package io.guru98.telemetry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.anastr.speedviewlib.SpeedView;

/**
 * Created by guru9 on 21/3/2018.
 */

public class Maintab extends Fragment implements TelemetryDataReceiver.Receiver{

    private TelemetryDataReceiver mReceiver;
    private SpeedView speedometer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container,false);
        speedometer =(SpeedView) rootView.findViewById(R.id.speedometer_view);
        speedometer.setWithTremble(false);
        speedometer.setMinMaxSpeed(0,1023);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mReceiver = new TelemetryDataReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, getContext(), TelemetryService.class);

        /* Send optional extras to Download IntentService */
        intent.putExtra("url", "http://192.168.4.1/");
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("requestId", 101);

        //getActivity().startService(intent);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case TelemetryService.STATUS_RUNNING:
                break;
            case TelemetryService.STATUS_FINISHED:

                int speed = resultData.getInt("result");
                /* Update SpeedView with result */
                speedometer.speedTo((float) speed,1000);

                break;
            case TelemetryService.STATUS_ERROR:
                /* Handle the error */
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                break;
        }
    }
}
