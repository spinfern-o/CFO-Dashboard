import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CFODashboard {

    static Scanner input = new Scanner(System.in);

    String month;
    int monthNum;
    double revenue;
    double directExpenses;
    double operatingExpenses;
    double budget;

    public CFODashboard(
        String month,
        int monthNum,
        double revenue,
        double directExpenses,
        double operatingExpenses,
        double budget
    ) {
        this.month = month;
        this.monthNum = monthNum;
        this.revenue = revenue;
        this.directExpenses = directExpenses;
        this.operatingExpenses = operatingExpenses;
        this.budget = budget;
    }

    public double grossProfit() {
        return revenue - directExpenses;
    }

    public double netProfit() {
        return grossProfit() - operatingExpenses;
    }

    public double budgetPerformance() {
        return budget - (directExpenses + operatingExpenses);
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

    public static CFODashboard askForMonth(Scanner input, ArrayList<CFODashboard> months, String prompt) {
        while (true) {
            System.out.println(prompt);
            if (!input.hasNextLine()) return null;
            String answer = input.nextLine().trim(); //erase whitespace
            int monthNum;
            try {
                monthNum = Integer.parseInt(answer); //changes user's string to int
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
                continue;
            }
            if (monthNum == 0) return null;
            CFODashboard selected = findMonth(months, monthNum);
            if (selected == null) {
                System.out.println("No data for month " + monthNum + ".");
                input.close();
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

    public static void main(String[] args) {

        ArrayList<CFODashboard> months = new ArrayList<>();

        String month;
        int monthNum;
        double revenue;
        double dExpenses;
        double oExpenses;
        double budget;

        try{
            File file = new File("test.csv");
            Scanner fileReader = new Scanner(file);
            fileReader.nextLine(); //skips the header

            while(fileReader.hasNextLine()){
                String line = fileReader.nextLine();

                String[] data = line.split(","); //csv files export w/comma

                month = data[0];
                monthNum = Integer.parseInt(data[1]);
                revenue = Double.parseDouble(data[2]);
                dExpenses = Double.parseDouble(data[3]);
                oExpenses = Double.parseDouble(data[4]);
                budget = Double.parseDouble(data[5]);

                CFODashboard monthly = new CFODashboard(
                    month,
                    monthNum,
                    revenue,
                    dExpenses,
                    oExpenses,
                    budget
                );
                months.add(monthly);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } 

        months.sort(
        (month1, month2) ->
            Integer.compare(month1.monthNum, month2.monthNum)
        );

        System.out.println("Months on file:");
        for (CFODashboard m: months){
            System.out.println(m.monthNum + " - " + m.month);
        }

        CFODashboard selectedMonth = askForMonth(input, months, "Which month's dashboard do you wish to see? (month num, 0 to quit)");

        /* change the code below to show the entirety of the dashboard
            AND ask if user wants to see another month as well */
        
        // if (selectedMonth.budgetPerformance() > 0) {
        //     System.out.println(
        //         "Budget: You were under budget by $" +
        //         selectedMonth.budgetPerformance());
        // } else if (selectedMonth.budgetPerformance() < 0) {
        //     System.out.println(
        //         "Budget: You were over budget by $" +
        //         Math.abs(selectedMonth.budgetPerformance()));
        // } else {
        //     System.out.println("You are exactly on budget.");
        // }

        input.close();
    }
}