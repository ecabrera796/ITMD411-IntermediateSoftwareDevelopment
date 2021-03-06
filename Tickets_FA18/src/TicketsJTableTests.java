/*
 * Erick Cabrera
 * Dec. 9, 2018
 * TicketsJTableTests.java
 * Tickets_FA18
 */

import org.junit.jupiter.api.Test;
import javax.swing.table.DefaultTableModel;
import java.sql.Statement;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

public class TicketsJTableTests {

    @Test
    // check for errors when creating tables
    public void tjtNullResults_ShouldThrow(){
        try{
            TicketsJTable.buildTableModel(null);
            fail();
        }catch (Exception ex){
            assertNotNull(ex);
        }
    }

    @Test
    // ensure everything is valid
    public void tjtValidResults_ShouldThrow(){
        try{
            Statement statement = Dao.getConnection().createStatement();
            DefaultTableModel table = TicketsJTable.buildTableModel(statement.executeQuery("select * from erickCab_tickets"));
            assertNotNull(table);
        }catch (Exception ex){
            fail();
        }
    }
}
