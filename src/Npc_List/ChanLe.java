/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Npc_List;/*telegram @tomihjhj_bot*/

import consts.ConstNpc;/*telegram @tomihjhj_bot*/
import FunC.Input;/*telegram @tomihjhj_bot*/
import Manager.ChanLeManager;
import npc.Npc;
import player.Player;
import services.ChanLeServices;
import services.Service;
import utils.Util;/*telegram @tomihjhj_bot*/

/**
 *
 * @author kagam
 */
public class ChanLe extends Npc {

    public ChanLe(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);/*telegram @tomihjhj_bot*/
    }

    @Override
    public void openBaseMenu(Player player) {
        ChanLeManager chanLeManager = new ChanLeManager();
        String secon = chanLeManager.time + "";/*telegram @tomihjhj_bot*/
        String secon2 = chanLeManager.timeToStart + "";/*telegram @tomihjhj_bot*/

        String npcSay1 = "Chẵn lẻ đê.\n "
                + "Bạn đã thắng được " + Util.numberToMoney((int) (player.rubyWin * 1.5)) + " thỏi vàng\n"
                + "|5|Chú ý : Chỉ cược 1 lần tối đa 10k thỏi vàng, mọi sự mất mát ad không giải quyết!\n"
                + "<" + secon2 + ">giây Nữa Bắt đầu phiên mới\n";/*telegram @tomihjhj_bot*/
        String[] menu1 = {"Chẵn", "Lẻ", "Xem \nlịch sử\nbản thân", "Soi Cầu", "Nhận\nphần thưởng"};/*telegram @tomihjhj_bot*/
        String npcSay2
                = "Chẵn lẻ đê.\n Bạn chưa thắng cược lần nào\n"
                + "|5|Chú ý : Chỉ cược 1 lần tối đa 10k thỏi vàng, mọi sự mất mát ad không giải quyết!\n"
                + "<" + secon + ">giây\n";/*telegram @tomihjhj_bot*/
        String[] menu2 = {"Chẵn", "Lẻ", "Xem \nlịch sử\nbản thân", "Soi Cầu"};/*telegram @tomihjhj_bot*/
        if (player.rubyWin > 0) {
            createOtherMenu(player, ConstNpc.BASE_MENU, npcSay1, menu1);/*telegram @tomihjhj_bot*/
        } else {
            createOtherMenu(player, ConstNpc.BASE_MENU, npcSay2, menu2);/*telegram @tomihjhj_bot*/
        }

    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 5) {

                if (!player.getSession().actived) {
                    Service.gI().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sử dụng chức năng này");/*telegram @tomihjhj_bot*/

                } else if (player.iDMark.isBaseMenu()) {
                    switch (select) {

                        case 0: // Chẵn
                            if (!ChanLeServices.gI().checkHavePariry()) {
                                Service.gI().sendThongBaoOK(player, "Chưa có phiên chẵn lẻ nào khởi động");/*telegram @tomihjhj_bot*/
                                break;/*telegram @tomihjhj_bot*/
                            } else if (ChanLeServices.gI().hasPlacedBet(player)) {
                                Service.gI().sendThongBaoOK(player, "Bạn đã đặt cược trước đó, không thể đặt lại!");/*telegram @tomihjhj_bot*/
                                break;/*telegram @tomihjhj_bot*/
                            } else {
                                Input.gI().CHAN(player);/*telegram @tomihjhj_bot*/
                            }
                            break;/*telegram @tomihjhj_bot*/
                        case 1:
                            if (!ChanLeServices.gI().checkHavePariry()) {
                                Service.gI().sendThongBaoOK(player, "Chưa có phiên chẵn lẻ nào khởi động");/*telegram @tomihjhj_bot*/
                                break;/*telegram @tomihjhj_bot*/
                            } else if (ChanLeServices.gI().hasPlacedBet(player)) {
                                Service.gI().sendThongBaoOK(player, "Bạn đã đặt cược trước đó, không thể đặt lại!");/*telegram @tomihjhj_bot*/
                                break;/*telegram @tomihjhj_bot*/
                            } else {
                                Input.gI().LE(player);/*telegram @tomihjhj_bot*/
                            }
                            break;/*telegram @tomihjhj_bot*/
                        case 2:
                            Service.gI().sendThongBaoOK(player,
                                    ChanLeServices.gI().getHistoryPlayer(player));/*telegram @tomihjhj_bot*/
                            break;/*telegram @tomihjhj_bot*/
                        case 3:
                            Service.gI().sendThongBaoOK(player,
                                    ChanLeServices.gI().getHistory());/*telegram @tomihjhj_bot*/
                            break;/*telegram @tomihjhj_bot*/
                        case 4:
                            if (player.rubyWin <= 0) {
                                Service.gI().sendThongBaoOK(player, "Có cái nịt mà nhận");/*telegram @tomihjhj_bot*/
                                break;/*telegram @tomihjhj_bot*/
                            }
                            ChanLeServices.gI().rewardRuby(player);/*telegram @tomihjhj_bot*/
                            break;/*telegram @tomihjhj_bot*/

                    }
                }
            }
        }
    }
};/*telegram @tomihjhj_bot*/
