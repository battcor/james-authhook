package org.apache.james.smtpserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.james.protocols.smtp.SMTPSession;
import org.apache.james.protocols.smtp.hook.AuthHook;
import org.apache.james.protocols.smtp.hook.HookResult;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This AuthHookSample hook can be used to authenticate against the james user repository
 */
public class AuthHookSample implements AuthHook {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub 
    }

    @Override
    public void init(Configuration arg0) throws ConfigurationException {
        // TODO Auto-generated method stub

    }

    @Override
    public HookResult doAuth(SMTPSession session, String username, String password) {
        // TODO Auto-generated method stub
        System.out.println("Congrats, AuthHookSample is called!");

        return HookResult.OK;
    }
    
    @SuppressWarnings("resource")
    public boolean reset() {
        DataInputStream is;
        DataOutputStream os;
        boolean result = true;
        String noReset = "Could not reset.";
        String reset = "The server has been reset.";

        try {
            Socket socket = new Socket(InetAddress.getByName("x.x.x.x"), 3994);
            // String string = "{\"id\":1,\"method\":\"object.deleteAll\",\"params\":[\"subscriber\"]}";
            String string = "{\"magicString\":\"string\",\"headerVersion\":\"1\",\"messageSize\":\"1\",\"version\":\"string\",\"functionName\":\"AuthenticateUser\",\"payload\":\"{ \"username\": \"testUser\", \"password\\\": \"testPassword12345!\" }\"}";
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
            PrintWriter pw = new PrintWriter(os);
            pw.println(string);
            pw.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            JSONObject json = new JSONObject(in.readLine());
            if (!json.has("result")) {
                System.out.println(noReset);
                result = false;
            }
            is.close();
            os.close();
        } catch (IOException e) {
            result = false;
            System.out.println(noReset);
            e.printStackTrace();            
        } catch (JSONException e) {
            result = false;
            System.out.println(noReset);
            e.printStackTrace();
        }
        System.out.println(reset);
        return result;
    }
}