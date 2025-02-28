package Bot;

import java.util.Random;
import models.Template;
import NgocThachServer.Manager;

public class NewBot {

    public static NewBot i;

    public boolean LOAD_PART = true;
    public int MAXPART = 0;
    public static int[][] PARTBOT = new int[Manager.ITEM_TEMPLATES.size()][4];

    private final String[] FIRST_NAMES = {
        "anh", "an", "bao", "binh", "chinh", "chung", "chi", "du", "duy", "duyen", "giang", "giau",
        "hoai", "hung", "huy", "huong", "hai", "hieu", "khanh", "khoa", "lam", "lan", "linh", "long",
        "mai", "minh", "nam", "ngan", "ngoc", "nhan", "nhi", "phat", "phuc", "phuong", "phu", "quyen",
        "quang", "quoc", "sang", "son", "suong", "tam", "thao", "thanh", "thien", "thi", "thu", "thuy",
        "thong", "trang", "trinh", "trung", "truc", "truong", "tu", "tuan", "tung", "tuyen", "uyen",
        "van", "vi", "vinh", "vu", "xuan", "yen", "ai", "am", "at", "bang", "cao", "chau", "cuong",
        "dan", "dao", "diep", "dinh", "dien", "duc", "duong", "hieu", "hong", "huynh", "kien", "lanh",
        "le", "luan", "ly", "minh", "nghi", "nguyen", "nguyen", "phu", "phuong", "quynh", "son", "thai",
        "thuong", "tinh", "toan", "tra", "trieu", "trong", "trung", "truong", "vinh", "vy", "xuan"
    };

    private final String[] LAST_NAMES = {
        "nguyen", "tran", "le", "pham", "hoang", "vo", "huynh", "phan", "dang", "bui", "do", "dao", "vu", "dinh",
        "truong", "trinh", "ngoc", "ly", "luong", "chau", "dang", "cao", "mai", "truong", "duong", "lam", "khong",
        "loc", "vuong", "ho", "bach", "tam", "dong", "anh", "quach", "kieu", "kim", "la", "lai", "ly", "mac",
        "my", "ngo", "oanh", "pho", "quyen", "san", "tan", "tai", "tien", "ton", "tong", "truc", "tuyen", "ung",
        "vinh", "xa", "yen", "xuan", "ai", "at", "bao", "bach", "bach", "cao", "chau", "chi", "cuong", "dan",
        "dao", "duc", "dien", "dinh", "hoai", "hai", "hieu", "hong", "huy", "huong", "huynh", "khanh", "khoa",
        "kien", "lam", "lan", "long", "luong", "manh", "mai", "minh", "nam", "ngan", "nhan", "nhat", "nguyen",
        "nguyen", "phat", "phuoc", "phuc", "phuong", "quang", "quoc", "quyen", "sang", "tam", "thang", "thao",
        "thanh", "thien", "thi", "thu", "thuy", "trang", "trinh", "trong", "trung", "truc", "tuyen", "uyen",
        "vinh", "vu", "xuan", "yen"
    };

    public static NewBot gI() {
        if (i == null) {
            i = new NewBot();
        }
        return i;
    }

    public void LoadPart() {
        if (LOAD_PART) {
            int i = 0;
            for (Template.ItemTemplate it : Manager.ITEM_TEMPLATES) {
                if (it.type == 5) {
                    if (it.head != -1 && it.leg != -1 && it.body != -1 && it.leg != 194) {
                        PARTBOT[i][0] = it.head;
                        PARTBOT[i][1] = it.leg;
                        PARTBOT[i][2] = it.body;
                        PARTBOT[i][3] = it.gender;
                        i++;
                        MAXPART++;
                    }
                }
            }
            LOAD_PART = false;
        }
    }

    public String Getname() {
        return FIRST_NAMES[new Random().nextInt(FIRST_NAMES.length)] + LAST_NAMES[new Random().nextInt(LAST_NAMES.length)];
    }

    public int getIndex(int gender) {
        int Random = new Random().nextInt(MAXPART);
        int gend = PARTBOT[Random][3];
        //   if(gend == gender || gend == 3){
        if (gend == gender) {
            return Random;
        } else {
            return getIndex(gender);
        }
    }

    public void runBot(int type, ShopBot shop, int slot) {
        LoadPart();
        for (int i = 0; i < slot; i++) {
            int Gender = new Random().nextInt(3);
            int Random1 = getIndex(Gender);
            int head = PARTBOT[Random1][0];
            int leg = PARTBOT[Random1][1];
            int body = PARTBOT[Random1][2];
            if (shop != null) {
                shop = new ShopBot(shop);
            }
            int flag = Manager.gI().FLAGS_BAGS.get(new Random().nextInt(Manager.gI().FLAGS_BAGS.size())).id;
            Bot b = new Bot((short) head, (short) body, (short) leg, type, Getname(), shop, (short) flag);
            Sanb bos = new Sanb(b);
            Mobb mo1 = new Mobb(b);
            b.mo1 = mo1;
            b.boss = bos;
            int congThem = new Random().nextInt(50000000);
            b.nPoint.limitPower = 8;
            b.nPoint.power = 1000 + congThem;
            b.nPoint.tiemNang = 20000000 + congThem;
            b.nPoint.dameg = 10000000;
            b.nPoint.mpg = 2000000000;
            b.nPoint.mpMax = 2000000000;
            b.nPoint.mp = 2000000000;
            b.nPoint.hpg = 10000;
            b.nPoint.hpMax = 10000;
            b.nPoint.hp = 10000000;
            b.nPoint.maxStamina = 20000;
            b.nPoint.stamina = 20000;
            b.nPoint.critg = 10;
            b.nPoint.defg = 10;
            b.gender = (byte) Gender;
            b.leakSkill();
            b.joinMap();
            if (shop != null) {
                shop.bot = b;
            }
            if (b != null) {
                BotManager.gI().bot.add(b);
            }
        }
    }
}
