# CFO Dashboard

A Java console app that turns two CSV files of monthly business financials into a
readable dashboard of KPIs, and lets you compare any two months side by side.

Built for food-service businesses, which are managed by prime cost, food cost, labor
cost, occupancy, gross/net margin, and budget utilization.

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

## Example usage

The app lists the months it loaded, writes `output/data.json`, then loops on two prompts:

1. **Pick a month** — enter its month number to print that month's dashboard.
2. **Pick a month to compare against** — enter another month number for a side-by-side
   table. Keep entering numbers to compare against more months.

Enter `0` at the second prompt to pick a different month; `0` at the first prompt exits.

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
Gross Profit                  59.5%
Net Profit                    38.5%

Prime Cost                    38.0%
Food Cost                     26.0%
Labor Cost                    12.0%
Occupancy                     11.0%
You used 102.50% of your budget ($1,500.00 over)
Which month's dashboard would you like to compare it to? (month num, 0 to quit)
8

july vs august
Metrics                        july       august     Difference
Revenue                $ 100,000.00 $  80,000.00 $  +20,000.00

Gross Profit                  59.5%        60.0%        -0.5 pts
Net Profit                    38.5%        39.4%        -0.9 pts
Prime Cost                    38.0%        37.5%        +0.5 pts

Food Cost                     26.0%        26.3%        -0.3 pts
Labor Cost                    12.0%        11.3%        +0.8 pts
Occupancy                     11.0%        14.6%        -3.6 pts
Direct Expenses               40.5%        40.0%        +0.5 pts
Operating Expenses            21.0%        20.6%        +0.4 pts

Total Expenses                61.5%        60.6%        +0.9 pts
Budget Used                  102.5%        97.0%        +5.5 pts
```

## Data files

Both files live in `data/` and are joined on the **month num** column.

### `data/dashboard.csv`

| Column      | Meaning                                       |
| ----------- | --------------------------------------------- |
| `month`     | Display name (e.g. `july`)                    |
| `month num` | Integer key, also what you type at the prompt |
| `revenue`   | Total revenue for the month                   |
| `budget`    | Planned total expense budget for the month    |

```csv
month,month num,revenue,budget
july,7,100000,60000
august,8,80000,50000
september,9,50000,30000
```

### `data/expenses.csv`

| Column               | Meaning                                                   |
| -------------------- | --------------------------------------------------------- |
| `month num`          | Integer key matching `dashboard.csv`                      |
| `cogs`               | Cost of goods sold (food and beverage)                    |
| `rent`               | Rent, counted toward occupancy cost                       |
| `tax`                | Property tax, counted toward occupancy cost               |
| `labor`              | Total labor cost                                          |
| `direct expenses`    | All costs that scale with sales (COGS + labor + other)    |
| `operating expenses` | All overhead (rent + tax + utilities, marketing, admin)   |

```csv
month num,cogs,rent,tax,labor,direct expenses,operating expenses
7,26000,6000,5000,12000,40500,21000
8,21000,6200,5500,9000,32000,16500
9,11000,6000,5200,3500,15000,12000
```

`direct expenses` and `operating expenses` are the totals used for every calculation.
`cogs`, `rent`, `tax`, and `labor` are components reported individually — they are
subsets of those totals, not additions to them.

Parsing notes:

- The first line of each file is treated as a header and skipped.
- Amounts may contain commas (`1,800`) — they're stripped before parsing.
- Empty or unparseable cells fall back to `0.0` rather than crashing.
- Trailing empty columns are preserved, so a blank final cell reads as `0`, not an error.
- A month present in `dashboard.csv` with no matching row in `expenses.csv` is skipped
  with a message; it won't appear in the menu.

## Metrics

All ratios are expressed as a percentage of revenue, except Budget Used.

| Metric             | Formula                                                      |
| ------------------ | ------------------------------------------------------------ |
| Gross Profit       | `(revenue − direct expenses) / revenue`                      |
| Net Profit         | `(revenue − direct expenses − operating expenses) / revenue` |
| Prime Cost         | `(COGS + labor) / revenue`                                   |
| Food Cost          | `COGS / revenue`                                             |
| Labor Cost         | `labor / revenue`                                            |
| Occupancy          | `(rent + tax) / revenue`                                     |
| Total Expenses     | `(direct + operating expenses) / revenue`                    |
| Budget Used        | `(direct + operating expenses) / budget`                     |

In the comparison table, the revenue row shows a signed dollar difference and every
percentage row shows a signed change in **percentage points** (`pts`).

### Industry benchmarks

For a full-service restaurant, healthy ranges are roughly:

| Metric      | Target  |
| ----------- | ------- |
| Food cost   | 28–35%  |
| Labor cost  | 28–32%  |
| Prime cost  | 55–65%  |
| Occupancy   | 6–10%   |
| Net margin  | 3–6%    |

Prime cost is the one operators watch most closely: it is the largest share of spend
and the part management can actually change week to week.

## JSON output

Every run writes `output/data.json` — the same months and metrics in machine-readable
form, for charting or a web front end. All calculations happen in Java, so the JSON
carries finished values and consumers never recompute them.

```json
{
  "generated": "2026-09-12",
  "months": [
    {
      "month": "july",
      "monthNum": 7,
      "revenue": 100000.00,
      "primeCost": 38.00,
      "budgetPercent": 102.50
    }
  ]
}
```

## Using your own numbers

1. Replace the rows in `data/dashboard.csv` and `data/expenses.csv`, keeping the
   headers and column order intact.
2. Make sure every `month num` in `dashboard.csv` has a matching row in `expenses.csv`.
3. Recompile and run — the menu is built from whatever loaded.

## Project layout

```
CFODashboard.java   Main class: CSV loading, metrics, prompts, display, comparison
Expenses.java       Per-month expense record with getters and derived sums
data/               Input CSVs
output/data.json    Generated on every run
bin/                Compiled classes (git-ignored)
.vscode/launch.json VS Code run configuration
```
