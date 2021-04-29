package ru.netology.data;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import ru.netology.data.models.*;

import java.sql.*;

public class DBUtils {

    private static Connection conn;
    private static final String url = System.getProperty("db.url");
    private static final String user = System.getProperty("db.login");
    private static final String password = System.getProperty("db.pass");

    private static final String creditSQLQuery = "SELECT * FROM credit_request_entity WHERE created IN (SELECT max(created) " +
            "FROM credit_request_entity);";
    private static final String orderSQLQuery = "SELECT * FROM order_entity WHERE created IN (SELECT max(created) " +
            "FROM order_entity);";
    private static final String paymentSQLQuery = "SELECT * FROM payment_entity WHERE created IN (SELECT max(created) " +
            "FROM payment_entity);";

    @SneakyThrows
    private static Connection getConnection() {
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    @SneakyThrows
    public static void clearDBTables() {
        val runner = new QueryRunner();
        runner.update(getConnection(), "DELETE FROM credit_request_entity;");
        runner.update(getConnection(), "DELETE FROM payment_entity;");
        runner.update(getConnection(), "DELETE FROM order_entity;");
    }

    @SneakyThrows
    public static String getLastPaymentStatus() {
        val runner = new QueryRunner();
        try {
            conn = getConnection();
            return runner.query(getConnection(), paymentSQLQuery, new BeanHandler<>(PaymentEntity.class)).getStatus();
        } catch (NullPointerException e) {
            return "Empty last payment status";
        }
    }

    @SneakyThrows
    public static String getLastCreditStatus() {
        val runner = new QueryRunner();
        try {
            conn = getConnection();
            return runner.query(getConnection(), creditSQLQuery, new BeanHandler<>(CreditEntity.class)).getStatus();
        } catch (NullPointerException e) {
            return "Empty last credit status";
        }
    }

    @SneakyThrows
    public static int getAmountPayment() {
        val runner = new QueryRunner();
        try {
            conn = getConnection();
            return runner.query(getConnection(), paymentSQLQuery, new BeanHandler<>(PaymentEntity.class)).getAmount();
        } catch (NullPointerException e) {
            return Integer.parseInt("Empty amount payment");
        }
    }

    @SneakyThrows
    public static String getTransactionIdFromPayment() {
        val runner = new QueryRunner();
        try {
            conn = getConnection();
            return runner.query(getConnection(), paymentSQLQuery, new BeanHandler<>(PaymentEntity.class)).getTransaction_id();
        } catch (NullPointerException e) {
            return "Empty transaction ID";
        }

    }

    @SneakyThrows
    public static String getPaymentIdFromOrder() {
        val runner = new QueryRunner();
        try {
            conn = getConnection();
            return runner.query(getConnection(), orderSQLQuery, new BeanHandler<>(OrderEntity.class)).getPayment_id();
        } catch (NullPointerException e) {
            return "Empty payment ID";
        }
    }

    @SneakyThrows
    public static String getBankIdFromCredit() {
        val runner = new QueryRunner();
        try {
            conn = getConnection();
            return runner.query(getConnection(), paymentSQLQuery, new BeanHandler<>(CreditEntity.class)).getBank_id();
        } catch (NullPointerException e) {
            return "Empty bank ID";
        }
    }

    @SneakyThrows
    public static String getCreditIdFromOrder() {
        val runner = new QueryRunner();
        try {
            conn = getConnection();
            return runner.query(getConnection(), orderSQLQuery, new BeanHandler<>(OrderEntity.class)).getCredit_id();
        } catch (NullPointerException e) {
            return "Empty Credit ID";
        }
    }
}