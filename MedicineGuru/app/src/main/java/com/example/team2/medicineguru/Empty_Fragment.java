package com.example.team2.medicineguru;
import android.app.Fragment;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

/**
 * Created by TutorialsPoint7 on 8/23/2016.
 */

public class Empty_Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         * Inflate the layout for this fragment
         */
        return inflater.inflate(R.layout.lm_fragement, container, false);
    }
}