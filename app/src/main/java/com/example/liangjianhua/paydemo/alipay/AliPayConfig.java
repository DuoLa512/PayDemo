package com.example.liangjianhua.paydemo.alipay;

/**
 * @author Ljh
 * @date 2018/7/26
 */
public class AliPayConfig {

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2016080200146881";

    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "";
    /** 支付宝账户登录授权业务：入参target_id值 */
    public static final String TARGET_ID = "";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
    public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDKxNYWhDeNip+gytUtJarHF0qBaBfb9g2H0W5ESG+exswLatLRTnj5zloVoZXaaJNqaH8uIs7Q3jrYM2MTG90SQ03HiRtzfErdT9D2GaS5teDrMwSwjgGFPdW0JpJ7mbtZi5GiJgBr9rjv4LTAhj5DtUgsolOADzhFkqfwZvrIRvIve4EYtVldU1leEIfSsvO97d8q34Vi94kcroI+3iQlNcp0Ruacig/95yqMzwZB9yLZLcpk4cUflARO03SR+XeTu1jMOc7cHjGveNt4gCNmbQhCbDcFnlhYXvUN+VCoL07Nm/1ayjlOmGyZmda8TAC+7DKs/IOpTbHKYLH5lscJAgMBAAECggEAcxYHYR5IHZj5gxy8o/RGGtv4WLyJFjpwcY8H48+tMtQoia5m24+LkFVzweVFlYJXcUTAMqJk+h24DnAUIotlDXGP2tnR79ky8KvVsWjamuFFOJlrZv4ilMqE2cwuxeJ6eJeD4CFriduuGdBTJxbKjkl9HoA/nmaFDM/xa7w3SKYhPDQquPac/g/uwArhfKpmZrSq4eFMXrtWcv0JTyu0QJkLXPsT2JpdkUCSu9E0mGUffpXcqSTOwYjv/KhnAYMKj5gSrzVJH3hL/IUkDthuY3IT8nD+NBGdAzHK9GMlIYuX9nkqbkApkInS9v5lMCSdF0o9MPi2scUjXQML8DfU0QKBgQDkWQ/mVIb5bttoA/Y6dwsZr404yNiyUUToeKdj20Ob4PJCpHcMYtt6CZ3RcoEdxDcqvIzNI2apEmTWXj64ty7p+CHIWSQtObCp6/iQk02unMSsI7qDEpWqs+sbvvpPuC1lebikDbpSWmC1Y3iErsageLnIa4o24AUOY81sY1poHQKBgQDjUs7MnuJADFTL2cH6cYkDCFIXAfEkwvPpWYfdBtSaHgDQYpuaeFm7pq5Wsrn8DarFxR5Hh322Nk0WEyelKhTjW0mao+hWO+SQIj+hLbEN1ixdtogE+VUa+4A6ovBD4G2KM053GTF2HwJKv392P/gtfZVNnDQl/pMI5kJigR2e3QKBgQC+eHPEwrkbkpa9OqDGv8mokUCRHydyBkweRZYKuzi363jokdsjn8pHD4Ju1L5Sn/sMUN55to/Jc6hdD5vt5A7j5q/3ODPu7XIpKsXZgqWHgwiiNeM7teLV0uHH8RNGL/Dd9hxH65OBYhXM973tnwPbkxgUz28outA84o7VXEJrGQKBgBQricvgISUbsv5vcOW+4zBQsa/27SWc4rWGyyZSb9TI6ruStEnNefDLhlaM/zGdKNTpe5lPLVlYkhhuVKs0A6x0siA1gOz93XbogwrTGg2nPkKGaqU+Vk3RmPwa7wSmyjFkAgdRRfE8KSDmwvGEFYo7xV6giIQChZtZivRgjndFAoGAGHmYkIV2wpmb3ZIvWIts9cPozKHW2m9M0ahcG5+bkkfSBz4LMePZ4oND+CntGs5n5EGnX2oPG5nQ8Yzhr87yul2MxN4MXekdIKLV+XWxmZi29VeZIdeqMTCldKrFaW0t8wmainC2/TuULHl7PXZCQkioyd2Fs6RU6/2kGd31A80=";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
}
