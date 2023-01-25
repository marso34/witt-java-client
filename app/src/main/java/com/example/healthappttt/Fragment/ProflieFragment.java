package com.example.healthappttt.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.healthappttt.R;
import com.example.healthappttt.Activity.SetExerciseActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProflieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProflieFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProflieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProflieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProflieFragment newInstance(String param1, String param2) {
        ProflieFragment fragment = new ProflieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_proflie, container, false);

        Button testBtn = (Button) view.findViewById(R.id.testBtn);

        testBtn.setOnClickListener(new View.OnClickListener() { //운동시작 버튼 누를시 이벤트
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SetExerciseActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        return view;
    }
}