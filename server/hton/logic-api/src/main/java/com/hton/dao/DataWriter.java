package com.hton.dao;

import com.hton.dao.exceptions.DataWriterException;

public interface DataWriter {
    void store(Object var1) throws DataWriterException;

    void edit(Object var1) throws DataWriterException;

    void remove(Object var1) throws DataWriterException;
}
