package com.example.hanabi;

public class Room {
    public String roomMasterId;            // 이메일에서 @ 이전 아이디
    public String numberOfPlayer;          // 현재 방에 참가 중인 플레이어 수
    public String password = null;         // 패스워드
    public String roomNumber;              // 방 번호
    public String title;                   // 방 제목
    public String isGameStart;             // 게임 시작 했는지
    public Room(){}
}
