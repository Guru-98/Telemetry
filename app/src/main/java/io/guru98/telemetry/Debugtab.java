package io.guru98.telemetry;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;

@SuppressWarnings("SpellCheckingInspection")
public class Debugtab extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_debug, container,false);
        rootView.findViewById(R.id.log_refresh_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TelemetryClient telemetryClient = new TelemetryClient();
                try {
                    telemetryClient.execute(new URL("http","192.168.4.1","/"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                String response = null;
                try {
                    response = telemetryClient.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ((TextView) rootView.findViewById(R.id.textViewSocket)).setText(response);
            }
        });
        return rootView;
    }
}
