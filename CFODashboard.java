import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

public class CFODashboard {

    static Scanner input = new Scanner(System.in);

    String month;
    int monthNum;
    double revenue;
    double budget;
    Expenses expenses;

    public CFODashboard(
        String month,
        int monthNum,
        double revenue,
        double budget,
        Expenses expenses
    ) {
        this.month = month;
        this.monthNum = monthNum;
        this.revenue = revenue;
        this.budget = budget;
        this.expenses = expenses;
    }

    public double grossProfit() {
        return ((revenue - expenses.getDirectExpenses())/revenue) * 100;
    }

    public double netProfit() {
        return ((grossProfit() - expenses.getOperatingExpenses())/revenue) * 100;
    }

    public double foodCost() {
        return (expenses.getCOGS()/revenue) * 100;
    }

    public double laborCost() {
        return (expenses.getLabor()/revenue) * 100;
    }

    public double primeCost(){
        return ((expenses.getCOGS() + expenses.getLabor())/revenue) * 100;
    }

    public double occupancy(){
        return (expenses.getRent()/revenue) * 100;
    }

    public double budgetPercent(){
        return (budget/expenses.getTotalExpenses()) * 100;
    }

    public void onBudget(){
        if (budget - expenses.getTotalExpenses() > 0){
            System.out.printf("You used %.2f%% of your budget%n", ((int)budgetPercent() * 100/100.0)); //trucated to 2 decimal numbers w/printf
        } else if (budget - expenses.getTotalExpenses() < 0){
            System.out.printf("You used %.2f%% of your budget%n", ((int)budgetPercent() * 100/100.0));
        } else {
            System.out.println("You're exactly on budget");
        }
    }

    public static void compareMonths(CFODashboard month1, CFODashboard month2) {
        double difference = month1.netProfit() - month2.netProfit();
        if (difference > 0) {
            System.out.println(month1.month + " is more profitable by $" + difference);
        } else if (difference < 0) {
            System.out.println(month2.month + " is more profitable by $" + Math.abs(difference));
        } else {
            System.out.println("They are equally profitable making a net profit of " + month1.netProfit());
        }
    }

    public static CFODashboard askForMonth(Scanner input, ArrayList<CFODashboard> months, String prompt){
        while (true){
            System.out.println(prompt);
            String answer = input.nextLine().trim();
            int monthNum;

            try {
                monthNum = Integer.parseInt(answer);
            }catch (NumberFormatException e){
                System.out.println("Please enter an integer");
                continue;
            }

            if (monthNum == 0){
                return null;
            }

            CFODashboard selected = findMonth(months, monthNum);

            if (selected == null){
                System.out.println("No data for month " + monthNum + ".");
                continue;
            }

            return selected;
        }
    }

    public static CFODashboard findMonth(ArrayList<CFODashboard> months, int monthNum){
        for (CFODashboard m: months){
            if (m.monthNum == monthNum){
                return m;
            } 
        }
        return null;
    }

    public void displayBoard(){
        System.out.println(month + "'s financial dashboard");
        System.out.printf("%-22s %12s%n", "Revenue",      String.format("$%,.2f", revenue));
        System.out.printf("%-22s %12s%n", "Gross Profit", String.format("$%,.2f", grossProfit()));
        System.out.printf("%-22s %12s%n", "Net Profit",   String.format("$%,.2f", netProfit()));
        System.out.println();
        System.out.printf("%-22s %11.1f%%%n", "Prime Cost", primeCost());
        System.out.printf("%-22s %11.1f%%%n", "Food Cost",  foodCost());
        System.out.printf("%-22s %11.1f%%%n", "Labor Cost", laborCost());
        System.out.printf("%-22s %11.1f%%%n", "Occupancy",  occupancy());
        onBudget();
    }
    public static void main(String[] args) {
        ArrayList<CFODashboard> months = new ArrayList<>();
        HashMap<Integer, Expenses> expensesByMonth = new HashMap<>(); //creates hashmap to link all the .javas via monthNum

        try{ //expenses .csv first; double check on the order of the csvs3
            File file = new File("data/expenses.csv");
            Scanner expenseReader = new Scanner(file);
            expenseReader.nextLine();

            while(expenseReader.hasNextLine()){
                String line = expenseReader.nextLine();
                String[] eData = line.split(",");

                int monthNum = Integer.parseInt(eData[0]);
                double COGS = Double.parseDouble(eData[1]);
                double rent = Double.parseDouble(eData[2]);
                double labor = Double.parseDouble(eData[3]);
                double directExpenses = Double.parseDouble(eData[4]);
                double operatingExpenses = Double.parseDouble(eData[5]);

                Expenses expenses = new Expenses(monthNum, COGS, rent, labor, directExpenses, operatingExpenses);

                expensesByMonth.put(monthNum, expenses); //into hashmap
            }
            expenseReader.close();
        } catch (FileNotFoundException e){
            System.out.println("Expenses file not found"); 
        }

        try{
            File file = new File("data/dashboard.csv");
            Scanner fileReader = new Scanner(file);
            fileReader.nextLine(); //skips the header

            while(fileReader.hasNextLine()){
                String line = fileReader.nextLine();

                String[] data = line.split(","); //csv files export w/comma

                String month = data[0];
                int monthNum = Integer.parseInt(data[1]);
                double revenue = Double.parseDouble(data[2]);
                double budget = Double.parseDouble(data[3]);
                Expenses expenses = expensesByMonth.get(monthNum); //retrieves the monthNum's expenses object

                CFODashboard monthly = new CFODashboard(
                    month,
                    monthNum,
                    revenue,
                    budget,
                    expenses
                );

                months.add(monthly);
            }
            fileReader.close();

        } catch (FileNotFoundException e) {
                System.out.println("File not found");
        } 

        System.out.println("Months on file:");
        for (CFODashboard m: months){
            System.out.println(m.monthNum + " - " + m.month);
        }
        CFODashboard selectedMonth = askForMonth(input, months, "Which month's dashboard do you wish to see? (month num, 0 to quit)");
        
        if (selectedMonth != null){
            selectedMonth.displayBoard();
        } 

        input.close();
    }
}