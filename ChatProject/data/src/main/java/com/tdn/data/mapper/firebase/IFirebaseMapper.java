package com.tdn.data.mapper.firebase;

public interface IFirebaseMapper<From,To,String> {
    To map(From from,String id);
}
