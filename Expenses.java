public class Expenses {

    int eMonthNum;
    double COGS;
    double rent;
    double labor;
    double directExpenses;
    double operatingExpenses;

    public Expenses(
        int eMonthNum,
        double COGS,
        double rent,
        double labor,
        double directExpenses,
        double operatingExpenses
    ) {
        this.eMonthNum = eMonthNum;
        this.COGS = COGS;
        this.rent = rent;
        this.labor = labor;
        this.directExpenses = directExpenses;
        this.operatingExpenses = operatingExpenses;
    }

    public int getExpensesMonthNum(){
        return eMonthNum;
    }
    public double getDirectExpenses(){
        return directExpenses;
    }

    public double getOperatingExpenses(){
        return operatingExpenses;
    }

    public double getTotalExpenses(){
        return directExpenses + operatingExpenses;
    }

    public double getRent(){
        return rent;
    }

    public double getCOGS(){
        return COGS;
    }

    public double getLabor(){
        return labor;
    }
}