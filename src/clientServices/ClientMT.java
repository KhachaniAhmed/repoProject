/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientServices;

import bean.ConnectedUsers;
import bean.Conversation;
import bean.Message;
import bean.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import util.Session;

/**
 *
 * @author HP
 */
public class ClientMT {

    private Socket clientS;

    public ClientMT(Socket clientS) {
        this.clientS = clientS;
    }

    public ClientMT() {
    }

    public Socket connecter() throws IOException {
        clientS = new Socket((String) Session.getAttribut("adresseIP"), 50834);
        Session.setAttribut(clientS, "connectedSocket");
        return clientS;
    }

    public void deconnecter() throws IOException {
        Socket socket = (Socket) Session.getAttribut("connectedSocket");
        socket.close();
        Session.setAttribut(null, "connectedSocket");
    }

    public void quiter() throws IOException {
        Socket serviceSocket = (Socket) Session.getAttribut("connectedServiceSocket");
        serviceSocket.close();
        Session.setAttribut(null, "connectedServiceSocket");
        deconnecter();
    }

    public void beforConnection() throws IOException {
        Socket serviceSocket = new Socket((String) Session.getAttribut("adresseIP"), 60830);
        Session.setAttribut(serviceSocket, "connectedServiceSocket");

    }

    public ConnectedUsers connection(ConnectedUsers connect) throws IOException {
        Socket s = connecter();
        connect.setPort(s.getLocalPort());
        connect.setIp(s.getInetAddress().getHostAddress());
        return connect;

    }

    public void send(User ConnectedUser, ConnectedUsers portDist, String msg, Conversation conversation) throws IOException {
        Message message = new Message(ConnectedUser, portDist.getPort(), msg, conversation);
        ObjectOutputStream outObject = new ObjectOutputStream(clientS.getOutputStream());
        outObject.writeObject(message);
        outObject.flush();
    }

    public Message recieve() throws IOException, ClassNotFoundException {
        ObjectInputStream inOpject = new ObjectInputStream(clientS.getInputStream());
        Message message = (Message) inOpject.readObject();
        return message;
    }

}
