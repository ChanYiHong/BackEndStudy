package me.hcy.SpringBootMVC;

import java.util.Scanner;

public class pang {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // 몇개 입력할지 정함
        System.out.println("숫자 개수를 입력하세요 : ");
        int n = in.nextInt();
        int[] list = new int[n];

        System.out.println("숫자를 " + n + "개 입력하세요!");

        // 숫자 n개 입력
        for(int i = 0; i < n; i++){
            int temp = in.nextInt();
            list[i] = temp;
        }

        // 여기서는 input이 최대 10000, 최소 0이라 가정.
        int max = 0;
        int min = 10000;

        // 비교연산
        // max보다 배열에 저장된 값이 크면 max에 현재 배열에 저장된 값 저장
        // min보다 배열에 저장된 값이 작으면 min에 현재 배열에 저장된 값 저장
        for(int i = 0; i < n; i++){
            if(max < list[i]){
                max = list[i];
            }
            if(min > list[i]){
                min = list[i];
            }
        }

        // 정답 출력
        System.out.println("최대값 : " + max);
        System.out.println("최소값 : " + min);
    }
}
