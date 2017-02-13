package com.appbaragi.smarthome;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kim on 2017-02-04.
 */

public class ButtonFragment extends Fragment {
    boolean setShower = false;
    boolean setLight =false;
    boolean setAircon=false;

    ImageView airconImg;
    ImageView showerImg;
    ImageView lightImg;

    TextView showerText;
    TextView lightText;
    TextView airconText;

    MainTab mainTab = null;

    public ButtonFragment(MainTab mainTab) {
        this.mainTab = mainTab;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.button_fragment, container, false);

        showerImg = (ImageView) view.findViewById(R.id.shower);
        showerText =(TextView)view.findViewById(R.id.shower_text);
        showerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setShower == false) {
                    showerImg.setImageResource(R.drawable.shower);
                    Resources res = getResources();
                    new PostAsync(getContext(),"목욕물 켜").execute();
                    showerText.setText("목욕물 켜짐");
                    setShower = true;
                } else {
                    showerImg.setImageResource(R.drawable.shower_off);
                    new PostAsync(getContext(),"목욕물 꺼").execute();
                    showerText.setText("목욕물 꺼짐");
                    setShower = false;
                }
            }
        });
        lightImg = (ImageView) view.findViewById(R.id.light);
        lightText=(TextView) view.findViewById(R.id.light_text);
        lightImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setLight == false) {
                    lightImg.setImageResource(R.drawable.light_bulb_with_filament_on);
                    new PostAsync(getContext(),"불 켜").execute();
                    lightText.setText("불 켜짐");
                    setLight = true;
                } else {
                    lightImg.setImageResource(R.drawable.light_bulb_with_filamen);
                    new PostAsync(getContext(),"불 꺼").execute();
                    lightText.setText("불 꺼짐");
                    setLight = false;
                }
            }
        });
        airconImg = (ImageView) view.findViewById(R.id.aircon);
        airconText=(TextView)view.findViewById(R.id.aircon_text);
        airconImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setAircon == false) {
                    airconImg.setImageResource(R.drawable.air_conditioner_on);
                    new PostAsync(getContext(),"에어컨 켜").execute();
                    airconText.setText("에어컨 켜짐");
                    setAircon = true;
                } else {
                    airconImg.setImageResource(R.drawable.air_conditioner);
                    new PostAsync(getContext(),"에어컨 꺼").execute();
                    airconText.setText("에어컨 꺼짐");
                    setAircon = false;
                }
            }
        });

        return view;
    }
}
