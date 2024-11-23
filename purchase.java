import java.sql.*;
public class purchase {
    private static String url= "jdbc:mysql://localhost:3306/PharmacyInventory";
   private  final static   String username = "root";
   private  final static  String password = "kanishka";
   private static String query = "UPDATE MedicineInventory SET Quantity = Quantity- ? WHERE MedicineName = ?";

    public static void main(String[] args) {
        pruchase("Paracetamol", 2);
    }
    static void pruchase(String MedicineName,int QuantiityPurchased){
        try{
            Connection con = DriverManager.getConnection(url, username, password);
        PreparedStatement pre = con.prepareStatement(query);
        pre.setInt(1,QuantiityPurchased);
        pre.setString(2, MedicineName);
        int row = pre.executeUpdate();
        if(row>0){
            System.out.println("Medicine is Purchased");
        }
        else{
            System.out.println("Error");
        }
        }catch(SQLException e){
            e.printStackTrace();
        }
        
        
    }
}
