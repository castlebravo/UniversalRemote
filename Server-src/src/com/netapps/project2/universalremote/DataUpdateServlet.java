package com.netapps.project2.universalremote;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class DataUpdateServlet extends HttpServlet
{   
    @Override
    protected void doGet(HttpServletRequest req, 
        HttpServletResponse resp) throws ServletException, IOException{
        // if the HTTP GET contained a query
        if(req.getQueryString() != null){
            
            // if it is the Google Chart data query
            if(req.getQueryString().contains("tq=chart"))
                resp.getWriter().write(
                        UDPListener.manager_.getTopClient().getFormattedData());  

            
            // if it is the mobile app requesting the UDP port
            else if(req.getQueryString().contains("req_udp_port"))
                resp.getWriter().write(Integer.toString(UDPListener.port_));

            
            // if it is the mobile app requesting the sensor data to be cleared
            else if(req.getQueryString().contains("req_data_clr"))
                UDPListener.manager_.getTopClient().clearData();
            
            
        // if the HTTP GET did not contain a query
        }else{
            // provide a link to return to the home screen
            resp.getWriter().write("<a href=\"http://" + GUI.server_ip_address_ +
                    ":" + GUI.port_tcp_ +"\">Return Home</a>");
        }
    }  
}