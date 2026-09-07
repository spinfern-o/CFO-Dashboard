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
        return ((revenue - expenses.getDirectExpenses() - expenses.getOperatingExpenses())/revenue) * 100;
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
        return (expenses.getTotalExpenses()/budget) * 100;
    }

    public void onBudget(){
        if (budget - expenses.getTotalExpenses() > 0){
            System.out.printf("You used %.2f%% of your budget%n", ((int)(budgetPercent() * 100)/100.0)); //trucated to 2 decimal numbers w/printf
        } else if (budget - expenses.getTotalExpenses() < 0){
            System.out.printf("You used %.2f%% of your budget%n", ((int)(budgetPercent() * 100)/100.0));
        } else {
            System.out.println("You're exactly on budget");
        }
    }

    public static void compareMonths(CFODashboard month1, CFODashboard month2) {
        System.out.println();
        System.out.println(month1.month + " vs " + month2.month);

        System.out.printf(
            "%-22s %12s %12s %14s%n",
            "Metrics",
            month1.month,
            month2.month,
            "Difference"
        );

        System.out.printf(
            "%-22s $%,11.2f $%,11.2f $%+,12.2f%n",
            "Revenue",
            month1.revenue,
            month2.revenue,
            month1.revenue - month2.revenue
        );

        // Profit margins
        System.out.printf(
            "%-22s %11.1f%% %11.1f%% %+11.1f pts%n",
            "Gross Profit",
            month1.grossProfit(),
            month2.grossProfit(),
            month1.grossProfit() - month2.grossProfit()
        );

        System.out.printf(
            "%-22s %11.1f%% %11.1f%% %+11.1f pts%n",
            "Net Profit",
            month1.netProfit(),
            month2.netProfit(),
            month1.netProfit() - month2.netProfit()
        );

        // Expense metrics as % of revenue
        System.out.printf(
            "%-22s %11.1f%% %11.1f%% %+11.1f pts%n",
            "Prime Cost",
            month1.primeCost(),
            month2.primeCost(),
            month1.primeCost() - month2.primeCost()
        );

        System.out.printf(
            "%-22s %11.1f%% %11.1f%% %+11.1f pts%n",
            "Food Cost",
            month1.foodCost(),
            month2.foodCost(),
            month1.foodCost() - month2.foodCost()
        );

        System.out.printf(
            "%-22s %11.1f%% %11.1f%% %+11.1f pts%n",
            "Labor Cost",
            month1.laborCost(),
            month2.laborCost(),
            month1.laborCost() - month2.laborCost()
        );

        System.out.printf(
            "%-22s %11.1f%% %11.1f%% %+11.1f pts%n",
            "Occupancy",
            month1.occupancy(),
            month2.occupancy(),
            month1.occupancy() - month2.occupancy()
        );

        double direct1 =
            (month1.expenses.getDirectExpenses() / month1.revenue) * 100;

        double direct2 =
            (month2.expenses.getDirectExpenses() / month2.revenue) * 100;

        System.out.printf(
            "%-22s %11.1f%% %11.1f%% %+11.1f pts%n",
            "Direct Expenses",
            direct1,
            direct2,
            direct1 - direct2
        );

        double operating1 =
            (month1.expenses.getOperatingExpenses() / month1.revenue) * 100;

        double operating2 =
            (month2.expenses.getOperatingExpenses() / month2.revenue) * 100;

        System.out.printf(
            "%-22s %11.1f%% %11.1f%% %+11.1f pts%n",
            "Operating Expenses",
            operating1,
            operating2,
            operating1 - operating2
        );

        double total1 =
            (month1.expenses.getTotalExpenses() / month1.revenue) * 100;

        double total2 =
            (month2.expenses.getTotalExpenses() / month2.revenue) * 100;

        System.out.printf(
            "%-22s %11.1f%% %11.1f%% %+11.1f pts%n",
            "Total Expenses",
            total1,
            total2,
            total1 - total2
        );

        System.out.printf("%-22s %11.1f%% %11.1f%% %+11.1f pts%n","Budget Used",month1.budgetPercent(),month2.budgetPercent(),month1.budgetPercent() - month2.budgetPercent());
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
        System.out.printf("%-22s %11.1f%%%n", "Gross Profit", grossProfit());
        System.out.printf("%-22s %11.1f%%%n", "Net Profit",   netProfit());
        System.out.println();
        System.out.printf("%-22s %11.1f%%%n", "Prime Cost", primeCost());
        System.out.printf("%-22s %11.1f%%%n", "Food Cost",  foodCost());
        System.out.printf("%-22s %11.1f%%%n", "Labor Cost", laborCost());
        System.out.printf("%-22s %11.1f%%%n", "Occupancy",  occupancy());
        onBudget();
    }

    public static double parseAmount(String cell) {
        if (cell == null || cell.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(
                cell.trim().replace(",", "") //in case for inputs w/comma (e.g. 1,800)
            );
        } catch (NumberFormatException e) { 
            return 0.0;
        }
    }

    public static String[] parseCsvLine(String line) {
        String[] fields = line.split(",", -1); //counts for empty fields
        for (int i = 0; i < fields.length; i++) {
            fields[i] = fields[i].trim();
        }
        return fields;
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
                String[] eData = parseCsvLine(line);

                int monthNum = (int) parseAmount(eData[0]);
                double COGS = parseAmount(eData[1]);
                double rent = parseAmount(eData[2]);
                double labor = parseAmount(eData[3]);
                double directExpenses = parseAmount(eData[4]);
                double operatingExpenses = parseAmount(eData[5]);

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

                String[] data = parseCsvLine(line);
                String month = data[0];
                int monthNum = (int) parseAmount(data[1]);
                double revenue = parseAmount(data[2]);
                double budget = parseAmount(data[3]);
                Expenses expenses = expensesByMonth.get(monthNum); //retrieves the monthNum's expenses object

                if (expenses == null){
                    System.out.println("No expenses for month " + monthNum + ", skipping month");
                    continue;
                }
                
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

        while(true){
            CFODashboard selectedMonth = askForMonth(input, months, "Which month's dashboard do you wish to see? (month num, 0 to quit)");
            
            if (selectedMonth == null){
                break;
            }

            selectedMonth.displayBoard();
            
            while(true){
                CFODashboard comparativeMonth = askForMonth(input, months, "Which month's dashboard would you like to compare it to? (month num, 0 to quit)");
                
                if (comparativeMonth == null){
                    break;
                }

                compareMonths(selectedMonth, comparativeMonth);
            }
        }

        input.close();
    }
}