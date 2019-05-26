package lmao;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @author Aleksei Chaika
 */
public class CommandHandler implements Serializable {
    private DataBaseManager dbm;
    private boolean running = true;
    public static String regex = ("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
            "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])" +
            "*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])" +
            "|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:" +
            "[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");


    public CommandHandler(DataBaseManager dbm) {
        this.dbm = new DataBaseManager();
    }
    public String init(SocketDto input) throws IOException, ArrayIndexOutOfBoundsException, SQLException {

            String[] splittedCommand = input.getCommand().split("\\s+");
            switch (splittedCommand[0]) {
                case ("reg"): {
                    if (splittedCommand.length == 3 && splittedCommand[2].matches(regex)) {
                        return dbm.registration(splittedCommand[1], splittedCommand[2]);
                    } else {
                       return "Invalid command: reg <username> <e-mail>. Try again.";
                    }
                }
                case ("login"): {
                    if (splittedCommand.length == 3) {
                        return dbm.authentification(splittedCommand[1], splittedCommand[2]);
                    } else {
                        return "Invalid command: login <username> <password>. Try again.";
                    }
                }
                case ("add"):
                    if (splittedCommand.length == 4) {
                        return dbm.add(splittedCommand[1], splittedCommand[2], Integer.parseInt(splittedCommand[3]));
                    } else {
                        return "Invalid command: add <Name> <Surname> <Age>. Try again.";
                    }
                case ("remove"):
                    if (splittedCommand.length == 4) {
                        return dbm.remove(splittedCommand[1], splittedCommand[2], Integer.parseInt(splittedCommand[3]));
                    } else {
                        return "Invalid command: remove <Name> <Surname> <Age>. Try again.";
                    }
                case ("list"):
                    return dbm.userList();
                case ("show"):
                    return dbm.show();
                case ("help"):
                    return dbm.help();
                case ("logout"):
                    return dbm.logout();
                case ("exit"):
                    return dbm.exit();
            }
            return "Unexpected command";
    }
}



