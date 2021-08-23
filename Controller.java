import java.util.*;
import java.io.*;
import java.text.*;
 
public class Controller
{
    public final static int SCENE_MAIN_MENU = 0;
    public final static int SCENE_MENU_LIST = 1;
    public final static int SCENE_ORDER = 2;
    public final static int SCENE_EDIT_MENU_ITEM = 3;
    public final static int SCENE_GENERATE_REPORTS = 4;
    
    private UserInterface cView;
    private Database    cDatabase;
    
    private int scene;
    private int state;
    private String todaysDate;
    
    public Controller()
    {
        this.cDatabase = new Database();
        this.cView = new UserInterface(this.cDatabase);
        this.scene = SCENE_MAIN_MENU;
        
        Date date = new Date();
        SimpleDateFormat stf = new SimpleDateFormat("yyyy/MM/dd");
        todaysDate = stf.format(date);
        cView.setTodaysDate(todaysDate);
        
    }
    
    /***********************************************************
     * Main menu 
     **********************************************************/
    
    public void mainLoop()
    {
         while( state == 0)
        {
            switch( this.scene)
            {
                case SCENE_MAIN_MENU:
                   selectOrderMenu();
                   break;
                case SCENE_MENU_LIST:
                    showMenuList();
                    break;
                case SCENE_ORDER:
                    selectOrderMenu();
                    break;
                case SCENE_EDIT_MENU_ITEM:
                    chooseEditMenuItemMode();
                    break;
                default:
                    this.scene = SCENE_MAIN_MENU;
                    break;
            }
        }
        
        cView.finish();
    }

     /***********************************************************
     * Bestellungsarten
     **********************************************************/
    private void selectOrderMenu()
    {
        String  key;
        int     inputNumber = 0;
        
 
        switch(inputNumber)
        {
            case 1:
                createOrder();
            break;

            case 2:
                closeOrder();
            break;

            case 3:
                showOrderList();

            break;
 
        }
    }
    
    private void showOrderList()
    {
        boolean done = false;
        String key;
        
        while(!done)
        {
            cView.showOrderList();

            key = cView.userInput();
            if(key.equalsIgnoreCase("Q"))
            {
                return;
            }
            
            try{
                int ID = Integer.parseInt(key);
                Order rOrder = cDatabase.findOrderByID(ID);
                if( rOrder == null)
                {
                    printErrorMessageToView("Nicht gefunden.");
                    pause(2);
                }
                else
                {
                    cView.clearScreen();
                    cView.showOrderDetail(ID);
                    printMessageToView("Bitte geben Sie etwas ein, um zu beenden.");
                    key = cView.userInput();
                    done = true;
                }                
            }
            catch(Exception e)
            {
                printErrorMessageToView("Geben Sie ein integer ein.");
                pause(2);
            }
        }
    }
    
    private void printMessageToView(String string) {
	
	}

	private void createOrder()
    {
        int newOrderID = cDatabase.addOrder();

    }
    

	public void addNewOrderItem(int orderID)
    {
        boolean done = false;
        int addItemID = 0;
        byte addItemQuantity = 0;
        MenuItem    rNewItem = null;
        String key;
            
        while(!done)
        {
            cView.addOrderItemView();
            while(addItemID == 0)
            {
                try
                {
                    printMessageToView("Waehlen Sie MenuID:");
                    key = cView.userInput();
                    if(key.equalsIgnoreCase("Q"))
                    {
                        printMessageToView("Transaktion ist abgebrochen.");
                        pause(2);
                        return;
                    }
                    addItemID = Integer.parseInt(key);
                    rNewItem = cDatabase.findMenuItemByID(addItemID);
                    if(rNewItem == null)
                    {
                        printErrorMessageToView("MenuID[" + addItemID + "]ist nicht gefunden.");
                        addItemID = 0;
                    }
                }
                catch(Exception e)
                {
                    printErrorMessageToView("Geben Sie id nummer ein.");
                }
            }
            
            while(addItemQuantity == 0)
            {
                try
                {
                    printMessageToView("Geben Sie Menge:");
                    key = cView.userInput();
                    if(key.equalsIgnoreCase("Q"))
                    {
                        printMessageToView("Transaktion ist abgebrochen.");
                        pause(2);
                        return;
                    }
                    addItemQuantity = Byte.parseByte(key);
                    if( addItemQuantity <= 0)
                    {
                        printErrorMessageToView("Geben Sie eine positive nummer.");
                        addItemQuantity = 0;
                    }
                }
                catch(NumberFormatException nfe)
                {
                    printErrorMessageToView("Menge ist so gross!");
                }
                catch(Exception e)
                {
                    printErrorMessageToView("Geben Sie eine gültige nummer ein.");
                }
            }
            
            printMessageToView("MenuID:" + addItemID + " MenuName:" + rNewItem.getName() 
                        + " Menge:" + addItemQuantity);
            
            printMessageToView("OK?(JA:j)");
            key = cView.userInput();
            if(!key.equalsIgnoreCase("J"))
            {
                printMessageToView("Abgebrochen.");
                addItemID = 0;
                addItemQuantity = 0;
                rNewItem = null;
                continue;
            }
            

            cDatabase.addOrderItem(orderID, rNewItem, addItemQuantity);
            
            printMessageToView("Möchten Sie Gericht hinzufügen?(JA:j)");
            key = cView.userInput();
            if(!key.equalsIgnoreCase("J"))
            {
                done = true;
            }
            else
            {
                addItemID = 0;
                addItemQuantity = 0;
                rNewItem = null;
            }
        }
    }
 
    
    private void printErrorMessageToView(String string) {
		
	}

	private void closeOrder()
    {
        cView.closeOrderView();
        
        int closeOrderID = findOrder();
        if(closeOrderID == -1) return;
        
        Order rOrder = cDatabase.findOrderByID(closeOrderID);
        
        if(rOrder.getState() != 0)
        {
            printMessageToView("Die Bestellung ist bereits geschlossen.");
            pause(2);
            return;
        }
        
        printMessageToView("Möchten Sie diese Bestellung schließen?(JA:j)");
        String key = cView.userInput();
        
        if(key.equalsIgnoreCase("J"))
        {
            cDatabase.closeOrder(closeOrderID);
            printMessageToView("Die Bestellung wurde geschlossen.");
            pause(2);
        }
    }
    
  
     private int findOrder() {
		return 0;
	}

	/***********************************************************
     * Menüelementmodus bearbeiten
     **********************************************************/
    private void chooseEditMenuItemMode()
    {
        String  key;
        int     inputNumber = 0;
        
        cView.choseEditMenuView();
        printMessageToView("Waehlen Sie eine Nummer:");
        key = cView.userInput();
        
        if(key.equalsIgnoreCase("Q"))
        {
            scene = SCENE_MAIN_MENU;
            return;
        }
        
        while(inputNumber == 0)
        {
            try
            {
                inputNumber = Integer.parseInt(key);     
                switch(inputNumber)
                {
                    case 1:
                        addNewMenuItem();
                    break;
                    case 2:
                        deleteMenuItem();
                    break;
                    default:
                        printMessageToView("Waehlen Sie 1 oder 2:");
                        key = cView.userInput();
                    break;
                }
            }
            catch(Exception e)
            {
                printMessageToView("Waehlen Sie 1 oder 2:");
                key = cView.userInput();
            }
        }
    }
    
    //----------------------------------------------------------
    // Make and check new ID (used by addMenuItem method only)
    //----------------------------------------------------------
    private int generateMenuID()
    {
        int newID = 0;
        String key;
        
        printMessageToView("Choose ID for new item:");
        key = cView.userInput();
        
        while(newID == 0)
        {
            //Back to main menu
            if(key.equalsIgnoreCase("Q"))
                return 0;
            try
            {
                newID = Integer.parseInt(key);
                if(newID > 9999)
                {
                    printMessageToView( "Bitte geben Sie weniger als 10000 ein.");
                    key = cView.userInput();
                    newID = 0;
                }
                else
                {
                    //Check if there is same ID
                    MenuItem   rMenuItem = cDatabase.findMenuItemByID(newID);
                    //if(found)
                    if(rMenuItem != null)
                    {
                        printMessageToView( "ID:" + newID + "ist bereits verwendet von " + rMenuItem.getName());
                        printMessageToView("Bitte versuchen Sie es mit einer anderen Nummer:");
                        key = cView.userInput();
                        newID = 0;
                    }
                }
            }
            catch(Exception e)
            {
                printMessageToView("Bitte waehlen Sie ein integer.");
                key = cView.userInput();
            }

        }
        return newID;
    }
    //----------------------------------------------------------
    // Neue Gerichte hinzufügen
    //----------------------------------------------------------
    private void addNewMenuItem()
    {
        int newID=0;
        String   newName;
        double  newPrice;
        byte     newType;
        String key;
        
        cView.addMenuItemView();
        
        boolean done = false;
        while(!done)
        {
            newID = generateMenuID();
            if (newID == 0)
            {
                return;
            }

            printMessageToView("Geben Sie Gerichtsname:");
            newName = cView.userInput();
            
            newPrice = 0;
            printMessageToView("Geben Sie Preis:");
            key = cView.userInput();
            while(newPrice == 0)
            {
                try
                {
                    newPrice = Double.parseDouble(key);
                    if(newPrice <= 0)
                    {
                        printMessageToView("Geben Sie eine positive Nummer:");
                        key = cView.userInput();
                        newPrice = 0;
                    }
                }
                catch(Exception e)
                {
                    printMessageToView("Geben Sie eine gültige Nummer:");
                    key = cView.userInput();
                }
            }
            
            newType = 0;
            printMessageToView("Geben Sie Gerichte(1:MAIN 2:GETRAENK 3:ALKOHOL 4:DESSERT):");
            key = cView.userInput();
            while(newType == 0)
            {
                try
                {
                    newType = Byte.parseByte(key);
                    if(newType < 1 || 4< newType)
                    {
                        printMessageToView("Geben Sie 1 bis 4 ein:");
                        key = cView.userInput();
                        newType = 0;
                    }
                }
                catch(Exception e)
                {
                    printMessageToView("Geben Sie eine gültige Nummer:");
                    key = cView.userInput();
                }
            }
            
            printMessageToView("NeueID:" + newID);
            printMessageToView("Neue Gerichtsname:" + newName);
            printMessageToView("Neuer Gerichtspreise:" + newPrice);
            switch(newType)
            {
                case MenuItem.MAIN:
                    printMessageToView("Neue Gerichttype:MAIN");
                    break;
                case MenuItem.DRINK:
                    printMessageToView("Neue Gerichttype:GETRAENK");
                    break;
                case MenuItem.ALCOHOL:
                    printMessageToView("Neue Gerichttype:ALKOHOL");
                    break;
                case MenuItem.DESSERT:
                    printMessageToView("Neue Gerichttype:DESSERT");
                    break;
            }   
            
            printMessageToView("\nOK? (J:JA)");
            key = cView.userInput();
            
            if(key.equalsIgnoreCase("J"))
            {
                try
                {
                    cDatabase.addMenuItem(newID, newName, newPrice, newType);
                    printMessageToView("Neue Gerichttype hat hinzugefügt.");
                }
                catch(DatabaseException dbe)
                {
                    printErrorMessageToView("Hinzufügen fehlt.");
                }
                
                done = true;
            }
        }
        
        printMessageToView("Bitte geben Sie etwas ein, um zu beenden.");
        key = cView.userInput();
    }
    
    //----------------------------------------------------------
    // Gerichte löschen
    //----------------------------------------------------------
    private void deleteMenuItem()
    {
        String  key;
        int     inputNumber = 0;
        MenuItem   rMenuItem = null;
        
         while(inputNumber == 0)
        {
            try
            {
  
                printMessageToView("Gericht ID:" + rMenuItem.getID());
                printMessageToView("Name:" + rMenuItem.getName());
                printMessageToView("Preis:" + rMenuItem.getPrice());
                printMessageToView("Möchten Sie löschen? (JA:J)");
                
                key = cView.userInput();
                if(!key.equalsIgnoreCase("J"))
                {
                    printMessageToView("Die Transaktion ist abgebrochen.");
                    pause(2);

                    return;
                }
                
                cDatabase.deleteMenuItem(rMenuItem);
                    
                printMessageToView("Gelöscht.");
                pause(2);
            }
            catch(Exception e)
            {
                printMessageToView("ID muss eine gültige Nummer.");
                pause(2);
            }
        }
    }
    
    private void pause(int i) {
		
		
	}

	private void showMenuList()
    {
        cView.showMenuList();
        printMessageToView("Geben Sie bitte etwas, um zu beenden.");
        cView.userInput();

        scene = SCENE_MAIN_MENU;

    }
}
