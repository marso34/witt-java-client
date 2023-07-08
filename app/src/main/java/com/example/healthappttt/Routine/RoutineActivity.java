package com.example.healthappttt.Routine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.healthappttt.R;
import com.example.healthappttt.databinding.ActivityRoutineBinding;

public class RoutineActivity extends AppCompatActivity {
    ActivityRoutineBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoutineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        int code = intent.getIntExtra("code", 285); // 프리페런스헬퍼로 디폴트값 넣기

        Bundle bundle = new Bundle();
        bundle.putSerializable("code", code);
        replaceFragment(new RoutineFragment(), new Bundle());
    }

    private void replaceFragment (Fragment fragment, Bundle bundle){ //프래그먼트 설정
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if( fragment.isAdded() )
        {
            // Fragment 가 이미 추가되어 있으면 삭제한 후, 새로운 Fragment 를 생성한다.
            // 새로운 Fragment 를 생성하지 않으면 2번째 보여질 때에 Fragment 가 보여지지 않는 것 같습니다.
            fragmentTransaction.remove( fragment );
            fragment = new Fragment();
        }

        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}