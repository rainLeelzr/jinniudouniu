package com.jinniu.commonjn.model.mahjong;

/**
 * 麻将游戏的所有操作类型
 */
public class OperateType {

    public enum YingRuan {
        YING("硬", 2),
        RUAN("软", 1);

        private String name;

        // 倍数
        private int multiple;

        YingRuan(String name, int multiple) {
            this.name = name;
            this.multiple = multiple;
        }
    }

    /**
     * 除了赢牌的所有操作类型，例如杠、碰
     */
    public static class Commonm {

        public enum CommonType {
            PENG("碰"),
            GANG("杠");
            private String name;

            CommonType(String name) {
                this.name = name;
            }
        }
    }

    /**
     * 赢牌的所有操作类型
     */
    public static class Win {

        public enum WinType {
            ZI_MO("自摸"),
            CHI_HU("吃胡");

            private String name;

            WinType(String name) {
                this.name = name;
            }
        }

        // 牌型组合形式
        public enum CardTypeCombination {
            PENG_PENG_HU("碰碰胡", 10),
            QI_DUI("七对", 10),
            PING_HU("平胡", 0);

            private String name;

            // 炮数
            private int shot;

            CardTypeCombination(String name, int shot) {
                this.name = name;
                this.shot = shot;
            }
        }

        // 牌型附加形式
        public enum AdditionalType {

            RAUN_QING_YI_SE("清一色", 15),
            RAUN_GANG_SHANG_HUA("杠上花", 5),
            RAUN_MEN_QIAN_QING("门前清", 1),
            RAUN_DAN_DIAO("单吊", 0),
            RAUN_KA_PAI("卡牌", 0);

            private String name;
            // 炮数
            private int shot;

            AdditionalType(String name, int shot) {
                this.name = name;
                this.shot = shot;
            }

        }
    }
}
