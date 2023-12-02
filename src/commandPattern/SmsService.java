package commandPattern;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SmsService {
    static public void emailMsg(String title, String msg){
        String from = "kuspis.yaromir2@gmail.com";
        String appPassword = "vnhg uybz gbzf cagi";
        String to = "yaromyroleh.kuspis.oi.2023@lpnu.ua";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(from, appPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(title);
            message.setText(msg);

            Transport.send(message);

            System.out.println("Email sent successfully.");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    static int count = 0;
    public static void writeLineToFile(String line) {
        try {
            line = count + ". " + line;
            if(count == 0){
                line = "New run of the program:\n" + line;
            }
            count++;

            File file = new File("logger.txt");

            // If the file doesn't exist, create a new file
            if (!file.exists()) {
                file.createNewFile();
            }

            // Open the file in append mode
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);

            // Write the given line as a new line
            bw.write(line);
            bw.newLine();

            // Close the BufferedWriter
            bw.close();
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }
}
