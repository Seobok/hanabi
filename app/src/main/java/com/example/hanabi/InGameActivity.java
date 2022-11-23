package com.example.hanabi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.HashMap;

public class InGameActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference userRef, boardRef, roomRef;

    String id;
    String roomID;

    Card[] cardList = new Card[50];

    card_info[] left_card = new card_info[5] ;
    card_info[] right_card = new card_info[5] ;
    card_info[] new_card = new card_info[5] ;
    card_info[] my_card = new card_info[5] ;
    card_info[] score_deck = new card_info[5] ;

    TextView dump_green[] = new TextView[10] ;
    Integer[] Rid_dump_green = { R.id.dump_green_1_1 , R.id.dump_green_1_2 , R.id.dump_green_1_3 , R.id.dump_green_2_1 , R.id.dump_green_2_2 , R.id.dump_green_3_1 , R.id.dump_green_3_2 , R.id.dump_green_4_1 , R.id.dump_green_4_2 , R.id.dump_green_5_1 } ;

    TextView dump_red[] = new TextView[10] ;
    Integer[] Rid_dump_red = { R.id.dump_red_1_1 , R.id.dump_red_1_2 , R.id.dump_red_1_3 , R.id.dump_red_2_1 , R.id.dump_red_2_2 , R.id.dump_red_3_1 , R.id.dump_red_3_2 , R.id.dump_red_4_1 , R.id.dump_red_4_2 , R.id.dump_red_5_1 } ;

    TextView dump_white[] = new TextView[10] ;
    Integer[] Rid_dump_white = { R.id.dump_white_1_1 , R.id.dump_white_1_2 , R.id.dump_white_1_3 , R.id.dump_white_2_1 , R.id.dump_white_2_2 , R.id.dump_white_3_1 , R.id.dump_white_3_2 , R.id.dump_white_4_1 , R.id.dump_white_4_2 , R.id.dump_white_5_1 } ;

    TextView dump_blue[] = new TextView[10] ;
    Integer[] Rid_dump_blue = { R.id.dump_blue_1_1 , R.id.dump_blue_1_2 , R.id.dump_blue_1_3 , R.id.dump_blue_2_1 , R.id.dump_blue_2_2 , R.id.dump_blue_3_1 , R.id.dump_blue_3_2 , R.id.dump_blue_4_1 , R.id.dump_blue_4_2 , R.id.dump_blue_5_1 } ;

    TextView dump_y[] = new TextView[10] ;
    Integer[] Rid_dump_y = { R.id.dump_y_1_1 , R.id.dump_y_1_2 , R.id.dump_y_1_3 , R.id.dump_y_2_1 , R.id.dump_y_2_2 , R.id.dump_y_3_1 , R.id.dump_y_3_2 , R.id.dump_y_4_1 , R.id.dump_y_4_2 , R.id.dump_y_5_1 } ;


    ImageView left_image[] = new ImageView[5] ;
    Integer[] Rid_left = { R.id.l1_image , R.id.l2_image , R.id.l3_image , R.id.l4_image , R.id.l5_image } ;

    TextView l_number[] = new TextView[5] ;
    Integer[] Rid_l_n = { R.id.l1_number , R.id.l2_number , R.id.l3_number , R.id.l4_number , R.id.l5_number } ;

    ImageView right_image[] = new ImageView[5] ;
    Integer[] Rid_right = { R.id.r1_image , R.id.r2_image , R.id.r3_image , R.id.r4_image , R.id.r5_image } ;

    TextView r_number[] = new TextView[5] ;
    Integer[] Rid_r_n = { R.id.r1_number , R.id.r2_number , R.id.r3_number , R.id.r4_number , R.id.r5_number } ;

    ImageView my_image[] = new ImageView[5] ;
    Integer[] Rid_my = { R.id.m1_image , R.id.m2_image , R.id.m3_image , R.id.m4_image , R.id.m5_image } ;

    TextView m_number[] = new TextView[5] ;
    Integer[] Rid_m_n = { R.id.m1_number , R.id.m2_number , R.id.m3_number , R.id.m4_number , R.id.m5_number } ;

    ImageView score_image[] = new ImageView[5] ;
    Integer[] Rid_score = { R.id.s1_image , R.id.s2_image , R.id.s3_image , R.id.s4_image , R.id.s5_image } ;

    TextView score_text ;

    Button step1_info, step1_submission;
    Button step2_throw, step2_sub, step2_left_player, step2_right_player;
    Button step3_number_1, step3_number_2, step3_number_3, step3_number_4, step3_number_5;
    Button step4_color, step4_number;
    Button.OnClickListener step1_O, step_number , step4_0 ;


    Boolean step4_check;

    int life = 3 ;
    int card_num, player ;
    // player left : 1 , right : 2
    int sub_or_throw ;
    int left_player , right_player , my_id ;
    String temp_string ;

    LinearLayout step1, step2_submission, step2_info, step4, step3_number;

    // 0 : red , 1 : blue , 2 : white , 3 : yellow , 4 : green 5 : ~
    String[] color_array = { "red_" , "blue_" , "white_" , "yellow_" , "green_" , "card" } ;

    public void CARD_THROW( ) { // 내 카드 버리기


    }

    public void CARD_SUB( ) { // 내 카드 제출

        card_info sub_card = my_card[ card_num ] ;

        int color = sub_card.Color() ;
        int number = sub_card.Number() ;

        if( score_deck[ color ].Number() + 1 == number ) { // O
            score_deck[ color ].Set( color , number + 1 );

            String temp_string = color_array[ score_deck[ color ].Color() ] + score_deck[ color ].Number() ;
            int resID = getResId( temp_string , R.drawable.class); // or other resource class
            score_image[ color ].setImageResource( resID ) ;

            Integer score = 0 ;
            for( int i = 0 ; i < 5 ; i ++ ) score += score_deck[ i ].Number() ;
            score_text.setText( score.toString() );

        }
        else { // X

            life -- ;
            CARD_THROW() ;

        }

    }

    public void INFO_COLOR() { // 색 정보

        // 0 : red , 1 : blue , 2 : white , 3 : yellow , 4 : green 5 : ~
        if( player == 1 ){ // left

            int i ;
            int set_color = left_card[ card_num ].Color() ;

            if( set_color == 0 ) for( i = 0 ; i < 5 ; i ++ ) if( left_card[ i ].Color() == set_color ) l_number[ i ].setBackgroundColor( Color.RED ) ;
            else if( set_color == 1 ) for( i = 0 ; i < 5 ; i ++ ) if( left_card[ i ].Color() == set_color ) l_number[ i ].setBackgroundColor( Color.BLUE ) ;
            else if( set_color == 2 ) for( i = 0 ; i < 5 ; i ++ ) if( left_card[ i ].Color() == set_color ) l_number[ i ].setBackgroundColor( Color.WHITE ) ;
            else if( set_color == 3 ) for( i = 0 ; i < 5 ; i ++ ) if( left_card[ i ].Color() == set_color ) l_number[ i ].setBackgroundColor( Color.YELLOW ) ;
            else if( set_color == 4 ) for( i = 0 ; i < 5 ; i ++ ) if( left_card[ i ].Color() == set_color ) l_number[ i ].setBackgroundColor( Color.GREEN ) ;

        }
        else { // right

            int i ;
            int set_color = right_card[ card_num ].Color() ;

            if( set_color == 0 ) for( i = 0 ; i < 5 ; i ++ ) if( right_card[ i ].Color() == set_color ) r_number[ i ].setBackgroundColor( Color.RED ) ;
            else if( set_color == 1 ) for( i = 0 ; i < 5 ; i ++ ) if( right_card[ i ].Color() == set_color ) r_number[ i ].setBackgroundColor( Color.BLUE ) ;
            else if( set_color == 2 ) for( i = 0 ; i < 5 ; i ++ ) if( right_card[ i ].Color() == set_color ) r_number[ i ].setBackgroundColor( Color.WHITE ) ;
            else if( set_color == 3 ) for( i = 0 ; i < 5 ; i ++ ) if( right_card[ i ].Color() == set_color ) r_number[ i ].setBackgroundColor( Color.YELLOW ) ;
            else if( set_color == 4 ) for( i = 0 ; i < 5 ; i ++ ) if( right_card[ i ].Color() == set_color ) r_number[ i ].setBackgroundColor( Color.GREEN ) ;

        }

    }

    public void INFO_NUMBER() { // 숫자 정보

        if( player == 1 ){ // left

            int i ;
            Integer set_number = left_card[ card_num ].Number() ;

            for( i = 0 ; i < 5 ; i ++ ) if( set_number == left_card[ i ].Number() ) l_number[ i ].setText( set_number.toString() );

        }
        else { // right

            int i ;
            Integer set_number = right_card[ card_num ].Number() ;

            for( i = 0 ; i < 5 ; i ++ ) if( set_number == right_card[ i ].Number() ) r_number[ i ].setText( set_number.toString() );

        }


    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

//    int resID = getResId( temp_string , R.drawable.class); // or other resource class

    public void initCardList() {
        for(int i=0;i<50;i++)
        {
            cardList[i] = new Card();

            String color;
            if(i<10)
                color = "red";
            else if (i<20)
                color = "blue";
            else if (i<30)
                color = "white";
            else if (i<40)
                color = "yellow";
            else
                color = "green";
            cardList[i].color = color;

            String number;
            if(0 <= (i%10) && (i%10) <= 2)
                number = "1";
            else if (3 <= (i%10) && (i%10) <= 4)
                number = "2";
            else if (5 <= (i%10) && (i%10) <= 6)
                number = "3";
            else if (7 <= (i%10) && (i%10) <= 8)
                number = "4";
            else
                number = "5";
            cardList[i].number = number ;
            cardList[i].position = "deck";
            cardList[i].handPosition = "";
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.in_game);

        // <-- firebase initialize
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance("https://hanabi-ea7e9-default-rtdb.asia-southeast1.firebasedatabase.app/");
        roomID = getIntent().getStringExtra("RoomNum");
        roomRef = firebaseDatabase.getReference("Room").child(roomID);
        userRef = roomRef.child("User");
        boardRef = roomRef.child("Board");
        // -->


        //<-- get current player id
        String[] email = firebaseUser.getEmail().split("@",2);
        id = email[0];
        //-->

        initCardList();

        // id
        if( my_id == 1 ){
            right_player = 3 ;
            left_player = 2 ;
        }
        else if( my_id == 2 ){
            left_player = 3 ;
            right_player = 1 ;
        }
        else {
            left_player = 1 ;
            right_player = 2 ;
        }
        //

        boardRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Card card = snapshot.getValue(Card.class);

                cardList[Integer.parseInt(snapshot.getKey())] = card;
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        for( int i = 0 ; i < 10 ; i ++ ) {

            dump_blue[ i ] = (TextView) findViewById(Rid_dump_blue[ i ]) ;
            dump_blue[ i ].setTextColor( Color.BLUE );
            dump_green[ i ] = (TextView) findViewById(Rid_dump_green[ i ]) ;
            dump_green[ i ].setTextColor( Color.GREEN );
            dump_red[ i ] = (TextView) findViewById(Rid_dump_red[ i ]) ;
            dump_red[ i ].setTextColor( Color.RED );
            dump_y[ i ] = (TextView) findViewById(Rid_dump_y[ i ]) ;
            dump_y[ i ].setTextColor( Color.YELLOW );
            dump_white[ i ] = (TextView) findViewById(Rid_dump_white[ i ]) ;
            dump_white[ i ].setTextColor( Color.WHITE );

        }

        for( int i = 0 ; i <  5 ; i ++ ) {

            my_card[ i ] = new card_info() ;
            right_card[ i ] = new card_info() ;
            left_card[ i ] = new card_info() ;

            left_image[ i ] = (ImageView) findViewById(Rid_left[ i ] ) ;
            l_number[ i ] = (TextView) findViewById(Rid_l_n[ i ] ) ;

            right_image[ i ] = (ImageView) findViewById(Rid_right[ i ] ) ;
            r_number[ i ] = (TextView) findViewById(Rid_r_n[ i ] ) ;

            my_image[ i ] = (ImageView) findViewById(Rid_my[ i ] ) ;
            m_number[ i ] = (TextView) findViewById(Rid_m_n[ i ] ) ;
            my_image[ i ].setImageResource( R.drawable.card ) ;

            score_image[ i ] = (ImageView) findViewById(Rid_score[ i ] ) ;

        }

        temp_string = color_array[ left_card[ 0 ].Color() ] + left_card[ 0 ].Number() ;
        int resID = getResId( temp_string , R.drawable.class); // or other resource class
        //left_image[ 0 ].setImageResource( resID );

        score_text = (TextView) findViewById(R.id.score_text) ;

        step1 = (LinearLayout) findViewById(R.id.step1 );
        step2_info = (LinearLayout) findViewById(R.id.step2_info );
        step2_submission = (LinearLayout) findViewById(R.id.step2_submission );
        step3_number = (LinearLayout) findViewById(R.id.step3_number);
        step4 = (LinearLayout) findViewById(R.id.step4 );

        step1_info = (Button) findViewById(R.id.step1_info) ;
        step1_submission = (Button) findViewById(R.id.step1_submission) ;

        step2_throw = (Button) findViewById(R.id.step2_throw) ;
        step2_sub = (Button) findViewById(R.id.step2_sub) ;

        step2_left_player = (Button) findViewById(R.id.step2_left_player ) ;
        step2_right_player = (Button) findViewById(R.id.step2_right_player ) ;

        step3_number_1 = (Button) findViewById(R.id.step3_number_1) ;
        step3_number_2 = (Button) findViewById(R.id.step3_number_2) ;
        step3_number_3 = (Button) findViewById(R.id.step3_number_3) ;
        step3_number_4 = (Button) findViewById(R.id.step3_number_4) ;
        step3_number_5 = (Button) findViewById(R.id.step3_number_5) ;

        step4_color = (Button) findViewById(R.id.step4_color) ;
        step4_number = (Button) findViewById(R.id.step4_number) ;


        step1_O = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.step1_info:
                        step2_info.setVisibility(View.VISIBLE);
                        step1.setVisibility(View.INVISIBLE);
                        card_num = player = -1 ;
                        break;
                    case R.id.step1_submission:
                        step2_submission.setVisibility(View.VISIBLE);
                        step1.setVisibility(View.INVISIBLE);
                        step4_check = false ;
                        break;
                    case R.id.step2_left_player:
                        step3_number.setVisibility(View.VISIBLE) ;
                        step2_info.setVisibility(View.INVISIBLE);
                        step4_check = true ;
                        player = 1 ;
                        break ;
                    case R.id.step2_right_player:
                        step3_number.setVisibility(View.VISIBLE) ;
                        step2_info.setVisibility(View.INVISIBLE);
                        step4_check = true ;
                        player = 2 ;
                        break ;
                    case R.id.step2_throw:
                        step3_number.setVisibility(View.VISIBLE) ;
                        step2_submission.setVisibility(View.INVISIBLE);
                        sub_or_throw = -1 ; // sub_or_throw = -1 : 버림
                        break ;
                    case R.id.step2_sub:
                        step3_number.setVisibility(View.VISIBLE) ;
                        step2_submission.setVisibility(View.INVISIBLE);
                        sub_or_throw = 1 ;
                        break ;
                }

            }
        };

        step_number = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                step3_number.setVisibility(View.INVISIBLE) ;
                switch (view.getId()) {
                    case R.id.step3_number_1:
                        card_num = 1 ;
                        break ;
                    case R.id.step3_number_2:
                        card_num = 2 ;
                        break ;
                    case R.id.step3_number_3:
                        card_num = 3 ;
                        break ;
                    case R.id.step3_number_4:
                        card_num = 4 ;
                        break ;
                    case R.id.step3_number_5:
                        card_num = 5 ;
                        break ;
                }

                if( step4_check == true )
                    step4.setVisibility(View.VISIBLE);
                else if( sub_or_throw == -1 ) { // 카드 버림

                    CARD_THROW();

                }
                else if( sub_or_throw == 1 ) { // 카드 제출

                    CARD_SUB() ;
                }

            }
        };

        step4_0 = new View.OnClickListener(){

            public void onClick(View view) {

                step4.setVisibility(View.INVISIBLE);
                switch (view.getId()) {
                    case R.id.step4_color:
                        INFO_COLOR();
                        break;
                    case R.id.step4_number:
                        INFO_NUMBER();
                        break;
                    }
            }

        };

        step1_info.setOnClickListener(step1_O);
        step1_submission.setOnClickListener(step1_O);

        step2_throw.setOnClickListener(step1_O) ;
        step2_sub.setOnClickListener(step1_O) ;

        step2_left_player.setOnClickListener(step1_O) ;
        step2_right_player.setOnClickListener(step1_O) ;

        step3_number_1.setOnClickListener(step_number) ;
        step3_number_2.setOnClickListener(step_number) ;
        step3_number_3.setOnClickListener(step_number) ;
        step3_number_4.setOnClickListener(step_number) ;
        step3_number_5.setOnClickListener(step_number) ;
        step4_color.setOnClickListener(step4_0);
        step4_number.setOnClickListener(step4_0);

    }

}

class card_info {

    private int color = 0; // 0 : red , 1 : blue , 2 : white , 3 : yellow , 4 : green
    private int number = 0;
    private int idx ;

    public void set_idx( int Idx ) { idx = Idx ; }

    public void Set( int Color , int Number ) {
        color = Color ;
        number = Number ;
    }

    public int Color() {
        return color ;
    }

    public int Number() {
        return number ;
    }

    public int Idx(){ return idx ; }

}

/*

step1   턴시작시
step1_sumission 카드 제출
step1_info 카드정보


step2_submission
step2_throw 카드버리기
step2_sub 카드 제출

step2_info
step2_left_player 왼쪽
step2_right_player 오른쪽

step3_number
step3_number_1
step3_number_2
step3_number_3
step3_number_4
step3_number_5

step4 // step1_info 만
step4_color 색
step4_number 숫자

1. 주어진 정보로 색,번호에 대한 표시해주는것을 로컬로 해야하는지 or 이것도 db써야하는지?
2. 각각의 기기에서 카드정보 저장 or db에서 매턴마다 받아옴?
3. string 변수로 setimageresource 사용할수 있나?  - 해결

 */

