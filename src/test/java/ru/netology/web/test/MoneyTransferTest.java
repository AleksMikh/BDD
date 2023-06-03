package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTest {

    @BeforeEach
    public void successAuth() {

        open("http://localhost:9999");

        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void positiveSecondCardToFirst() {

        val dashboardPage = new DashboardPage();

        int balanceFirstCard = dashboardPage.getFirstCardBalance();
        int balanceSecondCard = dashboardPage.getSecondCardBalance();
        val moneyTransfer = dashboardPage.firstCardButton();
        val infoCard = DataHelper.getSecondCardNumber();
        String sum = "1000";
        moneyTransfer.transferForm(sum, infoCard);

        assertEquals(balanceFirstCard + Integer.parseInt(sum), dashboardPage.getFirstCardBalance());
        assertEquals(balanceSecondCard - Integer.parseInt(sum), dashboardPage.getSecondCardBalance());
    }

    @Test
    void positiveFirstCardToSecond() {

        val dashboardPage = new DashboardPage();

        int balanceFirstCard = dashboardPage.getFirstCardBalance();
        int balanceSecondCard = dashboardPage.getSecondCardBalance();
        val moneyTransfer = dashboardPage.secondCardButton();
        val infoCard = DataHelper.getFirstCardNumber();
        String sum = "2000";
        moneyTransfer.transferForm(sum, infoCard);

        assertEquals(balanceFirstCard - Integer.parseInt(sum), dashboardPage.getFirstCardBalance());
        assertEquals(balanceSecondCard + Integer.parseInt(sum), dashboardPage.getSecondCardBalance());
    }

    @Test
    void clickCancel() {

        val dashboardPage = new DashboardPage();

        val moneyTransfer = dashboardPage.firstCardButton();
        moneyTransfer.cancelButton();
    }

    @Test
    void negativeSecondCardToFirst() {

        val dashboardPage = new DashboardPage();

        val moneyTransfer = dashboardPage.secondCardButton();
        val infoCard = DataHelper.getFirstCardNumber();
        String sum = "15000";
        moneyTransfer.transferForm(sum, infoCard);
        moneyTransfer.getError();
    }

    @Test
    void negativeFirstCardToSecond() {

        val dashboardPage = new DashboardPage();

        val moneyTransfer = dashboardPage.firstCardButton();
        val infoCard = DataHelper.getSecondCardNumber();
        String sum = "50000";
        moneyTransfer.transferForm(sum, infoCard);
        moneyTransfer.getError();
    }

    @Test
    void negativeTransferZero() {

        val dashboardPage = new DashboardPage();

        val moneyTransfer = dashboardPage.secondCardButton();
        val infoCard = DataHelper.getFirstCardNumber();
        String sum = "0";
        moneyTransfer.transferForm(sum, infoCard);
        moneyTransfer.getError();
    }
}