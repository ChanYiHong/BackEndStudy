package hcy.servlet.basic.request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(name = "requestHeaderServlet", urlPatterns = "/request-header")
public class RequestHeaderServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        printStartLine(request);
        printHeaders(request);
        printHeaderUtils(request);
        printEtc(request);
    }

    private void printStartLine(HttpServletRequest request) {

        System.out.println("--- REQUEST LINE START ---");
        System.out.println("request.getMethod() = " + request.getMethod());  // GET
        System.out.println("request.getProtocol() = " + request.getProtocol());  // HTTP/1.1
        System.out.println("request.getScheme() = " + request.getScheme());  // HTTP
        System.out.println("request.getRequestURL() = " + request.getRequestURL()); // http://localhost:8080/request-header
        System.out.println("request.getRequestURI() = " + request.getRequestURI()); // /request-header
        System.out.println("request.getQueryString() = " + request.getQueryString()); // null  (쿼리 스트링에 파라미터x)
        System.out.println("request.isSecure() = " + request.isSecure()); // false (https가 아니라서)
        System.out.println("--- REQUEST LINE END ---");

    }

    // Header 모든 정보.
    private void printHeaders(HttpServletRequest request) {
        System.out.println("--- Headers - start ---");

//        // header 정보 가져오는 옛날방식.
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while(headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//            System.out.println(headerName + " : " + headerName);
//        }

        // 요즘 방식.
        request.getHeaderNames().asIterator()
                .forEachRemaining(headerName -> System.out.println(headerName + " : " + headerName));

        request.getHeader("host"); // 하나씩 꺼내기.

        System.out.println("--- Headers - end ---");
    }

    private void printHeaderUtils(HttpServletRequest request) {
        System.out.println("--- Header 편의 조회 start ---");
        System.out.println("[Host 편의 조회]");
        System.out.println("request.getServerName() = " + request.getServerName()); // localhost
        System.out.println("request.getServerPort() = " + request.getServerPort()); // 8080
        System.out.println();

        System.out.println("[Accept - Language 편의 조회]");
        request.getLocales().asIterator()
                .forEachRemaining(locale -> {
                    System.out.println("locale = " + locale);  // ko_KR, ko, en_US, en
                });

        System.out.println("request.getLocale() = " + request.getLocale());  // ko_KR

        System.out.println();

        System.out.println("[Cookie 편의 조회]");
        if (request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                System.out.println(cookie.getName() + " : " + cookie.getValue());
            }
        }
        System.out.println();

        System.out.println("[Content 편의 조회]");
        System.out.println("request.getContentType() = " + request.getContentType());
        System.out.println("request.getContentLength() = " + request.getContentLength());
        System.out.println("request.getCharacterEncoding() = " + request.getCharacterEncoding());

        System.out.println("--- Header 편의 조회 end ---");
        System.out.println();
    }

    private void printEtc(HttpServletRequest request) {
        System.out.println("--- 기타 조회 start ---");
        // HTTP 메세지에 있는 정보는 아님.

        System.out.println("[Remote 정보]"); // 요청이 온 곳에 대한 정보.
        System.out.println("request.getRemoteHost() = " + request.getRemoteHost());
        System.out.println("request.getRemoteAddr() = " + request.getRemoteAddr());
        System.out.println("request.getRemotePort() = " + request.getRemotePort());
        System.out.println();

        System.out.println("[local 정보]"); // 내 서버 정보.
        System.out.println("request.getLocalName() = " + request.getLocalName());
        System.out.println("request.getLocalAddr() = " + request.getLocalAddr());
        System.out.println("request.getLocalPort() = " + request.getLocalPort());

        System.out.println("--- 기타 조회 end ---");
    }
}
