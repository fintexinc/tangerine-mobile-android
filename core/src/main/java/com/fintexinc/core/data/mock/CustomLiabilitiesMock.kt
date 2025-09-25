package com.fintexinc.core.data.mock

const val CUSTOM_LIABILITIES_MOCK = "[{\n" +
        "    \"custom_liability\": {\n" +
        "        \"id\": 1,\n" +
        "        \"userId\": \"INV-0001\",\n" +
        "        \"type\": \"MORTGAGE\",\n" +
        "        \"name\": \"Primary Residence Mortgage\",\n" +
        "        \"currency\": \"CAD\",\n" +
        "        \"current_balance\": 285000.0,\n" +
        "        \"monthly_payment\": 1850.0,\n" +
        "        \"interest_rate\": 3.25,\n" +
        "        \"effective_date\": \"2021-06-15\",\n" +
        "        \"is_deleted\": false,\n" +
        "        \"notes\": \"5-year fixed rate mortgage, renewed in 2024\",\n" +
        "        \"created_at\": \"2021-06-15T11:30:00Z\",\n" +
        "        \"updated_at\": \"2025-08-20T14:25:00Z\"\n" +
        "    },\n" +
        "    \"custom_liability_history\": [\n" +
        "        {\n" +
        "            \"id\": 1,\n" +
        "            \"custom_liability_id\": 1,\n" +
        "            \"is_deleted\": false,\n" +
        "            \"value\": 350000.0,\n" +
        "            \"effective_date\": \"2021-06-15\"\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 2,\n" +
        "            \"custom_liability_id\": 1,\n" +
        "            \"is_deleted\": false,\n" +
        "            \"value\": 325000.0,\n" +
        "            \"effective_date\": \"2022-06-15\"\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 3,\n" +
        "            \"custom_liability_id\": 1,\n" +
        "            \"is_deleted\": false,\n" +
        "            \"value\": 305000.0,\n" +
        "            \"effective_date\": \"2023-06-15\"\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 4,\n" +
        "            \"custom_liability_id\": 1,\n" +
        "            \"is_deleted\": false,\n" +
        "            \"value\": 285000.0,\n" +
        "            \"effective_date\": \"2024-06-15\"\n" +
        "        }\n" +
        "    ]\n" +
        "}, {\n" +
        "    \"custom_liability\": {\n" +
        "        \"id\": 2,\n" +
        "        \"userId\": \"INV-0002\",\n" +
        "        \"type\": \"CREDIT_LINE\",\n" +
        "        \"name\": \"Business Line of Credit\",\n" +
        "        \"currency\": \"CAD\",\n" +
        "        \"current_balance\": 15000.0,\n" +
        "        \"monthly_payment\": 450.0,\n" +
        "        \"interest_rate\": 5.75,\n" +
        "        \"effective_date\": \"2023-09-20\",\n" +
        "        \"is_deleted\": false,\n" +
        "        \"notes\": \"Used for business cash flow management\",\n" +
        "        \"created_at\": \"2023-09-20T08:45:00Z\",\n" +
        "        \"updated_at\": \"2025-08-20T14:25:00Z\"\n" +
        "    },\n" +
        "    \"custom_liability_history\": [\n" +
        "        {\n" +
        "            \"id\": 5,\n" +
        "            \"custom_liability_id\": 2,\n" +
        "            \"is_deleted\": false,\n" +
        "            \"value\": 25000.0,\n" +
        "            \"effective_date\": \"2023-09-20\"\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 6,\n" +
        "            \"custom_liability_id\": 2,\n" +
        "            \"is_deleted\": false,\n" +
        "            \"value\": 22000.0,\n" +
        "            \"effective_date\": \"2024-03-20\"\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 7,\n" +
        "            \"custom_liability_id\": 2,\n" +
        "            \"is_deleted\": false,\n" +
        "            \"value\": 18000.0,\n" +
        "            \"effective_date\": \"2024-09-20\"\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 8,\n" +
        "            \"custom_liability_id\": 2,\n" +
        "            \"is_deleted\": false,\n" +
        "            \"value\": 15000.0,\n" +
        "            \"effective_date\": \"2025-03-20\"\n" +
        "        }\n" +
        "    ]\n" +
        "}, {\n" +
        "    \"custom_liability\": {\n" +
        "        \"id\": 3,\n" +
        "        \"userId\": \"INV-0001\",\n" +
        "        \"type\": \"PERSONAL_LOAN\",\n" +
        "        \"name\": \"Car Loan - Toyota Corolla\",\n" +
        "        \"currency\": \"CAD\",\n" +
        "        \"current_balance\": 8500.0,\n" +
        "        \"monthly_payment\": 285.0,\n" +
        "        \"interest_rate\": 4.99,\n" +
        "        \"effective_date\": \"2024-03-15\",\n" +
        "        \"is_deleted\": false,\n" +
        "        \"notes\": \"4-year auto loan for vehicle purchase\",\n" +
        "        \"created_at\": \"2024-03-15T13:20:00Z\",\n" +
        "        \"updated_at\": \"2025-08-20T14:25:00Z\"\n" +
        "    },\n" +
        "    \"custom_liability_history\": [\n" +
        "        {\n" +
        "            \"id\": 9,\n" +
        "            \"custom_liability_id\": 3,\n" +
        "            \"is_deleted\": false,\n" +
        "            \"value\": 12000.0,\n" +
        "            \"effective_date\": \"2024-03-15\"\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 10,\n" +
        "            \"custom_liability_id\": 3,\n" +
        "            \"is_deleted\": false,\n" +
        "            \"value\": 10500.0,\n" +
        "            \"effective_date\": \"2024-09-15\"\n" +
        "        },\n" +
        "        {\n" +
        "            \"id\": 11,\n" +
        "            \"custom_liability_id\": 3,\n" +
        "            \"is_deleted\": false,\n" +
        "            \"value\": 8500.0,\n" +
        "            \"effective_date\": \"2025-03-15\"\n" +
        "        }\n" +
        "    ]\n" +
        "}]\n"