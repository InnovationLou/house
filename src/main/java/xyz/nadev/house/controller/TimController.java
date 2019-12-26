package xyz.nadev.house.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.nadev.house.service.ImMsgService;
import xyz.nadev.house.service.TimService;
import xyz.nadev.house.vo.ResponseVO;

import java.util.Date;

/**
 *  功能：
 *  1. 发送消息(product/message)
 *  2. 接收消息(轮询方式)(接收未读消息)
 *  3. 查询历史消息(传入历史消息的时间段,按照天来算)
 *  4. 查询历史聊天者(每个历史聊天者都表示一个好友,聊了天就有记录,有记录就是由关联,有关联就是好友)
 */
@Slf4j
@RestController
@RequestMapping("/tim")
@CrossOrigin
public class TimController {

    @Autowired
    TimService timService;

    @Autowired
    ImMsgService imMsgService;

    @ApiOperation("返回用户UserSig")
    @GetMapping("/sig")
    public ResponseVO genSig(String userId) {
        return timService.genUserSig(userId);
    }

    @ApiOperation(value = "发送一条信息给某某人@param Authorization 头信息，获取发送者的信息\n" +
            "     * @param type 消息类型, 默认为message, 即普通消息, 前台可以指定 product, 此时必须传入 houseId\n" +
            "     * @param msg 消息内容,纯文本\n" +
            "     * @param receiverId 消息发送给谁, 不可为空\n" +
            "     * @param houseId type为'product'时才传入,否则不传入\n" +
            "     * @return 消息发送成功与否")
    @PutMapping("/msg")
    public ResponseVO sendMsg(@RequestHeader String Authorization
            , @RequestParam(required = false) String type
            , @RequestParam String msg
            , @RequestParam Integer receiverId
            , @RequestParam(required = false) Integer houseId){
        return imMsgService.sendMsg(Authorization, type, msg, receiverId, houseId);
    }

    @ApiOperation(value = "接收某个人发送过来的未读消息,这个接口只接收未读消息, 接收后就变成已读状态")
    @GetMapping("/msg/{senderId}")
    public ResponseVO getFreshMsg(@RequestHeader String Authorization
            ,@PathVariable Integer senderId){
        return imMsgService.receiveFreshMsg(Authorization, senderId);
    }

    @ApiOperation(value = "根据传入的时间段信息 和发送者ID查询历史信息")
    @GetMapping("/msg/history")
    public ResponseVO getHistoryMsg(@RequestHeader String Authorization
            , @RequestParam Date start
            , @RequestParam Date end
            , @RequestParam Integer senderId){
        return imMsgService.searchHistoryMsg(Authorization, start, end, senderId);
    }

    @ApiOperation(value = "获取和用户聊过天的人")
    @GetMapping("/chatter")
    public ResponseVO getChaterListAndLatestWords(@RequestHeader String Authorization){
        return imMsgService.getChaterListAndLatestWords(Authorization);
    }

    //删除聊天朋友



}
