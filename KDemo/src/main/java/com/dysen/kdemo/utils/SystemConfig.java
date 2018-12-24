package com.dysen.kdemo.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SystemConfig implements Serializable {
    //法币
    public final static String LEHAL_USD = "USD";
    public final static String LEHAL_USDT = "USDT";
    public final static String LEHAL_CNY = "CNY";
    public final static String LEHAL_QC = "QC";
    public final static String OPTIONAL = "optional";
    public final static String SUCCESS = "1000";
    //初级实名认证
    public final static String Primary = "5003";
    //高级实名认证
    public final static String Senior = "5004";
    //高级实名认证
    public final static String Video = "5008";


    //中国比特币积分等级与费率标准
    public final static String getLevel() {
        String str = wwwUrl() + "i/document?item=11";
        return str;
    }

    private static String wwwUrl() {

        return "https://"+"www."+0+"/";
    }

    //用户协议
    public final static String getAgreement() {
        String str = wwwUrl() + "i/document?item=4";
        return str;
    }

    /**
     * 数据请求-注册
     * <p>
     * userType 用户名类型 1：手机号码，2：邮箱
     * userName 用户名
     * countryCode 区号（+86）中国
     * loginPwd 登录密码
     * msgCode 验证码
     * recommendCode 邀请码
     */

    public static List<String> getHttpRegister() {
        List<String> param = new ArrayList<String>();
        param.add("userType");
        param.add("userName");
        param.add("countryCode");
        param.add("loginPwd");
        param.add("msgCode");
        param.add("recommendCode");
        return param;
    }

    /**
     * 数据请求-用户登录
     * <p>
     * userName		   用户名/手机号/邮箱
     * password	       登录密码（RSA加密）
     * mobileCode	   短信验证码
     * googleCode      谷歌验证码
     * countryCode     国家码，如果有值时代表是手机登录
     */

    public static List<String> getHttpLogin() {
        List<String> param = new ArrayList<String>();
        param.add("userName");
        param.add("password");
        param.add("dynamicCode");
        param.add("googleCode");
        param.add("countryCode");
        return param;
    }

    /**
     * 数据请求-发送验证码
     * <p>
     * countryCode		   区号
     * encryptNumber	  RSA加密手机号
     * encryptEmail       RSA加密邮箱
     * type	              1:注册,2:找回密码 3 :其他 4:提现人民币 5:提 现比特币 6:提现莱特币
     * idCardNo
     */
    public static List<String> getHttpSendCode() {
        List<String> param = new ArrayList<String>();
        param.add("countryCode");
        param.add("encryptNumber");
        param.add("encryptEmail");
        param.add("type");
        return param;
    }

    /**
     * 数据请求-用户发送验证码
     * type	 1:注册,2:找回密码 3 :其他 4:提现人民币 5:提现比特币 6:提现莱特币 7:提现以太币 8:设置谷歌验证码 9:修改资金密 码 10:异地登录短信验证 15:异地登录短信验证
     */
    public static List<String> getHttpUserSendCode() {
        List<String> param = new ArrayList<String>();
        param.add("type");
        return param;
    }


    /**
     * 数据请求-找回密码
     * <p>
     * method       找回方式 1:手机 2:邮箱
     * userName     户名/手机号/邮箱
     * countryCode  区号,手机找回时必须传入
     * newPassword  登录密码（RSA加密）
     * msgCode      动态（RSA加密）
     * cardId	    实名认证的证件号，实名认证通过的才需要， 新增参数
     */

    public static List<String> getHttpChangePwd() {
        List<String> param = new ArrayList<String>();
        param.add("method");
        param.add("userName");
        param.add("countryCode");
        param.add("newPassword");
        param.add("msgCode");
        param.add("cardId");
        return param;
    }


    /**
     * 数据请求-修改资金密码
     * <p>
     * oldSafePwd	   旧资金密码
     * newPassword     登录密码
     * mobileCode	   短信验证码
     */

    public static List<String> getHttpResetSafePwd() {
        List<String> param = new ArrayList<String>();
        param.add("oldSafePwd");
        param.add("newSafePwd");
        param.add("mobileCode");
        return param;
    }

    /**
     * 数据请求-修改密码
     * type
     * oldPassword	   旧登录密码
     * newPassword     新登录密码
     * dynamicCode
     * googleCode
     */

    public static List<String> getHttpResetPwd() {
        List<String> param = new ArrayList<String>();
        param.add("type");
        param.add("oldPassword");
        param.add("newPassword");
        param.add("dynamicCode");
        param.add("googleCode");
        return param;
    }

    /**
     * 数据请求-设置谷歌验证
     * secret        密钥
     * type	         类型 1 设置/修改谷歌验证 0 关闭谷歌验证
     * mobileCode    短信验证码
     * googleCode    谷歌验证码
     */

    public static List<String> getHttpSetGoogleCode() {
        List<String> param = new ArrayList<String>();
        param.add("secret");
        param.add("type");
        param.add("dynamicCode");
        param.add("oldgCode");
        param.add("googleCode");
        return param;
    }

    /**
     * 开启/关闭Google登录/提现验证
     * operation     操作类型 0 关闭 1 开启
     * authType	     验证类型 1：登录验证2：提现验证
     * googleCode    谷歌验证码（RSA加密）关闭提现验证时需要谷歌验证码
     */

    public static List<String> getHttpChangeGoogleAuth() {
        List<String> param = new ArrayList<String>();
        param.add("operation");
        param.add("authType");
        param.add("googleCode");
        return param;
    }


    /**
     * 根据市场卖买行情数据
     * <p>
     * length	     数据长度,可传入 5,10,20,50
     * depth	     深度间距,0.1 0.3 0.5 1
     * currencyType	 货币类型
     * exchangeType  兑换货币类型
     */

    public static List<String> getHttpMarketDepth() {
        List<String> param = new ArrayList<String>();
        param.add("length");
        param.add("depth");
        param.add("currencyType");
        param.add("exchangeType");
        return param;
    }

    /**
     * 委托下单数据
     * <p>
     * timeStamp	 时间戳
     * type	         类型  1:买入 0:卖出
     * currencyType  货币类型
     * exchangeType  兑换货币类型
     * isPlan        1:计划/委托交易 0:立即交易
     * unitPrice     买入/卖出单价
     * number        买入/卖出数量
     * safePwd 资金密码 (RSA加密)
     */
    public static List<String> getHttpDoEntrust() {
        List<String> param = new ArrayList<String>();
        param.add("timeStamp");
        param.add("type");
        param.add("currencyType");
        param.add("exchangeType");
        param.add("isPlan");
        param.add("unitPrice");
        param.add("number");
        param.add("safePwd");
        param.add("acctype");
        return param;
    }

    /**
     * 委托下单数据
     * <p>
     * type	         类型  1:买入 0:卖出 -1:不限制
     * currencyType  货币类型
     * exchangeType  兑换货币类型
     * dayIn3        3天内数据 0:否 1:是 默认1
     * status        0不限制 1 已取消成功 2 交易成功 3 交易中(未完全成交)
     * pageIndex     页码
     * pageSize      每页显示数量
     */

    public static List<String> getHttpTransRecord(){
        List<String> param=new ArrayList<String>();
        param.add("recordType");
        param.add("is_lever");
        param.add("type");
        param.add("currencyType");
        param.add("exchangeType");
        param.add("dayIn3");
        param.add("status");
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }
    /**
     * 委托下单数据
     *
     * type	         类型  1:买入 0:卖出 -1:不限制
     * currencyType  货币类型
     * exchangeType  兑换货币类型
     * dayIn3        3天内数据 0:否 1:是 默认1
     * status        0不限制 1 已取消成功 2 交易成功 3 交易中(未完全成交)
     * pageIndex     页码
     * pageSize      每页显示数量
     *
     */
    public static List<String> getHttpEntrustRecord() {
        List<String> param = new ArrayList<String>();
        param.add("type");
        param.add("currencyType");
        param.add("exchangeType");
        param.add("dayIn3");
        param.add("status");
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    /**
     * 批量取消交易
     * <p>
     * type	         取消类型 1:买入 2:卖出 3:全部
     * currencyType  货币类型
     * exchangeType  兑换货币类型
     */
    public static List<String> getHttpCancelAllOrders() {
        List<String> param = new ArrayList<String>();
        param.add("type");
        param.add("currencyType");
        param.add("exchangeType");
        return param;
    }

    /**
     * 取消单笔交易
     * <p>
     * currencyType  货币类型
     * exchangeType  兑换货币类型
     * entrustId     交易id
     */
    public static List<String> getHttpCancelOrder() {
        List<String> param = new ArrayList<String>();
        param.add("currencyType");
        param.add("exchangeType");
        param.add("entrustId");
        return param;
    }

    /**
     * 根据市场卖买行情数据
     * <p>
     * currencyType	 货币类型
     * exchangeType  兑换货币类型
     * step
     * size
     */

    public static List<String> getHttpIndexMarketChart() {
        List<String> param = new ArrayList<String>();
        param.add("currencyType");
        param.add("exchangeType");
        param.add("step");
        param.add("size");
        return param;
    }

    public static List<String> getLastTrades() {
        List<String> param = new ArrayList<String>();
        param.add("marketName");
        param.add("lastId");
        return param;
    }

    /**
     * 修改提现地址备注
     * <p>
     * currencyType  货币类型
     * withdrawAddressId  提现地址ID
     * memo     备注
     */
    public static List<String> getHttpUpdateWithdrawAddressMemo() {
        List<String> param = new ArrayList<String>();
        param.add("currencyType");
        param.add("withdrawAddressId");
        param.add("memo");
        return param;
    }

    /**
     * 修改提现地址备注
     * <p>
     * currencyType 货币类型
     * cashAmount 提现金额
     * receiveAddress 接收地址
     * liuyan 留言
     * feeInfoId
     * isInnerTransfer
     * safePwd 资金密码 (RSA加密)
     * googleCode google验证码(RSA加密)
     * dynamicCode 动态(短信、邮件)验证码(RSA加密)
     */
    public static List<String> getHttpWithdraw() {
        List<String> param = new ArrayList<String>();
        param.add("currencyType");
        param.add("cashAmount");
        param.add("receiveAddress");
        param.add("liuyan");
        param.add("feeInfoId");
        param.add("isInnerTransfer");
        param.add("safePwd");
        param.add("googleCode");
        param.add("dynamicCode");
        return param;
    }

    /**
     * 查询提现账单
     * <p>
     * currencyType  货币类型
     * status  状态 -1:全部 0:打币中 1:失败 2:成功 3:已取消
     * pageIndex     页码
     * pageSize     每页显示数量
     */
    public static List<String> getHttpSearchWithdraw() {
        List<String> param = new ArrayList<String>();
        param.add("currencyType");
        param.add("status");
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    /**
     * 取消BTC/LTC/ETH提现
     * <p>
     * currencyType  货币类型
     * withdrawId    提现记录ID
     * safePwd       资金密码
     */
    public static List<String> getHttpCancelWithdraw() {
        List<String> param = new ArrayList<String>();
        param.add("currencyType");
        param.add("withdrawId");
        param.add("safePwd");
        return param;
    }

    /**
     * 获取免息券
     * <p>
     * currencyType  货币类型
     * status    状态值 用”|”隔开
     * pageIndex     页码
     * pageSize     每页显示数量
     */
    public static List<String> getHttpInterestFree() {
        List<String> param = new ArrayList<String>();
        param.add("currencyType");
        param.add("status");
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    /**
     * 兑换免息券
     * <p>
     * type            兑换类型 1密钥兑换 2 积分兑换
     * secret          密钥
     * pointNumber     欲兑换积分
     * safePwd         资金密码
     */
    public static List<String> getHttpExchangeInterestFree() {
        List<String> param = new ArrayList<String>();
        param.add("type");
        param.add("secret");
        param.add("pointNumber");
        param.add("safePwd");
        return param;
    }

    /**
     * 融资融币­借款
     * <p>
     * currencyType    货币类型
     * amount          借入金额
     * ticketId        免息卷id
     * safePwd         资金密码(RSA加密)
     */
    public static List<String> getHttpDoLoan() {
        List<String> param = new ArrayList<String>();
        param.add("currencyType");
        param.add("amount");
        param.add("ticketId");
        param.add("safePwd");
        return param;
    }

    /**
     * 融资融币­借款记录
     * <p>
     * currencyType  货币类型
     * status        借款状态 1还款中 2已还款 3需要平仓 4平仓还款(不传时 表 示查询全部)
     * isIn          区分借入/借出 1 借入记录 0 借出记录
     * pageIndex     页码
     * pageSize      每页显示数量
     */
    public static List<String> getHttpLoanRecords() {
        List<String> param = new ArrayList<String>();
        param.add("currencyType");
        param.add("status");
        param.add("isIn");
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    /**
     * 融资融币­一键还款
     * <p>
     * loanRecords   json数组[{“id”:”10”,”repayAmount”:”30.0”,”isPart”:1} ,]
     * 说明:id: 借款记录id repayAmount: 本次还款本金 isPart: 1部分还款 0 全部还款
     * safePwd      资金密码(RSA加密)
     */
    public static List<String> getHttpFastRepay() {
        List<String> param = new ArrayList<String>();
        param.add("loanRecords");
        param.add("safePwd");
        return param;
    }

    /**
     * 融资融币­还款记录
     * <p>
     * loanRecordId  借款记录id
     * pageIndex     页码
     * pageSize      每页显示数量
     */
    public static List<String> getHttpRepayRecords() {
        List<String> param = new ArrayList<String>();
        param.add("loanRecordId");
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    /**
     * 初级实名认证
     * <p>
     * realName        真实姓名
     * cardId          证件号
     * country         证件所属国家
     */
    public static List<String> getHttpSimpleIdentityAuth() {
        List<String> param = new ArrayList<String>();
        param.add("realName");
        param.add("cardId");
        param.add("country");
        return param;
    }

    /**
     * 高级实名认证
     * <p>
     * frontalImg      身份证正面照
     * backImg         身份证背面照
     * loadImg         手持身份证照
     * type            操作类型 1：保存2：提交审核
     */
    public static List<String> getHttpHighIdentityAuth() {
        List<String> param = new ArrayList<String>();
        param.add("frontalImg");
        param.add("backImg");
        param.add("loadImg");
        param.add("type");
        return param;
    }

    /**
     * 提交实名认证(海外英文版)
     * <p>
     * frontalImg      正面照
     * loadImg         手持照
     * proofAddressImg  住址证明照
     * country
     * countryCode
     * firstName
     * lastName
     * doucumentNumber
     * streetAddress
     * city
     * postalCode
     * type       操作类型 1：保存2：提交审核
     */
    public static List<String> getHttpOverseasAuth() {
        List<String> param = new ArrayList<String>();
        param.add("frontalImg");
        param.add("loadImg");
        param.add("proofAddressImg");
        param.add("country");
        param.add("countryCode");
        param.add("firstName");
        param.add("lastName");
        param.add("doucumentNumber");
        param.add("streetAddress");
        param.add("city");
        param.add("postalCode");
        param.add("type");
        return param;
    }

    /**
     * 高级实名认证
     * <p>
     * frontalImg      身份证正面照
     * backImg         身份证背面照
     * loadImg         手持身份证照
     * proofAddressImg 住址证明照
     * bankCardId      银行卡号
     * bankTel         银行预留手机号
     * bankCardType    银行id
     * realName        真实姓名
     * cardId          证件号
     * country         证件所属国家
     * type            操作类型 1：保存2：提交审核
     */
    public static List<String> getHttpDepthIdentityAuth() {
        List<String> param = new ArrayList<String>();
        param.add("frontalImg");
        param.add("backImg");
        param.add("loadImg");
        param.add("proofAddressImg");
        param.add("bankCardId");
        param.add("bankTel");
        param.add("bankId");
        param.add("realName");
        param.add("cardId");
        param.add("country");
        param.add("type");
        return param;
    }


    /**
     * 上传图像
     * file_upload_stat	Integer	是	=1
     * plan_task_name	Integer	是	=goods
     * userId	Integer	是	用户id
     * userType	Integer	是	=1
     * savePicSize	Boolean	是	=false
     * auth	Boolean	是	=true
     * _fma.pu._0.ima	是		图片数据
     * rs	Integer	是	手机端=1
     */
    public static List<String> updateImage() {
        List<String> param = new ArrayList<String>();
        param.add("file_upload_stat");
        param.add("plan_task_name");
        param.add("userId");
        param.add("userType");
        param.add("savePicSize");
        param.add("auth");
        param.add("_fma.pu._0.ima");
        param.add("rs");
        return param;
    }

    /**
     * 查询账单
     * <p>
     * dataType 0:30天内数据 1:30天前数据
     * currencyType
     * pageIndex 页码 从1开始
     * pageSize 每页显示数量
     */
    public static List<String> getHttpSearchBill() {
        List<String> param = new ArrayList<String>();
        param.add("currencyType");
        param.add("dataType");
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    /**
     * 提交银行汇款单
     * <p>
     * type 方式 1:线下汇款 2:支付宝
     * rechargeBankId 付款银行静态ID(1=支付宝)
     * rechargeAmount 充值金额
     * realName 用户账户真实姓名
     * cardNumber 用户账户卡号(支付宝账号)
     * bankReId 用户账户的id(支付宝账户id)
     * remark 备注
     */
    public static List<String> getHttpSubmitRecharge() {
        List<String> param = new ArrayList<String>();
        param.add("type");
        param.add("rechargeBankId");
        param.add("rechargeAmount");
        param.add("realName");
        param.add("cardNumber");
        param.add("bankReId");
        param.add("remark");
        return param;
    }

    /**
     * 人民币充值/提现记录
     * <p>
     * type 1：充值 2：提现
     * status 状态值 0 充值成功 1 充值失败 2 处理中 3 提现中，等待处理 4 提现成功 5 提现失败 6 等待确认 8 已取消 9 等待汇款/确认 10 等待实名认证 其他状态值显示“–” , 获取全部记录时不传此参数
     * pageIndex 页码 从1开始
     * pageSize
     */
    public static List<String> getHttpRechargeList() {
        List<String> param = new ArrayList<String>();
        param.add("type");
        param.add("status");
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    /**
     * RMB提现操作
     * <p>
     * bankRid         历史提现银行卡id（新银行卡不传）
     * bankStaticId    银行静态id
     * province        所属省编号
     * city            所属市编号
     * branchName
     * isDefault
     * acountId        银行卡号
     * realName	       真实姓名
     * withdrawAmount  提现金额
     * safePwd         资金密码 （RSA加密）
     * dynamicCode     动态（短信、邮件）验证码（RSA加密）
     * googleCode      google验证码（RSA加密）
     */
    public static List<String> getHttpRmbWithdraw() {
        List<String> param = new ArrayList<String>();
        param.add("bankRid");
        param.add("bankStaticId");
        param.add("province");
        param.add("city");
        param.add("branchName");
        param.add("isDefault");
        param.add("acountId");
        param.add("realName");
        param.add("withdrawAmount");
        param.add("safePwd");
        param.add("dynamicCode");
        param.add("googleCode");
        return param;
    }

    /**
     * 添加银行卡
     * <p>
     * account	 银行卡账号
     * realName	 真实名字
     * bankId	 银行卡id
     * iprovince 银行卡开卡省
     * city      银行卡开卡城市
     * branchName 支行名称
     * isDefault  是否默认卡
     */
    public static List<String> getHttpAddBankCard() {
        List<String> param = new ArrayList<String>();
        param.add("account");
        param.add("realName");
        param.add("bankId");
        param.add("province");
        param.add("city");
        param.add("branchName");
        param.add("isDefault");
        return param;
    }

    /**
     * RMB提现操作
     * <p>
     * type            验证策略类型
     * category        验证分类  （1. 登录 2. 交易 3. 提现）
     * safePwd         资金密码 （RSA加密）
     * dynamicCode     动态（短信、邮件）验证码（RSA加密）
     * googleCode      google验证码（RSA加密）
     */
    public static List<String> getHttpChangeAuth() {
        List<String> param = new ArrayList<String>();
        param.add("type");
        param.add("category");
        param.add("safePwd");
        param.add("dynamicCode");
        param.add("googleCode");
        return param;
    }

    /**
     * 新增计划委托
     * <p>
     * type
     * currencyType
     * exchangeType
     * planAmount
     * triggerHighPrice
     * triggerLowPrice
     * planHighPrice
     * planLowPrice
     * safePwd
     */
    public static List<String> getHttpDoPlanEntrust() {
        List<String> param = new ArrayList<String>();
        param.add("type");
        param.add("currencyType");
        param.add("exchangeType");
        param.add("planAmount");
        param.add("triggerHighPrice");
        param.add("triggerLowPrice");
        param.add("planHighPrice");
        param.add("planLowPrice");
        param.add("safePwd");
        return param;
    }

    /**
     * 页查询吐槽反馈列表
     * <p>
     * timestamp 	时间戳，用于查询该时间之前的数据，防止分页加载数据错乱，每次分页查询都传人相同的时间即可
     * pageIndex    页码 从1开始
     * pageSize
     */
    public static List<String> getHttpQueryUserOpinion() {
        List<String> param = new ArrayList<String>();
        param.add("timestamp");
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    /**
     * 页查询吐槽反馈列表
     * <p>
     * marketId	 交易区ID
     * amount 数量
     * isBuy
     */
    public static List<String> getHttpC2cExchange() {
        List<String> param = new ArrayList<String>();
        param.add("marketId");
        param.add("amount");
        param.add("isBuy");
        return param;
    }

    public static List<String> getHttpC2cRecord() {
        List<String> param = new ArrayList<String>();
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    public static List<String> getHttpLeverDoTransfer() {
        List<String> param = new ArrayList<String>();
        param.add("amount");
        param.add("marketName");
        param.add("coin");
        return param;
    }

    //借入
    public static List<String> getHttpLeverDoLoan() {
        List<String> param = new ArrayList<String>();
        param.add("coin");
        param.add("marketName");
        param.add("amount");
        param.add("interestRateOfDay");
        param.add("repaymentDay");
        param.add("isLoop");//是否到期自动续借（1自动续借 0自动还款）
        param.add("safePwd");
        return param;
    }

    //借出
    public static List<String> getHttpLeverDoOut() {
        List<String> param = new ArrayList<String>();
        param.add("rate");//利率
        param.add("amount");//借出金额
        param.add("safePwd");//资金密码
        param.add("loanCoin");//币种
        return param;
    }

    //还款
    public static List<String> getHttpLeverDoRepay() {
        List<String> param = new ArrayList<String>();
        param.add("id");//借贷ID
        param.add("repayType");//还款方式 （0全部 1部分）
        param.add("repayAmount");//还款金额
        return param;
    }

    public static List<String> getHttpLoanList() {
        List<String> param = new ArrayList<String>();
        param.add("isIn");
        param.add("coin");
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    public static List<String> getHttpLoanRecordList() {
        List<String> param = new ArrayList<String>();
        param.add("loanId");//借出就传 借入不用传
        param.add("marketName");//币种
        param.add("status");//状态
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    public static List<String> getHttpRepayList() {
        List<String> param = new ArrayList<String>();
        param.add("loanId");
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    public static List<String> getHttpLoanBill() {
        List<String> param = new ArrayList<String>();
        param.add("marketName");
        param.add("dataType");//0：30天内数据 1：30天前数据
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    //理财挂单
    public static List<String> getHttpFinancingDoOut() {
        List<String> param = new ArrayList<String>();
        param.add("loanCoin");//币种
        param.add("amount");//金额
        param.add("rate");//利率
        param.add("day");//放款天数
        param.add("isLoop");//是否自动放贷
        param.add("safePwd");//资金密码
        return param;
    }

    //买入/卖出币
    public static List<String> getHttpDoOtcOrder() {
        List<String> param = new ArrayList<String>();
        param.add("isBuy");//是否买入（1:买入,0:卖出）
        param.add("adId");//广告ID
        param.add("price");//市场
        param.add("amount");
        return param;
    }

    public static List<String> getHttpOtcOrder() {
        List<String> param = new ArrayList<String>();
        param.add("status");//状态（0:未支付,1:已付款,2:已完成,3:已取消,4:申诉中,5:等待接单,6:拒绝接单,7:接单超时）
        param.add("isMerchant");//是否商家订单（0:普通用户订单,1:商家订单）
        param.add("marketName");//市场
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    public static List<String> getHttpPublishAd() {
        List<String> param = new ArrayList<String>();
        param.add("isBuy");//是否买入（1:买入,0:卖出）
        param.add("marketName");//OTC市场
        param.add("price");//价格
        param.add("amount");//数量
        param.add("minMoney");//单笔最小成交额
        param.add("maxMoney");//单笔最大成交额
        param.add("remark");//备注
        param.add("payWay");//支付方式
        param.add("floatPer");//浮动比例，向上浮动5%传1.05，向下浮动5%传0.95，不浮动传1
        param.add("minPrice");//最低价格，卖出时赋值
        param.add("maxPrcie");//最高价格，买入时赋值
        param.add("safePwd");//资金安全密码
        return param;
    }

    public static List<String> getHttpEditAd() {
        List<String> param = new ArrayList<String>();
        param.add("id");//广告id
        param.add("isBuy");//是否买入（1:买入,0:卖出）
        param.add("marketName");//OTC市场
        param.add("price");//价格
        param.add("amount");//数量
        param.add("minMoney");//单笔最小成交额
        param.add("maxMoney");//单笔最大成交额
        param.add("remark");//备注
        param.add("payWay");//支付方式
        param.add("floatPer");//浮动比例，向上浮动5%传1.05，向下浮动5%传0.95，不浮动传1
        param.add("minPrice");//最低价格，卖出时赋值
        param.add("maxPrcie");//最高价格，买入时赋值
        param.add("safePwd");//资金安全密码
        return param;
    }

    public static List<String> getHttpApplyAppeal() {
        List<String> param = new ArrayList<String>();
        param.add("orderId");//订单ID
        param.add("appealType");//申诉类型
        param.add("content");//申诉内容
        param.add("pic");//图片证据
        return param;
    }

    public static List<String> getHttpSubmitAppealDetail() {
        List<String> param = new ArrayList<String>();
        param.add("id");//申诉ID
        param.add("content");//申诉内容
        param.add("pic");//图片证据
        return param;
    }

    public static List<String> getHttpAdList() {
        List<String> param = new ArrayList<String>();
        param.add("type");
        param.add("marketName");
        param.add("numSort");
        param.add("priceSort");
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    public static List<String> getHttpOnlineAdList() {
        List<String> param = new ArrayList<String>();
        param.add("type");//类型（1:买入,2:卖出）
        param.add("numSort");//按数量排序（1:降序,2:升序）
        param.add("priceSort");//按价格排序（1:降序,2:升序）
        param.add("marketName");//市场
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    public static List<String> getHttpPublishAdList() {
        List<String> param = new ArrayList<String>();
        param.add("type");//广告类型（1:买入,2:卖出）
        param.add("status");//状态（1:上架,2:下架,3:已完成）
        param.add("merchantUserId");//商家用户id（不为空则查询商家发布的广告，为空则查询当前用户发布的广告）
        param.add("marketName");//市场
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }

    public static List<String> getHttpOtcContact() {
        List<String> param = new ArrayList<String>();
        param.add("phone");//手机号码
        param.add("email");//邮箱
        param.add("qq");
        param.add("alipay");//支付宝
        param.add("wechat");//微信
        param.add("whatsApp");
        param.add("telegram");
        param.add("skype");
        param.add("other");
        return param;
    }

    public static List<String> getHttpOtcAccount() {
        List<String> param = new ArrayList<String>();
        param.add("bankRid");
        param.add("type");
        param.add("realName");//真实姓名
        param.add("bankId");//银行id
        param.add("bankName");//银行名称
        param.add("account");//银行帐号
        param.add("address");//开户地址
        param.add("swftCode");
        param.add("payQRCodeUrl");//二维码链接
        param.add("signleAddress");//个人地址
        param.add("note");//个人地址
        param.add("safePwd");
        return param;
    }

    //获取币种资料
    public static List<String> getCoinData() {
        List<String> param = new ArrayList<String>();
        param.add("coinName");
        return param;
    }

    public static int getRandomMoney() {
        Random random = new Random();
        return random.nextInt(89) + 11;
    }

    public static String deFormat(String str, int type) {
        try {
            BigDecimal bigDecimal = new BigDecimal(str);
            String str_ws = "";
            if (type >= 0) {
                String str_ws_ls = "0.#";
                for (int n = 1; type > 1 && n < type; n++) {
                    str_ws_ls = str_ws_ls + "#";
                }
                str_ws = str_ws_ls;
            } else {
                String str_ws_ls = "0.00";
                type = type * -1;
                for (int n = 2; type > 2 && n < type; n++) {
                    str_ws_ls = str_ws_ls + "#";
                }
                str_ws = str_ws_ls;
            }
            DecimalFormat df_ls = new DecimalFormat(str_ws);
            str = df_ls.format(bigDecimal.setScale(type, BigDecimal.ROUND_FLOOR).doubleValue());
        } catch (Exception e) {
            str = "0.00";
        }
        return str;
    }

    public static Double toDouble(String str) {
        double doub = 0.00;
        try {
            BigDecimal bigDecimal = new BigDecimal(str);
            doub = bigDecimal.doubleValue();
        } catch (Exception e) {
            doub = 0.00;
        }
        return doub;
    }

    public static BigDecimal toBigDecimal(String str) {
        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal(str);

        } catch (Exception e) {
            bigDecimal = new BigDecimal("0");
        }
        return bigDecimal;
    }

    /**
     * 资金流向=================================================
     * 开始
     */
    public static List<String> getKline() {
        List<String> param = new ArrayList<String>();
        param.add("marketName");
        param.add("timeRange");
        return param;
    }

    public static List<String> getFundDistribute() {
        List<String> param = new ArrayList<String>();
        param.add("marketName");
        return param;
    }

    public static List<String> getBigRecord() {
        List<String> param = new ArrayList<String>();
        param.add("marketName");
        param.add("pageNo");
        param.add("pageSize");
        param.add("lastTime");
        return param;
    }

    public static List<String> getHistoryFunds() {
        List<String> param = new ArrayList<String>();
        param.add("marketName");
        return param;
    }

    public static List<String> getNetfunds() {
        List<String> param = new ArrayList<String>();
        param.add("marketName");
        return param;
    }
    /**
     * 资金流向=================================================
     * 结束
     */

    //获取最新的中币新闻
    public static List<String> getLatestZbNews() {
        List<String> param = new ArrayList<String>();
        param.add("token");
        param.add("userId");
        param.add("pageIndex");
        param.add("pageSize");
        return param;
    }
    //发布帖子
    public static List<String> getdoPost() {
        List<String> param = new ArrayList<String>();
        param.add("coinName");
        param.add("content");
        param.add("pictureUrl");
        param.add("userId");
        param.add("token");
        return param;
    }

    //获取帖子列表
    public static List<String> getPostList() {
        List<String> param = new ArrayList<String>();
        param.add("coinName");
        param.add("type");
        param.add("postId");
        param.add("userId");
        param.add("token");
        return param;
    }
    // 发表评论
    public static List<String> getdoComment() {
        List<String> param = new ArrayList<String>();
        param.add("postId");        //帖子id
        param.add("coinName");      //币种区 btc
        param.add("content");       //评论内容
        param.add("replyId");       //回复对象用户id
        param.add("replyNickName"); //回复对象用户昵称
        param.add("userId");        //用户id
        param.add("token");         //登录token
        return param;
    }
    // 取消评论
    public static List<String> getcancelComment() {
        List<String> param = new ArrayList<String>();
        param.add("postId");        //帖子id
        param.add("coinName");      //币种区 btc
        param.add("commentId");     //用户评论id
        param.add("userId");        //用户id
        param.add("token");         //登录token
        return param;
    }
    // 点赞
    public static List<String> getdoPraise() {
        List<String> param = new ArrayList<String>();
        param.add("postId");        //帖子id
        param.add("userId");        //用户id
        param.add("token");         //登录token
        return param;
    }
    // 取消点赞
    public static List<String> getcancelPraise() {
        List<String> param = new ArrayList<String>();
        param.add("postId");        //帖子id
        param.add("userId");        //用户id
        param.add("token");         //登录token
        return param;
    }
}
