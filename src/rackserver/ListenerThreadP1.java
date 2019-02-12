/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rackserver;

import rackserver.UI.Overlay;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author Matteo
 */
public class ListenerThreadP1 implements Runnable
{

    Application context;
    static BufferedReader inFromPi = null;
    String piResponse;
	
    public ListenerThreadP1(Socket _socket, Application context)
    {
        this.context = context;
        try{ inFromPi = new BufferedReader(new InputStreamReader(_socket.getInputStream())); } catch (Exception e) {}
    }
	
    @Override
    public void run()
    { 
        while(context.connectedToP1)
        {
            try {piResponse = inFromPi.readLine();} catch(Exception e) {System.out.println(e);}
            if(piResponse != null)
            {
                if(piResponse.equals("disconnecting"))
                {
                    UtilitiesClass.getInstance().DisconnectPi("p1", context);
                    context.frame.commandLineText.append("P1 has interrupted connection\n");
                    UtilitiesClass.getInstance().WriteToAndroidClient("p1-interrupt", context);
                }
                else
                {
                    context.frame.temperatureLabel.setText(piResponse + "°");
                    context.currentTemperature = piResponse;
                }
            }
            else
                context.frame.commandLineText.append("Null response\n");
        }
    }
}
