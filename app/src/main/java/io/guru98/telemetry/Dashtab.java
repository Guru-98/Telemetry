package io.guru98.telemetry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.anastr.speedviewlib.SpeedView;

/**
 * Created by guru9 on 21/3/2018.
 */

public class Dashtab extends Fragment implements TelemetryDataReceiver.Receiver {

    private final String TAG = "MainTab Fragment";

    private TelemetryDataReceiver mReceiver;
    private SpeedView speedometer;
    private SpeedView tachometer;
    private TextView gN, g1, g2, g3, g4, g5, g6;
    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "Creating View");
        View rootView = inflater.inflate(R.layout.tab_dash, container, false);
        speedometer = rootView.findViewById(R.id.speedometer_view);
        tachometer = rootView.findViewById(R.id.tachometer_view);
        gN = rootView.findViewById(R.id.gear_N);
        g1 = rootView.findViewById(R.id.gear_1);
        g2 = rootView.findViewById(R.id.gear_2);
        g3 = rootView.findViewById(R.id.gear_3);
        g4 = rootView.findViewById(R.id.gear_4);
        g5 = rootView.findViewById(R.id.gear_5);
        g6 = rootView.findViewById(R.id.gear_6);
        Log.d(TAG, "Created View");
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "Creating Fragment");
        mReceiver = new TelemetryDataReceiver(new Handler());
        mReceiver.setReceiver(this);

        intent = new Intent(getContext(), TelemetryService.class);
        /* Send optional extras to Download IntentService */
        intent.putExtra("url", "http://192.168.4.1/");
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("requestId", 101);

        Log.d(TAG, "Starting Service");
        getActivity().startService(intent);
        Log.d(TAG, "Created Fragment");
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case TelemetryService.STATUS_RUNNING:
                break;
            case TelemetryService.STATUS_FINISHED:

                int speed = resultData.getInt("speed");
                int gear = resultData.getInt("gear");
                speedometer.speedTo((float) speed / 1023 * 100, 1000);


                gN.setSelected(false);
                g1.setSelected(false);
                g2.setSelected(false);
                g3.setSelected(false);
                g4.setSelected(false);
                g5.setSelected(false);
                g6.setSelected(false);

                switch (gear) {
                    case 0:
                        gN.setSelected(true);
                        break;
                    case 1:
                        g1.setSelected(true);
                        break;
                    case 2:
                        g2.setSelected(true);
                        break;
                    case 3:
                        g3.setSelected(true);
                        break;
                    case 4:
                        g4.setSelected(true);
                        break;
                    case 5:
                        g5.setSelected(true);
                        break;
                    case 6:
                        g6.setSelected(true);
                        break;
                }
                break;

            case TelemetryService.STATUS_ERROR:
                /* Handle the error */
                String error = resultData.getString(Intent.EXTRA_TEXT);
                try {
                    Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.d("Main_tab", e.getMessage());
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Pausing Fragment");
        intent = new Intent(Intent.ACTION_SYNC, null, getContext(), TelemetryService.class);
        Log.d(TAG, "Stoping Service");
        getActivity().stopService(intent);
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "Destroying Fragment");
        super.onDestroyView();
        intent = new Intent(Intent.ACTION_SYNC, null, getContext(), TelemetryService.class);
        Log.d(TAG, "Stoping Service");
        getActivity().stopService(intent);
    }
}
