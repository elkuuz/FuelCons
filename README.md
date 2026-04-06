# FuelCons

## Database Setup (MariaDB)

This project connects to a local MariaDB database using:

- JDBC URL: `jdbc:mariadb://localhost:3306/fuel_calculator_localization`
- User/password from `src/main/resources/db.properties`
- SQL bootstrap script: `init.sql`

## Prerequisites

- Java 21
- Maven 3.9+
- MariaDB server running on `localhost:3306`

## 1) Install and start MariaDB (macOS)

```zsh
brew install mariadb
brew services start mariadb
```

## 2) Create schema, tables, seed data, and app user

Run the bundled SQL script as a privileged MariaDB account:

```zsh
mariadb -u root -p < init.sql
```

What this script does:

- Creates database `fuel_calculator_localization`
- Creates user `fuel_user` with password `password123`
- Grants permissions on the database
- Creates `calculation_records` and `localization_strings`
- Inserts localization text for `EN`, `FR`, `JP`, `IR`

## 3) Verify app credentials

`src/main/resources/db.properties` should match the user created in `init.sql`:

```properties
db.user=fuel_user
db.password=password123
```

If you change one, update the other.

## 4) Quick verification

Open MariaDB and confirm seeded rows exist:

```zsh
mariadb -u fuel_user -p
```

```sql
USE fuel_calculator_localization;
SELECT COUNT(*) AS localization_rows FROM localization_strings;
SELECT COUNT(*) AS calculations FROM calculation_records;
```

You should see localization rows greater than 0.

## 5) Run the app

```zsh
mvn clean javafx:run
```

## Troubleshooting

- `Access denied for user ...`: re-run `init.sql` or fix credentials in `db.properties`.
- `Unknown database 'fuel_calculator_localization'`: run `mariadb -u root -p < init.sql`.
- If `CREATE USER 'fuel_user'@'localhost'` fails because the user already exists, run:

```sql
DROP USER IF EXISTS 'fuel_user'@'localhost';
```

Then execute `init.sql` again.

## Notes

- The app currently uses a fixed DB URL in `src/main/java/org/example/db/DatabaseConnection.java`.
- `Dockerfile` and `deployment.yaml` package/run the app but do not provision a MariaDB instance.

