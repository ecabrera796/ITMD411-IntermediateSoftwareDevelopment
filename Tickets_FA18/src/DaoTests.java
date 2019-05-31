/*
 * Erick Cabrera
 * Dec. 9, 2018
 * DaoTests.java
 * Tickets_FA18
 */

import org.junit.jupiter.api.Test;
import java.sql.ResultSet;
import java.sql.Statement;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

public class DaoTests {

    @Test
    // checks for db
    public void daoGetInstanceIsNotNull(){
        assertNotNull(Dao.getInstance());
    }

    @Test
    // checks db connection
    public void daoGetConnectionIsNotNull(){
        assertNotNull(Dao.getConnection());
    }

    @Test
    // checks for errors when creating tables
    public void daoCreateTablesNoAlter_DoesNotThrow(){
        Dao.getInstance().exceptionCaught = false;
        Dao.getInstance().createTables(false);
        assertFalse(Dao.getInstance().exceptionCaught);
    }

    @Test
    // check for errors when creating altered tables
    public void daoCreateTablesAlter_DoesNotThrow(){
        Dao.getInstance().exceptionCaught = false;
        Dao.getInstance().createTables(true);
        assertFalse(Dao.getInstance().exceptionCaught);
    }

    @Test
    // check for errors when adding users
    public void daoAddUsers_DoesNotThrow(){
        Dao.getInstance().exceptionCaught = false;
        Dao.getInstance().addUsers();
        assertFalse(Dao.getInstance().exceptionCaught);
    }

    @Test
    // check for errors when adding tickets with no description
    public void daoAddTicket_WithNullDescr_DoesNotThrow(){
        Dao.getInstance().exceptionCaught = false;
        Login.LoggedInUser = new User("tester", "user");
        Dao.getInstance().addTicket(null);
        assertFalse(Dao.getInstance().exceptionCaught);
    }

    @Test
    // check for errors when adding tickets with no user
    public void daoAddTicket_WithNullUser_DoesThrow(){
        Dao.getInstance().exceptionCaught = false;
        Login.LoggedInUser = null;
        Dao.getInstance().addTicket("A Test Ticket");
        assertTrue(Dao.getInstance().exceptionCaught);
    }

    @Test
    // check for errors when deleting tickets with invalid ID
    public void daoRemoveTicket_WithInvalidId_ShouldNotThrow(){
        Dao.getInstance().exceptionCaught = false;
        Dao.getInstance().removeTicket(-1);
        assertFalse(Dao.getInstance().exceptionCaught);
    }

    @Test
    // check for errors when deleting tickets with valid ID 
    public void daoRemoveTicket_WithValidId_ShouldNotThrow(){
        Dao.getInstance().exceptionCaught = false;
        Login.LoggedInUser = new User("tester", "user");
        int id = Dao.getInstance().addTicket("A ticket to be removed");
        Dao.getInstance().removeTicket(id);
        assertFalse(Dao.getInstance().exceptionCaught);
    }


    @Test
    // check for errors when updating tickets with invalid ID
    public void daoUpdateTicket_WithInvalidId_ShouldThrow(){
        Dao.getInstance().exceptionCaught = false;
        Dao.getInstance().updateTicket(-1, null, null, false);
        assertTrue(Dao.getInstance().exceptionCaught);
    }

    @Test
    // ensure tickets are added
    public void daoAddTicket_ShouldAddATicketToDB(){
        Login.LoggedInUser = new User("tester", "user");
        int id = Dao.getInstance().addTicket("A ticket to be removed");
        try {
            Statement statement = Dao.getConnection().createStatement();
            ResultSet results = statement.executeQuery("select * from erickCab_tickets where ticket_id = '"+id+"'");
            while(results.next()){
                String issuer = results.getString(2);
                String desc = results.getString(3);
                String status = results.getString(4);

                assertTrue(desc.equalsIgnoreCase("A ticket to be removed") &&
                        issuer.equalsIgnoreCase("tester") &&
                        status.equalsIgnoreCase("Open"));
                return;
            }
            fail();

        }catch(Exception ex){
            fail();
        }

    }
}
