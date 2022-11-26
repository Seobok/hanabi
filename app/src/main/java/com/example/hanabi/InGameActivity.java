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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;

public class InGameActivity extends AppCompatActivity {

    private static final String TAG = "InGameActivity";

    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    DatabaseReference userRef, boardRef, roomRef, logRef, lifeRef;

    String id;
    String roomID;

    User user;

    Card[] cardList = new Card[50];

    int[][] hand_card = new int[3][5];
    card_info[] score_deck = new card_info[5];

    TextView dump_green[] = new TextView[10];
    Integer[] Rid_dump_green = {R.id.dump_green_1_1, R.id.dump_green_1_2, R.id.dump_green_1_3, R.id.dump_green_2_1, R.id.dump_green_2_2, R.id.dump_green_3_1, R.id.dump_green_3_2, R.id.dump_green_4_1, R.id.dump_green_4_2, R.id.dump_green_5_1};

    TextView dump_red[] = new TextView[10];
    Integer[] Rid_dump_red = {R.id.dump_red_1_1, R.id.dump_red_1_2, R.id.dump_red_1_3, R.id.dump_red_2_1, R.id.dump_red_2_2, R.id.dump_red_3_1, R.id.dump_red_3_2, R.id.dump_red_4_1, R.id.dump_red_4_2, R.id.dump_red_5_1};

    TextView dump_white[] = new TextView[10];
    Integer[] Rid_dump_white = {R.id.dump_white_1_1, R.id.dump_white_1_2, R.id.dump_white_1_3, R.id.dump_white_2_1, R.id.dump_white_2_2, R.id.dump_white_3_1, R.id.dump_white_3_2, R.id.dump_white_4_1, R.id.dump_white_4_2, R.id.dump_white_5_1};

    TextView dump_blue[] = new TextView[10];
    Integer[] Rid_dump_blue = {R.id.dump_blue_1_1, R.id.dump_blue_1_2, R.id.dump_blue_1_3, R.id.dump_blue_2_1, R.id.dump_blue_2_2, R.id.dump_blue_3_1, R.id.dump_blue_3_2, R.id.dump_blue_4_1, R.id.dump_blue_4_2, R.id.dump_blue_5_1};

    TextView dump_y[] = new TextView[10];
    Integer[] Rid_dump_y = {R.id.dump_y_1_1, R.id.dump_y_1_2, R.id.dump_y_1_3, R.id.dump_y_2_1, R.id.dump_y_2_2, R.id.dump_y_3_1, R.id.dump_y_3_2, R.id.dump_y_4_1, R.id.dump_y_4_2, R.id.dump_y_5_1};

    ImageView left_image[] = new ImageView[5];
    Integer[] Rid_left = {R.id.l1_image, R.id.l2_image, R.id.l3_image, R.id.l4_image, R.id.l5_image};

    TextView l_number[] = new TextView[5];
    Integer[] Rid_l_n = {R.id.l1_number, R.id.l2_number, R.id.l3_number, R.id.l4_number, R.id.l5_number};

    ImageView right_image[] = new ImageView[5];
    Integer[] Rid_right = {R.id.r1_image, R.id.r2_image, R.id.r3_image, R.id.r4_image, R.id.r5_image};

    TextView r_number[] = new TextView[5];
    Integer[] Rid_r_n = {R.id.r1_number, R.id.r2_number, R.id.r3_number, R.id.r4_number, R.id.r5_number};

    ImageView my_image[] = new ImageView[5];
    Integer[] Rid_my = {R.id.m1_image, R.id.m2_image, R.id.m3_image, R.id.m4_image, R.id.m5_image};

    TextView m_number[] = new TextView[5];
    Integer[] Rid_m_n = {R.id.m1_number, R.id.m2_number, R.id.m3_number, R.id.m4_number, R.id.m5_number};

    ImageView score_image[] = new ImageView[5];
    Integer[] Rid_score = {R.id.s1_image, R.id.s2_image, R.id.s3_image, R.id.s4_image, R.id.s5_image};

    TextView score_text;

    Button step1_info, step1_submission;
    Button step2_throw, step2_sub, step2_left_player, step2_right_player;
    Button step3_number_1, step3_number_2, step3_number_3, step3_number_4, step3_number_5;
    Button step4_color, step4_number;
    Button.OnClickListener step1_O, step_number, step4_0;

    Boolean step4_check;

    int life = 3;
    int card_num, player;
    // player left : 1 , right : 2
    int sub_or_throw;
    int left_player, right_player, my_id;

    LinearLayout step1, step2_submission, step2_info, step4, step3_number;

    // 0 : red , 1 : blue , 2 : white , 3 : yellow , 4 : green 5 : ~
    String[] color_array = {"red_", "blue_", "white_", "yellow_", "green_", "card"};

    public void CARD_PAINT( int card_id ) {

        String temp_string ;

        String temp = cardList[ card_id ].position ;
        Card now = cardList[ card_id ] ;
        int color = Integer.parseInt( now.color ) ;
        int number = Integer.parseInt( now.number ) ;

        if( temp.equals("submit")) {

            score_deck[ color ].Set( color , number );

            Integer score = 0 ;
            for( int i = 0 ; i < 5 ; i ++ ) score += score_deck[ i ].Number() ;
            score_text.setText( score.toString() );

            temp_string = color_array[ score_deck[ color ].Color() ] + score_deck[ color ].Number() ;
            int resID = getResId( temp_string , R.drawable.class); // or other resource class
            score_image[ color ].setImageResource( resID ) ;

        }
        else if ( temp.equals("dump") ) {

        }
        else {

            int player_id = Integer.parseInt( temp ) ;
            int h_P = Integer.parseInt( now.handPosition ) ;

            temp_string = color_array[ color ] + Integer.toString( number ) ;

            int resID = getResId(temp_string, R.drawable.class); // or other resource class
            hand_card[ player_id ][ h_P ] = ( card_id ) ;

            if( player_id == my_id ){
                my_image[ h_P ].setImageResource(resID);
                m_number[ h_P ].setText( temp_string );
            }
            else if( player_id == ( my_id + 1 ) % 3 ){
                left_image[ h_P ].setImageResource(resID);
                l_number[ h_P ].setText( temp_string );
            }
            else{
                right_image[ h_P ].setImageResource(resID);
                r_number[ h_P ].setText( temp_string );
            }

        }

    }

    public void INFO_PAINT_NUMBER( int player_id , int card_id ) {

        int i ;
        Integer set_number = Integer.parseInt( cardList[ card_id ].number ) ;

        if( player_id == my_id ) for( i = 0 ; i < 5 ; i ++ ) { if( set_number == Integer.parseInt( cardList[ hand_card[ my_id ][ i ] ].number ) ) m_number[ i ].setText( set_number.toString() ) ; }
        else if( player_id == ( ( my_id ) + 1 ) % 3 ) for( i = 0 ; i < 5 ; i ++ ) { if( set_number == Integer.parseInt( cardList[ hand_card[ left_player ][ i ] ].number ) ) l_number[ i ].setText( set_number.toString() ) ;}
        else for( i = 0 ; i < 5 ; i ++ ) { if( set_number == Integer.parseInt( cardList[ hand_card[ right_player ][ i ] ].number ) ) r_number[ i ].setText( set_number.toString() ) ;}

    }

    public void INFO_PAINT_COLOR( int player_id , int card_id ) {

        // 0 : red , 1 : blue , 2 : white , 3 : yellow , 4 : green 5 : ~

            int i ;
            int set_color = Integer.parseInt( cardList[ card_id ].color ) ;

        if( player_id == my_id ){

            if( set_color == 0 ) for( i = 0 ; i < 5 ; i ++ ) { if( Integer.parseInt( cardList[ hand_card[ my_id ][ i ] ].color ) == set_color ) m_number[ i ].setBackgroundColor( Color.RED ) ; }
            else if( set_color == 1 ) for( i = 0 ; i < 5 ; i ++ ) { if( Integer.parseInt( cardList[ hand_card[ my_id ][ i ] ].color ) == set_color ) m_number[ i ].setBackgroundColor( Color.BLUE ) ; }
            else if( set_color == 2 ) for( i = 0 ; i < 5 ; i ++ ) { if( Integer.parseInt( cardList[ hand_card[ my_id ][ i ] ].color ) == set_color ) m_number[ i ].setBackgroundColor( Color.WHITE ) ; }
            else if( set_color == 3 ) for( i = 0 ; i < 5 ; i ++ ) { if( Integer.parseInt( cardList[ hand_card[ my_id ][ i ] ].color ) == set_color ) m_number[ i ].setBackgroundColor( Color.YELLOW ) ; }
            else if( set_color == 4 ) for( i = 0 ; i < 5 ; i ++ ) { if( Integer.parseInt( cardList[ hand_card[ my_id ][ i ] ].color ) == set_color ) m_number[ i ].setBackgroundColor( Color.GREEN ) ; }

        }
        else if( player_id == ( ( my_id ) + 1 ) % 3 ){ // left

            if( set_color == 0 ) for( i = 0 ; i < 5 ; i ++ ) { if( Integer.parseInt( cardList[ hand_card[ left_player ][ i ] ].color ) == set_color ) l_number[ i ].setBackgroundColor( Color.RED ) ; }
            else if( set_color == 1 ) for( i = 0 ; i < 5 ; i ++ ) { if( Integer.parseInt( cardList[ hand_card[ left_player ][ i ] ].color ) == set_color ) l_number[ i ].setBackgroundColor( Color.BLUE ) ; }
            else if( set_color == 2 ) for( i = 0 ; i < 5 ; i ++ ) { if( Integer.parseInt( cardList[ hand_card[ left_player ][ i ] ].color ) == set_color ) l_number[ i ].setBackgroundColor( Color.WHITE ) ; }
            else if( set_color == 3 ) for( i = 0 ; i < 5 ; i ++ ) { if( Integer.parseInt( cardList[ hand_card[ left_player ][ i ] ].color ) == set_color ) l_number[ i ].setBackgroundColor( Color.YELLOW ) ; }
            else if( set_color == 4 ) for( i = 0 ; i < 5 ; i ++ ) { if( Integer.parseInt( cardList[ hand_card[ left_player ][ i ] ].color ) == set_color ) l_number[ i ].setBackgroundColor( Color.GREEN ) ; }

        }
        else { // right

            if (set_color == 0) for (i = 0; i < 5; i++){ if ( Integer.parseInt( cardList[ hand_card[ right_player ][ i ] ].color ) == set_color) r_number[i].setBackgroundColor(Color.RED); }
            else if (set_color == 1) for (i = 0; i < 5; i++) { if ( Integer.parseInt( cardList[ hand_card[ right_player ][ i ] ].color ) == set_color) r_number[i].setBackgroundColor(Color.BLUE); }
            else if (set_color == 2) for (i = 0; i < 5; i++) { if ( Integer.parseInt( cardList[ hand_card[ right_player ][ i ] ].color ) == set_color ) r_number[i].setBackgroundColor(Color.WHITE); }
            else if (set_color == 3) for (i = 0; i < 5; i++) { if ( Integer.parseInt( cardList[ hand_card[ right_player ][ i ] ].color ) == set_color) r_number[i].setBackgroundColor(Color.YELLOW); }
            else if (set_color == 4) for (i = 0; i < 5; i++) { if ( Integer.parseInt( cardList[ hand_card[ right_player ][ i ] ].color ) == set_color) r_number[i].setBackgroundColor(Color.GREEN) ; }

        }

    }

    public void CARD_THROW( ) { // 내 카드 버리기
        drawCard(my_id, card_num);
    }

    public void CARD_SUB( ) { // 내 카드 제출

        Card sub_card = cardList[ hand_card[ my_id ][ card_num ] ] ;

        int color = Integer.parseInt( sub_card.color ) ;
        int number = Integer.parseInt( sub_card.number ) ;

        if( score_deck[ color ].Number() + 1 == number ) { // O

            int now_num = hand_card[ my_id ][ card_num ] ;

            Hashtable<String,String> newCard = new Hashtable<>();
            newCard.put("color",cardList[now_num].color);
            newCard.put("number",cardList[now_num].number);
            newCard.put("position", "submit" );
            newCard.put("handPosition", "" );
            boardRef.child(Integer.toString(now_num)).setValue(newCard);

            cardList[now_num].position = "submit";
            cardList[now_num].handPosition = "" ;

            drawCard(my_id, card_num) ;

        }
        else { // X
            life -- ;
            CARD_THROW() ;

            lifeRef.setValue(life);
        }

    }

    public void INFO_COLOR() { // 색 정보

        int idx;
        // 0 : red , 1 : blue , 2 : white , 3 : yellow , 4 : green 5 : ~
        if( player == 1 )idx = hand_card[ left_player ][ card_num ] ;
        else idx = hand_card[ right_player ][ card_num ] ;

        Hashtable<String,String> newLog = new Hashtable<>();
        newLog.put("logType","hint");
        newLog.put("hintUser",Integer.toString((my_id+player)%3));
        newLog.put("cardID", Integer.toString(idx));
        newLog.put("hintType","color");
        logRef.setValue(newLog);

    }

    public void INFO_NUMBER() { // 숫자 정보

        int idx ;

        if( player == 1 ) idx = hand_card[ left_player ][ card_num ] ;
        else idx = hand_card[ right_player ][ card_num ] ;

        Hashtable<String,String> newLog = new Hashtable<>();
        newLog.put("logType","hint");
        newLog.put("hintUser",Integer.toString((my_id+player)%3));
        newLog.put("cardID", Integer.toString(idx));
        newLog.put("hintType","number");
        logRef.setValue(newLog);

    }

    public void nextTurn() {
        Hashtable<String,String> newLog = new Hashtable<>();
        newLog.put("logType","nextTurn");
        newLog.put("hintUser",Integer.toString(my_id));
        newLog.put("cardID", "");
        newLog.put("hintType","");
        logRef.setValue(newLog);
    }

    public void drawCard(int playerID, int playerHand) {
        Random ranint = new Random();

        int randCardID = ranint.nextInt(50);
        while(!cardList[randCardID].position.equals("deck")) {
            randCardID = ranint.nextInt(50);
        }

        Hashtable<String,String> newCard = new Hashtable<>();
        newCard.put("color",cardList[randCardID].color);
        newCard.put("number",cardList[randCardID].number);
        newCard.put("position", Integer.toString(playerID));
        newCard.put("handPosition",Integer.toString(playerHand));
        boardRef.child(Integer.toString(randCardID)).setValue(newCard);

        cardList[randCardID].position = Integer.toString(playerID);
        cardList[randCardID].handPosition = Integer.toString(playerHand);
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
                color = "0";
            else if (i<20)
                color = "1";
            else if (i<30)
                color = "2";
            else if (i<40)
                color = "3";
            else
                color = "4";
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
        android.util.Log.d("TAG", roomID);
        roomRef = firebaseDatabase.getReference("Room").child(roomID);
        userRef = roomRef.child("User");
        boardRef = roomRef.child("Board");
        logRef = roomRef.child("Log");
        lifeRef = roomRef.child("Life");

        lifeRef.setValue(3);
        // -->


        //<-- get current player id
        String[] email = firebaseUser.getEmail().split("@",2);
        id = email[0];
        //-->

        initCardList();

        //<-- reference listener
        boardRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Card card = snapshot.getValue(Card.class);

                int now = Integer.parseInt( snapshot.getKey() ) ;
                cardList[ now ] = card;
                CARD_PAINT( now ) ;

                //if(card.position.equals())

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

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User newUser = snapshot.getValue(User.class);

                user = newUser;

                if(newUser.p1.equals(id)) {
                    my_id = 0;
                }
                else if(newUser.p2.equals(id)) {
                    my_id = 1 ;
                }
                else if (newUser.p3.equals(id)) {
                    my_id = 2 ;
                }

                // id
                left_player = ( my_id + 1 ) % 3 ;
                right_player = ( my_id + 2 ) % 3 ;
                //

                if(newUser.p2Ready.equals("true")) {
                    //TODO p2Ready 이미지 생성
                }
                else {
                    //TODO p2Ready 이미지 제거
                }

                if(newUser.p3Ready.equals("true")) {
                    //TODO p3Ready 이미지 생성
                }
                else {
                    //TODO p3Ready 이미지 제거
                }

                if(newUser.p2Ready.equals("true") && newUser.p3Ready.equals("true")) {
                    //TODO 게임시작버튼 생성
                }
                else {
                    //TODO 게임시작버튼 제거
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log newLog = snapshot.getValue(Log.class);
                if(newLog == null)
                    newLog = new Log();

                if(newLog.logType.equals("hint")) {
                    if(newLog.hintType.equals("color")) {
                        int h_U = Integer.parseInt(newLog.hintUser) ;
                        int card_id = Integer.parseInt(newLog.cardID) ;
                        INFO_PAINT_COLOR( h_U , card_id );

                        //cardList[Integer.parseInt(newLog.cardID)].color
                    }
                    else if (newLog.hintType.equals("number")) {

                        int h_U = Integer.parseInt(newLog.hintUser) ;
                        int card_id = Integer.parseInt(newLog.cardID) ;
                        INFO_PAINT_NUMBER( h_U , card_id );

                        //cardList[Integer.parseInt(newLog.cardID)].number
                    }

                }/*
                else if(newLog.logType.equals("submit")) {  //hintType에 제출한 handPosition을 저장 (따로 만들어도 됨)
                    if(cardList[Integer.parseInt(newLog.cardID)].position.equals('p'+newLog.hintUser)               //제출한 카드가 hintUser에게 있는 카드인지 확인
                            && cardList[Integer.parseInt(newLog.cardID)].handPosition.equals(newLog.hintType)) {    //제출한 카드의 위치가 맞는지 확인
                        //TODO 제출성공
                    }
                    else {
                        //TODO 제출실패
                    }
                }
                */
                else if(newLog.logType.equals("nextTurn")) {
                    if(newLog.hintUser.equals(Integer.toString(left_player))) {
                        //TODO 내턴 시작
                        step1.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lifeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                life = snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //-->

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

            /*
            hand_card[ my_id ][ i ] = new int ;
            hand_card[ right_player ][ i ] = new int ;
            hand_card[ left_player ][ i ] = new int ;
             */
            score_deck[ i ] = new card_info() ;
            score_deck[ i ].Set( i , 0 ) ;

            left_image[ i ] = (ImageView) findViewById(Rid_left[ i ] ) ;
            l_number[ i ] = (TextView) findViewById(Rid_l_n[ i ] ) ;

            right_image[ i ] = (ImageView) findViewById(Rid_right[ i ] ) ;
            r_number[ i ] = (TextView) findViewById(Rid_r_n[ i ] ) ;

            my_image[ i ] = (ImageView) findViewById(Rid_my[ i ] ) ;
            m_number[ i ] = (TextView) findViewById(Rid_m_n[ i ] ) ;
            my_image[ i ].setImageResource( R.drawable.card ) ;

            score_image[ i ] = (ImageView) findViewById(Rid_score[ i ] ) ;

        }

        /* 임시 데이터 */
/*
        Random ranint = new Random() ;
        for( int i = 0 ; i < 5 ; i ++ ) {

            int temp = ranint.nextInt(5 ) ;
            hand_card[ left_player ][ i ].Set( temp , temp + 1 ) ;
            temp_string = color_array[ hand_card[ left_player ][ i ].Color() ] + Integer.toString(hand_card[ left_player ][ i ].Number()) ;
            resID = getResId( temp_string , R.drawable.class); // or other resource class
            left_image[ i ].setImageResource( resID );

            temp = ranint.nextInt(5 ) ;
            hand_card[ right_player ][ i ].Set( temp , temp + 1 ) ;
            temp_string = color_array[ hand_card[ right_player ][ i ].Color() ] + Integer.toString( hand_card[ right_player ][ i ].Number()) ;
            resID = getResId( temp_string , R.drawable.class); // or other resource class
            right_image[ i ].setImageResource( resID );

            temp = ranint.nextInt(5 ) ;
            hand_card[ my_id ][ i ].Set( temp , temp + 1 ) ;
            temp_string = color_array[ hand_card[ my_id ][ i ].Color() ] + Integer.toString(hand_card[ my_id ][ i ].Number()) ;
            resID = getResId( temp_string , R.drawable.class); // or other resource class
            my_image[ i ].setImageResource( resID );

            score_deck[ i ].Set( 0 , 0 ) ;

        }
 */

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

        if(my_id == 0) {


            for(int _i=0;_i<3;_i++)
            {
                for(int _j=0;_j<5;_j++)
                {
                    drawCard(_i,_j);
                }

            }

            step1.setVisibility( View.VISIBLE );

        }


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
                        card_num = 0 ;
                        break ;
                    case R.id.step3_number_2:
                        card_num = 1 ;
                        break ;
                    case R.id.step3_number_3:
                        card_num = 2 ;
                        break ;
                    case R.id.step3_number_4:
                        card_num = 3 ;
                        break ;
                    case R.id.step3_number_5:
                        card_num = 4 ;
                        break ;
                }

                if( step4_check == true )
                    step4.setVisibility(View.VISIBLE);
                else if( sub_or_throw == -1 ) { // 카드 버림

                    CARD_THROW();

                    nextTurn();
                }
                else if( sub_or_throw == 1 ) { // 카드 제출

                    CARD_SUB() ;

                    nextTurn();
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

                nextTurn();

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

