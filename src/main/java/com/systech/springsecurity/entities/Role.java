package com.systech.springsecurity.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "tb_roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//gerar o id de forma automatica
    @Column(name = "role_id")
    private Long roleId;

    private String name;


    public enum Values{
        ADMIN(1L),
        BASIC(2L);

        long roleId;

        Values(long roleId){
            this.roleId = roleId;
        }

//        public long getRoleId(){
//            return roleId;
//        }


    }
}
