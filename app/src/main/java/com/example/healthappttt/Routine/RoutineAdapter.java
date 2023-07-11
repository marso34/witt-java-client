package com.example.healthappttt.Routine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Exercise.ExerciseData;
import com.example.healthappttt.Data.Exercise.ExerciseComparator;
import com.example.healthappttt.Data.Exercise.RoutineData;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.pkData;
import com.example.healthappttt.R;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.MainViewHolder> {
    private Context context;

    private ServiceApi service;
    private SQLiteUtil sqLiteUtil;

    private ArrayList<RoutineData> routines;
    private int attribute; // == 0 내 루틴, attribute > 0 운동 기록할 때 선택용, attribute < 0 남의 루틴

    private OnClickRoutine onClickRoutine;

    /**
     * @attribute attribute(운동 기록) > 0  or attribute == 0(내 루틴) or attribute < 0(다른 사람 루틴)
     */
    public RoutineAdapter(Context context, ArrayList<RoutineData> routines, int attribute) {
        this.routines = routines;
        this.context = context;
        this.attribute = attribute;

        service = RetrofitClient.getClient().create(ServiceApi.class);
        sqLiteUtil = SQLiteUtil.getInstance();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout RoutineLayout, ClickLayout, NullLayout;
        public TextView startTimeView, endTimeView;
        public ImageView EditBtn;
        public RecyclerView recyclerView;
        public ExerciseAdapter adapter;

        public MainViewHolder(View view) {
            super(view);

            this.RoutineLayout = view.findViewById(R.id.routineLayout);
            this.ClickLayout = view.findViewById(R.id.clickLayout);
            this.NullLayout = view.findViewById(R.id.nullLayout);

            this.startTimeView = view.findViewById(R.id.startTime);
            this.endTimeView = view.findViewById(R.id.endTime);
            this.EditBtn = view.findViewById(R.id.editBtn);

            this.recyclerView = view.findViewById(R.id.recyclerView);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_routine, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);


        mainViewHolder.ClickLayout.setOnClickListener(v -> {
            int position = mainViewHolder.getAbsoluteAdapterPosition();

            if (attribute > 0)
                onClickRoutine.onClickRoutine(routines.get(position));
        });

        mainViewHolder.EditBtn.setOnClickListener(v -> {
            int position = mainViewHolder.getAbsoluteAdapterPosition();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View dialogView = LayoutInflater.from(parent.getContext()).inflate(R.layout.routine_edit_popup, null);

            Button e = dialogView.findViewById(R.id.edit);
            Button copyBtn = dialogView.findViewById(R.id.copy);
            Button deleteBtn = dialogView.findViewById(R.id.delete);

            builder.setView(dialogView);
            AlertDialog alertDialog  = builder.create();
            alertDialog.getWindow().setGravity(Gravity.BOTTOM);   //다이얼로그 하단
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            e.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = mainViewHolder.getAbsoluteAdapterPosition();
                    onClickRoutine.onClickRoutine(routines.get(position));
                    alertDialog.dismiss();
                }
            });

            copyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "추후 업데이트 예정입니다.", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteToDB(position); // "예" 클릭시 삭제
                    alertDialog.dismiss();
                }
            });
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        if (routines.size() > 0) {
            holder.RoutineLayout.setVisibility(View.VISIBLE);
            holder.NullLayout.setVisibility(View.GONE);
            holder.ClickLayout.setVisibility(View.GONE);

            holder.startTimeView.setText(TimeToString(routines.get(position).getStartTime()));
            holder.endTimeView.setText(TimeToString(routines.get(position).getEndTime()));

            if (attribute == 0) { // 내 루틴

            } else if (attribute > 0) { // 운동 기록
                holder.ClickLayout.setVisibility(View.VISIBLE);
                holder.EditBtn.setVisibility(View.GONE);
            } else { // 다른 사람 루틴
                holder.EditBtn.setVisibility(View.GONE);
            }

            setRecyclerView(holder.recyclerView, holder.adapter, position);

        } else {
            holder.RoutineLayout.setVisibility(View.GONE);
            holder.NullLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (routines.size() == 0)   return 1;

        return routines.size();
    }

    private void DeleteToDB(int position) {
        Log.d("루틴 포지션", position + "");

        Log.d("루틴 아이디", routines.get(position).getID() + "");

        service.deleteRoutine(new pkData(routines.get(position).getID())).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful() && response.body() == 200) {
                    Log.d("성공", "루틴 삭제 성공");
                    DeleteToDev(position);
                    removeItem(position);
//                    Terminate(true, 2); // 루틴 삭제를 의미
                } else {
                    Toast.makeText(context, "루틴 삭제 실패", Toast.LENGTH_SHORT).show();
                    Log.d("실패", "루틴 삭제 실패");
//                    Terminate(false);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(context, "루틴 삭제 실패", Toast.LENGTH_SHORT).show();
                Log.d("실패", t.getMessage());
//                Terminate(false);
            }
        });
    }

    private void DeleteToDev(int position) {
        sqLiteUtil.setInitView(context, "RT_TB");
        sqLiteUtil.delete(routines.get(position).getID());
    }

    public void removeItem(int position) {
        routines.remove(position);
        notifyItemRemoved(position);
    }

    private String TimeToString(String Time) {
        String[] TimeSplit = Time.split(":");
        String am_pm = "";

        int hour = Integer.parseInt(TimeSplit[0]);

        if (hour < 12) {
            am_pm = "오전";
            if (hour == 0) hour = 12;
        } else {
            am_pm = "오후";
            if (hour >= 13) hour-= 12;
        }

        return am_pm + " " + hour + ":" + TimeSplit[1];
    }

    private void setRecyclerView(RecyclerView recyclerView, ExerciseAdapter adapter, int positon) {
        ArrayList<ExerciseData> e = (ArrayList<ExerciseData>) routines.get(positon).getExercises();
        Collections.sort(e, new ExerciseComparator());

        adapter = new ExerciseAdapter(e); // 나중에 routine
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    public void setOnClickRoutineListener(OnClickRoutine onClickRoutineListener) {
        this.onClickRoutine = onClickRoutineListener;
    } // 액티비티에서 콜백 메서드를 set

    public interface OnClickRoutine {
        void onClickRoutine(RoutineData r);
    } // 운동 클릭했을 때, 엑티비티에 값 전달을 위한 인터페이스
}
