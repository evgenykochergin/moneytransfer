package com.evgenykochergin.moneytransfer.rest;

import com.evgenykochergin.moneytransfer.dto.AccountRequestDto;
import com.evgenykochergin.moneytransfer.dto.AccountResponseDto;
import com.evgenykochergin.moneytransfer.dto.AccountTransferDto;
import com.evgenykochergin.moneytransfer.model.Account;
import com.evgenykochergin.moneytransfer.model.Amount;
import com.evgenykochergin.moneytransfer.service.AccountService;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public Collection<AccountResponseDto> getAll() {
        return accountService.getAll().stream().map(AccountResponseDto::of).collect(toList());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountResponseDto get(@PathParam("id") UUID id) {
        return AccountResponseDto.of(accountService.get(id));
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") UUID id) {
        accountService.remove(id);
        return Response.ok().build();
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(AccountRequestDto account) {
        Account createdAccount = accountService.add(Account
                .builder()
                .id(UUID.randomUUID())
                .amount(Amount.of(account.getAmount()))
                .version(0)
                .build()
        );
        return Response.status(Response.Status.CREATED).entity(AccountResponseDto.of(createdAccount)).build();
    }

    @POST
    @Path("/{id}/transfer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response transfer(@PathParam("id") UUID from, AccountTransferDto accountTransfer) {
        accountService.transfer(from, accountTransfer.getTo(), Amount.of(accountTransfer.getAmount()));
        return Response.status(Response.Status.ACCEPTED)
                .entity(AccountResponseDto.of(accountService.get(from)))
                .build();
    }
}