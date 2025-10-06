package com.equipement.batch;

import com.equipement.entity.Cable;

import java.util.List;

public interface DBWriter1 {
    void write(List<? extends Cable> items) throws Exception;
}
