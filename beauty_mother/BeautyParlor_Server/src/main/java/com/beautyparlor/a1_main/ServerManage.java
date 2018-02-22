package com.beautyparlor.a1_main;

import java.util.Scanner;

public class ServerManage extends Thread {
    public static int countAccept = 0;
    static Scanner in = new Scanner(System.in);
    static String temp="";

    @Override
    public void run() {
        for (; ;) {
            if (in.hasNextLine()) {
                GetOrder_From_Manager();
            }

        }
    }

    /**
     * 관리자로부터 명령어 입력받는 함수
     */
    public static void GetOrder_From_Manager() {
        String order = in.nextLine();
        switch (order) {
        case "user":
            System.out.println("누적 접속 인원: " + countAccept);
            break;
        }
        
        
    }
}
