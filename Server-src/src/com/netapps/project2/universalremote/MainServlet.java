package com.netapps.project2.universalremote;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class MainServlet extends HttpServlet
{
    private String  acl_data = "acl_data",
                    acl_options = "acl_options",
                    acl_div = "acl_div",
                    acl_chart = "acl_chart",
            
                    gyro_data = "gyro_data",
                    gyro_options = "gyro_options",
                    gyro_div = "gyro_div",
                    gyro_chart = "gyro_chart",
            
                    mag_data = "mag_data",
                    mag_options = "mag_options",
                    mag_div = "mag_div",
                    mag_chart = "mag_chart",
            
                    data_update_url_front_ = "http://",
                    data_update_url_back_ = "/dataupdateterminal?tqrt=scriptInjection&tqx=reqId:0";
    
    
    @Override
    protected void doGet(HttpServletRequest req, 
        HttpServletResponse resp) throws ServletException, IOException{
        // writes an html page including javascript using the Google Charts api
        //   to render and refresh 3 charts
        resp.getWriter().write(buildHtmlDocument());
    }
    
    
    
    //========================================================================//
    //============================Helper Functions============================//
    private String buildHtmlDocument(){
        return 
    "<html>\n" +
      buildHtmlHeader() +
      buildHtmlBody() +
    "</html>";
    }
    
    private String buildHtmlHeader(){
        return 
    "  <head>\n" +
    "    <script type=\"text/javascript\" " +
             "src='https://www.google.com/jsapi?autoload={\"modules\":" +
             "[{\"name\":\"visualization\",\"version\":\"1\",\"packages\":" +
             "[\"corechart\"]}]}'>" +
    "    </script>\n" +
    "    <script type=\"text/javascript\">\n" +
    "      google.setOnLoadCallback(drawChart);\n" +
    "      var query;\n" +
    "      var data;\n" +
                
    "      var " + acl_data + ";\n" +
    "      var " + acl_chart + ";\n" +
    "      var " + acl_options + ";\n" +
                
    "      var " + gyro_data + ";\n" +
    "      var " + gyro_chart + ";\n" +
    "      var " + gyro_options + ";\n" +
                
    "      var " + mag_data + ";\n" +
    "      var " + mag_chart + ";\n" +
    "      var " + mag_options + ";\n" +
                
    "      function handleQueryResponse(response){\n" +
    "        if(response.isError()){\n" +
    "          return;\n" +
    "        }else{\n" +
    "        data = response.getDataTable();\n" +
    "       " + acl_data + " = new google.visualization.DataView(data);\n" +
    "       " + gyro_data + " = new google.visualization.DataView(data);\n" +
    "       " + mag_data + " = new google.visualization.DataView(data);\n" +
                
    "       " + acl_data + ".setColumns([0,1,2,3]);\n" +
    "       " + gyro_data + ".setColumns([0,4,5,6]);\n" +
    "       " + mag_data + ".setColumns([0,7,8,9]);\n" +
                
    "       " + acl_chart + ".draw(" + acl_data + "," + acl_options + ");\n" +
    "       " + gyro_chart + ".draw(" + gyro_data + "," + gyro_options + ");\n" +
    "       " + mag_chart + ".draw(" + mag_data + "," + mag_options + ");\n" +
    "        }\n" +
    "      }\n" +
    "      function drawChart(){\n" +
             buildChartData(acl_data, "a", "component") + 
             buildChartData(gyro_data, "g", "component") + 
             buildChartData(mag_data, "m", "component") +
                
             buildChartOptions(acl_options, "Accelerometer Data", "Time (ms)", "Acceleration (m/s²)",
                "-20", "20") +
             buildChartOptions(gyro_options, "Gyroscope Data", "Time (ms)", "Rotation (rad/s)",
                "-10", "10") +
             buildChartOptions(mag_options, "Magnetometer Data", "Time (ms)", "Field Strength (uT)",
                "-40", "40") +
                
             buildChart(acl_chart, acl_div, acl_data, acl_options) +
             buildChart(gyro_chart, gyro_div, gyro_data, gyro_options) +
             buildChart(mag_chart, mag_div, mag_data, mag_options) +
                
    "        query = new google.visualization.Query('" + 
                data_update_url_front_ + GUI.server_ip_address_ + ":" +
                GUI.port_tcp_ + data_update_url_back_ + "');\n" +
    "        query.setQuery('chart');\n" +
    "        query.setTimeout(0.25);\n" +
    "        query.setRefreshInterval(0.25);\n" +
    "        query.send(handleQueryResponse);\n" +
    "      }\n" +                
    "    </script>\n" +
    "  </head>\n";
    }
    
    private String buildChartData(String name, String postfix, String label){
        return
    "        var " + name + " = new google.visualization.DataTable(\n" +
    "        {\n" +
    "          cols: [{id:'time', label:'Time', type:'number'},\n" +
    "             {'id':'x" + postfix + "','label':'x " + label + "','type':'number'},\n" +
    "             {'id':'y" + postfix + "','label':'y " + label + "','type':'number'},\n" +
    "             {'id':'z" + postfix + "','label':'z " + label + "','type':'number'}],\n" +
    "          rows: []\n" +
    "        }, 0.6);\n\n";
    }
    
    private String buildChartOptions(String name, String title, String x_title,
            String y_title, String y_min, String y_max){
        return
    "        " + name + " = {\n" +
    "          axisTitlesPosition:'out',\n" +
    "          legend:{\n" +
    "            position:'bottom'" +
    "          },\n" +
    "          title:'" + title + "',\n" +
                
    "          chartArea:{\n" +
    "            width:'85%',\n" +
    "            height:'75%'\n" +
    "          },\n" +
                
    "          vAxis:{\n" +
    "            title:'" + y_title + "',\n" +
    "            minValue:'" + y_min + "',\n" +
    "            maxValue:'" + y_max + "'\n" +
    "          },\n" +
                
    "          hAxis:{\n" +
    "            title:'" + x_title + "'\n" +
    "          },\n" +
                
    "          animation:{\n" +
    "            duration: 200,\n" +
    "            easing: 'linear'\n" +
    "          }\n" +
    "        };\n\n";
    }
    
    
    private String buildChart(String name, String div, String data, String options){
        return
    "        " + name + " = new google.visualization.LineChart("
                + "document.getElementById('" + div + "'));\n" +
    "        " + name + ".draw(" + data + ", " + options + ");\n";
    }
    
    private String buildHtmlBody(){        
        return 
    "  <body>\n" +
         buildDiv("title", "width: 100%; height: 50px; text-align: center; "
                + "font-size: 48;", "Universal Remote Data") +
         buildDiv(acl_div, "width: 100%; height: 500px;", "") +
         buildDiv(gyro_div, "width: 100%; height: 500px;", "") +
         buildDiv(mag_div, "width: 100%; height: 500px;", "") +
    "  </body>\n";
    }
    
    private String buildDiv(String id, String css, String text){
        return
    "    <div id=\"" +id + "\" style=\"" + css + "\">" + text + "</div>\n";
    }
}
