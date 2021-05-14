/**
 File: Final_Project/VE_SB_FinalProject.java
 This program a rough version of a GrubHub App, similar to the ones that Smithies 
 have used this semester. A user can view a menu of all food options from a menu file. 
 They can then choose a meal from the item, and receive a receipt containing all users’ 
 inputs. A user also has the option to cancel an order. The receipt file can be 
 reorganized and filtered. 
 There are two classes of objects (besides the main class): the Person, who orders a meal 
 at a certain time, and the Meal, which contains all the food attributes, like drinks, 
 desserts, side dishes, and main meal.
 All of these operations are sorted in a series of menus, using objects, arrays, and lists. 

 Authors: Emma Vejcik and Sethatevy Bong
 
 Files:
 Receipts.txt
 Menu.txt

 Sample File (Menu.txt):
 House Options: Lamont, Tyler, King, Cutter/Ziskind
 Main Meals: Bowl, Salad, Cheeseburger, Salmon, Chicken Nuggets, Tacos, Sandwich, Quesadilla 
 Side Meals: Fries, Fruit, Chips, Salad, Beans, Hardboiled eggs
 Dessert: Cake, Oreos, Chia Pudding, Cannolis, Churros
 Drink: Milk, Pepsi, Diet Pepsi, Water
 
 Software:
 Java must be installed to run the program.
 Tested on MAC OS and Window.
 */
package Final_Project;
import java.io.*;
import java.util.*;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;

public class VE_SB_FinalProject{
	static Scanner in = new Scanner(System.in);
    public static void main(String [] args){
		Scanner fin = null;
		try { fin = new Scanner(new File("Menu.txt"));}//Read the offers from Menu.txt
		catch ( IOException ex ) { System.out.print(ex);}
		while( fin.hasNext() ){ //check end of file
			String line = fin.nextLine();
			System.out.println(line);
		}

		fin.close();
		System.out.println();
        PrintWriter fout = null;
        try{
            File ff = new File("Receipts.txt");
            if(ff.createNewFile()){
                System.out.println("new: " + ff.getName());
            }else{
                System.out.println("File already exits: Receipts.txt");
            }
            fout = new PrintWriter(ff);
        }
        catch(IOException e){
            System.out.println(e);
        }
		System.out.println();

        String name; 
        List <Meal> meal = new ArrayList<>();
		List <PersonThis> people = new ArrayList<>();
		// Create an order with 5 selections for a main meal, side meal, drink, dessert, and house
		String [] order = {"0","0","0","0","0"};
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        
		int option;
		Meal newMeal;
		Date date;
		PersonThis newPerson;
        do{
            System.out.println("Please enter your name.");
			name = in.nextLine();
			System.out.println("Ready to order? Choose a house to order from.");
        	System.out.println("1. Lamont \n2. Cutter/Ziskind \n3. Tyler \n4. King \n0. Cancel");
			option = in.nextInt();
			//Food Menu
            switch(option){
                case 1: order = lamont(meal, order); break;
                case 2: order = cutterZ(meal, order); break;
                case 3: order = tyler(meal, order); break;
                case 4: order = king(meal, order); break;
                case 0: System.out.println("Bye!"); break;
                default: System.out.println("Bad option. Pick again.");
            }

            newMeal = new Meal (order[0], order[1], order[2], order[3], order[4]);
			date = new Date(System.currentTimeMillis());
			newPerson = new PersonThis(name, newMeal, date);
			fout.write(newPerson + "\n");

			System.out.println("Order completed. Press a number to order again. Else, press 0 to exit.");
			option = in.nextInt();
			in.nextLine();
        }while(option!= 0);

        if(order[0].equals("cancelled")){
            System.out.println("Order cancelled. Bye!");
        }
        fout.close();
        //add processing bar for console
		ProgressBarTraditionalThis pb2 = new ProgressBarTraditionalThis();
      	pb2.start();
		pause(2000);
		pb2.showProgress = false;
		System.out.println("Done! Receipt completed. Thank you for ordering.");
		//write the receipt
		Scanner receiptScan = null;
        try{
            receiptScan = new Scanner(new File("Receipts.txt"));
            receiptScan.useDelimiter(" ");
        }
        catch(IOException ex){
            System.out.println(ex);
        }
        
		List <ReceiptLine> rr = new ArrayList<>();
		//add contents to receipt
		while(receiptScan.hasNext()){
            String r = receiptScan.nextLine();
            String receiptSplit[] = r.split(" ");
            String nameReceipt = receiptSplit[0];
			String houseReceipt = receiptSplit[2];
			//System.out.println(Arrays.toString(receiptSplit));
			//System.out.println(receiptSplit[receiptSplit.length - 3]);
			String timeReceipt = receiptSplit[receiptSplit.length - 3];
            ReceiptLine newReceipt = new ReceiptLine(nameReceipt, houseReceipt, timeReceipt);
            rr.add(newReceipt);
        }
        receiptScan.close();
        
        //allows us to perform functions on receipt without affecting the original list
		List <ReceiptLine> rr2 = new ArrayList<>(rr);
		List <ReceiptLine> rr3 = new ArrayList<>(rr);
		List <ReceiptLine> rr4 = new ArrayList<>(rr);
		List <ReceiptLine> rr5 = new ArrayList<>(rr);

		System.out.println("Do you want to view the reciept? Press 1 to view.");
		int receiptView = in.nextInt();
		int optionReceipt;
		if(receiptView == 1){
			do{
			System.out.println("Pick an option from the menu. \n1. Display \n2. Search for name \n3. Search for house. \n4. Sort by name \n0. Exit");
			optionReceipt = in.nextInt();
			switch(optionReceipt){
				case 1: receipt1(rr); break;
				case 2: receipt2(rr2); break;
				case 3: receipt3(rr3); break;
				case 4: receipt4(rr4); break;
				case 0: System.out.println("Thank you for using our app. Good bye!"); break;
				default: System.out.println("Bad option. Try again.");
			}
			}while(optionReceipt != 0);
		}
		receiptScan.close();
    }//main
    
    // Display receipt
	public static void receipt1(List <ReceiptLine> rr){
		for(int i = 0; i < rr.size(); i++){
			System.out.println(rr.get(i));
		}
		//System.out.println(rr);
        int count = 0;
        for(int j = 0; j < rr.size(); j++){
            count++;
        }
        System.out.println("There are " + count + " orders.");
	}
    
    //Search for name
	public static void receipt2(List <ReceiptLine> rr){
		Scanner newIn = new Scanner(System.in);
		System.out.println("Enter a name for an order.");
		String name = newIn.nextLine();
		nameSearch(rr, name);
	}

    //Search for house
	public static void receipt3(List <ReceiptLine> rr){
		Scanner newIn2 = new Scanner(System.in);
		System.out.println("Enter a house for an order. (Chapin, Cutter/Ziskind, Tyler, Lamont)");
		String house = newIn2.nextLine();
		houseSearch(rr, house);
	}

    //Sort by name
	public static void receipt4(List <ReceiptLine> rr){
        Collections.sort(rr,new Comparator <ReceiptLine> (){
			public int compare(ReceiptLine r1, ReceiptLine r2){
				return r1.getName().compareTo(r2.getName());
		}});
		System.out.println("Sorted by Name: " );
		receipt1(rr);
    }

	/*public static void sortTime(List <ReceiptLine> rr){
		//System.out.print(arr + " ");
        Collections.sort(rr,new Comparator <ReceiptLine> (){
			public int compare(ReceiptLine r1, ReceiptLine r2){
				return r1.getTime().compareTo(r2.getTime());
		}});
		System.out.println("Sorted by Time: " );
		receipt1(rr);
    }*/

    //receipt2's helper function: allow user to search for orders from a specific name
	public static void nameSearch (List <ReceiptLine> rr, String name){
		boolean check = false;
        for(int i = 0; i < rr.size(); i++){
            if (containsName(rr.get(i), name)){
                System.out.println(name + " ordered from " + rr.get(i).getHouse() + " at " + rr.get(i).getTime());
				check = true;
            }
        }
		if(check == false){
			System.out.print("No orders.");
		}
    }
	//receipt3's helper function: allow user to search for orders from a specific house
	public static void houseSearch (List <ReceiptLine> rr, String house){
		boolean check = false;
        for(int i = 0; i < rr.size(); i++){
            if (containsHouse(rr.get(i), house)){
                System.out.println(rr.get(i).getName() + " ordered at " + rr.get(i).getTime() + " from " + house);
				check = true;
            }
        }
		if(check == false){
			System.out.print("No orders.");
		}
    }
	//function used in nameSearch
	public static boolean containsName(ReceiptLine rec, String word){
        word.replace(" ", "");
        rec.getName().replace(" ", "");
        if(rec.getName().equals(word)){
            return true;
        }
        else return false;
    }
	//function used in houseSearch
	public static boolean containsHouse(ReceiptLine rec, String word){
        word.replace(" ", "");
        rec.getHouse().replace(" ", "");
        if(rec.getHouse().equals(word)){
            return true;
        }
        else return false;
    }

	public static void pause(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			System.err.format("IOException: %s%n", e);
		}
	}
//////////////////////////////////////////Food Section/////////////////////////////////////////////
	
	public static String [] lamont (List <Meal> meal, String [] order){
	    System.out.println("Lamont House: \n1. Salad \n2. Bowl");
	    order[0] = ("Lamont");
	    
	    int option;
	    boolean ordered = true;
	    	
	        do{
                option = in.nextInt();
	            switch(option){
	                case 1: order[1] = "Salad"; System.out.println("You've ordered " + order[1] + "."); salad(meal, order,1); ordered = false; break;
	                case 2: order[1] = "Bowl"; System.out.println("You've ordered " + order[1] + ".");
	                bowl(meal, order); ordered = false; break;
	                case 0: System.out.println("Order cancelled."); order[0] = "cancelled"; ordered = false; break;
	                default: System.out.println("Bad option. Pick again.");
	            }
	        }while(option!= 0 && ordered == true);
	
		if(!order[0].equals("cancelled")){
	        order = side_lamont(meal, order);
	    }
		if(!order[0].equals("cancelled")){
	        order = dessert(meal, order);
	    }
		if(!order[0].equals("cancelled")){
	        order = drink(meal, order);
	    }
	    return order;
	}// lamont
	
	public static String [] salad(List <Meal> meal, String [] order, int place){
        System.out.println("Salad Menu: \n1. Caesar \n2. Greek \n3. Potato");
		int option;
	    boolean ordered = true;
	        do{
                option = in.nextInt();
	            switch(option){
	                case 1: order[place] = "Caesar " + order[place]; ordered = false; break;
	                case 2: order[place] = "Greek " + order[place]; ordered = false; break;
                    case 3: order[place] = "Potato " + order[place]; ordered = false; break;
	                case 0: System.out.println("Order cancelled."); order[0] = "cancelled"; ordered = false; break;
	                default: System.out.println("Bad option. Pick again.");
	            }
	        }while(option!= 0 && ordered == true);
	    return order;
	}// salad
	
	public static String [] bowl(List <Meal> meal, String [] order){
        System.out.println("Bowl Menu: \n1. Chana Masala \n2. French Lentil \n3. Down South \n4. Umami");
		int option;
	    boolean ordered = true;
	        do{
                option = in.nextInt();
	            switch(option){
	                case 1: order[1] = "Chana Masala" + order[1]; ordered = false; break;
	                case 2: order[1] = "French Lentil";  ordered = false; break;
	                case 3: order[1] = "Down South" + order[1];  ordered = false; break;
	                case 4: order[1] = "Umami" + order[1];  ordered = false; break;
	                case 0: System.out.println("Order cancelled."); order[0] = "cancelled"; ordered = false; break;
	                default: System.out.println("Bad option. Pick again.");
	            }
	        }while(option!= 0 && ordered == true);
	    return order;
	}// bowl 
	
	public static String [] side_lamont(List <Meal> meal, String [] order){
        System.out.println("Side Menu: \n1. Fruit \n2. Hard boiled eggs \n3. Fries");
		int option;
	    boolean ordered = true;
	        do{
                option = in.nextInt();
	            switch(option){
	                case 1: order[2] = "Fruit"; ordered = false; break;
	                case 2: order[2] = "Hardboiled eggs";  ordered = false; break;
                    case 3: order[2] = "Fries"; ordered = false; break;
	                case 0: System.out.println("Order cancelled."); order[0] = "cancelled"; ordered = false; break;
	                default: System.out.println("Bad option. Pick again.");
	            }
	        }while(option!= 0 && ordered == true);
	    return order;
	}// side_lamont
	
	
	public static String [] cutterZ (List <Meal> meal, String [] order){
	    System.out.println("Cutter/Ziskind House: \n1. Chicken Nuggers \n2. Salmon \n3. Cheeseburger");
	    order[0] = ("Cutter/Ziskind");
	    int option;
	    boolean ordered = true;
	        do{
                option = in.nextInt();
	            switch(option){
	                case 1: order[1] = "Chicken Nuggets"; ordered = false; break;
	                case 2: order[1] = "Salmon"; ordered = false; break;
	                case 3: order[1] = "Cheeseburger"; ordered = false; break;
	                case 0: System.out.println("Order cancelled."); order[0] = "cancelled"; break;
	                default: System.out.println("Bad option. Pick again.");
	            }
	        }while(option!= 0 && ordered == true);
			if(!order[0].equals("cancelled")){
				order = side_CZ(meal, order);
			}
			if(!order[0].equals("cancelled")){
				order = dessert(meal, order);
			}
			if(!order[0].equals("cancelled")){
				order = drink(meal, order);
			}
	    return order;
	}// cutterZ
	
	public static String [] side_CZ(List <Meal> meal, String [] order){
        System.out.println("Side Menu: \n1. Fries \n2. Rice \n3. Salad");
		int option;
	    boolean ordered = true;
	        do{
                option = in.nextInt();
	            switch(option){
	                case 1: order[2] = "Fries"; ordered = false; break;
	                case 2: order[2] = "Rice"; ordered = false; break;
	                case 3: order[2] = "Salad"; order = salad(meal, order, 3); ordered = false; break;
	                case 0: System.out.println("Order cancelled."); order[0] = "cancelled"; break;
	                default: System.out.println("Bad option. Pick again.");
	            }
	        }while(option!= 0 && ordered == true);
	    return order;
	}// side_CZ
	
	public static String [] tyler (List <Meal> meal, String [] order){
	    System.out.println("Tyler House: \n1. Turkey Sandwich \n2. BLT \n3. Tofu Banh Mi \n4. Pastrami Reuben on Rye \n5. Vegan Mushroom Reuben");  
	    order[0] = ("Tyler");
	
	    int option;
	    boolean ordered = true;
	        do{
                option = in.nextInt();
	            switch(option){
	                case 1: order[1] = "Turkey Sandwich"; ordered = false; break;
	                case 2: order[1] = "BLT"; ordered = false; break;
	                case 3: order[1] = "Tofu Banh Mi"; ordered = false; break;
	                case 4: order[1] = "Pastrami Reuben on Rye"; ordered = false; break;
	                case 5: order[1] = "Vegan Mushroom Reuben"; ordered = false; break;
	                case 0: System.out.println("Order cancelled."); order[0] = "cancelled"; break;
	                default: System.out.println("Bad option. Pick again.");
	            }
	        }while(option!= 0 && ordered == true);
	
			if(!order[0].equals("cancelled")){
				order = side_tyler(meal, order);
			}
			if(!order[0].equals("cancelled")){
				order = dessert(meal, order);
			}
			if(!order[0].equals("cancelled")){
				order = drink(meal, order);
			}
	    return order;
	}// tyler
	
	public static String [] side_tyler(List <Meal> meal, String [] order){
        System.out.println("Side Menu: \n1. Chips \n2. Fruit \n3. Salad");
		int option;
	    boolean ordered = true;
	        do{
                option = in.nextInt();
	            switch(option){
	                case 1: order[2] = "Chips"; ordered = false; break;
	                case 2: order[2] = "Fruit"; ordered = false; break;
	                case 3: order[2] = "Salad"; order = salad(meal, order, 3); ordered = false; break;
	                case 0: System.out.println("Order cancelled."); order[0] = "cancelled"; break;
	                default: System.out.println("Bad option. Pick again.");
	            }
	        }while(option!= 0 && ordered == true);
	    return order;
	}// side_tyler
	
	
	public static String [] king(List <Meal> meal, String [] order){
	    System.out.println("King House: \n1. Cheese Quesadillas \n2. Beef Barbacoa Tacos \n3. Chicken Tacos al Pastor \n4. Pork Carnitas Tacos");
	    order[0] = ("King");
	
	    int option;
	    boolean ordered = true;
	        do{
                option = in.nextInt();
	            switch(option){
	                case 1: order[1] = "Cheese Quesadillas"; ordered = false; break;
	                case 2: order[1] = "Beef Barbacoa Tacos"; ordered = false; break;
	                case 3: order[1] = "Chicken Tacos al Pastor"; ordered = false; break;
	                case 4: order[1] = "Pork Carnitas Tacos"; ordered = false; break;
	                case 0: System.out.println("Order cancelled."); order[0] = "cancelled"; break;
	                default: System.out.println("Bad option. Pick again.");
	            }
	        }while(option!= 0 && ordered == true);
	        
			if(!order[0].equals("cancelled")){
				order = side_king(meal, order);
			}
			if(!order[0].equals("cancelled")){
				order = dessert(meal, order);
			}
			if(!order[0].equals("cancelled")){
				order = drink(meal, order);
			}
	    return order;
	}//king
	
	public static String [] side_king(List <Meal> meal, String [] order){
        System.out.println("Side Menu: \n1. Beans \n2. Rice \n3. Salad");
		int option;
		boolean ordered = true;
	        do{
                option = in.nextInt();
	            switch(option){
	                case 1: order[2] = "Beans"; ordered = false; break;
	                case 2: order[2] = "Rice"; ordered = false; break;
	                case 3: order[2] = "Salad"; order = salad(meal, order,3); ordered = false; break;
	                case 0: System.out.println("Order cancelled."); order[0] = "cancelled"; break;
	                default: System.out.println("Bad option. Pick again.");
	            }
	        }while(option!= 0 && ordered == true);
	    return order;
	}//side_king
		
	public static String [] dessert(List <Meal> meal, String [] order){
        System.out.println("Dessert Menu: \n1. Chocolate Cake \n2. Oreos \n3. Very Berry Chia Pudding \n4. Vegan Banana Bread \n5. Mini Cannolis \n6. Churros");
		int option;
	    boolean ordered = true;
	        do{
                option = in.nextInt();
	            switch(option){
	                case 1: order[3] = "Chocolate Cake"; ordered = false; break;
	                case 2: order[3] = "Oreos";  ordered = false; break;
	                case 3: order[3] = "Very Berry Chia Pudding";  ordered = false; break;
	                case 4: order[3] = "Vegan Banana Bread";  ordered = false; break;
	                case 5: order[3] = "Mini Cannolis";  ordered = false; break;
	                case 6: order[3] = "Churros"; ordered = false; break;
	                case 0: System.out.println("Order cancelled."); order[0] = "cancelled"; ordered = false; break;
	                default: System.out.println("Bad option. Pick again.");
	            }
	        }while(option!= 0 && ordered == true);
	    return order;
	}//dessert
	
	public static String [] drink(List <Meal> meal, String [] order){
        System.out.println("Drink Menu: \n1. Milk \n2. Pepsi \n3. Diet Pepsi \n4. Water");
		int option;
	    boolean ordered = true;
	        do{
                option = in.nextInt();
	            switch(option){
	                case 1: order[4] = "Milk"; ordered = false; break;
	                case 2: order[4] = "Pepsi";  ordered = false; break;
	                case 3: order[4] = "Diet Pepsi";  ordered = false; break;
	                case 4: order[4] = "Water";  ordered = false; break;
	                case 0: System.out.println("Order cancelled."); order[0] = "cancelled"; ordered = false; break;
	                default: System.out.println("Bad option. Pick again.");
	            }
	        }while(option!= 0 && ordered == true);
	    return order;
	}//drink
}
///////////////////////////////////////////Person This//////////////////////////////////////////////////////////////////
class PersonThis{
	//This class is used for each order
	String name;
	Meal meal;
	Date orderDate; 

	PersonThis(String name, Meal meal, Date orderDate){
	    this.name = name;
	    this.meal = meal;
	    this.orderDate = orderDate;
	}//constructor
	
	public String toString(){
		return name + " ordered " + meal + " at " + orderDate;
	}//toString
	public String getName(){return name;}
	public Meal getMeal(){return meal;}
	public Date orderTime(){return orderDate;}
}//PersonThis

/////////////////////////////////////////////////Meal////////////////////////////////////////////////////////////
class Meal{
    String house;
    String mainDish;
    String side;
    String dessert;
    String drink;

    Meal(String house, String mainDish, String side, String dessert, String drink){
        this.house = house;
        this.mainDish = mainDish;
        this.side = side;
        this.dessert = dessert;
        this.drink = drink;
    }//constructor

    public String getHouse() {return house;}
    public String getMain(){return mainDish;}
    public String getSide(){return side;}
    public String getDessert(){return dessert;}
    public String getDrink(){return drink;}
    public String toString(){return house + " Order: " + mainDish + ", " + side + ", " + dessert + ", " + drink;}
    
}// Meal

////////////////////////////////////////////////ProgressBar//////////////////////////////////////////////////////////////////////////
class ProgressBarTraditionalThis extends Thread {
	boolean showProgress = true;
	public void run() {
	  String anim  = "=====================";
	  int x = 0;
	  while (showProgress) {
		System.out.print("\r Processing... " 
			 + anim.substring(0, x++ % anim.length())
			 + " "); 
		try { Thread.sleep(100); }
		catch (Exception e) {};
	  }//while
	}//run
}//ProgressBarTraditionalThis

///////////////////////////////////////////////////Receipt Line/////////////////////////////////////////////////////////////////
class ReceiptLine{
	String name;
	String house;
	String time;
	ReceiptLine(String name, String house, String time){
		this.name = name;
		this.house = house;
		this.time = time;
	}
	public String toString(){
		return name + " ordered from " + house + " at " + time;
	}
	public String getName(){
		return name;
	}
	public String getHouse(){
		return house;
	}
	public String getTime(){
		return time;
	}
}//ReceiptLine

