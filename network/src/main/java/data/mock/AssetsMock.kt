package data.mock

const val ASSETS_MOCK = "{\n" +
        "    \"investment\": [\n" +
        "        {\n" +
        "            \"id\": \"ACCT-INV-001\",\n" +
        "            \"userId\": \"INV-0001\",\n" +
        "            \"accountNumber\": \"***9019\",\n" +
        "            \"registeredName\": \"TFSA of Alex Tremblay\",\n" +
        "            \"inceptionDate\": \"2023-02-10\",\n" +
        "            \"beneficiaries\": [\n" +
        "                {\"name\": \"Marie Tremblay\", \"relationship\": \"Spouse\", \"percentage\": 100}\n" +
        "            ],\n" +
        "            \"accountName\": \"TFSA - Core Balanced\",\n" +
        "            \"accountType\": \"TFSA\",\n" +
        "            \"productType\": \"MUTUAL_FUND\",\n" +
        "            \"fund\": {\n" +
        "                \"fundCode\": \"TAN-BAL\",\n" +
        "                \"fundName\": \"Tangerine Core Balanced Portfolio\",\n" +
        "                \"riskLevel\": \"MEDIUM\",\n" +
        "                \"currency\": \"CAD\",\n" +
        "                \"units\": 152.347,\n" +
        "                \"navPerUnit\": 26.85,\n" +
        "                \"priceAsOf\": \"2025-09-15\"\n" +
        "            },\n" +
        "            \"MarketValue\": 409000.52,\n" +
        "            \"BookCost\": 280000.0,\n" +
        "            \"GainLoss\": 290000.52,\n" +
        "            \"GainLossPercentage\": 7.64,\n" +
        "            \"linkedDate\": \"2025-01-10\",\n" +
        "            \"lastUpdated\": \"2025-08-20T12:45:00Z\"\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": \"ACCT-INV-002\",\n" +
        "            \"userId\": \"INV-0001\",\n" +
        "            \"accountNumber\": \"***7917\",\n" +
        "            \"registeredName\": \"RSP of Alex Tremblay\",\n" +
        "            \"inceptionDate\": \"2022-11-05\",\n" +
        "            \"beneficiaries\": [\n" +
        "                {\"name\": \"Marie Tremblay\", \"relationship\": \"Spouse\", \"percentage\": 100}\n" +
        "            ],\n" +
        "            \"accountName\": \"RSP - Equity Growth\",\n" +
        "            \"accountType\": \"RSP\",\n" +
        "            \"productType\": \"MUTUAL_FUND\",\n" +
        "            \"fund\": {\n" +
        "                \"fundCode\": \"TAN-EQG\",\n" +
        "                \"fundName\": \"Tangerine Equity Growth Portfolio\",\n" +
        "                \"riskLevel\": \"MEDIUM_TO_HIGH\",\n" +
        "                \"currency\": \"CAD\",\n" +
        "                \"units\": 98.0,\n" +
        "                \"navPerUnit\": 30.12,\n" +
        "                \"priceAsOf\": \"2025-08-15\"\n" +
        "            },\n" +
        "            \"MarketValue\": 295100.76,\n" +
        "            \"BookCost\": 310000.0,\n" +
        "            \"GainLoss\": -148.24,\n" +
        "            \"GainLossPercentage\": -4.78,\n" +
        "            \"linkedDate\": \"2025-02-05\",\n" +
        "            \"lastUpdated\": \"2025-08-20T12:45:00Z\"\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": \"ACCT-INV-003\",\n" +
        "            \"userId\": \"INV-0002\",\n" +
        "            \"accountNumber\": \"***7978\",\n" +
        "            \"registeredName\": \"Non-Registered of Sophie Dubois\",\n" +
        "            \"inceptionDate\": \"2024-05-12\",\n" +
        "            \"beneficiaries\": [],\n" +
        "            \"accountName\": \"Non-Registered - Dividend\",\n" +
        "            \"accountType\": \"NON_REGISTERED\",\n" +
        "            \"productType\": \"MUTUAL_FUND\",\n" +
        "            \"fund\": {\n" +
        "                \"fundCode\": \"TAN-DIV\",\n" +
        "                \"fundName\": \"Tangerine Dividend Portfolio\",\n" +
        "                \"riskLevel\": \"MEDIUM\",\n" +
        "                \"currency\": \"CAD\",\n" +
        "                \"units\": 210.5,\n" +
        "                \"navPerUnit\": 15.75,\n" +
        "                \"priceAsOf\": \"2025-08-15\"\n" +
        "            },\n" +
        "            \"MarketValue\": 931005.38,\n" +
        "            \"BookCost\": 320000.0,\n" +
        "            \"GainLoss\": 115.38,\n" +
        "            \"GainLossPercentage\": 3.61,\n" +
        "            \"linkedDate\": \"2025-03-12\",\n" +
        "            \"lastUpdated\": \"2025-08-20T12:45:00Z\"\n" +
        "        }\n" +
        "    ],\n" +
        "    \"banking\": [\n" +
        "        {\n" +
        "            \"id\": \"ACCT-BNK-001\",\n" +
        "            \"userId\": \"INV-0001\",\n" +
        "            \"accountNumber\": \"***6043\",\n" +
        "            \"accountName\": \"Everyday Chequing\",\n" +
        "            \"accountType\": \"CHEQUING\",\n" +
        "            \"accountBalance\": 345000.33,\n" +
        "            \"currency\": \"CAD\",\n" +
        "            \"linkedDate\": \"2025-04-01\",\n" +
        "            \"lastUpdated\": \"2025-08-20T12:40:00Z\"\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": \"ACCT-BNK-002\",\n" +
        "            \"userId\": \"INV-0002\",\n" +
        "            \"accountNumber\": \"***6142\",\n" +
        "            \"accountName\": \"Savings Account\",\n" +
        "            \"currency\": \"CAD\",\n" +
        "            \"accountType\": \"SAVINGS\",\n" +
        "            \"accountBalance\": 320450.10,\n" +
        "            \"linkedDate\": \"2025-05-22\",\n" +
        "            \"lastUpdated\": \"2025-08-20T12:40:00Z\"\n" +
        "        }, \n" +
        "        { \n" +
        "            \"id\": \"ACCT-BNK-003\",\n" +
        "            \"userId\": \"INV-0001\",\n" +
        "            \"accountNumber\": \"***6045\",\n" +
        "            \"accountName\": \"Everyday Chequing\",\n" +
        "            \"accountType\": \"CHEQUING\",\n" +
        "            \"accountBalance\": 445000.33,\n" +
        "            \"currency\": \"CAD\",\n" +
        "            \"linkedDate\": \"2025-06-01\",\n" +
        "            \"lastUpdated\": \"2025-08-20T12:40:00Z\"\n" +
        "        }, \n" +
        "        { \n" +
        "            \"id\": \"ACCT-BNK-004\",\n" +
        "            \"userId\": \"INV-0002\",\n" +
        "            \"accountNumber\": \"***4932\",\n" +
        "            \"accountName\": \"Savings Account\",\n" +
        "            \"currency\": \"CAD\",\n" +
        "            \"accountType\": \"SAVINGS\",\n" +
        "            \"accountBalance\": 120450.10,\n" +
        "            \"linkedDate\": \"2025-07-22\",\n" +
        "            \"lastUpdated\": \"2025-08-20T12:40:00Z\"\n" +
        "        }\n" +
        "    ],\n" +
        "    \"custom\": [\n" +
        "        {\n" +
        "            \"id\": \"ASSET-CUS-001\",\n" +
        "            \"userId\": \"INV-0001\",\n" +
        "            \"assetName\": \"Toyota Corolla 2018\",\n" +
        "            \"assetType\": \"Vehicles\",\n" +
        "            \"assetValue\": 220000.0,\n" +
        "            \"annualizedRateOfReturn\": 15.0,\n" +
        "            \"linkedDate\": \"2025-08-15\",\n" +
        "            \"lastUpdated\": \"2025-11-20\"\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": \"ASSET-CUS-002\",\n" +
        "            \"userId\": \"INV-0002\",\n" +
        "            \"assetName\": \"Cottage in Muskoka\",\n" +
        "            \"assetType\": \"Real Estate\",\n" +
        "            \"annualizedRateOfReturn\": 52.0,\n" +
        "            \"assetValue\": 225000.0,\n" +
        "            \"linkedDate\": \"2025-09-12\",\n" +
        "            \"lastUpdated\": \"2025-11-20\"\n" +
        "        }\n" +
        "    ]\n" +
        "}\n" +
        "\n"