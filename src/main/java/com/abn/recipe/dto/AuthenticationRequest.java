package com.abn.recipe.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest implements Serializable {
    private static final long serialVersionUID = -6986746375915710855L;
    
    @NotNull(message = "username must Not Be Null")
    @NotBlank(message = "username must Not Be Blank")
    private String username;
    @NotNull(message = "password must Not Be Null")
    @NotBlank(message = "password must Not Be Blank")
    private String password;
}
