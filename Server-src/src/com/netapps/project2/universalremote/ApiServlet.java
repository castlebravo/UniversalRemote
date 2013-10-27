package com.netapps.project2.universalremote;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class ApiServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, 
        HttpServletResponse resp) throws ServletException, IOException{
        
        // respond with an XML document containing the API
        resp.getWriter().write(
        "<?xml version=\"1.0\"?>\n" +
        "<document>\n" +
        "   <body>\n" +
        "       <api>\n" +
        "           <method>\n" +
        "               <command>request_udp_port</command>\n" +
        "               <query>/dataupdateterminal?req_udp_port</query>\n" +
        "               <action>returns the port that the UDP listener is " +
                            "running on in string format</action>\n" +
        "           </method>\n" +
        "           <method>\n" +
        "               <command>request_sensor_data_clear</command>\n" +
        "               <query>/dataupdateterminal?req_data_clr</query>\n" +
        "               <action>clears the stored sensor data, responds with " + 
                            "nothing</action>\n" +
        "           </method>\n" +        
        "       </api>\n" +
        "   </body>\n" +
        "</document>");
    }
}
