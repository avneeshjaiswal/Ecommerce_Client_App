package com.example.oem.ecommerce.Model;

import java.util.List;

/**
 * Created by avneesh jaiswal on 15-Mar-18.
 */

public class MyResponse {
    public long multicast_id;
    public int success;
    public int failure;
    public int canonical_ids;
    public List<Result> results;
}
