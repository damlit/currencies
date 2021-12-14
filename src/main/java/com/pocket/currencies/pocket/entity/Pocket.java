package com.pocket.currencies.pocket.entity;

import com.pocket.currencies.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "pocket")
public class Pocket {

    @Id
    @GeneratedValue
    private long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;
    @OneToMany(mappedBy = "pocket", fetch = FetchType.LAZY)
    private List<Deposit> deposits;
}
