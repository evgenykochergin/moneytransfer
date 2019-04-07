package com.evgenykochergin.moneytransfer.rest;

import com.evgenykochergin.moneytransfer.dto.AccountRequestDto;
import com.evgenykochergin.moneytransfer.dto.AccountResponseDto;
import com.evgenykochergin.moneytransfer.dto.AccountTransferConcurrentDto;
import com.evgenykochergin.moneytransfer.dto.AccountTransferDto;
import com.evgenykochergin.moneytransfer.model.Account;
import com.evgenykochergin.moneytransfer.model.Amount;
import com.evgenykochergin.moneytransfer.persistance.jdbc.exception.OptimisticLockException;
import com.evgenykochergin.moneytransfer.repository.exception.EntityNotFountException;
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
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountRestEndpoint {

    private final AccountService accountService;

    @Inject
    public AccountRestEndpoint(AccountService accountService) {
        this.accountService = accountService;
    }

    @GET
    public Response getAll() {
        List<AccountResponseDto> accounts = accountService
                .getAll()
                .stream()
                .map(AccountResponseDto::of)
                .collect(toList());
        if (accounts.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(accounts).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") UUID id) {
        try {
            return Response.ok(AccountResponseDto.of(accountService.get(id))).build();
        } catch (EntityNotFountException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") UUID id) {
        try {
            accountService.remove(id);
        } catch (EntityNotFountException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }


    @POST
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
    public Response transfer(@PathParam("id") UUID from, AccountTransferDto accountTransfer) {
        try {
            accountService.transfer(from, accountTransfer.getTo(), Amount.of(accountTransfer.getAmount()));
        } catch (EntityNotFountException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (OptimisticLockException e) {
            // to many requests
            return Response.status(429)
                    .entity(new AccountTransferConcurrentDto("Concurrent transfer, try again", from, accountTransfer.getTo()))
                    .build();
        }
        return Response.status(Response.Status.ACCEPTED)
                .entity(AccountResponseDto.of(accountService.get(from)))
                .build();
    }
}