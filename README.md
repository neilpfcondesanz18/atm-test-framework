# 🧪 ATM Test Automation Framework

A comprehensive, enterprise-grade test automation framework for the ATM & Cards Banking Platform — built for a Senior QA Automation Engineer role aligned to JPOS, SOA, and Agile/DevOps environments.

## 🛠 Tech Stack

| Layer | Tool |
|-------|------|
| Language | Java 21 |
| API Testing | RestAssured 5.x |
| UI Testing | Selenium WebDriver 4.x |
| BDD | Cucumber 7 (Gherkin) |
| Runner | TestNG 7 |
| Assertions | AssertJ + Hamcrest |
| Reporting | Allure 2.x |
| CI/CD | GitHub Actions |
| Report Hosting | GitHub Pages / AWS S3 |
| Data Generation | JavaFaker |

## 📁 Project Structure

```
atm-test-framework/
├── src/test/java/com/atm/
│   ├── api/              # API step definitions (RestAssured)
│   ├── ui/               # UI step definitions (Selenium)
│   ├── integration/      # End-to-end step definitions
│   ├── hooks/            # Cucumber Before/After hooks
│   ├── runners/          # TestNG + Cucumber runners
│   ├── config/           # FrameworkConfig singleton
│   └── utils/            # ApiClient, DriverManager, TestDataFactory
├── src/test/resources/
│   ├── features/
│   │   ├── api/          # API feature files
│   │   ├── ui/           # UI feature files
│   │   └── integration/  # E2E feature files
│   ├── config/           # Environment properties
│   ├── testng.xml        # TestNG suite config
│   └── allure.properties
└── .github/workflows/
    └── test-ci.yml       # CI pipeline + Allure publishing
```

## 🚀 Running Tests

### Prerequisites
- Java 21
- Maven 3.9+
- Chrome (for UI tests)
- Banking Platform running on `http://localhost:8080`

### Run all tests
```bash
mvn test
```

### Run by tag
```bash
mvn test -Dcucumber.filter.tags="@smoke"
mvn test -Dcucumber.filter.tags="@api"
mvn test -Dcucumber.filter.tags="@regression"
mvn test -Dcucumber.filter.tags="@cards"
```

### Run against a specific environment
```bash
mvn test -Dtest.env=staging
# or via ENV VAR
TEST_ENV=staging API_BASE_URL=https://your-staging-url.com mvn test
```

### Generate & open Allure report locally
```bash
mvn allure:serve
```

## 📊 Test Reporting

### GitHub Pages (automatic on every push to `main`)
```
https://neilpfcondesanz18.github.io/atm-test-framework/allure-report/
```
The report is published automatically by the GitHub Actions pipeline. Trend history is preserved across runs.

### AWS S3 (optional — uncomment in workflow)
1. Create an S3 bucket (e.g. `atm-test-reports`) in `ap-southeast-2`
2. Enable static website hosting on the bucket
3. Add these GitHub repository secrets:
   - `AWS_ACCESS_KEY_ID`
   - `AWS_SECRET_ACCESS_KEY`
   - `S3_BUCKET` → your bucket name
4. Uncomment the S3 section in `.github/workflows/test-ci.yml`

Report URL: `https://<bucket>.s3.ap-southeast-2.amazonaws.com/reports/latest/index.html`

## 🏷 Cucumber Tags

| Tag | Description |
|-----|-------------|
| `@smoke` | Critical path — fast, run on every PR |
| `@regression` | Full regression suite |
| `@api` | API-only tests |
| `@ui` | UI/browser tests |
| `@integration` | End-to-end flows |
| `@cards` | Card management tests |
| `@atm` | ATM transaction tests |
| `@negative` | Negative/error path tests |
| `@wip` | Work in progress — excluded from CI |

## 🔧 Configuration

All config is driven by `src/test/resources/config/`:
- `framework.properties` — base defaults
- `local.properties` — local dev overrides
- `ci.properties` — CI overrides

Environment variables override everything (ideal for CI):
```bash
API_BASE_URL=https://staging.bank.com
UI_BROWSER=chrome
UI_HEADLESS=true
```

## 🔄 CI/CD Pipeline

The GitHub Actions workflow (`.github/workflows/test-ci.yml`):
1. **Starts the SUT** (Banking Platform) locally via Maven
2. **Runs API and Integration test suites in parallel**
3. **Merges Allure results** from all parallel jobs
4. **Generates Allure report** with trend history
5. **Publishes to GitHub Pages** automatically
6. **Posts report URL** to the GitHub Actions job summary

You can also trigger manually with custom tags from the GitHub Actions UI.
# trigger ci
# trigger ci
