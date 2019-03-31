package com.evgenykochergin.moneytransfer.repository.impl;

import com.evgenykochergin.moneytransfer.model.Account;
import com.evgenykochergin.moneytransfer.model.Amount;
import com.evgenykochergin.moneytransfer.persistance.jdbc.JdbcTransactionalFactory;
import com.evgenykochergin.moneytransfer.repository.AccountRepository;
import com.evgenykochergin.moneytransfer.repository.exception.EntityNotFountException;
import com.evgenykochergin.moneytransfer.repository.exception.OptimisticLockException;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class JdbcAccountRepository implements AccountRepository {

    private static final String FIND_QUERY =
            "SELECT * FROM ACCOUNT WHERE ID=?";

    private static final String FIND_ALL_QUERY =
            "SELECT * FROM ACCOUNT";

    private static final String SAVE_QUERY =
            "INSERT INTO ACCOUNT (ID, AMOUNT, VERSION) VALUES (?, ?, ?, ?)";

    private static final String UPDATE_QUERY =
            "UPDATE ACCOUNT SET AMOUNT=?, VERSION=VERSION+1 WHERE ID=? AND VERSION=?";

    private static final String DELETE_QUERY =
            "DELETE FROM ACCOUNT WHERE ID=?";


    private final JdbcTransactionalFactory jdbcTransactionalFactory;

    @Inject
    public JdbcAccountRepository(JdbcTransactionalFactory jdbcTransactionalFactory) {
        this.jdbcTransactionalFactory = jdbcTransactionalFactory;
    }

    @Override
    public Account find(UUID id) {
        return jdbcTransactionalFactory.create(FIND_QUERY, statement -> {
            statement.setObject(1, id);
            statement.execute();
            try (ResultSet resultSet = statement.getResultSet()) {
                if (resultSet.first()) {
                    return createAccount(resultSet);
                } else {
                    throw new EntityNotFountException(id, Account.class);
                }
            }
        }).execute();
    }

    @Override
    public Collection<Account> findAll() {
        return jdbcTransactionalFactory.create(FIND_ALL_QUERY, statement -> {
            statement.execute();
            Collection<Account> accounts = new ArrayList<>();
            try (ResultSet resultSet = statement.getResultSet()) {
                while (resultSet.next()) {
                    accounts.add(createAccount(resultSet));
                }
            }
            return accounts;
        }).execute();
    }

    @Override
    public Account save(Account account) {
        return jdbcTransactionalFactory.create(SAVE_QUERY, statement -> {
            statement.setObject(1, account.getId());
            statement.setBigDecimal(2, account.getAmount().getValue());
            statement.setInt(3, account.getVersion());
            statement.execute();
            return account;
        }).execute();
    }

    @Override
    public Account update(Account account) {
        return jdbcTransactionalFactory.create(UPDATE_QUERY, statement -> {
            statement.setBigDecimal(1, account.getAmount().getValue());
            statement.setObject(2, account.getId());
            statement.setInt(3, account.getVersion());

            if (statement.executeUpdate() == 0) {
                throw new OptimisticLockException(account.getId(), Account.class);
            }
            return Account
                    .builder()
                    .id(account.getId())
                    .amount(account.getAmount())
                    .version(account.getVersion() + 1)
                    .build();
        }).execute();
    }

    @Override
    public void delete(UUID uuid) {
        jdbcTransactionalFactory.create(DELETE_QUERY, statement -> {
            statement.setObject(1, uuid);
            statement.execute();
            return null;
        }).execute();

    }

    private Account createAccount(ResultSet resultSet) throws SQLException {
        return Account.builder()
                .id((UUID) resultSet.getObject("ID"))
                .amount(Amount.of(resultSet.getBigDecimal("BALANCE")))
                .version(resultSet.getInt("VERSION"))
                .build();
    }


}
