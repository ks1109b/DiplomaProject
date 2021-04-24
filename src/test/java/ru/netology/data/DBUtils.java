package ru.netology.data;

import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import ru.netology.data.mode.*;

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

    public static String getLastPaymentStatus() throws SQLException {
        val runner = new QueryRunner();
        return runner.query(getConnection(), paymentSQLQuery, new BeanHandler<>(PaymentEntity.class)).status;
    }

    public static String getLastCreditStatus() throws SQLException {
        val runner = new QueryRunner();
        return runner.query(getConnection(), creditSQLQuery, new BeanHandler<>(CreditEntity.class)).status;
    }

    public static int getAmountPayment() throws SQLException {
        val runner = new QueryRunner();
        return runner.query(getConnection(), paymentSQLQuery, new BeanHandler<>(PaymentEntity.class)).amount;
    }

    public static String getTransactionIdFromPayment() throws SQLException {
        val runner = new QueryRunner();
        return runner.query(getConnection(), paymentSQLQuery, new BeanHandler<>(PaymentEntity.class)).getTransaction_id();
    }

    public static String getPaymentIdFromOrder() throws SQLException {
        val runner = new QueryRunner();
        return runner.query(getConnection(), orderSQLQuery, new BeanHandler<>(OrderEntity.class)).getPayment_id();
    }

    public static String getBankIdFromCredit() throws SQLException {
        val runner = new QueryRunner();
        return runner.query(getConnection(), paymentSQLQuery, new BeanHandler<>(CreditEntity.class)).getBank_id();
    }

    public static String getCreditIdFromOrder() throws SQLException {
        val runner = new QueryRunner();
        return runner.query(getConnection(), orderSQLQuery, new BeanHandler<>(OrderEntity.class)).getCredit_id();
    }
}