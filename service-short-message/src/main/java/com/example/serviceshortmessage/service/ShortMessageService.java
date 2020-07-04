package com.example.serviceshortmessage.service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2020/7/2 14:39
 * description:
 */
public interface ShortMessageService {

    void sendShortMessageFromCode(String phoneNumbers, String code, String codeType);
}
