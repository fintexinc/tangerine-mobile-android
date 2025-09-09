package com.fintexinc.core.data.mock

const val ACTIVITIES_MOCK = "[\n" +
        "    {\n" +
        "        \"id\": \"TXN-0001\",\n" +
        "        \"accountId\": \"ACCT-INV-001\",\n" +
        "        \"accountCategory\": \"INVESTMENT\",\n" +
        "        \"accountType\": \"TFSA\",\n" +
        "        \"productType\": \"MUTUAL_FUND\",\n" +
        "        \"transactionDate\": \"2025-08-14\",\n" +
        "        \"tradeDate\": \"2025-08-14\",\n" +
        "        \"settlementDate\": \"2025-08-16\",\n" +
        "        \"postedDate\": \"2025-08-16\",\n" +
        "        \"transactionType\": \"CONTRIBUTION\",\n" +
        "        \"tradeAction\": \"BUY\",\n" +
        "        \"transactionAmount\": 500.0,\n" +
        "        \"grossAmount\": 500.0,\n" +
        "        \"feeAmount\": 0.0,\n" +
        "        \"netAmount\": 500.0,\n" +
        "        \"currency\": \"CAD\",\n" +
        "        \"transactionDescription\": \"TFSA contribution\",\n" +
        "        \"referenceNumber\": \"T-20250814-001\",\n" +
        "        \"transactionStatus\": \"SETTLED\",\n" +
        "        \"investmentDetails\": {\n" +
        "            \"fundCode\": \"TAN-BAL\",\n" +
        "            \"fundName\": \"Tangerine Core Balanced Portfolio\",\n" +
        "            \"units\": 18.6279,\n" +
        "            \"navPerUnit\": 26.85,\n" +
        "            \"distributionType\": \"DIVIDEND\",\n" +
        "            \"taxYear\": 2025\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"id\": \"TXN-0002\",\n" +
        "        \"accountId\": \"ACCT-INV-001\",\n" +
        "        \"accountCategory\": \"INVESTMENT\",\n" +
        "        \"accountType\": \"TFSA\",\n" +
        "        \"productType\": \"MUTUAL_FUND\",\n" +
        "        \"transactionDate\": \"2025-07-31\",\n" +
        "        \"tradeDate\": \"2025-07-31\",\n" +
        "        \"settlementDate\": \"2025-08-02\",\n" +
        "        \"postedDate\": \"2025-08-02\",\n" +
        "        \"transactionType\": \"DISTRIBUTION\",\n" +
        "        \"tradeAction\": \"BUY\",\n" +
        "        \"transactionAmount\": 15.43,\n" +
        "        \"grossAmount\": 15.43,\n" +
        "        \"feeAmount\": 0.0,\n" +
        "        \"netAmount\": 15.43,\n" +
        "        \"currency\": \"CAD\",\n" +
        "        \"transactionDescription\": \"Monthly distribution\",\n" +
        "        \"referenceNumber\": \"T-20250731-020\",\n" +
        "        \"transactionStatus\": \"SETTLED\",\n" +
        "        \"investmentDetails\": {\n" +
        "            \"fundCode\": \"TAN-BAL\",\n" +
        "            \"fundName\": \"Tangerine Core Balanced Portfolio\",\n" +
        "            \"units\": 0.5746,\n" +
        "            \"navPerUnit\": 26.85,\n" +
        "            \"distributionType\": \"DIVIDEND\",\n" +
        "            \"taxYear\": 2025\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"id\": \"TXN-0003\",\n" +
        "        \"accountId\": \"ACCT-INV-002\",\n" +
        "        \"accountCategory\": \"INVESTMENT\",\n" +
        "        \"accountType\": \"RSP\",\n" +
        "        \"productType\": \"MUTUAL_FUND\",\n" +
        "        \"transactionDate\": \"2025-03-01\",\n" +
        "        \"tradeDate\": \"2025-03-01\",\n" +
        "        \"settlementDate\": \"2025-03-03\",\n" +
        "        \"postedDate\": \"2025-03-03\",\n" +
        "        \"transactionType\": \"CONTRIBUTION\",\n" +
        "        \"tradeAction\": \"BUY\",\n" +
        "        \"transactionAmount\": 1000.0,\n" +
        "        \"grossAmount\": 1000.0,\n" +
        "        \"feeAmount\": 0.0,\n" +
        "        \"netAmount\": 1000.0,\n" +
        "        \"currency\": \"CAD\",\n" +
        "        \"transactionDescription\": \"RSP contribution\",\n" +
        "        \"referenceNumber\": \"T-20250301-010\",\n" +
        "        \"transactionStatus\": \"SETTLED\",\n" +
        "        \"investmentDetails\": {\n" +
        "            \"fundCode\": \"TAN-EQG\",\n" +
        "            \"fundName\": \"Tangerine Equity Growth Portfolio\",\n" +
        "            \"units\": 33.1964,\n" +
        "            \"navPerUnit\": 30.12,\n" +
        "            \"distributionType\": \"DIVIDEND\",\n" +
        "            \"taxYear\": 2025\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"id\": \"TXN-0004\",\n" +
        "        \"accountId\": \"ACCT-INV-002\",\n" +
        "        \"accountCategory\": \"INVESTMENT\",\n" +
        "        \"accountType\": \"RSP\",\n" +
        "        \"productType\": \"MUTUAL_FUND\",\n" +
        "        \"transactionDate\": \"2025-06-15\",\n" +
        "        \"tradeDate\": \"2025-06-15\",\n" +
        "        \"settlementDate\": \"2025-06-17\",\n" +
        "        \"postedDate\": \"2025-06-17\",\n" +
        "        \"transactionType\": \"REDEMPTION\",\n" +
        "        \"tradeAction\": \"SELL\",\n" +
        "        \"transactionAmount\": 301.2,\n" +
        "        \"grossAmount\": 301.2,\n" +
        "        \"feeAmount\": 0.0,\n" +
        "        \"netAmount\": 301.2,\n" +
        "        \"currency\": \"CAD\",\n" +
        "        \"transactionDescription\": \"Partial redemption\",\n" +
        "        \"referenceNumber\": \"T-20250615-005\",\n" +
        "        \"transactionStatus\": \"SETTLED\",\n" +
        "        \"investmentDetails\": {\n" +
        "            \"fundCode\": \"TAN-EQG\",\n" +
        "            \"fundName\": \"Tangerine Equity Growth Portfolio\",\n" +
        "            \"units\": 10.0,\n" +
        "            \"navPerUnit\": 30.12,\n" +
        "            \"distributionType\": \"DIVIDEND\",\n" +
        "            \"taxYear\": 2025\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"id\": \"TXN-0005\",\n" +
        "        \"accountId\": \"ACCT-BNK-001\",\n" +
        "        \"accountCategory\": \"BANKING\",\n" +
        "        \"accountType\": \"CHEQUING\",\n" +
        "        \"productType\": \"GIC\",\n" +
        "        \"transactionDate\": \"2025-08-10\",\n" +
        "        \"tradeDate\": \"2025-08-10\",\n" +
        "        \"settlementDate\": \"2025-08-10\",\n" +
        "        \"postedDate\": \"2025-08-10\",\n" +
        "        \"transactionType\": \"DEPOSIT\",\n" +
        "        \"tradeAction\": \"BUY\",\n" +
        "        \"transactionAmount\": 1500.0,\n" +
        "        \"grossAmount\": 1500.0,\n" +
        "        \"feeAmount\": 0.0,\n" +
        "        \"netAmount\": 1500.0,\n" +
        "        \"currency\": \"CAD\",\n" +
        "        \"transactionDescription\": \"Paycheque deposit\",\n" +
        "        \"referenceNumber\": \"D-20250810-001\",\n" +
        "        \"transactionStatus\": \"COMPLETED\"\n" +
        "    },\n" +
        "    {\n" +
        "        \"id\": \"TXN-0006\",\n" +
        "        \"accountId\": \"ACCT-INV-003\",\n" +
        "        \"accountCategory\": \"INVESTMENT\",\n" +
        "        \"accountType\": \"NON_REGISTERED\",\n" +
        "        \"productType\": \"MUTUAL_FUND\",\n" +
        "        \"transactionDate\": \"2025-08-01\",\n" +
        "        \"tradeDate\": \"2025-08-01\",\n" +
        "        \"settlementDate\": \"2025-08-03\",\n" +
        "        \"postedDate\": \"2025-08-03\",\n" +
        "        \"transactionType\": \"PURCHASE\",\n" +
        "        \"tradeAction\": \"BUY\",\n" +
        "        \"transactionAmount\": 1000.0,\n" +
        "        \"grossAmount\": 1000.0,\n" +
        "        \"feeAmount\": 0.0,\n" +
        "        \"netAmount\": 1000.0,\n" +
        "        \"currency\": \"CAD\",\n" +
        "        \"transactionDescription\": \"Initial non-registered purchase\",\n" +
        "        \"referenceNumber\": \"NR-20250801-001\",\n" +
        "        \"transactionStatus\": \"SETTLED\",\n" +
        "        \"investmentDetails\": {\n" +
        "            \"fundCode\": \"TAN-DIV\",\n" +
        "            \"fundName\": \"Tangerine Dividend Portfolio\",\n" +
        "            \"units\": 63.4921,\n" +
        "            \"navPerUnit\": 15.75,\n" +
        "            \"distributionType\": \"DIVIDEND\",\n" +
        "            \"taxYear\": 2025\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"id\": \"TXN-0007\",\n" +
        "        \"accountId\": \"ACCT-INV-003\",\n" +
        "        \"accountCategory\": \"INVESTMENT\",\n" +
        "        \"accountType\": \"NON_REGISTERED\",\n" +
        "        \"productType\": \"MUTUAL_FUND\",\n" +
        "        \"transactionDate\": \"2025-08-15\",\n" +
        "        \"tradeDate\": \"2025-08-15\",\n" +
        "        \"settlementDate\": \"2025-08-17\",\n" +
        "        \"postedDate\": \"2025-08-17\",\n" +
        "        \"transactionType\": \"DISTRIBUTION\",\n" +
        "        \"tradeAction\": \"BUY\",\n" +
        "        \"transactionAmount\": 12.6,\n" +
        "        \"grossAmount\": 12.6,\n" +
        "        \"feeAmount\": 0.0,\n" +
        "        \"netAmount\": 12.6,\n" +
        "        \"currency\": \"CAD\",\n" +
        "        \"transactionDescription\": \"Monthly distribution\",\n" +
        "        \"referenceNumber\": \"NR-20250815-002\",\n" +
        "        \"transactionStatus\": \"SETTLED\",\n" +
        "        \"investmentDetails\": {\n" +
        "            \"fundCode\": \"TAN-DIV\",\n" +
        "            \"fundName\": \"Tangerine Dividend Portfolio\",\n" +
        "            \"units\": 0.8,\n" +
        "            \"navPerUnit\": 15.75,\n" +
        "            \"distributionType\": \"DIVIDEND\",\n" +
        "            \"taxYear\": 2025\n" +
        "        }\n" +
        "    },\n" +
        "    {\n" +
        "        \"id\": \"TXN-0008\",\n" +
        "        \"accountId\": \"ACCT-INV-003\",\n" +
        "        \"accountCategory\": \"INVESTMENT\",\n" +
        "        \"accountType\": \"NON_REGISTERED\",\n" +
        "        \"productType\": \"MUTUAL_FUND\",\n" +
        "        \"transactionDate\": \"2025-08-20\",\n" +
        "        \"tradeDate\": \"2025-08-20\",\n" +
        "        \"settlementDate\": \"2025-08-22\",\n" +
        "        \"postedDate\": \"2025-08-22\",\n" +
        "        \"transactionType\": \"REDEMPTION\",\n" +
        "        \"tradeAction\": \"SELL\",\n" +
        "        \"transactionAmount\": 157.5,\n" +
        "        \"grossAmount\": 157.5,\n" +
        "        \"feeAmount\": 0.0,\n" +
        "        \"netAmount\": 157.5,\n" +
        "        \"currency\": \"CAD\",\n" +
        "        \"transactionDescription\": \"Partial redemption for cash needs\",\n" +
        "        \"referenceNumber\": \"NR-20250820-003\",\n" +
        "        \"transactionStatus\": \"SETTLED\",\n" +
        "        \"investmentDetails\": {\n" +
        "            \"fundCode\": \"TAN-DIV\",\n" +
        "            \"fundName\": \"Tangerine Dividend Portfolio\",\n" +
        "            \"units\": 10.0,\n" +
        "            \"navPerUnit\": 15.75,\n" +
        "            \"distributionType\": \"DIVIDEND\",\n" +
        "            \"taxYear\": 2025\n" +
        "        }\n" +
        "    }\n" +
        "]\n" +
        "\n"