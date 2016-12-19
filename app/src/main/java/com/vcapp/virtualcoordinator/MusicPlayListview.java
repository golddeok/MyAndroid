package com.vcapp.virtualcoordinator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MusicPlayListview extends AppCompatActivity {

    ArrayList<Song> al = new ArrayList<Song>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play_listview);

        // 인기가요 순위 데이터 (다량의 데이터 준비)
        al.add(new Song("연결고리",R.drawable.bobby,"Bobby"));
        al.add(new Song("아름다워 (Feat. 지코)",R.drawable.cjam,"씨잼"));
        al.add(new Song("널 사랑하지 않아",R.drawable.aban,"어반자카파"));
//        al.add(new Song("걱정말아요 그대",R.drawable.img04,"이적"));
//        al.add(new Song("그날을 내 등 뒤로",R.drawable.img05,"허각"));
//        al.add(new Song("청춘 (feat. 김창완)",R.drawable.img06,"김필(Kim Feel"));
//        al.add(new Song("Hot Pink",R.drawable.img07,"EXID(이엑스아이디)"));
//        al.add(new Song("널 생각해",R.drawable.img08,"윤하(Younha/ユンナ)"));
//        al.add(new Song("사랑은 가슴이 시킨다 Part.3",R.drawable.img09,"버즈(Buzz)"));
//        al.add(new Song("또 다시 사랑",R.drawable.img10,"임창정"));


        MyAdapter adapter = new MyAdapter(
                getApplicationContext(), // 현재화면의 제어권자
                R.layout.music_items,  // 리스트뷰의 한행의 레이아웃
                al);         // 데이터

        ListView lv = (ListView)findViewById(R.id.listView1);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 상세정보 화면으로 이동하기(인텐트 날리기)
                // 1. 다음화면을 만든다
                // 2. AndroidManifest.xml 에 화면을 등록한다
                // 3. Intent 객체를 생성하여 날린다
                Intent intent = new Intent(
                        getApplicationContext(), // 현재화면의 제어권자
                        MusicDetail.class); // 다음넘어갈 화면

                // intent 객체에 데이터를 실어서 보내기
                // 리스트뷰 클릭시 인텐트 (Intent) 생성하고 position 값을 이용하여 인텐트로 넘길값들을 넘긴다
                intent.putExtra("title", al.get(position).title);
                intent.putExtra("img", al.get(position).img);
                intent.putExtra("artist", al.get(position).artist);

                startActivity(intent);
            }
        });
    }
}

class MyAdapter extends BaseAdapter { // 리스트 뷰의 아답타
    Context context;
    int layout;
    ArrayList<Song> al;
    LayoutInflater inf;
    public MyAdapter(Context context, int layout, ArrayList<Song> al) {
        this.context = context;
        this.layout = layout;
        this.al = al;
        inf = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return al.size();
    }
    @Override
    public Object getItem(int position) {
        return al.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null) {
            convertView = inf.inflate(layout, null);
        }
        ImageView iv = (ImageView)convertView.findViewById(R.id.imageView1);
        TextView tvName = (TextView)convertView.findViewById(R.id.textView1);
        TextView tvInfo = (TextView)convertView.findViewById(R.id.textView2);

        Song m = al.get(position);
        iv.setImageResource(m.img);
        tvName.setText(m.title);
        tvInfo.setText(m.artist);

        return convertView;
    }
}

class Song { // 자바빈
    String title = ""; // 곡 title
    int img; // 앨범 이미지
    String artist = ""; // artist
    public Song(String title, int img, String artist) {
        super();
        this.title = title;
        this.img = img;
        this.artist = artist;
    }
    public Song() {}
}
