package lmao;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;


public class DataBaseManager implements Serializable {
    public static Connection connection;

    static {
        try {

            connection = DBConnect.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static CurrentUser currentUser = new CurrentUser();
    Encryptor encryptor = new Encryptor();


    public DataBaseManager() {
        try {
            CurrentUser currentUser = new CurrentUser();
            connection = DBConnect.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String registration(String acc, String email) {

        if (currentUser.getAccountName() == null) {
            String TO = email;
            String FROM = "trafalgande@gmail.com";
            String PASS = "emailreciever";

            Properties props = System.getProperties();
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            //props.put("mail.smtp.trust", "localhost");
            props.put("mail.smtp.user", FROM);
            props.put("mail.smtp.password", PASS);
            props.put("mail.smtp.port", "587");
            //props.put("mail.smtp.port", "10001");
            props.put("smtp.mail.host", "localhost");
            props.put("mail.smtp.auth", "true");



            PassGenerator passGenerator = new PassGenerator();
            String pass = passGenerator.generatePassword(10);

            try (PreparedStatement prs = connection.prepareStatement
                    ("select accountpass from registrationform where accountname = ?")) {
                prs.setString(1, acc);
                ResultSet rs = prs.executeQuery();
                if (rs.next()) {
                    return "This accountname already exists.";
                } else {
                    try (PreparedStatement preparedStatement = connection.prepareStatement
                            (" INSERT INTO registrationform(accountname, accountpass, accountmail)"
                                    + " VALUES (?,?,?)")) {
                        preparedStatement.setString(1, acc);
                        preparedStatement.setString(2, encryptor.encryptThisString(pass));
                        preparedStatement.setString(3, email);
                        preparedStatement.executeUpdate();
                        currentUser.setAccountPassword(pass);
                        currentUser.setAccountName(acc);
                        PreparedStatement tempPrs = connection.prepareStatement
                                ("SELECT account_id from registrationform where accountname =?");
                        tempPrs.setString(1, currentUser.getAccountName());
                        ResultSet tempRs = tempPrs.executeQuery();
                        if (tempRs.next()) {
                            currentUser.setAccountId(tempRs.getInt("account_id"));
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException mex) {
                System.out.println(mex);
            }
            try {
                Session session = Session.getDefaultInstance(props,
                        new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(FROM, PASS);
                            }
                        });
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(FROM));
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(TO));
                msg.setSubject("Your PostgerSQL password.");
                msg.setSentDate(new Date());
                msg.setText("Thank you for registration!\n" + "Your account is: " + pass + " : " + acc);
                Transport transport = session.getTransport("smtp");
                transport.connect("smtp.gmail.com", FROM, PASS);
                transport.sendMessage(msg, msg.getAllRecipients());
                //Transport.send(msg);
            } catch (MessagingException mex) {
                System.out.println("Send is failed, exception: " + mex);
            }
            StringBuilder output = new StringBuilder();
            output.append("Your account: " + currentUser.getAccountName() + " was created. ");
            output.append("Your password was sent to your e-mail: " + email);
            return output.toString();
        } else {
            return "You have already authentificated";
    }
}


    public String authentification(String acc, String pass) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement
                        ("SELECT account_id FROM registrationform WHERE accountname = ? and accountpass = ?");
                preparedStatement.setString(1, acc);
                preparedStatement.setString(2, encryptor.encryptThisString(pass));
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                        currentUser.setAccountName(acc);
                        currentUser.setAccountPassword(pass);
                        currentUser.setAccountId(rs.getInt("account_id"));
                        return "Welcome " + currentUser.getAccountName() + "! Your ID: " + currentUser.getAccountId();
                } else {
                    return "Wrong password or username.";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return "Type <help> for the reference.";
    }


    public String userList() {
        if (currentUser.getAccountName() != null) {
            StringBuilder sb = new StringBuilder();
            try {
                ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM registrationform");
                String formatString = "%-15s\t%-15s\t%-15s\n";
                sb.append(String.format("%4s   %-25s   %-25s%n", "| USER's ID |", "|   ACCOUNT NAME   |", "|      E-MAIL      |"));
                while (rs.next()) {
                    sb.append(String.format("%-10s\t", rs.getString(3)));
                    sb.append(String.format("%-25s\t", rs.getString(1)));
                    sb.append(String.format("%-30s\n", rs.getString(4)));
                }
            } catch (SQLException mex) {
                System.out.println(mex);
            }
            return sb.toString();
        } else {
            return "You have to be authorized or create account if you haven't.";
        }
    }

    public String show() {
        if (currentUser.getAccountName() != null) {
            StringBuilder sb = new StringBuilder();
            try {
                ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM character");
                sb.append(String.format("%4s  %-15s  %-15s  %-5s  %-15s%n", "| ID |", "| FIRST NAME |", "| SECOND NAME |", "| AGE |", "| TIME |"));
                while (rs.next()) {
                    sb.append(String.format("%-7s\t", rs.getString(3)));
                    sb.append(String.format("%-17s\t", rs.getString(1)));
                    sb.append(String.format("%-15s\t", rs.getString(2)));
                    sb.append(String.format("%-5s\t", rs.getString(5)));
                    sb.append(String.format("%-15s\n", rs.getTime(4)));
                }
            } catch (SQLException mex) {
                System.out.println(mex);
            }
            return sb.toString();
        } else {
            return "You have to be authorized or create account if you haven't.";
        }
    }

    public String help() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("::REFERENCE::\n");
        stringBuilder.append("reg <username> <e-mail> : creates account with ur login.\n");
        stringBuilder.append("login <username> <password> : enters to your account.\n");
        stringBuilder.append("logout : logs out from database.\n");
        stringBuilder.append("add <Name> <Surname> <Age> : adds the element depends on parameters.\n");
        stringBuilder.append("remove <Name> <Surname> <Age> : removes the element depends on parameters.\n");
        stringBuilder.append("list : shows the list of users.\n");
        stringBuilder.append("show : shows data of connected database.\n");
        return stringBuilder.toString();
    }


    public String add(String fname, String sname, int age) throws SQLException {
        if (currentUser.getAccountName() != null) {
            try (PreparedStatement prs = connection.prepareStatement
                    ("select firstname from character where secondname = ?")) {
                prs.setString(1, sname);
                ResultSet rs = prs.executeQuery();
                if (rs.next()) {
                    return "This character already exists.";
                } else {
                    try (PreparedStatement preparedStatement = connection.prepareStatement
                            (" INSERT INTO character(firstname, secondname, age, character_id, time) " + " VALUES (?,?,?,?,current_time)")) {
                        preparedStatement.setString(1, fname);
                        preparedStatement.setString(2, sname);
                        preparedStatement.setInt(3, age);
                        PreparedStatement prs1 = connection.prepareStatement
                                ("SELECT account_id from registrationform where accountname = ? and accountpass = ?");
                        prs1.setString(1, currentUser.getAccountName());
                        prs1.setString(2, encryptor.encryptThisString(currentUser.getAccountPassword()));
                        ResultSet rs1 = prs1.executeQuery();
                        while (rs1.next()) {
                            int cid = rs1.getInt("account_id");
                            preparedStatement.setInt(4, cid);
                        }
                        preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        return "Duplicate value";
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("Element was added.");
            /*sb.append("It's properties:\n");
            sb.append("Name: " + fname + ". Surname: " + sname + ". Age: " + age + ". Time of creation: " +
                    OffsetDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-YYYY hh:mm:ss")));*/
                    return sb.toString();
                }
            }
        } else {
            return "You have to be authorized or create account if you haven't.";
    }
}

    public String remove(String fname, String sname, int age) {
        int Uid = 0, Cid = 0;
        if (currentUser.getAccountName() != null) {
            try {
                PreparedStatement prs = connection.prepareStatement
                        ("SELECT character_id from character where firstname = ? and secondname = ? and age = ?");
                prs.setString(1, fname);
                prs.setString(2, sname);
                prs.setInt(3,age);
                ResultSet rs = prs.executeQuery();
                while (rs.next()) {
                    Cid = rs.getInt("character_id");
                }
            } catch (SQLException mex) {
                System.out.println(mex);
            }
            Uid = currentUser.getAccountId();
            if (Uid == Cid) {
                try (PreparedStatement preparedStatement = connection.prepareStatement
                        ("DELETE from character where firstname = ? and secondname = ? and age = ?")) {
                    preparedStatement.setString(1, fname);
                    preparedStatement.setString(2, sname);
                    preparedStatement.setInt(3, age);
                    preparedStatement.executeUpdate();
                } catch (SQLException mex) {
                    System.out.println("Remove is failed: " + mex);
                    return "No such element.";
                }
            } else {
                return "You don't have permissions to remove this element.";
            }
            return "Element: " + fname + " was removed from data.";
        } else {
            return "You have to be authorized or create account if you haven't.";
        }
    }

    public String logout() {
        if (currentUser.getAccountName() != null) {
            currentUser.setAccountName(null);
            currentUser.setAccountPassword(null);
        } else {
            return "You have to be authorized.";
        }
        return "Bye-bye!";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        return false;
    }

    public String exit() throws IOException {
        Thread.currentThread().interrupt();
        System.err.println("Client is disconnected.");
        System.err.println("Bye-bye");
        StringBuilder sb = new StringBuilder();
        sb.append("You have been disconnected.\n");
        sb.append("Press enter.");
        return sb.toString();
    }
}