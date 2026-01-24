package com.example.hello_spring;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class Main {
     public static void main(String[] args) throws Exception {
        // 1. "8080번 포트(문)"에 우리 가게(ServerSocket) 문을 엽니다.
        //    (이제 8080이라는 숫자의 의미를 압니다!)
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("---- 서버가 8080 포트에서 문을 열었습니다 ----");

        // 2. 가게는 24시간 문을 닫지 않습니다 (무한 루프)
        while (true) {
            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("---- 새로운 손님(클라이언트)이 연결되었습니다! ----");

                // 4. 전담 직원이 손님에게 "응답(음식)"을 보냅니다.
                //    (손님의 주문(요청)을 읽는 코드는 너무 복잡해서 일단 생략!)
                try (OutputStream outputStream = clientSocket.getOutputStream()) {
                    // 5. 우리가 "직접" HTTP 응답 메시지를 손으로 만듭니다. (가장 끔찍한 부분)
                    //    (HTTP/1.1 200 OK 약속, Content-Length 약속...)
                    String responseBody = "Hello, World!";
                    byte[] bodyBytes = responseBody.getBytes(StandardCharsets.UTF_8);

                    outputStream.write("HTTP/1.1 200 OK\r\n".getBytes(StandardCharsets.US_ASCII));
                    outputStream.write(("Content-Length: " + bodyBytes.length + "\r\n").getBytes(StandardCharsets.US_ASCII));
                    outputStream.write("Content-Type: text/plain; charset=utf-8\r\n".getBytes(StandardCharsets.US_ASCII));
                    outputStream.write("Connection: close\r\n".getBytes(StandardCharsets.US_ASCII));
                    outputStream.write("\r\n".getBytes(StandardCharsets.US_ASCII)); // 헤더와 본문 사이의 필수 빈 줄
                    outputStream.write(bodyBytes);
                    outputStream.flush(); // 접시에 담아 내보내기
                }

                // 6. 응대가 끝났으니 직원은 퇴근합니다 (연결 종료)
                System.out.println("---- 손님 응대가 끝나 연결을 종료합니다. ----");
            } catch (java.net.SocketException se) {
                // 손님이 먼저 떠난 경우(broken pipe 등) 서버가 종료되지 않도록 무시하고 다음 손님을 맞이합니다.
                System.out.println("[INFO] 클라이언트 연결 문제: " + se.getMessage());
            } catch (Exception e) {
                // 다른 예외도 서버 전체가 죽지 않도록 로깅만 하고 계속 진행
                System.out.println("[ERROR] 요청 처리 중 예외: " + e);
            }
        }
    }
}
