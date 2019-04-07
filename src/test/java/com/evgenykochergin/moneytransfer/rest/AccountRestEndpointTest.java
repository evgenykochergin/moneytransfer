package com.evgenykochergin.moneytransfer.rest;

import com.evgenykochergin.moneytransfer.dto.AccountRequestDto;
import com.evgenykochergin.moneytransfer.dto.AccountResponseDto;
import com.evgenykochergin.moneytransfer.dto.AccountTransferDto;
import com.evgenykochergin.moneytransfer.guice.module.AccountModule;
import com.evgenykochergin.moneytransfer.guice.module.CoreModule;
import com.evgenykochergin.moneytransfer.guice.module.GuiceServetModule;
import com.evgenykochergin.moneytransfer.webserver.WebServer;
import com.google.inject.Guice;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AccountRestEndpointTest {

    private static WebServer webServer;

    private final RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(8080)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .build();

    @BeforeClass
    public static void setUp() throws Exception {
        webServer = WebServer.start(
                8080,
                () -> Guice.createInjector(
                        new GuiceServetModule(),
                        new CoreModule(),
                        new AccountModule())
        );
    }

    @AfterClass
    public static void tearDown() throws Exception {
        webServer.close();
    }

    @Test
    public void canPostAccount() {
        AccountResponseDto accountResponse = postAccount(100);
        assertThat(accountResponse.getAmount(), equalTo(new BigDecimal(100)));
    }

    @Test
    public void canGetAccount() {
        AccountResponseDto createdAccount = postAccount(100);
        AccountResponseDto accountResponse = getAccount(createdAccount.getId());
        assertThat(accountResponse, equalTo(createdAccount));
    }

    @Test
    public void canTransfer() {
        AccountResponseDto fromResponse = postAccount(100);
        AccountResponseDto toResponse = postAccount(50);
        given()
                .spec(requestSpecification)
                .body(new AccountTransferDto(toResponse.getId(), new BigDecimal(10)))
                .post("/account/" + fromResponse.getId() + "/transfer")
                .then()
                .statusCode(202);

        AccountResponseDto updatedFrom = getAccount(fromResponse.getId());
        AccountResponseDto updatedTo = getAccount(toResponse.getId());

        assertThat(updatedFrom.getAmount(), equalTo(new BigDecimal(90)));
        assertThat(updatedTo.getAmount(), equalTo(new BigDecimal(60)));
    }

    @Test
    public void cannotTransferWithExceededAmount() {
        AccountResponseDto fromResponse = postAccount(100);
        AccountResponseDto toResponse = postAccount(50);
        given()
                .spec(requestSpecification)
                .body(new AccountTransferDto(toResponse.getId(), new BigDecimal(110)))
                .post("/account/" + fromResponse.getId() + "/transfer")
                .then()
                .statusCode(500);

        AccountResponseDto notUpdatedFrom = getAccount(fromResponse.getId());
        AccountResponseDto notUpdatedTo = getAccount(toResponse.getId());

        assertThat(notUpdatedFrom, equalTo(fromResponse));
        assertThat(notUpdatedTo, equalTo(toResponse));
    }

    private AccountResponseDto postAccount(int amount) {
        Response response = given()
                .spec(requestSpecification)
                .body(new AccountRequestDto(new BigDecimal(amount)))
                .post("/account");
        response.then().statusCode(201);
        return response.as(AccountResponseDto.class);
    }

    private AccountResponseDto getAccount(UUID id) {
        Response response = given()
                .spec(requestSpecification)
                .get("/account/" + id);
        response.then().statusCode(200);
        return response.as(AccountResponseDto.class);
    }

}