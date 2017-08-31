package com.tuankhai.travelassistants.webservice.parser;

import android.net.ParseException;

/**
 * Created by Khai on 31/08/2017.
 */

public interface IParser<TIn, TOut> {
    TOut parse(TIn data) throws ParseException;
}