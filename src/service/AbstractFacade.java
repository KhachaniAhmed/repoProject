/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.UserService;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import util.Session;

/**
 *
 * @author HP
 */
public class AbstractFacade {

    public void writeObject(UserService userService) throws IOException {
        Socket socket = (Socket) Session.getAttribut("connectedServiceSocket");
        userService.setPort(socket.getLocalPort());
        ObjectOutputStream outObject = new ObjectOutputStream(socket.getOutputStream());
        outObject.writeObject(userService);
        outObject.flush();
    }

    public UserService getService() throws IOException, ClassNotFoundException {
        Socket socket = (Socket) Session.getAttribut("connectedServiceSocket");
        ObjectInputStream inOpject = new ObjectInputStream(socket.getInputStream());
        UserService service = (UserService) inOpject.readObject();
        return service;
    }

    public UserService doExecute(UserService userService) throws IOException {
        writeObject(userService);
        try {
            UserService service = getService();
            return service;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("do execute Client catchs an error" + e.getLocalizedMessage());
            return null;
        }
    }
}
