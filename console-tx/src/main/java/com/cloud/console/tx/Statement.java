package com.cloud.console.tx;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Frank on 2019-01-17.
 */
@Getter @Setter
public class Statement {
    private String statement;
    private Object params;
}
