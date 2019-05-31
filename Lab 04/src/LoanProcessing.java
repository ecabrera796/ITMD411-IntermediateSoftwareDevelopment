import java.sql.ResultSet;
public class LoanProcessing extends BankRecords {
    private static BankRecords mInstance = new BankRecords();
    public static void main(String[] args){
        mInstance.readData();
        BankRecords[] robjs =  mInstance.getRobjs().toArray(new BankRecords[0]);
        Dao dao = new Dao();
        dao.createTable();
        dao.insertRecords(robjs);
        ResultSet resultSet = dao.retrieveRecords();
        printTableResults(resultSet);
    }
    //Put results in a table
    private static void printTableResults(ResultSet rs) {
        String heading = String.format("%-8s\t%-12s\t%-4s\n", "ID","INCOME","PEP");
        System.out.println("Loan Analysis Report");
        System.out.println(heading);
        try {
        	//Extract data from result set
            while (rs.next()) {
                //Retrieve by column id/name
                String id = rs.getString(2);
                double income = rs.getDouble(3);
                String pep = rs.getString(4);
                //Display values
                System.out.println(String.format("%-8s\t%-12.2f\t%-4s", id, income, pep));
            }
            rs.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}