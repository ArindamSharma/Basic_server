//package final_http_server;
import java.io.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import java.util.Base64 ;
import java.awt.image.BufferedImage;

public class HTTPServer extends Thread {
    private static int PORT = 8081 ;
    private static  String IP_ADDR = "0.0.0.0";
    private static int MAX = 5 ; // maximum number of connections
    private Socket client = null ;
    private BufferedReader in = null ;
    private DataOutputStream out = null;
    private static String contentHtml = ": text/html" ;
    private static String contentCss = ": text/css" ;
    private static  String contentJs = ": text/js" ;
    private static String contentImg1 = ": image/jpg";
    private static String contentImg2 = ": image/png";
    private static String contentfont1 = ": application/x-font-woff";
    private static String contentfont2 = ": application/x-font-sfnt";

    private static String contentEncoded= "application/x-www-form-urlencoded" ;

    public static void main(String[] args) throws Exception{
        ServerSocket server = new ServerSocket(PORT,MAX, InetAddress.getByName(IP_ADDR)) ;
        System.out.println("HTTP server is active on port " + PORT);
        while (true){
            HTTPServer httpServer = new HTTPServer(server.accept()) ;
            System.out.println("A thread is created called thread-");
            httpServer.start();
        }
    }
    public HTTPServer(Socket c){
        client = c ;
    }
    @Override
    public void run() {
        try {
            System.out.println("Client " +client.getInetAddress() + ":" + client.getPort() + " is connected");
            in = new BufferedReader(new InputStreamReader(client.getInputStream())) ;
            out = new DataOutputStream(client.getOutputStream()) ;
            String request = in.readLine() ;
            String header = request ;
            System.out.println(request);
            if(header == null)
                return;
            StringTokenizer tokenizer = new StringTokenizer(header);
            String method = tokenizer.nextToken() ;
            String query = tokenizer.nextToken() ;
            //getting all the http requests from a client
            // System.out.println("Inside ready of instream buffer and the the request from client is : ");
            //(str=input.readLine())!=null && str.length()!=0 || in.ready()
            while(in.ready()){
                // System.out.println(request);
                request = in.readLine() ;
            }
            // System.out.println("Hi");
            System.out.println("abra ca dabra -------------------------------- " + query);

            if(method.equals("GET")){
                if(query.equals("/") || query.equals("/index") || query.equals("/index.html")){
                    //request id for the home page
                    html_request("index.html");
                }
                else if(query.equals("/about.html") || query.equals("/about")){
					html_request("about.html");
                }
                else if(query.equals("/contact.html") || query.equals("/contact")){
					html_request("contact.html");
                }
                else if(query.endsWith(".css")){
                    css_request(query);
                }
                else if(query.endsWith(".js")){
                    js_request(query);
                }
                else if(query.endsWith(".png") || query.endsWith("favicon.ico")){
                    png_request(query);
                }
                else if(query.endsWith(".jpg")){
                    jpg_request(query);
                }
                else if(query.endsWith(".woff") || query.endsWith(".woff2")|| query.endsWith(".tff")){
                    font_request(query);
                }
				else{
					html_request("nopage.html");
				}
            }
        }
		catch (Exception e){
            // System.out.println(64) ;
            e.printStackTrace();
        }
    }

    public void html_request(String addr) throws Exception{
        StringBuffer responseBuffer = new StringBuffer() ;
		if(addr.substring(0,1).equals("/")){
			addr=addr.replaceFirst("/","templates/");
        }
        else{
            addr="templates/"+addr;
        }
		System.out.println(addr);
        File fptr = new File(addr);
        BufferedReader br = new BufferedReader(new FileReader(fptr));
        String st ;
        while((st = br.readLine()) != null)
            responseBuffer.append(st) ;
		// System.out.println(responseBuffer);
        SendReponse(200,responseBuffer.toString(),contentHtml);
    }

    public void css_request(String addr) throws Exception{
        StringBuffer responseBuffer = new StringBuffer() ;
		if(addr.substring(0,1).equals("/")){
			addr=addr.substring(1);
		}
		System.out.println(addr);
        File fptr = new File(addr) ;
        BufferedReader br = new BufferedReader(new FileReader(fptr));
        String st ;
        while((st = br.readLine()) != null)
            responseBuffer.append(st);
        SendReponsePost(200,responseBuffer.toString(),contentCss);
    }

    public void js_request(String addr) throws Exception{
        StringBuffer responseBuffer = new StringBuffer() ;
		if(addr.substring(0,1).equals("/")){
			addr=addr.substring(1);
		}
		System.out.println(addr);
        File fptr = new File(addr) ;
        BufferedReader br = new BufferedReader(new FileReader(fptr));
        String st ;
        while((st = br.readLine()) != null)
            responseBuffer.append(st);
        SendReponsePost(200,responseBuffer.toString(),contentJs);
    }

    public void jpg_request(String addr) throws Exception{
        StringBuffer responseBuffer = new StringBuffer() ;
		if(addr.substring(0,1).equals("/")){
			addr=addr.substring(1);
		}
		System.out.println(addr);
    File fptr = new File(addr) ;
    BufferedImage image = ImageIO.read(fptr);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ImageIO.write(image,contentImg1,outputStream);
    String encodedImage = Base64.getEncoder().encodeToString(outputStream.toByteArray());
    SendReponsePost(200, encodedImage,contentImg1);
    }

    public void png_request(String addr) throws Exception{
        StringBuffer responseBuffer = new StringBuffer() ;
		if(addr.substring(0,1).equals("/")){
			addr=addr.substring(1);
		}
		System.out.println(addr);
        File fptr = new File(addr) ;
        BufferedReader br = new BufferedReader(new FileReader(fptr));
        String st ;
        while((st = br.readLine()) != null)
            responseBuffer.append(st);
        SendReponsePost(200,responseBuffer.toString(),contentImg2);
    }
    public void font_request(String addr) throws Exception{
        StringBuffer responseBuffer = new StringBuffer() ;
		if(addr.substring(0,1).equals("/")){
			addr=addr.substring(1);
		}
		System.out.println(addr);
        File fptr = new File(addr) ;
        BufferedReader br = new BufferedReader(new FileReader(fptr));
        String st ;
        while((st = br.readLine()) != null)
            responseBuffer.append(st);
        if(addr.endsWith(".tff")){
            SendReponsePost(200,responseBuffer.toString(),contentfont2);
        }
        else if(addr.endsWith(".woff") || addr.endsWith(".woff2")){
            SendReponsePost(200,responseBuffer.toString(),contentfont1);
        }
    }
    public void SendReponseCss(int status, String reponseString, String content) throws Exception{
        String NEW_LINE = "\r\n" ;
        String statusLine = null ;
        String serverDetils = Headers.SERVER + ": Java Server" ;
        String contentLengthLine = null ;
        String contentTypeLine = Headers.CONTENT_TYPE + content + NEW_LINE ;
        if(status == 200)
            statusLine = Status.HTTP_200 ;
        else
            statusLine = Status.HTTP_404 ;
        statusLine = statusLine + NEW_LINE ;
        contentLengthLine = Headers.CONTENT_LENGTH + reponseString.length() + NEW_LINE;

        out.writeBytes(statusLine);
        out.writeBytes(serverDetils);
        out.writeBytes(contentTypeLine);
        out.writeBytes(contentLengthLine);
        out.writeBytes(Headers.CONNECTION + ": close" + NEW_LINE);
        out.writeBytes(NEW_LINE);
        out.writeBytes(reponseString);
        out.close();
    }

    public void SendReponse(int status, String reponseString, String content) throws Exception{
        String NEW_LINE = "\r\n" ;
        String statusLine = null ;
        String serverDetils = Headers.SERVER + ": Java Server" ;
        String contentLengthLine = null ;
        String contentTypeLine = Headers.CONTENT_TYPE + content + NEW_LINE ;
        if(status == 200)
            statusLine = Status.HTTP_200 ;
        else
            statusLine = Status.HTTP_404 ;
        statusLine = statusLine + NEW_LINE ;
        contentLengthLine = Headers.CONTENT_LENGTH + reponseString.length() + NEW_LINE;

        out.writeBytes(statusLine);
        out.writeBytes(serverDetils);
        out.writeBytes(contentTypeLine);
        out.writeBytes(contentLengthLine);
        out.writeBytes(Headers.CONNECTION + ": close" + NEW_LINE);
        out.writeBytes(NEW_LINE);
        out.writeBytes(reponseString);
        out.close();
    }

    public void SendReponsePost(int status, String reponseString, String content) throws Exception{
        String NEW_LINE = "\r\n" ;
        String statusLine = null ;
        String serverDetils = Headers.SERVER + ": Java Server" ;
        String contentLengthLine = null ;
        String contentTypeLine = Headers.CONTENT_TYPE + content + NEW_LINE ;
        if(status == 200)
            statusLine = Status.HTTP_200 ;
        else
            statusLine = Status.HTTP_404 ;
        statusLine = statusLine + NEW_LINE ;
        contentLengthLine = Headers.CONTENT_LENGTH + reponseString.length() + NEW_LINE;
        String urlParameters  = "param1=a&param2=b&param3=c";
        byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int postDataLength = postData.length;
        out.writeBytes(statusLine);
        out.writeBytes(serverDetils);
        out.writeBytes(contentTypeLine);
        out.writeBytes(contentLengthLine);
        out.writeBytes(Headers.CONNECTION + ": close" + NEW_LINE);
        out.writeBytes(NEW_LINE);
        out.writeBytes(reponseString);
        out.writeBytes(NEW_LINE);
//        out.write(postData);
        out.close();
    }

    private static class Headers{
        public static final String SERVER = "Server" ;
        public static final String CONNECTION = "Connection" ;
        public static final String CONTENT_LENGTH = "Content-Length" ;
        public static final String CONTENT_TYPE = "Content-Type" ;
    }
    private static class Status{
        public static final String HTTP_200 = "HTTP/1.1 200 OK" ;
        public static final String HTTP_404 = "HTTP/1.1 404 Not Found" ;
    }
}
