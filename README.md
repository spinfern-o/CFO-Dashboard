# CFO Dashboard

A Java console app that turns two CSV files of monthly business financials into a
readable dashboard of KPIs. Which also lets you compare any two months side by side.

Built for businesses that run with prime cost, food cost, labor cost, occupancy,
gross/net margin, and budget utilization.

## Requirements

- JDK 17 or newer (developed against Temurin 21)

## Quick start

Compile and run from the project root:

```bash
javac -d bin CFODashboard.java Expenses.java && java -cp bin CFODashboard
```

> **Run from the project root.** The CSV paths (`data/expenses.csv`, `data/dashboard.csv`)
> are relative to the working directory. In VS Code, the included `.vscode/launch.json`
> config named **CFODashboard** already handles this.

## Using it

The app lists the months it loaded, then loops on two prompts:

1. **Pick a month** — enter its month number to print that month's dashboard.
2. **Pick a month to compare against** — enter another month number for a side-by-side table.
   Keep entering numbers to compare against more months.

Enter `0` at either prompt to back out; `0` at the first prompt exits the program.

### Example session

```
Months on file:
7 - july
8 - august
9 - september
Which month's dashboard do you wish to see? (month num, 0 to quit)
7
july's financial dashboard
Revenue                 $100,000.00
Gross Profit                  42.0%
Net Profit                     8.0%

Prime Cost                    58.0%
Food Cost                     28.0%
Labor Cost                    30.0%
Occupancy                      8.0%
You used 92.00% of your budget ($8,000.00 under)
Which month's dashboard would you like to compare it to? (month num, 0 to quit)
8

july vs august
Metrics                        july       august     Difference
Revenue                $ 100,000.00 $  80,000.00 $  +20,000.00

Gross Profit                  42.0%        40.0%        +2.0 pts
Net Profit                     8.0%         5.0%        +3.0 pts
Prime Cost                    58.0%        60.0%        -2.0 pts

Food Cost                     28.0%        30.0%        -2.0 pts
Labor Cost                    30.0%        30.0%        +0.0 pts
Occupancy                      8.0%        10.0%        -2.0 pts
Direct Expenses               58.0%        60.0%        -2.0 pts
Operating Expenses            34.0%        35.0%        -1.0 pts

Total Expenses                92.0%        95.0%        -3.0 pts
Budget Used                   92.0%       100.0%        -8.0 pts
```

## Data files

Both files live in `data/` and are joined on the **month num** column.

### `data/dashboard.csv`

| Column      | Meaning                                        |
| ----------- | ---------------------------------------------- |
| `month`     | Display name (e.g. `july`)                     |
| `month num` | Integer key, also what you type at the prompt  |
| `revenue`   | Total revenue for the month                    |
| `budget`    | Planned total expense budget for the month     |

```csv
month,month num,revenue,budget
july,7,100000,100000
august,8,80000,76000
september,9,50000,50000
```

### `data/expenses.csv`

| Column               | Meaning                                                      |
| -------------------- | ------------------------------------------------------------ |
| `month num`          | Integer key matching `dashboard.csv`                         |
| `cogs`               | Cost of goods sold (food/beverage)                           |
| `rent`               | Rent, used for occupancy cost                                |
| `labor`              | Total labor cost                                             |
| `direct expenses`    | All direct costs (typically COGS + labor and other direct)   |
| `operating expenses` | Overhead / indirect costs                                    |

```csv
month num,cogs,rent,labor,direct expenses,operating expenses
7,28000,8000,30000,58000,34000
8,24000,8000,24000,48000,28000
9,15000,8000,15000,30000,22000
```

Parsing notes:

- The first line of each file is treated as a header and skipped.
- Amounts may contain commas (`1,800`) — they're stripped before parsing.
- Empty or unparseable cells fall back to `0.0` rather than crashing.
- A month present in `dashboard.csv` with no matching row in `expenses.csv` is skipped
  with a message; it won't appear in the menu.

To use your own numbers, replace the rows in these two files, keeping the headers and
column order intact.

## Metrics

All ratios are expressed as a percentage of revenue, except budget used.

| Metric             | Formula                                                        |
| ------------------ | -------------------------------------------------------------- |
| Gross Profit       | `(revenue − direct expenses) / revenue`                        |
| Net Profit         | `(revenue − direct expenses − operating expenses) / revenue`   |
| Prime Cost         | `(COGS + labor) / revenue`                                     |
| Food Cost          | `COGS / revenue`                                               |
| Labor Cost         | `labor / revenue`                                              |
| Occupancy          | `rent / revenue`                                               |
| Budget Used        | `(direct + operating expenses) / budget`                       |

In the comparison table, dollar rows show a signed difference and percentage rows show a
signed change in **percentage points** (`pts`).

## Project layout

```
CFODashboard.java   Main class: CSV loading, metrics, prompts, display, comparison
Expenses.java       Per-month expense record with getters and total/derived sums
data/               Input CSVs
bin/                Compiled classes (git-ignored)
.vscode/launch.json VS Code run configuration
```
