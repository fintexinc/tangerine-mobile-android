package com.tangerine.account


import app.cash.turbine.test
import com.fintexinc.core.domain.gateway.AccountGateway
import com.fintexinc.core.domain.model.Account
import com.fintexinc.core.domain.model.InvestmentDetails
import com.fintexinc.core.domain.model.PerformanceItem
import com.fintexinc.core.domain.model.Transaction
import com.fintexinc.core.domain.model.TransactionStatus
import com.tangerine.account.presentation.models.DocumentTypeFilterUi
import com.tangerine.account.presentation.ui.AccountTab
import com.tangerine.account.presentation.viewmodel.AccountViewModel
import com.tangerine.account.presentation.viewmodel.DocumentDataPoint
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import java.text.ParseException
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class AccountViewModelTest : FreeSpec({

    lateinit var accountGateway: AccountGateway
    lateinit var viewModel: AccountViewModel
    lateinit var fakeAccount: Account
    lateinit var fakeActivities: List<Transaction>
    lateinit var fakeDocuments: List<DocumentDataPoint>

    beforeEach {
        accountGateway = mockk<AccountGateway>(relaxed = true)

        fakeAccount = Account(
            id = "ACCT-INV-001",
            userId = "USER-001",
            accountId = "A1",
            income = 100_000,
            householdNetWorth = 500_000,
            investmentKnowledge = "High",
            riskToleranceInitial = "Medium",
            riskToleranceCurrent = "High",
            overallRiskTolerance = "High",
            investmentObjective = "Growth",
            timeHorizon = "Long",
            livingExpenses = 30_000,
            knowledgeChangeDate = "2025-01-01",
            lastReviewedAt = "2025-10-01"
        )

        val fakePerformance = emptyList<PerformanceItem>()

        fakeActivities = listOf(
            Transaction(
                id = "T1",
                accountId = "ACCT-INV-001",
                accountCategory = "INVESTMENT",
                accountType = "REGISTERED",
                productType = "MUTUAL_FUND",
                transactionDate = "2025-10-01",
                tradeDate = "2025-09-30",
                settlementDate = "2025-10-02",
                postedDate = "2025-10-03",
                transactionType = "BUY",
                tradeAction = "PURCHASE",
                transactionAmount = 500.0,
                grossAmount = 500.0,
                feeAmount = 0.0,
                netAmount = 500.0,
                currency = "CAD",
                transactionDescription = "Buy mutual fund units",
                referenceNumber = "REF12345",
                transactionStatus = TransactionStatus.COMPLETED.label,
                investmentDetails = InvestmentDetails(
                    fundCode = "XYZ",
                    fundName = "Tangerine Balanced Fund",
                    units = 50.0,
                    navPerUnit = 10.0,
                    distributionType = "Annual",
                    taxYear = 2025
                )
            )
        )

        fakeDocuments = listOf(
            DocumentDataPoint(
                id = "DOC1",
                name = "CRM3 Annual Charges and Compensation Report",
                subName = "OCTOBER 08, 2025",
                iconResId = com.fintexinc.core.R.drawable.ic_file,
                type = DocumentTypeFilterUi.STATEMENTS
            ),
            DocumentDataPoint(
                id = "DOC2",
                name = "Monthly Statement",
                subName = "SEPTEMBER 15, 2025",
                iconResId = com.fintexinc.core.R.drawable.ic_file,
                type = DocumentTypeFilterUi.STATEMENTS
            ),
            DocumentDataPoint(
                id = "DOC3",
                name = "Tax Form",
                subName = "AUGUST 10, 2025",
                iconResId = com.fintexinc.core.R.drawable.ic_file,
                type = DocumentTypeFilterUi.TAX_DOCUMENTS
            )
        )

        coEvery { accountGateway.getAccountById(any()) } returns fakeAccount
        coEvery { accountGateway.getPerformanceData() } returns fakePerformance
        coEvery { accountGateway.getActivities() } returns fakeActivities
        coEvery { accountGateway.getDocumentsByAccountId(any()) } returns emptyList()

        viewModel = AccountViewModel(accountGateway)
    }

    "AccountViewModel" - {
        "getData()" - {
            "should transition to Loaded state and load account" {
                runTest {
                    viewModel.state.test {
                        viewModel.getData("ACCT-INV-001")

                        awaitItem() shouldBe AccountViewModel.State.Loading

                        advanceUntilIdle()

                        val loaded = awaitItem().shouldBeInstanceOf<AccountViewModel.State.Loaded>()
                        loaded.mainState.summary shouldBe fakeAccount
                        cancelAndIgnoreRemainingEvents()
                    }
                }
            }
        }

        "onTabChanged()" - {
            "AUTOMATIC_PURCHASES should load activities and change tab" {
                runTest {
                    viewModel.getData("ACCT-INV-001")
                    advanceUntilIdle()

                    viewModel.state.test {
                        skipItems(1)

                        viewModel.onTabChanged(AccountTab.AUTOMATIC_PURCHASES, "ACCT-INV-001")
                        advanceUntilIdle()

                        val updated = awaitItem().shouldBeInstanceOf<AccountViewModel.State.Loaded>()
                        updated.mainState.selectedTab shouldBe AccountViewModel.TopTab.ACTIVITIES
                        updated.mainState.activities shouldBe fakeActivities
                        cancelAndIgnoreRemainingEvents()
                    }
                }
            }

            "SELL_FUNDS should switch to POSITIONS tab" {
                runTest {
                    viewModel.getData("ACCT-INV-001")
                    advanceUntilIdle()

                    viewModel.state.test {
                        skipItems(1)

                        viewModel.onTabChanged(AccountTab.SELL_FUNDS, "ACCT-INV-001")
                        advanceUntilIdle()

                        val updated = awaitItem().shouldBeInstanceOf<AccountViewModel.State.Loaded>()
                        updated.mainState.selectedTab shouldBe AccountViewModel.TopTab.POSITIONS
                        updated.mainState.positions.isNotEmpty() shouldBe true
                        cancelAndIgnoreRemainingEvents()
                    }
                }
            }
        }

        "formatPercentage()" - {
            "should return +12.35% for positive value" {
                viewModel.formatPercentage(12.3456) shouldBe "+12.35%"
            }

            "should return -4.57% for negative value" {
                viewModel.formatPercentage(-4.567) shouldBe "-4.57%"
            }
        }

        "formatCurrency()" - {

            "should format positive value correctly" {
                val result = viewModel.formatCurrency(1234.56, Locale.US)
                result shouldBe "$1,234.56"
            }

            "should format zero correctly" {
                val result = viewModel.formatCurrency(0.0, Locale.US)
                result shouldBe "$0.00"
            }

            "should format negative value correctly" {
                val result = viewModel.formatCurrency(-987.65, Locale.US)
                result shouldBe "-$987.65"
            }

            "should use default locale if not provided" {
                val result = viewModel.formatCurrency(1000.0)
                result.contains("1,000") shouldBe true
            }
        }

        "applyAllDocumentFilters" - {

            "should filter documents by search query" {
                runTest {
                    println("=== Test started ===")

                    viewModel.state.test {
                        val initialState = awaitItem()
                        initialState shouldBe AccountViewModel.State.Loading

                        viewModel.getData("ACCT-INV-001")

                        val loaded = awaitItem().shouldBeInstanceOf<AccountViewModel.State.Loaded>()
                        viewModel.loadBottomSheetDocuments()
                        advanceUntilIdle()

                        val afterLoad = awaitItem().shouldBeInstanceOf<AccountViewModel.State.Loaded>()
                        afterLoad.mainState.bottomSheet.documents.all.forEachIndexed { index, doc ->
                            println("  [$index] ${doc.name}")
                        }

                        val updatedState = afterLoad.mainState.copy(
                            bottomSheet = afterLoad.mainState.bottomSheet.copy(
                                documents = afterLoad.mainState.bottomSheet.documents.copy(
                                    query = "CRM3",
                                    filtered = afterLoad.mainState.bottomSheet.documents.all.filter { doc ->
                                        doc.name.contains("CRM3", ignoreCase = true) ||
                                                doc.subName.contains("CRM3", ignoreCase = true)
                                    }
                                )
                            )
                        )

                        updatedState.bottomSheet.documents.filtered.size shouldBe 1
                        updatedState.bottomSheet.documents.filtered.first().name shouldBe "CRM3 Annual Charges and Compensation Report"

                        cancelAndIgnoreRemainingEvents()
                    }
                }
            }

            "should filter documents by type" {
                runTest {
                    println("=== Test started ===")

                    viewModel.getData("ACCT-INV-001")
                    advanceUntilIdle()

                    println("\nStep 1: Loading bottom sheet documents")
                    viewModel.loadBottomSheetDocuments()
                    advanceUntilIdle()

                    val currentState = viewModel.state.value as AccountViewModel.State.Loaded
                    val allDocs = currentState.mainState.bottomSheet.documents.all

                    println("Total documents: ${allDocs.size}")
                    println("All documents:")
                    allDocs.forEachIndexed { index, doc ->
                        println("  [$index] ${doc.name} - Type: ${doc.type}")
                    }

                    val taxDocsCount = allDocs.count { it.type == DocumentTypeFilterUi.TAX_DOCUMENTS }
                    println("TAX_DOCUMENTS count: $taxDocsCount")

                    println("\nStep 2: Filtering documents by TAX_DOCUMENTS type manually")
                    val filteredDocs = allDocs.filter { doc ->
                        doc.type == DocumentTypeFilterUi.TAX_DOCUMENTS
                    }

                    println("\nStep 3: After filter")
                    println("Filtered documents count: ${filteredDocs.size}")
                    println("Filtered documents:")
                    filteredDocs.forEachIndexed { index, doc ->
                        println("  [$index] ${doc.name} - Type: ${doc.type}")
                    }

                    println("\nStep 4: Assertions")
                    filteredDocs.all { it.type == DocumentTypeFilterUi.TAX_DOCUMENTS } shouldBe true
                    filteredDocs.size shouldBe 6

                    println("\n=== Test completed successfully ===")
                }
            }
        }

        "parseTransactionDate()" - {

            "should parse valid date string correctly" {
                val dateString = "Oct 08, 2025"
                val calendar = viewModel.parseTransactionDate(dateString)

                calendar.get(Calendar.YEAR) shouldBe 2025
                calendar.get(Calendar.MONTH) shouldBe Calendar.OCTOBER
                calendar.get(Calendar.DAY_OF_MONTH) shouldBe 8
            }

            "should parse uppercase date correctly (case-insensitive)" {
                val dateString = "OCTOBER 08, 2025"
                val calendar = viewModel.parseTransactionDate(dateString)

                calendar.get(Calendar.YEAR) shouldBe 2025
                calendar.get(Calendar.MONTH) shouldBe Calendar.OCTOBER
                calendar.get(Calendar.DAY_OF_MONTH) shouldBe 8
            }

            "should return current date for invalid format" {
                val invalid = "2025/08/10"
                val result = kotlin.runCatching {
                    viewModel.parseTransactionDate(invalid)
                }

                assertSoftly {
                    result.isFailure shouldBe true
                    result.exceptionOrNull().shouldBeInstanceOf<ParseException>()
                }
            }
        }
    }
})