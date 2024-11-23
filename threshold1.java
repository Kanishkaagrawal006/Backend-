import java.sql.*;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class threshold1 {
    private final  static String url="jdbc:mysql://localhost:3306/PharmacyInventory";
    private  final static   String username = "root";
    private  final static  String password = "kanishka";
    private final static  int TIMER_INTERVAL= 360000;
    private static Session s ;
    private static String  EMAIL = "";
    private static String SupplierEmail;
    private static String MedicineName;
    private static int  Quantiity;
    private static int MedicineID;
    private static String key = "";
    public static void main(String[] args) {
       Timer t = new Timer();
       t.schedule(new TimerTask() {

        @Override
        public void run() {
            checkandnotify();
                    }
            
        
       },0, TIMER_INTERVAL);
}
private static  void checkandnotify(){
    Connection con = null;
    Statement stml = null;
    ResultSet rs = null;
    try{
         con = DriverManager.getConnection(url, username, password);
        String Query = "SELECT MedicineName,MedicineID,Quantity,SupplierEmail FROM MedicineInventory WHERE Quantity<=Threshold";
         stml = con.createStatement();
        rs = stml.executeQuery(Query);
        if (!rs.next()) {
            System.out.println("No medicines below threshold.");
            return;
        }
        do{
             MedicineID = rs.getInt("MedicineID");
            Quantiity = rs.getInt("Quantity");
            MedicineName = rs.getString("MedicineName");
            SupplierEmail = rs.getString("SupplierEmail");
        sendEmail();
                }while(rs.next());
            }catch(SQLException e){
                e.printStackTrace();
            }
            finally {
                try {
                    if (rs != null) rs.close();
                    if (stml != null) stml.close();
                    if (con != null) con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        private static void sendEmail() {
            Properties prop = System.getProperties();
            prop.put("mail.smtp.port", "587");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.debug", "true");

            Authenticator auth = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL,key);
                }
            };
            Session s = Session.getInstance(prop, auth);
            try {
                // Create a MimeMessage
                MimeMessage message = new MimeMessage(s);
                message.setFrom(new InternetAddress(EMAIL)); 
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(SupplierEmail));
                message.setSubject("Low Stock Alert: " + MedicineName); // Email subject
                message.setText(
                    "Dear Supplier,\n\n" +
                    "The stock for the medicine " + MedicineName + " is low.\n" +
                    "Current Quantity: " + Quantiity + ".\n" +
                    "Please replenish the stock at the earliest.\n\n" +
                    "Thank you."
                );
    
                // Send the message
                Transport.send(message);
    
                System.out.println("Email sent successfully!");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
