import java.util.*;
import java.io.*;

public class UserInterface {
    private Scanner inputScanner;
    private Database crDatabase; //reference of database
    private String  todaysDate;
    
    /****************************************************************************
     * Constructor
     ***************************************************************************/   
    public UserInterface(Database rDatabase) {
        this.inputScanner = new Scanner(System.in);
        this.crDatabase = rDatabase;
    }
  
    /****************************************************************************
     * Setter
     ***************************************************************************/
     public void setTodaysDate(String today)
     {
         this.todaysDate = today;
     }
    
     /****************************************************************************
     * Private methods
     ***************************************************************************/  
     private void displayTitle(String title)
     {
         String output = String.format("// %-65s//", title);
         displayMessage("//////////////////////////////////////////////////////////////////////");
         displayMessage(output);
         displayMessage("//////////////////////////////////////////////////////////////////////");
     }
     

    /****************************************************************************
     * Public methods
     ***************************************************************************/   
     public void clearScreen() {
        System.out.println('\u000c');
    }   
    
    //Normally used by controller
    public void displayMessage(String message)
    {
         System.out.println(message);
    }
    
    public void displayErrorMessage(String message)
    {
         System.out.println("** Error:" + message + "**");
    }   
    
    public String userInput()
    {
        String result = inputScanner.next();
        inputScanner.nextLine();//clear buffer
        return result;
    }
    
    public void finish()
    {
        clearScreen();
    }
    
    /****************************************************************************
     * Display methods
     ***************************************************************************/
    ////////////////////////////////////////////////////////////////////////////
    // Main menu
    ////////////////////////////////////////////////////////////////////////////
    public void showMainMenu(int userType)
    {
        clearScreen();
        displayTitle("Main menu");
        displayMessage("----------[Menu]----------");
     /*   {
            displayMessage("[2] Log out");
            displayMessage("[3] Show menu list");
            displayMessage("[4] Order management");           
            displayMessage("-------[Management]------");
            displayMessage("[5] Show kunden list");
            displayMessage("[6] Kunden management");
            displayMessage("[7] Edit menu items");
            displayMessage("[8] Generate reports");
        }*/
        displayMessage("--------------------------");
       
            displayMessage("[F] Clocked out");
        displayMessage("[Q] Quit");
    }
    ////////////////////////////////////////////////////////////////////////////
    // Login
    ////////////////////////////////////////////////////////////////////////////
     public void loginView()
    {
        clearScreen();
        displayTitle("Login");
    }
    ////////////////////////////////////////////////////////////////////////////
    // Order
    ////////////////////////////////////////////////////////////////////////////
    //---------------------------------------------------------------
    // Order management main menu
    //---------------------------------------------------------------
    public void showOrderMenu()
    {
        clearScreen();
        displayTitle("Order");
        displayMessage("[1] Create order");
        displayMessage("[2] Update order");
        displayMessage("[3] Close order");
        displayMessage("[4] Cancel order");
        displayMessage("[5] Show order list");
        displayMessage("[Q] Back to main menu");
        displayMessage("-----------------------------------------");
    }
    
    public void addMenuItemView()
    {
        clearScreen();
        displayTitle("Add menu item");
    }
    
    public void editMenuItemView(MenuItem rMenuItem)
    {
        clearScreen();
        showMenuItemData(rMenuItem);
        displayMessage("-----------------------------------------");
        displayMessage("[1]:Name");
        displayMessage("[2]:Price");
        displayMessage("[3]:Type");
        displayMessage("[4]:Set promotion price");
        displayMessage("[5]:Reset item state");
        displayMessage("[Q]:Quit");
        displayMessage("-----------------------------------------");
    }
    //---------------------------------------------------------------
    // Edit order detail menu (Create order or Update order)
    //---------------------------------------------------------------
    public void editOrderView()
    {
        clearScreen();
        displayTitle("Edit Order");
        displayMessage("[1] Add new item");
        displayMessage("[2] Delete item");
        displayMessage("[3] Show order detail");
        displayMessage("[Q] Quit");
        displayMessage("-----------------------------------------");
    }
    
    public void addOrderItemView()
    {
        clearScreen();
        displayTitle("Add Order Item");
        sub_showMenuList();
    }
    
    public void deleteOrderItemView()
    {
        clearScreen();
        displayTitle("Delete Order Item");
    }
    
    public void closeOrderView()
    {
        clearScreen();
        displayTitle("Close order");
        showOrderList();
    }
    
    public void cancelOrderView()
    {
        clearScreen();
        displayTitle("Cancel order");
        showOrderList();
    }
    
    public void generateReportView()
    {
        clearScreen();
        displayTitle("Generate reports");
        displayMessage("[1] Sales repors");
        displayMessage("[2] Payment list");
        displayMessage("[Q] Quit");
        displayMessage("-----------------------------------------");
    }
    


    ////////////////////////////////////////////////////////////////////////////
    // Kunden
    ////////////////////////////////////////////////////////////////////////////
    //---------------------------------------------------------------
    // Kunden management main menu
    //---------------------------------------------------------------
    public void addNewKundenView()
    {
        clearScreen();
        displayTitle("Add new staff");
    }
    
    public void staffManagementView()
    {
        clearScreen();
        displayTitle("Edit Kunden");
        displayMessage("[1] Add new staff");
        displayMessage("[2] Update staff info");
        displayMessage("[3] Delete staff info");
        displayMessage("[Q] Back to main menu");
        displayMessage("-----------------------------------------");
    }
    
    public void updateKundenView( Kunden rKunden)
    {
        clearScreen();
        showKundenData(rKunden);
        displayMessage("\nChoose Edit number\n");
        displayMessage("[1]:Last name");
        displayMessage("[2]:First name");
        displayMessage("[3]:Forth clock out");
        displayMessage("[4]:Change start time");
        displayMessage("[5]:Change finish time");
        displayMessage("[Q]:Quit");
    }
    
    ////////////////////////////////////////////////////////////////////////////
    // Menu
    ////////////////////////////////////////////////////////////////////////////
    public void choseEditMenuView()
    {
        clearScreen();
        displayTitle("Edit Menu");
        displayMessage("[1] Add new item");
        displayMessage("[2] Update item");
        displayMessage("[3] Delete item");
        displayMessage("[Q] Quit");
        displayMessage("-----------------------------------------");
    }
      /****************************************************************************
     * Show data lists in the database
     ***************************************************************************/
    //---------------------------------------------------------------
    // Order information
    //---------------------------------------------------------------
    public void showOrderList()
    {
        Iterator<Order> it = crDatabase.getOrderList().iterator();
        String          state;
        String          output;
        double          totalAllOrder = 0;
        int             orderCnt = 0;
        int             cancelCnt = 0;
        double          cancelTotal = 0;
        
        clearScreen();
        displayTitle("Order List (" + todaysDate + ")");
        while (it.hasNext()) {
            Order re = it.next();
            double totalOfEachOrder = re.getTotal();
            switch(re.getState())
            {
                case Order.ORDER_CLOSED:
                    state = "Closed";
                    totalAllOrder += totalOfEachOrder;
                    orderCnt++;
                break;
                case Order.ORDER_CANCELED:
                    state = "Canceled";
                    cancelTotal += totalOfEachOrder;
                    cancelCnt++;
                break;
                default:
                    state = "-";
                    totalAllOrder += totalOfEachOrder;
                    orderCnt++;
                break;
            }
            
            output = String.format("Order ID:%4d  KundenName:%-20s  Total:$%5.2f State:%-8s",
                                            re.getOrderID(),re.getKundenName(),totalOfEachOrder,state);
            displayMessage(output);
        }
        displayMessage("-------------------------------------------------------");
        displayMessage("Total sales:$" + totalAllOrder + "(" + orderCnt + ")" +
                    "  Canceled:$" + cancelTotal + "(" + cancelCnt + ")\r\n");
    }
    
    public void showOrderDetail( int orderID)
    {
        Order rOrder = crDatabase.findOrderByID(orderID);
        double totalCharge = 0;
        
        displayTitle("Order Details");
        displayMessage("OrderID:" + orderID);
        displayMessage("Order Date:" + todaysDate);
        displayMessage("Kunden Name:" + rOrder.getKundenName());
        displayMessage("");
        
        String output;
        output = String.format("%-4s %-9s %-24s %-9s %s","No.", "Menu ID", "Menu Name", "Quantity", "Charge");
        displayMessage(output);

        Iterator<OrderDetail> it = rOrder.getOrderDetail().iterator();
        OrderDetail    re;
        int count = 1;
        

        while (it.hasNext()) {
            re = it.next();
            double totalPliceOfItem = re.getTotalPrice();
            output = String.format("%-4d|%-9s|%-24s|%9d|%5.2f",
                                    count, re.getItemID(), re.getItemName(), re.getQuantity(), totalPliceOfItem);
            displayMessage(output);
            totalCharge += totalPliceOfItem;
            count++;
        }
        if(count == 0)
            displayMessage("No item");
        else
        {
            output = String.format("%-6.2f", totalCharge);
            displayMessage("--------------------------------------------------------");
            displayMessage("                                    Total charge:" + output);
        }
    }   
     //---------------------------------------------------------------
    // Kunden information
    //---------------------------------------------------------------   
    public void showKundenData( Kunden rKunden)
    {
        displayMessage("*************** Kunden Data ***************");
        displayMessage("Kunden last name:" + rKunden.getNachname());
        displayMessage("Kunden first name:" + rKunden.getVorname());
    }
    

    public void showKundenList()
    {
        clearScreen();
        Iterator<Order> it = crDatabase.getKundenList().iterator();
        List<Kunden> kun = new ArrayList<Kunden>();

        
        displayTitle("Kunden List");
        
    }
    public static void printList(List<Kunden> list) {

        System.out.println("Kunden: ");

        for (Kunden kunden : list) {
            System.out.printf("%s", kunden);
        }
        System.out.println();
    }
    public void showMenuList()
    {
        clearScreen();
        displayTitle("Menu List");
        sub_showMenuList();
    }
    
    private void sub_showMenuList()
    {
        Iterator<MenuItem> it = crDatabase.getMenuList().iterator();
        
        while (it.hasNext()) {
            MenuItem re = (MenuItem)it.next();
             byte menuType = re.getType();
             String strMenuType;
            switch( menuType)
            {
                case MenuItem.MAIN:
                strMenuType = "Main";
                break;
                case MenuItem.DRINK:
                strMenuType = "Drink";
                break;
                case MenuItem.ALCOHOL:
                strMenuType = "Alcohol";
                break;
                case MenuItem.DESSERT:
                strMenuType = "Dessert";
                break;
                default:
                strMenuType = "Undefined";
                break;
            }
            String output = String.format("Menu ID:%4d  Name:%-20s  Price:%5.2f Type:%s",
                                            re.getID(),re.getName(),re.getPrice(),strMenuType);
           if(re.getState() == MenuItem.PROMOTION_ITEM)
           {
               output += " ** Today's Special!! **";
            }
            displayMessage(output);
        }
    }
    
    //Display menu item detail
    public void showMenuItemData( MenuItem rMenuItem)
    {
        displayTitle("Menu item Data");
        //displayMessage("******** Menu item Data *********");
        displayMessage("Name:" + rMenuItem.getName());
        
        if(rMenuItem.getState() == MenuItem.PROMOTION_ITEM)
        {
            displayMessage("Promotion Price:" + rMenuItem.getPrice());
        }
        else
        {
            displayMessage("Price:" + rMenuItem.getPrice());
        }
        
        byte menuType = rMenuItem.getType();
        switch( menuType)
        {
            case MenuItem.MAIN:
            displayMessage("Type:Main");
            break;
            case MenuItem.DRINK:
            displayMessage("Type:Drink");
            break;
            case MenuItem.ALCOHOL:
            displayMessage("Type:Alcohol");
            break;
            case MenuItem.DESSERT:
            displayMessage("Type:Dessert");
            break;
            default:
            displayMessage("Type:Undefined");
            break;
        }
        if(rMenuItem.getState() == MenuItem.PROMOTION_ITEM)
            displayMessage("State: Today's special!!");
        else
            displayMessage("State: Regular item");
    }
}
