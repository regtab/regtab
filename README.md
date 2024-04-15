# Regtab
Regtab is a Java library that provides _Regular Table Language_ (RTL)
intended to simplify data extraction from arbitrary tables represented
in machine-readable sources such as spreadsheets, CSV or HTML. 

RTL allows expressing patterns
that describe a class of tables. Such patterns enable
extracting recordsets from arbitrary tables.

## Dependency

```xml
<dependency>
    <groupId>com.regtab.core</groupId>
    <artifactId>regtab-core</artifactId>
    <version>0.1.0</version>
</dependency>
```
## Usage

For example, there is a CSV file providing cross-tabulation 
on population by country and year as follows:
```
,2000,2005,2010
Australia,19028802,20176844,22031750
Austria,8002186,8201359,8351643
Belgium,10239085,10445852,10839905
```

Need to extract a recordset as follows:
```
DATA,COUNTRY,YEAR
19028802,Australia,2000
20176844,Australia,2005
22031750,Australia,2010
8002186,Austria,2000
8201359,Austria,2005
8351643,Austria,2010
10239085,Belgium,2000
10445852,Belgium,2005
10839905,Belgium,2010
```

The solution can be implemented as RTL-pattern as follows:
```
[[SKIP][VAL: SCHEMA='YEAR']+]
[[VAL: SCHEMA='CONTRY'][VAL: RECORD=(ROW;COL); SCHEMA='DATA']+]+
```

The pattern can be applied as follows:
```java
import com.regtab.core.model.ITable;
import com.regtab.core.model.Recordset;
import com.regtab.core.readers.CSVReader;
import com.regtab.core.rtl.interpreter.RTLPattern;
import com.regtab.core.writers.CSVWriter;
import java.io.IOException;

public class Demo {
    public static void main(String[] args) throws IOException {
        // Read source data from CSV and create an instance of interpretable table
        CSVReader csvReader = new CSVReader("population_by_country_and_year.csv");
        ITable table = csvReader.read();

        // Write the appropriate RTL-pattern as string
        String rtl = "[[SKIP][VAL: SCHEMA='YEAR']+]"
                   + "[[VAL: SCHEMA='COUNTRY'][VAL: RECORD=(ROW;COL); SCHEMA='DATA']+]+";

        // Apply the RTL-pattern to the interpretable table
        RTLPattern.apply(rtl, table);

        // Extract the recordset
        Recordset recordset = table.extract();

        // Extract the recordset to CSV
        CSVWriter csvWriter = new CSVWriter("recordset.csv");
        csvWriter.write(recordset);
    }
}
```

## Author

* Alexey O. Shigarov

## License
Apache License, Version 2.0
