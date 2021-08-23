import java.util.*;

public class Bestellung
{
    final public static int ORDER_CLOSED = 1;
    final public static int ORDER_CANCELED = 2;
    
    private int orderID;
    private String date;
    private String kundenName;
    private int state;
    private double total;
    private ArrayList<BestellungDetail> orderDetailList = new ArrayList<BestellungDetail>();

    public Bestellung()
    {
        this.orderID =-1;
        this.state = 0;
        this.total = 0;
    }

    int getBestellungID()
    {
        return this.orderID;
    }
    int getState()
    {
        return this.state;
    }
    double getTotal()
    {
        return this.total;
    }
    ArrayList<BestellungDetail> getBestellungDetail()
    {
        return this.orderDetailList;
    }
    
    public void setBestellungID(int newID)
    {
        this.orderID = newID;
    }
    
    public void setState(int state)
    {
        this.state = state;
    }
    
    public void addItem(MenuItem rNewMenuItem, byte quantity)
    {
        Iterator<BestellungDetail> it = orderDetailList.iterator();
        BestellungDetail re;
        
        boolean found = false;
        
        while( it.hasNext() && !found)
        {
            re = it.next();
            if( rNewMenuItem.getID() == re.getItemID())
            {
                found = true;
                re.addQuantity(quantity);
            }
        }
        
        if(!found)
        {
            BestellungDetail detail = new BestellungDetail(rNewMenuItem, quantity);
            orderDetailList.add(detail);
            
        }
        
        calculateTotal();
    }
    
    public boolean deleteItem(int index)
    {
        try
        {
            orderDetailList.remove(index);
            calculateTotal();
            return true;
        }
        catch(Exception e)
        {
             return false;
        }
    }
    
    public void  calculateTotal()
    {
        total = 0;
        BestellungDetail re;
         Iterator<BestellungDetail> it = orderDetailList.iterator();
         while (it.hasNext()) {
            re = it.next();
            total += re.getTotalPrice();
        }
    }
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getKundenName() {
		return kundenName;
	}
	public void setKundenName(String kundenName) {
		this.kundenName = kundenName;
	}

}
