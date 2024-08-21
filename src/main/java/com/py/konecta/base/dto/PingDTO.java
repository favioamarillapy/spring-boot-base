package com.py.konecta.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PingDTO {

    private String version;
    private Object fecha;
    private boolean bd;
}
