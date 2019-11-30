package com.example.demo.entity.enums;



public class AppEnum {



    public enum OrderMap {
        /**
         * 默认 id 排序
         */
        id("id", "id"),
        name("name", "name"),
        lexile("lexile", "lexile"),
        code("code", "code"),
        stockAvailable("stock_available","stockAvailable"),
        stockTotal("stock_total","stockTotal"),
        updateTime("update_time", "updateTime"),
        releaseTime("release_time", "releaseTime"),
        /**
         * 热度排序(绘本完成阅读人数)
         */
        finishAmount("finish_amount", "finishAmount"),
        /**
         * 综合排序(完成人数/开始人数 x100% +log完成人数)
         */
        complexScore("complex_score", "complexScore")
        ;

        private String column;
        private String model;

        OrderMap(String column, String model) {
            this.column = column;
            this.model = model;
        }

        public static String getModelNameByColumn(String name) {
            OrderMap[] values = OrderMap.values();
            for (int i = 0; i < values.length; i++) {
                if (values[i].getModel().equalsIgnoreCase(name)) {
                    return values[i].getColumn();
                }
            }
            return null;
        }

        public String getColumn() {
            return column;
        }

        public String getModel() {
            return model;
        }
    }


}
