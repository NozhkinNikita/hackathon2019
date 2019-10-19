package com.hton.entities;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "location")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "users")
@ToString(exclude = "users")
public class LocationEntity implements BaseEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = UserEntity.class)
    @JoinTable(name = "user_location",
            joinColumns = {@JoinColumn(name = "locationId", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "userId", nullable = false, updatable = false)})
    @Fetch(value = FetchMode.SUBSELECT)
    private List<UserEntity> users;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = RouterEntity.class, mappedBy = "locationId")
    private List<RouterEntity> routers;

    @Override
    public List<String> getBaseFields() {
        return Arrays.asList("id", "name");
    }

    @Override
    public List<String> getJoinFields() {
        return Arrays.asList("users", "routers");
    }
}
