package com.example.healthappttt.Fragment;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthappttt.Activity.CreateRoutineActivity;
import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.RoutineAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutineChildFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutineChildFragment extends Fragment {
    private static final int REQUEST_CODE = 100;

    private RecyclerView recyclerView;
    private RoutineAdapter adapter;
    private CardView addRoutineBtn;


    private ArrayList<Routine> routines;
    private int day_of_week;



    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;// 파이어베이스 유저관련 접속하기위한 변수
    private FirebaseFirestore db;
    private String UserUid;
    private String dayOfWeek = "";



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RoutineChildFragment() {}
    
    public RoutineChildFragment(int day_of_week) {
        this.day_of_week = day_of_week;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RoutineChildFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RoutineChildFragment newInstance(String param1, String param2) {
        RoutineChildFragment fragment = new RoutineChildFragment();
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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_routine_child, container, false);


        recyclerView = view.findViewById(R.id.recyclerView);
        addRoutineBtn = view.findViewById(R.id.addRoutine);

//
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        UserUid = mAuth.getCurrentUser().getUid();
//

        routines = new ArrayList<>();

        switch (day_of_week) {
            case 0: dayOfWeek = "sun"; break;
            case 1: dayOfWeek = "mon"; routines.add(new Routine()); break;
            case 2: dayOfWeek = "tue"; routines.add(new Routine()); routines.add(new Routine()); break;
            case 3: dayOfWeek = "wed"; routines.add(new Routine()); routines.add(new Routine()); routines.add(new Routine()); break;
            case 4: dayOfWeek = "thu"; routines.add(new Routine()); routines.add(new Routine()); routines.add(new Routine()); routines.add(new Routine()); break;
            case 5: dayOfWeek = "fri"; routines.add(new Routine()); routines.add(new Routine()); routines.add(new Routine()); routines.add(new Routine()); routines.add(new Routine()); break;
            case 6: dayOfWeek = "sat"; routines.add(new Routine()); routines.add(new Routine()); routines.add(new Routine()); routines.add(new Routine()); routines.add(new Routine()); routines.add(new Routine()); break;
        }

        setRecyclerView();

        addRoutineBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), CreateRoutineActivity.class);
            intent.putExtra("dayOfWeek", day_of_week);
            startActivityForResult(intent, REQUEST_CODE);
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && data != null) {
//            Routine r = data.getSerializableExtra("result");
            String result = (String) data.getSerializableExtra("result");
            routines.add(new Routine());
            adapter.notifyDataSetChanged();

            Log.d("Test", result);
        }
    } // startActivityForResult로 실행한 액티비티의 반환값을 전달받는 메서드

    private void setRecyclerView() {
        adapter = new RoutineAdapter(routines, getContext()); // 나중에 routine
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

//        if (adapter != null) {
//            adapter.setOnExerciseClickListener(new setExerciseAdapter.OnExerciseClick() {
//                @Override
//                public void onExerciseClick(int postion) {
//                    deleteExercise(postion);
//                    adapter.removeItem(postion);
//                    adapter.notifyDataSetChanged();
//
////                    saveRoutine(routine.getExercises().get(postion));
//                }
//            });
//        }
    }


}