package com.xebia.devradar;

import java.util.List;

public class HudsonUserDetailDTO {

    public List<HudsonUserPropertyDTO> property;

    public String getUserMail() {
        if (property != null && property.size() > 0) {
            return property.get(0).address;
        }
        return null;
    }

    static private class HudsonUserPropertyDTO {
        public String address;
    }
}
