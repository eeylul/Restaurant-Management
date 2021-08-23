import java.util.*;
import java.text.*;

public class Controller_GUI
{
    private Database    cDatabase;

    private String      errorMessage;
    
    public Controller_GUI()
    {
        this.cDatabase = new Database();
        try
        {
            cDatabase.loadFiles();
        }
        catch(DatabaseException de)
        {
            System.out.println(de.getErrMessage());
            System.exit(0);
        }
        
        
    }
    
    private void  setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }
    
    public String  getErrorMessage()
    {
        String result = this.errorMessage;
        this.errorMessage = "";
        return result;
    }
    
  
    
    public int getOrderState(int orderID)
    {
        return cDatabase.getOrderState(orderID);
    }
    
   
     /***********************************************************
     * Menuverwaltung
     **********************************************************/
    public boolean addNewMenuItem(int newID, String newName, double newPrice, byte menuType)
    {
        MenuItem rMenuItem = cDatabase.findMenuItemByID(newID);
        if(rMenuItem != null)
        {
            setErrorMessage("ID:" + newID + " ist schon benutzt von " + rMenuItem.getName());
            return false;
        }
        
        try
        {
            cDatabase.addMenuItem(newID, newName, newPrice, menuType);
            return true;
        }
        catch(DatabaseException de)
        {
            setErrorMessage(de.getErrMessage());
            return false;
        }
    }
    
    public boolean updateMenuItem(int id, String newName, double newPrice, byte menuType)
    {
        try
        {
            cDatabase.editMenuItemData(id, newName, newPrice, menuType);
            return true;
        }
        catch(DatabaseException de)
        {
            setErrorMessage(de.getErrMessage());
            return false;
        }
    }
    
    public boolean deleteMenuItem(int id)
    {
        MenuItem rMenuItem= cDatabase.findMenuItemByID(id);
        if(rMenuItem == null)
        {
            setErrorMessage("Menu Gericht ID:" + id + " ist nicht gefunden.");
            return false;
        }
        
        try
        {
            cDatabase.deleteMenuItem(rMenuItem);
        }
        catch(DatabaseException de)
        {
            setErrorMessage(de.getErrMessage());
            return false;
        }
        return true;
    }
     
    public MenuItem    getMenuItemData(int menuItemID)
    {
        return cDatabase.findMenuItemByID(menuItemID);
    }
     /***********************************************************
     * Bestellungverwaltung
     **********************************************************/
    public int createOrder()
    {
        return cDatabase.addOrder();
    }
    
    public boolean addNewOrderItem(int orderID, int addItemID, byte addItemQuantity)
    {
        Order rOrder = cDatabase.findOrderByID(orderID);
        
        MenuItem    rNewItem = null;
        
        rNewItem = cDatabase.findMenuItemByID(addItemID);
        if(rNewItem == null)
        {
            setErrorMessage("MenuID[" + addItemID + "]ist nicht gefunden.");
            addItemID = 0;
            return false;
        }
        

         cDatabase.addOrderItem(orderID, rNewItem, addItemQuantity);
         return true;
    }
    
    public boolean deleteOrderItem(int orderID, int deleteNo)
    {
        Order rOrder = cDatabase.findOrderByID(orderID);
       
        deleteNo -=1;  
        if(!cDatabase.deleteOrderItem(orderID, deleteNo))
        {

            setErrorMessage("Nicht gefunden.");
            return false;
        }
        return true;
    }
    
    public boolean closeOrder(int closeOrderID)
    {
        Order rOrder = cDatabase.findOrderByID(closeOrderID);
        
        if(rOrder.getState() != 0)
        {
            setErrorMessage("Die Bestellung ist schon geschlossen. ");
            return false;
        }
        cDatabase.closeOrder(closeOrderID);

        return true;
    }
    
 
    
    /***********************************************************
     * Erstellen Stringsliste
     **********************************************************/
   
    public ArrayList<String>  createOrderList()
    {
        Iterator<Order> it = cDatabase.getOrderList().iterator();
        String          state;
        ArrayList<String> initData = new ArrayList<String>();
        String          output;
        
        while (it.hasNext()) {
            Order re = it.next();
            switch(re.getState())
            {
                case Order.ORDER_CLOSED:
                    state = "geschlossen";
                break;
                default:
                    state = "-";
                break;
            }
            
            output = String.format("Order ID:%4d Total:$%5.2f State:%-8s\n",
                                            re.getOrderID(),re.getTotal(),state);
            initData.add(output);
        }
        if(initData.isEmpty())
            initData.add("Keine Bestellung.");
        return initData;
    }
    
    public ArrayList<String> createOrderItemlList(int orderID)
    {
        Order rOrder = cDatabase.findOrderByID(orderID);
        ArrayList<String> initData = new ArrayList<String>();

        if(rOrder == null)
        {
            initData.add("Keine Bestellung Information");
            return initData;
        }
        
        String output;

        Iterator<OrderDetail> it = rOrder.getOrderDetail().iterator();
        OrderDetail    re;
        
        int count = 0;
        
        while (it.hasNext()) {
            re = it.next();
            output = String.format("%-4d|%-24s|%5d|%5.2f",
                                    ++count, re.getItemName(), re.getQuantity(), re.getTotalPrice());
           initData.add(output);
        }
        if(initData.isEmpty())
            initData.add("Kein Gericht.");
        return initData;
    }   
    
    public ArrayList<String> createMenuList(int disuplayMenuType)
    {
        Iterator<MenuItem> it = cDatabase.getMenuList().iterator();
        ArrayList<String> initData = new ArrayList<String>();
        
        while (it.hasNext()) {
            MenuItem re = (MenuItem)it.next();
             byte menuType = re.getType();
             if(disuplayMenuType!= 0 && disuplayMenuType != menuType)
                continue;
             String strMenuType;
            switch( menuType)
            {
                case MenuItem.MAIN:
                strMenuType = "Main";
                break;
                case MenuItem.DRINK:
                strMenuType = "Trinken";
                break;
                case MenuItem.ALCOHOL:
                strMenuType = "Alkohol";
                break;
                case MenuItem.DESSERT:
                strMenuType = "Dessert";
                break;
                default:
                strMenuType = "Undefiniert";
                break;
            }
            String output = String.format("Menu ID:%4d  Name:%-20s  Preis:%5.2f Typ:%s",
                                            re.getID(),re.getName(),re.getPrice(),strMenuType);
           if(re.getState() == MenuItem.PROMOTION_ITEM)
           {
               output += " ** Babur Somer's Special!! **";
            }
            
            initData.add(output);
        }
        if(initData.isEmpty())
            initData.add("Keine Bestellung.");
        return initData;
    }

	public double getOrderTotalCharge(int currentOrderID) {

		return 0;
	}

	public ArrayList<String> createKundenList() {
		return null;
	}

	public Kunden getKundenData(int kundenID) {
		return null;
	}
    
}
