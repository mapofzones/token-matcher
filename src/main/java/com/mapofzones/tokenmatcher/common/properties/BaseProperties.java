package com.mapofzones.tokenmatcher.common.properties;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
public class BaseProperties {

    private Duration syncTime;
    private Integer threads;
    private String threadsNaming;

}
