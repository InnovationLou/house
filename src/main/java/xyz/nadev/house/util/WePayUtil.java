package xyz.nadev.house.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import xyz.nadev.house.vo.ResultEntity;
import xyz.nadev.house.vo.TransferDto;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by mrruan on 2019/06/15.
 */
public class WePayUtil {

    private static Logger logger = LoggerFactory.getLogger(WePayUtil.class);

    /**
     * 获取随机字符串 (采用截取8位当前日期数  + 4位随机整数)
     * @return
     */
    public static String getNonceStr() {
        //获得当前日期
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String currTime = outFormat.format(now);
        //截取8位
        String strTime = currTime.substring(8, currTime.length());
        //得到4位随机整数
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < 4; i++) {
            num = num * 10;
        }
        num = (int)random * num;
        return strTime + num;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    /**
     * 这个工具用来将用户退款的请求参数拿来转换为map对象
     * @return 拼接后字符串
     */
    public static String transferRefundRequestToMap(TransferDto dto) {
        Map<String, String> dtoMap = new HashMap<>();
        dtoMap.put("appid",dto.getAppid());
        dtoMap.put("mch_id",dto.getMch_id());
        dtoMap.put("nonce_str",dto.getNonce_str());
        dtoMap.put("out_trade_no",dto.getOut_trade_no());
        dtoMap.put("total_fee",String.valueOf(dto.getTotal_fee()) );
        dtoMap.put("refund_fee",String.valueOf(dto.getRefund_fee()));
        dtoMap.put("out_refund_no",dto.getOut_refund_no());
        return createLinkString(dtoMap);
    }
    /**
     * 这个工具用来将平台打款的请求参数拿来转换为map对象
     * @return 拼接后字符串
     */
    public static String transferRequestToMap(TransferDto dto) {
        Map<String, String> dtoMap = new HashMap<>();
        dtoMap.put("mch_appid",dto.getAppid());
        dtoMap.put("mchid",dto.getMch_id());
        dtoMap.put("nonce_str",dto.getNonce_str());
        dtoMap.put("partner_trade_no",dto.getPartner_trade_no());
        dtoMap.put("openid",dto.getOpenid() );
        dtoMap.put("check_name",dto.getCheck_name());
        dtoMap.put("amount",String.valueOf(dto.getAmount()));
        dtoMap.put("desc",dto.getDesc());
        dtoMap.put("spbill_create_ip",dto.getSpbill_create_ip());
        return createLinkString(dtoMap);
    }

    /**
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                    || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    /**
     * MD5 加密，转为指定类型
     * @param text
     * @param key
     * @param input_charset
     * @return
     */
    public static String sign(String text, String key, String input_charset) {
        text = text + "&key=" + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }

    public static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            System.out.println("指定编码集后的"+content.getBytes(charset).length);
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    /**
     * 解析xml得到 prepay_id 预支付id
     * @param result
     * @return
     * @throws DocumentException
     */
    public static String getPayNo(String result) throws DocumentException {
        Map<String, String> map = new HashMap<>();
        InputStream in = new ByteArrayInputStream(result.getBytes());
        SAXReader read = new SAXReader();
        Document doc = read.read(in);
        //得到xml根元素
        Element root = doc.getRootElement();
        //遍历  得到根元素的所有子节点
        @SuppressWarnings("unchecked")
        List<Element> list =root.elements();
        for(Element element:list){
            //装进map
            map.put(element.getName(), element.getText());
        }
        //返回码
        String return_code = map.get("return_code");
        //返回信息
        String result_code = map.get("result_code");
        //预支付id
        String prepay_id = "";
        //return_code 和result_code 都为SUCCESS 的时候返回 预支付id
        if(return_code.equals("SUCCESS")&&result_code.equals("SUCCESS")){
            prepay_id = map.get("prepay_id");
        }
        return prepay_id;
    }
    /**
     * 解析 回调时的xml装进map 返回
     * @param result
     * @return
     * @throws DocumentException
     */
    public static Map<String, String> getNotifyUrl(String result) throws DocumentException{
        Map<String, String> map = new HashMap<String, String>();
        InputStream in = new ByteArrayInputStream(result.getBytes());
        SAXReader read = new SAXReader();
        Document doc = read.read(in);
        //得到xml根元素
        Element root = doc.getRootElement();
        //遍历  得到根元素的所有子节点
        @SuppressWarnings("unchecked")
        List<Element> list =root.elements();
        for(Element element:list){
            //装进map
            map.put(element.getName().toString(), element.getText().toString());
        }
        return map;
    }

    /**
     * 验证签名，判断是否是从微信发过来
     * 验证方法：接收微信服务器回调我们url的时候传递的xml中的参数 然后再次加密，看是否与传递过来的sign签名相同
     * @param map
     * @return
     */
    public static boolean verifyWeixinNotify(Map<String, String> map,String key) {
        //根据微信服务端传来的各项参数 进行再一次加密后  与传过来的 sign 签名对比
        String signWx = map.get("sign");
        map.remove("sign");//重新获得签名一定要去除sign
        String mapStr = createLinkString(map);
        System.out.println("回调获得的数据组成mapStr" + mapStr);
        String signOwn = sign(mapStr, key, "utf-8").toUpperCase();         //根据微信端参数进行加密的签名
        System.out.println("回调获得的数据进行重新加密认证得到sign:" + signOwn);
                       //微信端传过来的签名
        if(signOwn.equals(signWx)){
            //如果两个签名一致，验证成功
            return true;
        }
        return false;
    }

    /**
     *
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     */
    public static String httpRequest(String requestUrl,String requestMethod,String outputStr){
        // 创建SSLContext
        StringBuffer buffer=null;
        try{
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

            //往服务器端写内容
            if(null !=outputStr){
                OutputStream os=conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }
            // 读取服务器端返回的内容
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            buffer = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public enum SignType {
        MD5, HMACSHA256
    }

    /**
     * 作用：统一下单<br>
     * 场景：公共号支付、扫码支付、APP支付
     * @param reqData 向wxpay post的请求数据
     * @param connectTimeoutMs 连接超时时间，单位是毫秒
     * @param readTimeoutMs 读超时时间，单位是毫秒
     * @return API返回数据
     * @throws Exception
     */
    public Map<String, String> unifiedOrder(Map<String, String> reqData,String key,  int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

        String respXml = this.requestWithoutCert(url, reqData, connectTimeoutMs, readTimeoutMs);
        return this.processResponseXml(respXml,key);
    }

    /**
     * 处理 HTTPS API返回数据，转换成Map对象。return_code为SUCCESS时，验证签名。
     * @param xmlStr API返回的XML格式数据
     * @return Map类型数据
     * @throws Exception
     */
    public Map<String, String> processResponseXml(String xmlStr,String key) throws Exception {
        String RETURN_CODE = "return_code";
        String return_code;
        Map<String, String> respData = xmlToMap(xmlStr);
        if (respData.containsKey(RETURN_CODE)) {
            return_code = respData.get(RETURN_CODE);
        }
        else {
            throw new Exception(String.format("No `return_code` in XML: %s", xmlStr));
        }

        if (return_code.equals("FAIL")) {
            return respData;
        }
        else if (return_code.equals("SUCCESS")) {
            if (this.isResponseSignatureValid(respData,key)) {
                return respData;
            }
            else {
                throw new Exception(String.format("Invalid sign value in XML: %s", xmlStr));
            }
        }
        else {
            throw new Exception(String.format("return_code value %s is invalid in XML: %s", return_code, xmlStr));
        }
    }

    /**
     * 判断xml数据的sign是否有效，必须包含sign字段，否则返回false。
     *
     * @param reqData 向wxpay post的请求数据
     * @return 签名是否有效
     * @throws Exception
     */
    public boolean isResponseSignatureValid(Map<String, String> reqData,String key) throws Exception {
        // 返回数据的签名方式和请求中给定的签名方式是一致的
        return isSignatureValid(reqData, key, SignType.MD5);
    }

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。
     *
     * @param data Map类型数据
     * @param key API密钥
     * @param signType 签名方式
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValid(Map<String, String> data, String key, SignType signType) throws Exception {
        if (!data.containsKey("sign") ) {
            return false;
        }
        String sign = data.get("sign");
        return generateSignature(data, key, signType).equals(sign);
    }

    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data 待签名数据
     * @param key API密钥
     * @param signType 签名方式
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key, SignType signType) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals("sign")) {
                continue;
            }
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        sb.append("key=").append(key);
        System.out.println("拼接的结果为："+sb.toString());

        if (SignType.MD5.equals(signType)) {
            return MD5(sb.toString()).toUpperCase();
        }
        else if (SignType.HMACSHA256.equals(signType)) {
            return HMACSHA256(sb.toString(), key);
        }
        else {
            throw new Exception(String.format("Invalid sign_type: %s", signType));
        }
    }

    /**
     * 生成签名
     *
     * @param data 待签名数据
     * @param key API密钥
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key) throws Exception {
        return generateSignature(data, key, SignType.MD5);
    }

    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key: data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
        try {
            writer.close();
        }
        catch (Exception ex) {
        }
        return output;
    }

    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
        InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
        org.w3c.dom.Document doc = documentBuilder.parse(stream);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getDocumentElement().getChildNodes();
        for (int idx=0; idx<nodeList.getLength(); ++idx) {
            Node node = nodeList.item(idx);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                data.put(element.getNodeName(), element.getTextContent());
            }
        }
        try {
            stream.close();
        }
        catch (Exception ex) {

        }
        return data;
    }

    /**
     * 不需要证书的请求
     * @param strUrl String
     * @param reqData 向wxpay post的请求数据
     * @param connectTimeoutMs 超时时间，单位是毫秒
     * @param readTimeoutMs 超时时间，单位是毫秒
     * @return API返回数据
     * @throws Exception
     */
    public String requestWithoutCert(String strUrl, Map<String, String> reqData,
                                     int connectTimeoutMs, int readTimeoutMs) throws Exception {
        String UTF8 = "UTF-8";
        String reqBody = mapToXml(reqData);
        URL httpUrl = new URL(strUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setConnectTimeout(connectTimeoutMs);
        httpURLConnection.setReadTimeout(readTimeoutMs);
        httpURLConnection.connect();
        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write(reqBody.getBytes(UTF8));
        //获取内容
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, UTF8));
        final StringBuffer stringBuffer = new StringBuffer();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line).append("\n");
        }
        String resp = stringBuffer.toString();
        if (stringBuffer!=null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (inputStream!=null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (outputStream!=null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resp;
    }

    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String MD5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 生成 HMACSHA256
     * @param data 待处理数据
     * @param key 密钥
     * @return 加密结果
     * @throws Exception
     */
    public static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    //获取用户登录的IP工具
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.equals("") && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.equals("") && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    /**
     * 传入参数为out_trade_no用于用户退款   因为不同api请求参数命名不一样 必须修改才能用
     * @param out_trade_no
     * @param appkey
     * @param certPath
     * @param model
     * @return
     * @throws Exception
     */
    public static ResultEntity doRefundTransfers(String out_trade_no, String appkey, String certPath, TransferDto model) throws Exception {
        //appkey 和 certPath传入
        String APP_KEY = appkey;
        String CERT_PATH = certPath;

        try {
            //1.计算参数签名  先将transferDto 转换为map
            String paramStr = transferRefundRequestToMap(model);
            String mysign = paramStr + "&key=" + APP_KEY;
            String sign = MD5(mysign).toUpperCase();

            //2.封装请求参数
            StringBuilder reqXmlStr = new StringBuilder();
            reqXmlStr.append("<xml>");
            reqXmlStr.append("<appid>" + model.getAppid() + "</appid>");
            reqXmlStr.append("<mch_id>" + model.getMch_id() + "</mch_id>");
            reqXmlStr.append("<nonce_str>" + model.getNonce_str() + "</nonce_str>");
            reqXmlStr.append("<out_trade_no>" + model.getOut_trade_no() + "</out_trade_no>");
            reqXmlStr.append("<out_refund_no>" + model.getOut_refund_no() + "</out_refund_no>");
            reqXmlStr.append("<total_fee>" + model.getTotal_fee() + "</total_fee>");
            reqXmlStr.append("<refund_fee>" + model.getRefund_fee() + "</refund_fee>");
            reqXmlStr.append("<sign>" + sign + "</sign>");
            reqXmlStr.append("</xml>");
            logger.info("request xml = " + reqXmlStr);
            //使用https访问接口

            //3.加载证书请求接口
            String result = HttpRequestHandler.httpRequestPost(out_trade_no, reqXmlStr.toString(),
                    model, CERT_PATH);
            if(result.contains("<result_code><![CDATA[FAIL]]></result_code>")){
                logger.info(result);
                return new ResultEntity(false, "调用微信接口成功, 业务提交失败，详情请看: " + result);
            }else{
                logger.info(result);
                return new ResultEntity(true, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultEntity(false, e.getMessage());
        }
    }

    /**
     * 企业付款到个人零钱核心代码 该方法实现企业付款给个人
     * @param appkey 小程序的appKey
     * @param certPath 加密pem文件的位置
     * @param model 封装请求信息的对象
     * @param enpayuser 企业付款的微信api地址
     * @return
     */
    public static ResultEntity doPayTransfers(String enpayuser, String appkey, String certPath, TransferDto model) throws Exception {
        //appkey 和 certPath传入
        String APP_KEY = appkey;
        String CERT_PATH = certPath;

        try {
            //1.计算参数签名
            String paramStr = transferRequestToMap(model);
            String mysign = paramStr + "&key=" + APP_KEY;
            String sign = MD5(mysign).toUpperCase();

            //2.封装请求参数
            StringBuilder reqXmlStr = new StringBuilder();
            reqXmlStr.append("<xml>");
            reqXmlStr.append("<mchid>" + model.getMch_id() + "</mchid>");
            reqXmlStr.append("<mch_appid>" + model.getAppid() + "</mch_appid>");
            reqXmlStr.append("<nonce_str>" + model.getNonce_str() + "</nonce_str>");
            reqXmlStr.append("<check_name>" + model.getCheck_name() + "</check_name>");
            reqXmlStr.append("<openid>" + model.getOpenid() + "</openid>");
            reqXmlStr.append("<amount>" + model.getAmount() + "</amount>");
            reqXmlStr.append("<desc>" + model.getDesc() + "</desc>");
            reqXmlStr.append("<sign>" + sign + "</sign>");
            reqXmlStr.append("<partner_trade_no>" + model.getPartner_trade_no() + "</partner_trade_no>");
            reqXmlStr.append("<spbill_create_ip>" + model.getSpbill_create_ip() + "</spbill_create_ip>");
            reqXmlStr.append("</xml>");
            logger.info("request xml = " + reqXmlStr);
            //使用https访问接口
            //3.加载证书请求接口
            String result = HttpRequestHandler.httpRequestPost(enpayuser, reqXmlStr.toString(),
                    model, CERT_PATH);
            if(result.contains("CDATA[FAIL]")){
                logger.error(result);
                return new ResultEntity(false, "调用微信接口失败, 具体信息请查看访问日志: " + result);
            }else{
                logger.error(result);
                return new ResultEntity(true, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultEntity(false, e.getMessage());
        }
    }


    /**
     * 检查签名的工具
     * @param request
     * @return
     */
    public static Boolean checkSign(HttpServletRequest request,String openid) throws Exception {
        Boolean flag= false;
        String sign = request.getParameter("sign");//签名
        System.out.println("获得传入的sign为："+sign);

        //检查sigin是否过期
        Enumeration<?> pNames =  request.getParameterNames();
        Map<String, String> params = new HashMap<String, String>();
        while (pNames.hasMoreElements()) {
            String pName = (String) pNames.nextElement();
            if("sign".equals(pName)) continue;//sign 在生成签名前应该是不参与排序的,就不放进来了
            String pValue = (String)request.getParameter(pName);
            params.put(pName, pValue);
//            logger.info(pName+":"+pValue);
            System.out.println("pname:"+pName+"=pValue"+pValue);
        }
        String prestr = WePayUtil.createLinkString(params);
        System.out.println("排序后的签名字段：" + prestr);
        String mysign = WePayUtil.sign(prestr, openid, "utf-8").toUpperCase();
        System.out.println( mysign);
        if (sign.equals(mysign)){
            flag = true;
        }
//        System.out.println("生成的sign："+generateSignature(params, openid));
//        if(sign.equals(generateSignature(params, openid))){
//            flag = true;
//        }
        return flag;
    }



    public static final String NOTIFY_SUCCESS = "<xml>\n" +
            "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
            "  <return_msg><![CDATA[OK]]></return_msg>\n" +
            "</xml>";
    public static final String NOTIFY_FAIL_SERVER_ERROR = "<xml>\n" +
            "  <return_code><![CDATA[FAIL]]></return_code>\n" +
            "  <return_msg><![CDATA[internal error occured!]]></return_msg>\n" +
            "</xml>";
    public static final String NOTIFY_FAIL_IO_ERROR = "<xml>\n" +
            "  <return_code><![CDATA[FAIL]]></return_code>\n" +
            "  <return_msg><![CDATA[IO EXCEPTION,输入流有问题]]></return_msg>\n" +
            "</xml>";
    public static final String NOTIFY_FAIL_WRONG_RETURN_CODE = "<xml>\n" +
            "  <return_code><![CDATA[FAIL]]></return_code>\n" +
            "  <return_msg><![无效的或者错误的return_code]]></return_msg>\n" +
            "</xml>";
    public static final String NOTIFY_FAIL_UNKNOWN_DATA = "<xml>\n" +
            "  <return_code><![CDATA[FAIL]]></return_code>\n" +
            "  <return_msg><![无法验证数据的合法性，请重新尝试]]></return_msg>\n" +
            "</xml>";
    public static final String NOTIFY_FAIL_REPEAT_ERROR ="<xml>\n" +
            "  <return_code><![CDATA[FAIL]]></return_code>\n" +
            "  <return_msg><![该订单已经支付，请勿重复回调!]]></return_msg>\n" +
            "</xml>";

}
