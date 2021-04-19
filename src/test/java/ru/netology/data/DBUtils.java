package ru.netology.data;

import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import ru.netology.data.mode.*;

import java.sql.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DBUtils {

    private static Connection conn;
    private static final String url = System.getProperty("db.url");
    private static final String user = "app";
    private static final String password = "pass";

    private static final String creditSQLQuery = "SELECT * FROM credit_request_entity WHERE created IN (SELECT max(created) " +
            "FROM credit_request_entity);";
    private static final String orderSQLQuery = "SELECT * FROM order_entity WHERE created IN (SELECT max(created) " +
            "FROM order_entity);";
    private static final String paymentSQLQuery = "SELECT * FROM payment_entity WHERE created IN (SELECT max(created) " +
            "FROM payment_entity);";

//    private static Connection getConnection() throws SQLException {
//            val conn = DriverManager.getConnection(url, user, password);
//        return conn;
//    }

    private static Connection getConnection() throws SQLException {
        if (conn == null)
            conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    public static void clearDBTables() throws SQLException {
        val runner = new QueryRunner();
        runner.update(getConnection(), "DELETE FROM credit_request_entity;");
        runner.update(getConnection(), "DELETE FROM payment_entity;");
        runner.update(getConnection(), "DELETE FROM order_entity;");
    }

//    public static void clearDBTables() throws SQLException {
//        val runner = new QueryRunner();
//        try (val conn = getConnection()
//        ) {
//            runner.update(conn, "DELETE  FROM credit_request_entity;");
//            runner.update(conn, "DELETE  FROM payment_entity;");
//            runner.update(conn, "DELETE  FROM order_entity;");
//        } catch (SQLException e) {
//            System.out.println(e.toString());
//        }
//    }

    public static void checkLastPaymentStatus(String status) throws SQLException {
        val runner = new QueryRunner();
        val paymentRow = runner.query(getConnection(), paymentSQLQuery, new BeanHandler<>(PaymentEntity.class));
        assertEquals(status, paymentRow.status, "Transaction status should be as");
    }

    public static void checkLastCreditStatus(String status) throws SQLException {
        val runner = new QueryRunner();
        val creditRow = runner.query(getConnection(), creditSQLQuery, new BeanHandler<>(CreditEntity.class));
        assertEquals(status, creditRow.status, "Credit status should be as");
    }

    public static void checkRowCreditNotNull() throws SQLException {
        val runner = new QueryRunner();
        val orderRow = runner.query(getConnection(), orderSQLQuery, new BeanHandler<>(OrderEntity.class));
        val creditRow = runner.query(getConnection(), creditSQLQuery, new BeanHandler<>(CreditEntity.class));
        assertNotNull(orderRow);
        assertNotNull(creditRow);
    }

    public static void checkRowPaymentNotNull() throws SQLException {
        val runner = new QueryRunner();
        val orderRow = runner.query(getConnection(), orderSQLQuery, new BeanHandler<>(OrderEntity.class));
        val paymentRow = runner.query(getConnection(), paymentSQLQuery, new BeanHandler<>(PaymentEntity.class));
        assertNotNull(orderRow);
        assertNotNull(paymentRow);
    }

    public static void compareExpectedAmountWithActual(int amount) throws SQLException {
        val runner = new QueryRunner();
        val paymentRow = runner.query(getConnection(), paymentSQLQuery, new BeanHandler<>(PaymentEntity.class));
        assertEquals(amount, paymentRow.amount, "Transaction amount should be as");
    }

    public static void compareIDsPaymentAndOrder() throws SQLException {
        val runner = new AtomicReference<>(new QueryRunner());
        val paymentRow = runner.get().query(getConnection(), paymentSQLQuery, new BeanHandler<>(PaymentEntity.class));
        val orderRow = runner.get().query(getConnection(), orderSQLQuery, new BeanHandler<>(OrderEntity.class));
        assertEquals(paymentRow.transaction_id, orderRow.payment_id, "Payment and Order IDs are not equal");
    }

    public static void compareIDsCreditAndOrder() throws SQLException {
        val runner = new QueryRunner();
        val creditRow = runner.query(getConnection(), creditSQLQuery, new BeanHandler<>(CreditEntity.class));
        val orderRow = runner.query(getConnection(), orderSQLQuery, new BeanHandler<>(OrderEntity.class));
        assertEquals(creditRow.bank_id, orderRow.payment_id, "Credit and Order IDs are not equal");
    }

}
