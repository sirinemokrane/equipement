package com.equipement.batch;

import com.equipement.entity.Cable;

import java.util.List;

public interface DBWriter2 {
    void write(List<? extends Cable> users) throws Exception;
}
