import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
public class restock {
 private final static String url = "jdbc:mysql://localhost:3306/PharmacyInventory";
   private  final static   String username = "root";
   private  final static  String password = "kanishka";

    public static void main(String[] args) {
        restock(true , "Paracetamol", 200);
    }
    private static void restock(boolean confirmation,String MedicineName,int QuantityRestocked){
        if(confirmation==true){
String query = "UPDATE MedicineInventory SET Quantity = Quantity+? WHERE MedicineName = ?";
try{
    Connection con = DriverManager.getConnection(url,username,password);
    PreparedStatement p = con.prepareStatement(query);
    p.setInt(1,QuantityRestocked);
    p.setString(2, MedicineName);
    int row = p.executeUpdate();
    if(row>0){
        System.out.println("Restocked the Database");

    }
    else{
        System.out.println("Error in Restocking ");
    }

}catch(SQLException e){
    e.printStackTrace();
}
        }
    }
}
