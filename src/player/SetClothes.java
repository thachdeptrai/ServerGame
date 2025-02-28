package player;

/*
 *
 *
 * @author NgocThach
 */
import models.Item;

public class SetClothes {

    private Player player;

    public SetClothes(Player player) {
        this.player = player;
    }

    public byte songoku;
    public byte thienXinHang;
    public byte kirin;
    public byte kaioken;
    public byte thanVuTruKaio;

    public byte ocTieu;
    public byte pikkoroDaimao;
    public byte picolo;
    public byte lienHoan;
    public byte nail;

    public byte kakarot;
    public byte cadic;
    public byte nappa;
    public byte giamSatThuong;
    public byte cadicM;

    public byte worldcup;
    public byte setDHD;

    //set tu tien
    public byte sethanhtinh;
    public byte setvutru;
    public byte sethoangkim;
    public byte settukim;
    public byte setvongcot;
    public byte setanhdao;
    public byte sethuyetlong;
    public byte setblack;

    public boolean godClothes;
    public int ctHaiTac = -1;
    public int ctDietQuy = -1;
    public int ctBunmaTocMau = -1;
    public int ctTiecbaiBien = -1;

    public byte tienNhan;
//    public 

    public void setup() {
        setDefault();
        setupSKT();
        setupSKHNew();
        this.godClothes = true;
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id > 567 || item.template.id < 555) {
                    this.godClothes = false;
                    break;
                }
            } else {
                this.godClothes = false;
                break;
            }
        }
        Item ct = this.player.inventory.itemsBody.get(5);
        if (ct.isNotNullItem()) {
            switch (ct.template.id) {
                case 618:
                case 619:
                case 620:
                case 621:
                case 622:
                case 623:
                case 624:
                case 626:
                case 627:
                    this.ctHaiTac = ct.template.id;
                    break;
                case 1087:
                case 1088:
                case 1089:
                case 1090:
                case 1091:
                    this.ctDietQuy = ct.template.id;
                    break;
                case 1208:
                case 1209:
                case 1210:
                    this.ctBunmaTocMau = ct.template.id;
                    break;
                case 1234:
                case 1235:
                case 1236:
                    this.ctTiecbaiBien = ct.template.id;
                    break;

            }
        }

    }

    private void setupSKT() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 129:
                        case 141:
                            isActSet = true;
                            songoku++;
                            break;
                        case 127:
                        case 139:
                            isActSet = true;
                            thienXinHang++;
                            break;
                        case 128:
                        case 140:
                            isActSet = true;
                            kirin++;
                            break;
                        case 131:
                        case 143:
                            isActSet = true;
                            ocTieu++;
                            break;
                        case 132:
                        case 144:
                            isActSet = true;
                            pikkoroDaimao++;
                            break;
                        case 130:
                        case 142:
                            isActSet = true;
                            picolo++;
                            break;
                        case 135:
                        case 138:
                            isActSet = true;
                            nappa++;
                            break;
                        case 133:
                        case 136:
                            isActSet = true;
                            kakarot++;
                            break;
                        case 134:
                        case 137:
                            isActSet = true;
                            cadic++;
                            break;
                        case 250:
                        case 253:
                            isActSet = true;
                            kaioken++;
                            break;
                        case 251:
                        case 254:
                            isActSet = true;
                            lienHoan++;
                            break;
                        case 252:
                            isActSet = true;
                            giamSatThuong++;
                            break;
//                        case 255:
//                        case 256:
//                            isActSet = true;
//                            tienNhan++;
//                            break;
                        case 255:
                        case 263:
                        case 264:
                        case 265:
                            isActSet = true;
                            sethanhtinh++;
                            break;

                        case 256:
                        case 266:
                        case 267:
                        case 268:
                            isActSet = true;
                            setvutru++;
                            break;
                        case 257:
                        case 269:
                        case 270:
                        case 271:
                            isActSet = true;
                            sethoangkim++;
                            break;
                        case 258:
                        case 272:
                        case 273:
                        case 274:
                            isActSet = true;
                            settukim++;
                            break;
                        case 259:
                        case 275:
                        case 276:
                        case 277:
                            isActSet = true;
                            setvongcot++;
                            break;
                        case 260:
                        case 278:
                        case 279:
                        case 280:
                            isActSet = true;
                            setanhdao++;
                            break;
                        case 261:
                        case 281:
                        case 282:
                        case 283:
                            isActSet = true;
                            sethuyetlong++;
                            break;
                        case 262:
                        case 284, 285, 286:
                            isActSet = true;
                            setblack++;
                            break;

                        case 21:
                            if (io.param == 80) {
                                setDHD++;
                            }
                            break;
                    }

                    if (isActSet) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

    private void setupSKHNew() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (Item.ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 245:
                        case 246:
                        case 247:
                        case 248:
                            isActSet = true;
                            thanVuTruKaio++;
                            break;
                        case 237:
                        case 238:
                        case 239:
                        case 240:
                            isActSet = true;
                            nail++;
                            break;
                        case 241:
                        case 242:
                        case 243:
                        case 244:
                            isActSet = true;
                            cadicM++;
                            break;
                    }

                    if (isActSet) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

    private void setDefault() {
        this.songoku = 0;
        this.thienXinHang = 0;
        this.kirin = 0;
        this.kaioken = 0;
        this.ocTieu = 0;
        this.pikkoroDaimao = 0;
        this.picolo = 0;
        this.lienHoan = 0;
        this.kakarot = 0;
        this.cadic = 0;
        this.nappa = 0;
        this.tienNhan = 0;
        this.giamSatThuong = 0;

        this.thanVuTruKaio = 0;

        this.nail = 0;

        this.cadicM = 0;
        this.setDHD = 0;
        this.worldcup = 0;
        this.godClothes = false;
        this.ctHaiTac = -1;
        this.ctDietQuy = -1;
        this.ctBunmaTocMau = -1;
        this.ctTiecbaiBien = -1;
        // set tien 
        
        this.setblack = 0;
        this.setanhdao = 0;
        this.sethanhtinh = 0;
        this.sethoangkim = 0;
        this.sethuyetlong = 0;
        this.settukim = 0;
        this.setvongcot = 0;
        this.setvutru = 0;
        
    }

    public boolean checkSetGod() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id < 555 || item.template.id > 567) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean checkSetDes() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                if (item.template.id < 650 || item.template.id > 662) {

                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public void dispose() {
        this.player = null;
    }
}
