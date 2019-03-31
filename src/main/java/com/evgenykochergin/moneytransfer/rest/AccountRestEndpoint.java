package com.evgenykochergin.moneytransfer.rest;

import com.evgenykochergin.moneytransfer.dto.AccountDto;
import com.evgenykochergin.moneytransfer.model.Account;
import com.evgenykochergin.moneytransfer.model.Amount;
import com.evgenykochergin.moneytransfer.service.AccountService;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Path("/account")
public class AccountRestEndpoint {
    private final AccountService accountService;

    @Inject
    public AccountRestEndpoint(AccountService accountService) {
        this.accountService = accountService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<AccountDto> getAll() {
        return accountService.getAll().stream().map(AccountDto::of).collect(toList());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountDto get(@PathParam("id") UUID id) {
        return AccountDto.of(accountService.get(id));
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@FormParam("amount") BigDecimal amount) {
        Account createdAccount = accountService.add(Account
                .builder()
                .id(UUID.randomUUID())
                .amount(Amount.of(amount))
                .version(0)
                .build()
        );
        return Response.status(Response.Status.CREATED).entity(createdAccount).build();
    }

    @POST
    @Path("/{id}/transfer")
    @Produces(MediaType.APPLICATION_JSON)
    public void transfer(@PathParam("id") UUID from, @FormParam("to") UUID to, @FormParam("amount") BigDecimal amount) {
        accountService.transfer(from, to, Amount.of(amount));
    }
}