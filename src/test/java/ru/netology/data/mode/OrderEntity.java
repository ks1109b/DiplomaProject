package ru.netology.data.mode;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    String credit_id;
    public String payment_id;
    String id;
    String created;
}
