
package com.example.projektsklep.model.entities.role;

import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.enums.AdminOrUser;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {
    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof Role role && getId() != null && getId().equals(role.getId());
    }

    @Override
    public int hashCode() { return Role.class.hashCode(); }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private AdminOrUser roleType;

    public static Role fromAdminOrUser(AdminOrUser adminOrUser) {
        return Role.builder().roleType(AdminOrUser.valueOf(adminOrUser.name())).build();
    }
    @ManyToMany
    private Set<User> users = new HashSet<>();


}


