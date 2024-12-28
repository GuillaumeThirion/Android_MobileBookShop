package be.hepl.mobilebookshop.protocol;

import android.content.Context;
import be.hepl.entity.BookElement;
import be.hepl.protocol.bspp.requests.*;
import be.hepl.protocol.bspp.responses.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

public class BSPPClient {

    private static Socket socket;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;


    /* STATIC METHODS */

    public static int loginClient(Context context, String lastName, String firstName, boolean isNew) {
        try {
            Properties props = new Properties();
            InputStream fis = context.getAssets().open("config.properties");
            props.load(fis);

            //String ipServer = props.getProperty("ipServerAlessio");
            String ipServer = props.getProperty("ipServerGuillaume");
            int portServer = Integer.parseInt(props.getProperty("PORT_PAYMENT"));

            socket = new Socket(ipServer, portServer); // Initialise la socket
            oos = new ObjectOutputStream(socket.getOutputStream()); // Initialise les flux de données
            ois = new ObjectInputStream(socket.getInputStream());

            RequestCLIENT request = new RequestCLIENT(lastName, firstName, isNew);
            oos.writeObject(request);
            ResponseCLIENT response = (ResponseCLIENT) ois.readObject();

            return response.clientId();
        } catch (Exception ex) {
            System.err.println("Erreur de connexion: " + ex.getMessage());
            closeConnection();
            return -4;
        }
    }

    public static ArrayList<BookElement> selectBook(Integer id, String title, String authorLastName, String authorFirstName, String subjectName, Float maxPrice) {
        try {
            RequestSELECT_BOOK request = new RequestSELECT_BOOK(id, title, authorLastName, authorFirstName, subjectName, maxPrice);
            oos.writeObject(request);
            ResponseSELECT_BOOK response = (ResponseSELECT_BOOK) ois.readObject();
            return response.books();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Erreur de connexion: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    public static boolean addCaddyItem(Integer bookId, Integer quantity) {
        try {
            RequestADD_CADDY_ITEM request = new RequestADD_CADDY_ITEM(bookId, quantity);
            oos.writeObject(request);
            ResponseADD_CADDY_ITEM response = (ResponseADD_CADDY_ITEM) ois.readObject();
            return response.isOk();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Erreur de connexion: " + ex.getMessage());
            return false;
        }
    }

    public static boolean delCaddyItem(Integer bookId, Integer quantity) {
        try {
            RequestDEL_CADDY_ITEM request = new RequestDEL_CADDY_ITEM(bookId, quantity);
            oos.writeObject(request);
            ResponseDEL_CADDY_ITEM response = (ResponseDEL_CADDY_ITEM) ois.readObject();
            return response.isOk();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Erreur de connexion: " + ex.getMessage());
            return false;
        }
    }

    public static boolean cancelCaddy() {
        try {
            RequestCANCEL_CADDY request = new RequestCANCEL_CADDY();
            oos.writeObject(request);
            ResponseCANCEL_CADDY response = (ResponseCANCEL_CADDY) ois.readObject();
            return response.isOk();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Erreur de connexion: " + ex.getMessage());
            return false;
        }
    }

    public static boolean payCaddy() {
        try {
            RequestPAY_CADDY request = new RequestPAY_CADDY();
            oos.writeObject(request);
            ResponsePAY_CADDY response = (ResponsePAY_CADDY) ois.readObject();
            return response.isOk();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Erreur de connexion: " + ex.getMessage());
            return false;
        }
    }

    public static void closeConnection() {
        try {
            if (ois != null) ois.close();
            if (oos != null) oos.close();
            if (socket != null) socket.close();
        } catch (IOException ex) {
            System.err.println("Erreur lors de la fermeture de la connexion: " + ex.getMessage());
        }
    }
}
