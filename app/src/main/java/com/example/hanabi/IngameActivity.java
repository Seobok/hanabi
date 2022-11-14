package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    card_info[] left_card = new card_info[5] ;
    card_info[] right_card = new card_info[5] ;
    card_info[] new_card = new card_info[5] ;
    card_info[] my_card = new card_info[5] ;

    ImageView left_image[] = new ImageView[5] ;
    Integer[] Rid_left = { R.id.l1_image , R.id.l2_image , R.id.l3_image , R.id.l4_image , R.id.l5_image } ;

    ImageView right_image[] = new ImageView[5] ;
    Integer[] Rid_right = { R.id.r1_image , R.id.r2_image , R.id.r3_image , R.id.r4_image , R.id.r5_image } ;

    ImageView my_image[] = new ImageView[5] ;
    Integer[] Rid_my = { R.id.m1_image , R.id.m2_image , R.id.m3_image , R.id.m4_image , R.id.m5_image } ;

    Button step1_info, step1_submission;
    Button step2_throw, step2_sub, step2_left_player, step2_right_player;
    Button step3_number_1, step3_number_2, step3_number_3, step3_number_4, step3_number_5;
    Button step4_color, step4_number;
    Button.OnClickListener step1_O, step_number;


    Boolean step4_check;
    int card_num, player, color_or_number;
    int sub_or_throw ;
    String temp_string ;

    LinearLayout step1, step2_submission, step2_info, step4, step3_number;

    // 0 : red , 1 : blue , 2 : white , 3 : yellow , 4 : green
    String[] color_array = { "red_" , "blue_" , "white_" , "yellow_" , "green_" } ;


    public void CARD_THROW(int card_num) { // 내 카드 버리기


    }

    public void CARD_SUB( int card_num ) { // 내 카드 제출


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temp_string = color_array[ 1 ] + "1" ;

        for( int i = 0 ; i <  5 ; i ++ ) {

            my_card[ i ] = new card_info() ;
            right_card[ i ] = new card_info() ;
            left_card[ i ] = new card_info() ;
            new_card[ i ] = new card_info() ;
            left_image[ i ] = (ImageView) findViewById(Rid_left[ i ] ) ;
            right_image[ i ] = (ImageView) findViewById(Rid_right[ i ] ) ;
            my_image[ i ] = (ImageView) findViewById(Rid_my[ i ] ) ;

        }


        left_card[ 0 ].Set( 1 , 2 ) ;
        right_card[ 0 ].Set( 1 , 2 ) ;
        my_card[ 0 ].Set( 3 , 1 ) ;


        temp_string = color_array[ left_card[ 0 ].Color() ] + left_card[ 0 ].Number() ;
        int resID = getResId( temp_string , R.drawable.class); // or other resource class
        left_image[ 0 ].setImageResource( resID ) ;

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
                        card_num = player = color_or_number = -1 ;
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

                }
                else if( sub_or_throw == 1 ) { // 카드 제출

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

    }

}

class card_info {

    private int color = 0; // 0 : red , 1 : blue , 2 : white , 3 : yellow , 4 : green
    private int number = 0;

    public void Set(int a, int b) {
        color = a;
        number = b;
    }

    public int Color() {
        return color ;
    }

    public int Number() {
        return number ;
    }

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

