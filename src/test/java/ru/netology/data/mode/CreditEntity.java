package ru.netology.data.mode;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditEntity {
    String id;
    public String bank_id;
    String created;
    public String status;
}
