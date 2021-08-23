import java.util.*;
import java.io.*;
import java.lang.*;

public class Database
{
    private final static String MENU_FILE = "dataFiles/menu_item.txt";

    private ArrayList<MenuItem> menuList = new ArrayList<MenuItem>();
    private ArrayList<Order> orderList = new ArrayList<Order>();
    
    
    
    private Date  date;
    int     todaysOrderCounts;
   
    public Database()
    {
        date = new Date();
        todaysOrderCounts = 0;
    }
     
     public ArrayList<MenuItem> getMenuList()
     {
         return menuList;
     }
     
     public ArrayList<Order> getOrderList()
     {
         return orderList;
     }
    
    //----------------------------------------------------------
    // Gerichte aus ID finden
    //----------------------------------------------------------
    public MenuItem   findMenuItemByID(int id)
    {
        Iterator<MenuItem> it = menuList.iterator();
        MenuItem           re = null;
        boolean         found = false;
        
        if(id < 0){
            return null;
        }
        
        while (it.hasNext() && !found) {
            re = (MenuItem)it.next();  
            if( re.getID() == id)
            {
                found = true;
            }
        }
        
        if(found)
            return re;
        else        
            return null;
    }
    
    //----------------------------------------------------------
    // Bestellung aus ID finden
    //----------------------------------------------------------
    public Order   findOrderByID(int id)
    {
        Iterator<Order> it = orderList.iterator();
        Order           re = null;
        boolean         found = false;
        
        if(id < 0){
            return null;
        }
        
        while (it.hasNext() && !found) {
            re = it.next();  
            if( re.getOrderID() == id)
            {
                found = true;
            }
        }
        
        if(found)
            return re;
        else        
            return null;
    }

     //---------------------------------------------------------------
     // MenuItem
     //---------------------------------------------------------------
     public final static int EDIT_ITEM_NAME = 1;
     public final static int EDIT_ITEM_PRICE = 2;
     public final static int EDIT_ITEM_TYPE = 3;
     
     public void editMenuItemData(int id, String newName, double newPrice, byte menuType) throws DatabaseException
     {
         MenuItem rMenuItem = findMenuItemByID(id);
         rMenuItem.setName(newName);
         rMenuItem.setPrice(newPrice);
         rMenuItem.setType(menuType);
         
     }
     
     public void editMenuItemData(MenuItem rMenuItem, int which, String newData) throws DatabaseException
     {
         try
         {
             switch(which)
             {
                 case EDIT_ITEM_NAME:
                    rMenuItem.setName(newData);
                    break;
                 case EDIT_ITEM_PRICE:
                    double newPrice = Double.parseDouble(newData);
                    if(newPrice < 0)
                        throw new DatabaseException("Price must be positive number");
                    else
                        rMenuItem.setPrice(newPrice);
                    break;
                 case EDIT_ITEM_TYPE:
                    byte newType = Byte.parseByte(newData);
                    if(newType < MenuItem.MAIN || MenuItem.DESSERT < newType)
                        throw new DatabaseException("Type must be between " + MenuItem.MAIN
                                            + " and " + MenuItem.DESSERT + ")");
                    else
                        rMenuItem.setType(Byte.parseByte(newData));
                    break;
                 default:
                    break;
             }
        }
        catch(DatabaseException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new DatabaseException(e.getMessage());
        }
     }
     
     public void setMenuItemAsPromotionItem(MenuItem rMenuItem, double price)
     {
         rMenuItem.setState(MenuItem.PROMOTION_ITEM, price);
     }
     
     public void resetMenuState(MenuItem rMenuItem)
     {
         rMenuItem.resetState();
     }
     
     public void deleteMenuItem(MenuItem rMenuItem) throws DatabaseException
     {
         menuList.remove(rMenuItem);

     }
     
     public void addMenuItem(int newID, String newName, double newPrice, byte newType) throws DatabaseException
     {
         MenuItem newMenuItem = new MenuItem(newID, newName,newPrice, newType);
         menuList.add(newMenuItem);
         Collections.sort(menuList, new MenuItemComparator());
         
     }
     //---------------------------------------------------------------
     // Bestellung
     //---------------------------------------------------------------
     public int addOrder()
     {
         int newOrderID = 0;
         Order newOrder = new Order();
         newOrder.setOrderID( newOrderID);
         orderList.add(newOrder);
         return newOrderID;
     }
     
     public void addOrderItem(int orderID, MenuItem rItem, byte quantity)
     {
         Order rOrder = findOrderByID(orderID);
         rOrder.addItem(rItem, quantity);
     }
     
     public boolean deleteOrderItem(int orderID, int index)
     {
          Order rOrder = findOrderByID(orderID);
          if(rOrder == null)
            return false;
          return rOrder.deleteItem(index);
     }
     
     
     public boolean cancelOrder(int orderID)
     {
         Order rOrder = findOrderByID(orderID);
        if(rOrder == null)
            return false;
         rOrder.setState(Order.ORDER_CANCELED);
         return true;
     }

     public boolean deleteOrder(int orderID)
     {
         Order rOrder = findOrderByID(orderID);
        if(rOrder == null)
            return false;
         orderList.remove(rOrder);
         todaysOrderCounts--;
         return true;
     }
     
     public boolean closeOrder(int orderID)
     {
         Order rOrder = findOrderByID(orderID);
        if(rOrder == null)
            return false;
         rOrder.setState(Order.ORDER_CLOSED);
         return true;
     }
     
     public void closeAllOrder()
     {
        Iterator<Order> it = orderList.iterator();
        Order           re = null;
        
        while (it.hasNext()) {
            re = it.next();  
            if( re.getState() == 0)//neither closed and canceled
            {
                re.setState(Order.ORDER_CLOSED);
            }
        }
     }
     
     public int getOrderState(int orderID)
     {
         Order  re = findOrderByID(orderID);
         if(re == null)
             return -1;
         return re.getState();
     }
     
     
    /****************************************************************************
    * Daten hochladen
    ***************************************************************************/
    public void loadFiles() throws DatabaseException
    {
      
        loadMenuFile();
        loadWageInfoFile();
    }
    
    private void loadWageInfoFile() {
    }
    
	private void loadMenuFile() throws DatabaseException
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(MENU_FILE));
            String line = reader.readLine();

            while (line != null) {
                String[] record = line.split(",");

                String id = record[0].trim();
                String name = record[1].trim();
                String price = record[2].trim();
                String type = record[3].trim();

                MenuItem rMenuItem = new MenuItem(Integer.parseInt(id), name, Double.parseDouble(price), Byte.parseByte(type));
                menuList.add(rMenuItem);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException ioe) {
            String message = ioe.getMessage() + ioe.getStackTrace();
            throw new DatabaseException(message);
        }
    }
    
   
    /****************************************************************************
    * File Edit
    ***************************************************************************/
    
    public void updateMenuFile() throws DatabaseException
    {
        Writer          writer;
        String          tempFileName = "dataFiles/temp.txt";
        
        File tempFile = new File(tempFileName);
     
        try{
            writer = new BufferedWriter(new FileWriter(tempFile));
            Iterator<MenuItem> it = menuList.iterator();
        
            while (it.hasNext())
            {
                MenuItem re = (MenuItem)it.next();

                writer.write(re.getID() + "," + re.getName() + "," + re.getPrice() + "," + re.getType()+ "\r\n");
            }
            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            String message = e.getMessage() + e.getStackTrace();
            throw new DatabaseException(message);
        }
        
        File deleteFile = new File(MENU_FILE);
        deleteFile.delete();
        
        File newFile = new File(MENU_FILE);  
        tempFile.renameTo(newFile);
    }
	public boolean checkIfAllOrderClosed() {
		
		return false;
	}
	public ArrayList<Order> getKundenList() {
		return null;
	}
    
}
