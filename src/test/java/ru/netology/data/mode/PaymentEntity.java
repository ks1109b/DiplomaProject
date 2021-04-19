package ru.netology.data.mode;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {
    String id;
    public int amount;
    String created;
    public String status;
    public String transaction_id;
}
