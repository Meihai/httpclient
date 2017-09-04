package com.keydak.httpclient;

import com.google.gson.Gson;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.params.CoreConnectionPNames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by admin on 2017/9/2.
 */
public class JsonClient {
    //发送一个get请求
    public static String get(String path) throws Exception{
        HttpURLConnection httpConn=null;
        BufferedReader in=null;
        try{
            URL url=new URL(path);
            httpConn=(HttpURLConnection) url.openConnection();
            //读取响应
            if(httpConn.getResponseCode()==HttpURLConnection.HTTP_OK){
                StringBuffer content=new StringBuffer();
                String tempStr="";
                in= new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                while((tempStr=in.readLine())!=null){
                    content.append(tempStr);
                }
                return content.toString();
            }else{
                throw new Exception("请求出现了问题!");
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            in.close();
            httpConn.disconnect();
        }
        return null;
    }

    //发送一个post 请求
    public static String post(String path,String params) throws Exception{
        HttpURLConnection httpConn=null;
        BufferedReader in=null;
        PrintWriter out=null;
        try{
            URL url=new URL(path);
            httpConn=(HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("POST");
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);

            //发送post 请求数据
            out=new PrintWriter(httpConn.getOutputStream());
            out.println(params);
            out.flush();

            //读取响应
            if(httpConn.getResponseCode()==HttpURLConnection.HTTP_OK){
                StringBuffer content=new StringBuffer();
                String tempStr="";
                in =new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                while((tempStr=in.readLine())!=null){
                    content.append(tempStr);
                }
                return content.toString();
            }else{
                throw new Exception("post 请求出现了问题!");
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            in.close();
            out.close();
            httpConn.disconnect();
        }
        return null;
        // String resMessage=HttpClient.post("http://192.168.0.163:8005/locations","group=huang");
        // System.out.println(resMessage);
    }

    // 发送json数据
    public static HttpMethod postMethod(String url, String jsonStr) throws IOException{
        PostMethod postMethod=new PostMethod(url);
        //postMethod.setRequestHeader("Content-Type","application/x-www-form-urlencoded;chaset=utf-8");
        postMethod.setRequestHeader("Content-Type","application/json");

        RequestEntity requestEntity=new StringRequestEntity(jsonStr,"text/xml","UTF-8");
        postMethod.setRequestEntity(requestEntity);
        postMethod.releaseConnection();
        return postMethod;
    }


    private static class Point{
        private double posx;
        private double posy;
        private double posz;
        public void setPosx(double posx){
            this.posx=posx;
        }
        public double getPosx(){
            return this.posx;
        }
        public void setPosy(double posy){
            this.posy=posy;
        }
        public double getPosy(){
            return this.posy;
        }

        public void setPosz(double posz){
            this.posz=posz;
        }

        public double getPosz(){
            return this.posz;
        }
    }

    public static void printUsage(){
        String usage="usage:java -jar jar serverurl posx posy posz\n java -jar jarname http://localhost:8080/findServer/find/learn 2000.0 1200.0 300.0";
        System.out.println(usage);
    }

    public static void main(String[] args ){
//        if(args.length<4){
//            printUsage();
//            return;
//        }
//        String url=args[0];
        Point inspoint=new Point();
//        inspoint.setPosx(Double.parseDouble(args[1]));
//        inspoint.setPosy(Double.parseDouble(args[2]));
//        inspoint.setPosz(Double.parseDouble(args[3]));
        inspoint.setPosx(2000.0);
        inspoint.setPosy(1000.0);
        inspoint.setPosz(800.0);
        org.apache.commons.httpclient.HttpClient insHttpClient=new org.apache.commons.httpclient.HttpClient();
        insHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,60000);
        String url="http://localhost:8080/findServer/find/learn";
        HttpMethod method=null;
        try{
            String jsonStr=new Gson().toJson(inspoint);
            System.out.println("jsonStr="+jsonStr);
            method=postMethod(url, jsonStr);
            int sendStatus=0;
            sendStatus=insHttpClient.executeMethod(method);
            System.out.println("打印发送状态");
            System.out.println(sendStatus);
            System.out.println("------------------------------------------------");
            String response=method.getResponseBodyAsString();
            System.out.println(response);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(method!=null)
                method.releaseConnection();
        }

    }
}
