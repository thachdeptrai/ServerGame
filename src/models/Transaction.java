/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package models;

import DBConnect.NNTDB;
import java.io.IOException;
import java.sql.Timestamp;
import lombok.Data;
import network.Message;
import player.Player;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author kagam
 */
//@Data
@Getter
@Setter
public class Transaction extends Player {

    private int id;
    public String playerName;
    public int amount;
    public String bankName;
    public String accountName;
    public String accountNumber;
    public String description;
    public String qrLink;
    private boolean statusReceive; // Trạng thái nhận tiền
    private boolean statusDeposit; // Trạng thái nạp
    private Timestamp createdAt;
    private static Transaction instance;

    public static Transaction gI() {
        if (instance == null) {
            instance = new Transaction();
        }
        return instance;
    }

    public Transaction(String playerName, int amount, String bankName, String accountName, String accountNumber, String description, String qrLink, boolean statusReceive, boolean statusDeposit, Timestamp createdAt) {
        this.playerName = playerName;
        this.amount = amount;
        this.bankName = bankName;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.description = description;
        this.qrLink = qrLink;
        this.statusReceive = statusReceive;
        this.statusDeposit = statusDeposit;
        this.createdAt = createdAt;
    }

    public Transaction(int amount, String qrLink, String nganHangName, String chuTK, String stk, String NoiDung) {
        this.amount = amount;
        this.qrLink = qrLink;
        this.bankName = nganHangName;
        this.accountName = chuTK;
        this.accountNumber = stk;
        this.description = NoiDung;
    }

    public Transaction(int id, int amount, String qrLink, String nganHangName, String chuTK, String stk, String NoiDung, Timestamp Ngaytao, String nguoitao) {
        this.id = id;
        this.amount = amount;
        this.qrLink = qrLink;
        this.bankName = nganHangName;
        this.accountName = chuTK;
        this.accountNumber = stk;
        this.description = NoiDung;
        this.createdAt = Ngaytao;
        this.playerName = nguoitao;
    }

    public Transaction() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQrLink() {
        return qrLink;
    }

    public void setQrLink(String qrLink) {
        this.qrLink = qrLink;
    }

    public boolean isStatusReceive() {
        return statusReceive;
    }

    public void setStatusReceive(boolean statusReceive) {
        this.statusReceive = statusReceive;
    }

    public boolean isStatusDeposit() {
        return statusDeposit;
    }

    public void setStatusDeposit(boolean statusDeposit) {
        this.statusDeposit = statusDeposit;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

}
