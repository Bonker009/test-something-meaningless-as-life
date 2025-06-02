package com.kshrd.devconnect_springboot.  model.entity;
    
    

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Badges {
       private String badgeId;
       private String name;
       private String icon;
       private String description;
}
